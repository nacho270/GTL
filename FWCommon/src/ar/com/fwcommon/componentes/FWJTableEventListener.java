package ar.com.fwcommon.componentes;

public interface FWJTableEventListener {

	/** Para cuando se selecciona una fila */
	public void newRowSelected(int newRow, int oldRow);

	/** Para cuando se selecciona una columna */
	public void newColumnSelected(int newCol, int oldCol);

	/**
	 * Manejo del evento de edición de una celda de la tabla.
	 * @param cell
	 * @param row
	 */	
	public void cellEdited(int col, int row);

	/** Manejo del evento de click en la cabecera de la tabla */
	public void singleClickHeader();

	/** Manejo del evento de doble click en la cabecera de la tabla */
	public void doubleClickHeader();

}