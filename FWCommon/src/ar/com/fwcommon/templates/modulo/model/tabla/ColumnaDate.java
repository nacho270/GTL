package ar.com.fwcommon.templates.modulo.model.tabla;

import java.util.Date;

import ar.com.fwcommon.componentes.FWJTable;

public abstract class ColumnaDate<T> extends Columna<T> {

	public ColumnaDate(String nombre) {
		super(nombre, TIPO_DATE);		
		setAncho(100);
		setAlineacion(FWJTable.CENTER_ALIGN);
	}
	
	@Override
	public abstract Date getValor(T item);

}