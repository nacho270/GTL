package ar.com.textillevel.gui.modulos.stock.columnas.telas;

import ar.com.fwcommon.templates.modulo.model.tabla.ColumnaFloat;
import ar.com.textillevel.entidades.ventas.materiaprima.ItemMateriaPrimaTO;
import ar.com.textillevel.entidades.ventas.materiaprima.ItemMateriaPrimaTelaTO;

public class ColumnaStockTerminado extends ColumnaFloat<ItemMateriaPrimaTO>{
	
	public ColumnaStockTerminado() {
		super("Stock terminado");
		setAncho(100);
	}

	@Override
	public Float getValor(ItemMateriaPrimaTO item) {
		ItemMateriaPrimaTelaTO itemTela = (ItemMateriaPrimaTelaTO)item;
		return itemTela.getStockTerminado()==null?0:itemTela.getStockTerminado().floatValue();
	}
}
