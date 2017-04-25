package ar.com.textillevel.modulos.odt.enums;

import java.util.HashMap;
import java.util.Map;

public enum ESectorMaquina {

	SECTOR_COSIDO  			( (byte) 1, "Sector cosido", true),
	SECTOR_HUMEDO			( (byte) 2, "Sector húmedo", false),
	SECTOR_SECO				( (byte) 3, "Sector seco", false),
	SECTOR_ESTAMPERIA		( (byte) 4, "Sector estampería", false),
	SECTOR_TERMINADO		( (byte) 5, "Sector terminado", true);

	private ESectorMaquina(Byte id, String descripcion, boolean admiteInterProcesamiento) {
		this.id = id;
		this.descripcion = descripcion;
		this.admiteInterProcesamiento = admiteInterProcesamiento;
	}

	private Byte id;
	private String descripcion;
	private boolean admiteInterProcesamiento; //cuando es true significa que en ese sector se hace un procesamiento intermedio a nivel sistema, o dicho, de otra manera si 
											  //ese sector tiene un modulo de lite asociado que no herede de ProcesarODTEnSectorAbstractAction que sea solo leer la ODT y loguear
											  //las 2 cambios de estado POR_COMENZAR/FINALIZADO en la misma operación.

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

	public boolean isAdmiteInterProcesamiento() {
		return admiteInterProcesamiento;
	}

	public void setAdmiteInterProcesamiento(boolean admiteInterProcesamiento) {
		this.admiteInterProcesamiento = admiteInterProcesamiento;
	}

	public static ESectorMaquina getById(Byte id) {
		if (id == null) return null;
		return getKeyMap().get(id);
	}
	
	private static Map<Byte, ESectorMaquina> keyMap;
	
	private static Map<Byte, ESectorMaquina> getKeyMap() {
		if (keyMap == null) {
			keyMap = new HashMap<Byte, ESectorMaquina>();
			ESectorMaquina values[] = values();
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
