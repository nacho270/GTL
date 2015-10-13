package ar.com.textillevel.gui.modulos.stock.columnas.anilinas;

import ar.com.fwcommon.templates.modulo.model.tabla.ColumnaInt;
import ar.com.textillevel.entidades.ventas.materiaprima.ItemMateriaPrimaTO;
import ar.com.textillevel.entidades.ventas.materiaprima.anilina.Anilina;

public class ColumnaStockColorIndex extends ColumnaInt<ItemMateriaPrimaTO>{

	public ColumnaStockColorIndex() {
		super("C.I");
		setAncho(80);
	}

	@Override
	public Integer getValor(ItemMateriaPrimaTO item) {
		return ((Anilina)item.getMateriaPrima()).getColorIndex();
	}
}
