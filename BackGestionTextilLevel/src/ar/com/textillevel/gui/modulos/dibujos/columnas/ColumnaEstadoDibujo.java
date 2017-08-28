package ar.com.textillevel.gui.modulos.dibujos.columnas;

import ar.com.fwcommon.templates.modulo.model.tabla.ColumnaString;
import ar.com.textillevel.entidades.ventas.articulos.DibujoEstampado;

public class ColumnaEstadoDibujo extends ColumnaString<DibujoEstampado> {
	
	public ColumnaEstadoDibujo() {
		super("Estado");
		setAncho(90);
	}

	@Override
	public String getValor(DibujoEstampado item) {
		return item.getEstado().getDescripcion();
	}
}
