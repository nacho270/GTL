package ar.com.textillevel.gui.modulos.personal.modulos.legajos.columnas;

import ar.com.fwcommon.templates.modulo.model.tabla.ColumnaString;
import ar.com.textillevel.modulos.personal.entidades.legajos.Empleado;

public class ColumnaSindicatoEmpleado extends ColumnaString<Empleado>{

	public ColumnaSindicatoEmpleado() {
		super("Sindicato");
		setAncho(150);
	}

	@Override
	public String getValor(Empleado item) {
		return item.getLegajo()!=null?item.getLegajo().getPuesto().getSindicato().getNombre():null;
	}
}
