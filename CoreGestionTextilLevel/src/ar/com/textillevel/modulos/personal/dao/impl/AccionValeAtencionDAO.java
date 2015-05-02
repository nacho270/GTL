package ar.com.textillevel.modulos.personal.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ar.clarin.fwjava.dao.impl.GenericDAO;
import ar.com.textillevel.modulos.personal.dao.api.AccionValeAtencionDAOLocal;
import ar.com.textillevel.modulos.personal.entidades.legajos.valesdeatencion.AccionValeAtencion;
import ar.com.textillevel.modulos.personal.entidades.legajos.valesdeatencion.ValeAtencion;

@SuppressWarnings("rawtypes")
@Stateless
public class AccionValeAtencionDAO extends GenericDAO<AccionValeAtencion, Integer> implements AccionValeAtencionDAOLocal {

	@SuppressWarnings("unchecked")
	public List<AccionValeAtencion> getHistoria(ValeAtencion valeAtencion) {
		String hql = "SELECT ava From AccionValeAtencion ava JOIN FETCH ava.valeAtencion WHERE ava.valeAtencion = :valeAtencion ORDER BY ava.fechaHora DESC";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("valeAtencion", valeAtencion);
		List<AccionValeAtencion> resultList = q.getResultList();
		return resultList;
	}

	public void borrarAccionesValeAtencion(ValeAtencion valeAtencion) {
		String hql = "DELETE FROM AccionValeAtencion ava WHERE ava.valeAtencion.id = :idValeAtencion";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idValeAtencion", valeAtencion.getId());
		q.executeUpdate();
	}

}