package ar.com.textillevel.modulos.personal.entidades.legajos.valesdeatencion;

import java.util.HashMap;
import java.util.Map;

public enum EEStadoValeEnfermedad {

	ABIERTO						(1, "ABIERTO", ""), 
	NO_JUSTIFICADO				(2, "NO JUSTIFICADO", "Marcar el vale como No Justificado."),
	JUSTIFICADO_Y_CONTROL		(3, "JUSTIFICADO Y CONTROL", "Marcar el vale como Justificado y Control."),
	JUSTIFICADO_Y_ALTA			(4, "JUSTIFICADO Y ALTA", "Marcar el vale como Justificado y Alta.");

	private EEStadoValeEnfermedad(Integer id, String descripcion, String descrAccion) {
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

	public static EEStadoValeEnfermedad getById(Integer id) {
		if (id == null) return null;
		return getKeyMap().get(id);
	}

	private static Map<Integer, EEStadoValeEnfermedad> keyMap;

	private static Map<Integer, EEStadoValeEnfermedad> getKeyMap() {
		if (keyMap == null) {
			keyMap = new HashMap<Integer, EEStadoValeEnfermedad>();
			EEStadoValeEnfermedad values[] = values();
			for (int i = 0; i < values.length; i++) {
				keyMap.put(values[i].getId(), values[i]);
			}
		}
		return keyMap;
	}

	public static EEStadoValeEnfermedad getByDescripcion(String descripcion) {
		if (descripcion == null) return null;
		return getDescripcionMap().get(descripcion);
	}

	private static Map<String, EEStadoValeEnfermedad> descricpcionMap;
	
	private static Map<String, EEStadoValeEnfermedad> getDescripcionMap() {
		if (descricpcionMap == null) {
			descricpcionMap = new HashMap<String, EEStadoValeEnfermedad>();
			EEStadoValeEnfermedad values[] = values();
			for (int i = 0; i < values.length; i++) {
				descricpcionMap.put(values[i].getDescrAccion(), values[i]);
			}
		}
		return descricpcionMap;
	}

	
	@Override
	public String toString() {
		return getDescripcion();
	}

}