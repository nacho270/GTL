package ar.com.textillevel.entidades.documentos.remito.enums;

import java.util.HashMap;
import java.util.Map;

public enum ESituacionODTRE {

	CON_ODT								(0,"CON ODT"), 
	CON_ODT_PARCIAL						(1,"CON ODT SIN COLOR"), 
	SIN_ODT								(2,"SIN ODT");

	private ESituacionODTRE(Integer id, String descripcion) {
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
	
	public static ESituacionODTRE getById(Integer id) {
		if (id == null) return null;
		return getKeyMap().get(id);
	}
	
	private static Map<Integer, ESituacionODTRE> keyMap;
	
	private static Map<Integer, ESituacionODTRE> getKeyMap() {
		if (keyMap == null) {
			keyMap = new HashMap<Integer, ESituacionODTRE>();
			ESituacionODTRE values[] = values();
			for (int i = 0; i < values.length; i++) {
				keyMap.put(values[i].getId(), values[i]);
			}
		}
		return keyMap;
	}

	@Override
	public String toString() {
		return descripcion;
	}
}