package ar.com.textillevel.entidades.documentos.remito.enums;

import java.util.HashMap;
import java.util.Map;

public enum ELugarTarima {

	ABAJO								(0,"ABAJO", "AB"), 
	MEDIO								(1,"MEDIO", "MED"), 
	ARRIBA								(2,"ARRIBA", "ARR");

	private ELugarTarima(Integer id, String descripcion, String descrCorta) {
		this.id = id;
		this.descripcion = descripcion;
		this.descrCorta = descrCorta;
	}

	private Integer id;
	private String descripcion;
	private String descrCorta;


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
	
	public String getDescrCorta() {
		return descrCorta;
	}

	public void setDescrCorta(String descrCorta) {
		this.descrCorta = descrCorta;
	}

	public static ELugarTarima getById(Integer id) {
		if (id == null) return null;
		return getKeyMap().get(id);
	}
	
	private static Map<Integer, ELugarTarima> keyMap;
	
	private static Map<Integer, ELugarTarima> getKeyMap() {
		if (keyMap == null) {
			keyMap = new HashMap<Integer, ELugarTarima>();
			ELugarTarima values[] = values();
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