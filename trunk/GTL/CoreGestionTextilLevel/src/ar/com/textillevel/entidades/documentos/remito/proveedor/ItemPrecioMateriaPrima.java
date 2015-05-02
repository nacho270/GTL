package ar.com.textillevel.entidades.documentos.remito.proveedor;

import java.math.BigDecimal;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import ar.com.textillevel.entidades.documentos.remito.proveedor.visitor.IItemRemitoSalidaVisitor;
import ar.com.textillevel.entidades.ventas.materiaprima.PrecioMateriaPrima;

@Entity
@DiscriminatorValue(value="IPMP")
public class ItemPrecioMateriaPrima extends ItemRemitoSalidaProveedor { 

	private static final long serialVersionUID = 8369461690245230046L;

	private PrecioMateriaPrima precioMateriaPrima;

	@ManyToOne
	@JoinColumn(name="F_PRECIO_MP_P_ID", nullable=true)
	public PrecioMateriaPrima getPrecioMateriaPrima() {
		return precioMateriaPrima;
	}

	public void setPrecioMateriaPrima(PrecioMateriaPrima precioMateriaPrima) {
		this.precioMateriaPrima = precioMateriaPrima;
	}

	@Override
	@Transient
	public BigDecimal getStockActual() {
		return precioMateriaPrima.getStockActual();
	}

	@Override
	public String toString() {
		return precioMateriaPrima.getMateriaPrima().getDescripcion() + " - " + precioMateriaPrima.getAlias();
	}

	@Override
	@Transient
	public void accept(IItemRemitoSalidaVisitor visitor) {
		visitor.visit(this);
	}

}
