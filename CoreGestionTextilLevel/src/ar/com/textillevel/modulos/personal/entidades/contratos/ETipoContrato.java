package ar.com.textillevel.modulos.personal.entidades.contratos;

import java.util.HashMap;
import java.util.Map;

public enum ETipoContrato {
	PLAZO_FIJO(1, "Plazo fijo"), 
	A_PRUEBA(2, "A prueba"),
	EFECTIVO(3,"Efectivo");

	private int id;
	private String descripcion;

	private ETipoContrato(int id, String descripcion) {
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

	private static Map<Integer, ETipoContrato> keyMap;

	public static ETipoContrato getById(Integer id) {
		if (id == null)
			return null;
		return getKeyMap().get(id);
	}

	private static Map<Integer, ETipoContrato> getKeyMap() {
		if (keyMap == null) {
			keyMap = new HashMap<Integer, ETipoContrato>();
			ETipoContrato[] valores = values();
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
