package ar.com.textillevel.modulos.personal.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ar.com.fwcommon.dao.impl.GenericDAO;
import ar.com.textillevel.modulos.personal.dao.api.ConceptoReciboSueldoDAOLocal;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.Sindicato;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.conceptos.ConceptoReciboSueldo;

@Stateless
public class ConceptoReciboSueldoDAO extends GenericDAO<ConceptoReciboSueldo, Integer> implements ConceptoReciboSueldoDAOLocal{

	@SuppressWarnings("unchecked")
	public List<ConceptoReciboSueldo> getAllBySindicato(Sindicato sindicato) {
		String hql = " SELECT c FROM ConceptoReciboSueldo c WHERE c.sindicato.id = :idSindicato ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idSindicato", sindicato.getId());
		return q.getResultList();
	}
}
