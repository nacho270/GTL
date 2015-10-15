package ar.com.fwcommon.templates.modulo.gui.filtros;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import ar.com.fwcommon.boss.BossEstilos;
import ar.com.fwcommon.templates.modulo.ModuloTemplate;
import ar.com.fwcommon.templates.modulo.gui.meta.GuiSet;
import ar.com.fwcommon.templates.modulo.gui.utils.JHideablePanel;
import ar.com.fwcommon.templates.modulo.model.filtros.Filtro;
import ar.com.fwcommon.templates.modulo.model.filtros.Filtros;
import ar.com.fwcommon.templates.modulo.model.listeners.FilterChangeEvent;
import ar.com.fwcommon.templates.modulo.model.listeners.FilterChangeListener;
import ar.com.fwcommon.templates.modulo.resources.ModuleSettingsManager;

/**
 * Gui que agrupa todos los filtros
 * 
 * 
 * @param <T> Tipo de datos que va a filtrar (datos de la tabla)
 */
public class GuiFiltros<T> extends GuiSet<T, Filtro<T, ?>> implements IGuiFiltros<T> {

	private static final long serialVersionUID = -2739467467312538102L;
	
	private static final int MAX_FILTERS_PER_LINE = 500;
	private Map<String, JPanel> filterGroups;
	private List<JPanel> panelesFilas;
	private List<Integer> panelesCoordenadaX;

	public GuiFiltros(ModuloTemplate<T, ?> owner, Filtros<T> filtros) {
		super(owner);
		this.setLayout(new GridBagLayout());
		panelesCoordenadaX = new ArrayList<Integer>();
		setModel(filtros);		
	}
	
	/* (non-Javadoc)
	 * ar.com.fwcommon.templates.modulo.gui.filtros.IGuiFiltros#getModel()
	 */
	public Filtros<T> getModel() {
		return (Filtros<T>)super.getModel();
	}

	/* (non-Javadoc)
	 * ar.com.fwcommon.templates.modulo.gui.filtros.IGuiFiltros#setModel(ar.com.fwcommon.templates.modulo.model.filtros.Filtros)
	 */
	public void setModel(Filtros<T> filtros) {
		if (this.model != filtros) {
			if (this.model != null) this.model.removeListChangeListener(getElementListChangeListener());
			this.model = filtros;
			this.model.addListChangeListener(getElementListChangeListener());
			
			
			this.removeAll();
			getPanelesFilas().clear();
			getPanelesCoordenadaX().clear();
			final int filterCount = filtros.getElementCount();
			for (int i=0; i<filterCount; i++) {
				addElement(filtros.getElement(i));
			}
		}
	}

	private JHideablePanel component;
	@Override
	public Component getComponent() {
		if (component == null) {
			component = new JHideablePanel(new BorderLayout());
			component.setTitle(ModuleSettingsManager.getInstance().getStringResource(ModuleSettingsManager.RESOURCE_STRING_FILTOS));
			component.getContentPane().add(this, BorderLayout.CENTER);
			component.setCollapsed(false);
		}
		return component;
	}

	/*
	 * (non-Javadoc)
	 * ar.com.fwcommon.templates.modulo.gui.meta.GuiSet#addGroupElements(java.lang.String, java.util.List)
	 */
	@Override
	protected void addGroupElements(String name, List<Filtro<T, ?>> filtros) {
		if (filtros.size() > 0) {
			double weightX = 0;
			int fila = filtros.get(0).getRenderingInformation().getFila();
			JPanel jPanelGroup = new JPanel(new GridBagLayout());
			for (int i=0; i<filtros.size(); i++) {
				GuiFiltro<T, ?> guiFiltro = GuiFiltroFactory.createGuiFiltro(getOwner(), filtros.get(i));
				//Agrego el listener
				guiFiltro.addFilterChangeListener(getFilterChangeListener());
				GridBagConstraints constrains;
				if (guiFiltro.canGrow()) {
					weightX += 1.0;
					constrains = new GridBagConstraints(i, 0, 1, 1, 1, 0,
							GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, 
							new Insets(0, 0, 0, 0), 0, 0);
				} else {
					constrains = new GridBagConstraints(i, 0, 1, 1, 0, 0,
							GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, 
							new Insets(0, 0, 0, 0), 0, 0);					
				}
				jPanelGroup.add(guiFiltro,constrains);
			}
			jPanelGroup.setBorder(BorderFactory.createTitledBorder(null, name,
					TitledBorder.DEFAULT_JUSTIFICATION,
					TitledBorder.DEFAULT_POSITION, BossEstilos.getDefaultFont()));
			getFilterGroups().put(name, jPanelGroup);
			addComponent(fila, jPanelGroup, weightX);
			this.validate();
		}
	}

