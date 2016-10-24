package ar.com.textillevel.gui.acciones.impresionordendepagoapersona;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.util.DateUtil;
import ar.com.textillevel.entidades.cuenta.to.ETipoDocumento;
import ar.com.textillevel.entidades.documentos.pagopersona.OrdenDePagoAPersona;
import ar.com.textillevel.entidades.documentos.pagopersona.formapago.FormaPagoOrdenDePagoPersona;
import ar.com.textillevel.entidades.documentos.pagopersona.formapago.FormaPagoOrdenDePagoPersonaCheque;
import ar.com.textillevel.entidades.documentos.pagopersona.formapago.FormaPagoOrdenDePagoPersonaEfectivo;
import ar.com.textillevel.entidades.documentos.recibo.to.ChequeTO;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.JasperHelper;
import ar.com.textillevel.util.GestorTerminalBarcode;

public class ImprimirOrdenDePagoAPersonaHandler {

	private final OrdenDePagoAPersona orden;
	private final JDialog owner;

	private static final String ARCHIVO_JASPER_DEPOSITO = "/ar/com/textillevel/reportes/orden_de_pago_persona.jasper";
	
	public ImprimirOrdenDePagoAPersonaHandler(OrdenDePagoAPersona orden, JDialog owner) {
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
				OrdenDePagoAPersonaTO ordenTo = new OrdenDePagoAPersonaTO(getOrden());
				JasperReport reporte = JasperHelper.loadReporte(ARCHIVO_JASPER_DEPOSITO);
				try {
					JasperPrint jasperPrint = JasperHelper.fillReport(reporte, ordenTo.getParameters(), Collections.singletonList(ordenTo));
					Integer cantidadAImprimir = Integer.valueOf(input);
					JasperHelper.imprimirReporte(jasperPrint, true, true, cantidadAImprimir);
				} catch (JRException e) {
					e.printStackTrace();
				}
			}
		} while (!ok);
	}

	public OrdenDePagoAPersona getOrden() {
		return orden;
	}
	
	public static class OrdenDePagoAPersonaTO implements Serializable{

		private static final long serialVersionUID = 4106889333320679977L;

		private static final int CANT_CHEQUES_POR_COLUMNA = 5;
		
		private final String nroOrden;
		private final String persona;
		private final String fecha;
		private final String totalCheques;
		private final String cantidadCheques;
		private final String totalLetrasCheques;
		private final String motivo;
		private final String totalEfectivo;
		private final String totalgeneral;
		
		private List<ChequeTO> cheques1;
		private List<ChequeTO> cheques2;
		
		public OrdenDePagoAPersonaTO(OrdenDePagoAPersona ordenPosta){
			this.nroOrden = String.valueOf(ordenPosta.getNroOrden());
			this.persona = ordenPosta.getPersona().getRazonSocial();
			this.fecha = DateUtil.dateToString(ordenPosta.getFecha(), DateUtil.SHORT_DATE);
			int cantidadCheques = 0;
			Double sumaCheques = 0d;
			Double totalEfectivo = 0d;
			List<ChequeTO> cheques = new ArrayList<ChequeTO>();
			for(FormaPagoOrdenDePagoPersona dep : ordenPosta.getFormasDePago()){
				if(dep instanceof FormaPagoOrdenDePagoPersonaCheque){
					ChequeTO chequeTo = new ChequeTO();
					chequeTo.setBanco(((FormaPagoOrdenDePagoPersonaCheque)dep).getCheque().getBanco().getNombre());
					chequeTo.setCuit(((FormaPagoOrdenDePagoPersonaCheque)dep).getCheque().getCuit());
					chequeTo.setImporte(GenericUtils.getDecimalFormat().format(((FormaPagoOrdenDePagoPersonaCheque)dep).getCheque().getImporte().doubleValue()));
					chequeTo.setNumero(((FormaPagoOrdenDePagoPersonaCheque)dep).getCheque().getNumero() + " (" + ((FormaPagoOrdenDePagoPersonaCheque)dep).getCheque().getNumeracion() +")");
					cheques.add(chequeTo);
					cantidadCheques++;
					sumaCheques += ((FormaPagoOrdenDePagoPersonaCheque)dep).getCheque().getImporte().doubleValue();
				}else if(dep instanceof FormaPagoOrdenDePagoPersonaEfectivo){
					totalEfectivo = ((FormaPagoOrdenDePagoPersonaEfectivo)dep).getImporte().doubleValue();
				}
			}
			this.totalCheques = GenericUtils.getDecimalFormat().format(sumaCheques);
			this.cantidadCheques = String.valueOf(cantidadCheques);
			this.totalLetrasCheques = GenericUtils.convertirNumeroATexto(ordenPosta.getMontoTotal().doubleValue());
			this.totalgeneral = GenericUtils.getDecimalFormat().format(ordenPosta.getMontoTotal());
			this.motivo = ordenPosta.getDetalle();
			this.totalEfectivo = GenericUtils.getDecimalFormat().format(totalEfectivo);
			llenarAndDesdoblarListaCheques(cheques);
		}

		private void llenarAndDesdoblarListaCheques(List<ChequeTO> chequesTO) {
			this.cheques1 = new ArrayList<ChequeTO>();
			this.cheques2 = new ArrayList<ChequeTO>();
			int desde = 0;
			for(; desde < Math.min(CANT_CHEQUES_POR_COLUMNA, chequesTO.size()); desde++) {
				cheques1.add(chequesTO.get(desde));
			}
			for(; desde < chequesTO.size(); desde++) {
				cheques2.add(chequesTO.get(desde));
			}
		}

		@SuppressWarnings({ "unchecked", "rawtypes" })
		public Map getParameters() {
			Map params = new HashMap();
			params.put("NRO_ORDEN",String.valueOf(nroOrden));
			params.put("FECHA",fecha);
			params.put("NOMBRE_PERSONA",persona);
			params.put("CANT_CHEQUES",cantidadCheques);
			params.put("TOTAL",totalCheques);
			params.put("TOTAL_LETRAS",totalLetrasCheques.toUpperCase());
			params.put("TOTAL_EFECTIVO",totalEfectivo);
			params.put("TOTAL_GENERAL",totalgeneral);
			params.put("MOTIVO",motivo.toUpperCase());
			params.put("SUBREPORT_DIR", "ar/com/textillevel/reportes/");
			params.put("IMPRIMIR_DATOS", !GenericUtils.isSistemaTest());
			try {
				params.put("IMAGEN", GenericUtils.createBarCode(GestorTerminalBarcode.crear(ETipoDocumento.ORDEN_PAGO_PERSONA, Integer.valueOf(nroOrden), GenericUtils.isSistemaTest())));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return params;
		}

		public JRDataSource getChequesDS1() {
			return new JRBeanCollectionDataSource(cheques1);
		}
		
		public JRDataSource getChequesDS2() {
			return new JRBeanCollectionDataSource(cheques2);
		}
	}
}
