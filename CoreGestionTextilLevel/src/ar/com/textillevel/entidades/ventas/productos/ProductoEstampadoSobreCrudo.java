package ar.com.textillevel.entidades.ventas.productos;

import java.io.Serializable;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import ar.com.textillevel.entidades.enums.ETipoProducto;
import ar.com.textillevel.entidades.ventas.productos.visitor.IProductoVisitor;

@Entity
@DiscriminatorValue("ESTAM_CRUDO")
public class ProductoEstampadoSobreCrudo extends Producto implements Serializable {

	private static final long serialVersionUID = -1334903480856153780L;

	@Override
	@Transient
	public ETipoProducto getTipo() {
		return ETipoProducto.ESTAMPADO_SOBRE_CRUDO;
	}

	@Override
	@Transient
	public void accept(IProductoVisitor visitor) {
		visitor.visit(this);
	}
}
