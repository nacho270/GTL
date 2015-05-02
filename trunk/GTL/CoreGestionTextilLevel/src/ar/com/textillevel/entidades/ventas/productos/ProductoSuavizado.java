package ar.com.textillevel.entidades.ventas.productos;

import java.io.Serializable;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import ar.com.textillevel.entidades.enums.ETipoProducto;
import ar.com.textillevel.entidades.ventas.productos.visitor.IProductoVisitor;

@Entity
@DiscriminatorValue("SUAV")
public class ProductoSuavizado extends Producto implements Serializable{

	private static final long serialVersionUID = 8929618386488473058L;

	@Override
	@Transient
	public ETipoProducto getTipo() {
		return ETipoProducto.SUAVIZADO;
	}

	@Override
	@Transient
	public void accept(IProductoVisitor visitor) {
		visitor.visit(this);
	}

}
