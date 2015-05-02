package ar.com.textillevel.modulos.personal.entidades.legajos.sanciones;

import java.util.HashMap;
import java.util.Map;

public enum EEStadoCartaDocumento {

	CREADA				( 1, "CREADA", ""), 
	IMPRESA				( 2, "IMPRESA", "Imprimir la carta documento."),
	ENVIADA				( 3, "ENVIADA", "Marcar como enviada la carta documento."),
	RECIBIDA			( 4, "RECIBIDA", "Marcar como recibida la carta documento."),
	NO_RECIBIDA			( 5, "NO RECIBIDA", "Marcar como no recibida la carta documento."),
	JUSTIFICADA			( 6, "JUSTIFICADA", "Marcar como justificada la carta documento."),
	NO_JUSTIFICADA		( 7, "JUSTIFICADA", "Marcar como no justificada la carta documento.")
	;

	private EEStadoCartaDocumento(Integer id, String descripcion, String descrAccion) {
		this.id = id;
		this.descripcion = descripcion;
		this.descrAccion = descrAccion;
	}

	private Integer id;
	private String descripcion;
	private String descrAccion;

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

	public String getDescrAccion() {
		return descrAccion;
	}

	public void setDescrAccion(String descrAccion) {
		this.descrAccion = descrAccion;
	}

	public static EEStadoCartaDocumento getById(Integer id) {
		if (id == null) return null;
		return getKeyMap().get(id);
	}

	private static Map<Integer, EEStadoCartaDocumento> keyMap;

	private static Map<Integer, EEStadoCartaDocumento> getKeyMap() {
		if (keyMap == null) {
			keyMap = new HashMap<Integer, EEStadoCartaDocumento>();
			EEStadoCartaDocumento values[] = values();
			for (int i = 0; i < values.length; i++) {
				keyMap.put(values[i].getId(), values[i]);
			}
		}
		return keyMap;
	}

	@Override
	public String toString() {
		return getDescripcion();
	}

	public static EEStadoCartaDocumento getByDescripcion(String descripcion) {
		if (descripcion == null) return null;
		return getDescripcionMap().get(descripcion);
	}

	private static Map<String, EEStadoCartaDocumento> descricpcionMap;
	
	private static Map<String, EEStadoCartaDocumento> getDescripcionMap() {
		if (descricpcionMap == null) {
			descricpcionMap = new HashMap<String, EEStadoCartaDocumento>();
			EEStadoCartaDocumento values[] = values();
			for (int i = 0; i < values.length; i++) {
				descricpcionMap.put(values[i].getDescrAccion(), values[i]);
			}
		}
		return descricpcionMap;
	}

}