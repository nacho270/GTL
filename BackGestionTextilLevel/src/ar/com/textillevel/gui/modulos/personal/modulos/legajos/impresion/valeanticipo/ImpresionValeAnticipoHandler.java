package ar.com.textillevel.gui.modulos.personal.modulos.legajos.impresion.valeanticipo;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.util.DateUtil;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.JasperHelper;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.vale.ValeAnticipo;

public class ImpresionValeAnticipoHandler {

	private static final String ARCHIVO_JASPER = "/ar/com/textillevel/reportes/vale_anticipo.jasper";
	private final ValeAnticipo valeAnticipo;
	private final Window owner;
	
	public ImpresionValeAnticipoHandler(ValeAnticipo valeAnticipo, Frame owner) {
		this.valeAnticipo = valeAnticipo;
		this.owner = owner;
	}
	
	public ImpresionValeAnticipoHandler(ValeAnticipo valeAnticipo, Dialog owner) {
		this.valeAnticipo = valeAnticipo;
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
				JasperReport reporte = JasperHelper.loadReporte(ARCHIVO_JASPER);
				try {
					JasperPrint jasperPrint = JasperHelper.fillReport(reporte, getParameters(valeAnticipo), Collections.singletonList(valeAnticipo));
					Integer cantidadAImprimir = Integer.valueOf(input);
					JasperHelper.imprimirReporte(jasperPrint, true, true, cantidadAImprimir);
				} catch (JRException e) {
					e.printStackTrace();
				}
			}
		} while (!ok);
	}

	private Map<String, Object> getParameters(ValeAnticipo valeAnticipo) {
		Map<String, Object> mapa = new HashMap<String, Object>();
		mapa.put("USUARIO", valeAnticipo.getUsuarioLogueado());
		mapa.put("NOMBRE_EMPLEADO", valeAnticipo.getLegajo().getEmpleado().toString());
		mapa.put("CONCEPTO", valeAnticipo.getConcepto());
		mapa.put("TOTAL", GenericUtils.getDecimalFormat().format(valeAnticipo.getMonto().doubleValue()));
		mapa.put("MONTO_LETRAS", valeAnticipo.getMontoLetras());
		mapa.put("DIA_FECHA", String.valueOf(DateUtil.getDia(valeAnticipo.getFecha())));
		mapa.put("MES_FECHA", String.valueOf(DateUtil.getMes(valeAnticipo.getFecha())));
		mapa.put("ANIO_FECHA", String.valueOf(DateUtil.getAnio(valeAnticipo.getFecha())));
		return mapa;
	}

}