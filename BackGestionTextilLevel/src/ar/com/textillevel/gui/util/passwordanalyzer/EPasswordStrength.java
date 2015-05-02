package ar.com.textillevel.gui.util.passwordanalyzer;

import java.awt.Color;

public enum EPasswordStrength {

	DEBIL	(1, "Debil", Color.RED),
	MEDIO	(2, "Medio", Color.YELLOW.darker()), 
	FUERTE	(3,"Fuerte", Color.GREEN.darker());

	private EPasswordStrength(Integer id, String descripcion, Color color) {
		this.id = id;
		this.descripcion = descripcion;
		this.color = color;
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

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
}
