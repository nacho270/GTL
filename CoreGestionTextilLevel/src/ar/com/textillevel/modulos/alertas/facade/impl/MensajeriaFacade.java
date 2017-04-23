package ar.com.textillevel.modulos.alertas.facade.impl;

import java.util.concurrent.Executors;

import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import ar.com.textillevel.modulos.alertas.facade.api.local.MensajeriaFacadeLocal;

@Stateless
public class MensajeriaFacade implements MensajeriaFacadeLocal {

	private static final String ACTIVEMQ_URL = System.getProperty("textillevel.activemq.url", "tcp://localhost:61616");
	private static final String TOPIC = System.getProperty("textillevel.activemq.topic", "SALEM_TOPIC");

	@Override
	public void enviarMensaje() {
		final Session session;
		Connection connection = null;
		try {
			ConnectionFactory factory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
			connection = factory.createConnection();
			connection.start();

			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Topic topic = session.createTopic(TOPIC);

			final MessageProducer producer = session.createProducer(topic);
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			Executors.newSingleThreadExecutor().execute(new Runnable() {

				@Override
				public void run() {
					for (int i = 0; i < 1000; i++) {
						try {
							TextMessage msg = session.createTextMessage();
							msg.setText("Hooooooooooooollllllllllllllaaaaaaaaaaaaaaa_" + i);
							producer.send(msg);
							Thread.sleep(30000);
						} catch (Exception e) {
						}
					}

				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
