package ar.com.textillevel.entidades.ventas.productos;

import java.io.Serializable;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import ar.com.textillevel.entidades.enums.ETipoProducto;
import ar.com.textillevel.entidades.ventas.productos.visitor.IProductoVisitor;

@Entity
@DiscriminatorValue("REVIS")
public class ProductoRevisado extends Producto implements Serializable{

	private static final long serialVersionUID = 8316857053817338487L;

	@Override
	public void accept(IProductoVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	@Transient
	public ETipoProducto getTipo() {
		return ETipoProducto.REVISADO;
	}
}
