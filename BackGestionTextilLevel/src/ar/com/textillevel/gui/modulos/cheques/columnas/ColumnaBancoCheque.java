package ar.com.textillevel.gui.modulos.cheques.columnas;

import ar.clarin.fwjava.templates.modulo.model.tabla.ColumnaString;
import ar.com.textillevel.entidades.cheque.Cheque;

public class ColumnaBancoCheque extends ColumnaString<Cheque>{

	public ColumnaBancoCheque() {
		super("Banco");
		setAncho(180);
	}

	@Override
	public String getValor(Cheque item) {
		return item.getBanco().getNombre();
	}
}
