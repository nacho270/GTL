package ar.com.textillevel.gui.modulos.cheques.columnas;

import java.util.Date;

import ar.com.fwcommon.templates.modulo.model.tabla.ColumnaDate;
import ar.com.textillevel.entidades.cheque.Cheque;

public class ColumnaFechaDepositoCheque extends ColumnaDate<Cheque>{

	public ColumnaFechaDepositoCheque() {
		super("Fecha de deposito");
		setAncho(130);
	}

	@Override
	public Date getValor(Cheque item) {
		return new Date(item.getFechaDeposito().getTime());
	}
}
