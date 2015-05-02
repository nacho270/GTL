package ar.com.textillevel.gui.modulos.stock.columnas.cilindros;

import ar.clarin.fwjava.templates.modulo.model.tabla.ColumnaFloat;
import ar.com.textillevel.entidades.ventas.materiaprima.Cilindro;
import ar.com.textillevel.entidades.ventas.materiaprima.ItemMateriaPrimaTO;

public class ColumnaStockDiametroCilindro extends ColumnaFloat<ItemMateriaPrimaTO>{

	public ColumnaStockDiametroCilindro() {
		super("Diámetro");
		setAncho(80);
	}

	@Override
	public Float getValor(ItemMateriaPrimaTO item) {
		return ((Cilindro)item.getMateriaPrima()).getDiametroCilindro().floatValue();
	}
}
