package ar.com.textillevel.gui.modulos.personal.abm.conceptosrecibosueldo;

import ar.com.textillevel.modulos.personal.entidades.recibosueldo.conceptos.ConceptoReciboSueldo;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.conceptos.ConceptoReciboSueldoHaber;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.conceptos.ConceptoReciboSueldoRetencion;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.conceptos.enums.ETipoConceptoReciboSueldo;

public class ConceptoReciboSueldoFactory {
	public static ConceptoReciboSueldo createConcepto(ETipoConceptoReciboSueldo tipoConcepto){
		if(tipoConcepto == ETipoConceptoReciboSueldo.HABER) return new ConceptoReciboSueldoHaber();
		return new ConceptoReciboSueldoRetencion();
	}
}
