package ar.com.fwcommon.templates.modulo.gui.filtros;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;

import ar.com.fwcommon.boss.BossEstilos;
import ar.com.fwcommon.templates.modulo.ModuloTemplate;
import ar.com.fwcommon.templates.modulo.cabecera.Cabecera;
import ar.com.fwcommon.templates.modulo.model.filtros.FiltroListaOpciones;
import ar.com.fwcommon.templates.modulo.model.filtros.FiltroRenderingInformation;

/**
 * GUI que muestra filtros del tipo Lista desplegable (ComboBox)
 * 
 *
 * @param <T> Tipo de datos que va a filtrar
 * @param <E> Elementos que se colocarán en el combo
 */
public class GuiFiltroCmb<T, E> extends GuiFiltro<T, E> {

	private static final long serialVersionUID = -6904337023014374346L;
	
	private FiltroRenderingInformation renderInfo;
	private JLabel jLabelName;
    private JComboBox jComboBox = null;

    public GuiFiltroCmb(ModuloTemplate<T, ? extends Cabecera<?>> owner, FiltroListaOpciones<T, E> filtro) {
        super(owner, filtro);
        this.renderInfo = filtro.getRenderingInformation();
        final E value = getFiltro().getValue(); 
        construct(filtro);
        fillComboData(filtro);
        setValue(value);
    }

	private void construct(FiltroListaOpciones<T, E> filtro) {
		this.setLayout(new GridBagLayout());
		this.add(getJLabelName(), new GridBagConstraints(0, 0, 1, 1, 0, 0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
						5, 0, 0), 0, 0));
        this.add(getJComboBox(), new GridBagConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.CENTER, 
        		GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 0), 0, 0));
	}
    
    /**
     * Devuelve el componente <code>JComboBox</code> utilizado en pantalla
     * @return <code>JComboBox</code> a utilizar
     */
    protected JComboBox getJComboBox() {
    	if(jComboBox == null) {
    		jComboBox = new JComboBox();
    		if (renderInfo.getMinimumSize() != null) {
    			jComboBox.setMinimumSize(renderInfo.getMinimumSize());
    			jComboBox.setPreferredSize(renderInfo.getMinimumSize());
    		}
    		if (renderInfo.getMaximumSize() != null) {
    			jComboBox.setMaximumSize(renderInfo.getMaximumSize());
    		}
    		jComboBox.addItemListener(new CmbItemListener());
    	}
    	return jComboBox;
    }

    /**
     * Devuelve el componente <code>JLabel</code> utilizado en pantalla 
     * @return <code>JLabel</code> para mostrar el nombre
     */
    protected JLabel getJLabelName() {
    	if (jLabelName == null) {
    		jLabelName = new JLabel();
    		String name = getFiltro().getNombre();
    		if (name != null && name.length() > 0) {
    			jLabelName.setText(name + ":");
    			jLabelName.setVisible(true);
    		} else {
    			jLabelName.setVisible(false);
    		}    		
    		jLabelName.setFont(BossEstilos.getDefaultFont());
    	}
    	return jLabelName;
    }
    
    /**
     * Carga el combo con los datos del modelo
     * 
     * @param filtro Modelo de donde obtener los datos
     */
	private void fillComboData(FiltroListaOpciones<T, E> filtro) {
		this.getJComboBox().removeAllItems();
		if (filtro.hasTodosOption() && filtro.getStringTODOS() != null) {
			this.getJComboBox().addItem(filtro.getStringTODOS());
		}
        for (E element : filtro.getValoresSeleccionables()) {
        	this.getJComboBox().addItem(element);	
        }
	}

    /*
	 * (non-Javadoc)
	 * ar.com.fwcommon.templates.modulo.gui.filtros.GuiFiltro#getFiltro()
	 */
	@Override
	public FiltroListaOpciones<T, E> getFiltro() {
		return (FiltroListaOpciones<T, E>)super.getFiltro();
	}

	/*
	 * (non-Javadoc)
	 * ar.com.fwcommon.templates.modulo.gui.filtros.GuiFiltro#refreshData()
	 */
	@Override
	protected void refreshData() {
		Object selectedObject = getJComboBox().getSelectedItem();
		fillComboData(getFiltro());
		getJComboBox().setSelectedItem(selectedObject);
	}
	
	/*
	 * (non-Javadoc)
	 * ar.com.fwcommon.templates.modulo.gui.filtros.GuiFiltro#refreshName()
	 */
	@Override
	protected void refreshName() {
		String name = getFiltro().getNombre();
		if (name != null && name.length() > 0) {
			getJLabelName().setText(name + ":");
			getJLabelName().setVisible(true);
		} else {
			getJLabelName().setVisible(false);
		}    		
	}
	
	/* (non-Javadoc)
	 * ar.com.fwcommon.templates.modulo.gui.filtros.GuiFiltro#setValue(java.lang.Object)
	 */
	@Override
	protected void setValue(E value) {
		if (value != null) {
			getJComboBox().setSelectedItem(value);
		} else if (getFiltro().hasTodosOption() && getFiltro().getStringTODOS() != null) {
			getJComboBox().setSelectedItem(getFiltro().getStringTODOS());
		} else {
			getJComboBox().setSelectedItem(null);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * ar.com.fwcommon.templates.modulo.gui.filtros.GuiFiltro#canGrow()
	 */
	@Override
	boolean canGrow() {
		if (getFiltro().getRenderingInformation().isAjustable() != null)
			return getFiltro().getRenderingInformation().isAjustable();
		return true;
	}
	
	/**
	 * Notifica a los listeners que se produjo un cambio en el filtro.<br>
	 * Esta función toma en cuenta el valor especial todos.
	 * @param value Valor que toma el filtro
	 */
	@Override
	@SuppressWarnings("unchecked")
	protected void fireFilterChangeListener(Object value) {
		if (getFiltro().hasTodosOption() && getFiltro().getStringTODOS() != null 
				&& getFiltro().getStringTODOS().equals(getJComboBox().getSelectedItem())) {
			
			getFiltro().setInternalTodosSelected(true);
			super.fireFilterChangeListener(null);
			
		} else {
			getFiltro().setInternalTodosSelected(false);
			super.fireFilterChangeListener((E)getJComboBox().getSelectedItem());
		}
	}


	private class CmbItemListener implements ItemListener {
		public void itemStateChanged(ItemEvent ie) {
			if(ie.getStateChange() == ItemEvent.SELECTED) {
				fireFilterChangeListener(getJComboBox().getSelectedItem());
			}
		}
	}

}