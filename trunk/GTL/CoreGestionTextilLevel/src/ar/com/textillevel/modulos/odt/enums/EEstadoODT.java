package ar.com.textillevel.modulos.odt.enums;

import java.util.HashMap;
import java.util.Map;

public enum EEstadoODT {
	ANTERIOR			( 0, "Anterior"), 				//usado para las ODTs viejas que no se van a procesar desde la implementacion
	PENDIENTE			( 1, "Pendiente"),				//Nuevo estado inicial
	COMPLETA			( 2, "Completa"),				//Estado con secuencia completada
	IMPRESA				( 3, "Impresa"),				//Ya se imprimio
	EN_PROCESO			( 4, "En proceso"), 			//Esta en una maquina
	DETENIDA			( 5, "Detenida"),				//Ya arrancó, pero se detuvo
	EN_OFICINA		  	( 6, "En oficina"),				//Ya se terminó
	FACTURADA			( 7, "Facturada");				//Ya se facturó

	private EEstadoODT(Integer id, String descripcion) {
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
	
	public static EEstadoODT getById(Integer id) {
		if (id == null) return null;
		return getKeyMap().get(id);
	}
	
	private static Map<Integer, EEstadoODT> keyMap;
	
	private static Map<Integer, EEstadoODT> getKeyMap() {
		if (keyMap == null) {
			keyMap = new HashMap<Integer, EEstadoODT>();
			EEstadoODT values[] = values();
			for (int i = 0; i < values.length; i++) {
				keyMap.put(values[i].getId(), values[i]);
			}
		}
		return keyMap;
	}
	
	@Override
	public String toString(){
		return descripcion;
	}
}
