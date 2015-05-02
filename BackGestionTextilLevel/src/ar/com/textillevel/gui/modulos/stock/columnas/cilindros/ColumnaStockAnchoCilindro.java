package ar.com.textillevel.gui.modulos.stock.columnas.cilindros;

import ar.clarin.fwjava.templates.modulo.model.tabla.ColumnaFloat;
import ar.com.textillevel.entidades.ventas.materiaprima.Cilindro;
import ar.com.textillevel.entidades.ventas.materiaprima.ItemMateriaPrimaTO;

public class ColumnaStockAnchoCilindro extends ColumnaFloat<ItemMateriaPrimaTO>{

	public ColumnaStockAnchoCilindro() {
		super("Ancho");
		setAncho(80);
	}

	@Override
	public Float getValor(ItemMateriaPrimaTO item) {
		return ((Cilindro)item.getMateriaPrima()).getAnchoCilindro().floatValue();
	}
}
