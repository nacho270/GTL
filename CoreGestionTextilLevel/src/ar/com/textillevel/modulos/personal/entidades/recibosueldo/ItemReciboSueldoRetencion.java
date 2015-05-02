package ar.com.textillevel.modulos.personal.entidades.recibosueldo;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import ar.com.textillevel.modulos.personal.entidades.recibosueldo.conceptos.ConceptoReciboSueldoRetencion;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.visitor.IItemReciboSueldoVisitor;

@Entity
@DiscriminatorValue(value="RET")
public class ItemReciboSueldoRetencion extends ItemReciboSueldo {

	private static final long serialVersionUID = -3748733736789071406L;

	private ConceptoReciboSueldoRetencion conceptoRetencion;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "F_CONCEPTO_RET_P_ID", nullable=true)
	public ConceptoReciboSueldoRetencion getConceptoRetencion() {
		return conceptoRetencion;
	}

	public void setConceptoRetencion(ConceptoReciboSueldoRetencion conceptoRetencion) {
		this.conceptoRetencion = conceptoRetencion;
	}

	@Override
	public void aceptarVisitor(IItemReciboSueldoVisitor visitor) {
		visitor.visit(this);
	}

}
