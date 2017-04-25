package ar.com.textillevel.modulos.notificaciones.facade.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.com.textillevel.modulos.notificaciones.dao.api.local.NotificacionUsuarioDAOLocal;
import ar.com.textillevel.modulos.notificaciones.entidades.NotificacionUsuario;
import ar.com.textillevel.modulos.notificaciones.facade.api.remote.NotificacionUsuarioFacadeRemote;

@Stateless
public class NotificacionUsuarioFacade implements NotificacionUsuarioFacadeRemote {

	@EJB
	private NotificacionUsuarioDAOLocal notificacionesDAO;

	@Override
	public List<NotificacionUsuario> getNotificacionesByUsuario(Integer idUsuarioSistema, Integer max) {
		return notificacionesDAO.getNotificacionesByUsuario(idUsuarioSistema, max);
	}

	@Override
	public Integer getCountNotificacionesNoLeidasByUsuario(Integer idUsuarioSistema) {
		return notificacionesDAO.getCountNotificacionesNoLeidasByUsuario(idUsuarioSistema);
	}

}
