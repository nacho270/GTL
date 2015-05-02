package ar.com.textillevel.entidades.gente;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="T_INFO_LOCALIDAD")
public class InfoLocalidad implements Serializable{

	private static final long serialVersionUID = -9092029032561755103L;

	private Integer id;
	private String nombreLocalidad;
	private Integer codigoPostal;
	private Integer codigoArea;
	
	@Id
	@Column(name="P_ID")
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name="A_NOMBRE_LOCALIDAD")
	public String getNombreLocalidad() {
		return nombreLocalidad;
	}
	
	public void setNombreLocalidad(String nombreLocalidad) {
		this.nombreLocalidad = nombreLocalidad;
	}
	
	@Column(name="A_COD_POSTAL")
	public Integer getCodigoPostal() {
		return codigoPostal;
	}
	
	public void setCodigoPostal(Integer codigoPostal) {
		this.codigoPostal = codigoPostal;
	}
	
	@Column(name="A_COD_AREA")
	public Integer getCodigoArea() {
		return codigoArea;
	}
	
	public void setCodigoArea(Integer codigoArea) {
		this.codigoArea = codigoArea;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigoArea == null) ? 0 : codigoArea.hashCode());
		result = prime * result + ((codigoPostal == null) ? 0 : codigoPostal.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((nombreLocalidad == null) ? 0 : nombreLocalidad.hashCode());
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
		InfoLocalidad other = (InfoLocalidad) obj;
		if (codigoArea == null) {
			if (other.codigoArea != null)
				return false;
		} else if (!codigoArea.equals(other.codigoArea))
			return false;
		if (codigoPostal == null) {
			if (other.codigoPostal != null)
				return false;
		} else if (!codigoPostal.equals(other.codigoPostal))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (nombreLocalidad == null) {
			if (other.nombreLocalidad != null)
				return false;
		} else if (!nombreLocalidad.equals(other.nombreLocalidad))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return nombreLocalidad;
	}
}
