package ar.com.fwcommon.templates.modulo.model.tabla;

/**
 * Encargado de manejar las columnas ocultas dentro del módulo.
 * 
 * 
 */
public interface TablaColumnVisibilityPersister {
	/**
	 * Persiste el orden de las columnas
	 * 
	 * @param columnHidden Orden de las ocultas
	 */
	public void saveColumnHidden(int[] columnHidden);

	/**
	 * Recupera las columnas ocultas.
	 * 
	 * @return Orden de las columnas ocultas. <code>null</code> si no tiene ninguno
	 */
	public int[] loadColumnHidden();
}
