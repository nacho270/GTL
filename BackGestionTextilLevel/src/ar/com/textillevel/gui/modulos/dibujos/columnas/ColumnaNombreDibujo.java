package ar.com.textillevel.gui.modulos.dibujos.columnas;

import ar.com.fwcommon.templates.modulo.model.tabla.ColumnaString;
import ar.com.textillevel.entidades.ventas.articulos.DibujoEstampado;

public class ColumnaNombreDibujo extends ColumnaString<DibujoEstampado>{

	public ColumnaNombreDibujo() {
		super("Nombre");
		setAncho(200);
	}

	@Override
	public String getValor(DibujoEstampado item) {
		return item.getNombre();
	}
}
