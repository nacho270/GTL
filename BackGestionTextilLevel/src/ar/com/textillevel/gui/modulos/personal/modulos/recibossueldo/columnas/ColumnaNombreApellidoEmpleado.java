package ar.com.textillevel.gui.modulos.personal.modulos.recibossueldo.columnas;

import ar.com.fwcommon.templates.modulo.model.tabla.ColumnaString;
import ar.com.textillevel.modulos.personal.entidades.legajos.Empleado;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.to.InfoReciboSueltoTO;

public class ColumnaNombreApellidoEmpleado extends ColumnaString<InfoReciboSueltoTO> {

	public ColumnaNombreApellidoEmpleado() {
		super("Nombre y apellido");
		setAncho(200);
	}

	@Override
	public String getValor(InfoReciboSueltoTO item) {
		Empleado empleado = item.getLegajo().getEmpleado();
		return empleado.getNombre() + " " + empleado.getApellido();
	}

}
