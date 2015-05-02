package ar.com.textillevel.entidades.cuenta.movimientos;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import ar.com.textillevel.entidades.cuenta.movimientos.visitor.IFilaMovimientoVisitor;
import ar.com.textillevel.entidades.documentos.ordendedeposito.OrdenDeDeposito;

@Entity
@DiscriminatorValue(value = "MHB")
public class MovimientoHaberBanco extends MovimientoCuenta {

	private static final long serialVersionUID = 2603941476857317747L;

	private OrdenDeDeposito ordenDeDeposito;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="F_ODD_P_ID")
	public OrdenDeDeposito getOrdenDeDeposito() {
		return ordenDeDeposito;
	}

	public void setOrdenDeDeposito(OrdenDeDeposito ordenDeDeposito) {
		this.ordenDeDeposito = ordenDeDeposito;
	}
	
	@Override
	public void aceptarVisitor(IFilaMovimientoVisitor visitor) {
		visitor.visit(this);
	}
}
