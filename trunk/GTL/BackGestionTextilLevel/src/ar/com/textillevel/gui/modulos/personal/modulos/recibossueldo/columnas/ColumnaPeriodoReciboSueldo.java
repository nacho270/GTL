package ar.com.textillevel.gui.modulos.personal.modulos.recibossueldo.columnas;

import ar.clarin.fwjava.templates.modulo.model.tabla.ColumnaString;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.ReciboSueldo;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.to.InfoReciboSueltoTO;
import ar.com.textillevel.modulos.personal.utils.ReciboSueldoHelper;

public class ColumnaPeriodoReciboSueldo extends ColumnaString<InfoReciboSueltoTO> {

	public ColumnaPeriodoReciboSueldo() {
		super("Período");
		setAncho(250);
	}

	@Override
	public String getValor(InfoReciboSueltoTO item) {
		ReciboSueldo reciboSueldo = item.getReciboSueldo();
		return reciboSueldo == null ? "" : ReciboSueldoHelper.getInstance().calcularPeriodoRS(reciboSueldo);
	}

}
