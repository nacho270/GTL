package ar.com.fwcommon.templates.modulo.model.listeners;

import java.util.EventObject;
import java.util.List;

import ar.com.fwcommon.templates.modulo.ModuloTemplate;

/**
 * Evento enviado a las acciones
 * 
 *
 * @param <T> Tipo de datos que se manejan en el módulo template
 */
@SuppressWarnings("serial")
public class AccionEvent<T> extends EventObject {
	private final List<T> selectedElements;
	
	public AccionEvent(ModuloTemplate<T, ?> source, List<T> selectedElements) {
		super(source);
		this.selectedElements = selectedElements;
	}

	/**
	 * Devuelve el {@link ModuloTemplate} a la que pertenece la acción
	 * @return {@link ModuloTemplate} a la que pertenece la acción
	 * ModuloTemplate
	 */
	@Override
	@SuppressWarnings("unchecked")
	public ModuloTemplate<T, ?> getSource() {
		return (ModuloTemplate<T, ?>)super.getSource();
	}

	/**
	 * Devuelve la lista con los elementos seleccionados
	 * 
	 * @return Lista con los elementos seleccionados
	 */
	public List<T> getSelectedElements() {
		return selectedElements;
	}
}
