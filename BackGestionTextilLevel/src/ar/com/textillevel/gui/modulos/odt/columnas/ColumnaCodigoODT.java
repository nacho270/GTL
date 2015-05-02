package ar.com.textillevel.gui.modulos.odt.columnas;

import ar.clarin.fwjava.templates.modulo.model.tabla.ColumnaString;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;
import ar.com.textillevel.util.ODTCodigoHelper;

public class ColumnaCodigoODT extends ColumnaString<OrdenDeTrabajo>{

	public ColumnaCodigoODT() {
		super("Código");
	}

	@Override
	public String getValor(OrdenDeTrabajo item) {
		return ODTCodigoHelper.getInstance().formatCodigo(item.getCodigo());
	}
}
