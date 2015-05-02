package ar.com.textillevel.gui.modulos.personal.modulos.legajos.gui;

import ar.com.textillevel.modulos.personal.entidades.legajos.AccionHistorica;

/**
 * Clase que encapsula un evento de cambio de orden de elementos.
 * @author Dromero
 * @param <E>
 */
public class AccionHistoricaEvent {

	private AccionHistorica ah;

	public AccionHistorica getAh() {
		return ah;
	}

	public void setAh(AccionHistorica ah) {
		this.ah = ah;
	}

	public AccionHistoricaEvent(AccionHistorica ah) {
		this.ah = ah;
	}

}