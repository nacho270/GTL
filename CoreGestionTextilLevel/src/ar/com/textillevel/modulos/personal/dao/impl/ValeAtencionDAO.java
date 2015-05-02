package ar.com.textillevel.modulos.personal.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ar.clarin.fwjava.dao.impl.GenericDAO;
import ar.com.textillevel.modulos.personal.dao.api.ValeAtencionDAOLocal;
import ar.com.textillevel.modulos.personal.entidades.legajos.LegajoEmpleado;
import ar.com.textillevel.modulos.personal.entidades.legajos.valesdeatencion.ValeAtencion;

@Stateless
@SuppressWarnings("unchecked")
public class ValeAtencionDAO extends GenericDAO<ValeAtencion, Integer> implements ValeAtencionDAOLocal {

	public List<ValeAtencion> getValesAtencion(LegajoEmpleado legajo) {
		String hql = "SELECT va From ValeAtencion va WHERE va.legajo.id = :idLegajo ORDER BY va.fechaVale DESC ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idLegajo", legajo.getId());
		return q.getResultList();
	}

}
