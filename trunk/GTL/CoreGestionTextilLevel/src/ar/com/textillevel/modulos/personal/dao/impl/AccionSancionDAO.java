package ar.com.textillevel.modulos.personal.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ar.clarin.fwjava.dao.impl.GenericDAO;
import ar.com.textillevel.modulos.personal.dao.api.AccionSancionDAOLocal;
import ar.com.textillevel.modulos.personal.entidades.legajos.sanciones.AccionSancion;
import ar.com.textillevel.modulos.personal.entidades.legajos.sanciones.Sancion;

@SuppressWarnings("rawtypes")
@Stateless
public class AccionSancionDAO extends GenericDAO<AccionSancion, Integer> implements AccionSancionDAOLocal {

	@SuppressWarnings("unchecked")
	public List<AccionSancion> getHistoria(Sancion sancion) {
		String hql = "SELECT acs From AccionSancion acs JOIN FETCH acs.sancion WHERE acs.sancion.id = :idSancion ORDER BY acs.fechaHora DESC";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idSancion", sancion.getId());
		return q.getResultList();
	}

	public void borrarAccionesSancion(Sancion sancion) {
		String hql = " DELETE FROM AccionSancion acs WHERE acs.sancion.id = :idSancion ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idSancion", sancion.getId());
		q.executeUpdate();
	}

}
