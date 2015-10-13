package ar.com.textillevel.gui.modulos.personal.modulos.legajos.impresion.contratos.strategy;

import java.util.Map;

import ar.com.fwcommon.templates.main.AbstractMainTemplate;
import ar.com.textillevel.gui.modulos.personal.modulos.legajos.gui.JDialogInputFirmante;
import ar.com.textillevel.modulos.personal.entidades.legajos.Empleado;
import ar.com.textillevel.modulos.personal.entidades.legajos.HorarioDia;

public abstract class StrategyCreacionContrato {
	
	public abstract Map<String, Object> crearContrato(Empleado empleado);
	
	protected String obtenerPersonaFirma() {
		JDialogInputFirmante dialog = new JDialogInputFirmante(AbstractMainTemplate.getFrameInstance());
		dialog.setVisible(true);
		if(dialog.getResultado()!=null){
			return dialog.getResultado();
		}
		return null;
	}

	protected String generarStringHorario(Empleado empleado){
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i< empleado.getLegajo().getHorario().size();i++){
			HorarioDia hd =  empleado.getLegajo().getHorario().get(i);
			if(i>0 && i== empleado.getLegajo().getHorario().size()-1){
				sb.append(" y " );
			}
			sb.append(hd.getRangoHorario().toString());
			if(hd.getRangoDias().getDiaDesde().getNroDia() == hd.getRangoDias().getDiaHasta().getNroDia()){
				sb.append(" los " );
				String nombreDia = hd.getRangoDias().getDiaDesde().getNombre();
				if(!nombreDia.toLowerCase().endsWith("s")){
					nombreDia+="s";
				}
				sb.append(nombreDia);
			}else{
				sb.append(" de " + hd.getRangoDias().toString());
			}
			if(empleado.getLegajo().getHorario().size() > 1 && (i+1)< empleado.getLegajo().getHorario().size()-1){
				sb.append(", " );
			}
		}
		return sb.toString();
	}
}
