package ar.com.fwcommon.templates.modulo.model.tabla;

import ar.com.fwcommon.componentes.FWJTable;

public abstract class ColumnaFloat<T> extends Columna<T>{

	public ColumnaFloat(String nombre) {
		super(nombre, TIPO_FLOAT );
		setAncho(50);
		setAlineacion(FWJTable.RIGHT_ALIGN);
	}
	
	@Override
	public abstract Float getValor(T item);

}
