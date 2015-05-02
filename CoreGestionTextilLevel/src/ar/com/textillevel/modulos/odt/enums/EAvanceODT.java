package ar.com.textillevel.modulos.odt.enums;

import java.util.HashMap;
import java.util.Map;

public enum EAvanceODT {
	POR_COMENZAR  		( (byte) 1, "Por comenzar"),
	EN_PROCESO			( (byte) 2, "En proceso"),
	FINALIZADO			( (byte) 3, "Finalizado");

	private EAvanceODT(Byte id, String descripcion) {
		this.id = id;
		this.descripcion = descripcion;
	}

	private Byte id;
	private String descripcion;

	public Byte getId() {
		return id;
	}

	public void setId(Byte id) {
		this.id = id;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	public static EAvanceODT getById(Byte id) {
		if (id == null) return null;
		return getKeyMap().get(id);
	}
	
	private static Map<Byte, EAvanceODT> keyMap;
	
	private static Map<Byte, EAvanceODT> getKeyMap() {
		if (keyMap == null) {
			keyMap = new HashMap<Byte, EAvanceODT>();
			EAvanceODT values[] = values();
			for (int i = 0; i < values.length; i++) {
				keyMap.put(values[i].getId(), values[i]);
			}
		}
		return keyMap;
	}
	
	@Override
	public String toString(){
		return descripcion;
	}
}
