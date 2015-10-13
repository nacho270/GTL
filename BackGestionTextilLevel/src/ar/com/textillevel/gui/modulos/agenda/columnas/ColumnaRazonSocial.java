package ar.com.textillevel.gui.modulos.agenda.columnas;

import ar.com.fwcommon.templates.modulo.model.tabla.ColumnaString;
import ar.com.textillevel.entidades.gente.IAgendable;

public class ColumnaRazonSocial extends ColumnaString<IAgendable>{

	public ColumnaRazonSocial() {
		super("RAZON SOCIAL");
		setAncho(200);
	}

	@Override
	public String getValor(IAgendable item) {
		return item.getRazonSocial();
	}
}
