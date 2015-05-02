package ar.com.textillevel.modulos.personal.entidades.presentismo;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import ar.com.textillevel.modulos.personal.entidades.presentismo.visitor.IDescuentoPresentismoVisitor;

@Entity
@DiscriminatorValue(value = "DPA")
public class DescuentoPresentismoPorAusencia extends DescuentoPresentismo {

	private static final long serialVersionUID = 8736371055812464633L;

	private Integer cantidadFaltas;

	@Column(name = "A_CANT_FALTAS", nullable = true)
	public Integer getCantidadFaltas() {
		return cantidadFaltas;
	}

	public void setCantidadFaltas(Integer cantidadFaltas) {
		this.cantidadFaltas = cantidadFaltas;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cantidadFaltas == null) ? 0 : cantidadFaltas.hashCode());
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
		DescuentoPresentismoPorAusencia other = (DescuentoPresentismoPorAusencia) obj;
		if (cantidadFaltas == null) {
			if (other.cantidadFaltas != null)
				return false;
		} else if (!cantidadFaltas.equals(other.cantidadFaltas))
			return false;
		return true;
	}

	@Override
	@Transient
	public void visit(IDescuentoPresentismoVisitor visitor) {
		visitor.visit(this);
	}
}
