package ar.com.fwcommon.templates.modulo.model.filtros;

/**
 * Filtros en los que el usuario debe ingresar un texto para el filtrado
 * 
 * 
 * 
 *
 * @param <T> Tipo de elementos a filtrar
 */
public abstract class FiltroTexto<T> extends Filtro<T, String> {
	private int longitud = Integer.MAX_VALUE;
	
	public FiltroTexto(String nombre) {
		super(nombre);
	}

	/**
	 * Devuelve la longitud máxima que se va a permitir ingresar
	 * @return Longitud máxima que se va a permitir ingresar
	 */
	public int getLongitud() {
		return longitud;
	}

	/**
	 * Establece la longitud máxima que se va a permitir ingresar
	 * @param longitud Longitud máxima que se va a permitir ingresar
	 */
	protected void setLongitud(int longitud) {
		this.longitud = longitud;
	}

	/**
	 * Devuelve el String vacio como valor por defecto
	 * @return String vacio
	 */
	@Override
	protected String getDefaultValue() {
		return "";
	}
}
