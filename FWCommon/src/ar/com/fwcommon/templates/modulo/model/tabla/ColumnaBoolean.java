package ar.com.fwcommon.templates.modulo.model.tabla;

import ar.com.fwcommon.componentes.FWJTable;

public abstract class ColumnaBoolean<T> extends Columna<T> {

	public ColumnaBoolean(String nombre) {
		super(nombre, TIPO_BOOLEAN);
		setAncho(50);
		setAlineacion(FWJTable.CENTER_ALIGN);
	}
	
	@Override
	public abstract Boolean getValor(T item);

}