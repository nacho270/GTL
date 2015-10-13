package ar.com.fwcommon.auditoria.ejb;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ar.com.fwcommon.dao.impl.GenericDAO;

@Stateless
public class EventoDAO extends GenericDAO<Evento, Integer> implements EventoDAOLocal {

	@SuppressWarnings("unchecked")
	public List<Evento> getEventosPeriodoModulos(Timestamp fechaDesde, Timestamp fechaHasta, List<Integer> idModulos) {
		List<Evento> result = new ArrayList<Evento>();
		try {
			Query query = getEntityManager().createQuery(
					"FROM Evento e " + "WHERE e.fechaHora between :fechaDesde " + "AND :fechaHasta " + "AND e.idModulo in (:listaModulos) ")
					.setParameter("fechaDesde", fechaDesde)
					.setParameter("fechaHasta", fechaHasta)
					.setParameter("listaModulos", idModulos);
			result = query.getResultList();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}