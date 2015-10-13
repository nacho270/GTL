package ar.com.textillevel.gui.modulos.personal.modulos.legajos.columnas;

import ar.com.fwcommon.componentes.FWJTable;
import ar.com.fwcommon.templates.modulo.model.tabla.ColumnaString;
import ar.com.textillevel.modulos.personal.entidades.legajos.Empleado;

public class ColumnaTelefonoEmpleado extends ColumnaString<Empleado> {

	public ColumnaTelefonoEmpleado() {
		super("Teléfono");
		setAlineacion(FWJTable.CENTER_ALIGN);
	}

	@Override
	public String getValor(Empleado item) {
		return item.getUltimoDomicilio().getTelefono();
	}

}
