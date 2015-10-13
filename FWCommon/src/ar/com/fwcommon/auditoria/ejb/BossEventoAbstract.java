package ar.com.fwcommon.auditoria.ejb;

import java.sql.Timestamp;
import java.util.List;

import ar.com.fwcommon.componentes.error.FWException;

public abstract interface BossEventoAbstract {

	/**
	 * Busca el tipo de evento.
	 * @param tipo
	 * @return El tipo de evento
	 */
	public TipoEvento buscarTipoEventoPorTipo(int tipo) throws FWException;

	/**
	 * Graba el evento en la base de datos.
	 * @param evento El evento a grabar 
	 */
	public void grabarEvento(Evento evento) throws FWException;

	/**
	 * Retorna los eventos asociados a los módulos especificados en el período indicado.
	 * @param fechaDesde
	 * @param fechaHasta
	 * @param idModulos
	 * @return
	 */
	public List<Evento> getEventosPeriodoModulos(Timestamp fechaDesde, Timestamp fechaHasta, List<Integer> idModulos) throws FWException;

}