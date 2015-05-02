package ar.com.textillevel.gui.modulos.stock.columnas.telas;

import ar.clarin.fwjava.templates.modulo.model.tabla.ColumnaString;
import ar.com.textillevel.entidades.ventas.materiaprima.ItemMateriaPrimaTO;
import ar.com.textillevel.entidades.ventas.materiaprima.ItemMateriaPrimaTelaTO;
import ar.com.textillevel.entidades.ventas.materiaprima.Tela;

public class ColumnaStockComposicion extends ColumnaString<ItemMateriaPrimaTO>{

	public ColumnaStockComposicion() {
		super("Composicion");
		setAncho(200);
	}

	@Override
	public String getValor(ItemMateriaPrimaTO item) {
		if(item.getMateriaPrima()!=null){
			return ((Tela)item.getMateriaPrima()).getArticulo().getDescripcion();
		}else{
			ItemMateriaPrimaTelaTO itemTO = (ItemMateriaPrimaTelaTO)item;
			return itemTO.getArticulo().getDescripcion();
		}
	}
}

