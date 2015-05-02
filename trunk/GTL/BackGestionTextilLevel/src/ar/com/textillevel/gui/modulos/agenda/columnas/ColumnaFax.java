package ar.com.textillevel.gui.modulos.agenda.columnas;

import ar.clarin.fwjava.templates.modulo.model.tabla.ColumnaString;
import ar.com.textillevel.entidades.gente.IAgendable;

public class ColumnaFax extends ColumnaString<IAgendable>{

	public ColumnaFax() {
		super("FAX");
		setAncho(80);
	}

	@Override
	public String getValor(IAgendable item) {
		return item.getFax();
	}
}
