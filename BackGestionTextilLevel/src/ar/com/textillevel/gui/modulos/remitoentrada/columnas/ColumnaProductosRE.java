package ar.com.textillevel.gui.modulos.remitoentrada.columnas;

import ar.com.fwcommon.templates.modulo.model.tabla.ColumnaString;
import ar.com.fwcommon.util.StringUtil;
import ar.com.textillevel.entidades.documentos.remito.RemitoEntrada;
import ar.com.textillevel.entidades.documentos.remito.enums.ESituacionODTRE;

public class ColumnaProductosRE extends ColumnaString<RemitoEntrada> {

	public ColumnaProductosRE() {
		super("PRODUCTO");
		setAncho(800);
	}

	@Override
	public String getValor(RemitoEntrada item) {
		if(item.getSituacionODT() == ESituacionODTRE.CON_ODT) {
			return StringUtil.getCadena(item.getProductoArticuloList(), " | ");
		} else if(item.getSituacionODT() == ESituacionODTRE.SIN_ODT) {
			return "[SIN ODT]";
		} else {
			return "[ODT CON COLOR A DEFINIR]";
		}
	}

}
