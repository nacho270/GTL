package ar.com.textillevel.modulos.personal.facade.api.remote;

import java.util.List;

import javax.ejb.Remote;

import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.Sindicato;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.conceptos.ConceptoReciboSueldo;

@Remote
public interface ConceptoReciboSueldoFacadeRemote {
	public ConceptoReciboSueldo save(ConceptoReciboSueldo concepto, boolean edicion);
	public List<ConceptoReciboSueldo> getAllBySindicato(Sindicato sindicato);
	public void remove(ConceptoReciboSueldo concepto);
}
