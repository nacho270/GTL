package ar.com.textillevel.entidades.cuentaarticulo.movimientos;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import ar.com.textillevel.entidades.cuentaarticulo.movimientos.visitor.IMovimientoCuentaArticuloVisitor;
import ar.com.textillevel.entidades.documentos.remito.RemitoEntrada;

@Entity
@DiscriminatorValue(value = "MH")
public class MovimientoHaberCuentaArticulo extends MovimientoCuentaArticulo {

	private static final long serialVersionUID = 1160388719785369390L;

	private RemitoEntrada remitoEntrada;

	public MovimientoHaberCuentaArticulo() {
		
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="F_REMITO_ENTRADA_P_ID")
	public RemitoEntrada getRemitoEntrada() {
		return remitoEntrada;
	}

	public void setRemitoEntrada(RemitoEntrada remitoEntrada) {
		this.remitoEntrada = remitoEntrada;
	}

	@Override
	public void aceptarVisitor(IMovimientoCuentaArticuloVisitor visitor) {
		visitor.visit(this);
	}


}
