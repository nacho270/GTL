package ar.com.textillevel.gui.modulos.cheques.columnas;

import ar.clarin.fwjava.templates.modulo.model.tabla.ColumnaString;
import ar.com.textillevel.entidades.cheque.Cheque;

public class ColumnaNumeracionCheque extends ColumnaString<Cheque>{

	public ColumnaNumeracionCheque() {
		super("Numeraci�n");
		setAncho(80);
	}

	@Override
	public String getValor(Cheque item) {
		return item.getNumeracion().toString();
	}
}
