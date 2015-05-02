package ar.com.textillevel.entidades.documentos.factura.itemfactura;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import ar.com.textillevel.entidades.enums.ETipoItemFactura;
import ar.com.textillevel.entidades.ventas.materiaprima.PrecioMateriaPrima;

@Entity
@DiscriminatorValue(value="ITSTOCK")
public class ItemFacturaPrecioMateriaPrima extends ItemFactura{

	private static final long serialVersionUID = 2910421279765176844L;
	
	private PrecioMateriaPrima precioMateriaPrima;

	@Override
	@Transient
	public ETipoItemFactura getTipo() {
		return ETipoItemFactura.STOCK;
	}
	
	@ManyToOne
	@JoinColumn(name="F_PRECIO_MP_P_ID",nullable=true)
	public PrecioMateriaPrima getPrecioMateriaPrima() {
		return precioMateriaPrima;
	}

	public void setPrecioMateriaPrima(PrecioMateriaPrima precioMateriaPrima) {
		this.precioMateriaPrima = precioMateriaPrima;
	}

}
