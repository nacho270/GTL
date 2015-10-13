package ar.com.fwcommon.templates.modulo.model.tabla;

import ar.com.fwcommon.componentes.FWJTable;



public abstract class ColumnaString<T> extends Columna<T> {

	public ColumnaString(String nombre) {
		super(nombre, TIPO_STRING);
		setAncho(100);
		setAlineacion(FWJTable.LEFT_ALIGN);
	}

	@Override
	public abstract String getValor(T item);


}
