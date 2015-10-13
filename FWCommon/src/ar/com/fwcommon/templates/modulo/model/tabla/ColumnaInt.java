package ar.com.fwcommon.templates.modulo.model.tabla;

import ar.com.fwcommon.componentes.FWJTable;


public abstract class ColumnaInt<T> extends Columna<T> {

	public ColumnaInt(String nombre) {
		super(nombre, TIPO_INT);
		setAncho(50);
		setAlineacion(FWJTable.RIGHT_ALIGN);
	}

	@Override
	public abstract Integer getValor(T item);
}
