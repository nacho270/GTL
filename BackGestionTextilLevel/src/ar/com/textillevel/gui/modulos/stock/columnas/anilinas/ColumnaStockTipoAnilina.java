package ar.com.textillevel.gui.modulos.stock.columnas.anilinas;

import ar.com.fwcommon.templates.modulo.model.tabla.ColumnaString;
import ar.com.textillevel.entidades.ventas.materiaprima.ItemMateriaPrimaTO;
import ar.com.textillevel.entidades.ventas.materiaprima.anilina.Anilina;

public class ColumnaStockTipoAnilina extends ColumnaString<ItemMateriaPrimaTO>{

	public ColumnaStockTipoAnilina() {
		super("Tipo de anilina");
		setAncho(120);
	}

	@Override
	public String getValor(ItemMateriaPrimaTO item) {
		return ((Anilina)item.getMateriaPrima()).getTipoAnilina().getDescripcion();
	}
}
