package ar.com.textillevel.entidades.ventas.cotizacion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import ar.com.textillevel.entidades.ventas.articulos.GamaColor;
import ar.com.textillevel.util.Utils;

@Entity
@Table(name = "T_PRECIO_BASE")
public class PrecioBaseEstampado implements Serializable, Comparable<PrecioBaseEstampado> {

	private static final long serialVersionUID = 1156776031396199794L;

	private Integer id;

	private GamaColor gama;
	private List<RangoCantidadColores> rangosDeColores;
	private GrupoTipoArticuloBaseEstampado grupoTipoArticuloBase;

	public PrecioBaseEstampado() {
		this.rangosDeColores = new ArrayList<RangoCantidadColores>();
	}

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
	@JoinColumn(name = "F_GAMA_P_ID", nullable =  false)
	public GamaColor getGama() {
		return gama;
	}

	public void setGama(GamaColor gama) {
		this.gama = gama;
	}

	@OneToMany(cascade = {CascadeType.ALL}, fetch=FetchType.LAZY)
	@JoinColumn(name = "F_PRECIO_BASE_P_ID")
	@org.hibernate.annotations.Cascade(value = {org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
	public List<RangoCantidadColores> getRangosDeColores() {
		return rangosDeColores;
	}

	public void setRangosDeColores(List<RangoCantidadColores> rangosDeColores) {
		this.rangosDeColores = rangosDeColores;
	}

	@ManyToOne
	@JoinColumn(name = "F_GRUPO_P_ID", updatable=false, insertable=false, nullable=false)
	public GrupoTipoArticuloBaseEstampado getGrupoTipoArticuloBase() {
		return grupoTipoArticuloBase;
	}

	public void setGrupoTipoArticuloBase(GrupoTipoArticuloBaseEstampado grupoTipoArticuloBase) {
		this.grupoTipoArticuloBase = grupoTipoArticuloBase;
	}

	@Transient
	public RangoCantidadColores getRango(Integer minCantColores, Integer maxCantColores) {
		for(RangoCantidadColores r : getRangosDeColores()) {
			if(minCantColores != null && Utils.dentroDelRango(minCantColores, r.getMinimo(), r.getMaximo())) {
				return r;
			}
			if(maxCantColores != null && Utils.dentroDelRango(maxCantColores, r.getMinimo(), r.getMaximo())) {
				return r;
			}
		}
		return null;
	}

	@Transient
	public RangoCantidadColores getRangoSolapadoCon(Integer minCantColores, Integer maxCantColores) {
		for(RangoCantidadColores r : getRangosDeColores()) {
			if(minCantColores != null && Utils.dentroDelRangoEstricto(minCantColores, r.getMinimo(), r.getMaximo())) {
				return r;
			}
			if(maxCantColores != null && Utils.dentroDelRangoEstricto(maxCantColores, r.getMinimo(), r.getMaximo())) {
				return r;
			}
		}
		return null;
	}
	
	@Transient
	public int compareTo(PrecioBaseEstampado o) {
		return getGama().compareTo(o.getGama());
	}

	@Transient
	public void deepOrderBy() {
		Collections.sort(getRangosDeColores());
		for(RangoCantidadColores r : getRangosDeColores()) {
			r.deepOrderBy();
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((gama == null) ? 0 : gama.hashCode());
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
		PrecioBaseEstampado other = (PrecioBaseEstampado) obj;
		if (gama == null) {
			if (other.gama != null)
				return false;
		} else if (!gama.equals(other.gama))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}