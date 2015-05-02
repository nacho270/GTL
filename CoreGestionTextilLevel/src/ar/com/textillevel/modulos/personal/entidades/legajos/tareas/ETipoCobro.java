package ar.com.textillevel.modulos.personal.entidades.legajos.tareas;

import java.util.HashMap;
import java.util.Map;

public enum ETipoCobro {

	MENSUAL(1, "MENSUAL"), 
	QUINCENAL(2, "QUINCENAL");

	private int id;
	private String descripcion;

	private ETipoCobro(int id, String descripcion) {
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

	private static Map<Integer, ETipoCobro> keyMap;
	
	public static ETipoCobro getById(Integer id){
		if(id == null) return null;
		return getKeyMap().get(id);
	}
	
	private static Map<Integer, ETipoCobro> getKeyMap(){
		if(keyMap == null){
			keyMap = new HashMap<Integer, ETipoCobro>();
			ETipoCobro[] valores = values();
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
