package ar.com.textillevel.entidades.enums;

public enum ETipoBusquedaAgenda {

	TODOS(1, "Todos"),
	CLIENTE(2, "Cliente"), 
	PROVEEDOR(3, "Proveedor"),
	PERSONA(4, "Persona");

	private int id;
	private String criterio;

	private ETipoBusquedaAgenda(int id, String criterio) {
		this.id = id;
		this.criterio = criterio;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCriterio() {
		return criterio;
	}

	public void setCriterio(String criterio) {
		this.criterio = criterio;
	}
	
	public String toString(){
		return this.criterio;
	}
}
