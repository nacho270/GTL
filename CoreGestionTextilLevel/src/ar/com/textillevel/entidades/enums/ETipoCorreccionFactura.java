package ar.com.textillevel.entidades.enums;

import java.util.HashMap;
import java.util.Map;

public enum ETipoCorreccionFactura {
	NOTA_CREDITO	( 1, "Nota de crédito","NC"), 
	NOTA_DEBITO		( 2, "Nota de débito","ND");

	private ETipoCorreccionFactura(Integer id, String descripcion, String descripcionResumida) {
		this.id = id;
		this.descripcion = descripcion;
		this.descripcionResumida = descripcionResumida;
	}

	private Integer id;
	private String descripcion;
	private String descripcionResumida;

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
	
	public static ETipoCorreccionFactura getById(Integer id) {
		if (id == null) return null;
		return getKeyMap().get(id);
	}
	
	private static Map<Integer, ETipoCorreccionFactura> keyMap;
	
	private static Map<Integer, ETipoCorreccionFactura> getKeyMap() {
		if (keyMap == null) {
			keyMap = new HashMap<Integer, ETipoCorreccionFactura>();
			ETipoCorreccionFactura values[] = values();
			for (int i = 0; i < values.length; i++) {
				keyMap.put(values[i].getId(), values[i]);
			}
		}
		return keyMap;
	}
	
	public static ETipoCorreccionFactura getByDescripcion(String descripcion) {
		if (descripcion == null) return null;
		return getDescripcionMap().get(descripcion);
	}
	
	private static Map<String, ETipoCorreccionFactura> descricpcionMap;
	
	private static Map<String, ETipoCorreccionFactura> getDescripcionMap() {
		if (descricpcionMap == null) {
			descricpcionMap = new HashMap<String, ETipoCorreccionFactura>();
			ETipoCorreccionFactura values[] = values();
			for (int i = 0; i < values.length; i++) {
				descricpcionMap.put(values[i].getDescripcion(), values[i]);
			}
		}
		return descricpcionMap;
	}

	
	public String getDescripcionResumida() {
		return descripcionResumida;
	}

	
	public void setDescripcionResumida(String descripcionResumida) {
		this.descripcionResumida = descripcionResumida;
	}
}
