package ar.com.textillevel.gui.modulos.cheques.columnas;

import ar.clarin.fwjava.templates.modulo.model.tabla.ColumnaFloat;
import ar.com.textillevel.entidades.cheque.Cheque;

public class ColumnaImporteCheque extends ColumnaFloat<Cheque>{

	public ColumnaImporteCheque() {
		super("Importe");
		setAncho(100);
	}

	@Override
	public Float getValor(Cheque item) {
		return item.getImporte().floatValue();
	}
}
