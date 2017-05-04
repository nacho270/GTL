package ar.com.textillevel.gui.modulos.odt.columnas;

import ar.com.fwcommon.templates.modulo.model.tabla.ColumnaString;
import ar.com.textillevel.modulos.odt.to.ODTTO;

public class ColumnaEstadoODT extends ColumnaString<ODTTO> {

	public ColumnaEstadoODT() {
		super("Estado");
	}

	@Override
	public String getValor(ODTTO item) {
		return item.getEstado().getDescripcion();
	}

}
