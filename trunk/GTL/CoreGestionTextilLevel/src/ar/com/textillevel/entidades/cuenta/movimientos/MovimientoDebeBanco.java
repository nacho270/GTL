package ar.com.textillevel.entidades.cuenta.movimientos;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import ar.com.textillevel.entidades.cuenta.movimientos.visitor.IFilaMovimientoVisitor;

@Entity
@DiscriminatorValue(value = "MDB")
public class MovimientoDebeBanco extends MovimientoCuenta{

	private static final long serialVersionUID = -3733860420098360940L;

	@Override
	public void aceptarVisitor(IFilaMovimientoVisitor visitor) {
		visitor.visit(this);
	}

}
