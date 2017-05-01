package main.servicios.alertas;

import main.servicios.alertas.gui.AccionAbrirFactura;
import ar.com.textillevel.modulos.notificaciones.enums.ETipoNotificacion;

public enum ETipoNotificacionUI {

	ODT_EN_OFICINA(1, new AccionAbrirFactura());

	private ETipoNotificacionUI(Integer id, AccionNotificacion<?>... acciones) {
		this.id = id;
		this.acciones = acciones;
	}

	private Integer id;
	private AccionNotificacion<?>[] acciones;

	public Integer getId() {
		return id;
	}

	public AccionNotificacion<?>[] getAcciones() {
		return acciones;
	}

	public static ETipoNotificacionUI by(ETipoNotificacion tipoBackend) {
		for(ETipoNotificacionUI tnui : values()) {
			if (tnui.getId().equals(tipoBackend.getId())) {
				return tnui;
			}
		}
		return null;
	}
}
