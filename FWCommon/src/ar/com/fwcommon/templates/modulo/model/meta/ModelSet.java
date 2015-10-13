package ar.com.fwcommon.templates.modulo.model.meta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.event.EventListenerList;

import ar.com.fwcommon.templates.modulo.model.listeners.ListChangeEvent;
import ar.com.fwcommon.templates.modulo.model.listeners.ListChangeListener;

/**
 * Clase que agrupa a todos los agrupadores de negocio
 * 
 *
 * @param <T> Modelos que agrupa
 */
public class ModelSet<T> {
	private EventListenerList listeners = new EventListenerList();
	private List<Model<T>> elements;
	
	protected ModelSet() {
		super();
	}

	public synchronized final List<Model<T>> getElements() {
		if (elements == null) {
			elements = new ArrayList<Model<T>>();
		}
		return elements;
	}
	
	/**
	 * Devuelve la cantidad de elementos
	 * @return Cantidad de elementos
	 */
	public synchronized int getElementCount() {
		return getElements().size();
	}
	
	/**
	 * Devuelve un elemento determinado
	 * @param index indice del elemento
	 * @return Elemento correspondiente a ese índice
	 * @throws IndexOutOfBoundsException Si el índice se encuentra fuera de rango
	 */
	public synchronized Model<T> getElement(int index) {
		return getElements().get(index);
	}
	
	/**
	 * Agrega un elemento
	 * @param element Elemento a agregar
	 */
	public synchronized void addSingleElement(T element) {
		SingleModel<T> newModel = new SingleModel<T>(element); 
		getElements().add(newModel);
		fireListChangeAddedListener(newModel);
	}

	/**
	 * Agrega un grupo de elementos
	 * @param name Nombre del grupo de elementos
	 * @param elements Elementos a agregar
	 */
	public synchronized void addElementGroup(String name, T... elements) {
		addElementGroup(name, Arrays.asList(elements));
	}
	
	/**
	 * Agrega un grupo de elementos
	 * @param name Nombre del grupo de elementos
	 * @param elements Elementos a agregar
	 */
	public synchronized void addElementGroup(String name, List<T> elements) {
		GroupModel<T> newModel = new GroupModel<T>(name, elements);
		getElements().add(newModel);
		fireListChangeAddedListener(newModel);
	}
	
	/**
	 * Quita un elemento
	 * @param index Índice del elemento a quitar
	 * @throws IndexOutOfBoundsException Si el índice está fuera de rango
	 */
	public synchronized void removeElement(int index) {
		Model<T> oldModel = getElements().remove(index);
		fireListChangeRemovedListener(oldModel);
	}

    /**
     * Agrega un listener que notifica cuando se agregan/quitan elementos
     * @param listener Listener a agregar
     */
    public final void addListChangeListener(ListChangeListener listener) {
    	listeners.add(ListChangeListener.class, listener);
    }

    /**
     * Quita un listener que notifica cuando se agregan/quitan elementos
     * @param listener Listener a quitar
     */
    public final void removeListChangeListener(ListChangeListener listener) {
    	listeners.remove(ListChangeListener.class, listener);
    }
    
    /**
     * Avisa que se agregó un nuevo elemento
     * @param element Elemento que acaba se ser agregado
     */
    protected final void fireListChangeAddedListener(Model<T> element) {
    	fireListChangeAddedListener(Collections.singletonList(element));
    }
    
    /**
     * Avisa que se agregó un nuevo elemento
     * @param elements Elementos que acaban se ser agregados
     */
    protected final void fireListChangeAddedListener(Model<T>... elements) {
    	fireListChangeAddedListener(Arrays.asList(elements));
    }
    
    /**
     * Avisa que se agregó un nuevo elemento
     * @param elements Elementos que acaban se ser agregados
     */
    protected final void fireListChangeAddedListener(List<Model<T>> elements) {
		final ListChangeEvent e = new ListChangeEvent(this, Collections.unmodifiableList(elements));
		final ListChangeListener[] l = listeners.getListeners(ListChangeListener.class);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				for (int i = 0; i < l.length; i++) {
					l[i].elementsAdded(e);
				}
			}			
		});
    }
    
    /**
     * Avisa que se quitó un elemento
     * @param element Elemento que fue removido
     */
    protected final void fireListChangeRemovedListener(Model<T> element) {
    	fireListChangeRemovedListener(Collections.singletonList(element));
    }
    
    /**
     * Avisa que se quitaron un elementos
     * @param elements Elementos que fueron removidos
     */
    protected final void fireListChangeRemovedListener(Model<T>... elements) {
    	fireListChangeRemovedListener(Arrays.asList(elements));
    }
    
    /**
     * Avisa que se quitaron un elementos
     * @param elements Elementos que fueron removidos
     */
    protected final void fireListChangeRemovedListener(List<Model<T>> elements) {
		final ListChangeEvent e = new ListChangeEvent(this, Collections.unmodifiableList(elements));
		final ListChangeListener[] l = listeners.getListeners(ListChangeListener.class);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				for (int i = 0; i < l.length; i++) {
					l[i].elementsRemoved(e);
				}
			}			
		});
    }
}
