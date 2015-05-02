package ar.com.textillevel.entidades.enums;

import java.util.HashMap;
import java.util.Map;

public enum EEstadoOrdenDePago {
	PREPARADO			( 1, "Preparado"), 
	VERIFICADO			( 2, "Verificado");

	private EEstadoOrdenDePago(Integer id, String descripcion) {
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
	
	public static EEstadoOrdenDePago getById(Integer id) {
		if (id == null) return null;
		return getKeyMap().get(id);
	}
	
	private static Map<Integer, EEstadoOrdenDePago> keyMap;
	
	private static Map<Integer, EEstadoOrdenDePago> getKeyMap() {
		if (keyMap == null) {
			keyMap = new HashMap<Integer, EEstadoOrdenDePago>();
			EEstadoOrdenDePago values[] = values();
			for (int i = 0; i < values.length; i++) {
				keyMap.put(values[i].getId(), values[i]);
			}
		}
		return keyMap;
	}
	
	@Override
	public String toString() {
		return getDescripcion();
	}
}
