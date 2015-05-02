package ar.com.textillevel.entidades.documentos.factura.proveedor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import ar.com.textillevel.entidades.documentos.factura.proveedor.visitor.IItemFacturaProveedorVisitor;
import ar.com.textillevel.entidades.ventas.materiaprima.PrecioMateriaPrima;

@Entity
@DiscriminatorValue(value = "ITPMP")
public class ItemFacturaMateriaPrima extends ItemFacturaProveedor {

	private static final long serialVersionUID = 457890719732098239L;

	private PrecioMateriaPrima precioMateriaPrima;

	public ItemFacturaMateriaPrima() {
		super();
	}

	@ManyToOne
	@JoinColumn(name="F_PRECIO_MAT_PRIMA_P_ID")
	public PrecioMateriaPrima getPrecioMateriaPrima() {
		return precioMateriaPrima;
	}

	public void setPrecioMateriaPrima(PrecioMateriaPrima precioMateriaPrima) {
		this.precioMateriaPrima = precioMateriaPrima;
	}

	@Override
	public void accept(IItemFacturaProveedorVisitor visitor) {
		visitor.visit(this);
	}

}