package main.servicios;

import java.util.concurrent.Executors;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class MessageListener {

	private static final String URL = "tcp://localhost:61616";
	private static final String TOPIC = "SALEM_TOPIC";

	private static boolean isRunning = false;
	private static Connection connection;
	private static Session session;

	public static void start() {
		if (isRunning) {
			return;
		}
		System.out.println("Iniciando receptor de mensajes...");
		ConnectionFactory CONNECTION_FACTORY = new ActiveMQConnectionFactory(URL);
		try {
			connection = CONNECTION_FACTORY.createConnection();
			connection.start();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination destination = session.createTopic(TOPIC);
			final MessageConsumer consumer = session.createConsumer(destination);

			Executors.newSingleThreadExecutor().execute(new Runnable() {
				
				@Override
				public void run() {
					while(true) {
						try {
							// Bloqueante
							Message message = consumer.receive();
							System.out.println("Llego algo");
							if (message instanceof TextMessage) {
								TextMessage textMessage = (TextMessage) message;
								System.out.println("Recibi: " + textMessage.getText());
							}
						} catch (JMSException e) {
							e.printStackTrace();
						}
					}
				}
			});
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
	
	public static void stop() {
		System.out.println("Deteniendo receptor de mensajes...");
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
