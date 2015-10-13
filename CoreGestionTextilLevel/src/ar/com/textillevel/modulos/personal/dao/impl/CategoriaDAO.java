package ar.com.textillevel.modulos.personal.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ar.com.fwcommon.dao.impl.GenericDAO;
import ar.com.textillevel.modulos.personal.dao.api.CategoriaDAOLocal;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.Categoria;

@Stateless
@SuppressWarnings("unchecked")
public class CategoriaDAO extends GenericDAO<Categoria, Integer> implements CategoriaDAOLocal {

	public List<Categoria> getAllByIdSindicato(Integer idSindicato) {
		String hql = "SELECT c From Categoria c WHERE c.sindicato.id = :idSindicato ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idSindicato", idSindicato);
		return q.getResultList();
	}

	public Categoria getByIdEager(Integer id) {
		Categoria cat = getById(id);
		cat.getPuestos().size();
		return cat;
	}

	public List<Categoria> getCategoriasPorPuesto(Integer idPuesto) {
		String hql = " SELECT c FROM Categoria c JOIN FETCH c.puestos p WHERE p.id = :idPuesto ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idPuesto", idPuesto);
		return q.getResultList();
	}

	public List<Categoria> getAllByIdSindicatoEager(Integer idSindicato) {
		String hql = "SELECT c From Categoria c WHERE c.sindicato.id = :idSindicato ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idSindicato", idSindicato);
		List<Categoria> resultList = q.getResultList();
		for(Categoria c : resultList) {
			c.getPuestos().size();
		}
		return resultList;
	}

}
