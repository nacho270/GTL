package ar.com.textillevel.gui.modulos.stock.columnas.commons;

import ar.com.fwcommon.templates.modulo.model.tabla.ColumnaFloat;
import ar.com.textillevel.entidades.ventas.materiaprima.ItemMateriaPrimaTO;

public class ColumnaPrecioMateriaPrimaStockActual extends ColumnaFloat<ItemMateriaPrimaTO>{

	public ColumnaPrecioMateriaPrimaStockActual() {
		super("Stock actual");
		setAncho(100);
	}

	@Override
	public Float getValor(ItemMateriaPrimaTO item) {
		return item.getStock()==null?0f:item.getStock().floatValue();
	}
}
