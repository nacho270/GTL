package ar.com.textillevel.entidades.ventas.cotizacion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import ar.com.textillevel.entidades.ventas.articulos.GamaColor;

@Entity
@Table(name = "T_GRUPO_TIPO_ARTICULO_BASE")
public class GrupoTipoArticuloBaseEstampado extends GrupoTipoArticulo implements Serializable, Comparable<GrupoTipoArticuloBaseEstampado> {

	private static final long serialVersionUID = 2124229339045732222L;

	private List<PrecioBaseEstampado> precios;
	private RangoAnchoArticuloEstampado rangoAnchoArticulo;

	public GrupoTipoArticuloBaseEstampado() {
		this.precios = new ArrayList<PrecioBaseEstampado>();
	}

	@OneToMany(cascade = {CascadeType.ALL}, fetch=FetchType.LAZY)
	@JoinColumn(name = "F_GRUPO_P_ID")
	@org.hibernate.annotations.Cascade(value = {org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
	public List<PrecioBaseEstampado> getPrecios() {
		return precios;
	}

	public void setPrecios(List<PrecioBaseEstampado> precios) {
		this.precios = precios;
	}

	@ManyToOne
	@JoinColumn(name = "F_RANGO_P_ID", updatable=false, insertable=false, nullable=false)
	public RangoAnchoArticuloEstampado getRangoAnchoArticulo() {
		return rangoAnchoArticulo;
	}

	public void setRangoAnchoArticulo(RangoAnchoArticuloEstampado rangoAnchoArticulo) {
		this.rangoAnchoArticulo = rangoAnchoArticulo;
	}
	
	@Transient
	public PrecioBaseEstampado getPrecioBase(GamaColor base) {
		for(PrecioBaseEstampado p : getPrecios()) {
			if(p.getGama().equals(base)) {
				return p;
			}
		}
		return null;
	}

	@Transient
	public int compareTo(GrupoTipoArticuloBaseEstampado o) {
		return getTipoArticulo().compareTo(o.getTipoArticulo());
	}

	@Transient
	public void deepOrderBy() {
		Collections.sort(getPrecios());
		for(PrecioBaseEstampado p : getPrecios()) {
			p.deepOrderBy();
		}
	}

}