package ar.com.fwcommon.templates.modulo.model.listeners;

import java.util.EventObject;

/**
 * Evento que notifica cuando se produjo un cambio en un filtro
 * 
 * 
 */
public class FilterChangeEvent extends EventObject {
	private static final long serialVersionUID = 7123886932253466606L;
	private Object value;
	
	public FilterChangeEvent(Object source, Object value) {
		super(source);
		this.value = value;
	}

	/**
	 * Devuelve el nuevo valor del filtro
	 * 
	 * @return Valor del filtro
	 */
	public Object getValue() {
		return value;
	}
}
