package ar.com.textillevel.gui.acciones.impresionremito;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.taglibs.string.util.StringW;

import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.util.DateUtil;
import ar.clarin.fwjava.util.GuiUtil;
import ar.clarin.fwjava.util.StringUtil;
import ar.com.textillevel.entidades.documentos.remito.PiezaRemito;
import ar.com.textillevel.entidades.documentos.remito.RemitoSalida;
import ar.com.textillevel.entidades.ventas.productos.Producto;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.JasperHelper;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;
import ar.com.textillevel.modulos.odt.entidades.PiezaODT;
import ar.com.textillevel.util.ODTCodigoHelper;

@SuppressWarnings({"unchecked","rawtypes"})
public class ImprimirRemitoHandler {

	private final RemitoSalida remito;
	private List<RemitoSalida> remitos;

	private final JDialog owner;
	private final Integer nroSucursal;
	private static final String ARCHIVO_JASPER = "/ar/com/textillevel/reportes/remito_entrada.jasper";
	private static final String ARCHIVO_JASPER_B = "/ar/com/textillevel/reportes/remito_entrada_b.jasper";

	public ImprimirRemitoHandler(RemitoSalida remito, Integer nroSucursal, JDialog owner) {
		this.remito = remito;
		this.nroSucursal = nroSucursal;
		this.owner = owner;
	}

	public ImprimirRemitoHandler(List<RemitoSalida> remitos, Integer nroSucursal, JDialog owner) {
		this.remito = null;
		this.remitos = remitos;
		this.nroSucursal = nroSucursal;
		this.owner = owner;
	}

	public void imprimir() {
		boolean ok = false;
		do {
			String cantImprimirStr = JOptionPane.showInputDialog(owner, "Ingrese la cantidad de copias: ", "Imprimir", JOptionPane.INFORMATION_MESSAGE);
			if(cantImprimirStr == null){
				break;
			}
			if (cantImprimirStr.trim().length()==0 || !GenericUtils.esNumerico(cantImprimirStr)) {
				CLJOptionPane.showErrorMessage(owner, "Ingreso incorrecto", "error");
			} else {
				ok = true;
				internalImprimir(cantImprimirStr);
			}
		} while (!ok);
	}

	private void internalImprimir(String cantImprimirStr) {
		boolean hayMasDeUnRemito = remitos != null && !remitos.isEmpty();
		if(!hayMasDeUnRemito) {
			remitos = new ArrayList<RemitoSalida>();
			remitos.add(getRemito());
		}
		Collections.sort(remitos, new Comparator<RemitoSalida>() {
			public int compare(RemitoSalida o1, RemitoSalida o2) {
				return o1.getNroRemito().compareTo(o2.getNroRemito());
			}
		});

		JasperReport reporte = null;
//		if(System.getProperties().getProperty("test")== null || System.getProperties().getProperty("test").equals("0")) {
		if(!GenericUtils.isSistemaTest()){
			reporte = JasperHelper.loadReporte(ARCHIVO_JASPER);
		} else {
			reporte = JasperHelper.loadReporte(ARCHIVO_JASPER_B);
		}
		
		for(RemitoSalida rs : remitos) {
			JasperPrint jasperPrint = null;
			if(!GenericUtils.isSistemaTest()) {
				RemitoEntradaTO remitoEntrada = new RemitoEntradaTO(rs, nroSucursal);
				jasperPrint = JasperHelper.fillReport(reporte, remitoEntrada.getParameters(), Collections.singletonList(remitoEntrada));
			} else {
				RemitoEntradaBTO remitoEntrada = new RemitoEntradaBTO(rs, nroSucursal);
				jasperPrint = JasperHelper.fillReport(reporte, remitoEntrada.getParameters(), Collections.singletonList(remitoEntrada));
			}
			Integer cantidadAImprimir = Integer.valueOf(cantImprimirStr);
			for(int i = 0; i < cantidadAImprimir; i ++) {
				imprimirReporte(jasperPrint, rs, i+1, cantidadAImprimir);
			}
		}
	}

