package ar.com.textillevel.gui.modulos.remitosalida.columnas;

import ar.com.fwcommon.templates.modulo.model.tabla.ColumnaString;
import ar.com.textillevel.entidades.documentos.remito.RemitoSalida;

public class ColumnaProveedorRS extends ColumnaString<RemitoSalida> {

	public ColumnaProveedorRS() {
		super("PROVEEDOR");
		setAncho(200);
	}

	@Override
	public String getValor(RemitoSalida item) {
		if(item.getProveedor() == null) {
			return "";
		} else {
			return item.getProveedor().getRazonSocial();
		}
	}

}
