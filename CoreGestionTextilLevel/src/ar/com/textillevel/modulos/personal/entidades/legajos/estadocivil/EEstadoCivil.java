package ar.com.textillevel.modulos.personal.entidades.legajos.estadocivil;

import java.util.HashMap;
import java.util.Map;

public enum EEstadoCivil {
	SOLTERO(1, "SOLTERO"), 
	CASADO(2, "CASADO"),
	VIUDO(3, "VIUDO"),
	DIVORCIADO(4, "DIVORCIADO"),
	CONCUBINATO(5, "CONCUBINATO");

	private int id;
	private String descripcion;

	private EEstadoCivil(int id, String descripcion) {
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

	private static Map<Integer, EEstadoCivil> keyMap;
	
	public static EEstadoCivil getById(Integer id){
		if(id == null) return null;
		return getKeyMap().get(id);
	}
	
	private static Map<Integer, EEstadoCivil> getKeyMap(){
		if(keyMap == null){
			keyMap = new HashMap<Integer, EEstadoCivil>();
			EEstadoCivil[] valores = values();
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
