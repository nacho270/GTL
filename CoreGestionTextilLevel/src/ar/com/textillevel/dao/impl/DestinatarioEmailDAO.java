package ar.com.textillevel.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;
import ar.com.fwcommon.dao.impl.GenericDAO;
import ar.com.textillevel.dao.api.local.DestinatarioEmailDAOLocal;
import ar.com.textillevel.entidades.documentos.DestinatarioEmail;
import ar.com.textillevel.entidades.enums.ETipoBusquedaAgenda;

@Stateless
@SuppressWarnings("unchecked")
public class DestinatarioEmailDAO extends GenericDAO<DestinatarioEmail, String> implements DestinatarioEmailDAOLocal {

	public DestinatarioEmail getByParams(String email, Integer idEntidad, ETipoBusquedaAgenda tipoEntidad) {
		Query query = getEntityManager().createQuery("SELECT de FROM DestinatarioEmail AS de WHERE de.email=:email AND de.idEntidad = :idEntidad AND de.idTipoEntidad = :idTipoEntidad");
		query.setParameter("email", email);
		query.setParameter("idEntidad", idEntidad);
		query.setParameter("idTipoEntidad", tipoEntidad.getId());
		List<DestinatarioEmail> resultList = query.getResultList();
		if(resultList.isEmpty()) {
			return null;
		} else {
			return resultList.get(0);
		}
	}

	public List<DestinatarioEmail> getAllByEntidad(Integer idEntidad, ETipoBusquedaAgenda tipoEntidad) {
		Query query = getEntityManager().createQuery("SELECT de FROM DestinatarioEmail AS de WHERE de.idEntidad = :idEntidad AND de.idTipoEntidad = :idTipoEntidad");
		query.setParameter("idEntidad", idEntidad);
		query.setParameter("idTipoEntidad", tipoEntidad.getId());
		return query.getResultList();
	}

}