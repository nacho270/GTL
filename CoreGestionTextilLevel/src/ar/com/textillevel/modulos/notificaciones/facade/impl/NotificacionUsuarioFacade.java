package ar.com.textillevel.modulos.notificaciones.facade.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import ar.com.textillevel.dao.api.local.UsuarioSistemaDAOLocal;
import ar.com.textillevel.entidades.portal.UsuarioSistema;
import ar.com.textillevel.modulos.alertas.facade.api.local.MensajeriaFacadeLocal;
import ar.com.textillevel.modulos.notificaciones.dao.api.local.ConfiguracionNotificacionDAOLocal;
import ar.com.textillevel.modulos.notificaciones.dao.api.local.NotificacionUsuarioDAOLocal;
import ar.com.textillevel.modulos.notificaciones.entidades.ConfiguracionNotificacion;
import ar.com.textillevel.modulos.notificaciones.entidades.NotificacionUsuario;
import ar.com.textillevel.modulos.notificaciones.enums.ETipoDestinoNotificacion;
import ar.com.textillevel.modulos.notificaciones.enums.ETipoNotificacion;
import ar.com.textillevel.modulos.notificaciones.facade.api.local.NotificacionUsuarioFacadeLocal;
import ar.com.textillevel.modulos.notificaciones.facade.api.remote.NotificacionUsuarioFacadeRemote;

@Stateless
public class NotificacionUsuarioFacade implements NotificacionUsuarioFacadeRemote, NotificacionUsuarioFacadeLocal {

	private static final Logger LOGGER = Logger.getLogger(NotificacionUsuarioFacade.class);

	@EJB
	private NotificacionUsuarioDAOLocal notificacionesDAO;

	@EJB
	private UsuarioSistemaDAOLocal usuarioSistemaDAO;

	@EJB
	private ConfiguracionNotificacionDAOLocal configuracionNotificacionDAO;

	@EJB
	private MensajeriaFacadeLocal mensajeriaFacade;
	
	@Override
	public List<NotificacionUsuario> getNotificacionesByUsuario(Integer idUsuarioSistema, Integer max) {
		return notificacionesDAO.getNotificacionesByUsuario(idUsuarioSistema, max);
	}

	@Override
	public Integer getCountNotificacionesNoLeidasByUsuario(Integer idUsuarioSistema) {
		return notificacionesDAO.getCountNotificacionesNoLeidasByUsuario(idUsuarioSistema);
	}

	@Override
	public void generarNotificaciones(ETipoNotificacion tipo, Integer idRelacionado, Object... parms) {
		final ConfiguracionNotificacion configuracion = configuracionNotificacionDAO.getByTipo(tipo);
		if (configuracion == null) {
			LOGGER.warn("No se envian notificaciones debido a que no ha creado una configuracion para el tipo notificacion " + tipo);
			return;
		}
		List<UsuarioSistema> usuariosANotificar = usuarioSistemaDAO.getByModuloAsociado(configuracion.getModuloAsociado());
		if (usuariosANotificar == null || usuariosANotificar.isEmpty()) {
			return;
		}
		String nombreDestino = configuracion.getNombreDestino();
		ETipoDestinoNotificacion tipoDestino = configuracion.getTipoDestino();
		final String texto = String.format(tipo.getTexto(), parms);
		NotificacionUsuario nc = new NotificacionUsuario();
		nc.setTexto(texto);
		nc.setIdRelacionado(idRelacionado);
		nc.setTipoNotificacion(tipo);
		for (UsuarioSistema us : usuariosANotificar) {
			nc.setUsuarioSistema(us);
			notificacionesDAO.save(nc);
		}
		// el usuario es transient, asique no importa que usuario se haya asignado al final
		mensajeriaFacade.enviarNotificaciones(nc, nombreDestino, tipoDestino);
	}

	@Override
	public NotificacionUsuario marcarComoLeida(NotificacionUsuario nc) {
		nc.setLeida(true);
		return notificacionesDAO.save(nc);
	}
}
