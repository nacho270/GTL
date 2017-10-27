package ar.com.textillevel.gui.modulos.remitoentrada.columnas;

import ar.com.fwcommon.templates.modulo.model.tabla.ColumnaString;
import ar.com.textillevel.gui.modulos.remitoentrada.to.RemitoEntradaModuloTO;

public class ColumnaClienteRE extends ColumnaString<RemitoEntradaModuloTO> {

	public ColumnaClienteRE() {
		super("CLIENTE");
	}

	@Override
	public String getValor(RemitoEntradaModuloTO item) {
		return (item.isEsDe01() ? "01 / " : "") + item.getCliente().getNroCliente();
	}

}