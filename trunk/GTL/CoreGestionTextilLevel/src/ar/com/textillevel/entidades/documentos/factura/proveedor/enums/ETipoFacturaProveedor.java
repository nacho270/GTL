package ar.com.textillevel.entidades.documentos.factura.proveedor.enums;

import java.util.HashMap;
import java.util.Map;

public enum ETipoFacturaProveedor {

	NORMAL							(1,"Normal"), 
	SIN_REMITO						(2,"Sin remito"), 
	SERVICIO						(3,"Servicio");

	private ETipoFacturaProveedor(Integer id, String descripcion) {
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
	
	public static ETipoFacturaProveedor getById(Integer id) {
		if (id == null) return null;
		return getKeyMap().get(id);
	}
	
	private static Map<Integer, ETipoFacturaProveedor> keyMap;
	
	private static Map<Integer, ETipoFacturaProveedor> getKeyMap() {
		if (keyMap == null) {
			keyMap = new HashMap<Integer, ETipoFacturaProveedor>();
			ETipoFacturaProveedor values[] = values();
			for (int i = 0; i < values.length; i++) {
				keyMap.put(values[i].getId(), values[i]);
			}
		}
		return keyMap;
	}

	@Override
	public String toString() {
		return descripcion;
	}
}