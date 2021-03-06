package main.servicios;

import java.util.List;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;

import main.NotificableMainTemplate;

import org.apache.activemq.ActiveMQConnectionFactory;

import ar.com.textillevel.entidades.portal.UsuarioSistema;
import ar.com.textillevel.modulos.notificaciones.entidades.ConfiguracionNotificacion;
import ar.com.textillevel.modulos.notificaciones.entidades.NotificacionUsuario;
import ar.com.textillevel.modulos.notificaciones.enums.ETipoDestinoNotificacion;
import ar.com.textillevel.modulos.notificaciones.facade.api.remote.ConfiguracionNotificacionFacadeRemote;
import ar.com.textillevel.util.GTLBeanFactory;

public class NotificationsListener {

	private static final String URL = System.getProperty("textillevel.activemq.url", "tcp://localhost:61616");

	private boolean isRunning = false;
	private Connection connection;
	private static final ConnectionFactory CONNECTION_FACTORY = new ActiveMQConnectionFactory(URL);
	private Session session;
	private NotificableMainTemplate gtlMT;
	private UsuarioSistema usuario;
	private Thread listerThread;
	
	private NotificationsListener(NotificableMainTemplate gtlMT, UsuarioSistema usuario) {
		this.gtlMT = gtlMT;
		this.usuario = usuario;
		((ActiveMQConnectionFactory) CONNECTION_FACTORY).setTrustAllPackages(true);
	}

	public static NotificationsListener build(NotificableMainTemplate gtlMT, UsuarioSistema usuario) {
		return new NotificationsListener(gtlMT, usuario);
	}

	public void start() {
		if (isRunning) {
			return;
		}
		isRunning = true;
		listerThread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					do {
						try {
							connection = CONNECTION_FACTORY.createConnection();
						} catch (Exception e) {
							try {
								Thread.sleep(10000);
							} catch (Exception e2) {
							}
						}

					} while (connection == null);
					connection.start();
					System.out.println("Conectado a servicio de mensajes en " + URL);
					session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
					final List<ConfiguracionNotificacion> configuracionesHabilitadasParaUsuario = GTLBeanFactory.getInstance().getBean2(ConfiguracionNotificacionFacadeRemote.class).getConfiguracionesHabilitadasParaUsuario(usuario);
					if (configuracionesHabilitadasParaUsuario == null || configuracionesHabilitadasParaUsuario.isEmpty()) {
						return;
					}
					for (ConfiguracionNotificacion conf : configuracionesHabilitadasParaUsuario) {
						escucharDestinoEnOtroThread(conf);
					}
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		});
		listerThread.setDaemon(true);
		listerThread.start();
	}

	private void escucharDestinoEnOtroThread(final ConfiguracionNotificacion conf) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					final String nombreDestino = conf.getNombreDestino();
					Destination destination = conf.getTipoDestino() == ETipoDestinoNotificacion.TOPIC ? session.createTopic(nombreDestino) : session.createQueue(nombreDestino);
					final MessageConsumer consumer = session.createConsumer(destination);

					while (isRunning) {
						// Bloqueante
						Message message = consumer.receive();
						if (message != null) {
							String text = "";
							if (message instanceof TextMessage) {
								text = ((TextMessage) message).getText();
								gtlMT.mostrarNotificacion(text);
							} else if (message instanceof ObjectMessage) {
								NotificacionUsuario notifiacion = (NotificacionUsuario) ((ObjectMessage) message).getObject();
								gtlMT.mostrarNotificacion(notifiacion);
							}
							gtlMT.actualizarNotificaciones();
						}
					}
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	public void stop() {
		try {
			isRunning = false;
			if (session != null) {
				session.close();
			}
			if (connection != null) {
				connection.close();
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
}
