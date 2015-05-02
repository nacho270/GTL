package ar.com.textillevel.entidades.to.ivaventas;

import java.io.Serializable;

public class TotalesIVAVentasTO implements Serializable {

	private static final long serialVersionUID = -3364005288433553950L;

	private Double totalNetoGravado;
	private Double totalIVA21;
	private Double totalPercepcion;
	private Double totalExento;
	private Double totalNoGravado;
	private Double sumaTotalComp;
	
	public TotalesIVAVentasTO() {
		totalExento = 0d;
		totalIVA21 = 0d;
		totalNetoGravado = 0d;
		totalNoGravado = 0d;
		totalPercepcion = 0d;
		sumaTotalComp = 0d;
	}

	public Double getTotalNetoGravado() {
		return totalNetoGravado;
	}

	public void setTotalNetoGravado(Double totalNetoGravado) {
		this.totalNetoGravado = totalNetoGravado;
	}

	public Double getTotalIVA21() {
		return totalIVA21;
	}

	public void setTotalIVA21(Double totalIVA21) {
		this.totalIVA21 = totalIVA21;
	}

	public Double getTotalPercepcion() {
		return totalPercepcion;
	}

	public void setTotalPercepcion(Double totalPercepcion) {
		this.totalPercepcion = totalPercepcion;
	}

	public Double getTotalExento() {
		return totalExento;
	}

	public void setTotalExento(Double totalExento) {
		this.totalExento = totalExento;
	}

	public Double getTotalNoGravado() {
		return totalNoGravado;
	}

	public void setTotalNoGravado(Double totalNoGravado) {
		this.totalNoGravado = totalNoGravado;
	}

	public Double getSumaTotalComp() {
		return sumaTotalComp;
	}

	public void setSumaTotalComp(Double sumaTotalComp) {
		this.sumaTotalComp = sumaTotalComp;
	}
}
