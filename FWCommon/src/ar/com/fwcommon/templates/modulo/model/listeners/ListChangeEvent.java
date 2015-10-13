package ar.com.fwcommon.templates.modulo.model.listeners;

import java.util.EventObject;
import java.util.List;

/**
 * Evento que notifica cual fue el cambio producido
 * 
 * 
 */
public class ListChangeEvent extends EventObject {
	private static final long serialVersionUID = -9121807835983116970L;
	private List<? extends Object> elements;
	
	public ListChangeEvent(Object source, List<? extends Object> elements) {
		super(source);
		this.elements = elements;
	}

	/**
	 * Elementos que fueron afectados por el cambio
	 * 
	 * @return Lista inmutable de elementos que fueron afectados por el cambio
	 */
	public List<? extends Object> getElements() {
		return elements;
	}
}
