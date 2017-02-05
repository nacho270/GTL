package ar.com.textillevel.entidades.to;

import java.io.Serializable;

import ar.com.textillevel.entidades.enums.TerminalServiceError;

public class TerminalServiceResponse implements Serializable {

	private static final long serialVersionUID = -4072902962266559090L;

	private boolean error;
	private String mensajeError;
	private int codigoError;

	public TerminalServiceResponse() {
		this.error = false;
	}

	public TerminalServiceResponse(TerminalServiceError errorData) {
		this.error = true;
		this.mensajeError = errorData.getMensaje();
		this.codigoError = errorData.getCodigo();
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public String getMensajeError() {
		return mensajeError;
	}

	public void setMensajeError(String mensajeError) {
		this.mensajeError = mensajeError;
	}

	public int getCodigoError() {
		return codigoError;
	}

	public void setCodigoError(int codigoError) {
		this.codigoError = codigoError;
	}
}