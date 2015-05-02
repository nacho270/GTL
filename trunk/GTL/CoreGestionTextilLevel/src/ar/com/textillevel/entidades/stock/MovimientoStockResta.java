package ar.com.textillevel.entidades.stock;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import ar.com.textillevel.entidades.documentos.factura.Factura;
import ar.com.textillevel.entidades.documentos.remito.RemitoSalida;
import ar.com.textillevel.entidades.stock.visitor.IMovimientoStockVisitor;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;

@Entity
@DiscriminatorValue(value = "MSR")
public class MovimientoStockResta extends MovimientoStock {

	private static final long serialVersionUID = 2517892902555596402L;

	private Factura factura;
	private RemitoSalida remitoSalida;
	private OrdenDeTrabajo odt;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="F_FACTURA_P_ID", nullable=true)
	public Factura getFactura() {
		return factura;
	}

	public void setFactura(Factura factura) {
		this.factura = factura;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="F_REMITO_SAL_PROV_P_ID", nullable=true)
	public RemitoSalida getRemitoSalida() {
		return remitoSalida;
	}

	public void setRemitoSalida(RemitoSalida remitoSalida) {
		this.remitoSalida = remitoSalida;
	}

	@Override
	@Transient	
	public void aceptarVisitor(IMovimientoStockVisitor visitor) {
		visitor.visit(this);
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="F_ODT_P_ID", nullable=true)
	public OrdenDeTrabajo getOdt() {
		return odt;
	}
	
	public void setOdt(OrdenDeTrabajo odt) {
		this.odt = odt;
	}
}
