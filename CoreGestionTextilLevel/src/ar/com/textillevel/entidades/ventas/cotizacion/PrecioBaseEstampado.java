package ar.com.textillevel.entidades.ventas.cotizacion;

import java.io.Serializable;
import java.util.ArrayList;
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

@Entity
@Table(name = "T_PRECIO_BASE")
public class PrecioBaseEstampado implements Serializable {

	private static final long serialVersionUID = 1156776031396199794L;

	private Integer id;
	private GamaColor gama;
	private List<RangoCantidadColores> rangosDeColores;

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

	@Transient
	public RangoCantidadColores getRango(Integer minCantColores, Integer maxCantColores) {
		for(RangoCantidadColores r : getRangosDeColores()) {
			if(minCantColores != null && r.getMinimo() != null && minCantColores.equals(r.getMinimo()) && maxCantColores != null && r.getMaximo() != null && maxCantColores.equals(r.getMaximo())) {
				return r;
			}
		}
		return null;
	}

}