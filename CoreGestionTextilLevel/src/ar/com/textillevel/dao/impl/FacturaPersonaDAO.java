package ar.com.textillevel.dao.impl;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ar.com.fwcommon.dao.impl.GenericDAO;
import ar.com.fwcommon.util.NumUtil;
import ar.com.textillevel.dao.api.local.FacturaPersonaDAOLocal;
import ar.com.textillevel.entidades.documentos.pagopersona.FacturaPersona;
import ar.com.textillevel.entidades.gente.Persona;

@Stateless
public class FacturaPersonaDAO extends GenericDAO<FacturaPersona, Integer> implements FacturaPersonaDAOLocal{

	public boolean existeNroFacturaParaPersona(Integer nroFactura, Persona persona) {
		String hql = " SELECT COUNT(*) FROM FacturaPersona fp WHERE fp.nroFactura = :nroFactura AND fp.persona.id = :idPersona ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("nroFactura", nroFactura);
		q.setParameter("idPersona", persona.getId());
		return NumUtil.toInteger(q.getSingleResult())>0;
	}
}
