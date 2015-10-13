package ar.com.fwcommon.templates.modulo.gui.meta;

import java.awt.Component;

import ar.com.fwcommon.templates.modulo.model.meta.ModelSet;

/**
 * Clase que agrupa a las GUI de agrupadores de models
 * 
 * 
 *
 * @param <T> Elemento que se va a procesar (elemento de la tabla)
 * @param <E> Tipo de Item que se maneja (Accion, Filtro, etc...)
 */
public interface IGuiSet<T, E> extends IGuiPanelObservable {

	/**
	 * Devuelve el modelo de la GUI
	 * @return Modelo
	 */
	public ModelSet<E> getModel();
	
	/**
	 * Devuelve el componente Swing a mostrar
	 * @return Componente a mostrar
	 */
	public Component getComponent();
	
}