package ar.com.textillevel.gui.modulos.personal.modulos.vales.columnas;

import ar.com.fwcommon.templates.modulo.model.tabla.ColumnaFloat;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.vale.ValeAnticipo;

public class ColumnaMontoValeAnticipo extends ColumnaFloat<ValeAnticipo> {

	public ColumnaMontoValeAnticipo() {
		super("Monto");
		setAncho(90);
	}

	@Override
	public Float getValor(ValeAnticipo item) {
		return item.getMonto().floatValue();
	}
}
