package ar.com.textillevel.mobile.modules.common;

public class ErrorTO {
	private int numero;
	private String mensaje;

	public ErrorTO(int numero, String mensaje) {
		this.numero = numero;
		this.mensaje = mensaje;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
}
