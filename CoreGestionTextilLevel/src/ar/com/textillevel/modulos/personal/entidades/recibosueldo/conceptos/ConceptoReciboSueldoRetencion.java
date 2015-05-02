package ar.com.textillevel.modulos.personal.entidades.recibosueldo.conceptos;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import ar.com.textillevel.modulos.personal.entidades.recibosueldo.conceptos.enums.ETipoConceptoReciboSueldo;

@Entity
@DiscriminatorValue(value = "CONC_RET")
public class ConceptoReciboSueldoRetencion extends ConceptoReciboSueldo {

	private static final long serialVersionUID = 4989941318200195421L;

	@Override
	@Transient
	public ETipoConceptoReciboSueldo getTipo() {
		return ETipoConceptoReciboSueldo.RETENCION;
	}

}
