package ar.com.fwcommon.auditoria.ejb;

import java.sql.Timestamp;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.seam.annotations.Name;

import ar.com.fwcommon.componentes.error.FWException;

@Stateless
@Name("bossEvento")
public class BossEvento implements BossEventoLocal, BossEventoRemote {

	private final static int TAMANIO_CAMPO_CLASE = 256;
	//pf: TAMANIO_CAMPO_DESCRIPCION: cambio 4096 x 4000 (para que funcione en Oracle)
	private final static int TAMANIO_CAMPO_DESCRIPCION = 4000;
//	private static ListaTipoEventos listaTipoEventos = null;
	@EJB
	private EventoDAOLocal eventoDAO;
	@EJB
	private TipoEventoDAOLocal tipoEventoDAO;

//	private ListaTipoEventos getListaTipoEventos() {
//		if(listaTipoEventos == null) {
//			List<TipoEvento> todosLosTiposEventos = tipoEventoDAO.getAll();
//			listaTipoEventos = new ListaTipoEventos(todosLosTiposEventos.size(), todosLosTiposEventos);
//		}
//		return listaTipoEventos;
//	}

	/**
	 * Devuelve el objeto tipo de evento su tipo es 'tipo'.  
	 * @param tipo
	 * @return tipoEvento
	 * @throws FWException  
	 */
	public TipoEvento buscarTipoEventoPorTipo(int tipo) throws FWException {
		for(TipoEvento tipoEvento : tipoEventoDAO.getAll()) {
			if (tipoEvento.getTipo() == tipo) {
				return tipoEvento ;
			}
		}
		throw new FWException ("No existe un tipo de evento para el identificador de tipo dado (" + tipo + ")") ;
	}

	/**
	 * Graba el evento en la base de datos.
	 * @param evento 
	 */
	public void grabarEvento(Evento evento) {
		if(evento.getClase() != null && evento.getClase().length() > TAMANIO_CAMPO_CLASE)
			evento.setClase(evento.getClase().substring(0, TAMANIO_CAMPO_CLASE));
		if(evento.getDescripcion() != null && evento.getDescripcion().length() > TAMANIO_CAMPO_DESCRIPCION)
			evento.setDescripcion(evento.getDescripcion().substring(0, TAMANIO_CAMPO_DESCRIPCION));
		eventoDAO.save(evento);
	}

	/**
	 * Retorna los eventos asociados a los módulos especificados en el período indicado.
	 * @param fechaDesde
	 * @param fechaHasta
	 * @param idModulos
	 * @return
	 */
	public List<Evento> getEventosPeriodoModulos(Timestamp fechaDesde, Timestamp fechaHasta, List<Integer> idModulos) {
		return eventoDAO.getEventosPeriodoModulos(fechaDesde, fechaHasta, idModulos);
	}

}