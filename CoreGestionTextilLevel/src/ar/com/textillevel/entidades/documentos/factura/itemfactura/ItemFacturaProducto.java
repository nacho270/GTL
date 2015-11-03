package ar.com.textillevel.entidades.documentos.factura.itemfactura;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import ar.com.textillevel.entidades.enums.ETipoItemFactura;
import ar.com.textillevel.entidades.ventas.ProductoArticulo;

@Entity
@DiscriminatorValue(value="ITP")
public class ItemFacturaProducto extends ItemFactura {

	private static final long serialVersionUID = -4304875269554264135L;

	private ProductoArticulo productoArticulo;

	@ManyToOne
	@JoinColumn(name="F_PRODUCTO_ARTICULO_P_ID")
	public ProductoArticulo getProductoArticulo() {
		return productoArticulo;
	}

	public void setProductoArticulo(ProductoArticulo productoArticulo) {
		this.productoArticulo = productoArticulo;
	}

	@Override
	@Transient
	public ETipoItemFactura getTipo() {
		return ETipoItemFactura.PRODUCTO;
	}

}
