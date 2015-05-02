package ar.com.textillevel.dao.impl;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ar.clarin.fwjava.dao.impl.GenericDAO;
import ar.clarin.fwjava.util.NumUtil;
import ar.com.textillevel.dao.api.local.PerfilDAOLocal;
import ar.com.textillevel.entidades.portal.Perfil;

@Stateless
public class PerfilDAO extends GenericDAO<Perfil, Integer> implements PerfilDAOLocal{

	public boolean yaHayPerfilAdministrador() {
		String hql = "SELECT COUNT(*) FROM Perfil p WHERE p.isAdmin = 1 ";
		Query q = getEntityManager().createQuery(hql);
		return NumUtil.toInteger(q.getSingleResult())>0;
	}

}
