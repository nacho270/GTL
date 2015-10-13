package ar.com.fwcommon.templates.modulo.gui.filtros;

import ar.com.fwcommon.templates.modulo.gui.meta.IGuiSet;
import ar.com.fwcommon.templates.modulo.model.filtros.Filtro;
import ar.com.fwcommon.templates.modulo.model.filtros.Filtros;

/**
 * Gui que agrupa todos los filtros
 * 
 * 
 * @param <T> Tipo de datos que va a filtrar (datos de la tabla)
 */
public interface IGuiFiltros<T> extends IGuiSet<T, Filtro<T, ?>> {

	/**
	 * Devuelve el modelo de la GUI
	 * @return Modelo de los filtros
	 */
	public Filtros<T> getModel();

	/**
	 * Establece el modelo de la GUI
	 * @param filtros Modelo de los filtros
	 */
	public void setModel(Filtros<T> filtros);

}