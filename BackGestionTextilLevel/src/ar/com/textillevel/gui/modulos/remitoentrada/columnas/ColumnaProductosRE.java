package ar.com.textillevel.gui.modulos.remitoentrada.columnas;

import ar.com.fwcommon.templates.modulo.model.tabla.ColumnaString;
import ar.com.fwcommon.util.StringUtil;
import ar.com.textillevel.entidades.documentos.remito.RemitoEntrada;

public class ColumnaProductosRE extends ColumnaString<RemitoEntrada> {

	public ColumnaProductosRE() {
		super("PRODUCTO");
		setAncho(800);
	}

	@Override
	public String getValor(RemitoEntrada item) {
		return StringUtil.getCadena(item.getProductoArticuloList(), " | ");
	}

}
