package ar.com.textillevel.entidades.documentos.remito.proveedor.to;

import java.io.Serializable;

public class PiezaRemitoProvMobileTO implements Serializable {

	private static final long serialVersionUID = -3698563909273126165L;

	private String cantidadMasUnidad;
	private String descripcion;
	private String contenedor;
	private String cantContenedor; 

	public String getCantidadMasUnidad() {
		return cantidadMasUnidad;
	}

	public void setCantidadMasUnidad(String cantidadMasUnidad) {
		this.cantidadMasUnidad = cantidadMasUnidad;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getContenedor() {
		return contenedor;
	}

	public void setContenedor(String contenedor) {
		this.contenedor = contenedor;
	}

	public String getCantContenedor() {
		return cantContenedor;
	}

	public void setCantContenedor(String cantContenedor) {
		this.cantContenedor = cantContenedor;
	}

}