package ar.com.textillevel.modulos.personal.entidades.legajos.sanciones;

import java.util.HashMap;
import java.util.Map;

public enum ETipoSancion {

	APERCIBIMIENTO("Apercibimiento"),
	CARTA_DOCUMENTO("Carta Documento");
	
	private String descripcion;
	
	private ETipoSancion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public String toString() {
		return getDescripcion();
	}


	public static ETipoSancion getByDescripcion(String descripcion) {
		if (descripcion == null) return null;
		return getDescripcionMap().get(descripcion);
	}
	
	private static Map<String, ETipoSancion> descricpcionMap;
	
	private static Map<String, ETipoSancion> getDescripcionMap() {
		if (descricpcionMap == null) {
			descricpcionMap = new HashMap<String, ETipoSancion>();
			ETipoSancion values[] = values();
			for (int i = 0; i < values.length; i++) {
				descricpcionMap.put(values[i].getDescripcion(), values[i]);
			}
		}
		return descricpcionMap;
	}

	
}
