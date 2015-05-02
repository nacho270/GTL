package ar.com.textillevel.modulos.odt.entidades.maquinas.formulas;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import ar.com.textillevel.entidades.enums.EUnidad;
import ar.com.textillevel.entidades.ventas.materiaprima.Formulable;
import ar.com.textillevel.entidades.ventas.materiaprima.MateriaPrima;

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public abstract class MateriaPrimaCantidad<T extends Formulable> implements Serializable {

	private static final long serialVersionUID = -2497333504505284981L;

	private Integer id;
	private T materiaPrima;
	private Float cantidad;
//	private Float cantidadExplotada;
	private Integer idTipoUnidad;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
	@Column(name = "P_ID")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne(targetEntity=MateriaPrima.class)
	@JoinColumn(name="F_MATERIA_PRIMA_P_ID", nullable=false)
	@Fetch(FetchMode.JOIN)
	public T getMateriaPrima() {
		return materiaPrima;
	}

	public void setMateriaPrima(T materiaPrima) {
		this.materiaPrima = materiaPrima;
	}

	@Column(name = "A_CANTIDAD", nullable=false)
	public Float getCantidad() {
		return cantidad;
	}

	public void setCantidad(Float cantidad) {
		this.cantidad = cantidad;
	}

//	@Column(name = "A_CANTIDAD_EXPLOTADA", nullable=true)
//	public Float getCantidadExplotada() {
//		return cantidadExplotada;
//	}
//	
//	public void setCantidadExplotada(Float cantidadExplotada) {
//		this.cantidadExplotada = cantidadExplotada;
//	}

	@Column(name = "A_UNIDAD", nullable = true)
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

	@Transient
	public String getDescripcion() {
		return getMateriaPrima().getDescripcion();
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
		@SuppressWarnings("rawtypes")
		MateriaPrimaCantidad other = (MateriaPrimaCantidad) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}