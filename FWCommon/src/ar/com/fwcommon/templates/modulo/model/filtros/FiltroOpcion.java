package ar.com.fwcommon.templates.modulo.model.filtros;

/**
 * Filtro del tipo SI/NO
 * 
 * 
 * 
 *
 * @param <T> Tipo de elementos a filtrar
 */
public abstract class FiltroOpcion<T> extends Filtro<T, Boolean> {
	private Long maximo;
	private Long minimo;
	
	public FiltroOpcion(String nombre) {
		super(nombre);
	}

	/* (non-Javadoc)
	 * ar.com.fwcommon.templates.modulo.model.filtros.Filtro#getDefaultValue()
	 */
	@Override
	protected Boolean getDefaultValue() {
		return false;
	}
	public Long getMaximo() {
		return maximo;
	}

	public void setMaximo(Long maximo) {
		this.maximo = maximo;
	}

	public Long getMinimo() {
		return minimo;
	}

	public void setMinimo(Long minimo) {
		this.minimo = minimo;
	}
}
