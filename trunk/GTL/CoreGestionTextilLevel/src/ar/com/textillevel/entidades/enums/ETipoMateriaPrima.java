package ar.com.textillevel.entidades.enums;

import java.util.HashMap;
import java.util.Map;

public enum ETipoMateriaPrima {

	ANILINA(1, "Anilina"), 
	MATERIAL_CONSTRUCCION(2, "Material de construcción"), 
	PIGMENTO(3, "Pigmento"), 
	QUIMICO(4, "Quimico"),
	TELA(5, "Tela"),
	CILINDRO(6,"Cilindro"),
	CABEZAL(7,"Cabezal"),
	VARIOS(7,"Varios"),
	IBC(9,"IBC"),
	REACTIVO(10, "Reactivo")
	;

	private Integer id;
	private String descripcion;

	private ETipoMateriaPrima(Integer id, String descripcion) {
		this.id = id;
		this.descripcion = descripcion;
	}

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
	
	public static ETipoMateriaPrima getByDescripcion(String descripcion) {
		if (descripcion == null) return null;
		return getDescripcionMap().get(descripcion);
	}
	
	private static Map<String, ETipoMateriaPrima> descricpcionMap;
	
	private static Map<String, ETipoMateriaPrima> getDescripcionMap() {
		if (descricpcionMap == null) {
			descricpcionMap = new HashMap<String, ETipoMateriaPrima>();
			ETipoMateriaPrima values[] = values();
			for (int i = 0; i < values.length; i++) {
				descricpcionMap.put(values[i].getDescripcion(), values[i]);
			}
		}
		return descricpcionMap;
	}
}
