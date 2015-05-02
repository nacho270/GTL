package ar.com.textillevel.modulos.personal.enums;

public enum ETipoBusquedaEmpleados {
	ACTIVOS(1, "ACTIVOS"), 
	INACTIVOS(2, "INACTIVOS"),
	TODOS(3, "TODOS");

	private int id;
	private String descripcion;

	private ETipoBusquedaEmpleados(int id, String descripcion) {
		this.id = id;
		this.descripcion = descripcion;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	@Override
	public String toString() {
		return descripcion;
	}
}
