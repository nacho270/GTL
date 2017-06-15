package ar.com.textillevel.gui.modulos.stock.columnas.telas;

import ar.com.fwcommon.templates.modulo.model.tabla.ColumnaFloat;
import ar.com.textillevel.entidades.ventas.materiaprima.ItemMateriaPrimaTO;

public class ColumnaStockGramaje extends ColumnaFloat<ItemMateriaPrimaTO> {

	public ColumnaStockGramaje() {
		super("Gramaje");
		setAncho(90);
	}

	@Override
	public Float getValor(ItemMateriaPrimaTO item) {
		return null;
	}

}
