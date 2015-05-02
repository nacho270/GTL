package ar.com.textillevel.modulos.personal.dao.impl;

import java.sql.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ar.clarin.fwjava.dao.impl.GenericDAO;
import ar.com.textillevel.modulos.personal.dao.api.ConfiguracionVacacionesDAOLocal;
import ar.com.textillevel.modulos.personal.entidades.vacaciones.ConfiguracionVacaciones;

@Stateless
public class ConfiguracionVacacionesDAO extends GenericDAO<ConfiguracionVacaciones, Integer> implements ConfiguracionVacacionesDAOLocal{

	@SuppressWarnings("unchecked")
	public ConfiguracionVacaciones getConfiguracionVacaciones(Date fecha) {
		String hql = " SELECT c FROM ConfiguracionVacaciones c WHERE :fecha >= c.fechaVigencia ORDER BY c.fechaVigencia DESC ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("fecha", fecha);
		List<ConfiguracionVacaciones> confs = q.getResultList();
		if(confs != null && !confs.isEmpty()){
			return confs.get(0);
		}
		return null;
	}
}
