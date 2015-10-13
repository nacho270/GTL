package ar.com.textillevel.gui.acciones.impresionordendepago;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import main.GTLGlobalCache;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.util.DateUtil;
import ar.com.textillevel.entidades.documentos.ordendepago.OrdenDePago;
import ar.com.textillevel.entidades.documentos.ordendepago.formapago.FormaPagoOrdenDePago;
import ar.com.textillevel.entidades.documentos.ordendepago.pagos.PagoOrdenDePago;
import ar.com.textillevel.entidades.documentos.ordendepago.pagos.visitor.GetTotalFormasPagoVisitor;
import ar.com.textillevel.entidades.documentos.ordendepago.pagos.visitor.GetTotalPagosVisitor;
import ar.com.textillevel.entidades.documentos.recibo.to.ChequeTO;
import ar.com.textillevel.entidades.documentos.recibo.to.NotaCreditoTO;
import ar.com.textillevel.entidades.documentos.recibo.to.PagoTO;
import ar.com.textillevel.entidades.documentos.recibo.to.TransferenciaBancariaTO;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.JasperHelper;

@SuppressWarnings("unused")
public class ImprimirOrdenDePagoHandler {
	private final OrdenDePago orden;
	private final JDialog owner;
	private static final String ARCHIVO_JASPER = "/ar/com/textillevel/reportes/ordendepago.jasper";

	public ImprimirOrdenDePagoHandler(OrdenDePago orden, JDialog owner) {
		this.orden = orden;
		this.owner = owner;
	}
	
	@SuppressWarnings("unchecked")
	public void imprimir() {
		boolean ok = false;
		do {
			String input = JOptionPane.showInputDialog(owner, "Ingrese la cantidad de copias: ", "Imprimir", JOptionPane.INFORMATION_MESSAGE);
			if(input == null){
				break;
			}
			if (input.trim().length()==0 || !GenericUtils.esNumerico(input)) {
				FWJOptionPane.showErrorMessage(owner, "Ingreso incorrecto", "error");
			} else {
				ok = true;
				OrdenDePagoTO orden = new OrdenDePagoTO(getOrden());
				JasperReport reporte = JasperHelper.loadReporte(ARCHIVO_JASPER);
				try {
					JasperPrint jasperPrint = JasperHelper.fillReport(reporte, orden.getParameters(orden), Collections.singletonList(orden));
					Integer cantidadAImprimir = Integer.valueOf(input);
					JasperHelper.imprimirReporte(jasperPrint, true, true, cantidadAImprimir);
				} catch (JRException e) {
					e.printStackTrace();
				}
			}
		} while (!ok);
	}

	
	public OrdenDePago getOrden() {
		return orden;
	}

	public static class OrdenDePagoTO implements Serializable{

		private static final long serialVersionUID = -2905848898238839729L;
		
		private static final int CANT_CHEQUES_POR_FILA = 10;
		private static final int CANT_PAGOS_POR_FILA = 8;
		
		private final Integer nroOrden;
		private final Timestamp fechaOrden;
		private final String razonSocialProv;
		private final String direccionProv;
		private final String cuitProv;
		//private final String condVenta;
		
		private final BigDecimal totIngBrut;
		private final BigDecimal totIva;
		private final BigDecimal totGanancias;
		private final BigDecimal totEfectivo;
		private final BigDecimal total;
		
		private BigDecimal totalPagos;
		private final BigDecimal totalCheques;
		
		private List<PagoTO> pagos1;
		private List<PagoTO> pagos2;
		
		private List<ChequeTO> cheques1;
		private List<ChequeTO> cheques2;
		
		private final List<NotaCreditoTO> notasCredito;
		private final TransferenciaBancariaTO infoTransferencia;
		
		public OrdenDePagoTO(OrdenDePago orden){
			this.nroOrden = orden.getNroOrden();
			this.fechaOrden = orden.getFechaEmision();
			this.razonSocialProv = orden.getProveedor().getRazonSocial();
			this.direccionProv = orden.getProveedor().getDireccionFiscal().getDireccion() + " - " + orden.getProveedor().getDireccionFiscal().getLocalidad().getNombreLocalidad();
			this.cuitProv = orden.getProveedor().getCuit();
			//this.condVenta = orden.getProveedor().getCondicionVenta().getNombre();
			
			GetTotalFormasPagoVisitor totalesFormaPago = new GetTotalFormasPagoVisitor();
			for(FormaPagoOrdenDePago fp : orden.getFormasDePago()){
				fp.accept(totalesFormaPago);
			}
			notasCredito = totalesFormaPago.getNotasCredito();
			infoTransferencia = totalesFormaPago.getInfoTransf();
			Double totalNotasCredito = totalesFormaPago.getTotalNotasCredito();
			if(totalNotasCredito>0){
				NotaCreditoTO ncTO = new NotaCreditoTO();
				ncTO.setDescrNC("TOTAL");
				ncTO.setFecha("");
				ncTO.setImporte(GenericUtils.getDecimalFormat().format(totalNotasCredito));
				notasCredito.add(ncTO);
			}
			
			total = totalesFormaPago.getTotal();
			totalCheques = totalesFormaPago.getTotalCheques();
			totEfectivo = totalesFormaPago.getTotEfectivo();
			totGanancias = totalesFormaPago.getTotGanancias();
			totIngBrut = totalesFormaPago.getTotIngBrut();
			totIva = totalesFormaPago.getTotIva();
			
			List<PagoTO> pagosTemp = new ArrayList<PagoTO>();

			totalPagos = new BigDecimal(0d);
			GetTotalPagosVisitor sumaPagos = new GetTotalPagosVisitor(orden);
			for(PagoOrdenDePago p : orden.getPagos()){
				p.accept(sumaPagos);
				pagosTemp.add(sumaPagos.getPagoTO());
				totalPagos = totalPagos.add(p.getMontoPagado());
			}
			
			llenarAndDesdoblarListaCheques(totalesFormaPago.getCheques());
			llenarAndDesdoblarListaPagos(pagosTemp);
		}
		
