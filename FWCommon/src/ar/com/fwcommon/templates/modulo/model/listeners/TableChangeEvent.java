package ar.com.fwcommon.templates.modulo.model.listeners;

import java.util.EventObject;
import java.util.List;

/**
 * Evento que notifica cual fue el cambio producido
 * 
 * 
 */
@SuppressWarnings("serial")
public class TableChangeEvent extends EventObject {

	public static final int TYPE_DATA_CHANGE = 0;
	public static final int TYPE_COLUMN_CHANGE = 1;
	public static final int TYPE_COLUMN_GROUP_CHANGE = 2;
	public static final int TYPE_ANALISIS_CHANGE = 3;
	public static final int TYPE_SELECTION_MODE_CHANGE = 4;
	public static final int TYPE_FILL_MODE_CHANGE = 5;
	
	private final int eventType;
	private final List<?> selectedObjects;
	
	/**
	 * Crea un evento de cambio de tabla
	 * 
	 * @param source Quien originó el evento
	 * @param eventType Tipo de evento (ver {@link #getEventType()})
	 * #getEventType()
	 */
	public TableChangeEvent(Object source, int eventType) {
		this(source, eventType, null);
	}
	
	/**
	 * Crea un evento de cambio de tabla
	 * 
	 * @param source Quien originó el evento
	 * @param eventType Tipo de evento (ver {@link #getEventType()})
	 * @param selectedObjects Elementos que deben quedar seleccionados en la tabla
	 * #getEventType()
	 */
	public TableChangeEvent(Object source, int eventType, List<?> selectedObjects) {
		super(source);
		this.eventType = eventType;
		this.selectedObjects = selectedObjects;
	}

	/**
	 * Devuelve el tipo de evento. Los tipos de eventos son:
	 * <lu>
	 * 	<li><code>TYPE_DATA_CHANGE</code>: Si cambió la información de la tabla
	 *  <li><code>TYPE_COLUMN_CHANGE</code>: Si cambió la estructura de columnas
	 *  <li><code>TYPE_COLUMN_GROUP_CHANGE</code>: Si cambió la estructura de grupos de columnas 
	 *  <li><code>TYPE_ANALISIS_CHANGE</code>: Si cambió algo relacionado con el modo análisis
	 *  <li><code>TYPE_SELECTION_MODE_CHANGE</code>: Si cambió el modo de selección
	 *  <li><code>TYPE_FILL_MODE_CHANGE</code>: Si cambió el modo de llenado de la tabla (Fila/Columna)
	 * </lu>
	 * @return Tipo de evento
	 */
	public int getEventType() {
		return eventType;
	}

	/**
	 * Devuelve la lista de objetos que debe quedar seleccionada en la tabla o
	 * <code>null</code> si se quiere mantener la selección actual
	 * 
	 * @return Lista de objetos a seleccionar o <code>null</code> si se desea
	 *         mantener la selección actual
	 */
	public List<?> getSelectedObjects() {
		return selectedObjects;
	}
}
