package ar.com.fwcommon.auditoria.ejb;

import java.sql.Timestamp;
import java.util.List;

import javax.ejb.Local;

import ar.com.fwcommon.componentes.error.FWException;



@Local
public interface BossEventoLocal extends BossEventoAbstract {
	
	public TipoEvento buscarTipoEventoPorTipo(int tipo) throws FWException ;

	public void grabarEvento(Evento evento) ;

	public List<Evento> getEventosPeriodoModulos(Timestamp fechaDesde, Timestamp fechaHasta, List<Integer> idModulos) ;

}