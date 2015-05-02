package ar.com.textillevel.modulos.personal.entidades.recibosueldo;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import ar.com.textillevel.modulos.personal.entidades.recibosueldo.conceptos.ConceptoReciboSueldoHaber;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.visitor.IItemReciboSueldoVisitor;

@Entity
@DiscriminatorValue(value="HAB")
public class ItemReciboSueldoHaber extends ItemReciboSueldo {

	private static final long serialVersionUID = 2544507798144039002L;

	private ConceptoReciboSueldoHaber conceptoHaber;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "F_CONCEPTO_HAB_P_ID", nullable=true)
	public ConceptoReciboSueldoHaber getConceptoHaber() {
		return conceptoHaber;
	}

	public void setConceptoHaber(ConceptoReciboSueldoHaber conceptoHaber) {
		this.conceptoHaber = conceptoHaber;
	}

	@Override
	public void aceptarVisitor(IItemReciboSueldoVisitor visitor) {
		visitor.visit(this);
	}

}
