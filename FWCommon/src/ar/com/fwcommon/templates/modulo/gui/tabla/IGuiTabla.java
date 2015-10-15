package ar.com.fwcommon.templates.modulo.gui.tabla;

import java.awt.Component;
import java.util.List;

import ar.com.fwcommon.componentes.FWJTableAnalisis;
import ar.com.fwcommon.templates.modulo.model.tabla.Tabla;

public interface IGuiTabla<T> {

	/**
	 * Devuelve el modelo de la GUI
	 * @return Modelo de la Tabla
	 */
	public Tabla<T> getModel();

	/**
	 * Establece el modelo de la GUI
	 * @param model Modelo de la tabla
	 */
	public void setModel(Tabla<T> model);

	/**
	 * Devuelve el componente Swing a mostrar
	 * @return Componente a mostrar
	 */
	public Component getComponent();
	
	/**
	 * Devuelve el componente de la tabla utilizado para mostrar los datos.
	 * <p>
	 * <u><b>IMPORTANTE:</b></u><br>
	 * El componente puede utilizarse para consultar información, agregar
	 * listeners, etc., pero bajo <u>ninguna circunstancia</u> se debe
	 * modificar el estado del mismo (ya sean datos y/o propiedades del mismo). <br>
	 * Lo único que se permite modificar es la selección de la misma
	 * 
	 * @return Componente de la tabla
	 */
	public FWJTableAnalisis getJTable();

	/**
	 * Dice si hau alguna fila seleccionada o no
	 * 
	 * @return <code>true</code> si hay al menos una fila seleccionada.
	 *         <code>false</code> en caso contrario
	 */
	public boolean hayFilaSeleccionada();

	/**
	 * Devuelve el objeto que se encuentra en una fila determinada
	 * @param fila Fila en la que se encuentra el objeto
	 * @return Objeto correspondiente a dicha fila
	 */
	public T getObjeto(int fila);

	/**
	 * Devuelve el item seleccionado en la tabla.
	 * @return El item seleccionado.
	 */
	public T getObjetoSeleccionado();

	/**
	 * Devuelve una lista con los items seleccionados en la tabla.
	 * @return itemsTabla La lista de items seleccionados.
	 */
	public List<T> getObjetosSeleccionados();

}