		private void llenarAndDesdoblarListaPagos(List<PagoTO> pagosTOTemp) {
			this.pagos1 = new ArrayList<PagoTO>();
			this.pagos2 = new ArrayList<PagoTO>();
			int desde = 0;
			for(; desde < Math.min(CANT_PAGOS_POR_FILA, pagosTOTemp.size()); desde++) {
				pagos1.add(pagosTOTemp.get(desde));
			}
			for(; desde < pagosTOTemp.size(); desde++) {
				pagos2.add(pagosTOTemp.get(desde));
			}
			PagoTO pagoTO = new PagoTO();
			pagoTO.setConcepto("TOTAL");
			pagoTO.setFecha("");
			pagoTO.setImportePagado(GenericUtils.getDecimalFormat().format(totalPagos.floatValue()));
			
			if(!pagosTOTemp.isEmpty()) {
				if(pagosTOTemp.size() <= CANT_PAGOS_POR_FILA) {
					pagos1.add(pagoTO);
				} else {
					pagos2.add(pagoTO);
				}
			}
		}
		
		private void llenarAndDesdoblarListaCheques(List<ChequeTO> chequesTO) {
			this.cheques1 = new ArrayList<ChequeTO>();
			this.cheques2 = new ArrayList<ChequeTO>();
			int mitad = chequesTO.size() / 2;
			int desde = 0;
			for(; desde < Math.min(mitad, chequesTO.size()); desde++) {
				cheques1.add(chequesTO.get(desde));
			}
			for(; desde <  chequesTO.size(); desde++) {
				cheques2.add(chequesTO.get(desde));
			}
			//Agrego el item totalizador
			ChequeTO chequeTOTotal = new ChequeTO();
			chequeTOTotal.setCuit("TOTAL");
			chequeTOTotal.setBanco("");
			chequeTOTotal.setNumero("");
			chequeTOTotal.setImporte(GenericUtils.fixPrecioCero(GenericUtils.getDecimalFormat().format(totalCheques.doubleValue())));
			if(!chequesTO.isEmpty()) {
				if(chequesTO.size() <= mitad) {
					cheques1.add(chequeTOTotal);
				} else {
					cheques2.add(chequeTOTotal);
				}
			}
		}
		
		public JRDataSource getPagosDS2() {
			return new JRBeanCollectionDataSource(pagos2);
		}

		public JRDataSource getPagosDS1() {
			return new JRBeanCollectionDataSource(pagos1);
		}

		public JRDataSource getChequesDS1() {
			return new JRBeanCollectionDataSource(cheques1);
		}

		public JRDataSource getChequesDS2() {
			return new JRBeanCollectionDataSource(cheques2);
		}
		
		public JRDataSource getNotasCreditoDS() {
			if(notasCredito!=null && !notasCredito.isEmpty()){
				return new JRBeanCollectionDataSource(notasCredito);
			}
			return null;
		}
		
		@SuppressWarnings({ "unchecked", "rawtypes" })
		public Map getParameters(OrdenDePagoTO orden) {
			Map params = new HashMap();
			params.put("NRO_ORDEN",String.valueOf(nroOrden));
			params.put("FECHA_ORDEN",DateUtil.dateToString(fechaOrden, DateUtil.SHORT_DATE));
			params.put("NOMBRE_PROVEEDOR", razonSocialProv);
			params.put("CUIT_PROVEEDOR", cuitProv);
			params.put("DIRECCION_PROVEEDOR", direccionProv);
			params.put("COND_VENTA", "");
			params.put("SUBREPORT_DIR", "ar/com/textillevel/reportes/");
			params.put("USUARIO", GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName());
			params.put("TOT_RET_ING_BRUT", GenericUtils.fixPrecioCero(GenericUtils.getDecimalFormat().format(totIngBrut.doubleValue())));
			params.put("TOT_RET_GAN", GenericUtils.fixPrecioCero(GenericUtils.getDecimalFormat().format(totGanancias.doubleValue())));
			params.put("TOT_EFECTIVO", GenericUtils.fixPrecioCero(GenericUtils.getDecimalFormat().format(totEfectivo.doubleValue())));
			params.put("TOT_RET_IVA", GenericUtils.fixPrecioCero(GenericUtils.getDecimalFormat().format(totIva.doubleValue())));
			params.put("TOTAL_PAGADO", GenericUtils.fixPrecioCero(GenericUtils.getDecimalFormat().format(total.doubleValue())));
			params.put("TOTAL_LETRAS", "Son Pesos: " + GenericUtils.convertirNumeroATexto(total.doubleValue()));
			params.put("IMPRIMIR_DATOS", !GenericUtils.isSistemaTest());
			if(orden.infoTransferencia!=null){
				params.put("IMPORTE_TRANSF", orden.infoTransferencia.getImporteTransfBancaria());
				params.put("NRO_TRANSF", orden.infoTransferencia.getNroTx());
				params.put("OBS_TRANSF", orden.infoTransferencia.getObservacionesTx());
			}else{
				params.put("IMPORTE_TRANSF", "0,00");
				params.put("NRO_TRANSF", "------");
				params.put("OBS_TRANSF", "-------");
			}
			return params;
		}
	}
}
