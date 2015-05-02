package ar.com.textillevel.entidades.enums;

import java.util.HashMap;
import java.util.Map;

public enum EDescripcionPagoFactura {

	FACTURA(1, ""),
	SALDO(2, "SALDO"),
	A_CUENTA(3, "A CUENTA");
	
	private Integer id;
	private String descripcion;

	private EDescripcionPagoFactura(Integer id, String descripcion) {
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


	private static Map<Integer, EDescripcionPagoFactura> keyMap;
	
	public static EDescripcionPagoFactura getById(Integer id){
		if(id == null) return null;
		return getKeyMap().get(id);
	}

	private static Map<Integer, EDescripcionPagoFactura> getKeyMap(){
		if(keyMap == null){
			keyMap = new HashMap<Integer, EDescripcionPagoFactura>();
			EDescripcionPagoFactura[] valores = values();
			for(int  i = 0; i<valores.length;i++){
				keyMap.put(valores[i].id,valores[i]);
			}
		}
		return keyMap;
	}

}
