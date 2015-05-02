package ar.com.textillevel.modulos.personal.entidades.legajos.valesdeatencion;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Horario implements Serializable {

	private static final long serialVersionUID = 4105770615865692742L;

	private Integer horas;
	private Integer minutos;

	@Column(name = "A_HORAS", nullable=true)
	public Integer getHoras() {
		return horas;
	}

	public void setHoras(Integer horas) {
		this.horas = horas;
	}

	@Column(name = "A_MINUTOS", nullable=true)
	public Integer getMinutos() {
		return minutos;
	}

	public void setMinutos(Integer minutos) {
		this.minutos = minutos;
	}

	@Override
	public String toString() {
		return (horas<=9?"0"+horas:horas) + ":" + (minutos<=9?"0"+minutos:minutos) ;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((horas == null) ? 0 : horas.hashCode());
		result = prime * result + ((minutos == null) ? 0 : minutos.hashCode());
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
		Horario other = (Horario) obj;
		if (horas == null) {
			if (other.horas != null)
				return false;
		} else if (!horas.equals(other.horas))
			return false;
		if (minutos == null) {
			if (other.minutos != null)
				return false;
		} else if (!minutos.equals(other.minutos))
			return false;
		return true;
	}

}
