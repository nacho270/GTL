package ar.com.textillevel.entidades.cuenta.detalles;

import ar.com.textillevel.entidades.cheque.Cheque;
import ar.com.textillevel.entidades.documentos.factura.Factura;

//Para registrar un pago con cheque
public class DetallePagoChequeFactura extends DetalleMovimientoFactura {

	private Cheque cheque;

	public DetallePagoChequeFactura(Factura factura, Cheque cheque) {
		super(factura);
		this.cheque = cheque;
	}

	public Cheque getCheque() {
		return cheque;
	}

	public void setCheque(Cheque cheque) {
		this.cheque = cheque;
	}

}