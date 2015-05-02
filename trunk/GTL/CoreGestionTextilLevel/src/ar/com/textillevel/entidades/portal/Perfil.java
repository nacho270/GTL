package ar.com.textillevel.entidades.portal;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;

import ar.com.textillevel.modulos.alertas.entidades.TipoAlerta;

@Entity
@Table(name = "T_PERFIL")
public class Perfil implements Serializable {

	private static final long serialVersionUID = 298466289583188264L;

	private Integer id;
	private String nombre;
	private List<AccionesModulo> accionesModulo;
	private List<Modulo> modulos;
	private List<TipoAlerta> tiposDeAlertas;
	private Boolean isAdmin;

	@Id
	@Column(name = "P_ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "A_NOMBRE", nullable = false)
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@OneToMany(cascade=CascadeType.ALL)
	@JoinColumn(name="F_PERFIL_P_ID")
	@Cascade(value=org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	public List<AccionesModulo> getAccionesModulo() {
		return accionesModulo;
	}

	public void setAccionesModulo(List<AccionesModulo> accionesModulo) {
		this.accionesModulo = accionesModulo;
	}

	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name = "T_PERFIL_MODULO", 
			joinColumns = { @JoinColumn(name = "F_PERFIL_P_ID") }, 
			inverseJoinColumns = { @JoinColumn(name = "F_MODULO_P_ID") })
	public List<Modulo> getModulos() {
		return modulos;
	}

	public void setModulos(List<Modulo> modulos) {
		this.modulos = modulos;
	}
	
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name = "T_PERFIL_TIPO_ALERTAS", 
			joinColumns = { @JoinColumn(name = "F_PERFIL_P_ID") }, 
			inverseJoinColumns = { @JoinColumn(name = "F_TIPO_ALERTA_P_ID") })
	public List<TipoAlerta> getTiposDeAlertas() {
		return tiposDeAlertas;
	}
	
	public void setTiposDeAlertas(List<TipoAlerta> tiposDeAlertas) {
		this.tiposDeAlertas = tiposDeAlertas;
	}
	
	@Column(name="A_ADMIN",nullable=false)
	public Boolean getIsAdmin() {
		return isAdmin;
	}
	
	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	
	@Transient
	@Override
	public String toString() {
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
		Perfil other = (Perfil) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
