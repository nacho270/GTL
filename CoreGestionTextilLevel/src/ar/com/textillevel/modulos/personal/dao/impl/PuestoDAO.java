package ar.com.textillevel.modulos.personal.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ar.clarin.fwjava.dao.impl.GenericDAO;
import ar.com.textillevel.modulos.personal.dao.api.PuestoDAOLocal;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.Puesto;

@Stateless
@SuppressWarnings("unchecked")
public class PuestoDAO extends GenericDAO<Puesto, Integer> implements PuestoDAOLocal {

	public List<Puesto> getAllByIdSindicato(Integer idSindicato) {
		String hql = "SELECT p From Puesto p WHERE p.sindicato.id = :idSindicato ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idSindicato", idSindicato);
		return (List<Puesto>)q.getResultList();
	}

}
