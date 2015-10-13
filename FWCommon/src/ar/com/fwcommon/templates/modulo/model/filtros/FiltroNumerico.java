package ar.com.fwcommon.templates.modulo.model.filtros;

/**
 * Filtros en los que el usuario debe ingresar un número para el filtrado
 * 
 * 
 *
 * @param <T> Tipo de elementos a filtrar
 */
public abstract class FiltroNumerico<T> extends Filtro<T, Long> {
	private Long maximo;
	private Long minimo;
	
	public FiltroNumerico(String nombre) {
		super(nombre);
	}

	@Override
	protected Long getDefaultValue() {
		return null;
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
