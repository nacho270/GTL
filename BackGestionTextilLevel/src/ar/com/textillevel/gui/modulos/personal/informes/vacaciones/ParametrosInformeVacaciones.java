package ar.com.textillevel.gui.modulos.personal.informes.vacaciones;

import java.sql.Date;

import ar.com.textillevel.modulos.personal.entidades.legajos.Empleado;

public class ParametrosInformeVacaciones {
	private Empleado empleado;
	private Date fechaDesde;
	private Date fechaHasta;
	private Integer anioCorrespondiente;

	public Empleado getEmpleado() {
		return empleado;
	}

	public void setEmpleado(Empleado empleado) {
		this.empleado = empleado;
	}

	public Date getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}

	public Date getFechaHasta() {
		return fechaHasta;
	}

	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
	}

	public Integer getAnioCorrespondiente() {
		return anioCorrespondiente;
	}

	public void setAnioCorrespondiente(Integer anioCorrespondiente) {
		this.anioCorrespondiente = anioCorrespondiente;
	}
}