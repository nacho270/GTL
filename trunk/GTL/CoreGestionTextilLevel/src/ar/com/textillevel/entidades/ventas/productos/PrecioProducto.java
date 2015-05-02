package ar.com.textillevel.entidades.ventas.productos;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name="T_PRECIO_PRODUCTO")
public class PrecioProducto implements Serializable {

	private static final long serialVersionUID = 7845404449673025665L;

	private Integer id; 
	private Producto producto;
	private BigDecimal precio;
	private Timestamp fechaUltModif;

	public PrecioProducto() {
	//	this.fechaUltModif = DateUtil.getAhora();
	}

	@Id
	@Column(name="P_ID")
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne
	@JoinColumn(name="F_PRODUCTO_P_ID")
	@Fetch(FetchMode.JOIN)
	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	@Column(name="A_PRECIO", nullable=false)
	public BigDecimal getPrecio() {
		return precio;
	}

	public void setPrecio(BigDecimal precio) {
		this.precio = precio;
	}

	@Column(name="A_FECHA_ULT_MODIF", nullable=false)
	public Timestamp getFechaUltModif() {
		return fechaUltModif;
	}

	public void setFechaUltModif(Timestamp fechaUltModif) {
		this.fechaUltModif = fechaUltModif;
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
		final PrecioProducto other = (PrecioProducto) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}