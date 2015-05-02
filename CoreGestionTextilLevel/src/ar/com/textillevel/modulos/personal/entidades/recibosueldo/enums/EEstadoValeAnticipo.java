package ar.com.textillevel.modulos.personal.entidades.recibosueldo.enums;

import java.util.HashMap;
import java.util.Map;

public enum EEstadoValeAnticipo {

	A_DESCONTAR(1, "A DESCONTAR"), 
	DESCONTADO(2, "DESCONTADO");

	private EEstadoValeAnticipo(Integer id, String descripcion) {
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
	
	private static Map<Integer, EEstadoValeAnticipo> mapKeyValues;
	
	private static Map<Integer, EEstadoValeAnticipo> getKeyMap(){
		if(mapKeyValues == null){
			mapKeyValues = new HashMap<Integer, EEstadoValeAnticipo>();
			for(EEstadoValeAnticipo e : values()){
				mapKeyValues.put(e.getId(), e);
			}
		}
		return mapKeyValues;
	}
	
	public static EEstadoValeAnticipo getById(Integer id){
		return getKeyMap().get(id);
	}
	
	@Override
	public String toString(){
		return descripcion;
	}
}
