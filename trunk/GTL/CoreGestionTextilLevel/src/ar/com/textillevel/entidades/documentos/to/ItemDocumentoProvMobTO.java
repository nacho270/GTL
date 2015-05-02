package ar.com.textillevel.entidades.documentos.to;

import java.io.Serializable;

public class ItemDocumentoProvMobTO implements Serializable {

	private static final long serialVersionUID = -4585236478530319535L;

	private String cantidadMasUnidad;
	private String descripcion;
	private String precioUnitario;
	private String factor;
	private String descuento;
	private String importe;
	private String impuestos;

	public String getPrecioUnitario() {
		return precioUnitario;
	}

	public void setPrecioUnitario(String precioUnitario) {
		this.precioUnitario = precioUnitario;
	}

	public String getImporte() {
		return importe;
	}

	public void setImporte(String importe) {
		this.importe = importe;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getCantidadMasUnidad() {
		return cantidadMasUnidad;
	}

	public void setCantidadMasUnidad(String cantidadMasUnidad) {
		this.cantidadMasUnidad = cantidadMasUnidad;
	}

	public String getFactor() {
		return factor;
	}

	public void setFactor(String factor) {
		this.factor = factor;
	}

	public String getImpuestos() {
		return impuestos;
	}

	public void setImpuestos(String impuestos) {
		this.impuestos = impuestos;
	}

	public String getDescuento() {
		return descuento;
	}

	public void setDescuento(String descuento) {
		this.descuento = descuento;
	}

}
