package ar.com.textillevel.entidades.enums;

import java.util.HashMap;
import java.util.Map;

public enum EEstadoCheque {

	PENDIENTE_COBRAR	( 1, "Pendiente"), 
	EN_CARTERA			( 2, "En cartera"), 
	RECHAZADO			( 3, "Rechazado"),
	UTILIZADO			( 4, "Utilizado"),
	RECHAZADO_CARTERA	( 5, "Rechazado/En cartera"),
	RECHAZADO_DEVUELTO	( 6, "Rechazado/Devuelto"),
	VENCIDO				( 7, "Vencido"),
	SALIDA_BANCO		( 8, "Banco"),
	SALIDA_PROVEEDOR	( 9, "Proveedor"),
	SALIDA_CLIENTE		( 10, "Cliente"),
	SALIDA_PERSONA		( 11, "Persona");

	private EEstadoCheque(Integer id, String descripcion) {
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
	
	public static EEstadoCheque getById(Integer id) {
		if (id == null) return null;
		return getKeyMap().get(id);
	}
	
	private static Map<Integer, EEstadoCheque> keyMap;
	
	private static Map<Integer, EEstadoCheque> getKeyMap() {
		if (keyMap == null) {
			keyMap = new HashMap<Integer, EEstadoCheque>();
			EEstadoCheque values[] = values();
			for (int i = 0; i < values.length; i++) {
				keyMap.put(values[i].getId(), values[i]);
			}
		}
		return keyMap;
	}
	
	@Override
	public String toString() {
		return getDescripcion();
	}
}
