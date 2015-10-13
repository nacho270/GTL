package ar.com.fwcommon.templates.modulo.model.listeners;

import java.util.EventListener;

/**
 * Interfaz del un Listener para recibir notificaciones. Este listener indica
 * que ha cambiado el estado del modelo
 * 
 * 
 */
public interface ModelChangeListener extends EventListener{
	
	/**
	 * Invocado cuando cambió parte del modelo
	 */
	public void stateChanged(ModelChangeEvent e);	
}
