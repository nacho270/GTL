package ar.com.textillevel.gui.modulos.odt.columnas;

import ar.com.fwcommon.templates.modulo.model.tabla.ColumnaString;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;

public class ColumnaEstadoODT extends ColumnaString<OrdenDeTrabajo>{

	public ColumnaEstadoODT() {
		super("Estado");
	}

	@Override
	public String getValor(OrdenDeTrabajo item) {
		return item.getEstado().getDescripcion();
	}

}
