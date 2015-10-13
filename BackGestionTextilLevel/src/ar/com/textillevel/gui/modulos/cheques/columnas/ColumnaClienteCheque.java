package ar.com.textillevel.gui.modulos.cheques.columnas;

import ar.com.fwcommon.templates.modulo.model.tabla.ColumnaString;
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
