package ar.com.textillevel.entidades.ventas.productos;

import java.io.Serializable;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import ar.com.textillevel.entidades.enums.ETipoProducto;
import ar.com.textillevel.entidades.ventas.productos.visitor.IProductoVisitor;

@Entity
@DiscriminatorValue("IMPERM")
public class ProductoImpermeabilizado extends Producto implements Serializable{

	private static final long serialVersionUID = 1636533700616338154L;

	@Override
	@Transient
	public ETipoProducto getTipo() {
		return ETipoProducto.IMPERMEABILIZADO;
	}

	@Override
	@Transient
	public void accept(IProductoVisitor visitor) {
		visitor.visit(this);
	}

}
