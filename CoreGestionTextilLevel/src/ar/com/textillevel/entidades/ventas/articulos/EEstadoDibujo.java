package ar.com.textillevel.entidades.ventas.articulos;

import java.util.HashMap;
import java.util.Map;

public enum EEstadoDibujo {
	
	EN_STOCK ( 1, "EN STOCK"),
	DEVUELTO ( 2, "DEVUELTO"),
	GRABADO ( 3, "GRABADO"),
	ROTO ( 4, "ROTO");

	private EEstadoDibujo(Integer id, String descripcion) {
		this.id = id;
		this.descripcion = descripcion;
	}

	private Integer id;
	private String descripcion;

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
	
	public static EEstadoDibujo getById(Integer id) {
		if (id == null) return null;
		return getKeyMap().get(id);
	}
	
	private static Map<Integer, EEstadoDibujo> keyMap;
	
	private static Map<Integer, EEstadoDibujo> getKeyMap() {
		if (keyMap == null) {
			keyMap = new HashMap<Integer, EEstadoDibujo>();
			EEstadoDibujo values[] = values();
			for (int i = 0; i < values.length; i++) {
				keyMap.put(values[i].getId(), values[i]);
			}
		}
		return keyMap;
	}
	
	@Override
	public String toString(){
		return descripcion;
	}
}
