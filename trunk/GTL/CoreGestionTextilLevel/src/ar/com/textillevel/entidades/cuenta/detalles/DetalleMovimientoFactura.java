package ar.com.textillevel.entidades.cuenta.detalles;

import ar.com.textillevel.entidades.documentos.factura.Factura;

public abstract class DetalleMovimientoFactura extends DetalleMovimiento {

	private Factura factura;

	public DetalleMovimientoFactura(Factura factura) {
		this.factura = factura;
	}

	public Factura getFactura() {
		return factura;
	}

	public void setFactura(Factura factura) {
		this.factura = factura;
	}

}
