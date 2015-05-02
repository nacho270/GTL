package ar.com.textillevel.modulos.odt.enums;

import java.util.HashMap;
import java.util.Map;

public enum ESectorMaquina {
	SECTOR_COSIDO  			( (byte) 1, "Sector cosido"),
	SECTOR_HUMEDO			( (byte) 2, "Sector húmedo"),
	SECTOR_SECO				( (byte) 3, "Sector seco"),
	SECTOR_ESTAMPERIA		( (byte) 4, "Sector estampería"),
	SECTOR_TERMINADO		( (byte) 5, "Sector terminado");

	private ESectorMaquina(Byte id, String descripcion) {
		this.id = id;
		this.descripcion = descripcion;
	}

	private Byte id;
	private String descripcion;

	public Byte getId() {
		return id;
	}

	public void setId(Byte id) {
		this.id = id;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	public static ESectorMaquina getById(Byte id) {
		if (id == null) return null;
		return getKeyMap().get(id);
	}
	
	private static Map<Byte, ESectorMaquina> keyMap;
	
	private static Map<Byte, ESectorMaquina> getKeyMap() {
		if (keyMap == null) {
			keyMap = new HashMap<Byte, ESectorMaquina>();
			ESectorMaquina values[] = values();
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
