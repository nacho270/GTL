package ar.com.fwcommon.templates.modulo.gui.acciones;

import ar.com.fwcommon.templates.modulo.gui.meta.IGuiSet;
import ar.com.fwcommon.templates.modulo.model.acciones.Accion;
import ar.com.fwcommon.templates.modulo.model.acciones.Acciones;
import ar.com.fwcommon.templates.modulo.model.listeners.AccionExecutedListener;

/**
 * GUI que contiene todas las acciones
 * 
 * 
 *
 * @param <T> Tipo de datos que van a recibir las acciones
 */
public interface IGuiAcciones<T> extends IGuiSet<T, Accion<T>> {

	/**
	 * Devuelve el modelo de la GUI
	 * @return Modelo con las Acciones
	 */
	public Acciones<T> getModel();

	/**
	 * Establece el modelo de la GUI
	 * @param acciones Modelo con las Acciones
	 */
	public void setModel(Acciones<T> acciones);

	/**
	 * Agrega un listener que notifica cuando se ejecutó una acción
	 * @param listener Listener a agregar
	 */
	public void addAccionExecutedListener(AccionExecutedListener listener);

	/**
	 * Quita un listener que notifica cuando se ejecutó una acción
	 * @param listener Listener a quitar
	 */
	public void removeAccionExecutedListener(AccionExecutedListener listener);

}