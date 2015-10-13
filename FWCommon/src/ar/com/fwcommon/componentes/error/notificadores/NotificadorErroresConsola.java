package ar.com.fwcommon.componentes.error.notificadores;

import ar.com.fwcommon.componentes.error.FWException;

public class NotificadorErroresConsola implements INotificadorErrores {

	private static NotificadorErroresConsola instance;

	private NotificadorErroresConsola() {
	}

	public static NotificadorErroresConsola getInstance() {
		if(instance == null) {
			instance = new NotificadorErroresConsola();
		}
		return instance;
	}

	public void notificarError(FWException e) {
		e.printStackTrace();
	}

}