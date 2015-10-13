package ar.com.textillevel.modulos.personal.entidades.legajos;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import ar.com.fwcommon.entidades.Dia;
import ar.com.fwcommon.util.DateUtil;

@Embeddable
public class RangoDias implements Serializable {

	private static final long serialVersionUID = 7095484327188938404L;

	private Dia diaDesde;
	private Dia diaHasta;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="F_DIA_DESDE_P_ID")
	public Dia getDiaDesde() {
		return diaDesde;
	}

	public void setDiaDesde(Dia diaDesde) {
		this.diaDesde = diaDesde;
	}

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="F_DIA_HASTA_P_ID")
	public Dia getDiaHasta() {
		return diaHasta;
	}

	public void setDiaHasta(Dia diaHasta) {
		this.diaHasta = diaHasta;
	}
	
	@Override
	public String toString(){
		return diaDesde.getNombre() + " a " + diaHasta.getNombre();
	}

	@Transient
	public boolean seSolapaSinExtremos(RangoDias rango){
		return ( rango.getDiaDesde().getNroDia() > getDiaDesde().getNroDia() && rango.getDiaDesde().getNroDia() < getDiaHasta().getNroDia() ||
				 rango.getDiaHasta().getNroDia() > getDiaDesde().getNroDia() && rango.getDiaDesde().getNroDia() < getDiaDesde().getNroDia() ||
				 rango.getDiaDesde().getNroDia() < getDiaHasta().getNroDia() && rango.getDiaHasta().getNroDia() > getDiaHasta().getNroDia());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((diaDesde == null) ? 0 : diaDesde.hashCode());
		result = prime * result + ((diaHasta == null) ? 0 : diaHasta.hashCode());
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
		RangoDias other = (RangoDias) obj;
		if (diaDesde == null) {
			if (other.diaDesde != null)
				return false;
		} else if (!diaDesde.equals(other.diaDesde))
			return false;
		if (diaHasta == null) {
			if (other.diaHasta != null)
				return false;
		} else if (!diaHasta.equals(other.diaHasta))
			return false;
		return true;
	}

	@Transient
	public boolean contieneFecha(Date fecha) {
		int dia = DateUtil.getDiaSemana(fecha);
		return diaDesde.getNroDia()<= dia && dia <=diaHasta.getNroDia();
	}

}
