package ar.com.textillevel.gui.modulos.eventos.columnas;

import ar.com.fwcommon.auditoria.ejb.Evento;
import ar.com.fwcommon.templates.modulo.model.tabla.ColumnaString;

public class ColumnaDescripcionEvento extends ColumnaString<Evento>{

	public ColumnaDescripcionEvento() {
		super("Descripción");
		setAncho(400);
	}

	@Override
	public String getValor(Evento item) {
		return item.getDescripcion();
	}
}
