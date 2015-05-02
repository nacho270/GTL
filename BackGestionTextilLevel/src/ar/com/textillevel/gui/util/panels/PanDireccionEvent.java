package ar.com.textillevel.gui.util.panels;

import java.util.EventObject;

import ar.com.textillevel.entidades.gente.InfoLocalidad;

public class PanDireccionEvent extends EventObject {
	
	private static final long serialVersionUID = 533068410786317890L;

	private InfoLocalidad infoLocalidad;

	public PanDireccionEvent(Object source, InfoLocalidad infoLocalidad) {
		super(source);
		this.infoLocalidad = infoLocalidad;
	}

	public InfoLocalidad getInfoLocalidad() {
		return infoLocalidad;
	}

}
