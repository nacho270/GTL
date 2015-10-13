package ar.com.fwcommon.util;

/**
 * Interface, declarada para desacoplar el manejo de las distintas impresoras disponibles 
 * que puedo utilizar en los sistemas.
 *  
 * 
 */
public interface ListPrinterMode {
	
	/**
	 * El valor para la propiedad PropiedadNombre.
	 * @return
	 */
	String getPropiedadNombre();

	/**
	 * Obtiene El valor para la propiedad propiedadId.
	 * @return 
	 */
	String getPropiedadId();

	/**
	 * El valor para la propiedad PropiedadMatrizPuntos.
	 * @return
	 */
	String getPropiedadMatrizPuntos();

	/**
	 *  El titulo para el dialogo de la impresora.
	 * @return 
	 */
	String getTituloDialogo();
	
	/**
	 * Devuelve el nombre del tipo de impresora actual.
	 * @return
	 */
	String getNombreTipoImpresora();
		
}
