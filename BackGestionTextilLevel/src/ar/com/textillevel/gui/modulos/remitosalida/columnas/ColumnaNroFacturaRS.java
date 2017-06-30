package ar.com.textillevel.gui.modulos.remitosalida.columnas;

import ar.com.fwcommon.templates.modulo.model.tabla.ColumnaString;
import ar.com.fwcommon.util.StringUtil;
import ar.com.textillevel.entidades.documentos.remito.RemitoSalida;

public class ColumnaNroFacturaRS extends ColumnaString<RemitoSalida> {

	public ColumnaNroFacturaRS() {
		super("NRO. FACTURA");
	}

	@Override
	public String getValor(RemitoSalida item) {
		if(item.getNroFactura() == null) {
			return "";
		} else {
			String nro = StringUtil.fillLeftWithZeros(String.valueOf(item.getNroSucursal()), 4) + "-";
			nro += StringUtil.fillLeftWithZeros(String.valueOf(item.getNroFactura()), 8);
			return nro;
		}
	}

}
