package ar.com.textillevel.entidades.cuenta.movimientos;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import ar.com.textillevel.entidades.cuenta.movimientos.visitor.IFilaMovimientoVisitor;
import ar.com.textillevel.entidades.documentos.factura.proveedor.NotaCreditoProveedor;
import ar.com.textillevel.entidades.documentos.ordendepago.OrdenDePago;

@Entity
@DiscriminatorValue(value="MHP")
public class MovimientoHaberProveedor extends MovimientoCuenta {

	private static final long serialVersionUID = -2001753273263821551L;

	private OrdenDePago ordenDePago;
	private NotaCreditoProveedor notaCredito;
	

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="F_ORDEN_PAGO_P_ID")
	public OrdenDePago getOrdenDePago() {
		return ordenDePago;
	}
	
	public void setOrdenDePago(OrdenDePago ordenDePago) {
		this.ordenDePago = ordenDePago;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="F_NOT_CRED_P_ID")
	public NotaCreditoProveedor getNotaCredito() {
		return notaCredito;
	}

	public void setNotaCredito(NotaCreditoProveedor notaCredito) {
		this.notaCredito = notaCredito;
	}

	@Override
	public void aceptarVisitor(IFilaMovimientoVisitor visitor) {
		visitor.visit(this);
	}

}