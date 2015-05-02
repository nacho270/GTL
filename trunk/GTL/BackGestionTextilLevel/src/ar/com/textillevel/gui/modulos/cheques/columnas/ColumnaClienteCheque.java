package ar.com.textillevel.gui.modulos.cheques.columnas;

import ar.clarin.fwjava.templates.modulo.model.tabla.ColumnaString;
import ar.com.textillevel.entidades.cheque.Cheque;

public class ColumnaClienteCheque extends ColumnaString<Cheque>{

	public ColumnaClienteCheque() {
		super("Cliente");
		setAncho(180);
	}

	@Override
	public String getValor(Cheque item) {
		return item.getCliente().getRazonSocial();
	}
}
