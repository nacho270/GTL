package ar.com.fwcommon.templates.modulo.model.listeners;

import java.util.EventObject;
import java.util.List;

/**
 * Evento que indica que se ejecutó una acción
 * 
 */
@SuppressWarnings("serial")
public class AccionExecutedEvent extends EventObject {
	private final boolean needsRefreshTable;
	private final List<?> selectedObjects;

	public AccionExecutedEvent(Object source,  boolean needsRefreshTable, List<?> selectedObjects) {
		super(source);
		this.needsRefreshTable = needsRefreshTable;
		this.selectedObjects = selectedObjects;
	}

	/**
	 * Dice si luego de ejecutar la acción es necesario recargar la tabla
	 * 
	 * @return <code>true</code> si es necesario recargar la tabla.
	 *         <code>false</code> en caso contrario
	 */
	public boolean isNeedsRefreshTable() {
		return needsRefreshTable;
	}

	/**
	 * Devuelve la lista de los objetos que deben quedar seleccionados luego de
	 * recargar la tabla. <code>null</code> si se quiere el comportamiento
	 * default (dejar seleccionados los que se encontraban seleccionados)
	 * 
	 * @return Lista de objetos que deben quedar seleccionados. <code>null</code> si se quiere 
	 */
	public List<?> getSelectedObjects() {
		return selectedObjects;
	}
}
