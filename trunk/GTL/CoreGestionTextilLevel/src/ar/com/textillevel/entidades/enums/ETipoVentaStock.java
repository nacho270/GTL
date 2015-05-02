package ar.com.textillevel.entidades.enums;

public enum ETipoVentaStock {
	TELA("Tela"), 
	CILINDRO("Cilindro"),
	CABEZAL("Cabezal");

	private ETipoVentaStock(String descripcion) {
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
