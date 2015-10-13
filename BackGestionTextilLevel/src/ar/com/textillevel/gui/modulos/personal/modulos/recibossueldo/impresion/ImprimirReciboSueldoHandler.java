package ar.com.textillevel.gui.modulos.personal.modulos.recibossueldo.impresion;

import java.awt.Frame;
import java.sql.Date;
import java.util.Collections;

import javax.swing.JOptionPane;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.textillevel.gui.modulos.personal.modulos.recibossueldo.gui.JDialogInputFechasImpresion;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.JasperHelper;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.ReciboSueldo;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.enums.EEstadoReciboSueldo;

public class ImprimirReciboSueldoHandler {

	private final ReciboSueldo reciboSueldo;
	private final Frame owner;
	private static final String ARCHIVO_JASPER = "/ar/com/textillevel/reportes/recibo_sueldo.jasper";

	public ImprimirReciboSueldoHandler(ReciboSueldo reciboSueldo, Frame owner) {
		this.reciboSueldo = reciboSueldo;
		this.owner = owner;
	}

	@SuppressWarnings("unchecked")
	public boolean imprimir() {
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
				ReciboSueldoTO recibo = null;
				if(getReciboSueldo().getEstado() == EEstadoReciboSueldo.VERIFICADO) { //Es la primera vez
					JDialogInputFechasImpresion dialogo = new JDialogInputFechasImpresion(owner, "Fecha de último depósito / Fecha de pago");
					dialogo.setVisible(true);
					if(dialogo.isAcepto()) {
						Date fechaPago = dialogo.getFechaPago();
						Date fechaUltDeposito = dialogo.getFechaUltDeposito();
						getReciboSueldo().setFechaPago(fechaPago);
						getReciboSueldo().setFechaUltDeposito(fechaUltDeposito);
						recibo = new ReciboSueldoTO(getReciboSueldo(), fechaUltDeposito, fechaPago);
					} else {
						return false;
					}
				} else {
					recibo = new ReciboSueldoTO(getReciboSueldo(), getReciboSueldo().getFechaUltDeposito(), getReciboSueldo().getFechaPago());
				}
				JasperReport reporte = JasperHelper.loadReporte(ARCHIVO_JASPER);
				try {
					JasperPrint jasperPrint = JasperHelper.fillReport(reporte, recibo.getParameters(), Collections.singletonList(recibo));
					Integer cantidadAImprimir = Integer.valueOf(input);
					JasperHelper.imprimirReporte(jasperPrint, true, true, cantidadAImprimir);
					return true;
				} catch (JRException e) {
					e.printStackTrace();
				}
			}
		} while (!ok);
		return false;
	}

	private ReciboSueldo getReciboSueldo() {
		return reciboSueldo;
	}

}
