package ar.com.fwcommon.templates.modulo.gui.totales;

import ar.com.fwcommon.templates.modulo.gui.meta.IGuiSet;
import ar.com.fwcommon.templates.modulo.model.totales.Total;
import ar.com.fwcommon.templates.modulo.model.totales.Totales;

/**
 * GUI que agrupa a todos los totales
 *  
 * 
 */
public interface IGuiTotales<T> extends IGuiSet<T, Total<T>> {

	/**
	 * Devuelve el modelo de la GUI
	 * @return Modelo con los totales
	 */
	public Totales<T> getModel();

	/**
	 * Establece el modelo de la GUI
	 * @param totales Modelo con los totales
	 */
	public void setModel(Totales<T> totales);

}