package ar.com.fwcommon.auditoria.ejb;

import java.sql.Timestamp;
import java.util.List;

import javax.ejb.Local;

import ar.com.fwcommon.dao.api.local.DAOLocal;

@Local
public interface EventoDAOLocal extends DAOLocal<Evento, Integer> {

	public List<Evento> getEventosPeriodoModulos(Timestamp fechaDesde, Timestamp fechaHasta, List<Integer> idModulos) ;

}