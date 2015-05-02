package ar.com.textillevel.gui.modulos.stock.columnas.commons;

import ar.clarin.fwjava.templates.modulo.model.tabla.ColumnaString;
import ar.com.textillevel.entidades.ventas.materiaprima.ItemMateriaPrimaTO;

public class ColumnaPrecioMateriaPrimaUnidad extends ColumnaString<ItemMateriaPrimaTO>{

	public ColumnaPrecioMateriaPrimaUnidad() {
		super("Unidad");
	}

	@Override
	public String getValor(ItemMateriaPrimaTO item) {
		return item.getMateriaPrima().getUnidad().getDescripcion();
	}
}
