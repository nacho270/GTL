package ar.com.textillevel.entidades.enums;

import java.util.HashMap;
import java.util.Map;

public enum ETipoMoneda {

	PESOS(1, "Pesos"),
	DOLAR(2, "Dolar");

	private Integer id;
	private String descripcion;

	private ETipoMoneda(Integer id, String descripcion) {
		this.id = id;
		this.descripcion = descripcion;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String toString() {
		return descripcion;
	}

	public static ETipoMoneda getById(Integer id) {
		if (id == null) return null;
		return getKeyMap().get(id);
	}
	
	private static Map<Integer, ETipoMoneda> keyMap;
	
	private static Map<Integer, ETipoMoneda> getKeyMap() {
		if (keyMap == null) {
			keyMap = new HashMap<Integer, ETipoMoneda>();
			ETipoMoneda values[] = values();
			for (int i = 0; i < values.length; i++) {
				keyMap.put(values[i].getId(), values[i]);
			}
		}
		return keyMap;
	}

}