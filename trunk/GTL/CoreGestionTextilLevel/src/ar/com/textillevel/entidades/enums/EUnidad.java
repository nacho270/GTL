package ar.com.textillevel.entidades.enums;

import java.util.HashMap;
import java.util.Map;

public enum EUnidad {
	
	METROS(1, "MTS"), 
	UNIDAD(2, "UD"),
	KILOS(3, "KG"),
	LITROS(4,"LTS"),
	GRAMOS_POR_KILOS(5,"GR/KG"),
	GRAMOS_POR_LITROS(6,"GR/LTS"),
	PORCENTAJE(7,"% (KG)")
	;

	private EUnidad(Integer id, String descripcion) {
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
	
	@Override
	public String toString() {
		return getDescripcion();
	}
	
	private static Map<Integer, EUnidad> keyMap;
	
	public static EUnidad getById(Integer id){
		if(id == null) return null;
		return getKeyMap().get(id);
	}
	
	private static Map<Integer, EUnidad> getKeyMap(){
		if(keyMap == null){
			keyMap = new HashMap<Integer, EUnidad>();
			EUnidad[] valores = values();
			for(int  i = 0; i<valores.length;i++){
				keyMap.put(valores[i].id,valores[i]);
			}
		}
		return keyMap;
	}
}