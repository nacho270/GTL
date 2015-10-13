package ar.com.textillevel.gui.modulos.personal.modulos.vales.columnas;

import ar.com.fwcommon.templates.modulo.model.tabla.ColumnaString;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.vale.ValeAnticipo;

public class ColumnaValeAnticipoUtilizado extends ColumnaString<ValeAnticipo> {

	public ColumnaValeAnticipoUtilizado() {
		super("Utilizado");
		setAncho(90);
	}

	@Override
	public String getValor(ValeAnticipo item) {
		return item.getEstadoVale().getDescripcion();
	}
}
