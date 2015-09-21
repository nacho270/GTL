package ar.com.textillevel.entidades.ventas.productos;

import java.io.Serializable;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import ar.com.textillevel.entidades.enums.ETipoProducto;
import ar.com.textillevel.entidades.ventas.productos.visitor.IProductoVisitor;

@Entity
@DiscriminatorValue("DESCR")
public class ProductoDescrude extends Producto implements Serializable {

	private static final long serialVersionUID = -6537413053638474122L;

	@Override
	@Transient
	public ETipoProducto getTipo() {
		return ETipoProducto.DESCRUDE;
	}

	@Override
	@Transient
	public void accept(IProductoVisitor visitor) {
		visitor.visit(this);
	}

}
