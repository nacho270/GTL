package ar.com.textillevel.gui.modulos.cheques.columnas;

import ar.com.fwcommon.templates.modulo.model.tabla.ColumnaString;
import ar.com.textillevel.entidades.cheque.Cheque;

public class ColumnaCapitalOInteriorCheque extends ColumnaString<Cheque>{

	public ColumnaCapitalOInteriorCheque() {
		super("Capital o interior");
		setAncho(60);
	}

	@Override
	public String getValor(Cheque item) {
		return (Character.toLowerCase(item.getCapitalOInterior()) == 'c'?"Capital":"Interior");
	}
}
