package ar.com.textillevel.gui.modulos.stock.cabecera;

import ar.com.fwcommon.templates.modulo.cabecera.Cabecera;

public class CabeceraStock extends Cabecera<ModeloCabeceraStock>{

	private static final long serialVersionUID = 1L;

	@Override
	public ModeloCabeceraStock getModel() {
		return new ModeloCabeceraStock();
	}

}
