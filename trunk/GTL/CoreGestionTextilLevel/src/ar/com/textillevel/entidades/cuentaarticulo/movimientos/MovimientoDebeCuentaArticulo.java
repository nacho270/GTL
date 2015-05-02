package ar.com.textillevel.entidades.cuentaarticulo.movimientos;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import ar.com.textillevel.entidades.cuentaarticulo.movimientos.visitor.IMovimientoCuentaArticuloVisitor;
import ar.com.textillevel.entidades.documentos.remito.RemitoSalida;

@Entity
@DiscriminatorValue(value = "MD")
public class MovimientoDebeCuentaArticulo extends MovimientoCuentaArticulo {

	private static final long serialVersionUID = -6308408361859720138L;

	private RemitoSalida remitoSalida;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="F_REMITO_SALIDA_P_ID")
	public RemitoSalida getRemitoSalida() {
		return remitoSalida;
	}

	public void setRemitoSalida(RemitoSalida remitoSalida) {
		this.remitoSalida = remitoSalida;
	}

	@Override
	public void aceptarVisitor(IMovimientoCuentaArticuloVisitor visitor) {
		visitor.visit(this);
	}

}
