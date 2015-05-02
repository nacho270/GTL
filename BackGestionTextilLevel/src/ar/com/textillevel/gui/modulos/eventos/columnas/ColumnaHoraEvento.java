package ar.com.textillevel.gui.modulos.eventos.columnas;

import ar.clarin.fwjava.auditoria.ejb.Evento;
import ar.clarin.fwjava.templates.modulo.model.tabla.ColumnaString;
import ar.clarin.fwjava.util.DateUtil;

public class ColumnaHoraEvento extends ColumnaString<Evento>{

	public ColumnaHoraEvento() {
		super("Fecha y hora");
		setAncho(150);
	}

	@Override
	public String getValor(Evento item) {
		return DateUtil.dateToString(item.getFechaHora(), DateUtil.SHORT_DATE_WITH_HOUR);
	}
}
