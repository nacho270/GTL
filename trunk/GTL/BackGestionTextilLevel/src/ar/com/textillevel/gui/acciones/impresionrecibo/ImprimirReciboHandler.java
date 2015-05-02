package ar.com.textillevel.gui.acciones.impresionrecibo;

import java.math.BigDecimal;
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
import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.util.DateUtil;
import ar.clarin.fwjava.util.StringUtil;
import ar.com.textillevel.entidades.documentos.recibo.Recibo;
import ar.com.textillevel.entidades.documentos.recibo.formapago.FormaPago;
import ar.com.textillevel.entidades.documentos.recibo.formapago.ImpresionFormaPagoVisitor;
import ar.com.textillevel.entidades.documentos.recibo.pagos.PagoRecibo;
import ar.com.textillevel.entidades.documentos.recibo.pagos.visitor.ItemImpresionReciboVisitor;
import ar.com.textillevel.entidades.documentos.recibo.to.ChequeTO;
import ar.com.textillevel.entidades.documentos.recibo.to.NotaCreditoTO;
import ar.com.textillevel.entidades.documentos.recibo.to.PagoTO;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.JasperHelper;

public class ImprimirReciboHandler {

	private final Recibo recibo;
	private final JDialog owner;
	private final Integer nroSucursal;
	private static final String ARCHIVO_JASPER = "/ar/com/textillevel/reportes/recibo.jasper";

