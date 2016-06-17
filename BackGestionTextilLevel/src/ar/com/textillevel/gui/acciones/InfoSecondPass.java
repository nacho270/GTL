package ar.com.textillevel.gui.acciones;

import java.awt.Color;
import java.math.BigDecimal;

public class InfoSecondPass {

	private Integer idDocumentoPago;
	private Integer fila;
	private Color color;
	private BigDecimal monto;

	public InfoSecondPass(Integer idDocumentoPago, Integer fila, Color color, BigDecimal monto) {
		this.idDocumentoPago = idDocumentoPago;
		this.fila = fila;
		this.color = color;
		this.monto = monto;
	}

	public Integer getIdDocumentoPago() {
		return idDocumentoPago;
	}

	public void setIdDocumentoPago(Integer idDocumentoPago) {
		this.idDocumentoPago = idDocumentoPago;
	}

	public Integer getFila() {
		return fila;
	}

	public void setFila(Integer fila) {
		this.fila = fila;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public BigDecimal getMonto() {
		return monto;
	}

	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}

}