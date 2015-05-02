package ar.com.textillevel.gui.modulos.agenda.columnas;

import ar.clarin.fwjava.templates.modulo.model.tabla.ColumnaInt;
import ar.com.textillevel.entidades.gente.IAgendable;

public class ColumnaCodigoPostal extends ColumnaInt<IAgendable> {

	public ColumnaCodigoPostal() {
		super("CP");
		setAncho(40);
	}

	@Override
	public Integer getValor(IAgendable item) {
		return item.getCodigoPostal();
	}
}
