package ar.com.textillevel.entidades.enums;

import java.util.HashMap;
import java.util.Map;

public enum ETipoInformeProduccion {

	METROS(1, "MTS"), 
	KILOS(2, "KG");

	private ETipoInformeProduccion(Integer id, String descripcion) {
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
	
	private static Map<Integer, ETipoInformeProduccion> keyMap;
	
	public static ETipoInformeProduccion getById(Integer id){
		if(id == null) return null;
		return getKeyMap().get(id);
	}
	
	private static Map<Integer, ETipoInformeProduccion> getKeyMap(){
		if(keyMap == null){
			keyMap = new HashMap<Integer, ETipoInformeProduccion>();
			ETipoInformeProduccion[] valores = values();
			for(int  i = 0; i<valores.length;i++){
				keyMap.put(valores[i].id,valores[i]);
			}
		}
		return keyMap;
	}
}
