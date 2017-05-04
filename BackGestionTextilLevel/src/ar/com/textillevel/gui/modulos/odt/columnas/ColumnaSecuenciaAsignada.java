package ar.com.textillevel.gui.modulos.odt.columnas;

import ar.com.fwcommon.templates.modulo.model.tabla.ColumnaBoolean;
import ar.com.textillevel.modulos.odt.to.ODTTO;

public class ColumnaSecuenciaAsignada extends ColumnaBoolean<ODTTO> {

	public ColumnaSecuenciaAsignada() {
		super("Tiene secuencia");
		setAncho(110);
	}

	@Override
	public Boolean getValor(ODTTO item) {
		return item.isTieneSecuencia();
	}

}
