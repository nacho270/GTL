package ar.com.textillevel.entidades.documentos.factura.itemfactura;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import ar.com.textillevel.entidades.enums.ETipoItemFactura;
import ar.com.textillevel.entidades.ventas.productos.Producto;

@Entity
@DiscriminatorValue(value="ITP")
public class ItemFacturaProducto extends ItemFactura {
	
	private static final long serialVersionUID = -4304875269554264135L;

	private Producto producto;

	@ManyToOne
	@JoinColumn(name="F_PRODUCTO_P_ID")
	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	@Override
	@Transient
	public ETipoItemFactura getTipo() {
		return ETipoItemFactura.PRODUCTO;
	}
}
