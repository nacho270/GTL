package ar.com.textillevel.modulos.personal.entidades.fichadas.antifichadas.enums;

import java.util.HashMap;
import java.util.Map;

public enum EMotivoAntifichada {
	
	/* ESTOS SON LOS QUE SE ME OCURRIERON, HABRIA QUE PONER O SACAR SEGUN CORRESPONDA...
	 * POR EJEMPLO, VER QUE ONDA CUANDO HAY UN VELORIO O DIA DE ESTUDIO (QUIZAS NO APLIQUE AL SISTEMA, 
	 * PERO HABRIA QUE TENERLO EN CUENTA) */
	
	LICENCIA_MATERNIDAD(1, "Licencia por maternidad"), 
	ENFERMEDAD(2, "Enfermedad"), 
	VACACIONES(3, "Vacaciones"), 
	LICENCIA_PATERNIDAD(4, "Licencia por paternidad"),
	SUSPENSION(5,"Suspención");

	private EMotivoAntifichada(Integer id, String descripcion) {
		this.setId(id);
		this.setDescripcion(descripcion);
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

	private static Map<Integer, EMotivoAntifichada> keyMap;

	public static EMotivoAntifichada getById(Integer id) {
		if (id == null)
			return null;
		return getKeyMap().get(id);
	}

	private static Map<Integer, EMotivoAntifichada> getKeyMap() {
		if (keyMap == null) {
			keyMap = new HashMap<Integer, EMotivoAntifichada>();
			EMotivoAntifichada[] valores = values();
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
