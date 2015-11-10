package ar.com.textillevel.entidades.ventas.productos;

import java.io.Serializable;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import ar.com.textillevel.entidades.enums.ETipoProducto;
import ar.com.textillevel.entidades.ventas.productos.visitor.IProductoVisitor;

@Entity
@DiscriminatorValue("TENIDO")
public class ProductoTenido extends Producto implements Serializable {

	private static final long serialVersionUID = 2902009434349939354L;

	@Override
	@Transient
	public ETipoProducto getTipo() {
		return ETipoProducto.TENIDO;
	}
	
	@Transient
	public String getDescripcionParaFactura(){
		return this.getDescripcion().substring(0, this.getDescripcion().lastIndexOf('-')-1).trim().toUpperCase();
	}

	@Override
	@Transient
	public void accept(IProductoVisitor visitor) {
		visitor.visit(this);
	}

}
