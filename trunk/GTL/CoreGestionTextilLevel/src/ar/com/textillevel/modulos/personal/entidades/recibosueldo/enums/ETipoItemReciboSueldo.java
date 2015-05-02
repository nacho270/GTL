package ar.com.textillevel.modulos.personal.entidades.recibosueldo.enums;

import java.util.HashMap;
import java.util.Map;

public enum ETipoItemReciboSueldo {

	SUELDO_BASICO(1, "SUELDO BASICO"),
	HORAS_EXTRAS_AL_50(2, "HORAS TRABAJADAS AL 50%"),
	HORAS_EXTRAS_AL_100(3, "HORAS TRABAJADAS AL 100%"),
	ANTIGUEDAD(4, "ANTIGUEDAD"),
	CONCEPTO_HABER(5, "CONCEPTO HABER"),
	CONCEPTO_DEBE(6, "CONCEPTO DEBE"),
	PREMIO_PRESENTISMO(7, "PREMIO ASISTENCIA"),
	ASIGNACION_NO_REMUNERATIVA_SIMPLE(8, "ASIGNACION NO REMUNERATIVA"),
	VALE_ANTICIPO(9, "ANTICIPO DE SUELDO"),
	FERIADO(10, "FERIADO"),
	ASIGNACION_NO_REMUNERATIVA_PORC_SUELDO_BRUTO(11, "ASIGNACION NO REMUNERATIVA PORCENTAJE SUELDO BRUTO"),
	;

	private Integer id;
	private String nombre;

	private ETipoItemReciboSueldo(Integer id, String nombre) {
		this.id = id;
		this.nombre = nombre;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	private static Map<Integer, ETipoItemReciboSueldo> keyMap;
	
	public static ETipoItemReciboSueldo getById(Integer id){
		if(id == null) return null;
		return getKeyMap().get(id);
	}
	
	private static Map<Integer, ETipoItemReciboSueldo> getKeyMap(){
		if(keyMap == null){
			keyMap = new HashMap<Integer, ETipoItemReciboSueldo>();
			ETipoItemReciboSueldo[] valores = values();
			for(int  i = 0; i<valores.length;i++){
				keyMap.put(valores[i].id,valores[i]);
			}
		}
		return keyMap;
	}

}