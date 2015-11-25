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
import ar.com.textillevel.entidades.ventas.articulos.Color;
import ar.com.textillevel.entidades.ventas.articulos.DibujoEstampado;
import ar.com.textillevel.entidades.ventas.articulos.GamaColor;
import ar.com.textillevel.entidades.ventas.articulos.VarianteEstampado;
import ar.com.textillevel.entidades.ventas.productos.Producto;

@Entity
@Table(name = "T_PRODUCTO_ARTICULO")
public class ProductoArticulo implements Serializable {

	private static final long serialVersionUID = 4968495793348305923L;

	private Integer id;
	private Producto producto;
	private Articulo articulo;
	private GamaColor gamaColor;
	private Color color;
	private DibujoEstampado dibujo;
	private VarianteEstampado variante;
	private Float precioCalculado;

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

	@ManyToOne
	@JoinColumn(name="F_GAMA_P_ID")
	@Fetch(FetchMode.JOIN)
	public GamaColor getGamaColor() {
		return gamaColor;
	}
	
	public void setGamaColor(GamaColor gamaColor) {
		this.gamaColor = gamaColor;
	}

	@ManyToOne
	@JoinColumn(name="F_COLOR_P_ID")
	@Fetch(FetchMode.JOIN)
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	@ManyToOne
	@JoinColumn(name="F_DIBUJO_P_ID")
	@Fetch(FetchMode.JOIN)
	public DibujoEstampado getDibujo() {
		return dibujo;
	}
	
	public void setDibujo(DibujoEstampado dibujo) {
		this.dibujo = dibujo;
	}
	
	@ManyToOne
	@JoinColumn(name="F_VARIANTE_P_ID")
	@Fetch(FetchMode.JOIN)
	public VarianteEstampado getVariante() {
		return variante;
	}
	
	public void setVariante(VarianteEstampado variante) {
		this.variante = variante;
	}

	@Transient
	public Float getPrecioCalculado() {
		return precioCalculado;
	}

	public void setPrecioCalculado(Float precioCalculado) {
		this.precioCalculado = precioCalculado;
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
		if(getTipo() != other.getTipo()) {
			return false;
		}
		if (articulo == null) {
			if (other.articulo != null)
				return false;
		} else if (!articulo.equals(other.articulo))
			return false;
		if(getTipo() == ETipoProducto.TENIDO) {
			return getGamaColor().equals(other.getGamaColor()) && getColor().equals(other.getColor()); 
		} else if(getTipo() == ETipoProducto.ESTAMPADO) {
			return getDibujo().equals(other.getDibujo()) && getVariante().equals(other.getVariante());
		}
		return true;
	}

	@Transient
	public String toString() {
		StringBuilder sb = new StringBuilder("");
		sb.append(getProducto());
		if(getTipo() == ETipoProducto.ESTAMPADO) {
			sb.append(" - " + getDibujo() + " - " + getVariante());
		}
		if(getTipo() == ETipoProducto.TENIDO) {
			sb.append(" - " + getColor());
		}
		sb.append((getArticulo() == null ? "":  (" - " + getArticulo())));
		return sb.toString();
	}

}