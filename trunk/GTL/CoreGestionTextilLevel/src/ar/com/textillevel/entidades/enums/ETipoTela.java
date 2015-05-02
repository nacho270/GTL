package ar.com.textillevel.entidades.enums;

public enum ETipoTela {
	
	CRUDA("Cruda"), 
	TERMINADA("Terminada");

	private ETipoTela(String descripcion) {
		this.descripcion = descripcion;
	}

	private String descripcion;

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
}
