package ar.com.textillevel.entidades.documentos.factura.proveedor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import ar.com.textillevel.entidades.ventas.materiaprima.PrecioMateriaPrima;

@Entity
@DiscriminatorValue(value = "ICMP")
public class ItemCorreccionMateriaPrima extends ItemCorreccionFacturaProveedor {

	private static final long serialVersionUID = -252527615719471279L;

	private PrecioMateriaPrima precioMateriaPrima;

	public ItemCorreccionMateriaPrima() {
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

}
