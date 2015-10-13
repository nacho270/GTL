package ar.com.textillevel.gui.modulos.stock.columnas.telas;

import ar.com.fwcommon.templates.modulo.model.tabla.ColumnaFloat;
import ar.com.textillevel.entidades.ventas.materiaprima.ItemMateriaPrimaTO;
import ar.com.textillevel.entidades.ventas.materiaprima.ItemMateriaPrimaTelaTO;

public class ColumnaStockFisico extends ColumnaFloat<ItemMateriaPrimaTO>{

	public ColumnaStockFisico() {
		super("Stock físico");
		setAncho(100);
	}

	@Override
	public Float getValor(ItemMateriaPrimaTO item) {
		ItemMateriaPrimaTelaTO itemTo = (ItemMateriaPrimaTelaTO)item;
		return itemTo.getStockFisico()==null?0f:itemTo.getStockFisico().floatValue();
	}
}
