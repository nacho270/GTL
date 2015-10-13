package ar.com.fwcommon.templates.modulo.model.filtros;

import ar.com.fwcommon.templates.modulo.gui.filtros.RangoFechas;
/**
 * Filtro de Rango de Fechas.
 *  
 * @param <T> Elemento que se desea filtrar
 * @param <RangoFechas> 
 */
public abstract class FiltroRangoFechas<T, E> extends Filtro<T, RangoFechas> {
	private RangoFechas valoresRangoFechas;
	private String nombreLabelDesde;
	private String nombreLabelHasta;
	
	public FiltroRangoFechas(String nombreLabelDesde, String nombreLabelHasta) {
		super();
		this.nombreLabelDesde = nombreLabelDesde;
		this.nombreLabelHasta = nombreLabelHasta;
	}
	
	public void setNombreLabelDesde(String nombreLabelDesde) {
		this.nombreLabelDesde = nombreLabelDesde;
	}
	
	public void setNombreLabelHasta(String nombreLabelHasta) {
		this.nombreLabelHasta = nombreLabelHasta;
	}
	
	public String getNombreLabelDesde() {
		return this.nombreLabelDesde;
	}
	
	public String getNombreLabelHasta() {
		return this.nombreLabelHasta;
	}
	
	/**
	 * Devuelve los valores entre los que podrá seleccionar el usuario
	 * 
	 * @return Rango de valores que podrá seleccionar el usuario
	 */
	public RangoFechas getValoresSeleccionables() {
		if(valoresRangoFechas == null) {
			valoresRangoFechas = new RangoFechas(); 
		}
		return valoresRangoFechas;
	}

	/**
	 * En caso que el filtro tenga valor todos, establece el valor de todos
	 * seleccionado. Sino, el primer elemento de la lista.
	 * 
	 * @return <code>null</code> si la lista no tiene valores o si el valor todos esta habilitado. 
	 */
	@Override
	protected RangoFechas getDefaultValue() {
		return null;
	}
}