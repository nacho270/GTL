package ar.com.textillevel.modulos.personal.entidades.legajos.sanciones;

import java.io.Serializable;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.com.textillevel.modulos.personal.entidades.legajos.AccionHistorica;
import ar.com.textillevel.modulos.personal.entidades.legajos.visitor.IAccionHistoricaVisitor;

@Entity
@Table(name = "T_PERS_ACCION_SANCION")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TIPO", discriminatorType = DiscriminatorType.STRING)
public abstract class AccionSancion<T extends Sancion> extends AccionHistorica implements Serializable {

	private static final long serialVersionUID = 4434790016381044761L;

	private T sancion;

	@ManyToOne(fetch = FetchType.LAZY, targetEntity=Sancion.class)
	@JoinColumn(name = "F_SANCION_P_ID", nullable=false)
	public T getSancion() {
		return sancion;
	}

	public void setSancion(T sancion) {
		this.sancion = sancion;
	}

	public void accept(IAccionHistoricaVisitor visitor) {
		visitor.visit(this);
	}

}