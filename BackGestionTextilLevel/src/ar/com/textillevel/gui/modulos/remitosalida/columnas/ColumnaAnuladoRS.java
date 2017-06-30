package ar.com.textillevel.gui.modulos.remitosalida.columnas;

import ar.com.fwcommon.templates.modulo.model.tabla.ColumnaString;
import ar.com.textillevel.entidades.documentos.remito.RemitoSalida;

public class ColumnaAnuladoRS extends ColumnaString<RemitoSalida> {

	public ColumnaAnuladoRS() {
		super("Anulado");
	}

	@Override
	public String getValor(RemitoSalida item) {
		return (item.getAnulado() != null && item.getAnulado()) ? "SI" : "NO";
	}

}
