package ar.com.textillevel.entidades.stock;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import ar.com.textillevel.entidades.documentos.remito.proveedor.RemitoEntradaProveedor;
import ar.com.textillevel.entidades.stock.visitor.IMovimientoStockVisitor;

@Entity
@DiscriminatorValue(value = "MSS")
public class MovimientoStockSuma extends MovimientoStock {
	
	private static final long serialVersionUID = 8750601629049669540L;
	
	private RemitoEntradaProveedor remito;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="F_REMITO_ENT_PROV_P_ID", nullable=true)
	public RemitoEntradaProveedor getRemito() {
		return remito;
	}

	public void setRemito(RemitoEntradaProveedor remito) {
		this.remito = remito;
	}


	@Override
	@Transient
	public void aceptarVisitor(IMovimientoStockVisitor visitor) {
		visitor.visit(this);
	}

}