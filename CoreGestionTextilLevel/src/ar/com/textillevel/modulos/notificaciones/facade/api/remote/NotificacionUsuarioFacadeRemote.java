package ar.com.textillevel.modulos.notificaciones.facade.api.remote;

import java.util.List;

import javax.ejb.Remote;

import ar.com.textillevel.modulos.notificaciones.entidades.NotificacionUsuario;
import ar.com.textillevel.modulos.notificaciones.enums.ETipoNotificacion;

@Remote
public interface NotificacionUsuarioFacadeRemote {

	public List<NotificacionUsuario> getNotificacionesByUsuario(Integer idUsuarioSistema, Integer max);
	public Integer getCountNotificacionesNoLeidasByUsuario(Integer idUsuarioSistema);
	public NotificacionUsuario marcarComoLeida(NotificacionUsuario nc);
	public void marcarComoLeidaATodosLosUsuarios(NotificacionUsuario nc);
	public void marcarComoLeidaATodosLosUsuarios(Integer idRelacionado, ETipoNotificacion tipo);

}
