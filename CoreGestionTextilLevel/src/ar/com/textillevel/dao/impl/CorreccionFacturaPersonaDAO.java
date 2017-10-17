package ar.com.textillevel.dao.impl;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import ar.com.fwcommon.dao.impl.GenericDAO;
import ar.com.fwcommon.util.NumUtil;
import ar.com.textillevel.dao.api.local.CorreccionFacturaPersonaDAOLocal;
import ar.com.textillevel.entidades.documentos.pagopersona.CorreccionFacturaPersona;
import ar.com.textillevel.entidades.documentos.pagopersona.ItemCorreccionFacturaPersona;
import ar.com.textillevel.entidades.documentos.pagopersona.NotaDebitoPersona;
import ar.com.textillevel.entidades.gente.Persona;

@Stateless
public class CorreccionFacturaPersonaDAO extends GenericDAO<CorreccionFacturaPersona, Integer> implements CorreccionFacturaPersonaDAOLocal {

	@SuppressWarnings("unchecked")
	public NotaDebitoPersona getNDByIdEager(Integer idND) {
		String hql = " SELECT nd FROM NotaDebitoPersona nd WHERE nd.id = :idND ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idND", idND);
		List<NotaDebitoPersona> lista = q.getResultList();
		if(lista.isEmpty() || lista.size() > 1){
			throw new IllegalArgumentException("No existe ND con id " + idND + " o existe más de una");
		}
		NotaDebitoPersona nd = lista.get(0);
		doEager(nd);
		return nd;
	}

	private void doEager(NotaDebitoPersona nd) {
		nd.getPersona().getApellido();
		for(ItemCorreccionFacturaPersona icfp : nd.getItemsCorreccion()) {
			icfp.getImpuestos().size();
		}
	}

	public boolean existeNroNDParaPersona(Integer nroND, Persona persona) {
		String hql = " SELECT COUNT(*) FROM NotaDebitoPersona fp WHERE fp.nroCorreccion = :nroND AND fp.persona.id = :idPersona ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("nroND", nroND);
		q.setParameter("idPersona", persona.getId());
		return NumUtil.toInteger(q.getSingleResult())>0;
	}

}