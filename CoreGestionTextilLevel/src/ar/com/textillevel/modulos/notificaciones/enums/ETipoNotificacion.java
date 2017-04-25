package ar.com.textillevel.modulos.notificaciones.enums;

import java.util.HashMap;
import java.util.Map;

public enum ETipoNotificacion {

	ODT_EN_OFICINA(1, "La ODT %s se encuentra en OFICINA");

	private ETipoNotificacion(Integer id, String texto) {
		this.id = id;
		this.texto = texto;
	}

	private Integer id;
	private String texto;

	public Integer getId() {
		return id;
	}

	public String getTexto() {
		return texto;
	}

	private static Map<Integer, ETipoNotificacion> keyMap;

	public static ETipoNotificacion getById(Integer id) {
		if (id == null)
			return null;
		return getKeyMap().get(id);
	}

	private static Map<Integer, ETipoNotificacion> getKeyMap() {
		if (keyMap == null) {
			keyMap = new HashMap<Integer, ETipoNotificacion>();
			ETipoNotificacion[] valores = values();
			for (int i = 0; i < valores.length; i++) {
				keyMap.put(valores[i].id, valores[i]);
			}
		}
		return keyMap;
	}
}
