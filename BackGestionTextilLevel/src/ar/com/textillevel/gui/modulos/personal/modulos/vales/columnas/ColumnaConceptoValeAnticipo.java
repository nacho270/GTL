package ar.com.textillevel.gui.modulos.personal.modulos.vales.columnas;

import ar.com.fwcommon.templates.modulo.model.tabla.ColumnaString;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.vale.ValeAnticipo;

public class ColumnaConceptoValeAnticipo extends ColumnaString<ValeAnticipo>{

	public ColumnaConceptoValeAnticipo() {
		super("Concepto");
		setAncho(220);
	}

	@Override
	public String getValor(ValeAnticipo item) {
		return item.getConcepto();
	}
}
