package ar.com.textillevel.entidades.ventas.cotizacion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

import ar.com.textillevel.util.Utils;

@Entity
@Table(name = "T_RANGO_CANTIDAD_COLORES")
public class RangoCantidadColores implements Serializable, Comparable<RangoCantidadColores> {

	private static final long serialVersionUID = -1466816699070964522L;

	private Integer id;
	private Integer minimo;
	private Integer maximo;
	private List<RangoCoberturaEstampado> rangos;
	private PrecioBaseEstampado precioBase;

	public RangoCantidadColores() {
		this.rangos = new ArrayList<RangoCoberturaEstampado>();
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

	@Column(name = "A_MINIMO", nullable = false)
	public Integer getMinimo() {
		return minimo;
	}

	public void setMinimo(Integer minimo) {
		this.minimo = minimo;
	}

	@Column(name = "A_MAXIMO", nullable = false)
	public Integer getMaximo() {
		return maximo;
	}

	public void setMaximo(Integer maximo) {
		this.maximo = maximo;
	}

	@OneToMany(cascade = {CascadeType.ALL}, fetch=FetchType.LAZY)
	@JoinColumn(name = "F_RANGO_CANT_COLORES_P_ID", nullable=false)
	@org.hibernate.annotations.Cascade(value = {org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
	public List<RangoCoberturaEstampado> getRangos() {
		return rangos;
	}

	public void setRangos(List<RangoCoberturaEstampado> rangos) {
		this.rangos = rangos;
	}

	@ManyToOne
	@JoinColumn(name = "F_PRECIO_BASE_P_ID", updatable=false, insertable=false, nullable=false)
	public PrecioBaseEstampado getPrecioBase() {
		return precioBase;
	}

	public void setPrecioBase(PrecioBaseEstampado precioBase) {
		this.precioBase = precioBase;
	}

	@Transient
	public List<RangoCoberturaEstampado> getRangoCobertura(Integer minCobertura, Integer maxCobertura) {
		Set<RangoCoberturaEstampado> rangos = new HashSet<RangoCoberturaEstampado>();
		for(RangoCoberturaEstampado r : getRangos()) {
			if(minCobertura != null && Utils.dentroDelRango(minCobertura, r.getMinimo(), r.getMaximo())) {
				rangos.add(r);
			}
			if(maxCobertura != null && Utils.dentroDelRango(maxCobertura, r.getMinimo(), r.getMaximo())) {
				rangos.add(r);
			}
		}
		return new ArrayList<RangoCoberturaEstampado>(rangos);
	}

	@Transient
	public RangoCoberturaEstampado getRangoCoberturaSolapadoCon(Integer minCobertura, Integer maxCobertura) {
		for(RangoCoberturaEstampado r : getRangos()) {
			if(minCobertura != null && Utils.dentroDelRangoEstricto(minCobertura, r.getMinimo(), r.getMaximo())) {
				return r;
			}
			if(maxCobertura != null && Utils.dentroDelRangoEstricto(maxCobertura, r.getMinimo(), r.getMaximo())) {
				return r;
			}
		}
		return null;
	}
	
	@Override
	public String toString() {
		return "De " + getMinimo().toString() + " a " + getMaximo().toString();
	}

	@Transient
	public int compareTo(RangoCantidadColores o) {
		Integer thisMin = getMinimo() == null ? getMaximo() : getMinimo();
		Integer otherMin = o.getMinimo() == null ? o.getMaximo() : o.getMinimo();
		if(thisMin != null && otherMin != null) {
			return thisMin.compareTo(otherMin);
		}
		return 0;
	}

	@Transient
	public void deepOrderBy() {
		Collections.sort(getRangos());
	}

	@Transient
	public Float getPrecio(Float porcentajeCobertura) {
		for(RangoCoberturaEstampado r : getRangos()) {
			if(porcentajeCobertura.intValue() >= r.getMinimo().floatValue() && porcentajeCobertura.intValue() <= r.getMaximo().floatValue()) {
				return r.getPrecio(); 
			}
		}
		return null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((maximo == null) ? 0 : maximo.hashCode());
		result = prime * result + ((minimo == null) ? 0 : minimo.hashCode());
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
		RangoCantidadColores other = (RangoCantidadColores) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (maximo == null) {
			if (other.maximo != null)
				return false;
		} else if (!maximo.equals(other.maximo))
			return false;
		if (minimo == null) {
			if (other.minimo != null)
				return false;
		} else if (!minimo.equals(other.minimo))
			return false;
		return true;
	}

	@Transient
	public RangoCantidadColores deepClone(PrecioBaseEstampado precio) {
		RangoCantidadColores rango = new RangoCantidadColores();
		rango.setMaximo(getMaximo());
		rango.setMinimo(getMinimo());
		rango.setPrecioBase(precio);
		for(RangoCoberturaEstampado rce : getRangos()) {
			rango.getRangos().add(rce.deepClone(rango));
		}
		return rango;
	}

	@Transient
	public void aumentarPrecios(float porcentajeAumento) {
		for(RangoCoberturaEstampado rce : getRangos()) {
			rce.setPrecio(rce.getPrecio() + ( (rce.getPrecio() * porcentajeAumento) / 100) );
		}
	}

}