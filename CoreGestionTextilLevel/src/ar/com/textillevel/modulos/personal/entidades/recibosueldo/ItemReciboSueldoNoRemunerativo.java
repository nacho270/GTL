package ar.com.textillevel.modulos.personal.entidades.recibosueldo;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import ar.com.textillevel.modulos.personal.entidades.recibosueldo.asignaciones.Asignacion;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.visitor.IItemReciboSueldoVisitor;

@Entity
@DiscriminatorValue(value="NOREM")
public class ItemReciboSueldoNoRemunerativo extends ItemReciboSueldo {

	private static final long serialVersionUID = -3797310558622868907L;

	private Asignacion asignacionNoRem;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "F_ASIGNACION_P_ID", nullable=true)
	public Asignacion getAsignacionNoRem() {
		return asignacionNoRem;
	}

	public void setAsignacionNoRem(Asignacion asignacionNoRem) {
		this.asignacionNoRem = asignacionNoRem;
	}

	@Override
	public void aceptarVisitor(IItemReciboSueldoVisitor visitor) {
		visitor.visit(this);
	}

}
