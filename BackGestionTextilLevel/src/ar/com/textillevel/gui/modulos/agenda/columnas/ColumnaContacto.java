package ar.com.textillevel.gui.modulos.agenda.columnas;

import ar.com.fwcommon.templates.modulo.model.tabla.ColumnaString;
import ar.com.textillevel.entidades.gente.IAgendable;

public class ColumnaContacto extends ColumnaString<IAgendable> {

	public ColumnaContacto() {
		super("CONTACTO");
	}

	@Override
	public String getValor(IAgendable item) {
		return item.getContacto();
	}
}
