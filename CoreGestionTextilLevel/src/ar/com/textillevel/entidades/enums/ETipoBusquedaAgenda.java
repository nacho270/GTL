package ar.com.textillevel.entidades.enums;

import java.util.HashMap;
import java.util.Map;

public enum ETipoBusquedaAgenda {

	TODOS(1, "Todos"),
	CLIENTE(2, "Cliente"), 
	PROVEEDOR(3, "Proveedor"),
	PERSONA(4, "Persona");

	private int id;
	private String criterio;

	private ETipoBusquedaAgenda(int id, String criterio) {
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

	public static ETipoBusquedaAgenda getById(Integer id) {
		if (id == null) return null;
		return getKeyMap().get(id);
	}
	
	private static Map<Integer, ETipoBusquedaAgenda> keyMap;
	
	private static Map<Integer, ETipoBusquedaAgenda> getKeyMap() {
		if (keyMap == null) {
			keyMap = new HashMap<Integer, ETipoBusquedaAgenda>();
			ETipoBusquedaAgenda values[] = values();
			for (int i = 0; i < values.length; i++) {
				keyMap.put(values[i].getId(), values[i]);
			}
		}
		return keyMap;
	}

}
