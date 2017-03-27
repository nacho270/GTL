package ar.com.textillevel.modulos.terminal.entidades;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "T_MODULO_TERMINAL")
public class ModuloTerminal implements Serializable {

	private static final long serialVersionUID = 3864350864307799347L;

	private Integer id;
	private String nombre;
	private String clase;
	private Boolean requiereLogin;

	@Id
	@Column(name = "P_ID", nullable = false)
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

	@Column(name = "A_CLASE", nullable = false)
	public String getClase() {
		return clase;
	}

	public void setClase(String clase) {
		this.clase = clase;
	}

	@Column(name = "A_REQUIERE_LOGIN", nullable = false)
	public Boolean getRequiereLogin() {
		return requiereLogin;
	}

	public void setRequiereLogin(Boolean requiereLogin) {
		this.requiereLogin = requiereLogin;
	}

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
		ModuloTerminal other = (ModuloTerminal) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