	private Integer imprimirReporte(JasperPrint reporte, RemitoSalida rs, int nroCopia, int totalCopias) {
		Integer ret = 0;
		try{
			JDialogAvisoImpresionRemitoSalida dialogoAviso = new JDialogAvisoImpresionRemitoSalida(owner, rs, nroCopia, totalCopias);
			GuiUtil.centrar(dialogoAviso);
			dialogoAviso.setVisible(true);
			JasperPrintManager.printReport(reporte, nroCopia==1);
		}catch (JRException je) {
			if (je.getCause() != null && je.getCause().getMessage().equals("Printer is not accepting job.")) {
				CLJOptionPane.showErrorMessage(null, "No se puede imprimir, la impresora no responde o no esta disponible. Por favor, verifique el estado", "Error");
			} else {
				CLJOptionPane.showErrorMessage(null, StringW.wordWrap(je.getMessage()), "Error");
			}
		}
		return ret;
	}

	private RemitoSalida getRemito() {
		return remito;
	}

	public static class RemitoEntradaBTO{
		private Map parameters;
		private final List<PiezaRemitoTO> piezas1;
		private final List<PiezaRemitoTO> piezas2;
		private final List<PiezaRemitoTO> piezas3;
		private static final int CANT_PIEZAS_SPLIT = 16;
		private static final int CANT_MAX_PIEZAS = 48;
		
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
			parameters.put("RAZON_SOCIAL", remito.getCliente().getRazonSocial());
			parameters.put("DOMICILIO", remito.getCliente().getDireccionFiscal().getDireccion()+ ", " + remito.getCliente().getDireccionFiscal().getLocalidad().getNombreLocalidad());
			parameters.put("SUBREPORT_DIR", "ar/com/textillevel/reportes/");
//			parameters.put("ARTICULO", extractArticulo(remito));
			parameters.put("POSICION_IVA", remito.getCliente().getPosicionIva() == null ? "" : remito.getCliente().getPosicionIva().getDescripcion());
			parameters.put("CUIT", remito.getCliente().getCuit());
			parameters.put("PROCESO", getProceso(remito));
			parameters.put("REMITO_ENT", String.valueOf(remito.getOdts().get(0).getRemito().getNroRemito()));
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
	
	public static class RemitoEntradaTO {

		private Map parameters;
		private List<PiezaRemitoTO> piezas1;
		private List<PiezaRemitoTO> piezas2;
		private List<PiezaRemitoTO> piezas3;
		private static final int CANT_PIEZAS_SPLIT = 19; 
		private static final int CANT_MAX_PIEZAS = 53;

		public RemitoEntradaTO(RemitoSalida remito, Integer nroSucursal) {
			cargarMap(remito, nroSucursal);
		}

