package ar.com.textillevel.gui.modulos.abm.listaprecios;

import java.awt.Frame;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

import org.apache.taglibs.string.util.StringW;

import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.error.validaciones.ValidacionException;
import ar.clarin.fwjava.util.DateUtil;
import ar.com.textillevel.entidades.config.ParametrosGenerales;
import ar.com.textillevel.entidades.enums.ETipoProducto;
import ar.com.textillevel.entidades.enums.EUnidad;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.ventas.cotizacion.Cotizacion;
import ar.com.textillevel.entidades.ventas.cotizacion.DefinicionPrecio;
import ar.com.textillevel.entidades.ventas.cotizacion.GrupoTipoArticuloBaseEstampado;
import ar.com.textillevel.entidades.ventas.cotizacion.GrupoTipoArticuloGama;
import ar.com.textillevel.entidades.ventas.cotizacion.PrecioBaseEstampado;
import ar.com.textillevel.entidades.ventas.cotizacion.PrecioGama;
import ar.com.textillevel.entidades.ventas.cotizacion.PrecioTipoArticulo;
import ar.com.textillevel.entidades.ventas.cotizacion.RangoAncho;
import ar.com.textillevel.entidades.ventas.cotizacion.RangoAnchoArticuloEstampado;
import ar.com.textillevel.entidades.ventas.cotizacion.RangoAnchoArticuloTenido;
import ar.com.textillevel.entidades.ventas.cotizacion.RangoAnchoComun;
import ar.com.textillevel.entidades.ventas.cotizacion.RangoCantidadColores;
import ar.com.textillevel.entidades.ventas.cotizacion.RangoCoberturaEstampado;
import ar.com.textillevel.entidades.ventas.cotizacion.VersionListaDePrecios;
import ar.com.textillevel.facade.api.remote.ListaDePreciosFacadeRemote;
import ar.com.textillevel.facade.api.remote.ParametrosGeneralesFacadeRemote;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.JasperHelper;
import ar.com.textillevel.util.GTLBeanFactory;

public class ImprimirListaDePreciosHandler {

	private static final String ARCHIVO_JASPER_COTIZACION = "/ar/com/textillevel/reportes/cotizacion.jasper";
	
	private List<DefinicionPrecio> definiciones;
	private VersionListaDePrecios versionListaDePrecios;
	private Cliente cliente;
	private ParametrosGenerales parametros;
	private ListaDePreciosFacadeRemote listaDePreciosFacade;
	
	private Frame padre;
	
	private ParametrosGeneralesFacadeRemote parametrosGeneralesFacade;
	
	public ImprimirListaDePreciosHandler(Frame padre, Cliente cliente, VersionListaDePrecios versionListaDePrecios) {
		this.padre = padre;
		this.versionListaDePrecios = versionListaDePrecios;
		this.definiciones = new ArrayList<DefinicionPrecio>(versionListaDePrecios.getPrecios());
		for(Iterator<DefinicionPrecio> it = this.definiciones.iterator(); it.hasNext(); ){
			DefinicionPrecio dp = it.next();
			if(dp.getTipoProducto() == ETipoProducto.REPROCESO_SIN_CARGO) {
				it.remove();
			}
		}
		this.cliente = cliente;
		this.parametros = getParametrosGeneralesFacade().getParametrosGenerales();
	}

