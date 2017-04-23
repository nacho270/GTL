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

import main.GTLMainTemplate;

import org.apache.activemq.ActiveMQConnectionFactory;

import ar.com.fwcommon.componentes.FWJOptionPane;

public class MessageListener {

	private static final String URL = "tcp://localhost:61616";
	private static final String TOPIC = "SALEM_TOPIC";

	private boolean isRunning = false;
	private Connection connection;
	private Session session;
	private GTLMainTemplate gtlMT;
	
	private MessageListener(GTLMainTemplate gtlMT) {
		this.gtlMT = gtlMT;
	}
	
	public static MessageListener build(GTLMainTemplate gtlMT) {
		return new MessageListener(gtlMT);
	}
	
	public void start() {
		if (isRunning) {
			return;
		}
		System.out.println("Iniciando receptor de mensajes...");
		final ConnectionFactory CONNECTION_FACTORY = new ActiveMQConnectionFactory(URL);
		Executors.newSingleThreadExecutor().execute(new Runnable() {
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
					System.out.println("Conexion establecida, esperando mensajes...");
					session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
					Destination destination = session.createTopic(TOPIC);
					final MessageConsumer consumer = session.createConsumer(destination);

					while (true) {
						try {
							// Bloqueante
							Message message = consumer.receive();
							System.out.println("Llego algo");
							if (message instanceof TextMessage) {
								TextMessage textMessage = (TextMessage) message;
								System.out.println("Recibi: " + textMessage.getText());
								gtlMT.actualizarNotificaciones();
								FWJOptionPane.showInformationMessage(null, textMessage.getText(), "Notificacion");
								
							}
						} catch (JMSException e) {
							//e.printStackTrace();
						}
					}
				} catch (JMSException e) {
//					e.printStackTrace();
				}
			}
		});
	}

	public void stop() {
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
