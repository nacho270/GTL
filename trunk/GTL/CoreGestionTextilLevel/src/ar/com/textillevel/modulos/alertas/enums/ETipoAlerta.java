package ar.com.textillevel.modulos.alertas.enums;

import java.util.HashMap;
import java.util.Map;

public enum ETipoAlerta {

	STOCK(1, "STOCK");

	private Integer id;
	private String descripcion;

	private ETipoAlerta(Integer id, String descripcion) {
		this.id = id;
		this.descripcion = descripcion;
	}

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

	private static Map<Integer, ETipoAlerta> keyMap;

	public static ETipoAlerta getById(Integer id) {
		if (id == null)
			return null;
		return getKeyMap().get(id);
	}

	private static Map<Integer, ETipoAlerta> getKeyMap() {
		if (keyMap == null) {
			keyMap = new HashMap<Integer, ETipoAlerta>();
			ETipoAlerta[] valores = values();
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
