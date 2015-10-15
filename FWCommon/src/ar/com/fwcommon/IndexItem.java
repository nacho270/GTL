package ar.com.fwcommon;

import java.io.Serializable;

/**
 * Clase útil para representar items, ya sea de una árbol (JTree) o de una lista (JList)
 * con número de <b>id</b> y <b>nombre</b>. Puede utilizarse la descripción para mostrar
 * por ejemplo un tooltip del ítem.
 * @version 1.1
 */
public class IndexItem implements Serializable {

	private static final long serialVersionUID = 1L;
	private int id;
	private String nombre;
	private String descripcion;
	private boolean distinguido;
	private int idPadre;

	public IndexItem() {
	}

	/**
	 * Método constructor.
	 * @param id El nro. de id del ítem.
	 * @param nombre El nombre del ítem.
	 */
	public IndexItem(int id, String nombre) {
		this(id, nombre, false);
	}

	/**
	 * Método constructor.
	 * @param id El nro. de id del ítem.
	 * @param nombre El nombre del ítem.
	 * @param distinguido Indica si el ítem se mostrará distinguido.
	 */
	public IndexItem(int id, String nombre, boolean distinguido) {
		this.id = id;
		this.nombre = nombre;
		this.distinguido = distinguido;
	}

	/**
	 * @return Devuelve el id del ítem.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Setea el <b>id</b> del ítem.
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return Devuelve el nombre del ítem.
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Setea el <b>nombre</b> del ítem.
	 * @param nombre
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * @return La descripción del ítem.
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * Setea la <b>descripción</b> al ítem.
	 * @param descripcion
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * @return true si el ítem está distinguido.
	 */
	public boolean isDistinguido() {
		return distinguido;
	}

	/**
	 * Setea al ítem distinguido.
	 * @param distinguido
	 */
	public void setDistinguido(boolean distinguido) {
		this.distinguido = distinguido;
	}

	public int getIdPadre() {
		return idPadre;
	}

	public void setIdPadre(int idPadre) {
		this.idPadre = idPadre;
	}

	/** Método <b>toString</b> */
	public String toString() {
		return nombre;
	}

	/** Método <b>equals</b> */
	public boolean equals(Object obj) {
		if(!(obj instanceof IndexItem))
			return false;
		else {
			return (((IndexItem)obj).getId() == id) &&
					(((IndexItem)obj).getNombre().equals(nombre)) &&
					(((IndexItem)obj).isDistinguido() == distinguido);
		}
	}

	public int hashCode() {
		return id << 3 ^ id >> 1;
	}

}