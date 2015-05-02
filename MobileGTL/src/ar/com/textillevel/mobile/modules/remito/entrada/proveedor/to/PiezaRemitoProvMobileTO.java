package ar.com.textillevel.mobile.modules.remito.entrada.proveedor.to;

import java.io.Serializable;

public class PiezaRemitoProvMobileTO implements Serializable {

	private static final long serialVersionUID = -2655404422775945504L;

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