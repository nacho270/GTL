package ar.com.textillevel.modulos.personal.dao.impl;

import java.sql.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ar.com.fwcommon.dao.impl.GenericDAO;
import ar.com.textillevel.modulos.personal.dao.api.AsignacionDAOLocal;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.asignaciones.Asignacion;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.asignaciones.AsignacionNoRemPorcSueldoBruto;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.asignaciones.AsignacionNoRemSimple;

@Stateless
@SuppressWarnings("unchecked")
public class AsignacionDAO extends GenericDAO<Asignacion, Integer> implements AsignacionDAOLocal {

	public List<Asignacion> getAllByIdSindicato(Integer idSindicato) {
		String hql = "SELECT a From Asignacion a WHERE a.sindicato.id = :idSindicato ORDER BY a.fechaDesde ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idSindicato", idSindicato);
		return q.getResultList();
	}

	public Asignacion getByIdEager(Integer idAsignacion) {
		Asignacion asignacion = getById(idAsignacion);
		asignacion.getAdicionales().size();
		return asignacion;
	}

	public List<AsignacionNoRemSimple> getAllNoRemSimpleByFechaAndIdSindicato(Date fecha, Integer idSindicato) {
		String hql = "SELECT a From AsignacionNoRemSimple a WHERE a.sindicato.id = :idSindicato AND :fecha BETWEEN a.fechaDesde AND a.fechaHasta ORDER BY a.fechaDesde ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idSindicato", idSindicato);
		q.setParameter("fecha", fecha);
		return q.getResultList();
	}

	public List<AsignacionNoRemPorcSueldoBruto> getAllNoRemPorcSueldoBrutoByFechaAndIdSindicato(Date fecha, Integer idSindicato) {
		String hql = "SELECT a From AsignacionNoRemPorcSueldoBruto a WHERE a.sindicato.id = :idSindicato AND :fecha BETWEEN a.fechaDesde AND a.fechaHasta ORDER BY a.fechaDesde ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idSindicato", idSindicato);
		q.setParameter("fecha", fecha);
		return q.getResultList();
	}

}