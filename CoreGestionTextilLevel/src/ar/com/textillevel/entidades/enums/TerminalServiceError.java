package ar.com.textillevel.entidades.enums;

public enum TerminalServiceError {
	OPERACION_NO_PERMITIDA(1, "Operacion no permitida"), //
	ODP_NO_ENCONTRADA(2, "Orden de pago no encontrada"), //
	RS_NO_ENCONTRADO(3, "Remito de salida no encontrado"), //
	TIPO_DOC_RECONOCIDO(4, "Tipo de documento no reconocido"), //
	CODIGO_NO_INGRESADO(5, "No se ha ingresado codigo");

	private TerminalServiceError(int codigo, String mensaje) {
		this.codigo = codigo;
		this.mensaje = mensaje;
	}

	private int codigo;
	private String mensaje;

	public int getCodigo() {
		return codigo;
	}

	public String getMensaje() {
		return mensaje;
	}
}
