package ar.com.textillevel.entidades.to.facturab;

import java.io.Serializable;


public class ClienteBTO implements Serializable {

	private static final long serialVersionUID = 633104887498288226L;

	private String razonSocial;
	private String cuit;
	private String condicionIVANoResp;
	private String condicionIVAExento;
	private String condicionIVARespMonot;
	private String condicionIVAConsFinal;
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

	
	public String getCondicionIVANoResp() {
		return condicionIVANoResp;
	}

	
	public void setCondicionIVANoResp(String condicionIVANoResp) {
		this.condicionIVANoResp = condicionIVANoResp;
	}

	
	public String getCondicionIVAExento() {
		return condicionIVAExento;
	}

	
	public void setCondicionIVAExento(String condicionIVAExento) {
		this.condicionIVAExento = condicionIVAExento;
	}

	
	public String getCondicionIVARespMonot() {
		return condicionIVARespMonot;
	}

	
	public void setCondicionIVARespMonot(String condicionIVARespMonot) {
		this.condicionIVARespMonot = condicionIVARespMonot;
	}

	
	public String getCondicionIVAConsFinal() {
		return condicionIVAConsFinal;
	}

	
	public void setCondicionIVAConsFinal(String condicionIVAConsFinal) {
		this.condicionIVAConsFinal = condicionIVAConsFinal;
	}
}
