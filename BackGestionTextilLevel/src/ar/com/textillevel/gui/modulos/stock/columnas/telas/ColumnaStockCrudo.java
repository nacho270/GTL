package ar.com.textillevel.gui.modulos.stock.columnas.telas;

import ar.clarin.fwjava.templates.modulo.model.tabla.ColumnaFloat;
import ar.com.textillevel.entidades.ventas.materiaprima.ItemMateriaPrimaTO;
import ar.com.textillevel.entidades.ventas.materiaprima.ItemMateriaPrimaTelaTO;

public class ColumnaStockCrudo extends ColumnaFloat<ItemMateriaPrimaTO>{

	public ColumnaStockCrudo() {
		super("Stock crudo");
		setAncho(100);
	}

	@Override
	public Float getValor(ItemMateriaPrimaTO item) {
		ItemMateriaPrimaTelaTO itemTela = (ItemMateriaPrimaTelaTO)item;
		return itemTela.getStockCrudo()==null?0:itemTela.getStockCrudo().floatValue();
	}
}
