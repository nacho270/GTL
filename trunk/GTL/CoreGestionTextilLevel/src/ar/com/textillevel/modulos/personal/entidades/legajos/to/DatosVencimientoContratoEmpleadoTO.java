package ar.com.textillevel.modulos.personal.entidades.legajos.to;

import java.io.Serializable;
import java.sql.Date;

public class DatosVencimientoContratoEmpleadoTO implements Serializable {

	private static final long serialVersionUID = 2263203611916176107L;

	private String nombre;
	private String apellido;
	private Date fechaVencimiento;

	public DatosVencimientoContratoEmpleadoTO() {
		super();
	}

	public DatosVencimientoContratoEmpleadoTO(String nombre, String apellido, Date fechaVencimiento) {
		super();
		this.nombre = nombre;
		this.apellido = apellido;
		this.fechaVencimiento = fechaVencimiento;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public Date getFechaVencimiento() {
		return fechaVencimiento;
	}

	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}
}
