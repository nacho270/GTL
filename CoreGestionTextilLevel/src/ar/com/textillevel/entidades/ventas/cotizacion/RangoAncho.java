package ar.com.textillevel.entidades.ventas.cotizacion;

import java.io.Serializable;
import java.util.List;

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

import ar.com.textillevel.entidades.enums.ETipoProducto;
import ar.com.textillevel.entidades.enums.EUnidad;
import ar.com.textillevel.entidades.ventas.ProductoArticulo;
import ar.com.textillevel.entidades.ventas.articulos.Articulo;
import ar.com.textillevel.entidades.ventas.articulos.TipoArticulo;
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
	
	public String toStringConUnidad(EUnidad unidad) {
		if(getAnchoExacto() == null) {
			return "De " + getAnchoMinimo().toString() + " " + unidad.getDescripcion().toLowerCase() + " a " + getAnchoMaximo().toString() + " " + unidad.getDescripcion().toLowerCase();
		} else {
			return getAnchoExacto().toString() + " " + unidad.getDescripcion().toLowerCase();
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
	public Float getPrecioProducto(ProductoArticulo productoArticulo) {
		if(productoArticulo.getTipo() == ETipoProducto.DEVOLUCION || productoArticulo.getTipo() == ETipoProducto.REPROCESO_SIN_CARGO) {
			return new Float(0);
		}
		Articulo articulo = productoArticulo.getArticulo();
		if ( (articulo != null && getAnchoExacto() != null && getAnchoExacto().floatValue() == articulo.getAncho().floatValue()) ||
				(articulo != null && getAnchoMinimo() != null && getAnchoMaximo() != null && Utils.dentroDelRango(articulo.getAncho().floatValue(), getAnchoMinimo(), getAnchoMaximo())) ) {
			return buscarPrecio(productoArticulo);
		}
		return null;
	}

	@Transient
	public boolean enRango(Float valor) {
		return valor != null && (getAnchoExacto() != null && valor.equals(getAnchoExacto()) || Utils.dentroDelRango(valor, getAnchoMinimo(), getAnchoMaximo()));
	}
	
	@Transient
	public <G extends GrupoTipoArticulo> G getPrecioArticulo(TipoArticulo ta, Articulo articulo, Class<G> clazz) {
		if(articulo == null) {
			for(G pta : getGruposTipoArticulo(clazz)) {
				if(pta.getTipoArticulo().equals(ta)) {
					return pta;
				}
			}
			return null;
		} else {
			G ptaDefault = null;
			G ptaConArticulo = null;
			for(G p : getGruposTipoArticulo(clazz)) {
				if(p.getTipoArticulo().equals(ta)) {
					if(p.getArticulo() == null) {
						ptaDefault = p;
					}
					if(p.getArticulo() != null && p.getArticulo().equals(articulo)) {
						ptaConArticulo = p;
					}
				}
			}
			return ptaConArticulo != null ? ptaConArticulo : ptaDefault;
		}
	}


	@Transient
	protected abstract <G extends GrupoTipoArticulo> List<G> getGruposTipoArticulo(Class<G> clazz);

	@Transient
	protected abstract Float buscarPrecio(ProductoArticulo productoArticulo);

	@Transient
	public abstract boolean estaDefinido(Articulo art);

	@Transient
	public abstract RangoAncho deepClone(DefinicionPrecio def);
	
	@Transient
	public abstract void aumentarPrecios(float porcentajeAumento);

}