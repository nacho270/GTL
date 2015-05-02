package ar.com.textillevel.mobile.modules.factura.proveedor.to;

import java.io.Serializable;

public class ItemImpuestoFacturaProvMobTO implements Serializable {

	private static final long serialVersionUID = -5804962697357396403L;

	private String descripcion;
	private String importe;

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getImporte() {
		return importe;
	}

	public void setImporte(String importe) {
		this.importe = importe;
	}

}