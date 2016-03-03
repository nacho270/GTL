package ar.com.textillevel.gui.modulos.odt.columnas;

import ar.com.fwcommon.templates.modulo.model.tabla.ColumnaBoolean;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;

public class ColumnaSecuenciaAsignada extends ColumnaBoolean<OrdenDeTrabajo> {

	public ColumnaSecuenciaAsignada() {
		super("Tiene secuencia");
		setAncho(110);
	}

	@Override
	public Boolean getValor(OrdenDeTrabajo item) {
		return item.getSecuenciaDeTrabajo() != null;
	}

}
