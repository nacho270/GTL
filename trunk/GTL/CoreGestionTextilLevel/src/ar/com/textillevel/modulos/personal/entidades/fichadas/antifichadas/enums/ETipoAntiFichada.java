package ar.com.textillevel.modulos.personal.entidades.fichadas.antifichadas.enums;

import java.util.HashMap;
import java.util.Map;

public enum ETipoAntiFichada {
	PARCIAL(1, "Parcial"), 
	VIGENCIA(2, "Vigencia");

	private ETipoAntiFichada(Integer id, String descripcion) {
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

	private static Map<Integer, ETipoAntiFichada> keyMap;

	public static ETipoAntiFichada getById(Integer id) {
		if (id == null)
			return null;
		return getKeyMap().get(id);
	}


	private static Map<Integer, ETipoAntiFichada> getKeyMap() {
		if (keyMap == null) {
			keyMap = new HashMap<Integer, ETipoAntiFichada>();
			ETipoAntiFichada[] valores = values();
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
