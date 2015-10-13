package ar.com.textillevel.gui.modulos.eventos.columnas;

import ar.com.fwcommon.auditoria.ejb.Evento;
import ar.com.fwcommon.templates.modulo.model.tabla.ColumnaString;

public class ColumnaUsuarioEvento extends ColumnaString<Evento>{

	public ColumnaUsuarioEvento() {
		super("Usuario");
		setAncho(150);
	}

	@Override
	public String getValor(Evento item) {
		return item.getUsuario();
	}
}
