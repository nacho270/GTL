package ar.com.textillevel.modulos.personal.entidades.legajos;

import java.util.HashMap;
import java.util.Map;

public enum EEstudiosCursados {
	PRIMARIO(1, "PRIMARIO"), 
	SECUNDARIO(2, "SECUNDARIO"), 
	TERCIARIO(3, "TERCIARIO"), 
	UNIVERSITARIO(4, "UNIVERSITARIO");

	private int id;
	private String descripcion;

	private EEstudiosCursados(int id, String descripcion) {
		this.id = id;
		this.descripcion = descripcion;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	private static Map<Integer, EEstudiosCursados> keyMap;

	public static EEstudiosCursados getById(Integer id) {
		if (id == null)
			return null;
		return getKeyMap().get(id);
	}

	private static Map<Integer, EEstudiosCursados> getKeyMap() {
		if (keyMap == null) {
			keyMap = new HashMap<Integer, EEstudiosCursados>();
			EEstudiosCursados[] valores = values();
			for (int i = 0; i < valores.length; i++) {
				keyMap.put(valores[i].id, valores[i]);
			}
		}
		return keyMap;
	}

	@Override
	public String toString() {
		return descripcion;
	}

}
