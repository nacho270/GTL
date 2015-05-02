package ar.com.textillevel.gui.modulos.odt.cabecera;

import java.sql.Date;

import ar.com.textillevel.modulos.odt.enums.EEstadoODT;

public class ModeloCabeceraODT {

	public Date fechaDesde;
	public Date fechaHasta;
	public EEstadoODT estadoODT;

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

	public EEstadoODT getEstadoODT() {
		return estadoODT;
	}

	public void setEstadoODT(EEstadoODT estadoODT) {
		this.estadoODT = estadoODT;
	}
}
