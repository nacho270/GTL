package ar.com.fwcommon.templates.modulo.model.tabla;

/**
 * Encargado de manejar el orden de las columnas dentro del módulo.
 * 
 * 
 */
public interface TablaColumnOrderPersister {
	/**
	 * Persiste el orden de las columnas
	 * 
	 * @param columnPermutation Orden de las columnas
	 */
	public void saveColumnPermutation(int[] columnPermutation);

	/**
	 * Recupera el orden de las columnas.
	 * 
	 * @return Orden de las columnas. <code>null</code> si no tiene ninguno
	 */
	public int[] loadColumnPermutation();
}
