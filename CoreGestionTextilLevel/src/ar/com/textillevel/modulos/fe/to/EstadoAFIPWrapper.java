package ar.com.textillevel.modulos.fe.to;

import java.io.Serializable;

public class EstadoAFIPWrapper implements Serializable {

	private static final long serialVersionUID = 2246424736021379959L;
	
	private String nombre;
	private String valor;

	public EstadoAFIPWrapper(String nombre, String valor) {
		this.nombre = nombre;
		this.valor = valor;
	}

	public String getNombre() {
		return nombre;
	}

	public String getValor() {
		return valor;
	}
	
	public boolean isOK() {
		return getValor().equalsIgnoreCase("ok");
	}
}
