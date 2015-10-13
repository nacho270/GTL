package ar.com.fwcommon.templates.modulo.model.tabla;

/**
 * Clase que permite agrupar columnas
 * 
 */
public class GrupoColumnasModel<T> {
	private String nombre;
	private Columna<T> columnaDesde;
	private Columna<T> columnaHasta;

	/**
	 * Constructor para crear un grupo de columnas
	 * 
	 * @param nombre valor para el nombre del Grupo.
	 * @param columnaDesde la primer <code>Columna</code> que pertenece al grupo. 
	 * @param columnaHasta la última <code>Columna</code> que pertenece al grupo.
	 */
	public GrupoColumnasModel(String nombre, Columna<T> columnaDesde, Columna<T> columnaHasta) {
		super();
		this.nombre = nombre;
		this.columnaDesde = columnaDesde;
		this.columnaHasta = columnaHasta;
	}

	/**
	 * Devuelve la primer <code>Columna</code> del grupo.
	 * @return la <code>Columna</code> que inicia al grupo.
	 * 
	 * Columna
	 */
	public Columna<T> getColumnaDesde() {
		return columnaDesde;
	}

	/**
	 * Establece la primer <code>Columna</code> del grupo.
	 * @param columnaDesde <code>Columna</code> que inicia al grupo.
	 */
	public void setColumnaDesde(Columna<T> columnaDesde) {
		this.columnaDesde = columnaDesde;
	}

	/**
	 * Devuelve la última <code>Columna</code> del grupo.
	 * @return la última <code>Columna</code> que pertenece al grupo.
	 * 
	 * Columna
	 */
	public Columna<T> getColumnaHasta() {
		return columnaHasta;
	}

	/**
	 * Establece la última <code>Columna</code> del grupo.
	 * @param columnaHasta <code>Columna</code> que finaliza al grupo.
	 */
	public void setColumnaHasta(Columna<T> columnaHasta) {
		this.columnaHasta = columnaHasta;
	}

	/**
	 * Devuelve el nombre del grupo.
	 * @return el valor para el nombre del grupo.
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Establece el nombre del grupo.
	 * @param nombre
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

}