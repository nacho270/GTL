package ar.com.textillevel.entidades.to.facturab;

import java.io.Serializable;

public class ItemFacturaBTO implements Serializable {

	private static final long serialVersionUID = -6196904930689863318L;

	private String cantidad;
	private String precioUnitario;
	private String importe;
	private String descripcion;

	public String getCantidad() {
		return cantidad;
	}

	public void setCantidad(String cantidad) {
		this.cantidad = cantidad;
	}

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
}
