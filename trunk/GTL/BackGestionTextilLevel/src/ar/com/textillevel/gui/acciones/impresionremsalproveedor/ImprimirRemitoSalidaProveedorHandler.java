package ar.com.textillevel.gui.acciones.impresionremsalproveedor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import main.GTLGlobalCache;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.util.DateUtil;
import ar.clarin.fwjava.util.StringUtil;
import ar.com.textillevel.entidades.documentos.remito.PiezaRemito;
import ar.com.textillevel.entidades.documentos.remito.RemitoSalida;
import ar.com.textillevel.entidades.documentos.remito.proveedor.ItemRemitoSalidaProveedor;
import ar.com.textillevel.entidades.ventas.productos.Producto;
import ar.com.textillevel.gui.acciones.impresionremito.ImprimirRemitoHandler.PiezaRemitoTO;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.JasperHelper;
import ar.com.textillevel.modulos.odt.entidades.PiezaODT;

@SuppressWarnings({"unchecked","rawtypes"})
public class ImprimirRemitoSalidaProveedorHandler {

	private RemitoSalida remito;

	private final JDialog owner;
	private static final String ARCHIVO_JASPER = "/ar/com/textillevel/reportes/remito_salida_proveedor.jasper";
	private static final String ARCHIVO_JASPER_B = "/ar/com/textillevel/reportes/remito_entrada_b.jasper";
	
	public ImprimirRemitoSalidaProveedorHandler(RemitoSalida remito, JDialog owner) {
		this.remito = remito;
		this.owner = owner;
	}

	public void imprimir() {
		boolean ok = false;
		do {
			String input = JOptionPane.showInputDialog(owner, "Ingrese la cantidad de copias: ", "Imprimir", JOptionPane.INFORMATION_MESSAGE);
			if(input == null){
				break;
			}
			if (input.trim().length()==0 || !GenericUtils.esNumerico(input)) {
				CLJOptionPane.showErrorMessage(owner, "Ingreso incorrecto", "error");
			} else {
				ok = true;
				JasperReport reporte = null;
				try {
					JasperPrint jasperPrint = null;
					if(!GenericUtils.isSistemaTest()){
						reporte = JasperHelper.loadReporte(ARCHIVO_JASPER);
						RemitoEntradaTO remitoEntrada = new RemitoEntradaTO(getRemito());
						jasperPrint = JasperHelper.fillReport(reporte, remitoEntrada.getParameters(), Collections.singletonList(remitoEntrada));
					}else{
						reporte = JasperHelper.loadReporte(ARCHIVO_JASPER_B);
						RemitoEntradaBTO remitoEntrada = new RemitoEntradaBTO(getRemito(), 1);
						jasperPrint = JasperHelper.fillReport(reporte, remitoEntrada.getParameters(), Collections.singletonList(remitoEntrada));
					}
					
					Integer cantidadAImprimir = Integer.valueOf(input);
					JasperHelper.imprimirReporte(jasperPrint, true, true, cantidadAImprimir);
				} catch (JRException e) {
					e.printStackTrace();
				}
			}
		} while (!ok);
		
	}

	private RemitoSalida getRemito() {
		return remito;
	}

	public static class RemitoEntradaTO {

		private Map parameters;
		private List<ItemRemitoSalidaProveedorTO> items;

		public RemitoEntradaTO(RemitoSalida remito) {
			cargarMap(remito);
		}

		private void cargarMap(RemitoSalida remito) {
			this.parameters  = new HashMap();
			parameters.put("FECHA_REMITO", DateUtil.dateToString(DateUtil.getManiana(remito.getFechaEmision())));
			parameters.put("RAZON_SOCIAL", remito.getProveedor().getRazonSocial());
			parameters.put("DOMICILIO", remito.getProveedor().getDireccionFiscal().getDireccion());
			parameters.put("LOCALIDAD", remito.getProveedor().getDireccionFiscal().getLocalidad().getNombreLocalidad());
			parameters.put("SUBREPORT_DIR", "ar/com/textillevel/reportes/");
			parameters.put("USUARIO", GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName());
			
			parameters.put("ARTICULO", "-");

			parameters.put("POSICION_IVA", remito.getProveedor().getPosicionIva() == null ? "" : remito.getProveedor().getPosicionIva().getDescripcion());
			parameters.put("CUIT", remito.getProveedor().getCuit());
			parameters.put("REMITO_ENT", String.valueOf(remito.getId()));

			int totalDePiezas = getTotalPiezas(remito.getItems());
			parameters.put("TOT_PIEZAS", fixPrecioCero(GenericUtils.getDecimalFormat().format(totalDePiezas)) + " " + GenericUtils.convertirNumeroATexto(Double.valueOf(totalDePiezas)));
			parameters.put("TOT_KILOS", "-");
			parameters.put("TOT_METROS", "-");
			populateItemsRemito(remito.getItems());
		}

