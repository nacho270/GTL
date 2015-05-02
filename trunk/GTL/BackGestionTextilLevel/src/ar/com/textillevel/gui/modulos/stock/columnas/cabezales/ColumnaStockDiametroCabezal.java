package ar.com.textillevel.gui.modulos.stock.columnas.cabezales;

import ar.clarin.fwjava.templates.modulo.model.tabla.ColumnaFloat;
import ar.com.textillevel.entidades.ventas.materiaprima.Cabezal;
import ar.com.textillevel.entidades.ventas.materiaprima.ItemMateriaPrimaTO;

public class ColumnaStockDiametroCabezal extends ColumnaFloat<ItemMateriaPrimaTO>{

	public ColumnaStockDiametroCabezal() {
		super("Di�metro");
		setAncho(80);
	}

	@Override
	public Float getValor(ItemMateriaPrimaTO item) {
		return ((Cabezal)item.getMateriaPrima()).getDiametroCabezal().floatValue();
	}
}
