package ar.com.fwcommon.templates.modulo.model.tabla;

/**
 * La tabla para un determinado modelo 
 * 
 * 
 *
 * @param <T> Elementos que se van a colocar en la tabla
 */
public interface IBuilderTabla<T> {
	
	/**
	 * Construye la tabla
	 * 
	 * @param idModel Identificador del modelo para el que se genera la tabla
	 * @return Tabla construida
	 */
	public Tabla<T> construirTabla(int idModel);
}
