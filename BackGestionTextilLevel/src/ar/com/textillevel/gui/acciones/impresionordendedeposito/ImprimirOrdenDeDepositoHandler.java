package ar.com.textillevel.gui.acciones.impresionordendedeposito;

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
import ar.com.textillevel.entidades.documentos.ordendedeposito.DepositoCheque;
import ar.com.textillevel.entidades.documentos.ordendedeposito.OrdenDeDeposito;
import ar.com.textillevel.entidades.documentos.recibo.to.ChequeTO;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.JasperHelper;

public class ImprimirOrdenDeDepositoHandler {

	private final OrdenDeDeposito orden;
	private final JDialog owner;
	private static final String ARCHIVO_JASPER_DEPOSITO = "/ar/com/textillevel/reportes/orden_de_deposito.jasper";
	
	public ImprimirOrdenDeDepositoHandler(OrdenDeDeposito orden, JDialog owner) {
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
				OrdenDeDepositoTO ordenTo = new OrdenDeDepositoTO(getOrden());
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

	public OrdenDeDeposito getOrden() {
		return orden;
	}
	
	public static class OrdenDeDepositoTO implements Serializable{

		private static final long serialVersionUID = 4106889333320679977L;

		private static final int CANT_CHEQUES_POR_COLUMNA = 22;
		
		private final String nroOrden;
		private final String banco;
		private final String fecha;
		private final String totalCheques;
		private final String cantidadCheques;
		private final String totalLetrasCheques;
		
		private List<ChequeTO> cheques1;
		private List<ChequeTO> cheques2;
		
		public OrdenDeDepositoTO(OrdenDeDeposito ordenPosta){
			this.nroOrden = String.valueOf(ordenPosta.getNroOrden());
			this.banco = ordenPosta.getBanco().getNombre();
			this.fecha = DateUtil.dateToString(ordenPosta.getFecha(), DateUtil.SHORT_DATE);
			this.totalCheques = GenericUtils.getDecimalFormat().format(ordenPosta.getMontoTotal().doubleValue());
			this.cantidadCheques = String.valueOf(ordenPosta.getDepositos().size());
			this.totalLetrasCheques = ordenPosta.getTotalLetras();
			List<ChequeTO> cheques = getListaDeChequesTO(ordenPosta);
			llenarAndDesdoblarListaCheques(cheques);
		}

		private List<ChequeTO> getListaDeChequesTO(OrdenDeDeposito orden) {
			List<ChequeTO> cheques = new ArrayList<ChequeTO>();
			for(DepositoCheque dep : orden.getDepositos()){
				ChequeTO chequeTo = new ChequeTO();
				chequeTo.setBanco(dep.getCheque().getBanco().getNombre());
				chequeTo.setCuit(dep.getCheque().getCuit());
				chequeTo.setImporte(GenericUtils.getDecimalFormat().format(dep.getCheque().getImporte().doubleValue()));
				chequeTo.setNumero(dep.getCheque().getNumero() + " (" + dep.getCheque().getNumeracion() +")");
				cheques.add(chequeTo);
			}
			return cheques;
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
			params.put("NOMBRE_BANCO",banco);
			params.put("CANT_CHEQUES",cantidadCheques);
			params.put("TOTAL",totalCheques);
			params.put("TOTAL_LETRAS",totalLetrasCheques);
			params.put("SUBREPORT_DIR", "ar/com/textillevel/reportes/");
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
