package ar.com.textillevel.gui.modulos.stock.columnas.cilindros;

import ar.com.fwcommon.templates.modulo.model.tabla.ColumnaFloat;
import ar.com.textillevel.entidades.ventas.materiaprima.Cilindro;
import ar.com.textillevel.entidades.ventas.materiaprima.ItemMateriaPrimaTO;

public class ColumnaStockMeshCilindro extends ColumnaFloat<ItemMateriaPrimaTO>{

	public ColumnaStockMeshCilindro() {
		super("Mesh");
		setAncho(80);
	}

	@Override
	public Float getValor(ItemMateriaPrimaTO item) {
		return ((Cilindro)item.getMateriaPrima()).getMeshCilindro().floatValue();
	}
}
