package ar.com.textillevel.modulos.personal.entidades.legajos.sanciones;

import java.util.HashMap;
import java.util.Map;

public enum ETipoCartaDocumento {

	AVISO_JUSTIF_FALTA			( 1, "AVISO DE JUSTIFICACION DE FALTAS"), 
	SANCION_POR_NO_JUSTIF 		( 2, "SANCION POR NO JUSTIFICAR FALTAS"),
	OTRA						( 3, "OTRA");

	private ETipoCartaDocumento(Integer id, String descripcion) {
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
	
	public static ETipoCartaDocumento getById(Integer id) {
		if (id == null) return null;
		return getKeyMap().get(id);
	}

	private static Map<Integer, ETipoCartaDocumento> keyMap;

	private static Map<Integer, ETipoCartaDocumento> getKeyMap() {
		if (keyMap == null) {
			keyMap = new HashMap<Integer, ETipoCartaDocumento>();
			ETipoCartaDocumento values[] = values();
			for (int i = 0; i < values.length; i++) {
				keyMap.put(values[i].getId(), values[i]);
			}
		}
		return keyMap;
	}

	public static ETipoCartaDocumento getByDescripcion(String descripcion) {
		if (descripcion == null) return null;
		return getDescripcionMap().get(descripcion);
	}

	private static Map<String, ETipoCartaDocumento> descricpcionMap;
	
	private static Map<String, ETipoCartaDocumento> getDescripcionMap() {
		if (descricpcionMap == null) {
			descricpcionMap = new HashMap<String, ETipoCartaDocumento>();
			ETipoCartaDocumento values[] = values();
			for (int i = 0; i < values.length; i++) {
				descricpcionMap.put(values[i].getDescripcion(), values[i]);
			}
		}
		return descricpcionMap;
	}
	
	@Override
	public String toString() {
		return getDescripcion();
	}

}