package ar.com.textillevel.entidades.cuenta.movimientos;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import ar.com.textillevel.entidades.cuenta.movimientos.visitor.IFilaMovimientoVisitor;
import ar.com.textillevel.entidades.documentos.pagopersona.OrdenDePagoAPersona;

@Entity
@DiscriminatorValue(value = "MHPERS")
public class MovimientoHaberPersona extends MovimientoCuenta {

	private static final long serialVersionUID = 4385001245248696529L;

	private OrdenDePagoAPersona ordenDePago;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="F_ODP_PERS_P_ID")
	public OrdenDePagoAPersona getOrdenDePago() {
		return ordenDePago;
	}
	
	public void setOrdenDePago(OrdenDePagoAPersona ordenDePago) {
		this.ordenDePago = ordenDePago;
	}

	@Override
	public void aceptarVisitor(IFilaMovimientoVisitor visitor) {
		visitor.visit(this);
	}
}
