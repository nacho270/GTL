package ar.com.textillevel.modulos.personal.entidades.legajos.valesdeatencion;

import java.util.HashMap;
import java.util.Map;

public enum ETipoVale {

	ENFERMEDAD("Enfermedad"),
	PREOCUPACIONAL("Preocupacional"),
	ACCIDENTE("Accidente");

	private String descripcion;
	
	private ETipoVale(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public String toString() {
		return getDescripcion();
	}


	public static ETipoVale getByDescripcion(String descripcion) {
		if (descripcion == null) return null;
		return getDescripcionMap().get(descripcion);
	}
	
	private static Map<String, ETipoVale> descricpcionMap;
	
	private static Map<String, ETipoVale> getDescripcionMap() {
		if (descricpcionMap == null) {
			descricpcionMap = new HashMap<String, ETipoVale>();
			ETipoVale values[] = values();
			for (int i = 0; i < values.length; i++) {
				descricpcionMap.put(values[i].getDescripcion(), values[i]);
			}
		}
		return descricpcionMap;
	}

	
}
