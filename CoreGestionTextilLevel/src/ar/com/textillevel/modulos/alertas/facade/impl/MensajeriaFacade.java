package ar.com.textillevel.modulos.alertas.facade.impl;

import java.util.List;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
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

@Stateless
public class MensajeriaFacade implements MensajeriaFacadeLocal {

	private static final Logger LOGGER = Logger.getLogger(MensajeriaFacade.class);

	private static final String ACTIVEMQ_URL = System.getProperty("textillevel.activemq.url", "tcp://localhost:61616");

	@EJB
	private UsuarioSistemaDAOLocal usuarioSistemaDAO;

	@EJB
	private ConfiguracionNotificacionDAOLocal configuracionNotificacionDAO;

	@EJB
	private NotificacionUsuarioDAOLocal notificacionDAO;

	private Connection activeMQConnection;

	@PostConstruct
	public void initActiveMQ() throws JMSException {
		activeMQConnection = new ActiveMQConnectionFactory(ACTIVEMQ_URL).createConnection();
		activeMQConnection.start();
	}

	@Override
	public void generarNotificaciones(final ETipoNotificacion tipo, final Object... parms) {
		final ConfiguracionNotificacion configuracion = configuracionNotificacionDAO.getByTipo(tipo);
		if (configuracion == null) {
			return;
		}
		Executors.newSingleThreadExecutor().execute(new Runnable() {

			@Override
			public void run() {
				List<UsuarioSistema> usuariosANotificar = usuarioSistemaDAO.getByModuloAsociado(configuracion.getModuloAsociado());
				if (usuariosANotificar == null || usuariosANotificar.isEmpty()) {
					return;
				}
				Session session = null;
				try {
					session = activeMQConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
					String nombreDestino = configuracion.getNombreDestino();
					ETipoDestinoNotificacion tipoDestino = configuracion.getTipoDestino();
					Destination destination = tipoDestino == ETipoDestinoNotificacion.QUEUE ? session.createQueue(nombreDestino) : session.createTopic(nombreDestino);
					final MessageProducer producer = session.createProducer(destination);
					producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
					final String texto = String.format(tipo.getTexto(), parms);
					for (UsuarioSistema us : usuariosANotificar) {
						NotificacionUsuario nc = new NotificacionUsuario();
						nc.setUsuarioSistema(us);
						nc.setTexto(texto);
						notificacionDAO.save(nc);
						producer.send(session.createObjectMessage(nc));
					}
				} catch (JMSException e) {
					LOGGER.error(e);
				} finally {
					try {
						if (session != null) {
							session.close();
						}
					} catch (JMSException e) {
						LOGGER.error(e);

					}
				}
			}
		});
	}
}
