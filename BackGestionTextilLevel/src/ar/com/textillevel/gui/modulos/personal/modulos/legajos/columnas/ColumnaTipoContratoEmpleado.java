package ar.com.textillevel.gui.modulos.personal.modulos.legajos.columnas;

import ar.clarin.fwjava.templates.modulo.model.tabla.ColumnaString;
import ar.com.textillevel.modulos.personal.entidades.legajos.Empleado;

public class ColumnaTipoContratoEmpleado extends ColumnaString<Empleado>{

	public ColumnaTipoContratoEmpleado() {
		super("Contrato");
	}

	@Override
	public String getValor(Empleado item) {
		return item.getContratoEmpleado()!=null? item.getContratoEmpleado().getContrato().toString():null;
	}
}
