package ar.com.textillevel.gui.modulos.odt.columnas;

import ar.clarin.fwjava.templates.modulo.model.tabla.ColumnaString;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;

public class ColumnaClienteODT extends ColumnaString<OrdenDeTrabajo>{

	public ColumnaClienteODT() {
		super("Cliente");
	}

	@Override
	public String getValor(OrdenDeTrabajo item) {
		return item.getRemito().getCliente().getDescripcionResumida();
	}

}
