package ar.com.textillevel.modulos.notificaciones.facade.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.com.textillevel.entidades.portal.UsuarioSistema;
import ar.com.textillevel.modulos.notificaciones.dao.api.local.ConfiguracionNotificacionDAOLocal;
import ar.com.textillevel.modulos.notificaciones.entidades.ConfiguracionNotificacion;
import ar.com.textillevel.modulos.notificaciones.facade.api.remote.ConfiguracionNotificacionFacadeRemote;

@Stateless
public class ConfiguracionNotificacionFacade implements ConfiguracionNotificacionFacadeRemote {

	@EJB
	private ConfiguracionNotificacionDAOLocal configuracionDAO;

	@Override
	public List<ConfiguracionNotificacion> getConfiguracionesHabilitadasParaUsuario(UsuarioSistema usuario) {
		return configuracionDAO.getConfiguracionesHabilitadasParaUsuario(usuario);
	}

}
