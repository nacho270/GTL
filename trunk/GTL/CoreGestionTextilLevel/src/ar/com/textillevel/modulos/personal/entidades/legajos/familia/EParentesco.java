package ar.com.textillevel.modulos.personal.entidades.legajos.familia;

import java.util.HashMap;
import java.util.Map;


public enum EParentesco {
	PADRE(1, "PADRE"), 
	MADRE(2, "MADRE"),
	CONYUGE(3, "CONYUGE"),
	HIJO_A(4,"HIJO/A");

	private int id;
	private String descripcion;

	private EParentesco(int id, String descripcion) {
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

	private static Map<Integer, EParentesco> keyMap;
	
	public static EParentesco getById(Integer id){
		if(id == null) return null;
		return getKeyMap().get(id);
	}
	
	private static Map<Integer, EParentesco> getKeyMap(){
		if(keyMap == null){
			keyMap = new HashMap<Integer, EParentesco>();
			EParentesco[] valores = values();
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
