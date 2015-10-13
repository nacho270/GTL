package ar.com.fwcommon.templates.modulo.model.acciones;

import ar.com.fwcommon.componentes.error.FWException;

/**
 * Construye las acciones que tendrá un determinado modelo
 * 
 * 
 * 
 * @param <T> Elementos que recibirán las acciones
 */
public interface IBuilderAcciones<T> {

	/**
	 * Construye las acciones
	 * 
	 * @param idModel Identificador del modelo para el que se generan las
	 *            acciones
	 * @return Acciones construidas
	 * @throws FWException 
	 */
	public Acciones<T> construirAcciones(int idModel) throws FWException;
}
