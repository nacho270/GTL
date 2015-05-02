package ar.com.textillevel.entidades.ventas.productos;

import java.io.Serializable;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import ar.com.textillevel.entidades.enums.ETipoProducto;
import ar.com.textillevel.entidades.ventas.productos.visitor.IProductoVisitor;

@Entity
@DiscriminatorValue("REPROCESOCC")
public class ProductoReprocesoConCargo extends Producto implements Serializable {

	private static final long serialVersionUID = 3163833474463463300L;

	@Override
	public void accept(IProductoVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	@Transient
	public ETipoProducto getTipo() {
		return ETipoProducto.REPROCESO_CON_CARGO;
	}
}
