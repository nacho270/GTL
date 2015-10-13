package ar.com.fwcommon.templates.modulo.model.totales;

import java.awt.Color;

import javax.swing.SwingUtilities;
import javax.swing.event.EventListenerList;

import ar.com.fwcommon.templates.modulo.model.listeners.ModelChangeEvent;
import ar.com.fwcommon.templates.modulo.model.listeners.ModelChangeListener;

/**
 * Clase abtracta que agrupa a los totales
 * 
 * 
 *
 * @param <T>
 */
public abstract class Total<T> {
	/**
	 * Indica que cambió el valor del filtro
	 */
	public static final int EVENT_TYPE_VALUE_CHANGE = 0;
	/**
	 * Indica que cambió el nombre del filtro
	 */
	public static final int EVENT_TYPE_NAME_CHANGE = 1;
	/**
	 * Indica que cambió el tooltip del filtro
	 */
	public static final int EVENT_TYPE_TOOLTIP_CHANGE = 2;
	/**
	 * Indica que cambió el color del filtro
	 */
	public static final int EVENT_TYPE_COLOR_CHANGE = 3;
	
	private EventListenerList listeners = new EventListenerList();
	private String nombre;
	private String tooltip;
	private Color color;
	private int value = 0;

    public Total(String nombre) {
    	super();
    	setNombre(nombre);
    }

    /**
     * Incorpora al total un valor determinado
     * @param objeto Objeto que se desea totalizar
     */
    public abstract void totalizar(T objeto);

    /**
     * Devuelve el valor del total
     * @return Valor del total
     */
    public int getValue() {
        return value;
    }

    /**
     * Establece el valor del total
     * @param value Valor del total
     */
    public void setValue(int value) {
    	if (this.value != value) {
	        this.value = value;
	        fireModelChangeListener(EVENT_TYPE_VALUE_CHANGE);
    	}
    }

    /**
	 * Incrementa el valor del total en una unidad
	 */
    public void incrementarValor() {
    	if(value==0){
    		setValue(0);
    	}
        setValue(getValue() + 1);
    } 

	/**
	 * Devuelve el nombre del total. <br>
	 * Este nombre será utilizado en el label de la GUI
	 * @return Nombre del total
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Establece el nombre del total. <br>
	 * Este nombre será utilizado en el label de la GUI
	 * @param nombre Nombre a establecer
	 */
	protected void setNombre(String nombre) {
		if (this.nombre != nombre) {
			this.nombre = nombre;
			fireModelChangeListener(EVENT_TYPE_NAME_CHANGE);
		}
	}

	/**
	 * Devuelve el color de fondo del total
	 * @return Color de fondo que tendrá el total
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Establece el color del fondo del total
	 * @param color Color de fondo del total
	 */
	protected void setColor(Color color) {
		if (this.color != color) {			
			this.color = color;
			fireModelChangeListener(EVENT_TYPE_COLOR_CHANGE);
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
	 * Agrega un listener que notifica cuando cambio el total
	 * @param listener Listener a agregar
	 */
	public void addModelChangeListener(ModelChangeListener listener) {
		listeners.add(ModelChangeListener.class, listener);
	}
	
	/**
	 * Quita un listener que notifica cuando cambio el total
	 * @param listener Listener a quitar
	 */
	public void removeModelChangeListener(ModelChangeListener listener) {
		listeners.remove(ModelChangeListener.class, listener);
	}
	
	/**
	 * Avisa que el total cambió
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