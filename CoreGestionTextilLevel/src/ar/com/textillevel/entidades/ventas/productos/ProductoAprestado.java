package ar.com.textillevel.entidades.ventas.productos;

import java.io.Serializable;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import ar.com.textillevel.entidades.enums.ETipoProducto;
import ar.com.textillevel.entidades.ventas.productos.visitor.IProductoVisitor;

@Entity
@DiscriminatorValue("APREST")
public class ProductoAprestado extends Producto implements Serializable {

	private static final long serialVersionUID = -1457466752729735824L;

	@Override
	public void accept(IProductoVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	@Transient
	public ETipoProducto getTipo() {
		return ETipoProducto.APRESTADO;
	}

}
