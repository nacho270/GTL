package ar.com.textillevel.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ar.clarin.fwjava.dao.impl.GenericDAO;
import ar.com.textillevel.dao.api.local.RubroPersonaDAOLocal;
import ar.com.textillevel.entidades.enums.ETipoRubro;
import ar.com.textillevel.entidades.gente.Rubro;

@Stateless
public class RubroPersonaDAO extends GenericDAO<Rubro, Integer> implements RubroPersonaDAOLocal {

	@SuppressWarnings("unchecked")
	public List<Rubro> getAllRubroByTipo(ETipoRubro tipo) {
		Query query = getEntityManager().createQuery("FROM Rubro AS r WHERE r.idTipoRubro = :idTipo ORDER BY r.nombre");
		query.setParameter("idTipo", tipo.getId());
		return query.getResultList();
	}

}
