package ar.com.textillevel.entidades.enums;

import java.util.HashMap;
import java.util.Map;

public enum ETipoRemitoSalida {

	CLIENTE							( 1), 
	PROVEEDOR						( 2), 
	CLIENTE_VENTA_DE_TELA			( 3),
	CLIENTE_SALIDA_01				(4);
	
	private ETipoRemitoSalida(Integer id) {
		this.id = id;
	}

	private Integer id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public static ETipoRemitoSalida getById(Integer id) {
		if (id == null) return null;
		return getKeyMap().get(id);
	}
	
	private static Map<Integer, ETipoRemitoSalida> keyMap;
	
	private static Map<Integer, ETipoRemitoSalida> getKeyMap() {
		if (keyMap == null) {
			keyMap = new HashMap<Integer, ETipoRemitoSalida>();
			ETipoRemitoSalida values[] = values();
			for (int i = 0; i < values.length; i++) {
				keyMap.put(values[i].getId(), values[i]);
			}
		}
		return keyMap;
	}
	
}
