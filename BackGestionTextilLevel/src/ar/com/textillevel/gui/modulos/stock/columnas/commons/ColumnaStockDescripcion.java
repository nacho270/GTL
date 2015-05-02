package ar.com.textillevel.gui.modulos.stock.columnas.commons;

import ar.clarin.fwjava.templates.modulo.model.tabla.ColumnaString;
import ar.com.textillevel.entidades.ventas.materiaprima.ItemMateriaPrimaTO;

public class ColumnaStockDescripcion extends ColumnaString<ItemMateriaPrimaTO>{

	public ColumnaStockDescripcion(String nombre) {
		super(nombre);
		setAncho(150);
	}

	@Override
	public String getValor(ItemMateriaPrimaTO item) {
		return item.getMateriaPrima().getDescripcion();
	}
}
