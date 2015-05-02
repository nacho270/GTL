package ar.com.textillevel.modulos.chat.schedule;

import java.io.IOException;
import java.util.Date;

import org.apache.log4j.Logger;
import org.jboss.varia.scheduler.Schedulable;

import ar.com.textillevel.modulos.chat.server.ChatServer;

public class ScheduleStartChatServer implements Schedulable{

	private static final Logger logger = Logger.getLogger(ScheduleStartChatServer.class);
	
	public void perform(Date arg0, long arg1) {
		try {
			logger.info("*********************INCIANDO SERVICIO DE CHAT*********************");
			ChatServer.getInstance().arrancar();
			logger.info("*********************SERVICIO DE CHAT INICIADO*********************");
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			//e.printStackTrace();
		}
	}
}
