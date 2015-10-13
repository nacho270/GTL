package ar.com.fwcommon.templates.modulo.model.listeners;

import java.util.EventListener;

/**
 * Interfaz del un Listener para recibir notificaciones. Este listener indica
 * que ha cambiado el estado de algún contenedor de objetos
 * 
 * 
 */
public interface ListChangeListener extends EventListener {
	/**
	 * Invocado cuando se han agregado elementos
	 */
	public void elementsAdded(ListChangeEvent e);

	/**
	 * Invocado cuando se han quitado elementos
	 */
	public void elementsRemoved(ListChangeEvent e);
}
