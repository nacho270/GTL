package ar.com.textillevel.entidades.cuenta.movimientos;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import ar.com.textillevel.entidades.cuenta.movimientos.visitor.IFilaMovimientoVisitor;
import ar.com.textillevel.entidades.documentos.factura.Factura;
import ar.com.textillevel.entidades.documentos.factura.NotaDebito;
import ar.com.textillevel.entidades.documentos.remito.RemitoSalida;

@Entity
@DiscriminatorValue(value = "MD")
public class MovimientoDebe extends MovimientoCuenta {

	private static final long serialVersionUID = -2977795836432042946L;

	private Factura factura;
	private NotaDebito notaDebito;
	private RemitoSalida remitoSalida;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="F_FACTURA_P_ID")
	public Factura getFactura() {
		return factura;
	}

	public void setFactura(Factura factura) {
		this.factura = factura;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="F_NOTA_DEBITO_P_ID")
	public NotaDebito getNotaDebito() {
		return notaDebito;
	}

	public void setNotaDebito(NotaDebito notaDebito) {
		this.notaDebito = notaDebito;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="F_REMITO_SALIDA_P_ID")
	public RemitoSalida getRemitoSalida() {
		return remitoSalida;
	}

	public void setRemitoSalida(RemitoSalida remitoSalida) {
		this.remitoSalida = remitoSalida;
	}

	@Override
	public void aceptarVisitor(IFilaMovimientoVisitor visitor) {
		visitor.visit(this);
	}
}
