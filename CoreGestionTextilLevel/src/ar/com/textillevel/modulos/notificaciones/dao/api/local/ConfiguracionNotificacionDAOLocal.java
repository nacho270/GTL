package ar.com.textillevel.modulos.notificaciones.dao.api.local;

import java.util.List;

import javax.ejb.Local;

import ar.com.fwcommon.dao.api.local.DAOLocal;
import ar.com.textillevel.entidades.portal.UsuarioSistema;
import ar.com.textillevel.modulos.notificaciones.entidades.ConfiguracionNotificacion;
import ar.com.textillevel.modulos.notificaciones.enums.ETipoNotificacion;

@Local
public interface ConfiguracionNotificacionDAOLocal extends DAOLocal<ConfiguracionNotificacion, Integer>{
	
	public ConfiguracionNotificacion getByTipo(ETipoNotificacion tipo);

	public List<ConfiguracionNotificacion> getConfiguracionesHabilitadasParaUsuario(UsuarioSistema usuario);

}
