package ar.com.textillevel.modulos.personal.facade.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.com.textillevel.modulos.personal.dao.api.ConceptoReciboSueldoDAOLocal;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.Sindicato;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.conceptos.ConceptoReciboSueldo;
import ar.com.textillevel.modulos.personal.facade.api.remote.ConceptoReciboSueldoFacadeRemote;

@Stateless
public class ConceptoReciboSueldoFacade implements ConceptoReciboSueldoFacadeRemote{

	@EJB
	private ConceptoReciboSueldoDAOLocal conceptoDao;
	
	public ConceptoReciboSueldo save(ConceptoReciboSueldo concepto, boolean edicion) {
		if(edicion){
			conceptoDao.removeById(concepto.getId());
		}
		return conceptoDao.save(concepto);
	}

	public List<ConceptoReciboSueldo> getAllBySindicato(Sindicato sindicato) {
		return conceptoDao.getAllBySindicato(sindicato);
	}

	public void remove(ConceptoReciboSueldo concepto) {
		conceptoDao.removeById(concepto.getId());	
	}
}
