package ar.com.textillevel.modulos.notificaciones.dao.api.local;

import java.util.List;

import javax.ejb.Local;

import ar.com.fwcommon.dao.api.local.DAOLocal;
import ar.com.textillevel.modulos.notificaciones.entidades.NotificacionUsuario;
import ar.com.textillevel.modulos.notificaciones.enums.ETipoNotificacion;

@Local
public interface NotificacionUsuarioDAOLocal extends DAOLocal<NotificacionUsuario, Integer> {

	List<NotificacionUsuario> getNotificacionesByUsuario(Integer idUsuarioSistema, Integer max);

	Integer getCountNotificacionesNoLeidasByUsuario(Integer idUsuarioSistema);

	void marcarComoLeidaATodosLosUsuarios(Integer idRelacionado, ETipoNotificacion tipo);

}
