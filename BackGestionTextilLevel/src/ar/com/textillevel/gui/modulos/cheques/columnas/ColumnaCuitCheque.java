package ar.com.textillevel.gui.modulos.cheques.columnas;

import ar.clarin.fwjava.templates.modulo.model.tabla.ColumnaString;
import ar.com.textillevel.entidades.cheque.Cheque;

public class ColumnaCuitCheque extends ColumnaString<Cheque>{

	public ColumnaCuitCheque() {
		super("CUIT");
		setAncho(90);
	}

	@Override
	public String getValor(Cheque item) {
		return item.getCuit();
	}
}
