package ar.com.textillevel.gui.modulos.remitoentrada.columnas;

import ar.com.fwcommon.templates.modulo.model.tabla.ColumnaString;
import ar.com.textillevel.entidades.documentos.remito.RemitoEntrada;

public class ColumnaClienteRE extends ColumnaString<RemitoEntrada> {

	public ColumnaClienteRE() {
		super("CLIENTE");
	}

	@Override
	public String getValor(RemitoEntrada item) {
		return (item.getArticuloStock() == null && item.getPrecioMatPrima() == null ? "" : "01 / ") + item.getCliente().getNroCliente();
	}

}
