package ar.com.textillevel.gui.modulos.personal.modulos.legajos.columnas;

import ar.com.fwcommon.templates.modulo.model.tabla.ColumnaString;
import ar.com.textillevel.modulos.personal.entidades.legajos.Empleado;

public class ColumnaNombreApellidoEmpleado extends ColumnaString<Empleado>{

	public ColumnaNombreApellidoEmpleado() {
		super("Nombre y apellido");
		setAncho(150);
	}

	@Override
	public String getValor(Empleado item) {
		return item.getNombre() + " " + item.getApellido();
	}
}
