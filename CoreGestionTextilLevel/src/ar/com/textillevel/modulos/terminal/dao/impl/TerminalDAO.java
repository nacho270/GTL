package ar.com.textillevel.modulos.terminal.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ar.com.fwcommon.dao.impl.GenericDAO;
import ar.com.textillevel.modulos.terminal.dao.api.local.TerminalDAOLocal;
import ar.com.textillevel.modulos.terminal.entidades.Terminal;

@Stateless
public class TerminalDAO extends GenericDAO<Terminal, Integer> implements TerminalDAOLocal {

	@Override
	@SuppressWarnings("unchecked")
	public Terminal getByIP(String ip) {
		String hql = "SELECT t From Terminal t WHERE t.ip = :ip";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("ip", ip);
		List<Terminal> resultList = q.getResultList();
		if (resultList == null || resultList.isEmpty()) {
			return null;
		}
		return resultList.get(0);
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean existeNombre(Integer id, String nombre) {
		String hql = "SELECT t From Terminal t WHERE t.nombre = :nombre "
				+ ((id != null) ? " AND t.id <> :id " : " ");
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("nombre", nombre);
		if(id!=null){
			q.setParameter("id", id);
		}
		List<Terminal> resultList = q.getResultList();
		return resultList != null && resultList.size() > 0;
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean existeIp(Integer id, String ip) {
		String hql = "SELECT t From Terminal t WHERE t.ip = :ip "
				+ ((id != null) ? " AND t.id <> :id " : " ");
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("ip", ip);
		if(id!=null){
			q.setParameter("id", id);
		}
		List<Terminal> resultList = q.getResultList();
		return resultList != null && resultList.size() > 0;
	}

}
