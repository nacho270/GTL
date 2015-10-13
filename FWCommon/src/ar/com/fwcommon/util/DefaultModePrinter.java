package ar.com.fwcommon.util;

/**
 * Crea una impresora por defecto. Al utilizar una impresora declarada se setean Systems properties, correspondientes al modo 
 * seleccionado.   
 * 
 *
 */
public class DefaultModePrinter implements ListPrinterMode {

	/**
	 * El valor para la propiedad printerMode : Selecciona una impresora normal. 
	 * 
	 * #setPrinterMode
	 */
	public static final int PRINTER_NOMAL = 0;

	/**
	 * El valor para la propiedad printerMode : Selecciona una impresora para la papeleta.
	 * 
	 * #setPrinterMode
	 */
	public static final int PRINTER_PAPELETA = 1;

	/**
	 * El valor para la propiedad printerMode : Selecciona una impresora para trabajar con Facturación.  
	 * 
	 * #setPrinterMode
	 */
	public static final int PRINTER_FACTURACION = 2;

	/**
	 * El modo de impresión actual.
	 */
	private int printerMode;

	/**
	 * Crea una impresora por defecto. La impresora utilizada es <code>PRINTER_NOMAL</code> 
	 *
	 */
	public DefaultModePrinter() {
		this(PRINTER_NOMAL);
	}

	/**
	 * Crea una impresora. La impresora utilizada es <code>printMode</code> 
	 *
	 */
	public DefaultModePrinter(int printMode) {
		super();
		setPrinterMode(printMode);
	}

	/**
	 * Setea el valor del modo de Impresora actual. Puede usarse
	 * cualquiera de estos valores declarados:
	 * <ul>
	 * <li> <code>PRINTER_NOMAL</code> 
	 * cualquier impresora disponible.
	 * <li> <code>PRINTER_PAPELETA</code>
	 * cualquier impresora para imprimir etiquetas ej: "Zebra".
	 * <li> <code>PRINTER_FACTURACION</code>
	 * cualquier impresora que emita una factura.
	 * </ul>
	 * 
	 * #getPrinterMode
	 */
	public void setPrinterMode(int printerMode) {
		switch (printerMode) {
		case PRINTER_NOMAL:
		case PRINTER_FACTURACION:
		case PRINTER_PAPELETA:
			this.printerMode = printerMode;
			break;
		default:
			throw new IllegalArgumentException("Es inválido el modo de impresión.");
		}			
	}

	/**
	 * Obtiene El valor para la propiedad printerMode.
	 * @return
	 * 
	 * #setPrinterMode
	 */
	public int getPrinterMode() {
		return printerMode;
	}

	/**
	 * Obtiene El valor para la propiedad propiedadId.
	 */
	public String getPropiedadId() {

		if (getPrinterMode() == PRINTER_NOMAL) {
			return "idImpresoraNormal";
		} else if (getPrinterMode() == PRINTER_FACTURACION ) {
			return "idImpresoraFactura";
		} else if (getPrinterMode() == PRINTER_PAPELETA ) {
			return "idImpresoraPapeleta";
		}		
		return null;
	}
	
	

	/**
	 * El valor para la propiedad PropiedadMatrizPuntos.
	 */
	public String getPropiedadMatrizPuntos() {
		if (getPrinterMode() == PRINTER_NOMAL) {
			return "ImpresoraMatrizPunto";
		} else if (getPrinterMode() == PRINTER_FACTURACION ) {
			return "ImpresoraMatrizPuntoFactura";
		} else if (getPrinterMode() == PRINTER_PAPELETA ) {
			return "idImpresoraPapeletaPapeleta";
		}		
		return null;		
	}
	
	
	/**
	 * El valor para la propiedad PropiedadNombre.
	 */
	public String getPropiedadNombre() {
		if (getPrinterMode() == PRINTER_NOMAL) {
			return "NombreImpresoraNormal";
		} else if (getPrinterMode() == PRINTER_FACTURACION ) {
			return "NombreImpresoraFactura";
		} else if (getPrinterMode() == PRINTER_PAPELETA ) {
			return "NombreImpresoraPapeleta";
		}		
		return null;
	}
	
	
	/**
	 * El titulo para el dialogo de la impresora.
	 */
	public String getTituloDialogo() {

		if (getPrinterMode() == PRINTER_NOMAL) {
			return "Selección de Impresora Normal";
		} else if (getPrinterMode() == PRINTER_FACTURACION ) {
			return "Selección de Impresora Facturación";
		} else if (getPrinterMode() == PRINTER_PAPELETA ) {
			return "Selección de Impresora Papeleta";
		}		
		return "Impresora No Asignada";		
	}

	public String getNombreTipoImpresora() {
		if (getPrinterMode() == PRINTER_NOMAL) {
			return "Normal";
		} else if (getPrinterMode() == PRINTER_FACTURACION ) {
			return "Factura";
		} else if (getPrinterMode() == PRINTER_PAPELETA ) {
			return "Papeleta";
		}		
		return null;
	}

}