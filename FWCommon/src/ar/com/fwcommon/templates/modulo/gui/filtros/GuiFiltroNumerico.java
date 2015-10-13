package ar.com.fwcommon.templates.modulo.gui.filtros;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.math.BigDecimal;
import java.text.ParseException;

import javax.swing.JLabel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import ar.com.fwcommon.boss.BossEstilos;
import ar.com.fwcommon.componentes.JNumericTextField;
import ar.com.fwcommon.templates.modulo.ModuloTemplate;
import ar.com.fwcommon.templates.modulo.cabecera.Cabecera;
import ar.com.fwcommon.templates.modulo.model.filtros.FiltroNumerico;
import ar.com.fwcommon.templates.modulo.model.filtros.FiltroRenderingInformation;

/**
 * GUI que muestra filtros de cajas de texto que solo permite el ingreso de números
 * 
 * 
 *
 * @param <T> Tipo de datos que va a filtrar
 */
@SuppressWarnings("serial")
public class GuiFiltroNumerico<T> extends GuiFiltro<T, Long> {
	private static final boolean FILTRO_INCREMENTAL = false;
	private FiltroRenderingInformation renderInfo;
	private JLabel jLabelName;
    private JNumericTextField jNumericTextField;

    public GuiFiltroNumerico(ModuloTemplate<T, ? extends Cabecera> owner, FiltroNumerico<T> filtro) {
        super(owner, filtro);
        this.renderInfo = filtro.getRenderingInformation();
        construct(filtro);
        setValue(getFiltro().getValue());
    }

	private void construct(FiltroNumerico<T> filtro) {
		this.setLayout(new GridBagLayout());
		this.add(getJLabelName(), 
				new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.WEST, 
						GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
        this.add(getJNumericTextField(), 
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
    protected JNumericTextField getJNumericTextField() {
    	if (jNumericTextField == null) {
	    	jNumericTextField = new JNumericTextField(JNumericTextField.INTEGER_DECIMAL_FORMAT);
	    	jNumericTextField.setNullAllowed(true);
	    	if (getFiltro().getMaximo() != null) jNumericTextField.setMaximumValue(new BigDecimal(getFiltro().getMaximo()));
	    	if (getFiltro().getMinimo() != null) jNumericTextField.setMinimumValue(new BigDecimal(getFiltro().getMinimo()));
	    	if (renderInfo.getMinimumSize() != null) {
	    		jNumericTextField.setMinimumSize(renderInfo.getMinimumSize());
	    		jNumericTextField.setPreferredSize(renderInfo.getMinimumSize());
	    	} 
	    	if (renderInfo.getMaximumSize() != null) {
	    		jNumericTextField.setMaximumSize(renderInfo.getMaximumSize());	    		
	    	}
	    	
	    	if (FILTRO_INCREMENTAL) {
		    	//Filtro Incremental
		    	jNumericTextField.getDocument().addDocumentListener(new DocumentListener(){
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
	    		jNumericTextField.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						fireFilterChangeListener(getValorSeleccionado());
					}				
				});
	    		//Cuando el componente pierde el foco
	    		jNumericTextField.addFocusListener(new FocusAdapter() {	    			
					@Override
					public void focusLost(FocusEvent e) {
						fireFilterChangeListener(getValorSeleccionado());
					}
				});
	    	}
    	}
        return jNumericTextField;
    }

	protected Long getValorSeleccionado() {
		try {
			Long value = getJNumericTextField().getLongValue();
			if (value != null && getFiltro().getValue() != null && value.equals(getFiltro().getValue())) return getFiltro().getValue();
			return value;
		} catch (ParseException e) {}
		return null;
    }

	/*
	 * (non-Javadoc)
	 * ar.com.fwcommon.templates.modulo.gui.filtros.GuiFiltro#getFiltro()
	 */
	@Override
	public FiltroNumerico<T> getFiltro() {
		return (FiltroNumerico<T>)super.getFiltro();
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
	protected void setValue(Long value) {
		if (value != null) {
			getJNumericTextField().setValue(value);
		} else {
			getJNumericTextField().setText("");
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