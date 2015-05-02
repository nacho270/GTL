package ar.com.textillevel.gui.modulos.personal.modulos.legajos.columnas;

import ar.clarin.fwjava.componentes.CLJTable;
import ar.clarin.fwjava.templates.modulo.model.tabla.ColumnaString;
import ar.com.textillevel.modulos.personal.entidades.legajos.Empleado;

public class ColumnaCUITLegajo extends ColumnaString<Empleado>{

	public ColumnaCUITLegajo() {
		super("C.U.I.T");
		setAlineacion(CLJTable.CENTER_ALIGN);
	}

	@Override
	public String getValor(Empleado item) {
		return item.getDocumentacion().getCuit();
	}
}
