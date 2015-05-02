package ar.com.textillevel.entidades.cuenta.movimientos;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import ar.com.textillevel.entidades.cuenta.movimientos.visitor.IFilaMovimientoVisitor;
import ar.com.textillevel.entidades.documentos.factura.proveedor.FacturaProveedor;
import ar.com.textillevel.entidades.documentos.factura.proveedor.NotaDebitoProveedor;

@Entity
@DiscriminatorValue(value="MDP")
public class MovimientoDebeProveedor extends MovimientoCuenta {

	private static final long serialVersionUID = -8917914495099132550L;

	private FacturaProveedor facturaProveedor;
	private NotaDebitoProveedor notaDebitoProveedor;


	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="F_FACTURA_PROV_P_ID")
	public FacturaProveedor getFacturaProveedor() {
		return facturaProveedor;
	}

	public void setFacturaProveedor(FacturaProveedor facturaProveedor) {
		this.facturaProveedor = facturaProveedor;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="F_NOT_DEB_PROV_P_ID")
	public NotaDebitoProveedor getNotaDebitoProveedor() {
		return notaDebitoProveedor;
	}

	public void setNotaDebitoProveedor(NotaDebitoProveedor notaDebitoProveedor) {
		this.notaDebitoProveedor = notaDebitoProveedor;
	}

	@Override
	public void aceptarVisitor(IFilaMovimientoVisitor visitor) {
		visitor.visit(this);
	}

}
