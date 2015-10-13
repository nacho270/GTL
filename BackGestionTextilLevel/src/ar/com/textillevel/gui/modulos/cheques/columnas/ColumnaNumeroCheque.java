package ar.com.textillevel.gui.modulos.cheques.columnas;

import ar.com.fwcommon.templates.modulo.model.tabla.ColumnaString;
import ar.com.textillevel.entidades.cheque.Cheque;

public class ColumnaNumeroCheque extends ColumnaString<Cheque>{

	public ColumnaNumeroCheque() {
		super("Nro.");
		setAncho(60);
	}

	@Override
	public String getValor(Cheque item) {
		return item.getNumero();
	}

}
