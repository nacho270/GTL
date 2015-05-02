package ar.com.textillevel.facade.api.remote;

import java.sql.Date;
import java.util.List;

import javax.ejb.Remote;

import ar.clarin.fwjava.auditoria.ejb.Evento;

@Remote
public interface EventoFacadeRemote {
	public Integer getCantidadDeRegistros(String usuario, Date fechaDesde, Date fechaHasta);
	public List<Evento> getEventosPorUsuarioYFechaPaginado(String usuario, Date fechaDesde, Date fechaHasta, Integer paginaActual, Integer cantPaginas);
}
