package ar.com.textillevel.entidades.enums;

import java.util.HashMap;
import java.util.Map;

public enum EEstadoRecibo {

	PENDIENTE			( 1, "Pendiente"), 
	ACEPTADO			( 2, "Aceptado"), 
	RECHAZADO			( 3, "Rechazado");

	private EEstadoRecibo(Integer id, String descripcion) {
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
	
	public static EEstadoRecibo getById(Integer id) {
		if (id == null) return null;
		return getKeyMap().get(id);
	}
	
	private static Map<Integer, EEstadoRecibo> keyMap;
	
	private static Map<Integer, EEstadoRecibo> getKeyMap() {
		if (keyMap == null) {
			keyMap = new HashMap<Integer, EEstadoRecibo>();
			EEstadoRecibo values[] = values();
			for (int i = 0; i < values.length; i++) {
				keyMap.put(values[i].getId(), values[i]);
			}
		}
		return keyMap;
	}
	
	public String toString() {
		return getDescripcion();
	}
}
