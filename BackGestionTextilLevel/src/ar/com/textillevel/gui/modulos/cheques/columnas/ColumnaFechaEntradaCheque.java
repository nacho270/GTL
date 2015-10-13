package ar.com.textillevel.gui.modulos.cheques.columnas;

import java.util.Date;

import ar.com.fwcommon.templates.modulo.model.tabla.ColumnaDate;
import ar.com.textillevel.entidades.cheque.Cheque;

public class ColumnaFechaEntradaCheque extends ColumnaDate<Cheque>{

	public ColumnaFechaEntradaCheque() {
		super("Fecha entrada");
		setAncho(130);
	}

	@Override
	public Date getValor(Cheque item) {
		return item.getFechaEntrada();
	}
}
