package ar.com.textillevel.modulos.personal.entidades.recibosueldo.enums;

import java.util.HashMap;
import java.util.Map;

public enum ETipoAnotacionReciboSueldo {

	AUSENCIA(1,"AUSENCIA", "A"),
	LICENCIA_POR_ENFERMEDAD(2,"LICENCIA POR ENFERMEDAD", "L"),
	VACACIONES(3,"VACACIONES", "VAC");

	private Integer id;
	private String descripcion;
	private String abreviacion;

	private ETipoAnotacionReciboSueldo(Integer id, String descripcion, String abreviacion) {
		this.id = id;
		this.descripcion = descripcion;
		this.abreviacion = abreviacion;
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

	public String getAbreviacion() {
		return abreviacion;
	}

	public void setAbreviacion(String abreviacion) {
		this.abreviacion = abreviacion;
	}
	
	private static Map<Integer, ETipoAnotacionReciboSueldo> keyMap;

	public static ETipoAnotacionReciboSueldo getById(Integer id) {
		if (id == null)
			return null;
		return getKeyMap().get(id);
	}

	private static Map<Integer, ETipoAnotacionReciboSueldo> getKeyMap() {
		if (keyMap == null) {
			keyMap = new HashMap<Integer, ETipoAnotacionReciboSueldo>();
			ETipoAnotacionReciboSueldo[] valores = values();
			for (int i = 0; i < valores.length; i++) {
				keyMap.put(valores[i].id, valores[i]);
			}
		}
		return keyMap;
	}

	@Override
	public String toString() {
		return descripcion;
	}

}