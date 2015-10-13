package ar.com.fwcommon.templates.modulo.model.listeners;

import javax.swing.event.ChangeEvent;

/**
 * Evento que notifica cual fue el cambio producido
 * 
 * 
 */
@SuppressWarnings("serial")
public class ModelChangeEvent extends ChangeEvent {
	private final int eventType;
	
	/**
	 * Crea un evento de cambio de Modelo
	 * 
	 * @param source Quien originó el evento
	 * @param eventType Tipo de evento
	 */
	public ModelChangeEvent(Object source, int eventType) {
		super(source);
		this.eventType = eventType;
	}

	/**
	 * Devuelve el tipo de evento
	 * 
	 * @return Tipo de evento
	 */
	public int getEventType() {
		return eventType;
	}
}
