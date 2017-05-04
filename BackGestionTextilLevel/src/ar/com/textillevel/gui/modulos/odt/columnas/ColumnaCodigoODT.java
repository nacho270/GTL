package ar.com.textillevel.gui.modulos.odt.columnas;

import ar.com.fwcommon.templates.modulo.model.tabla.ColumnaString;
import ar.com.textillevel.modulos.odt.to.ODTTO;

public class ColumnaCodigoODT extends ColumnaString<ODTTO>{

	public ColumnaCodigoODT() {
		super("Código");
	}

	@Override
	public String getValor(ODTTO item) {
		return item.getCodigo();
	}
}