		private int getTotalPiezas(List<ItemRemitoSalidaProveedor> items2) {
			int totalPiezas = 0;
			for(ItemRemitoSalidaProveedor i : items2) {
				totalPiezas += i.getCantSalida().intValue();
			}
			return totalPiezas;
		}

		private String fixPrecioCero(String format) {
			if(!StringUtil.isNullOrEmpty(format) && (format.equals(",00") || format.equals(".00"))) {
				return "0,00";
			}
			return format;
		}

		private void populateItemsRemito(List<ItemRemitoSalidaProveedor> items) {
			this.items = new ArrayList<ItemRemitoSalidaProveedorTO>();
			for(ItemRemitoSalidaProveedor irsp : items) {
				this.items.add(toItemRemitoTO(irsp));
			}
		}

		private ItemRemitoSalidaProveedorTO toItemRemitoTO(ItemRemitoSalidaProveedor itemRemitoSalidaProveedor) {
			ItemRemitoSalidaProveedorTO irspto = new ItemRemitoSalidaProveedorTO();
			irspto.setCantSalida(itemRemitoSalidaProveedor.getCantSalida().toString());
			irspto.setDescrItem(itemRemitoSalidaProveedor.toString());
			return irspto;
		}

		public Map getParameters() {
			return parameters;
		}

		public JRDataSource getItemsDS1() {
			return new JRBeanCollectionDataSource(items);
		}

	}

	public static class ItemRemitoSalidaProveedorTO {

		private String cantSalida;
		private String descrItem;

		public String getCantSalida() {
			return cantSalida;
		}

		public void setCantSalida(String cantSalida) {
			this.cantSalida = cantSalida;
		}

		public String getDescrItem() {
			return descrItem;
		}

		public void setDescrItem(String descrItem) {
			this.descrItem = descrItem;
		}

	}
	
	public static class RemitoEntradaBTO{
		private Map parameters;
		private final List<PiezaRemitoTO> piezas1;
		private final List<PiezaRemitoTO> piezas2;
		private final List<PiezaRemitoTO> piezas3;
		private static final int CANT_PIEZAS_SPLIT = 16;
		private static final int CANT_MAX_PIEZAS = 48;
		
		//TODO: NUEVO REPORTE CON UN SOLO LISTADO
		public RemitoEntradaBTO(RemitoSalida remito, Integer nroSucursal) {
			cargarMap(remito, nroSucursal);
			this.piezas1 = new ArrayList<PiezaRemitoTO>();
			this.piezas2 = new ArrayList<PiezaRemitoTO>();
			this.piezas3 = new ArrayList<PiezaRemitoTO>();
			populateListaPiezas(reescribirOrdenPiezas(remito.getPiezas()));
		}
		
		private void populateListaPiezas(List<PiezaRemito> piezas) {
			populateSingleListaPiezas(piezas1, 0, CANT_PIEZAS_SPLIT, piezas);
			populateSingleListaPiezas(piezas2, CANT_PIEZAS_SPLIT, 2*CANT_PIEZAS_SPLIT, piezas);
			populateSingleListaPiezas(piezas3, 2*CANT_PIEZAS_SPLIT, CANT_MAX_PIEZAS, piezas);
		}

		private void populateSingleListaPiezas(List<PiezaRemitoTO> piezasList, int from, int until, List<PiezaRemito> allPiezas) {
			for(int i = from; i < until; i++) {
				if(i < allPiezas.size()) {
					PiezaRemito piezaRemito = allPiezas.get(i);
					piezasList.add(toPiezaRemitoTO(piezaRemito, piezaRemito.getTotalMetros().toString()));
				} 
			}
		}

		private PiezaRemitoTO toPiezaRemitoTO(PiezaRemito piezaRemito, String totalString) {
			PiezaRemitoTO prto = new PiezaRemitoTO();
			prto.setMetros(totalString);
			prto.setNumero(piezaRemito.getOrdenPiezaCalculado() == null ? "" : piezaRemito.getOrdenPiezaCalculado());
			prto.setObservaciones(piezaRemito.getObservaciones() == null ? "" : piezaRemito.getObservaciones().trim());	
			return prto;
		}
		
