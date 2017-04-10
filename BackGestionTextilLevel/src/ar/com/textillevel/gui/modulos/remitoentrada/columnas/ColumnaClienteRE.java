package ar.com.textillevel.gui.modulos.remitoentrada.columnas;

import ar.com.fwcommon.templates.modulo.model.tabla.ColumnaInt;
import ar.com.textillevel.entidades.documentos.remito.RemitoEntrada;

public class ColumnaClienteRE extends ColumnaInt<RemitoEntrada> {

	public ColumnaClienteRE() {
		super("CLIENTE");
	}

	@Override
	public Integer getValor(RemitoEntrada item) {
		return item.getCliente().getNroCliente();
	}

}
