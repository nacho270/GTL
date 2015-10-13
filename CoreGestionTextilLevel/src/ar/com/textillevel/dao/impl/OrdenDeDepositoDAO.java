package ar.com.textillevel.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ar.com.fwcommon.dao.impl.GenericDAO;
import ar.com.fwcommon.util.NumUtil;
import ar.com.textillevel.dao.api.local.OrdenDeDepositoDAOLocal;
import ar.com.textillevel.entidades.documentos.ordendedeposito.OrdenDeDeposito;

@Stateless
public class OrdenDeDepositoDAO extends GenericDAO<OrdenDeDeposito, Integer> implements OrdenDeDepositoDAOLocal{

	public Integer getLastNroOrden() {
		String hql = "SELECT MAX(o.id) FROM OrdenDeDeposito o ";
		Query query = getEntityManager().createQuery(hql);
		return NumUtil.toInteger(query.getSingleResult());
	}

	@SuppressWarnings("unchecked")
	public OrdenDeDeposito getOrdenByNro(Integer nroOrden) {
		String hql = "SELECT o FROM OrdenDeDeposito o JOIN FETCH o.depositos WHERE o.nroOrden = :nro ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("nro", nroOrden);
		List<OrdenDeDeposito> ordenes = q.getResultList();
		if(ordenes!=null && !ordenes.isEmpty()){
			return ordenes.get(0);
		}
		return null;
	}

}
