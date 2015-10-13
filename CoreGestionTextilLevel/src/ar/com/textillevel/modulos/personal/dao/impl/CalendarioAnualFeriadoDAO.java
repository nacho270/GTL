package ar.com.textillevel.modulos.personal.dao.impl;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ar.com.fwcommon.dao.impl.GenericDAO;
import ar.com.textillevel.modulos.personal.dao.api.CalendarioAnualFeriadoDAOLocal;
import ar.com.textillevel.modulos.personal.entidades.calenlaboral.CalendarioAnualFeriado;

@Stateless
public class CalendarioAnualFeriadoDAO extends GenericDAO<CalendarioAnualFeriado, Integer> implements CalendarioAnualFeriadoDAOLocal {

	public boolean existeCalendario(Integer anio) {
		String query = " SELECT caf FROM CalendarioAnualFeriado caf " +
	   	   			  " WHERE caf.anio = :anio ";
		Query q = getEntityManager().createQuery(query);
		q.setParameter("anio", anio);
		return !q.getResultList().isEmpty();
	}

}
