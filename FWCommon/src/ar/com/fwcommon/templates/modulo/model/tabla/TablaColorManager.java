package ar.com.fwcommon.templates.modulo.model.tabla;

import java.awt.Color;

/**
 * Clase encargada de suministrar el color con que se debe pintar una fila
 * determinada
 * 
 * 
 */
public interface TablaColorManager<T> {

	/**
	 * Devuelve el color de fondo que se va a aplicar a una fila determinada
	 * 
	 * @param item Elemento correspondiente
	 * @return Color a utilizar para dicha fila. <code>null</code> si se
	 *         quiere utilizar el default
	 */
	public Color getBackgroundColor(T item);
}
