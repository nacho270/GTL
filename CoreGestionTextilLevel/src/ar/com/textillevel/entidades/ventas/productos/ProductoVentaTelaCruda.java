package ar.com.textillevel.entidades.ventas.productos;

import java.io.Serializable;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import ar.com.textillevel.entidades.enums.ETipoProducto;
import ar.com.textillevel.entidades.ventas.productos.visitor.IProductoVisitor;

@Entity
@DiscriminatorValue("VTA_TELA_CRUD")
public class ProductoVentaTelaCruda extends Producto implements Serializable{

	private static final long serialVersionUID = 8997055146821125545L;

	@Override
	@Transient
	public void accept(IProductoVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	@Transient
	public ETipoProducto getTipo() {
		return ETipoProducto.VENTA_TELA_CRUDA;
	}
}
