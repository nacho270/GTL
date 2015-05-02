package ar.com.textillevel.gui.modulos.personal.modulos.vales.columnas;

import java.util.Date;

import ar.clarin.fwjava.templates.modulo.model.tabla.ColumnaDate;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.vale.ValeAnticipo;

public class ColumnaFechaValeAnticipo extends ColumnaDate<ValeAnticipo>{

	public ColumnaFechaValeAnticipo() {
		super("Fecha");
	}

	@Override
	public Date getValor(ValeAnticipo item) {
		return item.getFecha();
	}
}