	public ImprimirReciboHandler(Recibo recibo, Integer nroSucursal, JDialog owner) {
		this.recibo = recibo;
		this.owner = owner;
		this.nroSucursal = nroSucursal;
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
				CLJOptionPane.showErrorMessage(owner, "Ingreso incorrecto", "error");
			} else {
				ok = true;
				ReciboTO recibo = new ReciboTO(getRecibo());
				JasperReport reporte = JasperHelper.loadReporte(ARCHIVO_JASPER);
				try {
					JasperPrint jasperPrint = JasperHelper.fillReport(reporte, recibo.getParameters(nroSucursal), Collections.singletonList(recibo));
					Integer cantidadAImprimir = Integer.valueOf(input);
					JasperHelper.imprimirReporte(jasperPrint, true, true, cantidadAImprimir);
				} catch (JRException e) {
					e.printStackTrace();
				}
			}
		} while (!ok);
	}

	private Recibo getRecibo() {
		return recibo;
	}

	public static class ReciboTO {

		private static final int CANT_CHEQUES_POR_COLUMNA = 7;
		private static final int CANT_NCs_POR_COLUMNA = 4;

		private Integer nroRecibo;
		private String fechaRecibo;
		private String razonSocial;
		private String domicilio;
		private String iva;
		private String cuit;
		private String txtTotalPesos;
		private List<PagoTO> pagos;
		private List<PagoTO> pagos2;
		private List<NotaCreditoTO> ncs1; //notas de credito 
		private List<NotaCreditoTO> ncs2; //notas de credito
		private List<ChequeTO> cheques;
		private List<ChequeTO> cheques2;
		
		private String importeTransfBancaria;
		private String nroTx;
		private String observacionesTx;
		
		private String importeRetIVA;
		private String importeRetGan;
		private String importeRetIIBB;
		private String importeEfectivo;
		private String importeTotal;

		public ReciboTO() {
		}

		public ReciboTO(Recibo recibo) {
			this.nroRecibo = recibo.getNroRecibo();
			this.fechaRecibo = DateUtil.dateToString(recibo.getFecha(), DateUtil.LONG_DATE);
			this.razonSocial = recibo.getCliente().getRazonSocial();
			this.domicilio = recibo.getCliente().getDireccionFiscal().getDireccion();
			this.iva = recibo.getCliente().getPosicionIva().getDescripcion();
			this.cuit = recibo.getCliente().getCuit();

			ImpresionFormaPagoVisitor ifpv = new ImpresionFormaPagoVisitor();
			for(FormaPago fp : recibo.getPagos()) {
				fp.accept(ifpv);
			}
			llenarAndDesdoblarListaCheques(ifpv.getChequeList(), ifpv.getTotalCheques());
			
			llenarAndDesdoblarListaNCs(ifpv.getNotaCreditoList(), ifpv.getTotalNCs());

			cargarPagos(recibo);

			this.importeTransfBancaria = fixPrecioCero(GenericUtils.getDecimalFormat().format(ifpv.getImporteTransfBancaria().doubleValue()));
			String observacionesTx2 = ifpv.getObservacionesTx();
			String nroTx2 = ifpv.getNtoTx();
			this.observacionesTx = StringUtil.isNullOrEmpty(observacionesTx2) ? "" : observacionesTx2 ;
			this.nroTx = StringUtil.isNullOrEmpty(nroTx2) ? "" : nroTx2;

			this.importeRetIVA = fixPrecioCero(GenericUtils.getDecimalFormat().format(ifpv.getImporteRetIVA().doubleValue()));
			this.importeRetGan =  fixPrecioCero(GenericUtils.getDecimalFormat().format(ifpv.getImporteRetGan().doubleValue()));
			this.importeRetIIBB =  fixPrecioCero(GenericUtils.getDecimalFormat().format(ifpv.getImporteRetIIBB().doubleValue()));
			this.importeEfectivo =  fixPrecioCero(GenericUtils.getDecimalFormat().format(ifpv.getImporteEfectivo().doubleValue()));
			this.importeTotal = GenericUtils.getDecimalFormat().format(recibo.getMonto().doubleValue());
			this.txtTotalPesos = recibo.getTxtCantidadPesos();
		}

		private void llenarAndDesdoblarListaNCs(List<NotaCreditoTO> notaCreditoList, BigDecimal totalNCs) {
			this.ncs1 = new ArrayList<NotaCreditoTO>();
			this.ncs2 = new ArrayList<NotaCreditoTO>();
			int desde = 0;
			for(; desde < Math.min(CANT_NCs_POR_COLUMNA, notaCreditoList.size()); desde++) {
				ncs1.add(notaCreditoList.get(desde));
			}
			for(; desde < notaCreditoList.size(); desde++) {
				ncs2.add(notaCreditoList.get(desde));
			}
			//Agrego el item totalizador
			NotaCreditoTO chequeTOTotal = new NotaCreditoTO();
			chequeTOTotal.setFecha("TOTAL");
			chequeTOTotal.setDescrNC("");
			chequeTOTotal.setImporte(fixPrecioCero(GenericUtils.getDecimalFormat().format(totalNCs.doubleValue())));
			if(!notaCreditoList.isEmpty()) {
				if(notaCreditoList.size() <= CANT_NCs_POR_COLUMNA) {
					ncs1.add(chequeTOTotal);
				} else {
					ncs2.add(chequeTOTotal);
				}
			}
		}

		private void llenarAndDesdoblarListaCheques(List<ChequeTO> chequesTO, BigDecimal totalImporteCheques) {
			this.cheques = new ArrayList<ChequeTO>();
			this.cheques2 = new ArrayList<ChequeTO>();
			int desde = 0;
			for(; desde < Math.min(CANT_CHEQUES_POR_COLUMNA, chequesTO.size()); desde++) {
				cheques.add(chequesTO.get(desde));
			}
			for(; desde < chequesTO.size(); desde++) {
				cheques2.add(chequesTO.get(desde));
			}
			//Agrego el item totalizador
			ChequeTO chequeTOTotal = new ChequeTO();
			chequeTOTotal.setCuit("TOTAL");
			chequeTOTotal.setBanco("");
			chequeTOTotal.setNumero("");
			chequeTOTotal.setImporte(fixPrecioCero(GenericUtils.getDecimalFormat().format(totalImporteCheques.doubleValue())));
			if(!chequesTO.isEmpty()) {
				if(chequesTO.size() <= CANT_CHEQUES_POR_COLUMNA) {
					cheques.add(chequeTOTotal);
				} else {
					cheques2.add(chequeTOTotal);
				}
			}
		}

		private void llenarAndDesdoblarListaPagos(List<PagoTO> pagosTOTemp, BigDecimal total) {
			this.pagos = new ArrayList<PagoTO>();
			this.pagos2 = new ArrayList<PagoTO>();
			int desde = 0;
			int mitad = pagosTOTemp.size() / 2;
			for(; desde < mitad; desde++) {
				pagos.add(pagosTOTemp.get(desde));
			}
			for(; desde < pagosTOTemp.size(); desde++) {
				pagos2.add(pagosTOTemp.get(desde));
			}
			PagoTO pagoTO = new PagoTO();
			pagoTO.setConcepto("TOTAL");
			pagoTO.setFecha("");
			pagoTO.setImportePagado(GenericUtils.getDecimalFormat().format(total.floatValue()));
			
			if(!pagosTOTemp.isEmpty()) {
				if(pagosTOTemp.size() <= mitad) {
					pagos.add(pagoTO);
				} else {
					pagos2.add(pagoTO);
				}
			}
			
		}

		private String fixPrecioCero(String format) {
			if(!StringUtil.isNullOrEmpty(format) && (format.equals(",00") || format.equals(".00"))) {
				return "0,00";
			}
			return format;
		}

		private void cargarPagos(Recibo recibo) {
			BigDecimal total = new BigDecimal(0);

			List<PagoTO> pagosTOTemp = new ArrayList<PagoTO>();
			for(PagoRecibo pr : recibo.getPagoReciboList()) {
				ItemImpresionReciboVisitor iirv = new ItemImpresionReciboVisitor(recibo);
				pr.accept(iirv);
				pagosTOTemp.add(iirv.getPagoTO());
				total = total.add(pr.getMontoPagado());
			}
			llenarAndDesdoblarListaPagos(pagosTOTemp, total);
		}

		public List<PagoTO> getPagos() {
			return pagos;
		}

		public JRDataSource getPagosDS2() {
			return new JRBeanCollectionDataSource(pagos2);
		}

		public JRDataSource getPagosDS() {
			return new JRBeanCollectionDataSource(pagos);
		}

		public JRDataSource getChequesDS() {
			return new JRBeanCollectionDataSource(cheques);
		}

		public JRDataSource getChequesDS2() {
			return new JRBeanCollectionDataSource(cheques2);
		}
		
		public JRDataSource getNcsDS1() {
			return new JRBeanCollectionDataSource(ncs1);
		}
		
		public JRDataSource getNcsDS2() {
			return new JRBeanCollectionDataSource(ncs2);
		}

		@SuppressWarnings({ "unchecked", "rawtypes" })
		public Map getParameters(Integer nroSucursal) {
			Map parameterMap = new HashMap();
			parameterMap.put("NRO_RECIBO", "N° " +  StringUtil.fillLeftWithZeros(nroSucursal.toString(), 4) + "-" + StringUtil.fillLeftWithZeros(nroRecibo.toString(), 7));
			parameterMap.put("FECHA_RECIBO", fechaRecibo);
			parameterMap.put("RAZON_SOCIAL", razonSocial);
			parameterMap.put("DOMICILIO", domicilio);
			parameterMap.put("IVA", iva);
			parameterMap.put("CUIT", cuit);
			parameterMap.put("CANT_PESOS", txtTotalPesos.toUpperCase());
			parameterMap.put("SUBREPORT_DIR", "ar/com/textillevel/reportes/");
			parameterMap.put("USUARIO", GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName());
			parameterMap.put("IS_TEST", GenericUtils.isSistemaTest());
			
			parameterMap.put("TOT_TRANSF_BANCARIA", importeTransfBancaria);
			parameterMap.put("TX_TRANSF_BANCARIA", nroTx);
			parameterMap.put("OBS_TRANSF_BANCARIA", observacionesTx);

			parameterMap.put("RETENCION_IVA", importeRetIVA);
			parameterMap.put("RETENCION_GAN", importeRetGan);
			parameterMap.put("RETENCION_IIBB", importeRetIIBB);
			parameterMap.put("EFECTIVO", importeEfectivo);

			parameterMap.put("TOTAL_PAGADO", importeTotal);

			return parameterMap;
		}

		public List<ChequeTO> getCheques() {
			return cheques;
		}

		public void setCheques(List<ChequeTO> cheques) {
			this.cheques = cheques;
		}

	}

}
