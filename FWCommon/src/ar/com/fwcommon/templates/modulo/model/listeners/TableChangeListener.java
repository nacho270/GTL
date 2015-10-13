package ar.com.fwcommon.templates.modulo.model.listeners;

import java.util.EventListener;

/**
 * Interfaz del un Listener para recibir notificaciones. Este listener indica
 * que ha cambiado el estado la tabla
 * 
 * 
 */
public interface TableChangeListener extends EventListener {
	
	/**
	 * Invocado cuando cambió la estructura de la tabla
	 */
	public void structureChange(TableChangeEvent e);
	
	/**
	 * Invocado cuando cambiaron los elementos de una forma compleja
	 */
	public void dataChange(TableChangeEvent e);

}
