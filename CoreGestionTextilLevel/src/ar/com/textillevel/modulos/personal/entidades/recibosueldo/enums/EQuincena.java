package ar.com.textillevel.modulos.personal.entidades.recibosueldo.enums;

import java.util.HashMap;
import java.util.Map;

public enum EQuincena {

	PRIMERA(1,"PRIMERA"),
	SEGUNDA(2,"SEGUNDA");

	private Integer id;
	private String nombre;

	private EQuincena (Integer id, String nombre) {
		this.id = id;
		this.nombre  = nombre;
	}

	public Integer getId() {
		return id;
	}
	
	public String getNombre() {
		return nombre;
	}

	public String toString() {
		return getNombre();
	}

	private static Map<Integer, EQuincena> keyMap;
	
	public static EQuincena getById(Integer id){
		if(id == null) return null;
		return getKeyMap().get(id);
	}
	
	private static Map<Integer, EQuincena> getKeyMap(){
		if(keyMap == null){
			keyMap = new HashMap<Integer, EQuincena>();
			EQuincena[] valores = values();
			for(int  i = 0; i<valores.length;i++){
				keyMap.put(valores[i].id,valores[i]);
			}
		}
		return keyMap;
	}


}
