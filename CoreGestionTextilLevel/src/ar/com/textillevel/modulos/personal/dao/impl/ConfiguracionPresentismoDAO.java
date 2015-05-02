package ar.com.textillevel.modulos.personal.dao.impl;

import java.sql.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ar.clarin.fwjava.dao.impl.GenericDAO;
import ar.com.textillevel.modulos.personal.dao.api.ConfiguracionPresentismoDAOLocal;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.Sindicato;
import ar.com.textillevel.modulos.personal.entidades.presentismo.ConfiguracionPresentismo;

@Stateless
public class ConfiguracionPresentismoDAO extends GenericDAO<ConfiguracionPresentismo, Integer> implements ConfiguracionPresentismoDAOLocal {

	@SuppressWarnings("unchecked")
	public List<ConfiguracionPresentismo> getAllBySindicato(Sindicato sindicato) {
		String hql = " SELECT cp FROM ConfiguracionPresentismo cp WHERE cp.sindicato.id = :idSindicato ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idSindicato", sindicato.getId());
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public ConfiguracionPresentismo getConfiguracionPosteriorBySindicato(Sindicato sindicato, Date fecha) {
		String hql = " SELECT cp FROM ConfiguracionPresentismo cp WHERE cp.sindicato.id = :idSindicato AND cp.fecha >= :fecha ORDER BY cp.fecha DESC ";
		Query q = getEntityManager().createQuery(hql);
		q.setMaxResults(1);
		q.setParameter("fecha", fecha);
		q.setParameter("idSindicato", sindicato.getId());
		List<ConfiguracionPresentismo> lista = q.getResultList();
		if(lista!=null && !lista.isEmpty()){
			return lista.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public ConfiguracionPresentismo getConfiguracionPresentismoByFechaYSindicato(Date fecha, Sindicato sindicato) {
		String hql = " SELECT cp FROM ConfiguracionPresentismo cp WHERE cp.sindicato.id = :idSindicato AND cp.fecha <= :fecha ORDER BY cp.fecha DESC ";
		Query q = getEntityManager().createQuery(hql);
		q.setMaxResults(1);
		q.setParameter("fecha", fecha);
		q.setParameter("idSindicato", sindicato.getId());
		q.setMaxResults(1);
		List<ConfiguracionPresentismo> lista = q.getResultList();
		if(lista!=null && !lista.isEmpty()){
			return lista.get(0);
		}
		return null;
	}
}
