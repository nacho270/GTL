package ar.com.fwcommon.templates.modulo.gui.acciones;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;

import ar.com.fwcommon.templates.modulo.ModuloTemplate;
import ar.com.fwcommon.templates.modulo.gui.meta.GuiSet;
import ar.com.fwcommon.templates.modulo.model.acciones.Accion;
import ar.com.fwcommon.templates.modulo.model.acciones.Acciones;
import ar.com.fwcommon.templates.modulo.model.listeners.AccionExecutedEvent;
import ar.com.fwcommon.templates.modulo.model.listeners.AccionExecutedListener;
import ar.com.fwcommon.util.log.FWLogger;

/**
 * GUI que contiene todas las acciones
 * 
 * 
 * 
 *
 * @param <T> Tipo de datos que van a recibir las acciones
 */
@SuppressWarnings("serial")
public class GuiAcciones<T> extends GuiSet<T, Accion<T>> implements IGuiAcciones<T> {
	private static final FWLogger logger = new FWLogger(GuiAcciones.class); 
	private Map<String, List<JComponent>> groupComponents;

	public GuiAcciones(ModuloTemplate<T, ?> owner, Acciones<T> acciones) {
		super(owner);		
		setModel(acciones);
		setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 5));
	}
	
	/* (non-Javadoc)
	 * ar.com.fwcommon.templates.modulo.gui.acciones.IGuiAcciones#getModel()
	 */
	public Acciones<T> getModel() {
		return (Acciones<T>)super.getModel();
	}
	/* (non-Javadoc)
	 * ar.com.fwcommon.templates.modulo.gui.acciones.IGuiAcciones#setModel(ar.com.fwcommon.templates.modulo.model.acciones.Acciones)
	 */
	public void setModel(Acciones<T> acciones) { 
		if (this.model != acciones) {
			if (acciones.getCantidadFilas() != 1) logger.warn("Las acciones pueden mostrarse en una única fila utilizando esta vista");
			removeAll();
			if (this.model != null) this.model.removeListChangeListener(getElementListChangeListener());
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
		//Creo la lista de componentes
		List<JComponent> lstComponents = new ArrayList<JComponent>();
		if (this.getComponentCount() > 0) {
			final JSeparator separator = new JSeparator(JSeparator.VERTICAL);
			separator.setPreferredSize(new Dimension(2, GuiAccion.DEFAULT_BUTTON_HEIGHT));
			lstComponents.add(separator);
		}
		for (Accion<T> accion : acciones) {
			lstComponents.add(createGuiAccion(accion));
		}
		getGroupComponents().put(name, lstComponents);
		for (JComponent component : lstComponents) {
			this.add(component);
		}
		this.validate();
	}

	/*
	 * (non-Javadoc)
	 * ar.com.fwcommon.templates.modulo.gui.meta.GuiSet#addSingleElement(java.lang.Object)
	 */
	@Override
	protected void addSingleElement(Accion<T> accion) {
		this.add(createGuiAccion(accion));
		this.validate();
	}

	/*
	 * (non-Javadoc)
	 * ar.com.fwcommon.templates.modulo.gui.meta.GuiSet#removeGroupElements(java.lang.String, java.util.List)
	 */
	@Override
	protected void removeGroupElements(String name, List<Accion<T>> acciones) {
		List<JComponent> components = getGroupComponents().remove(name);
		if (components != null) {
			for (JComponent component : components) {
				this.remove(component);
			}
			this.validate();
		}
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
	private Map<String, List<JComponent>> getGroupComponents() {
		if (groupComponents == null) {
			groupComponents = new HashMap<String, List<JComponent>>();
		}
		return groupComponents;
	}
	
	/* (non-Javadoc)
	 * ar.com.fwcommon.templates.modulo.gui.acciones.IGuiAcciones#addAccionExecutedListener(ar.com.fwcommon.templates.modulo.model.listeners.AccionExecutedListener)
	 */
	public void addAccionExecutedListener(AccionExecutedListener listener) {
		listenerList.add(AccionExecutedListener.class, listener);
	}
	
	/* (non-Javadoc)
	 * ar.com.fwcommon.templates.modulo.gui.acciones.IGuiAcciones#removeAccionExecutedListener(ar.com.fwcommon.templates.modulo.model.listeners.AccionExecutedListener)
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