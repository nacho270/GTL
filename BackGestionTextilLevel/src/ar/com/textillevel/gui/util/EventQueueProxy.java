package ar.com.textillevel.gui.util;

import java.awt.AWTEvent;
import java.awt.EventQueue;
import java.util.Timer;
import java.util.TimerTask;

import main.GTLMainTemplate;

import org.apache.log4j.Logger;

import ar.clarin.fwjava.boss.BossError;
import ar.clarin.fwjava.componentes.error.CLErrorDialog;

public class EventQueueProxy extends EventQueue {

	private static final Logger logger = Logger.getLogger(EventQueueProxy.class);
	
	private static final int MAX_SEGUNDOS_INACTIVO = 10;
	private static int segundosInactivo = 0;
	
	public EventQueueProxy(final GTLMainTemplate mt){
		TimerTask timerTask = new TimerTask() {
			@Override
			public void run() {
				segundosInactivo++;
				if(segundosInactivo>=MAX_SEGUNDOS_INACTIVO){
					segundosInactivo=0;
//					mt.logout();
				}
			}
		};
		Timer t = new Timer();
		t.scheduleAtFixedRate(timerTask, 0,1000);
	}
	
	@Override
	protected void dispatchEvent(AWTEvent newEvent) {
		try {
			// aca quizas haya que obviar los eventos de mouse y teclado. 
			//Tambien esta el problema de que en los modulos hay un timer que pide cosas periodicamente, entonces se ejecuta siempe el dispatch event
			//Otros problema es que no cierra los jdialogs
//			if(!(newEvent.getSource() instanceof WToolkit)){
//				System.out.println("Evento recibido: " + newEvent.toString());
//				System.out.println("Originado por: " + newEvent.getSource());
//				segundosInactivo=0;
//			}
			super.dispatchEvent(newEvent);
		} catch (Exception t) {
			String message = t.getMessage();
			logger.error(message, t);
//			if (message == null || message.length() == 0) {
//				message += "\n\nFatal: " + t.getClass();
//			}
//			JOptionPane.showMessageDialog(null, StringW.wordWrap(message),"Error general", JOptionPane.ERROR_MESSAGE);
			new CLErrorDialog(BossError.ERR_APLICACION, message, null, null, t, null).setVisible(true);
		}
	}
}
