package ar.com.fwcommon.templates.modulo.gui.acciones;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import ar.com.fwcommon.templates.modulo.ModuloTemplate;
import ar.com.fwcommon.templates.modulo.gui.meta.GuiSet;
import ar.com.fwcommon.templates.modulo.model.acciones.Accion;
import ar.com.fwcommon.templates.modulo.model.acciones.Acciones;
import ar.com.fwcommon.templates.modulo.model.listeners.AccionExecutedEvent;
import ar.com.fwcommon.templates.modulo.model.listeners.AccionExecutedListener;

/**
 * GUI que contiene todas las acciones. Las acciones se encuentran agrupadas en
 * frames que ocupan todas las filas
 * 
 * 
 * 
 * @param <T> Tipo de datos que van a recibir las acciones
 */
@SuppressWarnings("serial")
public class GuiAccionesMultilineBox<T> extends GuiSet<T, Accion<T>> implements IGuiAcciones<T> {
	private Map<String, JPanel> groupComponents;
	
	public GuiAccionesMultilineBox(ModuloTemplate<T, ?> owner, Acciones<T> acciones) {
		super(owner);	
		setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
		setModel(acciones);
	}
	
	/**
	 * Devuelve el modelo de la GUI
	 * @return Modelo con las Acciones
	 */
	public Acciones<T> getModel() {
		return (Acciones<T>)super.getModel();
	}
	/**
	 * Establece el modelo de la GUI
	 * @param acciones Modelo con las Acciones
	 */
	public void setModel(Acciones<T> acciones) { 
		if (this.model != acciones) {
			if (this.model != null) this.model.removeListChangeListener(getElementListChangeListener());
			removeAll();
			this.model = acciones;
			final int total = acciones.getElementCount();
			for (int i=0; i<total; i++) {
				addElement(acciones.getElement(i));
			}
			acciones.addListChangeListener(getElementListChangeListener());
			notificar();
		}
	}

	/*
	 * (non-Javadoc)
	 * ar.com.fwcommon.templates.modulo.gui.meta.GuiSet#addGroupElements(java.lang.String, java.util.List)
	 */
	@Override
	protected void addGroupElements(String name, List<Accion<T>> acciones) {
		int rows = getModel().getCantidadFilas();
		int cols = (acciones.size()+rows-1)/rows;
		//Creo la lista de componentes
		JPanel group = new JPanel(new GridLayout(rows, cols, 5, 5));
		group.setBorder(BorderFactory.createTitledBorder(name));
		for (Accion<T> accion : acciones) {
			group.add(createGuiAccion(accion));
		}
		getGroupComponents().put(name, group);
		this.add(group);
		this.validate();
	}

	/*
	 * (non-Javadoc)
	 * ar.com.fwcommon.templates.modulo.gui.meta.GuiSet#addSingleElement(java.lang.Object)
	 */
	@Override
	protected void addSingleElement(Accion<T> accion) {
		addGroupElements("", Collections.singletonList(accion));
	}

	/*
	 * (non-Javadoc)
	 * ar.com.fwcommon.templates.modulo.gui.meta.GuiSet#removeGroupElements(java.lang.String, java.util.List)
	 */
	@Override
	protected void removeGroupElements(String name, List<Accion<T>> acciones) {
		JPanel group = getGroupComponents().remove(name);
		if (group != null) {
			this.remove(group);
		}
		this.validate();
	}

	/*
	 * (non-Javadoc)
	 * ar.com.fwcommon.templates.modulo.gui.meta.GuiSet#removeSingleElement(java.lang.Object)
	 */
	@Override
	protected void removeSingleElement(Accion<T> accion) {
		Component[] components = getComponents();
		for (int i = 0; i < components.length; i++) {
			if (components[i] instanceof GuiAccion) {
				if (accion.equals(((GuiAccion)components[i]).getAccion())) {
					remove(components[i]);
					this.validate();
					return;
				}
			}
		}
	}
	
	/**
	 * Crea una GuiAcción para un determinado botón
	 * @param accion Acción para la que se quiere crear la GUI
	 * @return GUI de la acción
	 */
	private GuiAccion<T> createGuiAccion(Accion<T> accion) {
		GuiAccion<T> guiAccion = new GuiAccion<T>(getOwner(), accion);
		guiAccion.addAccionExecutedListener(getAccionExecutedListener());
		return guiAccion;
	}

	/**
	 * Devuelve un MAP en donde se guardan todos los componentes utilizados en un grupo
	 * @return <code>MAP&lt;nombre grupo, componentes utilizados&gt;</code>
	 */
	private Map<String, JPanel> getGroupComponents() {
		if (groupComponents == null) {
			groupComponents = new HashMap<String, JPanel>();
		}
		return groupComponents;
	}
	
	/**
	 * Agrega un listener que notifica cuando se ejecutó una acción
	 * @param listener Listener a agregar
	 */
	public void addAccionExecutedListener(AccionExecutedListener listener) {
		listenerList.add(AccionExecutedListener.class, listener);
	}
	
	/**
	 * Quita un listener que notifica cuando se ejecutó una acción
	 * @param listener Listener a quitar
	 */
	public void removeAccionExecutedListener(AccionExecutedListener listener) {
		listenerList.remove(AccionExecutedListener.class, listener);
	}
	
	/**
	 * Notifica que se ha ejecutado la acción
	 * 
	 * @param e Evento producido por la acción
	 */
	protected void fireActionExecutedListener(final AccionExecutedEvent e) {
		final AccionExecutedListener[] l = listenerList.getListeners(AccionExecutedListener.class);
		SwingUtilities.invokeLater(new Runnable() {
			@SuppressWarnings("unchecked")
			public void run() {
				for (int i = 0; i < l.length; i++) {
					l[i].accionExcecuted(e);
				}
			}			
		});
	}
	
	private AccionExecutedListener accionExecutedListener;
	/**
	 * Devuelve el listener que se les va a colocar a los GuiAccion para
	 * notificar a los suscriptores locales
	 * @return Listener que escucha las acciones
	 */
	private AccionExecutedListener getAccionExecutedListener() {
		if (accionExecutedListener == null) {
			accionExecutedListener = new AccionExecutedListener() {
				public void accionExcecuted(AccionExecutedEvent e) {
					fireActionExecutedListener(e);
				}
			};
		}
		return accionExecutedListener;
	}
}