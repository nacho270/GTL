package ar.com.textillevel.modulos.personal.entidades.legajos;

import java.util.HashMap;
import java.util.Map;


public enum ESexo {
	MASCULINO(1, "MASCULINO"), 
	FEMENINO(2, "FEMENINO");

	private int id;
	private String descripcion;

	private ESexo(int id, String descripcion) {
		this.id = id;
		this.descripcion = descripcion;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	private static Map<Integer, ESexo> keyMap;
	
	public static ESexo getById(Integer id){
		if(id == null) return null;
		return getKeyMap().get(id);
	}
	
	private static Map<Integer, ESexo> getKeyMap(){
		if(keyMap == null){
			keyMap = new HashMap<Integer, ESexo>();
			ESexo[] valores = values();
			for(int  i = 0; i<valores.length;i++){
				keyMap.put(valores[i].id,valores[i]);
			}
		}
		return keyMap;
	}

	@Override
	public String toString() {
		return descripcion;
	}
}
