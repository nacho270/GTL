package ar.com.textillevel.entidades.portal;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "T_MODULO")
public class Modulo implements Serializable {

	private static final long serialVersionUID = -2481455069488182694L;

	private Integer id;
	private String nombre;
	private String className;
	private Boolean trigger;
	private List<Accion> acciones;
	private Boolean servicio;

	@Id
	@Column(name = "P_ID")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name = "A_NOMBRE", nullable = true)
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Column(name = "A_CLASS", nullable = true)
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
	
	@Column(name="A_TRIGGER", nullable=false, columnDefinition="INTEGER UNSIGNED DEFAULT 0")
	public Boolean getTrigger() {
		return trigger;
	}
	
	public void setTrigger(Boolean trigger) {
		this.trigger = trigger;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "T_MODULOS_ACCIONES", 
				joinColumns = {@JoinColumn(name = "F_MODULO_P_ID")}, 
				inverseJoinColumns = {@JoinColumn(name = "F_ACCION_P_ID")})
	public List<Accion> getAcciones() {
		return acciones;
	}

	public void setAcciones(List<Accion> acciones) {
		this.acciones = acciones;
	}
	
	@Column(name="A_SERVICIO", nullable=false, columnDefinition="INTEGER UNSIGNED DEFAULT 0")
	public Boolean getServicio() {
		return servicio;
	}

	public void setServicio(Boolean servicio) {
		this.servicio = servicio;
	}
	
	@Transient
	@Override
	public String toString(){
		return nombre;
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
		Modulo other = (Modulo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
