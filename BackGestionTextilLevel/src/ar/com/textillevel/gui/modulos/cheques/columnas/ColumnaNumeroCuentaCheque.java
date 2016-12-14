package ar.com.textillevel.gui.modulos.cheques.columnas;

import ar.com.fwcommon.templates.modulo.model.tabla.ColumnaString;
import ar.com.textillevel.entidades.cheque.Cheque;

public class ColumnaNumeroCuentaCheque extends ColumnaString<Cheque>{

	public ColumnaNumeroCuentaCheque() {
		super("Cta.");
		setAncho(60);
	}

	@Override
	public String getValor(Cheque item) {
		return item.getNroCuenta();
	}

}
