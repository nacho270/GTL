package ar.com.textillevel.gui.util;

import java.io.Serializable;

public class ClienteMorosoTO implements Serializable{

	private static final long serialVersionUID = 991240260312691208L;

	private String razonSocial;
	private String montoDeuda;
	
	public ClienteMorosoTO(String razonSocial, String montoDeuda) {
		this.razonSocial = razonSocial;
		this.montoDeuda = montoDeuda;
	}

	public String getRazonSocial() {
		return razonSocial;
	}
	
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}
	
	public String getMontoDeuda() {
		return montoDeuda;
	}
	
	public void setMontoDeuda(String montoDeuda) {
		this.montoDeuda = montoDeuda;
	}
}
