package ar.com.textillevel.facade.impl;

import java.sql.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.com.fwcommon.auditoria.ejb.Evento;
import ar.com.fwcommon.auditoria.ejb.EventoDAOLocal;
import ar.com.textillevel.facade.api.remote.EventoFacadeRemote;

@Stateless
public class EventoFacade implements EventoFacadeRemote{

	@EJB
	private EventoDAOLocal eventoDao;
	
	public Integer getCantidadDeRegistros(String usuario, Date fechaDesde, Date fechaHasta) {
//		return eventoDao.getCantidadDeRegistros(usuario, fechaDesde, fechaHasta);
		return null;
	}

	public List<Evento> getEventosPorUsuarioYFechaPaginado(String usuario, Date fechaDesde, Date fechaHasta, Integer paginaActual, Integer cantPaginas) {
//		return eventoDao.getEventosPorUsuarioYFechaPaginado(usuario, fechaDesde, fechaHasta, paginaActual, cantPaginas);
		return null;
	}
}
