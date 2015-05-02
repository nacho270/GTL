package ar.com.textillevel.entidades.enums;

import java.util.HashMap;
import java.util.Map;

public enum ETipoImpuesto {

	IVA(1, "IVA"), 
	INGRESOS_BRUTOS(2, "IIBB"),
	EXENTO(3, "EXENTO");

	private int id;
	private String descripcion;

	private ETipoImpuesto(int id, String descripcion) {
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

	private static Map<Integer, ETipoImpuesto> keyMap;
	
	public static ETipoImpuesto getById(Integer id){
		if(id == null) return null;
		return getKeyMap().get(id);
	}
	
	private static Map<Integer, ETipoImpuesto> getKeyMap(){
		if(keyMap == null){
			keyMap = new HashMap<Integer, ETipoImpuesto>();
			ETipoImpuesto[] valores = values();
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