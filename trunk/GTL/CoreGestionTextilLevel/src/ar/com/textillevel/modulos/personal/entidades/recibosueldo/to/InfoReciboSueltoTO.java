package ar.com.textillevel.modulos.personal.entidades.recibosueldo.to;

import java.io.Serializable;
import ar.com.textillevel.modulos.personal.entidades.legajos.LegajoEmpleado;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.ReciboSueldo;

public class InfoReciboSueltoTO implements Serializable {

	private static final long serialVersionUID = 8012342476290758585L;

	private LegajoEmpleado legajo;
	private ReciboSueldo reciboSueldo;

	public LegajoEmpleado getLegajo() {
		return legajo;
	}

	public void setLegajo(LegajoEmpleado legajo) {
		this.legajo = legajo;
	}

	public ReciboSueldo getReciboSueldo() {
		return reciboSueldo;
	}

	public void setReciboSueldo(ReciboSueldo reciboSueldo) {
		this.reciboSueldo = reciboSueldo;
	}


}
