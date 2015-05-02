package ar.com.textillevel.modulos.personal.entidades.recibosueldo.conceptos;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import ar.com.textillevel.modulos.personal.entidades.recibosueldo.conceptos.enums.ETipoConceptoReciboSueldo;

@Entity
@DiscriminatorValue(value = "CONC_HABER")
public class ConceptoReciboSueldoHaber extends ConceptoReciboSueldo {

	private static final long serialVersionUID = -7492884663218542898L;

	@Override
	@Transient
	public ETipoConceptoReciboSueldo getTipo() {
		return ETipoConceptoReciboSueldo.HABER;
	}

}
