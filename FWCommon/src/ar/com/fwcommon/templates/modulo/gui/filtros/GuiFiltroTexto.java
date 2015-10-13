package ar.com.fwcommon.templates.modulo.gui.filtros;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import ar.com.fwcommon.boss.BossEstilos;
import ar.com.fwcommon.componentes.FWJTextField;
import ar.com.fwcommon.templates.modulo.ModuloTemplate;
import ar.com.fwcommon.templates.modulo.cabecera.Cabecera;
import ar.com.fwcommon.templates.modulo.model.filtros.FiltroRenderingInformation;
import ar.com.fwcommon.templates.modulo.model.filtros.FiltroTexto;

/**
 * GUI que muestra filtros de cajas de texto
 * 
 * 
 *
 * @param <T> Tipo de datos que va a filtrar
 */
@SuppressWarnings("serial")
public class GuiFiltroTexto<T> extends GuiFiltro<T, String> {
	private static final boolean FILTRO_INCREMENTAL = false;
	private FiltroRenderingInformation renderInfo;
	private JLabel jLabelName;
    private JTextField jTextField;

    public GuiFiltroTexto(ModuloTemplate<T, ? extends Cabecera<?>> owner, FiltroTexto<T> filtro) {
        super(owner, filtro);
        this.renderInfo = filtro.getRenderingInformation();
        construct(filtro);
        setValue(getFiltro().getValue());
    }

	private void construct(FiltroTexto<T> filtro) {
		this.setLayout(new GridBagLayout());
		this.add(getJLabelName(), 
				new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.WEST, 
						GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
        this.add(getJTextField(), 
        		new GridBagConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.CENTER, 
        				GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 0), 0, 0));
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
				getJLabelName().setText(name + ":");
				getJLabelName().setVisible(true);
			} else {
				getJLabelName().setVisible(false);
			}
			jLabelName.setFont(BossEstilos.getDefaultFont());
		}
		return jLabelName;
	}
    /**
     * Devuelve el componente de texto utilizado para ingresar el texto
     * @return Componente de texto
     */
    protected JTextField getJTextField() {
    	if (jTextField == null) {
	    	jTextField = new FWJTextField(getFiltro().getLongitud());
	    	if (renderInfo.getMinimumSize() != null) {
	    		jTextField.setMinimumSize(renderInfo.getMinimumSize());
	    		jTextField.setPreferredSize(renderInfo.getMinimumSize());
	    	} 
	    	if (renderInfo.getMaximumSize() != null) {
	    		jTextField.setMaximumSize(renderInfo.getMaximumSize());	    		
	    	}
	    	
	    	if (FILTRO_INCREMENTAL) {
		    	//Filtro Incremental
		    	jTextField.getDocument().addDocumentListener(new DocumentListener(){
					public void changedUpdate(DocumentEvent e) {}
	
					public void insertUpdate(DocumentEvent e) {
						fireFilterChangeListener(getValorSeleccionado());
					}
	
					public void removeUpdate(DocumentEvent e) {
						fireFilterChangeListener(getValorSeleccionado());
					}
		    		
		    	});
	    	} else {
	    		//Cuando presiona Enter
	    		jTextField.addKeyListener(new KeyAdapter() {
	    			@Override
					public void keyPressed(KeyEvent e) {
						if (e.getKeyCode() == KeyEvent.VK_ENTER) {
							fireFilterChangeListener(getValorSeleccionado());
						}				
					}				
				});
	    		//Cuando el componente pierde el foco
	    		jTextField.addFocusListener(new FocusAdapter() {	    			
					@Override
					public void focusLost(FocusEvent e) {
						fireFilterChangeListener(getValorSeleccionado());
					}
				});
	    	}
    	}
        return jTextField;
    }

	protected String getValorSeleccionado() {
        String valor = getJTextField().getText();
        if (valor.length() == 0) return null;
        if (getFiltro().getValue() != null && valor.equals(getFiltro().getValue())) return getFiltro().getValue();
        return valor;
    }

	/*
	 * (non-Javadoc)
	 * ar.com.fwcommon.templates.modulo.gui.filtros.GuiFiltro#getFiltro()
	 */
	@Override
	public FiltroTexto<T> getFiltro() {
		return (FiltroTexto<T>)super.getFiltro();
	}

	/*
	 * (non-Javadoc)
	 * ar.com.fwcommon.templates.modulo.gui.filtros.GuiFiltro#refreshData()
	 */
	@Override
	protected void refreshData() {}

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
	protected void setValue(String value) {
		if (value != null) {
			getJTextField().setText(value);
		} else {
			getJTextField().setText("");
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


}