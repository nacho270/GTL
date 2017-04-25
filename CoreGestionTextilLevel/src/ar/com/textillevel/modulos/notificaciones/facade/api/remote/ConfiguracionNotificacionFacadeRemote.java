package ar.com.textillevel.modulos.notificaciones.facade.api.remote;

import java.util.List;

import javax.ejb.Remote;

import ar.com.textillevel.entidades.portal.UsuarioSistema;
import ar.com.textillevel.modulos.notificaciones.entidades.ConfiguracionNotificacion;

@Remote
public interface ConfiguracionNotificacionFacadeRemote {

	public List<ConfiguracionNotificacion> getConfiguracionesHabilitadasParaUsuario(UsuarioSistema usuario);
}
