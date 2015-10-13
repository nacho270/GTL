package ar.com.textillevel.gui.modulos.agenda.columnas;

import ar.com.fwcommon.templates.modulo.model.tabla.ColumnaString;
import ar.com.textillevel.entidades.gente.IAgendable;

public class ColumnaCelular extends ColumnaString<IAgendable> {

	public ColumnaCelular() {
		super("CELULAR");
		setAncho(80);
	}

	@Override
	public String getValor(IAgendable item) {
		return item.getCelular();
	}
}
