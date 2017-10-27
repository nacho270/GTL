package ar.com.textillevel.gui.modulos.remitoentrada.columnas;

import ar.com.fwcommon.templates.modulo.model.tabla.ColumnaString;
import ar.com.textillevel.gui.modulos.remitoentrada.to.RemitoEntradaModuloTO;

public class ColumnaProductosRE extends ColumnaString<RemitoEntradaModuloTO> {

	public ColumnaProductosRE() {
		super("PRODUCTO/DIBUJOS");
		setAncho(800);
	}

	@Override
	public String getValor(RemitoEntradaModuloTO item) {
		return item.getDetalle();
	}

}
