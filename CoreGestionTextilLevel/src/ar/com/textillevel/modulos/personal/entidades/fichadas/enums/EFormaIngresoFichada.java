package ar.com.textillevel.modulos.personal.entidades.fichadas.enums;

import java.util.HashMap;
import java.util.Map;

public enum EFormaIngresoFichada {

	MANUAL(1, "Manual"), 
	AUTOMATICA(2, "Automática");

	private EFormaIngresoFichada(Integer id, String descripcion) {
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

	private static Map<Integer, EFormaIngresoFichada> keyMap;

	public static EFormaIngresoFichada getById(Integer id) {
		if (id == null)
			return null;
		return getKeyMap().get(id);
	}

	private static Map<Integer, EFormaIngresoFichada> getKeyMap() {
		if (keyMap == null) {
			keyMap = new HashMap<Integer, EFormaIngresoFichada>();
			EFormaIngresoFichada[] valores = values();
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