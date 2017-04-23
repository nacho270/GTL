package ar.com.textillevel.modulos.alertas.facade.api.local;

import javax.ejb.Local;

@Local
public interface MensajeriaFacadeLocal {

	public void enviarMensaje();
}
