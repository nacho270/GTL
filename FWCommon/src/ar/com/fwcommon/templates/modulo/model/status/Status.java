package ar.com.fwcommon.templates.modulo.model.status;

import java.awt.Color;

import javax.swing.SwingUtilities;
import javax.swing.event.EventListenerList;

import ar.com.fwcommon.templates.modulo.model.listeners.AccionEvent;
import ar.com.fwcommon.templates.modulo.model.listeners.ModelChangeEvent;
import ar.com.fwcommon.templates.modulo.model.listeners.ModelChangeListener;

/**
 * Componente que permite mostrar un estado de los elementos seleccionados
 * 
 * 
 *
 * @param <T> Elemento que del que se va a consultar
 */
public abstract class Status<T> {
	/**
	 * Indica que cambió el valor del estado
	 */
	public static final int EVENT_TYPE_VALUE_CHANGE = 0;
	/**
	 * Indica que cambió el nombre del estado
	 */
	public static final int EVENT_TYPE_NAME_CHANGE = 1;
	/**
	 * Indica que cambió el tooltip del estado
	 */
	public static final int EVENT_TYPE_TOOLTIP_CHANGE = 2;
	/**
	 * Indica que cambió el color del estado
	 */
	public static final int EVENT_TYPE_COLOR_CHANGE = 3;
	
	private EventListenerList listeners = new EventListenerList();	
	private String nombre;
	private String value;
	private String tooltip;
	private Color color = Color.WHITE;
	private Color colorInfo = Color.WHITE;
	
	public Status() {
		super();
	}

	/**
	 * Devuelve el nombre del estado. <br>
	 * Este nombre será utilizado en el label de la GUI
	 * @return Nombre del estado
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Establece el nombre del estado. <br>
	 * Este nombre será utilizado en el label de la GUI
	 * @param nombre Nombre a establecer
	 */
	public void setNombre(String nombre) {
		if (this.nombre != nombre) {
			this.nombre = nombre;
			fireModelChangeListener(EVENT_TYPE_NAME_CHANGE);
		}
	}

	/**
	 * Devuelve el tooltip que tendrá el componente
	 * @return tooltip que tendrá el componente
	 */
	public String getTooltip() {
		return tooltip;
	}

	/**
	 * Establece el tooltip que tiene el componente
	 * @param tooltip Tooltip del componente
	 */
	public void setTooltip(String tooltip) {
		if (this.tooltip != tooltip) {
			this.tooltip = tooltip;
			fireModelChangeListener(EVENT_TYPE_TOOLTIP_CHANGE);
		}
	}
	
    /**
     * Devuelve el valor del estado
     * @return Valor del estado
     */
    public String getValue() {
        return value;
    }

    /**
     * Establece el valor del estado
     * @param value Valor del estado
     */
    public void setValue(String value) {
    	if (this.value != value) {
	        this.value = value;
	        fireModelChangeListener(EVENT_TYPE_VALUE_CHANGE);
    	}
    }

	/**
	 * Devuelve el color de fondo del estado
	 * @return Color de fondo que tendrá el estado
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Establece el color del fondo del estado
	 * @param color Color de fondo del estado
	 */
	public void setColor(Color color) {
		if (this.color != color) {			
			this.color = color;
			fireModelChangeListener(EVENT_TYPE_COLOR_CHANGE);
		}
	}

	/**
	 * Actualiza en función de lo que se encuentra seleccionado el estado de este objeto 
	 * @param e Evento que contiene información del módulo
	 */
	protected abstract void update(AccionEvent<T> e);
	
	/**
	 * Agrega un listener que notifica cuando cambio el status
	 * @param listener Listener a agregar
	 */
	public void addModelChangeListener(ModelChangeListener listener) {
		listeners.add(ModelChangeListener.class, listener);
	}
	
	/**
	 * Quita un listener que notifica cuando cambio el status
	 * @param listener Listener a quitar
	 */
	public void removeModelChangeListener(ModelChangeListener listener) {
		listeners.remove(ModelChangeListener.class, listener);
	}
	
	/**
	 * Avisa que el status cambió
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
	
	/**
	 * Devuelve el color de fondo del estado del Label de Info
	 * @return Color de fondo que tendrá el Label de Info
	 */
	public Color getInfoColor() {
		return colorInfo;
	}

	/**
	 * Establece el color del fondo del Label de Info
	 * @param color Color de fondo del Label de Info
	 */
	public void setInfoColor(Color colorInfo) {
		if (this.colorInfo != colorInfo) {			
			this.colorInfo = colorInfo;
			fireModelChangeListener(EVENT_TYPE_COLOR_CHANGE);
		}
	}


}
