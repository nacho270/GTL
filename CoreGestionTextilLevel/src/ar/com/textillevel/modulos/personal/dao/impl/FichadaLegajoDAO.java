package ar.com.textillevel.modulos.personal.dao.impl;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ar.com.fwcommon.dao.impl.GenericDAO;
import ar.com.textillevel.modulos.personal.dao.api.FichadaLegajoDAOLocal;
import ar.com.textillevel.modulos.personal.entidades.fichadas.FichadaLegajo;

@Stateless
@SuppressWarnings("unchecked")
public class FichadaLegajoDAO extends GenericDAO<FichadaLegajo, Integer> implements FichadaLegajoDAOLocal{

	public List<FichadaLegajo> getAllByLegajo(Integer idLegajo) {
		String hql = " SELECT f FROM FichadaLegajo f WHERE f.legajo.id = :idLegajo ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idLegajo", idLegajo);
		return q.getResultList();
	}

	public List<FichadaLegajo> getAllByLegajoYFecha(Integer idLegajo, Date fechaDesde, Date fechaHasta) {
		String hql = " SELECT f FROM FichadaLegajo f WHERE f.legajo.id = :idLegajo "+
					 (fechaDesde!=null?" AND f.horario >= :fechaDesde ": " ")+
					 (fechaHasta!=null?" AND f.horario <= :fechaHasta ": " ")+
					 "ORDER BY f.horario ASC ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idLegajo", idLegajo);
		if(fechaDesde!=null){
			q.setParameter("fechaDesde", fechaDesde);
		}
		if(fechaHasta!=null){
			q.setParameter("fechaHasta", fechaHasta);
		}
		return q.getResultList();
	}

	public Timestamp getFechaHoraUltimaFichada() {
		String hql = " SELECT f.horario FROM FichadaLegajo f ORDER BY f.horario DESC ";
		Query q = getEntityManager().createQuery(hql);
		q.setMaxResults(1);
		List<Timestamp> lista = q.getResultList();
		return lista!=null &&!lista.isEmpty()?lista.get(0):null;
	}
}
