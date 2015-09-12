package ar.com.textillevel.entidades.ventas;

import java.io.Serializable;

import ar.com.textillevel.entidades.enums.ETipoProducto;

public class DatosAumentoTO implements Serializable {

	private static final long serialVersionUID = -8014354761364993552L;

	private ETipoProducto tipoProducto;
	private float porcentajeAumento;

	public ETipoProducto getTipoProducto() {
		return tipoProducto;
	}

	public void setTipoProducto(ETipoProducto tipoProducto) {
		this.tipoProducto = tipoProducto;
	}

	public float getPorcentajeAumento() {
		return porcentajeAumento;
	}

	public void setPorcentajeAumento(float porcentajeAumento) {
		this.porcentajeAumento = porcentajeAumento;
	}
}
