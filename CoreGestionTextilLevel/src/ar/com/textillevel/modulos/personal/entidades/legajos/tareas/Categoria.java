package ar.com.textillevel.modulos.personal.entidades.legajos.tareas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "T_PERS_CATEGORIA")
public class Categoria implements Serializable {

	private static final long serialVersionUID = 838973209015863527L;

	private Integer id;
	private String nombre;
	private Sindicato sindicato;
	private List<Puesto> puestos;
	
	public Categoria() {
		this.puestos = new ArrayList<Puesto>();
	}
 
	@Id
	@Column(name = "P_ID")
	@GeneratedValue(strategy=GenerationType.AUTO)	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "A_NOMBRE", nullable=false)
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@ManyToOne
	@JoinColumn(name="F_SINDICATO_P_ID",nullable=false)
	public Sindicato getSindicato() {
		return sindicato;
	}

	public void setSindicato(Sindicato sindicato) {
		this.sindicato = sindicato;
	}

	@ManyToMany
	@JoinTable(name = "T_CATEGORIA_PUESTO_ASOC", 
			joinColumns = { @JoinColumn(name = "F_CATEGORIA_P_ID") }, 
			inverseJoinColumns = { @JoinColumn(name = "F_PUESTO_P_ID") })
	public List<Puesto> getPuestos() {
		return puestos;
	}

	public void setPuestos(List<Puesto> puestos) {
		this.puestos = puestos;
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
		Categoria other = (Categoria) obj;
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