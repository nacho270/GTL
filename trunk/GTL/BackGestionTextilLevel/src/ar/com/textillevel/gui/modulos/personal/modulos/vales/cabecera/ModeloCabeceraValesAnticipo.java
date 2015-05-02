package ar.com.textillevel.gui.modulos.personal.modulos.vales.cabecera;

import java.sql.Date;

import ar.com.textillevel.modulos.personal.entidades.recibosueldo.enums.EEstadoValeAnticipo;

public class ModeloCabeceraValesAnticipo {

	private Date fechaDesde;
	private Date fechaHasta;
	private String apellidoEmpleado;
	private EEstadoValeAnticipo estadoVale;

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

	public String getApellidoEmpleado() {
		return apellidoEmpleado;
	}

	public void setApellidoEmpleado(String apellidoEmpleado) {
		this.apellidoEmpleado = apellidoEmpleado;
	}

	public EEstadoValeAnticipo getEstadoVale() {
		return estadoVale;
	}

	public void setEstadoVale(EEstadoValeAnticipo estadoVale) {
		this.estadoVale = estadoVale;
	}
}
