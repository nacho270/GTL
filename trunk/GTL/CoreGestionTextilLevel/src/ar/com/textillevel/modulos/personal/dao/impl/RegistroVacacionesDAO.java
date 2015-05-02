package ar.com.textillevel.modulos.personal.dao.impl;

import java.sql.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ar.clarin.fwjava.dao.impl.GenericDAO;
import ar.com.textillevel.modulos.personal.dao.api.RegistroVacacionesDAOLocal;
import ar.com.textillevel.modulos.personal.entidades.legajos.RegistroVacacionesLegajo;

@Stateless
public class RegistroVacacionesDAO extends GenericDAO<RegistroVacacionesLegajo, Integer> implements RegistroVacacionesDAOLocal{

	@SuppressWarnings("unchecked")
	public List<RegistroVacacionesLegajo> getAllRegistrosVacacionesByFecha(Date fechaDesde, Date fechaHasta) {
		String hql = " SELECT r FROM RegistroVacacionesLegajo r JOIN FETCH r.legajo JOIN FETCH r.legajo.empleado " +
					 " WHERE r.fechaDesde >= :fechaDesde AND r.fechaHasta <= :fechaHasta  ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("fechaDesde", fechaDesde);
		q.setParameter("fechaHasta", fechaHasta);
		return q.getResultList();
	}
}
