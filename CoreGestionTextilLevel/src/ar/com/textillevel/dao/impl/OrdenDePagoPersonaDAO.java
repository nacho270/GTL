package ar.com.textillevel.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ar.com.fwcommon.dao.impl.GenericDAO;
import ar.com.fwcommon.util.NumUtil;
import ar.com.textillevel.dao.api.local.OrdenDePagoPersonaDAOLocal;
import ar.com.textillevel.entidades.documentos.pagopersona.OrdenDePagoAPersona;

@Stateless
public class OrdenDePagoPersonaDAO extends GenericDAO<OrdenDePagoAPersona, Integer> implements OrdenDePagoPersonaDAOLocal {

	public Integer getUltimoNumeroOrden() {
		String hql = "SELECT MAX(o.id) FROM OrdenDePagoAPersona o ";
		Query query = getEntityManager().createQuery(hql);
		return NumUtil.toInteger(query.getSingleResult());
	}

	@SuppressWarnings("unchecked")
	public OrdenDePagoAPersona getOrdenByNro(Integer nroOrden) {
		String hql = "SELECT o FROM OrdenDePagoAPersona o WHERE o.nroOrden = :nroOrden ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("nroOrden", nroOrden);
		List<OrdenDePagoAPersona> l = q.getResultList();
		if (l == null || l.isEmpty()) {
			return null;
		}
		OrdenDePagoAPersona orden = l.get(0);
		orden.getPersona().getApellido();
		orden.getFormasDePago().size();

		return orden;
	}
}
