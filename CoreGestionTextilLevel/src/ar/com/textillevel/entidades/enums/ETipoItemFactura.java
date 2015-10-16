package ar.com.textillevel.entidades.enums;

public enum ETipoItemFactura {
	PRODUCTO("PRODUCTO"), 
	BONIFICACION("BONIFICACION"), 
	RECARGO("RECARGO"), 
	TUBOS("TUBOS"), 
	SEGURO("SEGURO"), 
	PERCEPCION("PERCEPCION"),
	STOCK("STOCK"),
	TELA_CRUDA("TELA CRUDA"),
	OTRO("OTRO"),
	CORRECCION_FACTURA("CORRECCION");

	private ETipoItemFactura(String descripcion) {
		this.descripcion = descripcion;
	}

	private String descripcion;

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	@Override
	public String toString(){
		return descripcion;
	}
}
