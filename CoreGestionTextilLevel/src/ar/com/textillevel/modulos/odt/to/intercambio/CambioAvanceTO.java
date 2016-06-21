package ar.com.textillevel.modulos.odt.to.intercambio;

import java.io.Serializable;
import java.sql.Timestamp;

import ar.com.textillevel.modulos.odt.entidades.workflow.CambioAvance;

public class CambioAvanceTO implements Serializable {

	private static final long serialVersionUID = -97299466712659620L;

	private Byte idAvance;
	private Timestamp fechaHora;
	private Integer idUsuarioSistema;
	private String observaciones;

	public CambioAvanceTO() {
	}
	
	public CambioAvanceTO(CambioAvance ca) {
		this.idAvance = ca.getAvance().getId();
		this.fechaHora = ca.getFechaHora();
		this.idUsuarioSistema = ca.getUsuario().getId();
		this.observaciones = ca.getObservaciones();
	}

	public Byte getIdAvance() {
		return idAvance;
	}

	public void setIdAvance(Byte idAvance) {
		this.idAvance = idAvance;
	}

	public Timestamp getFechaHora() {
		return fechaHora;
	}

	public void setFechaHora(Timestamp fechaHora) {
		this.fechaHora = fechaHora;
	}

	public Integer getIdUsuarioSistema() {
		return idUsuarioSistema;
	}

	public void setIdUsuarioSistema(Integer idUsuarioSistema) {
		this.idUsuarioSistema = idUsuarioSistema;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

}