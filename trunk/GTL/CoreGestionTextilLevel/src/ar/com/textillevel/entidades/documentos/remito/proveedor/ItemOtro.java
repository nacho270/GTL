package ar.com.textillevel.entidades.documentos.remito.proveedor;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import ar.com.textillevel.entidades.documentos.remito.proveedor.visitor.IItemRemitoSalidaVisitor;

@Entity
@DiscriminatorValue(value="IO")
public class ItemOtro extends ItemRemitoSalidaProveedor {

	private static final long serialVersionUID = 742593111425936418L;

	private String descripcion;

	public ItemOtro() {
	}

	@Column(name="A_DESCR", nullable=true)
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	@Override
	public String toString() {
		return getDescripcion();
	}

	@Override
	@Transient
	public void accept(IItemRemitoSalidaVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	@Transient
	public BigDecimal getStockActual() {
		return new BigDecimal(0);
	}

}