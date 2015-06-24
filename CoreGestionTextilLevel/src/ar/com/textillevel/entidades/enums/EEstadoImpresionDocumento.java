package ar.com.textillevel.entidades.enums;

import java.util.HashMap;
import java.util.Map;

public enum EEstadoImpresionDocumento {

	PENDIENTE		( 1, "Pendiente"), 
	AUTORIZADO_AFIP	( 3, "Autorizado AFIP"),
	IMPRESO			( 2, "Impreso");

	private EEstadoImpresionDocumento(Integer id, String descripcion) {
		this.id = id;
		this.descripcion = descripcion;
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
	
	public static EEstadoImpresionDocumento getById(Integer id) {
		if (id == null) return null;
		return getKeyMap().get(id);
	}
	
	private static Map<Integer, EEstadoImpresionDocumento> keyMap;
	
	private static Map<Integer, EEstadoImpresionDocumento> getKeyMap() {
		if (keyMap == null) {
			keyMap = new HashMap<Integer, EEstadoImpresionDocumento>();
			EEstadoImpresionDocumento values[] = values();
			for (int i = 0; i < values.length; i++) {
				keyMap.put(values[i].getId(), values[i]);
			}
		}
		return keyMap;
	}
}
