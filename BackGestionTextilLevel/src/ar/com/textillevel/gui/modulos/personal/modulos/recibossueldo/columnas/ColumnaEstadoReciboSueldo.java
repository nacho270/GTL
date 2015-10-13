package ar.com.textillevel.gui.modulos.personal.modulos.recibossueldo.columnas;

import ar.com.fwcommon.templates.modulo.model.tabla.ColumnaString;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.to.InfoReciboSueltoTO;

public class ColumnaEstadoReciboSueldo extends ColumnaString<InfoReciboSueltoTO> {

	public ColumnaEstadoReciboSueldo() {
		super("Estado");
		setAncho(150);
	}

	@Override
	public String getValor(InfoReciboSueltoTO item) {
		if(item.getReciboSueldo() == null) {
			return "NO CREADO";
		} else {
			return item.getReciboSueldo().getEstado().getNombre();
		}
	}

}
