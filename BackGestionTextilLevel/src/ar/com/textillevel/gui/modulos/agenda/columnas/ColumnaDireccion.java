package ar.com.textillevel.gui.modulos.agenda.columnas;

import ar.com.fwcommon.templates.modulo.model.tabla.ColumnaString;
import ar.com.textillevel.entidades.gente.IAgendable;

public class ColumnaDireccion extends ColumnaString<IAgendable> {

	public ColumnaDireccion() {
		super("DIRECCION");
		setAncho(110);
	}

	@Override
	public String getValor(IAgendable item) {
		return item.getDireccion();
	}
}
