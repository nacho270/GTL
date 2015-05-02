package ar.com.textillevel.modulos.personal.entidades.fichadas.enums;

import java.util.HashMap;
import java.util.Map;

public enum ETipoFichada {

	ENTRADA(1, "Entrada", "E"), SALIDA(2, "Salida", "S");

	private ETipoFichada(Integer id, String descripcion, String inicial) {
		this.setId(id);
		this.setDescripcion(descripcion);
		this.setInicial(inicial);
	}

	private Integer id;
	private String descripcion;
	private String inicial;

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

	private static Map<Integer, ETipoFichada> keyMap;
	private static Map<String, ETipoFichada> inicialMap;

	public static ETipoFichada getById(Integer id) {
		if (id == null)
			return null;
		return getKeyMap().get(id);
	}

	public static ETipoFichada getByInicial(String inicial) {
		if (inicial == null)
			return null;
		return getInicialMap().get(inicial);
	}

	private static Map<String, ETipoFichada> getInicialMap() {
		if (inicialMap == null) {
			inicialMap = new HashMap<String, ETipoFichada>();
			ETipoFichada[] valores = values();
			for (int i = 0; i < valores.length; i++) {
				inicialMap.put(valores[i].inicial, valores[i]);
			}
		}
		return inicialMap;
	}

	private static Map<Integer, ETipoFichada> getKeyMap() {
		if (keyMap == null) {
			keyMap = new HashMap<Integer, ETipoFichada>();
			ETipoFichada[] valores = values();
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

	public String getInicial() {
		return inicial;
	}

	public void setInicial(String inicial) {
		this.inicial = inicial;
	}
}
