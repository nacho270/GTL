package ar.com.textillevel.entidades.ventas.materiaprima;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import ar.com.textillevel.entidades.enums.ETipoMateriaPrima;
import ar.com.textillevel.entidades.enums.EUnidad;
import ar.com.textillevel.entidades.ventas.materiaprima.visitor.IMateriaPrimaVisitor;

@Entity
@Table(name = "T_MATERIA_PRIMA")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="TIPO", discriminatorType=DiscriminatorType.STRING)
public abstract class MateriaPrima implements Serializable {

	private static final long serialVersionUID = -6402053350665649358L;

	private Integer id;
	private String descripcion;
	private Integer idTipoUnidad;
	private String observaciones;
	private BigDecimal concentracion;
	private List<MateriaPrima> mpHijas; // esto es para marcar que MPs son iguales a esta
	private Integer idPadre; // mapeo para los querys
	
	public MateriaPrima() {
		this.mpHijas = new ArrayList<MateriaPrima>();
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "P_ID")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "A_DESCRIPCION", nullable = false, length = 50)
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	@Column(name = "A_UNIDAD", nullable = false)
	private Integer getIdTipoUnidad() {
		return idTipoUnidad;
	}

	private void setIdTipoUnidad(Integer idTipoUnidad) {
		this.idTipoUnidad = idTipoUnidad;
	}

	public void setUnidad(EUnidad unidad) {
		if (unidad == null) {
			this.setIdTipoUnidad(null);
		}
		setIdTipoUnidad(unidad.getId());
	}

	@Transient
	public EUnidad getUnidad() {
		if (getIdTipoUnidad() == null) {
			return null;
		}
		return EUnidad.getById(getIdTipoUnidad());
	}

	@Column(name="A_OBSERVACIONES", nullable=true,length=3000)
	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	
	@Column(name="A_CONCENTRACION",nullable=true)
	public BigDecimal getConcentracion() {
		return concentracion;
	}

	public void setConcentracion(BigDecimal concentracion) {
		this.concentracion = concentracion;
	}

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "F_MP_PADRE", nullable = true)
	public List<MateriaPrima> getMpHijas() {
		return mpHijas;
	}

	public void setMpHijas(List<MateriaPrima> mpHijas) {
		this.mpHijas = mpHijas;
	}
	
	@Column(name = "F_MP_PADRE", nullable = true, insertable = false, updatable = false)
	public Integer getIdPadre() {
		return idPadre;
	}

	protected void setIdPadre(Integer idPadre) {
		this.idPadre = idPadre;
	}
	
	@Override
	@Transient
	public String toString() {
		return getDescripcion();
	}

	@Transient
	public abstract void accept(IMateriaPrimaVisitor visitor);
	
	@Transient
	public abstract ETipoMateriaPrima getTipo();
	
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
		final MateriaPrima other = (MateriaPrima) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}