package ar.com.textillevel.gui.modulos.odt.impresion;

public enum EFormaImpresionODT {
	ENCABEZADO ("Solo encabezado"),
	ENCABEZADO_PROCEDIMIENTO ("Secuencia de trabajo"),
	ENCABEZADO_SECUENCIA ("Instrucciones"),
	ESTAMPADO("Solo estampado"),
	RESUMEN_ARTIULOS("Resumen por artículo"),
	AMBOS("Secuencia + Instrucciones");
	
	private EFormaImpresionODT (String descripcion){
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
		return descripcion.toUpperCase();
	}
}
