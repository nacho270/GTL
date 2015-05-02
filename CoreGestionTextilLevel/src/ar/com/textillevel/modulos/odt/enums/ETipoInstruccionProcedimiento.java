package ar.com.textillevel.modulos.odt.enums;

public enum ETipoInstruccionProcedimiento {

	PASADA("PASADA"), 
	TEXTO("TEXTO"), 
	TIPO_PRODUCTO("TIPO DE PRODUCTO");

	private ETipoInstruccionProcedimiento(String descripion) {
		this.descripcion = descripion;
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
