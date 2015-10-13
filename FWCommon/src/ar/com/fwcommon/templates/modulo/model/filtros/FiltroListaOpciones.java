package ar.com.fwcommon.templates.modulo.model.filtros;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ar.com.fwcommon.util.StringUtil;

/**
 * Filtro de una lista de única selección.
 *  
 * 
 *
 * @param <T> Elemento que se desea filtrar
 * @param <E> Elementos que se colocarán en la lista
 */
public abstract class FiltroListaOpciones<T, E> extends Filtro<T, E> {

	private List<E> valoresSeleccionables;
	private boolean todosSelected = false;
	private boolean todosOption = false;
	protected String stringTODOS = StringUtil.TODOS;

	public FiltroListaOpciones(String nombre) {
		super(nombre);
	}

	/**
	 * Devuelve los valores entre los que podrá seleccionar el usuario
	 * 
	 * @return Lista inmutable de valores que podrá seleccionar el usuario
	 */
	public List<E> getValoresSeleccionables() {
		if(valoresSeleccionables == null) {
			valoresSeleccionables = Collections.emptyList();
		}
		return valoresSeleccionables;
	}

	/**
	 * Establece los valores entre los que podrá seleccionar el usuario
	 * 
	 * @param Lista de valores que podrá seleccionar el usuario
	 */
	public void setValoresLista(List<? extends E> valores) {
		this.valoresSeleccionables = new ArrayList<E>();
		this.valoresSeleccionables.addAll(valores);
		this.valoresSeleccionables = Collections.unmodifiableList(this.valoresSeleccionables);
		fireModelChangeListener(EVENT_TYPE_VALUES_CHANGE);
	}
	
	/**
	 * Dice si debe haber una opción especial que sea &quot;Todos&quot; entre
	 * los posibles elementos a seleccionar
	 * 
	 * @return <code>TRUE</code> si hay una opción especial, <code>FALSE</code> en caso contrario
	 */
	public boolean hasTodosOption() {
		return todosOption;
	}

	/**
	 * Establece si entre los valores seleccioables debe haber una opción
	 * especial que sea &quot;Todos&quot
	 * 
	 * @param todosOption <code>TRUE</code> si hay una opción especial,
	 *            <code>FALSE</code> en caso contrario
	 */
	public void setTodosOption(boolean todosOption) {
		if (this.todosOption != todosOption) {
			this.todosOption = todosOption;
			fireModelChangeListener(EVENT_TYPE_VALUES_CHANGE);
		}
	}

	/**
	 * Dice si se ha seleccionado el valor especial &quot;Todos&quot;. <br>
	 * Es importante destacar, que en caso de que el valor especial
	 * &quot;Todos&quot; se encuentre seleccioando, la función
	 * {@link #getValue()} devolverá <code>NULL</code>
	 * 
	 * @return <code>TRUE</code> si la opción especial &quot;Todos&quot; se
	 *         encuentra seleccionada y <code>FALSE</code> en caso contrario
	 */
	public boolean isTodosSelected() {
		return todosSelected;
	}

	/**
	 * Establece si el valor seleccionado por el usuario en el filtro es el valor especial &quot;Todos&quot;
	 * 
	 * @param todosSelected <code>TRUE</code> si el valor seleccionado por el
	 *            usuario es &quot;Todos&quot;. <code>FALSE</code> en caso
	 *            contrario
	 */
	public final void setTodosSelected(boolean todosSelected) {
		setInternalTodosSelected(todosSelected);
		setValue(null);	//Esto ya dispara el evento
	}
	
	/**
	 * Establece si el valor seleccionado por el usuario en el filtro es el valor especial &quot;Todos&quot;<br>
	 * <b><u>IMPORTANTE:</u> Esta función es pública solo por una cuestión de
	 * implementación, pero no debe ser llamada directamente</b>
	 * 
	 * @param todosSelected <code>TRUE</code> si el valor seleccionado por el
	 *            usuario es &quot;Todos&quot;. <code>FALSE</code> en caso
	 *            contrario
	 */
	public final void setInternalTodosSelected(boolean todosSelected) {
		this.todosSelected = todosSelected;
	}

	/**
	 * Establece el String utilizado como valor especial
	 * 
	 * @param todos String con el valor especial
	 */
	protected void setStringTODOS(String todos) {
		this.stringTODOS = todos;
		fireModelChangeListener(EVENT_TYPE_VALUES_CHANGE);
	}

	/**
	 * Devuelve el String utilizado como valor especial
	 * 
	 * @return String con el valor especial
	 */
	public String getStringTODOS() {
		return this.stringTODOS;
	}

	/**
	 * En caso que el filtro tenga valor todos, establece el valor de todos
	 * seleccionado. Sino, el primer elemento de la lista.
	 * 
	 * @return <code>null</code> si la lista no tiene valores o si el valor todos esta habilitado. 
	 */
	@Override
	protected E getDefaultValue() {
		if (hasTodosOption()) { 
			return null;
		} else if (getValoresSeleccionables().size() > 0){
			return getValoresSeleccionables().get(0);
		}
		return null;
	}
}