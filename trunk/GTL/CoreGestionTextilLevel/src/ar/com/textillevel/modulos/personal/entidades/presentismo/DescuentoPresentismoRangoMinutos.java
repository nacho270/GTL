package ar.com.textillevel.modulos.personal.entidades.presentismo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import ar.com.textillevel.modulos.personal.entidades.presentismo.visitor.IDescuentoPresentismoVisitor;

@Entity
@DiscriminatorValue(value = "DPRM")
public class DescuentoPresentismoRangoMinutos extends DescuentoPresentismo implements Serializable {

	private static final long serialVersionUID = 3537399531824119629L;

	private Integer minutosDesde;
	private Integer minutosHasta;

	@Column(name = "A_MINUTOS_DESDE", nullable = true)
	public Integer getMinutosDesde() {
		return minutosDesde;
	}

	public void setMinutosDesde(Integer minutosDesde) {
		this.minutosDesde = minutosDesde;
	}

	@Column(name = "A_MINUTOS_HASTA", nullable = true)
	public Integer getMinutosHasta() {
		return minutosHasta;
	}

	public void setMinutosHasta(Integer minutosHasta) {
		this.minutosHasta = minutosHasta;
	}

	@Transient
	public boolean seSolapa(DescuentoPresentismoRangoMinutos descuento) {
		return descuento.getMinutosDesde() >= this.getMinutosDesde() && descuento.getMinutosDesde() <= this.getMinutosHasta() || descuento.getMinutosHasta() >= this.getMinutosDesde()
				&& descuento.getMinutosHasta() <= this.getMinutosHasta();
	}

	@Transient
	public boolean seSolapa(Integer minutosDesde, Integer minutosHasta) {
		return minutosDesde >= this.getMinutosDesde() && minutosDesde <= this.getMinutosHasta() || 
			   minutosHasta >= this.getMinutosDesde() && minutosHasta <= this.getMinutosHasta();
	}
	
	@Transient
	public boolean perteneceAlRango(Integer minutos){
		return minutos>=this.getMinutosDesde()&& minutos<=this.getMinutosHasta();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		DescuentoPresentismoRangoMinutos other = (DescuentoPresentismoRangoMinutos) obj;
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

	@Override
	@Transient
	public void visit(IDescuentoPresentismoVisitor visitor) {
		visitor.visit(this);
	}
}
