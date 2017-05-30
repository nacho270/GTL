package ar.com.textillevel.entidades.ventas;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import ar.com.textillevel.entidades.enums.ETipoProducto;
import ar.com.textillevel.entidades.ventas.articulos.Articulo;
import ar.com.textillevel.entidades.ventas.productos.Producto;

@Embeddable
public class ProductoArticuloParcial implements Serializable, IProductoParaODT {

	private static final long serialVersionUID = 1L;
	private Producto producto;
	private Articulo articulo;
	
	@ManyToOne
	@JoinColumn(name="F_PRODUCTO_PARCIAL_P_ID",nullable=true)
	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	@ManyToOne
	@JoinColumn(name="F_ARTICULO_PARCIAL_P_ID",nullable=true)
	@Override
	public Articulo getArticulo() {
		return articulo;
	}

	public void setArticulo(Articulo articulo) {
		this.articulo = articulo;
	}

	@Override
	public String toString() {
		return getProducto() + " - " + getArticulo();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((articulo == null) ? 0 : articulo.hashCode());
		result = prime * result + ((producto == null) ? 0 : producto.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProductoArticuloParcial other = (ProductoArticuloParcial) obj;
		if (articulo == null) {
			if (other.articulo != null)
				return false;
		} else if (!articulo.equals(other.articulo))
			return false;
		if (producto == null) {
			if (other.producto != null)
				return false;
		} else if (!producto.equals(other.producto))
			return false;
		return true;
	}

	@Override
	@Transient
	public ETipoProducto getTipo() {
		return getProducto().getTipo();
	}

	@Override
	@Transient
	public String toStringSinProducto() {
		StringBuilder sb = new StringBuilder("");
		if(getTipo() == ETipoProducto.TENIDO) {
			sb.append("A Definir");
		}
		sb.append((getArticulo() == null ? "":  (" - " + getArticulo())));
		return sb.toString();
	}
	
}