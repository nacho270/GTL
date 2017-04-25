package ar.com.textillevel.modulos.notificaciones.enums;

import java.util.HashMap;
import java.util.Map;

public enum ETipoDestinoNotificacion {

	QUEUE((byte)1),
	TOPIC((byte)2);

	private ETipoDestinoNotificacion(Byte id) {
		this.id = id;
	}

	private Byte id;

	public Byte getId() {
		return id;
	}

	private static Map<Byte, ETipoDestinoNotificacion> keyMap;

	public static ETipoDestinoNotificacion getById(Byte id) {
		if (id == null)
			return null;
		return getKeyMap().get(id);
	}

	private static Map<Byte, ETipoDestinoNotificacion> getKeyMap() {
		if (keyMap == null) {
			keyMap = new HashMap<Byte, ETipoDestinoNotificacion>();
			ETipoDestinoNotificacion[] valores = values();
			for (int i = 0; i < valores.length; i++) {
				keyMap.put(valores[i].id, valores[i]);
			}
		}
		return keyMap;
	}
}
