package ar.com.textillevel.web.spring.exception;

public class GTLException extends RuntimeException {

	private static final long serialVersionUID = -5449506858170748436L;

	private int codigo;

	public GTLException(int codigo, String message) {
		super(message);
		this.codigo = codigo;
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
}
