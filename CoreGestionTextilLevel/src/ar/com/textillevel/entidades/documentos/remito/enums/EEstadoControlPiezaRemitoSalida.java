package ar.com.textillevel.entidades.documentos.remito.enums;

import java.util.HashMap;
import java.util.Map;

public enum EEstadoControlPiezaRemitoSalida {

	PENDIENTE								(0,"PENDIENTE"), 
	MTS_OK_CONTROLADA						(1,"OK"), 
	MTS_NO_OK_REIMPRESA						(2,"OK-IMPR."),
	MTS_NO_OK_NO_IMPRESA					(3,"NO OK");

	private EEstadoControlPiezaRemitoSalida(Integer id, String descripcion) {
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
	
	public static EEstadoControlPiezaRemitoSalida getById(Integer id) {
		if (id == null) return null;
		return getKeyMap().get(id);
	}
	
	private static Map<Integer, EEstadoControlPiezaRemitoSalida> keyMap;
	
	private static Map<Integer, EEstadoControlPiezaRemitoSalida> getKeyMap() {
		if (keyMap == null) {
			keyMap = new HashMap<Integer, EEstadoControlPiezaRemitoSalida>();
			EEstadoControlPiezaRemitoSalida values[] = values();
			for (int i = 0; i < values.length; i++) {
				keyMap.put(values[i].getId(), values[i]);
			}
		}
		return keyMap;
	}

	@Override
	public String toString() {
		return descripcion;
	}
}