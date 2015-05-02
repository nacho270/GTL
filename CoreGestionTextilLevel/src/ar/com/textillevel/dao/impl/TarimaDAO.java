package ar.com.textillevel.dao.impl;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

import ar.clarin.fwjava.dao.impl.GenericDAO;
import ar.com.textillevel.dao.api.local.TarimaDAOLocal;
import ar.com.textillevel.entidades.documentos.remito.Tarima;

@Stateless
public class TarimaDAO extends GenericDAO<Tarima, Integer> implements TarimaDAOLocal {

	public List<Tarima> getAllSorted() {
		return getAllOrderBy("numero");
	}

	public boolean existsTarima(Tarima tarima) {
		String hql = "FROM Tarima t where (:id IS NULL OR t.id != :id) AND t.numero=:numero";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("id", tarima.getId());
		q.setParameter("numero", tarima.getNumero());
		return !q.getResultList().isEmpty();
	}

}