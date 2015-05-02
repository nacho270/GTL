package ar.com.textillevel.modulos.personal.entidades.recibosueldo.asignaciones.enums;

import java.util.HashMap;
import java.util.Map;

public enum ETipoAsignacion {
	
	SIMPLE(1, "Simple"), 
	PORCE_SUELDO_BRUTO(2, "Porcentaje de Sueldo Bruto");

	private ETipoAsignacion(Integer id, String descripcion) {
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
	public String toString(){
		return descripcion;
	}
	
	private static Map<Integer, ETipoAsignacion> mapValues;;
	
	public static ETipoAsignacion getById(Integer id){
		if(mapValues == null){
			mapValues = new HashMap<Integer, ETipoAsignacion>();
			for(ETipoAsignacion tipo : values()){
				mapValues.put(tipo.getId(), tipo);
			}
		}
		if(id == null){
			return null;
		}
		return mapValues.get(id);
	}
}
