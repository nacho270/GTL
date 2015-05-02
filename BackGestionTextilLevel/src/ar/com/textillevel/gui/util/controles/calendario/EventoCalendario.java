package ar.com.textillevel.gui.util.controles.calendario;

import java.io.Serializable;
import java.sql.Date;

public class EventoCalendario implements Serializable {

	private static final long serialVersionUID = -544760823002061198L;

	private String descripcion;
	private Date fecha;

	public EventoCalendario() {

	}

	public EventoCalendario(String descripcion, Date fecha) {
		this.descripcion = descripcion;
		this.fecha = fecha;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
}
