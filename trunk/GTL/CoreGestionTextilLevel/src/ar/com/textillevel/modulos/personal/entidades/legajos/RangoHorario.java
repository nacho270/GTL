package ar.com.textillevel.modulos.personal.entidades.legajos;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

@Embeddable
public class RangoHorario implements Serializable {

	private static final long serialVersionUID = 179377064689128537L;

	private Integer horaDesde;
	private Integer minutosDesde;
	private Integer horaHasta;
	private Integer minutosHasta;

	@Column(name = "A_HORA_DESDE", nullable = false)
	public Integer getHoraDesde() {
		return horaDesde;
	}

	public void setHoraDesde(Integer horaDesde) {
		this.horaDesde = horaDesde;
	}

	@Column(name = "A_MINUTOS_DESDE", nullable = false)
	public Integer getMinutosDesde() {
		return minutosDesde;
	}

	public void setMinutosDesde(Integer minutosDesde) {
		this.minutosDesde = minutosDesde;
	}

	@Column(name = "A_HORA_HASTA", nullable = false)
	public Integer getHoraHasta() {
		return horaHasta;
	}

	public void setHoraHasta(Integer horaHasta) {
		this.horaHasta = horaHasta;
	}

	@Column(name = "A_MINUTOS_HASTA", nullable = false)
	public Integer getMinutosHasta() {
		return minutosHasta;
	}

	public void setMinutosHasta(Integer minutosHasta) {
		this.minutosHasta = minutosHasta;
	}

	@Override
	public String toString() {
		return (horaDesde<=9?"0"+horaDesde:horaDesde) + ":" + (minutosDesde<=9?"0"+minutosDesde:minutosDesde) + " a " + (horaHasta<=9?"0"+horaHasta:horaHasta) + ":" + (minutosHasta<=9?"0"+minutosHasta:minutosHasta);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((horaDesde == null) ? 0 : horaDesde.hashCode());
		result = prime * result + ((horaHasta == null) ? 0 : horaHasta.hashCode());
		result = prime * result + ((minutosDesde == null) ? 0 : minutosDesde.hashCode());
		result = prime * result + ((minutosHasta == null) ? 0 : minutosHasta.hashCode());
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
		RangoHorario other = (RangoHorario) obj;
		if (horaDesde == null) {
			if (other.horaDesde != null)
				return false;
		} else if (!horaDesde.equals(other.horaDesde))
			return false;
		if (horaHasta == null) {
			if (other.horaHasta != null)
				return false;
		} else if (!horaHasta.equals(other.horaHasta))
			return false;
		if (minutosDesde == null) {
			if (other.minutosDesde != null)
				return false;
		} else if (!minutosDesde.equals(other.minutosDesde))
			return false;
		if (minutosHasta == null) {
			if (other.minutosHasta != null)
				return false;
		} else if (!minutosHasta.equals(other.minutosHasta))
			return false;
		return true;
	}

	@Transient
	public Double getCantidadDeHoras(){
		return getHoraHasta()- getHoraDesde() + (getMinutosHasta()-getMinutosDesde())/60d;
	}

	@Transient
	public boolean seSolapaSinExtremos(RangoHorario rangoHorario) {
		return ( rangoHorario.getHoraDesde() > getHoraDesde() && rangoHorario.getHoraHasta() < getHoraHasta() ||
				 rangoHorario.getHoraDesde() > getHoraDesde() && rangoHorario.getHoraDesde() < getHoraHasta() && rangoHorario.getHoraHasta() > getHoraHasta() ||
				 rangoHorario.getHoraHasta() > getHoraDesde() && rangoHorario.getHoraDesde() < getHoraDesde() );
	}
}
