package ar.com.textillevel.gui.modulos.eventos.columnas;

import ar.clarin.fwjava.auditoria.ejb.Evento;
import ar.clarin.fwjava.templates.modulo.model.tabla.ColumnaString;

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
