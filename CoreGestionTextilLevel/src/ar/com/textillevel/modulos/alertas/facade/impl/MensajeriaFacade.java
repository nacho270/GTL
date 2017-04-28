package ar.com.textillevel.modulos.alertas.facade.impl;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;

import ar.com.textillevel.modulos.alertas.facade.api.local.MensajeriaFacadeLocal;
import ar.com.textillevel.modulos.notificaciones.entidades.NotificacionUsuario;
import ar.com.textillevel.modulos.notificaciones.enums.ETipoDestinoNotificacion;

@Stateless
public class MensajeriaFacade implements MensajeriaFacadeLocal {

	private static final Logger LOGGER = Logger.getLogger(MensajeriaFacade.class);

	private static final String ACTIVEMQ_URL = System.getProperty("textillevel.activemq.url", "tcp://localhost:61616");

	private static Connection activeMQConnection;

	private static boolean didAcquireConnection = false;

	@PostConstruct
	public void initActiveMQ() {
		try {
			do {
				try {
					activeMQConnection = new ActiveMQConnectionFactory(ACTIVEMQ_URL).createConnection();
				} catch (Exception e) {
					try {
						Thread.sleep(10000);
					} catch (Exception e2) {
					}
				}
			} while (activeMQConnection == null);
			activeMQConnection.start();
			didAcquireConnection = true;
			LOGGER.info("Conexion establecida con el servicio de mensajes " + ACTIVEMQ_URL);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void enviarNotificaciones(final NotificacionUsuario notificacion, final String nombreDestino, final ETipoDestinoNotificacion tipoDestino) {
		if (!didAcquireConnection) {
			LOGGER.warn("No se envian notificaciones por falta de conexion con el servicio de mensajes " + ACTIVEMQ_URL);
			return;
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				Session session = null;
				try {
					session = activeMQConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
					Destination destination = tipoDestino == ETipoDestinoNotificacion.QUEUE ? session.createQueue(nombreDestino) : session.createTopic(nombreDestino);
					final MessageProducer producer = session.createProducer(destination);
					producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
					producer.send(session.createObjectMessage(notificacion));
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
		}).start();
	}
}
