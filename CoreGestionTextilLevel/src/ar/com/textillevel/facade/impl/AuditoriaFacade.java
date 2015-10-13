package ar.com.textillevel.facade.impl;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.com.fwcommon.auditoria.ejb.BossEventoLocal;
import ar.com.fwcommon.auditoria.ejb.Evento;
import ar.com.fwcommon.auditoria.ejb.TipoEventoDAOLocal;
import ar.com.fwcommon.util.DateUtil;
import ar.com.textillevel.facade.api.remote.AuditoriaFacadeLocal;

@Stateless
public class AuditoriaFacade<C> implements AuditoriaFacadeLocal<C>{

	@EJB
	private TipoEventoDAOLocal tipoEventoDao;
	
	@EJB
	private BossEventoLocal bossEvento;
	
	public void auditar(String usuario, String descripcion, Integer idTipoEvento, C clase) {
		Evento evento = new Evento();
		evento.setClase(getTipoClase(clase));
		evento.setTipoEvento(tipoEventoDao.getReferenceById(idTipoEvento));
		evento.setFechaHora(DateUtil.getAhora());
		evento.setEsDeSistema(true);
		evento.setIdModulo(-1);
		evento.setUsuario(usuario);
		evento.setDescripcion(descripcion);
		bossEvento.grabarEvento(evento);
	}
	
	private String getTipoClase(C tipo){
		return tipo.getClass().getCanonicalName();
	}
}
