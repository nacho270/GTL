package ar.com.textillevel.modulos.personal.entidades.recibosueldo.conceptos.enums;

import java.util.HashMap;
import java.util.Map;

public enum ETipoConceptoReciboSueldo {
	HABER(1, "Haber"), 
	RETENCION(2, "Retencion");

	private ETipoConceptoReciboSueldo(Integer id, String descripcion) {
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
	
	private static Map<Integer, ETipoConceptoReciboSueldo> mapValues;;
	
	public static ETipoConceptoReciboSueldo getById(Integer id){
		if(mapValues == null){
			mapValues = new HashMap<Integer, ETipoConceptoReciboSueldo>();
			for(ETipoConceptoReciboSueldo tipo : values()){
				mapValues.put(tipo.getId(), tipo);
			}
		}
		if(id == null){
			return null;
		}
		return mapValues.get(id);
	}
}
