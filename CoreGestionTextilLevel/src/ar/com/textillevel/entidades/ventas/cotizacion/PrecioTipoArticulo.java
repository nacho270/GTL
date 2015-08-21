package ar.com.textillevel.entidades.ventas.cotizacion;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "T_PRECIO_TIPO_ARTICULO")
public class PrecioTipoArticulo extends GrupoTipoArticulo implements Serializable, Comparable<PrecioTipoArticulo> {

	private static final long serialVersionUID = 1487171810237263464L;

	private Float precio;
	private RangoAnchoComun rangoAncho;

	@Column(name = "A_PRECIO", nullable = false)
	public Float getPrecio() {
		return precio;
	}

	public void setPrecio(Float precio) {
		this.precio = precio;
	}

	@ManyToOne
	@JoinColumn(name="F_RANGO_P_ID", updatable=false, insertable=false, nullable=false)
	public RangoAnchoComun getRangoAncho() {
		return rangoAncho;
	}

	public void setRangoAncho(RangoAnchoComun rangoAncho) {
		this.rangoAncho = rangoAncho;
	}

	@Transient
	public void deepRemove() {
		rangoAncho.getPrecios().remove(this);
		if(rangoAncho.getPrecios().isEmpty()) {
			rangoAncho.getDefinicionPrecio().getRangos().remove(rangoAncho);
		}
	}

	@Transient
	public int compareTo(PrecioTipoArticulo o) {
		int res = getTipoArticulo().compareTo(o.getTipoArticulo());
		if(res == 0) {
			return getPrecio().compareTo(o.getPrecio());
		}
		return res;	
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((getTipoArticulo() == null) ? 0 : getTipoArticulo().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		PrecioTipoArticulo other = (PrecioTipoArticulo) obj;
		if (getTipoArticulo() == null) {
			if (other.getTipoArticulo() != null)
				return false;
		} else if (!getTipoArticulo().equals(other.getTipoArticulo()))
			return false;
		return true;
	}

}