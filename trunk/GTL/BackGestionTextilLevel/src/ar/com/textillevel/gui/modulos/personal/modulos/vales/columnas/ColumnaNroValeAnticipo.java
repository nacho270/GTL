package ar.com.textillevel.gui.modulos.personal.modulos.vales.columnas;

import ar.clarin.fwjava.templates.modulo.model.tabla.ColumnaInt;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.vale.ValeAnticipo;

public class ColumnaNroValeAnticipo extends ColumnaInt<ValeAnticipo> {

	public ColumnaNroValeAnticipo() {
		super("Número vale");
		setAncho(90);
	}

	@Override
	public Integer getValor(ValeAnticipo item) {
		return item.getNroVale();
	}
}
