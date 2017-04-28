package ar.com.textillevel.modulos.alertas.facade.api.local;

import javax.ejb.Local;

import ar.com.textillevel.modulos.notificaciones.entidades.NotificacionUsuario;
import ar.com.textillevel.modulos.notificaciones.enums.ETipoDestinoNotificacion;

@Local
public interface MensajeriaFacadeLocal {

	public void enviarNotificaciones(NotificacionUsuario notificacion, String nombreDestino, ETipoDestinoNotificacion tipoDestino);
}
