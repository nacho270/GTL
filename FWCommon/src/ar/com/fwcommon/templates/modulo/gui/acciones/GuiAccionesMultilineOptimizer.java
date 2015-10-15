package ar.com.fwcommon.templates.modulo.gui.acciones;

import java.awt.Component;
import java.awt.Insets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import ar.com.fwcommon.templates.modulo.ModuloTemplate;
import ar.com.fwcommon.templates.modulo.gui.meta.GuiSet;
import ar.com.fwcommon.templates.modulo.gui.utils.DiagonalGridLayout;
import ar.com.fwcommon.templates.modulo.gui.utils.JPanelGroup;
import ar.com.fwcommon.templates.modulo.model.acciones.Accion;
import ar.com.fwcommon.templates.modulo.model.acciones.Acciones;
import ar.com.fwcommon.templates.modulo.model.listeners.AccionExecutedEvent;
import ar.com.fwcommon.templates.modulo.model.listeners.AccionExecutedListener;

/**
 * GUI que contiene todas las acciones. Las acciones son distribuidas en
 * multiples filas (de acuerdo con {@link Acciones#getCantidadFilas()}). La
 * distribución de las mismas intenta optimizar el espacio utilizando una
 * estrategia greedy (golosa) mediante el orden en que se reciben las acciones
 * (y grupos de acciones). Es decir, que para modificar la distribución de las
 * mismas habrá que ir alterando el orden en que se fueron agregando<br>
 * La estrategia se basa en las siguientes reglas:
 * <ol>
 * <li> Cuando se recibe una acción (o grupo de ellas) se le asigna una
 * ubicación, la cual nunca será alterada (sin importar las acciones que se
 * agreguen despues)
 * <li> Una fila en ningún momento podrá tener más acciones que la fila
 * inmediata superior.
 * <li> Un grupo de acciones solo podrá tener una única posición vacía
 * <li> Ninguna fila podrá tener una posicion vacia, a menos que esta posicion
 * se encuentre dentro de un grupo que satisface la regla anterior
 * <li> Al agregar una acción (o grupo de ellas) se colocará en una fila que
 * cumpla con todos los requisitos anteriores y en caso que haya varias
 * posibles, en la inferior de todas ellas.
 * <li> Cuando se agrega un grupo de acciones, una vez que se haya determinado
 * la fila superior del mismo (usando la regla anterior) se procurará que el
 * grupo ocupe la mayor cantidad de filas posibles que respeten todas las reglas anteriores
 * </ol>
 * @param <T> Tipo de datos que van a recibir las acciones
 */
public class GuiAccionesMultilineOptimizer<T> extends GuiSet<T, Accion<T>> implements IGuiAcciones<T> {

	private static final long serialVersionUID = -2893181818139689423L;
	
	private DiagonalGridLayout layout = new DiagonalGridLayout(1);
	private Map<String, JPanelGroup> groupComponents;
	
	public GuiAccionesMultilineOptimizer(ModuloTemplate<T, ?> owner, Acciones<T> acciones) {
		super(owner);	
		setLayout(layout);
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
			this.layout.setRows(acciones.getCantidadFilas());
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
		JPanelGroup group = new JPanelGroup();
		TitledBorder titledBorder = BorderFactory.createTitledBorder(name);
		group.setBorder(titledBorder);
		Insets insets =  titledBorder.getBorderInsets(group);
		group.setBorder(titledBorder);
		group.getLayout().setVgap(insets.top+insets.bottom);
		group.setRows(Math.max(acciones.size(), 1));
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
		this.add(createGuiAccion(accion));
		this.validate();
	}

	/*
	 * (non-Javadoc)
	 * ar.com.fwcommon.templates.modulo.gui.meta.GuiSet#removeGroupElements(java.lang.String, java.util.List)
	 */
	@Override
	protected void removeGroupElements(String name, List<Accion<T>> acciones) {
		JPanelGroup group = getGroupComponents().remove(name);
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
				if (accion.equals(((GuiAccion<?>)components[i]).getAccion())) {
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
	private Map<String, JPanelGroup> getGroupComponents() {
		if (groupComponents == null) {
			groupComponents = new HashMap<String, JPanelGroup>();
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