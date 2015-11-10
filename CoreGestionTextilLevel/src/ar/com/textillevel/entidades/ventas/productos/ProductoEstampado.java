package ar.com.textillevel.entidades.ventas.productos;

import java.io.Serializable;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import ar.com.textillevel.entidades.enums.ETipoProducto;
import ar.com.textillevel.entidades.ventas.productos.visitor.IProductoVisitor;

@Entity
@DiscriminatorValue("ESTAMPADO")
public class ProductoEstampado extends Producto implements Serializable {
	
	private static final long serialVersionUID = 2536700659829607857L;

	public ProductoEstampado() {
		super();
	}

	@Override
	@Transient
	public ETipoProducto getTipo() {
		return ETipoProducto.ESTAMPADO;
	}

	@Override
	@Transient
	public void accept(IProductoVisitor visitor) {
		visitor.visit(this);
	}

}