	/*
	 * (non-Javadoc)
	 * ar.com.fwcommon.templates.modulo.gui.meta.GuiSet#addSingleElement(java.lang.Object)
	 */
	@Override
	protected void addSingleElement(Filtro<T, ?> filtro) {
		GuiFiltro<T, ?> guiFiltro = GuiFiltroFactory.createGuiFiltro(getOwner(), filtro);
		guiFiltro.addFilterChangeListener(getFilterChangeListener());		
		addComponent(filtro.getRenderingInformation().getFila(), guiFiltro, guiFiltro.canGrow()?1:0);
		this.validate();
	}

	/*
	 * (non-Javadoc)
	 * ar.com.fwcommon.templates.modulo.gui.meta.GuiSet#removeGroupElements(java.lang.String, java.util.List)
	 */
	@Override
	protected void removeGroupElements(String name, List<Filtro<T, ?>> filtros) {
		JPanel jPanelGroup = getFilterGroups().get(name);
		if (jPanelGroup != null) {
			jPanelGroup.getParent().remove(jPanelGroup);
			
			//Quito los listeners
			Component[] components = jPanelGroup.getComponents();
			for (int i = 0; i < components.length; i++) {
				if (components[i] instanceof GuiFiltro) {
					((GuiFiltro<?, ?>)components[i]).removeFilterChangeListener(getFilterChangeListener());
				}
			}
			this.validate();
		}		
	}

	/*
	 * (non-Javadoc)
	 * ar.com.fwcommon.templates.modulo.gui.meta.GuiSet#removeSingleElement(java.lang.Object)
	 */
	@Override
	protected void removeSingleElement(Filtro<T, ?> filtro) {
		Component[] components = getPanelesFilas().get(filtro.getRenderingInformation().getFila()).getComponents();
		for (int i = 0; i < components.length; i++) {
			if (components[i] instanceof GuiFiltro) {
				if (filtro.equals(((GuiFiltro<?,?>)components[i]).getFiltro())) {
					((GuiFiltro<?,?>)components[i]).removeFilterChangeListener(getFilterChangeListener());
					remove(components[i]);
					this.validate();
					return;
				}
			}
		}
	}
	
	/**
	 * Agrega un componente determinado a una fila de filtros
	 * 
	 * @param fila Fila a la que se desea agregar (0 base index)
	 * @param component Componente que se desea agregar
	 * @param weightX Peso indicado para la expansión
	 */
	private void addComponent(int fila, JComponent component, double weightX) {
		while (fila >= getPanelesFilas().size()) {
			int pos = getPanelesFilas().size();
			getPanelesFilas().add(createNewRow());
			getPanelesCoordenadaX().add(0);
			this.add(getPanelesFilas().get(pos), new GridBagConstraints(0, pos,
					1, 1, 1, 0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}
		int x = getPanelesCoordenadaX().get(fila);
		getPanelesFilas().get(fila).add(component, 
				new GridBagConstraints(x, 0, 1, 1, weightX, 0, 
						GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
						new Insets(2, 2, 2, 2), 0, 0));
		getPanelesCoordenadaX().set(fila, x+1);
		this.validate();
	}
	
	
	/**
	 * Devuelve la lista que tiene todos los paneles de las filas
	 * @return Lista de paneles con las filas
	 */
	private List<JPanel> getPanelesFilas() {
		if(panelesFilas == null) {
			panelesFilas = new ArrayList<JPanel>();
		}
		return panelesFilas;
	}

	/**
	 * Devuelve la siguiente coordenada X a colocar en el gridbagconstrains
	 * @return Lista con todas las coordenadas X (una por fila)
	 */
	private List<Integer> getPanelesCoordenadaX() {
		if (panelesCoordenadaX == null) {
			panelesCoordenadaX = new ArrayList<Integer>();
		}
		return panelesCoordenadaX;
	}
	
	/**
	 * Crea un nuevo panel para utilizar como fila
	 * @return Nuevo panel
	 */
	private static JPanel createNewRow() {
		JPanel panel = new JPanel(new GridBagLayout());
		
		//Se utiliza un label dummy para la alineación izquierda :-)
		JLabel dummy = new JLabel(" ");
		dummy.setMaximumSize(new Dimension(100000,10));
		dummy.setMinimumSize(new Dimension(1,1));
		panel.add(dummy, new GridBagConstraints(MAX_FILTERS_PER_LINE, 0, 0, 0,
				0.0000001, 0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
		return panel;
	}
	
	/**
	 * Devuelve un Map con todos los paneles de los grupos de los filtros
	 * 
	 * @return <code>Map&lt;nombre grupo, JPanel&gt;</code>
	 */
	private Map<String, JPanel> getFilterGroups() {
		if (filterGroups == null) {
			filterGroups = new HashMap<String, JPanel>();
		}
		return filterGroups;
	}
	
	private FilterChangeListener filterChangeListener;
	/**
	 * Devuelve el listener que se le va a acoplar al GuiFiltro
	 * @return Listener que se le aplica a la GuiFiltro
	 */
	private FilterChangeListener getFilterChangeListener() {
		if (filterChangeListener == null) {
			filterChangeListener = new FilterChangeListener() {
				public void filterChange(FilterChangeEvent e) {
					notificar();				
				}				
			};
		}
		return filterChangeListener;
	}
}