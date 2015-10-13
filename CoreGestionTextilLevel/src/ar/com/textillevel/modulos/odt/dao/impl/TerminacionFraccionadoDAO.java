package ar.com.textillevel.modulos.odt.dao.impl;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ar.com.fwcommon.dao.impl.GenericDAO;
import ar.com.textillevel.modulos.odt.dao.api.local.TerminacionFraccionadoDAOLocal;
import ar.com.textillevel.modulos.odt.entidades.maquinas.TerminacionFraccionado;

@Stateless
public class TerminacionFraccionadoDAO extends GenericDAO<TerminacionFraccionado, Integer> implements TerminacionFraccionadoDAOLocal {

	public boolean existsTerminacionByNombre(TerminacionFraccionado terminacion) {
		String hql = "FROM TerminacionFraccionado t where (:id IS NULL OR t.id != :id) AND t.nombre=:nombre";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("id", terminacion.getId());
		q.setParameter("nombre", terminacion.getNombre());
		return !q.getResultList().isEmpty();
	}

}
