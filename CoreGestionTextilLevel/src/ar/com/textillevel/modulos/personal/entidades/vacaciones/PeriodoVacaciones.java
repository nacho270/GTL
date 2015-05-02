package ar.com.textillevel.modulos.personal.entidades.vacaciones;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="T_PERS_PERIODO_VACACIONES")
public class PeriodoVacaciones implements Serializable {

	private static final long serialVersionUID = 6965124154702189180L;

	private Integer id;
	private Integer cantidadDias;
	private Integer antiguedadAniosRequerida;

	@Id
	@Column(name="P_ID")
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name="A_CANT_DIAS",nullable=false)
	public Integer getCantidadDias() {
		return cantidadDias;
	}

	public void setCantidadDias(Integer cantidadDias) {
		this.cantidadDias = cantidadDias;
	}

	@Column(name="A_ANT_REQUERIDA",nullable=false)
	public Integer getAntiguedadAniosRequerida() {
		return antiguedadAniosRequerida;
	}

	public void setAntiguedadAniosRequerida(Integer antiguedadAniosRequerida) {
		this.antiguedadAniosRequerida = antiguedadAniosRequerida;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((antiguedadAniosRequerida == null) ? 0 : antiguedadAniosRequerida.hashCode());
		result = prime * result + ((cantidadDias == null) ? 0 : cantidadDias.hashCode());
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
		PeriodoVacaciones other = (PeriodoVacaciones) obj;
		if (antiguedadAniosRequerida == null) {
			if (other.antiguedadAniosRequerida != null)
				return false;
		} else if (!antiguedadAniosRequerida.equals(other.antiguedadAniosRequerida))
			return false;
		if (cantidadDias == null) {
			if (other.cantidadDias != null)
				return false;
		} else if (!cantidadDias.equals(other.cantidadDias))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return cantidadDias + " días";
	}
}
