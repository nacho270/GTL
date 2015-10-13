package ar.com.fwcommon.templates.modulo.model.filtros;

import javax.swing.SwingUtilities;
import javax.swing.event.EventListenerList;

import ar.com.fwcommon.templates.modulo.model.listeners.ModelChangeEvent;
import ar.com.fwcommon.templates.modulo.model.listeners.ModelChangeListener;

/**
 * Filtro que permite reducir la lista de elementos a mostrar.<br>
 * 
 * 
 *
 * @param <T> Tipo de datos que va a utilizar el filtro
 * @param <U> Tipo de datos ingresado por el usuario
 */
public abstract class Filtro<T, U> {
	/**
	 * Indica que cambiaron los valores que permite seleccionar el filtro
	 */
	public static final int EVENT_TYPE_VALUES_CHANGE = 0;
	
	/**
	 * Indica que cambió el nombre del filtro
	 */
	public static final int EVENT_TYPE_NAME_CHANGE = 1;
	
	/**
	 * Indica que el filtro ha actualizado su valor desde la GUI
	 */
	public static final int EVENT_TYPE_VALUE_CHANGE = 2;
		
	/**
	 * Evento interno que se dispara para enviar actualizaciones a la GUI
	 */
	public static final int EVENT_TYPE_GUI_VALUE_MUST_UPDATE = 3;
	
	private EventListenerList listeners = new EventListenerList();
	private String nombre;
	private U value;
	
	protected Filtro() {
		super();
	}
    /**
     * Crea un filtro. El constructor es de Package porque 
     */
    public Filtro(String nombre) {
        super();
        setNombre(nombre);
        setValue(getDefaultValue());
    }

	/**
	 * Dice si un elemento determinado pasa el filtro o no.
	 * 
	 * @param item el item a filtrar.
	 * @return <code>TRUE</code> si pasó el filtro y <code>FALSE</code> en
	 *         caso contrario
	 */
	public abstract boolean filtrar(T item);

	/**
	 * Devuelve el nombre del filtro. Este nombre será utilizado en la pantalla
	 * para mostrarse al usuario
	 * 
	 * @return Nombre del filtro
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Establece el nombre del filtro. Este nombre será utilizado en la pantalla
	 * para mostrarse al usuario
	 * 
	 * @param nombre Nombre del filtro
	 */
	public void setNombre(String nombre) {
		if (this.nombre != nombre) {
			this.nombre = nombre;
			fireModelChangeListener(EVENT_TYPE_NAME_CHANGE);
		}
	}

	/**
	 * Devuelve el valor que el usuario ha ingresado en el filtro
	 * 
	 * @return Valor que el usuario ha ingresado en el filtro
	 */
	public U getValue() {
		return value;
	}

	/**
	 * Establece el valor que el usuario ha ingresado en el filtro.
	 * 
	 * @param value Valor que el usuario ha ingresado en el filtro
	 */
	public final void setValue(U value) {
		if (this.value != value) {
			this.value = value;
			fireModelChangeListener(EVENT_TYPE_GUI_VALUE_MUST_UPDATE);
		}
	}
	
	/**
	 * Establece el valor que el usuario ha ingresado en el filtro.<br>
	 * <b><u>IMPORTANTE:</u> Esta función es pública solo por una cuestión de
	 * implementación, pero no debe ser llamada directamente</b>
	 * 
	 * @param value Valor que el usuario ha ingresado en el filtro
	 */
	public final void setInternalValue(U value) {
		if (this.value != value) {
			this.value = value;
			fireModelChangeListener(EVENT_TYPE_VALUE_CHANGE);
		}
	}
	
	/**
	 * Devuelve el valor por defecto del filtro
	 * @return Valor por defecto del filtro
	 */
	protected abstract U getDefaultValue();
	
	/**
	 * Coloca el filtro en su valor por defecto
	 */
	public void resetFilter() {
		setValue(getDefaultValue());
	}
	
	/**
	 * Devuelve información que va a ser utilizada para mostrar el filtro en
	 * pantalla
	 * 
	 * @return {@link FiltroRenderingInformation} que contiene la información
	 *         necesaria para mostrar el componente en pantalla
	 * FiltroRenderingInformation
	 */
	public abstract FiltroRenderingInformation getRenderingInformation();
	
	/**
	 * Agrega un listener que notifica cuando cambio el filtro
	 * @param listener Listener a agregar
	 */
	public void addModelChangeListener(ModelChangeListener listener) {
		listeners.add(ModelChangeListener.class, listener);
	}
	
	/**
	 * Quita un listener que notifica cuando cambio el filtro
	 * @param listener Listener a quitar
	 */
	public void removeModelChangeListener(ModelChangeListener listener) {
		listeners.remove(ModelChangeListener.class, listener);
	}
	
	/**
	 * Avisa que el filtro cambió
	 */
	protected void fireModelChangeListener(final int eventType) {
		final ModelChangeEvent e = new ModelChangeEvent(this, eventType);
		final ModelChangeListener[] l = listeners.getListeners(ModelChangeListener.class);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				for (int i = 0; i < l.length; i++) {
					l[i].stateChanged(e);
				}
			}			
		});
	}
}