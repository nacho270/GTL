package ar.com.fwcommon.templates.modulo.model.totales;

/**
 * Construye los totales para un determinado modelo
 * 
 * 
 *
 * @param <T> Elementos a totalizar
 */
public interface IBuilderTotales<T> {

	/**
	 * Construye los totales
	 * 
	 * @param idModel Identificador del modelo para el que se generan los
	 *            totales
	 * @return Totales construidos
	 */
	public Totales<T> construirTotales(int idModel);
}
