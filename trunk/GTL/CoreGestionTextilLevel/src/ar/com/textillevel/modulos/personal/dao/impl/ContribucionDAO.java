package ar.com.textillevel.modulos.personal.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ar.clarin.fwjava.dao.impl.GenericDAO;
import ar.com.textillevel.modulos.personal.dao.api.ContribucionDAOLocal;
import ar.com.textillevel.modulos.personal.entidades.contribuciones.Contribucion;

@Stateless
public class ContribucionDAO extends GenericDAO<Contribucion, Integer> implements ContribucionDAOLocal {

	@SuppressWarnings("unchecked")
	public List<Contribucion> getAllByIdSindicato(Integer idSindicato) {
		String hql = "SELECT c From Contribucion c WHERE c.sindicato.id = :idSindicato ORDER BY c.nombre ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idSindicato", idSindicato);
		return q.getResultList();
	}

	public Contribucion getByIdEager(Integer id) {
		Contribucion contribucion = getById(id);
		contribucion.getPeriodos().size();
		return contribucion;
	}

}
