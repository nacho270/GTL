package ar.com.textillevel.gui.modulos.stock.columnas.telas;

import ar.clarin.fwjava.templates.modulo.model.tabla.ColumnaFloat;
import ar.com.textillevel.entidades.ventas.materiaprima.ItemMateriaPrimaTO;
import ar.com.textillevel.entidades.ventas.materiaprima.ItemMateriaPrimaTelaTO;
import ar.com.textillevel.entidades.ventas.materiaprima.Tela;

public class ColumnaStockGramaje extends ColumnaFloat<ItemMateriaPrimaTO>{

	public ColumnaStockGramaje() {
		super("Gramaje");
		setAncho(90);
	}

	@Override
	public Float getValor(ItemMateriaPrimaTO item) {
		if(item.getMateriaPrima()!=null){
			return ((Tela)item.getMateriaPrima()).getArticulo().getGramaje().floatValue();
		}else{
			ItemMateriaPrimaTelaTO itemTO = (ItemMateriaPrimaTelaTO)item;
			return itemTO.getArticulo().getGramaje().floatValue();
		}
	}
}
