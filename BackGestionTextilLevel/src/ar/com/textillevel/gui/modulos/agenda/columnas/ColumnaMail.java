package ar.com.textillevel.gui.modulos.agenda.columnas;

import ar.clarin.fwjava.templates.modulo.model.tabla.ColumnaString;
import ar.com.textillevel.entidades.gente.IAgendable;

public class ColumnaMail extends ColumnaString<IAgendable> {

	public ColumnaMail() {
		super("E-MAIL");
	}

	@Override
	public String getValor(IAgendable item) {
		return item.getEmail();
	}
}
