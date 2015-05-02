package ar.com.textillevel.entidades.enums;

import java.util.HashMap;
import java.util.Map;

public enum EnumTipoFecha {
	
	FECHA_ENTRADA (1, "Fecha entrada"),
	FECHA_SALIDA  (2, "Fecha salida");
	
	private EnumTipoFecha(Integer id, String descripcion){
		this.setId(id);
		this.setDescripcion(descripcion);
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
	
	public static EnumTipoFecha getById(Integer id) {
		if (id == null)
			return null;
		return getKeyMap().get(id);
	}

	private static Map<Integer, EnumTipoFecha> keyMap;

	private static Map<Integer, EnumTipoFecha> getKeyMap() {
		if (keyMap == null) {
			keyMap = new HashMap<Integer, EnumTipoFecha>();
			EnumTipoFecha values[] = values();
			for (int i = 0; i < values.length; i++) {
				keyMap.put(values[i].getId(), values[i]);
			}
		}
		return keyMap;
	}
	
	@Override
	public String toString(){
		return this.getDescripcion();
	}
}
