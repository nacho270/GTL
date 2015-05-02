package ar.com.textillevel.modulos.personal.entidades.recibosueldo.enums;

import java.util.HashMap;
import java.util.Map;

public enum EEstadoReciboSueldo {

	CREADO(1, "CREADO"),
	VERIFICADO(2, "VERIFICADO"),
	IMPRESO(3, "IMPRESO");

	private Integer id;
	private String nombre;
	
	private EEstadoReciboSueldo(Integer id, String nombre) {
		this.id = id;
		this.nombre = nombre;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	private static Map<Integer, EEstadoReciboSueldo> keyMap;
	
	public static EEstadoReciboSueldo getById(Integer id){
		if(id == null) return null;
		return getKeyMap().get(id);
	}
	
	private static Map<Integer, EEstadoReciboSueldo> getKeyMap(){
		if(keyMap == null){
			keyMap = new HashMap<Integer, EEstadoReciboSueldo>();
			EEstadoReciboSueldo[] valores = values();
			for(int  i = 0; i<valores.length;i++){
				keyMap.put(valores[i].id,valores[i]);
			}
		}
		return keyMap;
	}

}