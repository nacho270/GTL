package ar.com.textillevel.gui.modulos.remitoentrada.columnas;

import ar.com.fwcommon.templates.modulo.model.tabla.ColumnaInt;
import ar.com.textillevel.entidades.documentos.remito.RemitoEntrada;

public class ColumnaNroRE extends ColumnaInt<RemitoEntrada> {

	public ColumnaNroRE() {
		super("NRO.");
	}

	@Override
	public Integer getValor(RemitoEntrada item) {
		return item.getNroRemito();
	}

}
