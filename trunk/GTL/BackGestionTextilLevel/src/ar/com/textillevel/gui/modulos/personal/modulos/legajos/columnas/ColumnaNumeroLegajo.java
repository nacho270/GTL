package ar.com.textillevel.gui.modulos.personal.modulos.legajos.columnas;

import ar.clarin.fwjava.componentes.CLJTable;
import ar.clarin.fwjava.templates.modulo.model.tabla.ColumnaInt;
import ar.com.textillevel.modulos.personal.entidades.legajos.Empleado;

public class ColumnaNumeroLegajo extends ColumnaInt<Empleado>{

	public ColumnaNumeroLegajo() {
		super("Nº de legajo");
		setAncho(100);
		setAlineacion(CLJTable.CENTER_ALIGN);
	}

	@Override
	public Integer getValor(Empleado item) {
		return item.getLegajo()!=null?item.getLegajo().getNroLegajo():null;
	}
}
