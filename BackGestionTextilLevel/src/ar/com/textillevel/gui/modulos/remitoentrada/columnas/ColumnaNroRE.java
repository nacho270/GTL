package ar.com.textillevel.gui.modulos.remitoentrada.columnas;

import ar.com.fwcommon.templates.modulo.model.tabla.ColumnaInt;
import ar.com.textillevel.gui.modulos.remitoentrada.to.RemitoEntradaModuloTO;

public class ColumnaNroRE extends ColumnaInt<RemitoEntradaModuloTO> {

	public ColumnaNroRE() {
		super("NRO.");
		setAncho(100);
	}

	@Override
	public Integer getValor(RemitoEntradaModuloTO item) {
		return item.getNroRemito();
	}

}