		private void cargarMap(RemitoSalida remito, Integer nroSucursal) {
			this.parameters  = new HashMap();
			parameters.put("FECHA_REMITO", DateUtil.dateToString(remito.getFechaEmision()));
			parameters.put("RAZON_SOCIAL", remito.getCliente().getRazonSocial());
			parameters.put("DOMICILIO", remito.getCliente().getDireccionFiscal().getDireccion());
			parameters.put("LOCALIDAD", remito.getCliente().getDireccionFiscal().getLocalidad().getNombreLocalidad());
			parameters.put("SUBREPORT_DIR", "ar/com/textillevel/reportes/");
			parameters.put("USUARIO", GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName());
			parameters.put("ARTICULO", extractArticulo(remito));
			parameters.put("POSICION_IVA", remito.getCliente().getPosicionIva() == null ? "" : remito.getCliente().getPosicionIva().getDescripcion());
			parameters.put("CUIT", remito.getCliente().getCuit());
			parameters.put("PROCESO", getProceso(remito));
			parameters.put("REMITO_ENT", String.valueOf(remito.getOdts().get(0).getRemito().getNroRemito()));
			parameters.put("ODT", extractODTs(remito.getOdts()));
			parameters.put("TOT_PIEZAS", fixPrecioCero(GenericUtils.getDecimalFormat().format(remito.getPiezas().size())) + " " + GenericUtils.convertirNumeroATexto(Double.valueOf(remito.getPiezas().size())));
			parameters.put("TOT_KILOS", fixPrecioCero(GenericUtils.getDecimalFormat().format(remito.getPesoTotal().doubleValue())));
			parameters.put("TOT_METROS", fixPrecioCero(GenericUtils.getDecimalFormat().format(remito.getTotalMetros())));
			populateListaPiezas(reescribirOrdenPiezas(remito.getPiezas()));
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

		private String extractODTs(List<OrdenDeTrabajo> odts) {
			List<String> odtList = new ArrayList<String>();
			for(OrdenDeTrabajo odt : odts) {
				odtList.add(ODTCodigoHelper.getInstance().formatCodigo(odt.getCodigo()));
			}
			return StringUtil.getCadena(odtList, ", ");
		}

		private String fixPrecioCero(String format) {
			if(!StringUtil.isNullOrEmpty(format) && (format.equals(",00") || format.equals(".00"))) {
				return "0,00";
			}
			return format;
		}

		private String getProceso(RemitoSalida remito) {
			Set<String> procesos = new HashSet<String>();
			for(Producto p : remito.getProductoList()) {
				procesos.add(p.getDescripcion());
			}
			return StringUtil.getCadena(procesos, ", ");
		}

		private String extractArticulo(RemitoSalida remito) {
			Set<String> articulos = new HashSet<String>();
 			List<Producto> productoList = remito.getProductoList();
			for(Producto p : productoList) {
				if(p.getArticulo() != null) {
					articulos.add(p.getArticulo().getNombre());
				}
			}
			return StringUtil.getCadena(articulos, ", ");
		}

		private void populateListaPiezas(List<PiezaRemito> piezas) {
			this.piezas1 = new ArrayList<PiezaRemitoTO>();
			this.piezas2 = new ArrayList<PiezaRemitoTO>();
			this.piezas3 = new ArrayList<PiezaRemitoTO>();
			populateSingleListaPiezas(piezas1, 0, CANT_PIEZAS_SPLIT, piezas);
			populateSingleListaPiezas(piezas2, CANT_PIEZAS_SPLIT, 2*CANT_PIEZAS_SPLIT, piezas);
			populateSingleListaPiezas(piezas3, 2*CANT_PIEZAS_SPLIT, CANT_MAX_PIEZAS, piezas);
		}

		private void populateSingleListaPiezas(List<PiezaRemitoTO> piezasList, int from, int until, List<PiezaRemito> allPiezas) {
			double metrosTot = 0d;
			for(int i = from; i < until; i++) {
				if(i < allPiezas.size()) {
					PiezaRemito piezaRemito = allPiezas.get(i);
					piezasList.add(toPiezaRemitoTO(piezaRemito, piezaRemito.getTotalMetros().toString()));
					metrosTot += piezaRemito.getMetros().doubleValue();
				} else {
					PiezaRemitoTO prto = new PiezaRemitoTO();
					prto.setMetros("");
					prto.setNumero("");
					prto.setObservaciones("");
					piezasList.add(prto);
				}
			}
			if(metrosTot != 0d) {
				PiezaRemito piezaRemitoST = new PiezaRemito();
				piezasList.add(toPiezaRemitoTO(piezaRemitoST, GenericUtils.getDecimalFormat().format(metrosTot)));
			}
		}

		private PiezaRemitoTO toPiezaRemitoTO(PiezaRemito piezaRemito, String totalString) {
			PiezaRemitoTO prto = new PiezaRemitoTO();
			prto.setMetros(totalString);
			prto.setNumero(piezaRemito.getOrdenPiezaCalculado() == null ? "" : piezaRemito.getOrdenPiezaCalculado());
			prto.setObservaciones(piezaRemito.getObservaciones() == null ? "" : piezaRemito.getObservaciones().trim());	
			return prto;
		}

		public Map getParameters() {
			return parameters;
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

	}

	public static class PiezaRemitoTO {

		private String numero;
		private String metros;
		private String observaciones;

		public String getNumero() {
			return numero;
		}

		public void setNumero(String numero) {
			this.numero = numero;
		}

		public String getMetros() {
			return metros;
		}

		public void setMetros(String metros) {
			this.metros = metros;
		}

		public String getObservaciones() {
			return observaciones;
		}

		public void setObservaciones(String observaciones) {
			this.observaciones = observaciones;
		}

	}
}