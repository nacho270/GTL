package ar.com.textillevel.gui.modulos.dibujos.columnas;

import ar.com.fwcommon.templates.modulo.model.tabla.ColumnaString;
import ar.com.textillevel.entidades.ventas.articulos.DibujoEstampado;

public class ColumnaClienteDibujo extends ColumnaString<DibujoEstampado> {
	
	public ColumnaClienteDibujo() {
		super("Cliente");
		setAncho(170);
	}

	@Override
	public String getValor(DibujoEstampado item) {
		return item.getCliente() != null ? item.getCliente().getRazonSocial() : "01";
	}
}
