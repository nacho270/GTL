package ar.com.textillevel.entidades.ventas.cotizacion;

import java.io.Serializable;

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

import ar.com.textillevel.entidades.ventas.productos.Producto;
import ar.com.textillevel.util.Utils;

@Entity
@Table(name = "T_RANGO_ANCHO_ARTICULO")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING)
public abstract class RangoAncho implements Serializable, Comparable<RangoAncho> {

	private static final long serialVersionUID = 1181170583796051214L;
	
	private Integer id;
	private Float anchoMinimo; // nulleable, si el otro no es null
	private Float anchoMaximo; // nulleable, si el otro no es null
	private Float anchoExacto; // nulleable, si el otro no es null
	private DefinicionPrecio definicionPrecio;
	
	@Id
	@Column(name = "P_ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "A_ANCHO_MINIMO")
	public Float getAnchoMinimo() {
		return anchoMinimo;
	}

	public void setAnchoMinimo(Float anchoMinimo) {
		this.anchoMinimo = anchoMinimo;
	}

	@Column(name = "A_ANCHO_MAXIMO")
	public Float getAnchoMaximo() {
		return anchoMaximo;
	}

	public void setAnchoMaximo(Float anchoMaximo) {
		this.anchoMaximo = anchoMaximo;
	}

	@Column(name = "A_ANCHO_EXACTO")
	public Float getAnchoExacto() {
		return anchoExacto;
	}

	public void setAnchoExacto(Float anchoExacto) {
		this.anchoExacto = anchoExacto;
	}

	@ManyToOne
	@JoinColumn(name = "F_DEFINICION_PRECIO_P_ID", updatable=false, insertable=false, nullable=false)
	public DefinicionPrecio getDefinicionPrecio() {
		return definicionPrecio;
	}

	public void setDefinicionPrecio(DefinicionPrecio definicionPrecio) {
		this.definicionPrecio = definicionPrecio;
	}

	@Override
	public String toString() {
		if(getAnchoExacto() == null) {
			return "De " + getAnchoMinimo().toString() + " a " + getAnchoMaximo().toString();
		} else {
			return getAnchoExacto().toString();
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((anchoExacto == null) ? 0 : anchoExacto.hashCode());
		result = prime * result
				+ ((anchoMaximo == null) ? 0 : anchoMaximo.hashCode());
		result = prime * result
				+ ((anchoMinimo == null) ? 0 : anchoMinimo.hashCode());
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
		RangoAncho other = (RangoAncho) obj;
		if (anchoExacto == null) {
			if (other.anchoExacto != null)
				return false;
		} else if (!anchoExacto.equals(other.anchoExacto))
			return false;
		if (anchoMaximo == null) {
			if (other.anchoMaximo != null)
				return false;
		} else if (!anchoMaximo.equals(other.anchoMaximo))
			return false;
		if (anchoMinimo == null) {
			if (other.anchoMinimo != null)
				return false;
		} else if (!anchoMinimo.equals(other.anchoMinimo))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Transient
	public int compareTo(RangoAncho o) {
		Float thisdesde = getAnchoExacto() == null ? (getAnchoMinimo() == null ? getAnchoMaximo() : getAnchoMinimo()) : getAnchoExacto();  
		Float otherdesde = o.getAnchoExacto() == null ? (o.getAnchoMinimo() == null ? o.getAnchoMaximo() : o.getAnchoMinimo()) : o.getAnchoExacto();
		if(thisdesde != null && otherdesde != null) {
			return thisdesde.compareTo(otherdesde);
		}
		return 0;
	}

	@Transient
	public abstract void deepOrderBy();

	@Transient
	public Float getPrecioProducto(Producto producto) {
		if (getAnchoExacto() != null && getAnchoExacto().floatValue() == producto.getArticulo().getAncho().floatValue()) {
			return buscarPrecio(producto);
		} else if (getAnchoMinimo() != null && getAnchoMaximo() != null && Utils.dentroDelRango(producto.getArticulo().getAncho().floatValue(), getAnchoMinimo(), getAnchoMaximo())) {
			return buscarPrecio(producto);
		}
		return null;
	}

	@Transient
	public abstract Float buscarPrecio(Producto producto);
	
	
}