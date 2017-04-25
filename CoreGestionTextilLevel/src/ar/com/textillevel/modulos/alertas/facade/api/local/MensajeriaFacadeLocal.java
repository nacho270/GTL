package ar.com.textillevel.modulos.alertas.facade.api.local;

import javax.ejb.Local;

import ar.com.textillevel.modulos.notificaciones.enums.ETipoNotificacion;

@Local
public interface MensajeriaFacadeLocal {

	public void generarNotificaciones(ETipoNotificacion tipo, Object...parms);
}
