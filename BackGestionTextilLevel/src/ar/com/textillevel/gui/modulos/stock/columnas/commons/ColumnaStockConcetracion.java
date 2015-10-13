package ar.com.textillevel.gui.modulos.stock.columnas.commons;

import ar.com.fwcommon.templates.modulo.model.tabla.ColumnaFloat;
import ar.com.textillevel.entidades.ventas.materiaprima.ItemMateriaPrimaTO;

public class ColumnaStockConcetracion extends ColumnaFloat<ItemMateriaPrimaTO>{

	public ColumnaStockConcetracion() {
		super("Concentración");
		setAncho(120);
	}

	@Override
	public Float getValor(ItemMateriaPrimaTO item) {
		return item.getMateriaPrima().getConcentracion()==null?null:item.getMateriaPrima().getConcentracion().floatValue();
	}
}
