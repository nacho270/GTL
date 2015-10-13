package ar.com.textillevel.gui.modulos.agenda.columnas;

import ar.com.fwcommon.templates.modulo.model.tabla.ColumnaString;
import ar.com.textillevel.entidades.gente.IAgendable;

public class ColumnaLocalidad extends ColumnaString<IAgendable>{

	public ColumnaLocalidad( ) {
		super("LOCALIDAD");
		setAncho(90);
	}

	@Override
	public String getValor(IAgendable item) {
		return item.getLocalidad();
	}
}
