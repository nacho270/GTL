package ar.com.textillevel.entidades.cuenta.to;

import java.io.Serializable;

public class ContactoTO implements Serializable {

	private static final long serialVersionUID = -5886956223038455209L;

	private String id;
	private String razonSocial;
	
	public ContactoTO(String id, String razonSocial) {
		this.id = id;
		this.razonSocial = razonSocial;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRazonSocial() {
		return razonSocial;
	}

	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}
	
	@Override
	public String toString() {
		return id + " - " + razonSocial;
	}
}
