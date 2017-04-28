package ar.com.textillevel.modulos.notificaciones.facade.api.local;

import javax.ejb.Local;

import ar.com.textillevel.modulos.notificaciones.enums.ETipoNotificacion;

@Local
public interface NotificacionUsuarioFacadeLocal {

	public void generarNotificaciones(ETipoNotificacion tipo, Object...parms);

}
