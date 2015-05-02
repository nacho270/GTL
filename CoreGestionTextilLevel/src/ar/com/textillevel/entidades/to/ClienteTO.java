package ar.com.textillevel.entidades.to;

import java.io.Serializable;

public class ClienteTO implements Serializable {

	private static final long serialVersionUID = 633104887498288226L;

	private String razonSocial;
	private String cuit;
	private String condicionIVA;
	private String direccion;
	private String localidad;

	public String getRazonSocial() {
		return razonSocial;
	}

	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	public String getCuit() {
		return cuit;
	}

	public void setCuit(String cuit) {
		this.cuit = cuit;
	}

	public String getCondicionIVA() {
		return condicionIVA;
	}

	public void setCondicionIVA(String condicionIVA) {
		this.condicionIVA = condicionIVA;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getLocalidad() {
		return localidad;
	}

	public void setLocalidad(String localidad) {
		this.localidad = localidad;
	}
}
