package ar.com.textillevel.modulos.personal.entidades.fichadas.enums;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import ar.com.textillevel.modulos.personal.entidades.fichadas.antifichadas.enums.EMotivoAntifichada;


public enum EEstadoDiaFichada {
	NORMAL(1, "Normal", Color.WHITE), 
	TARDE(2, "Tarde",Color.BLUE.brighter()),
	FALTA(3,"Falta",Color.DARK_GRAY),
	RETIRO_TEMPRANO(4,"Retiro temprano", Color.ORANGE),
	LICENCIA(5,"Licencia",Color.CYAN.darker()),
	FERIADO(6,"Feriado",Color.GREEN.darker()),
	DOMINGO(7,"Domingo",Color.MAGENTA),
	TEMPRANO_TARDE(8,"Temprano y tarde",Color.PINK.darker()),
	INCONSISTENTE(9,"Inconsistente", Color.PINK),
	TARDE_TEMPRANO(10,"Tarde y temprano",Color.GRAY),
	ENFERMEDAD(11,"Enfermedad",null),
	SUSPENSION(12,"Suspensión",null),
	VACACIONES(13,"Vacaciones",null);

	private EEstadoDiaFichada(Integer id, String descripcion, Color color) {
		this.setId(id);
		this.setDescripcion(descripcion);
		this.setColor(color);
	}

	private Integer id;
	private String descripcion;
	private Color color;

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

	private static Map<Integer, EEstadoDiaFichada> keyMap;

	public static EEstadoDiaFichada getById(Integer id) {
		if (id == null)
			return null;
		return getKeyMap().get(id);
	}

	private static Map<Integer, EEstadoDiaFichada> getKeyMap() {
		if (keyMap == null) {
			keyMap = new HashMap<Integer, EEstadoDiaFichada>();
			EEstadoDiaFichada[] valores = values();
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

	public static EEstadoDiaFichada getByMotivoAntiFichada(EMotivoAntifichada motivo) {
		switch (motivo) {
			case ENFERMEDAD: {
				return LICENCIA;
			}
			case LICENCIA_MATERNIDAD: {
				return LICENCIA;
			}
			case LICENCIA_PATERNIDAD: {
				return LICENCIA;
			}
			case VACACIONES: {
				return VACACIONES;
			}
			case SUSPENSION: {
				return SUSPENSION;
			}
		}
		return null;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
}
