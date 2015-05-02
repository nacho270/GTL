package ar.com.textillevel.entidades.enums;

import java.util.HashMap;
import java.util.Map;

public enum ETipoRubro {

	PROVEEDOR(1, "Proveedor"),
	PERSONA(2, "Persona");

	private int id;
	private String criterio;

	private ETipoRubro(int id, String criterio) {
		this.id = id;
		this.criterio = criterio;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCriterio() {
		return criterio;
	}

	public void setCriterio(String criterio) {
		this.criterio = criterio;
	}
	
	public String toString(){
		return this.criterio;
	}
	
	public static ETipoRubro getById(Integer id) {
		if (id == null)
			return null;
		return getKeyMap().get(id);
	}

	private static Map<Integer, ETipoRubro> keyMap;

	private static Map<Integer, ETipoRubro> getKeyMap() {
		if (keyMap == null) {
			keyMap = new HashMap<Integer, ETipoRubro>();
			ETipoRubro values[] = values();
			for (int i = 0; i < values.length; i++) {
				keyMap.put(values[i].getId(), values[i]);
			}
		}
		return keyMap;
	}
}
