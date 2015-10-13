package ar.com.fwcommon.templates.modulo.model.filtros;

/**
 * Construye los filtros para un determinado modelo 
 * 
 * 
 *
 * @param <T> Elementos que se van a filtrar
 */
public interface IBuilderFiltros<T> {

	/**
	 * Construye los filtros
	 * 
	 * @param idModel Identificador del modelo para el que se generan los
	 *            filtros
	 * @return Filtros construidos
	 */
	public Filtros<T> construirFiltros(int idModel);
}