	public void imprimir() {
		JDialogSeleccionarDefinicionesAImprimir d = new JDialogSeleccionarDefinicionesAImprimir(padre, definiciones);
		d.setVisible(true);
		if (d.isAcepto()) {
			this.definiciones = d.getDefinicionesAImprimir();
			boolean okValidez = false;
			String inputValidez = null;
			do {
				if(!okValidez) {
					Object input = JOptionPane.showInputDialog(padre, "Ingrese la validez de la cotización (días): ", "Ingrese la validez de la cotización", JOptionPane.INFORMATION_MESSAGE, null, null, String.valueOf(parametros.getValidezCotizaciones()));
					if(input == null){
						break;
					}
					inputValidez = input.toString();
					if(inputValidez.trim().length()==0 || !GenericUtils.esNumerico(inputValidez)) {
						CLJOptionPane.showErrorMessage(padre, "Ingreso incorrecto", "error");
					} else {
						okValidez = true;
						break;
					}
				}
			} while (!okValidez);
			
			if(!okValidez) {
				return;
			}

			Cotizacion cotizacion = generarYGrabarCotizacion(Integer.valueOf(inputValidez));

			JasperReport reporte = JasperHelper.loadReporte(ARCHIVO_JASPER_COTIZACION);
			try {
				JasperPrint jasperPrint = JasperHelper.fillReport(reporte, getParameters(inputValidez, cotizacion.getNumero()), createDefiniciones());
				JasperHelper.imprimirReporte(jasperPrint, true, true, 1);
			} catch (JRException e) {
				e.printStackTrace();
			}
			if(cotizacion != null) {
				CLJOptionPane.showInformationMessage(padre, "Se generó la cotización número '" + cotizacion.getNumero() + "'.", "Información");
			}
		} else {
			return;
		}
	}

	private Cotizacion generarYGrabarCotizacion(Integer validez) {
		try {
			Cotizacion cotizacion = getListaDePreciosFacade().generarCotizacion(cliente, versionListaDePrecios, validez);
			return cotizacion;
		} catch (ValidacionException e) {
			CLJOptionPane.showErrorMessage(padre, StringW.wordWrap("La cotización no se pudo generar. Probablemente se hicieron dos en el mismo momento. Por favor, reintente la operación."), "Error");
			return null;
		}
	}

	private List<DefinicionPrecioTO> createDefiniciones() {
		List<DefinicionPrecioTO> definicionesTO = new ArrayList<ImprimirListaDePreciosHandler.DefinicionPrecioTO>();
		for(int i = 0; i < this.definiciones.size(); i++) {
			DefinicionPrecio dp = this.definiciones.get(i);
			for (RangoAncho ra : dp.getRangos()) {
				if (ra instanceof RangoAnchoArticuloTenido) {
					RangoAnchoArticuloTenido raat = (RangoAnchoArticuloTenido) ra;
					for (GrupoTipoArticuloGama gtag : raat.getGruposGama()) {
						for (PrecioGama pg : gtag.getPrecios()) {
							DefinicionPrecioTO dTO = new DefinicionPrecioTO();
							dTO.setTipoProducto(dp.getTipoProducto().getDescripcion().toUpperCase());
							dTO.setAncho(ra.toStringConUnidad(EUnidad.METROS));
							dTO.setTipoArticulo(gtag.getTipoArticulo().getNombre().toUpperCase());
							dTO.setDescripcion(pg.getGamaCliente().getNombre());
							dTO.setPrecio("$ " + GenericUtils.getDecimalFormat().format(pg.getPrecio()) + " * x " + dp.getTipoProducto().getUnidad().getDescripcion().toLowerCase());
							definicionesTO.add(dTO);
						}
					}
				} else if (ra instanceof RangoAnchoArticuloEstampado) {
					RangoAnchoArticuloEstampado raae = (RangoAnchoArticuloEstampado) ra;
					for (GrupoTipoArticuloBaseEstampado gtabe : raae.getGruposBase()) {
						for (PrecioBaseEstampado pbe : gtabe.getPrecios()) {
							for (RangoCantidadColores rcc : pbe.getRangosDeColores()) {
								for (RangoCoberturaEstampado rce : rcc.getRangos()) {
									DefinicionPrecioTO dTO = new DefinicionPrecioTO();
									dTO.setTipoProducto(dp.getTipoProducto().getDescripcion().toUpperCase());
									dTO.setAncho(ra.toStringConUnidad(EUnidad.METROS));
									dTO.setTipoArticulo(gtabe.getTipoArticulo().getNombre().toUpperCase());
									dTO.setPrecio("$ " + GenericUtils.getDecimalFormat().format(rce.getPrecio()) + " * x " + dp.getTipoProducto().getUnidad().getDescripcion().toLowerCase());
									dTO.setDescripcion("Base " + pbe.getGama().getNombre().toUpperCase() + ".\n" + rcc.toString() + " colores.\n" + rce.toString() + " de cobertura.");
									definicionesTO.add(dTO);
								}
							}
						}
					}
				} else {
					RangoAnchoComun rac = (RangoAnchoComun) ra;
					for (PrecioTipoArticulo pta : rac.getPrecios()) {
						DefinicionPrecioTO dTO = new DefinicionPrecioTO();
						dTO.setTipoProducto(dp.getTipoProducto().getDescripcion().toUpperCase());
						dTO.setAncho(ra.toStringConUnidad(EUnidad.METROS));
						dTO.setDescripcion("-");
						dTO.setTipoArticulo(pta.getTipoArticulo().getNombre().toUpperCase());
						dTO.setPrecio("$ " + GenericUtils.getDecimalFormat().format(pta.getPrecio()) + " * x " + dp.getTipoProducto().getUnidad().getDescripcion().toLowerCase());
						definicionesTO.add(dTO);
					}
				} 
			}
//			if ( (i+1) < this.definiciones.size() ) {
//				DefinicionPrecioTO separador = new DefinicionPrecioTO();
//				definicionesTO.add(separador);
//			}
		}
		return definicionesTO;
	}
	
