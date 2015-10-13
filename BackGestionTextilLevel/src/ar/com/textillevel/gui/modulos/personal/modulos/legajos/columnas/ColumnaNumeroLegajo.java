package ar.com.textillevel.gui.modulos.personal.modulos.legajos.columnas;

import ar.com.fwcommon.componentes.FWJTable;
import ar.com.fwcommon.templates.modulo.model.tabla.ColumnaInt;
import ar.com.textillevel.modulos.personal.entidades.legajos.Empleado;

public class ColumnaNumeroLegajo extends ColumnaInt<Empleado>{

	public ColumnaNumeroLegajo() {
		super("Nº de legajo");
		setAncho(100);
		setAlineacion(FWJTable.CENTER_ALIGN);
	}

	@Override
	public Integer getValor(Empleado item) {
		return item.getLegajo()!=null?item.getLegajo().getNroLegajo():null;
	}
}
