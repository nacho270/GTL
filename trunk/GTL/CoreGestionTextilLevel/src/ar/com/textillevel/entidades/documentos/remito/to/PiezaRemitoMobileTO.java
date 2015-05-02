package ar.com.textillevel.entidades.documentos.remito.to;

import java.io.Serializable;

public class PiezaRemitoMobileTO implements Serializable {

	private static final long serialVersionUID = -6775392906425204266L;

	private String numero;
	private String metros;
	private String observaciones;
	private String odt;
	private String metrosEntrada; //solo para Remito de salida

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getMetros() {
		return metros;
	}

	public void setMetros(String metros) {
		this.metros = metros;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getOdt() {
		return odt;
	}

	public void setOdt(String odt) {
		this.odt = odt;
	}

	public String getMetrosEntrada() {
		return metrosEntrada;
	}

	public void setMetrosEntrada(String metrosEntrada) {
		this.metrosEntrada = metrosEntrada;
	}

}
