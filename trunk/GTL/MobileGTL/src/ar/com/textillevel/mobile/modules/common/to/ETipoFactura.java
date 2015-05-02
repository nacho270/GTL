package ar.com.textillevel.mobile.modules.common.to;

import java.util.HashMap;
import java.util.Map;

public enum ETipoFactura {
	
	A(1, "A"), 
	B(2, "B"),
	C(3, "C"), 
	X(4, "X"),
	E(5, "E");

	private ETipoFactura(Integer id, String descripcion) {
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
	
	private static Map<Integer, ETipoFactura> keyMap;
	
	public static ETipoFactura getById(Integer id){
		if(id == null) return null;
		return getKeyMap().get(id);
	}
	
	private static Map<Integer, ETipoFactura> getKeyMap(){
		if(keyMap == null){
			keyMap = new HashMap<Integer, ETipoFactura>();
			ETipoFactura[] valores = values();
			for(int  i = 0; i<valores.length;i++){
				keyMap.put(valores[i].id,valores[i]);
			}
		}
		return keyMap;
	}
}
