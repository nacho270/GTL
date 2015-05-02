package ar.com.textillevel.entidades.cuenta.movimientos;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import ar.com.textillevel.entidades.cuenta.movimientos.visitor.IFilaMovimientoVisitor;
import ar.com.textillevel.entidades.documentos.pagopersona.FacturaPersona;

@Entity
@DiscriminatorValue(value = "MDPERS")
public class MovimientoDebePersona extends MovimientoCuenta{

	private static final long serialVersionUID = -8420883607875869447L;
	
	private FacturaPersona facturaPersona;
	
	@Override
	public void aceptarVisitor(IFilaMovimientoVisitor visitor) {
		visitor.visit(this);
	}

	@ManyToOne
	@JoinColumn(name="F_FACTURA_PERS_P_ID")
	public FacturaPersona getFacturaPersona() {
		return facturaPersona;
	}

	public void setFacturaPersona(FacturaPersona factura) {
		this.facturaPersona = factura;
	}
}
