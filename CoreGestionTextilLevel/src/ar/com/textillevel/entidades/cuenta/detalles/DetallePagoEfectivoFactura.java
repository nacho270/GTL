package ar.com.textillevel.entidades.cuenta.detalles;

import java.math.BigDecimal;

import ar.com.textillevel.entidades.documentos.factura.Factura;

//Para hacer una nota de crédito o de débito dependiendo si monto > 0
public class DetallePagoEfectivoFactura extends DetalleMovimientoFactura {

	private BigDecimal monto;

	public DetallePagoEfectivoFactura(Factura factura, BigDecimal monto) {
		super(factura);
		this.monto = monto;
	}

	public BigDecimal getMonto() {
		return monto;
	}

	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}

}