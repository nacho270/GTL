package ar.com.textillevel.gui.modulos.agenda.columnas;

import ar.com.fwcommon.templates.modulo.model.tabla.ColumnaString;
import ar.com.textillevel.entidades.gente.IAgendable;

public class ColumnaTelefono extends ColumnaString<IAgendable>{

	public ColumnaTelefono() {
		super("TELEFONO");
		setAncho(80);
	}

	@Override
	public String getValor(IAgendable item) {
		return item.getTelefono();
	}
}
