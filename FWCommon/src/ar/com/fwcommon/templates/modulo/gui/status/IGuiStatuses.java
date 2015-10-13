package ar.com.fwcommon.templates.modulo.gui.status;

import ar.com.fwcommon.templates.modulo.gui.meta.IGuiSet;
import ar.com.fwcommon.templates.modulo.model.status.Status;
import ar.com.fwcommon.templates.modulo.model.status.Statuses;


public interface IGuiStatuses<T> extends IGuiSet<T, Status<T>> {
	
	/**
	 * Devuelve el modelo de la GUI
	 * @return Modelo con los statuses
	 */
	public Statuses<T> getModel();
	
	/**
	 * Establece el modelo de la GUI
	 * @param statuses Modelo con los statuses
	 */
	public void setModel(Statuses<T> statuses);

}
