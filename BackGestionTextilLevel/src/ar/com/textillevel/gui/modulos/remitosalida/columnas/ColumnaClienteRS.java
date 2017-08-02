package ar.com.textillevel.gui.modulos.remitosalida.columnas;

import ar.com.fwcommon.templates.modulo.model.tabla.ColumnaString;
import ar.com.textillevel.entidades.documentos.remito.RemitoSalida;

public class ColumnaClienteRS extends ColumnaString<RemitoSalida> {

	public ColumnaClienteRS() {
		super("CLIENTE");
	}

	@Override
	public String getValor(RemitoSalida item) {
		return item.getCliente() == null ? "" : (item.getCliente().getNroCliente()+"");
	}

}
