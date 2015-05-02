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
@DiscriminatorValue(value="IRC")
public class ItemRelacionContenedor extends ItemRemitoSalidaProveedor {

	private static final long serialVersionUID = -3399076806864070407L;

	private RelacionContenedorPrecioMatPrima relacionContPrecioMP;
	
	public ItemRelacionContenedor() {
	}

	@ManyToOne
	@JoinColumn(name="F_REL_P_ID", nullable=true)
	public RelacionContenedorPrecioMatPrima getRelacionContPrecioMP() {
		return relacionContPrecioMP;
	}

	public void setRelacionContPrecioMP(RelacionContenedorPrecioMatPrima relacionContPrecioMP) {
		this.relacionContPrecioMP = relacionContPrecioMP;
	}

	@Override
	@Transient
	public BigDecimal getStockActual() {
		return relacionContPrecioMP.getStockActual();
	}

	@Override
	public String toString() {
		PrecioMateriaPrima precioMateriaPrima = relacionContPrecioMP.getPrecioMateriaPrima();
		return relacionContPrecioMP.getContenedor().getNombre() + " [" + precioMateriaPrima.getMateriaPrima().getDescripcion() + " - " + precioMateriaPrima.getAlias() + "]" ;
	}

	@Override
	@Transient
	public void accept(IItemRemitoSalidaVisitor visitor) {
		visitor.visit(this);
	}

}
