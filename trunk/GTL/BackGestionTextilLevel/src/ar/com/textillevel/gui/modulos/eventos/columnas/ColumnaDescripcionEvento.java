package ar.com.textillevel.gui.modulos.eventos.columnas;

import ar.clarin.fwjava.auditoria.ejb.Evento;
import ar.clarin.fwjava.templates.modulo.model.tabla.ColumnaString;

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
