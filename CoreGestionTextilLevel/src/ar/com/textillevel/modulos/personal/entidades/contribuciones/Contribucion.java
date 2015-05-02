package ar.com.textillevel.modulos.personal.entidades.contribuciones;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.Cascade;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.Sindicato;

@Entity
@Table(name = "T_PERS_CONTRIBUCION")
public class Contribucion implements Serializable {

	private static final long serialVersionUID = 1221001435237320629L;

	private Integer id;
	private String nombre;
	private Sindicato sindicato;
	private List<PeriodoContribucion> periodos;

	public Contribucion() {
		this.periodos = new ArrayList<PeriodoContribucion>();
	}

	@Id
	@Column(name="P_ID")
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name="A_NOMBRE",nullable=false)
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@ManyToOne
	@JoinColumn(name="F_SINDICATO_P_ID")
	public Sindicato getSindicato() {
		return sindicato;
	}

	public void setSindicato(Sindicato sindicato) {
		this.sindicato = sindicato;
	}

	@OneToMany(cascade=CascadeType.ALL)
	@JoinColumn(name="F_CONTRIBUCION_P_ID")
	@Cascade(value=org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	public List<PeriodoContribucion> getPeriodos() {
		return periodos;
	}

	public void setPeriodos(List<PeriodoContribucion> periodos) {
		this.periodos = periodos;
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
		Contribucion other = (Contribucion) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return getNombre();
	}

}