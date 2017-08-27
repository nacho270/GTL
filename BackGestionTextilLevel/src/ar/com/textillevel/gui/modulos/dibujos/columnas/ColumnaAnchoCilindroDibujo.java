package ar.com.textillevel.gui.modulos.dibujos.columnas;

import ar.com.fwcommon.templates.modulo.model.tabla.ColumnaFloat;
import ar.com.textillevel.entidades.ventas.articulos.DibujoEstampado;

public class ColumnaAnchoCilindroDibujo extends ColumnaFloat<DibujoEstampado> {

	public ColumnaAnchoCilindroDibujo() {
		super("Ancho");
		setAncho(40);
	}

	@Override
	public Float getValor(DibujoEstampado item) {
		return item.getAnchoCilindro().floatValue();
	}
}
