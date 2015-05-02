package ar.com.textillevel.mobile.modules.cuenta.common;

import java.util.HashMap;
import java.util.Map;

public enum ETipoCuenta {
	CLIENTE(1), 
	PROVEEDOR(2);

	private ETipoCuenta(Integer id) {
		this.id = id;
	}

	private Integer id;
	
	public Integer getId() {
		return id;
	}

	private static Map<Integer, ETipoCuenta> keyMap;

	public static ETipoCuenta getById(Integer id) {
		if (id == null)
			return null;
		return getKeyMap().get(id);
	}

	private static Map<Integer, ETipoCuenta> getKeyMap() {
		if (keyMap == null) {
			keyMap = new HashMap<Integer, ETipoCuenta>();
			ETipoCuenta[] valores = values();
			for (int i = 0; i < valores.length; i++) {
				keyMap.put(valores[i].id, valores[i]);
			}
		}
		return keyMap;
	}
}
