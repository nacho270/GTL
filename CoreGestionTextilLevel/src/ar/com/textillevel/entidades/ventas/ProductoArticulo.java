package ar.com.textillevel.entidades.ventas;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import ar.com.textillevel.entidades.enums.ETipoProducto;
import ar.com.textillevel.entidades.ventas.articulos.Articulo;
import ar.com.textillevel.entidades.ventas.productos.Producto;

@Entity
@Table(name = "T_PRODUCTO_ARTICULO")
public class ProductoArticulo implements Serializable {

	private static final long serialVersionUID = 4968495793348305923L;

	private Integer id;
	private Producto producto;
	private Articulo articulo;

	@Id
	@Column(name = "P_ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne
	@JoinColumn(name="F_PRODUCTO_P_ID", nullable=false)
	@Fetch(FetchMode.JOIN)
	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	@ManyToOne
	@JoinColumn(name="F_ARTICULO_P_ID", nullable=true) //Devolucion y reproceso sin cargo no tienen artículo!!!
	@Fetch(FetchMode.JOIN)
	public Articulo getArticulo() {
		return articulo;
	}

	public void setArticulo(Articulo articulo) {
		this.articulo = articulo;
	}

	@Transient
	public ETipoProducto getTipo() {
		return getProducto().getTipo();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((articulo == null) ? 0 : articulo.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((producto == null) ? 0 : producto.hashCode());
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
		ProductoArticulo other = (ProductoArticulo) obj;
		if (articulo == null) {
			if (other.articulo != null)
				return false;
		} else if (!articulo.equals(other.articulo))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (producto == null) {
			if (other.producto != null)
				return false;
		} else if (!producto.equals(other.producto))
			return false;
		return true;
	}

	@Transient
	public String toString() {
		return getProducto() + (getArticulo() == null ? "":  (" - " + getArticulo()));
	}

}