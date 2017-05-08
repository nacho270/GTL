package ar.com.textillevel.modulos.notificaciones.facade.api.remote;

import java.util.List;

import javax.ejb.Remote;

import ar.com.textillevel.modulos.notificaciones.entidades.NotificacionUsuario;

@Remote
public interface NotificacionUsuarioFacadeRemote {

	public List<NotificacionUsuario> getNotificacionesByUsuario(Integer idUsuarioSistema, Integer max);
	public Integer getCountNotificacionesNoLeidasByUsuario(Integer idUsuarioSistema);
	public NotificacionUsuario marcarComoLeida(NotificacionUsuario nc);

}
