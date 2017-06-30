package ar.com.textillevel.gui.modulos.remitosalida.columnas;

import ar.com.fwcommon.templates.modulo.model.tabla.ColumnaInt;
import ar.com.textillevel.entidades.documentos.remito.RemitoSalida;

public class ColumnaNroRS extends ColumnaInt<RemitoSalida> {

	public ColumnaNroRS() {
		super("NRO.");
	}

	@Override
	public Integer getValor(RemitoSalida item) {
		return item.getNroRemito();
	}

}
