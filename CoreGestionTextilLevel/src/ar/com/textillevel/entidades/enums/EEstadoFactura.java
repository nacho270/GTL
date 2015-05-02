package ar.com.textillevel.entidades.enums;

import java.util.HashMap;
import java.util.Map;

public enum EEstadoFactura {
	
	IMPAGA	( 1, "Impaga"), 
	PAGADA	( 2, "Pagada"), 
	ANULADA	( 3, "Anulada"),
	VERIFICADA ( 4 , "Verificada");

	private EEstadoFactura(Integer id, String descripcion) {
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
	
	public static EEstadoFactura getById(Integer id) {
		if (id == null) return null;
		return getKeyMap().get(id);
	}
	
	private static Map<Integer, EEstadoFactura> keyMap;
	
	private static Map<Integer, EEstadoFactura> getKeyMap() {
		if (keyMap == null) {
			keyMap = new HashMap<Integer, EEstadoFactura>();
			EEstadoFactura values[] = values();
			for (int i = 0; i < values.length; i++) {
				keyMap.put(values[i].getId(), values[i]);
			}
		}
		return keyMap;
	}
}
