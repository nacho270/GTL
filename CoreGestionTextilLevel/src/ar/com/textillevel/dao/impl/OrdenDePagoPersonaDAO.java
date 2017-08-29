package ar.com.textillevel.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ar.com.fwcommon.dao.impl.GenericDAO;
import ar.com.fwcommon.util.NumUtil;
import ar.com.textillevel.dao.api.local.OrdenDePagoPersonaDAOLocal;
import ar.com.textillevel.entidades.cheque.Cheque;
import ar.com.textillevel.entidades.documentos.pagopersona.OrdenDePagoAPersona;

@Stateless
@SuppressWarnings("unchecked")
public class OrdenDePagoPersonaDAO extends GenericDAO<OrdenDePagoAPersona, Integer> implements OrdenDePagoPersonaDAOLocal {

	public Integer getUltimoNumeroOrden() {
		String hql = "SELECT MAX(o.nroOrden) FROM OrdenDePagoAPersona o ";
		Query query = getEntityManager().createQuery(hql);
		return NumUtil.toInteger(query.getSingleResult());
	}

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

	public OrdenDePagoAPersona getByCheque(Cheque ch) {
		String hql = "SELECT o FROM OrdenDePagoAPersona o JOIN FETCH o.formasDePago formas WHERE formas.cheque = :cheque ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("cheque", ch);
		List<OrdenDePagoAPersona> l = q.getResultList();
		if (l.isEmpty()) {
			return null;
		} else if(l.size()>1) {
			throw new IllegalArgumentException("Existe más de una Orden de Pago a Persona donde se usó el cheque " + ch);
		} else {
			return l.get(0);
		}
	}

}
