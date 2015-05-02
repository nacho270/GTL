package ar.com.textillevel.entidades.ventas.productos;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import ar.com.textillevel.entidades.enums.ETipoProducto;
import ar.com.textillevel.entidades.ventas.articulos.Articulo;
import ar.com.textillevel.entidades.ventas.productos.visitor.IProductoVisitor;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TIPO", discriminatorType=DiscriminatorType.STRING)
@Table(name="T_PRODUCTO")
public abstract class Producto implements Serializable {

	private static final long serialVersionUID = -311441841585244454L;
	private Integer id;
	private String descripcion;
	private BigDecimal precioDefault;
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

	@Column(name="A_DESCR", nullable=false)
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	@Column(name="A_PRECIO", nullable=false)
	public BigDecimal getPrecioDefault() {
		return precioDefault;
	}

	public void setPrecioDefault(BigDecimal precioDefault) {
		this.precioDefault = precioDefault;
	}

	@ManyToOne
	@JoinColumn(name="F_ARTICULO_P_ID", nullable=true)//esto se puso como nulleable en true para poder poner el producto "reproceso sin cargo"
	@Fetch(FetchMode.JOIN)
	public Articulo getArticulo() {
		return articulo;
	}

	public void setArticulo(Articulo articulo) {
		this.articulo = articulo;
	}
	
	@Override
	@Transient
	public String toString(){
		return descripcion;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Producto other = (Producto) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	@Transient
	public abstract ETipoProducto getTipo();

	@Transient
	public abstract void accept(IProductoVisitor visitor);

}
