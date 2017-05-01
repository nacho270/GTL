package main;

import ar.com.textillevel.modulos.notificaciones.entidades.NotificacionUsuario;


public interface NotificableMainTemplate {

	public void actualizarNotificaciones();

	public void mostrarNotificacion(String text);

	public void mostrarNotificacion(NotificacionUsuario notifiacion);
}