		private List<PiezaRemito> reescribirOrdenPiezas(List<PiezaRemito> piezas) {
			int ordenPiezaActual = 1;
			int ordenSubPiezaActual = 1;
			PiezaRemito prAnt = null;
			PiezaRemito pr = null;
			for(int i = 0; i < piezas.size(); i++) {
				pr = piezas.get(i);
				ordenPiezaActual = pr.getOrdenPieza() + 1;
				if((i - 1) > 0) {
					prAnt = piezas.get(i - 1);
				}
				if(prAnt == null) {
					pr.setOrdenPiezaCalculado(StringUtil.getCadena(extractOrdenes(pr.getPiezasPadreODT()), ","));
				} else {
					if(prAnt.getPiezaEntrada().getId().equals(pr.getPiezaEntrada().getId())) {
						pr.setOrdenPiezaCalculado(String.valueOf(ordenPiezaActual) + "-" + ordenSubPiezaActual);
						ordenSubPiezaActual ++;
					} else {
						pr.setOrdenPiezaCalculado(String.valueOf(ordenPiezaActual));
						ordenSubPiezaActual = 1;
					}
				}
				
			}
			return piezas;
		}

		private Integer[] extractOrdenes(List<PiezaODT> piezasPadreODT) {
			Integer[] ordenes = new Integer[piezasPadreODT.size()];
			int i = 0;
			for(PiezaODT podt : piezasPadreODT) {
				ordenes[i] = podt.getPiezaRemito().getOrdenPieza();
				i++;
			}
			return ordenes;
		}

		private void cargarMap(RemitoSalida remito, Integer nroSucursal) {
			this.parameters  = new HashMap();
			parameters.put("FECHA_REMITO", DateUtil.dateToString(remito.getFechaEmision()));
			parameters.put("NRO_REMITO", String.valueOf(remito.getNroRemito()));
			parameters.put("RAZON_SOCIAL", remito.getProveedor().getRazonSocial());
			parameters.put("DOMICILIO", remito.getProveedor().getDireccionFiscal().getDireccion());
			parameters.put("LOCALIDAD", remito.getProveedor().getDireccionFiscal().getLocalidad().getNombreLocalidad());
			parameters.put("SUBREPORT_DIR", "ar/com/textillevel/reportes/");
//			parameters.put("ARTICULO", extractArticulo(remito));
			parameters.put("POSICION_IVA", remito.getProveedor().getPosicionIva() == null ? "" : remito.getProveedor().getPosicionIva().getDescripcion());
			parameters.put("CUIT", remito.getProveedor().getCuit());
			parameters.put("PROCESO", getProceso(remito));
			parameters.put("REMITO_ENT", String.valueOf(remito.getId()));
			//parameters.put("ODT", extractODTs(remito.getOdts()));
			parameters.put("TOT_PIEZAS", "TOTAL PIEZAS: " + GenericUtils.fixPrecioCero(GenericUtils.getDecimalFormat().format(remito.getPiezas().size())) + " (" + GenericUtils.convertirNumeroATexto(Double.valueOf(remito.getPiezas().size()))+")");
			parameters.put("TOT_KILOS", "TOTAL KILOS: " + GenericUtils.fixPrecioCero(GenericUtils.getDecimalFormat().format(remito.getPesoTotal().doubleValue())));
			parameters.put("TOT_METROS", "TOTAL METROS: " + GenericUtils.fixPrecioCero(GenericUtils.getDecimalFormat().format(remito.getTotalMetros())));
		}
		
		public Map getParameters() {
			return parameters;
		}
		
		private String getProceso(RemitoSalida remito) {
			Set<String> procesos = new HashSet<String>();
			for(Producto p : remito.getProductoList()) {
				procesos.add(p.getDescripcion());
			}
			return StringUtil.getCadena(procesos, ", ");
		}

		public JRDataSource getPiezasDS1() {
			return new JRBeanCollectionDataSource(piezas1);
		}
		
		public JRDataSource getPiezasDS2() {
			return new JRBeanCollectionDataSource(piezas2);
		}

		public JRDataSource getPiezasDS3() {
			return new JRBeanCollectionDataSource(piezas3);
		}

		
//		private String getProceso(RemitoSalida remito) {
//			Set<String> procesos = new HashSet<String>();
//			for(Producto p : remito.getProductoList()) {
//				procesos.add(p.getDescripcion());
//			}
//			return StringUtil.getCadena(procesos, ", ");
//		}
//
//		private String extractArticulo(RemitoSalida remito) {
//			Set<String> articulos = new HashSet<String>();
// 			List<Producto> productoList = remito.getProductoList();
//			for(Producto p : productoList) {
//				if(p.getArticulo() != null) {
//					articulos.add(p.getArticulo().getNombre());
//				}
//			}
//			return StringUtil.getCadena(articulos, ", ");
//		}
//
//		private String extractODTs(List<OrdenDeTrabajo> odts) {
//			List<String> odtList = new ArrayList<String>();
//			for(OrdenDeTrabajo odt : odts) {
//				odtList.add(ODTCodigoHelper.getInstance().formatCodigo(odt.getCodigo()));
//			}
//			return StringUtil.getCadena(odtList, ", ");
//		}

	}


}