	private Map<String, Object> getParameters(String validez, Integer nroCotizacion) {
		Map<String, Object> mapa = new HashMap<String, Object>();
		mapa.put("FECHA", DateUtil.dateToString(DateUtil.getHoy(), DateUtil.SHORT_DATE));
		mapa.put("CORREO", cliente.getEmail());
		mapa.put("CLIENTE", cliente.getRazonSocial());
		mapa.put("CONTACTO", cliente.getContacto());
		String condicion = cliente.getCondicionVenta().getNombre().toLowerCase().contains("dias") ? cliente.getCondicionVenta().getNombre() : cliente.getCondicionVenta().getNombre() + " DIAS";
		mapa.put("COND_PAGO", condicion + " a partir de fecha de factura (no se aceptan promedios)."); // a riBer le gusta esto
		mapa.put("VALIDEZ", validez + " días");
		mapa.put("SEGURO", parametros.getPorcentajeSeguro() + "%.");
		mapa.put("TUBOS", "$ " + parametros.getPrecioPorTubo() + "* c/u.");
		mapa.put("CARGA_MINIMA_COLOR", parametros.getCargaMinimaColor() + " Kg. por color.");
		mapa.put("CARGA_MINIMA_ESTAMPADO", parametros.getCargaMinimaEstampado() + " Mts. por variante.");
		mapa.put("NRO_COTIZACION", ""+nroCotizacion);
		return mapa;
	}

	public ParametrosGeneralesFacadeRemote getParametrosGeneralesFacade() {
		if (parametrosGeneralesFacade == null) {
			parametrosGeneralesFacade = GTLBeanFactory.getInstance().getBean2(ParametrosGeneralesFacadeRemote.class);
		}
		return parametrosGeneralesFacade;
	}

	private ListaDePreciosFacadeRemote getListaDePreciosFacade() {
		if(listaDePreciosFacade == null) {
			listaDePreciosFacade = GTLBeanFactory.getInstance().getBean2(ListaDePreciosFacadeRemote.class);
		}
		return listaDePreciosFacade;
	}
	
	public static class DefinicionPrecioTO implements Serializable {

		private static final long serialVersionUID = -235946966463225784L;

		private String tipoProducto;
		private String tipoArticulo;
		private String ancho;
		private String descripcion;
		private String precio;

		public String getTipoProducto() {
			return tipoProducto;
		}

		public void setTipoProducto(String tipoProducto) {
			this.tipoProducto = tipoProducto;
		}

		public String getTipoArticulo() {
			return tipoArticulo;
		}

		public void setTipoArticulo(String tipoArticulo) {
			this.tipoArticulo = tipoArticulo;
		}

		public String getAncho() {
			return ancho;
		}

		public void setAncho(String ancho) {
			this.ancho = ancho;
		}

		public String getDescripcion() {
			return descripcion;
		}

		public void setDescripcion(String descripcion) {
			this.descripcion = descripcion;
		}

		public String getPrecio() {
			return precio;
		}

		public void setPrecio(String precio) {
			this.precio = precio;
		}
	}

}