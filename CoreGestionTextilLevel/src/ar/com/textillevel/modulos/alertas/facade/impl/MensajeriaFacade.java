package ar.com.textillevel.modulos.alertas.facade.impl;

import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
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
		Session session = null;
		Connection connection = null;
		try {
			ConnectionFactory factory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
			connection = factory.createConnection();
			connection.start();

			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Topic topic = session.createTopic(TOPIC);

			MessageProducer producer = session.createProducer(topic);
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			TextMessage msg = session.createTextMessage();
			msg.setText("Hooooooooooooollllllllllllllaaaaaaaaaaaaaaa");
			producer.send(msg);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (session != null) {
					session.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (JMSException jmsE) {
				jmsE.printStackTrace();
			}
		}
	}
}
