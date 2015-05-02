package ar.com.textillevel.entidades.to.ivacompras;

import java.util.HashMap;
import java.util.Map;

public enum ETipoDocumentoProveedor {

	FACTURA("FACT", "Factura"), 
	NOTA_DE_CREDITO("NC", "N. de Crédito"),
	NOTA_DE_DEBITO("ND", "N. de Débito"); 

	private String idStr;
	private String descripcion;

	private ETipoDocumentoProveedor(String id, String descripcion) {
		this.idStr = id;
		this.descripcion = descripcion;
	}

	public String getId() {
		return idStr;
	}

	public void setId(String idStr) {
		this.idStr = idStr;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	@Override
	public String toString(){
		return descripcion.toUpperCase();
	}

	public static ETipoDocumentoProveedor getByIdStr(String descripcion) {
		if (descripcion == null) return null;
		return getDescripcionMap().get(descripcion);
	}
	
	private static Map<String, ETipoDocumentoProveedor> descricpcionMap;
	
	private static Map<String, ETipoDocumentoProveedor> getDescripcionMap() {
		if (descricpcionMap == null) {
			descricpcionMap = new HashMap<String, ETipoDocumentoProveedor>();
			ETipoDocumentoProveedor values[] = values();
			for (int i = 0; i < values.length; i++) {
				descricpcionMap.put(values[i].getId(), values[i]);
			}
		}
		return descricpcionMap;
	}

}
