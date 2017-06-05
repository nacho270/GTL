package ar.com.textillevel.modulos.odt.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ar.com.fwcommon.dao.impl.GenericDAO;
import ar.com.textillevel.modulos.odt.dao.api.local.CodigoODTDAOLocal;
import ar.com.textillevel.modulos.odt.entidades.CodigoODT;

@Stateless
public class CodigoODTDAO extends GenericDAO<CodigoODT, Integer> implements CodigoODTDAOLocal {

	@SuppressWarnings("unchecked")
	public String getUltimoCodigoODT() {
		Query query = getEntityManager().createQuery(" SELECT odt.codigo " +
				 " FROM CodigoODT odt " +
				 " ORDER BY CAST(substring(odt.codigo, 1, 4) AS integer) DESC, " +
				 "		    CAST(substring(odt.codigo, 5, 2) AS integer) DESC, " +
				 "		    CAST(substring(odt.codigo, 7, length(odt.codigo)+6) AS integer) DESC");
		query.setMaxResults(1);
		List<String> resultList = query.getResultList();
		if(resultList.isEmpty()) {
			return null;
		} else {
			return resultList.get(0);
		}
	}

	@Override
	public void removeByCodigo(String codigo) {
		getEntityManager().createQuery("DELETE FROM CodigoODT cod WHERE cod.codigo = :codigo").setParameter("codigo", codigo).executeUpdate();
	}

}