package ar.com.fwcommon.templates.modulo.gui.meta;

import java.awt.Component;
import java.util.List;

import ar.com.fwcommon.componentes.GuiPanelObservable;
import ar.com.fwcommon.templates.modulo.ModuloTemplate;
import ar.com.fwcommon.templates.modulo.model.listeners.ListChangeEvent;
import ar.com.fwcommon.templates.modulo.model.listeners.ListChangeListener;
import ar.com.fwcommon.templates.modulo.model.meta.GroupModel;
import ar.com.fwcommon.templates.modulo.model.meta.Model;
import ar.com.fwcommon.templates.modulo.model.meta.ModelSet;
import ar.com.fwcommon.templates.modulo.model.meta.SingleModel;

/**
 * Clase que agrupa a las GUI de agrupadores de models
 * 
 * 
 *
 * @param <T> Elemento que se va a procesar (elemento de la tabla)
 * @param <E> Tipo de Item que se maneja (Accion, Filtro, etc...)
 */
public abstract class GuiSet<T, E>  extends GuiPanelObservable implements IGuiSet<T, E> {
	private final ModuloTemplate<T, ?> owner;
	private ListChangeListener elementListChangeListener;
	protected ModelSet<E> model;

	protected GuiSet(ModuloTemplate<T, ?> owner) {
		super();
		this.owner = owner;
	}
	
	/**
	 * Devuelve el Módulo Template al que pertenece esta GUI
	 * @return Modulo Template al que pertenece esta GUI
	 */
	protected ModuloTemplate<T, ?> getOwner() {
		return owner;
	}
	
	/* (non-Javadoc)
	 * ar.com.fwcommon.templates.modulo.gui.meta.IGuiSet#getModel()
	 */
	public ModelSet<E> getModel() {
		return model;
	}
	
	/*
	 * (non-Javadoc)
	 * ar.com.fwcommon.templates.modulo.gui.meta.IGuiSet#getComponent()
	 */
	public Component getComponent() {
		return this;
	}
	
	/**
	 * Agrega un elemento, que puede ser o bien un grupo o un elemento
	 * individual.
	 * 
	 * @param element Elemento a agregar
	 */
	protected final void addElement(Model<E> element) {
		if (element.isSingleModel()) {
			addSingleElement(((SingleModel<E>)element).getModel());
		} else {			
			GroupModel<E> groupModel = (GroupModel<E>)element;
			addGroupElements(groupModel.getName(), groupModel.getModels());
		}
	}

	/**
	 * Agrega un elemento individual<br>
	 * Esta función NO debe llamar a {@link #notificar()}
	 * @param element Elemento a agregar
	 */
	protected abstract void addSingleElement(E element);
	
	/**
	 * Agrega un grupo de elementos<br>
	 * Esta función NO debe llamar a {@link #notificar()}
	 * @param name Nombre del grupo
	 * @param elements Elementos del grupo
	 */
	protected abstract void addGroupElements(String name, List<E> elements);
	
	/**
	 * Quita un elemento, que puede ser o bien un grupo o un elemento
	 * individual.
	 * 
	 * @param element Elemento a quitar
	 */
	protected final void removeElement(Model<E> element) {
		if (element.isSingleModel()) {
			removeSingleElement(((SingleModel<E>)element).getModel());
		} else {			
			GroupModel<E> groupModel = (GroupModel<E>)element;
			removeGroupElements(groupModel.getName(), groupModel.getModels());
		}
	}

	/**
	 * Quita un elemento individual<br>
	 * Esta función NO debe llamar a {@link #notificar()}
	 * @param element Elemento a quitar
	 */
	protected abstract void removeSingleElement(E element);
	
	/**
	 * Quita un grupo de elementos<br>
	 * Esta función NO debe llamar a {@link #notificar()}
	 * @param name Nombre del grupo
	 * @param elements Elementos del grupo
	 */
	protected abstract void removeGroupElements(String name, List<E> elements);
	
	/**
	 * Notifica que la GUI cambió
	 */
	protected final void notificar() {
		setChanged();
		notifyObservers();
	}

	@SuppressWarnings("unchecked")
	protected final ListChangeListener getElementListChangeListener() {
		if (elementListChangeListener == null) {
			elementListChangeListener = new ListChangeListener() {
				public void elementsAdded(ListChangeEvent e) {
					List<? extends Object> elements = e.getElements();
					for (Object element : elements) {
						addElement((Model<E>)element);
					}
					notificar();
				}

				public void elementsRemoved(ListChangeEvent e) {
					List<? extends Object> elements = e.getElements();
					for (Object element : elements) {
						removeElement((Model<E>)element);
					}
					notificar();
				}				
			};
		}
		return elementListChangeListener;
	}
}
