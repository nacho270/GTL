package ar.com.textillevel.gui.modulos.dibujos.columnas;

import ar.com.fwcommon.templates.modulo.model.tabla.ColumnaInt;
import ar.com.textillevel.entidades.ventas.articulos.DibujoEstampado;

public class ColumnaNumeroDibujo extends ColumnaInt<DibujoEstampado>{

	public ColumnaNumeroDibujo() {
		super("Nro.");
		setAncho(40);
	}

	@Override
	public Integer getValor(DibujoEstampado item) {
		return item.getNroDibujo();
	}
}
