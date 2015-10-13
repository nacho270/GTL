package ar.com.fwcommon.templates.modulo.gui.filtros;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Date;
import java.text.SimpleDateFormat;

import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;

import ar.com.fwcommon.boss.BossEstilos;
import ar.com.fwcommon.componentes.FWBtnCalendarioBF;
import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.templates.modulo.ModuloTemplate;
import ar.com.fwcommon.templates.modulo.cabecera.Cabecera;
import ar.com.fwcommon.templates.modulo.model.filtros.FiltroRangoFechas;
import ar.com.fwcommon.templates.modulo.model.filtros.FiltroRenderingInformation;
import ar.com.fwcommon.util.DateUtil;

/**
 * GUI que muestra filtros del tipo Lista desplegable (ComboBox)
 * 
 *
 * @param <T> Tipo de datos que va a filtrar
 * @param <E> Elementos que se colocarán en el combo
 */
public class GuiFiltroRangoFechas<T, E> extends GuiFiltro<T, RangoFechas> {
	@SuppressWarnings("unused")
	private FiltroRenderingInformation renderInfo;
	private JLabel jLabelNameDesde;
	private JLabel jLabelNameHasta;
	private JCheckBox jCheckBoxFechasHabilitadas;
	private JFormattedTextField txtFechaDesde = null;
	private BotonCalendarioDesde btnCalendarioDesde = null;
	private JFormattedTextField txtFechaHasta = null;
	private BotonCalendarioHasta btnCalendarioHasta = null;
	private RangoFechas rangoSeleccionado = null;

    @SuppressWarnings("unchecked")
	public GuiFiltroRangoFechas(ModuloTemplate<T, ? extends Cabecera> owner, FiltroRangoFechas<T, RangoFechas> filtro) {
        super(owner, filtro);
        this.renderInfo = filtro.getRenderingInformation();
        final RangoFechas value = getFiltro().getValoresSeleccionables();
        construct(filtro);
        setValue(value);

     }

	private void construct(FiltroRangoFechas<T, RangoFechas> filtro) {
		this.setLayout(new GridBagLayout());
		this.add(getJCheckBoxFechasHabilitadas(), new GridBagConstraints(0, 0, 1, 1, 0, 0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
						5, 0, 0), 0, 7));
				this.add(getJLabelNameDesde(), new GridBagConstraints(1, 0, 1, 1, 0, 0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
						5, 0, 0), 0, 0));
        this.add(getTxtFechaDesde(), new GridBagConstraints(2, 0, 1, 1, 1, 0, GridBagConstraints.CENTER, 
        		GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 0), 0, 0));
        
        this.add(getBtnCalendarioDesde(), new GridBagConstraints(3, 0, 1, 1, 1, 0, GridBagConstraints.CENTER, 
        		GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 0), 0, 0));
        
        this.add(getJLabelNameHasta(), new GridBagConstraints(4, 0, 1, 1, 0, 0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
						5, 0, 0), 0, 0));
        this.add(getTxtFechaHasta(), new GridBagConstraints(5, 0, 1, 1, 1, 0, GridBagConstraints.CENTER, 
        		GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 0), 0, 0));
        
        this.add(getBtnCalendarioHasta(), new GridBagConstraints(6, 0, 1, 1, 1, 0, GridBagConstraints.CENTER, 
        		GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 0), 0, 0));
	}
    
    /**
     * Devuelve el componente <code>JLabel</code> utilizado en pantalla 
     * @return <code>JLabel</code> para mostrar el nombre
     */
    protected JLabel getJLabelNameDesde() {
    	if (jLabelNameDesde == null) {
    		jLabelNameDesde = new JLabel();
    		String name = getFiltro().getNombreLabelDesde(); 
    		if (name != null && name.length() > 0) {
    			jLabelNameDesde.setText(name + ":");
    			jLabelNameDesde.setVisible(true);
    		} else {
    			jLabelNameDesde.setVisible(false);
    		}    		
    		jLabelNameDesde.setFont(BossEstilos.getDefaultFont());
    	}
    	return jLabelNameDesde;
    }
    
    protected JLabel getJLabelNameHasta() {
    	if (jLabelNameHasta == null) {
    		jLabelNameHasta = new JLabel();
    		String name = getFiltro().getNombreLabelHasta(); 
    		if (name != null && name.length() > 0) {
    			jLabelNameHasta.setText(name + ":");
    			jLabelNameHasta.setVisible(true);
    		} else {
    			jLabelNameHasta.setVisible(false);
    		}    		
    		jLabelNameHasta.setFont(BossEstilos.getDefaultFont());
    	}
    	return jLabelNameHasta;
    }
    
    protected JCheckBox getJCheckBoxFechasHabilitadas() {
    	if (jCheckBoxFechasHabilitadas == null) {
    		jCheckBoxFechasHabilitadas = new JCheckBox("Filtrar Fechas");
    		jCheckBoxFechasHabilitadas.addItemListener(new ChkItemListener());
    	}
    	return jCheckBoxFechasHabilitadas;
	}
    /*
	 * (non-Javadoc)
	 * ar.com.fwcommon.templates.modulo.gui.filtros.GuiFiltro#getFiltro()
	 */
	@Override
	public FiltroRangoFechas<T, RangoFechas> getFiltro() {
		return (FiltroRangoFechas<T, RangoFechas>)super.getFiltro();
	}

	/*
	 * (non-Javadoc)
	 * ar.com.fwcommon.templates.modulo.gui.filtros.GuiFiltro#refreshData()
	 */
	@Override
	protected void refreshData() {
	}
	
	/*
	 * (non-Javadoc)
	 * ar.com.fwcommon.templates.modulo.gui.filtros.GuiFiltro#refreshName()
	 */
	@Override
	protected void refreshName() {
		String name = getFiltro().getNombre();
		if (name != null && name.length() > 0) {
			getJLabelNameDesde().setText(name + ":");
			getJLabelNameDesde().setVisible(true);
		} else {
			getJLabelNameDesde().setVisible(false);
		}    		
	}
	
	/* (non-Javadoc)
	 * ar.com.fwcommon.templates.modulo.gui.filtros.GuiFiltro#setValue(java.lang.Object)
	 */
	@Override
	protected void setValue(RangoFechas value) {
		if (value != null) {
			if(value.getFechaDesde() != null) {
				getTxtFechaDesde().setValue(value.getFechaDesde());
			}
			if(value.getFechaHasta() != null) {
				getTxtFechaHasta().setValue(value.getFechaHasta());
			}
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
	protected void fireFilterChangeListener(RangoFechas value) {
		if (getFiltro().getValoresSeleccionables().getFechaDesde()  != null 
				&& getFiltro().getValoresSeleccionables().getFechaDesde().equals(getTxtFechaDesde().getValue())) {
			
			super.fireFilterChangeListener(null);
			
		} else {
			super.fireFilterChangeListener(value);
		}
	}

	
	public JFormattedTextField getTxtFechaDesde() {
		if(txtFechaDesde == null) {
			txtFechaDesde = new JFormattedTextField(DateUtil.getSimpleDateFormat(DateUtil.WEEK_DAY_SHORT_DATE));
			txtFechaDesde.setEditable(false);
			txtFechaDesde.setEnabled(false);
			txtFechaDesde.setDisabledTextColor(Color.BLACK);
			txtFechaDesde.setHorizontalAlignment(JFormattedTextField.RIGHT);
			txtFechaDesde.setPreferredSize(new Dimension(130, 20));
		}
		return txtFechaDesde;
	}
	
	protected class BotonCalendarioDesde extends FWBtnCalendarioBF {
		public BotonCalendarioDesde() {
			super();
		}

		public BotonCalendarioDesde(Date fechaMinima, Date fechaMaxima) {
			super(null, null);
		}

		public void botonCalendarioPresionado() {
			getTxtFechaDesde().requestFocus();
			Date fechaDesde = getFechaDesde();
			Date fechaHasta = getFechaHasta();
			if(!fechaDesde.equals(getTxtFechaDesde().getValue())) {
				getTxtFechaDesde().setValue(fechaDesde);
				if ((fechaDesde!= null) && (fechaHasta!= null)) {
					if(fechaDesde.getTime()> fechaHasta.getTime()) {
						getTxtFechaHasta().setValue(fechaDesde);
					}
				}
				getRangoSeleccionado().setFechaDesde(fechaDesde);
				fireFilterChangeListener(getRangoSeleccionado());
			}
		}
	}
	
	
	public JFormattedTextField getTxtFechaHasta() {
		if(txtFechaHasta == null) {
			txtFechaHasta = new JFormattedTextField(DateUtil.getSimpleDateFormat(DateUtil.WEEK_DAY_SHORT_DATE));
			txtFechaHasta.setEditable(false);
			txtFechaHasta.setEnabled(false);
			txtFechaHasta.setDisabledTextColor(Color.BLACK);
			txtFechaHasta.setHorizontalAlignment(JFormattedTextField.RIGHT);
			txtFechaHasta.setPreferredSize(new Dimension(130, 20));
		}
		return txtFechaHasta;
	}
	
	
	protected class BotonCalendarioHasta extends FWBtnCalendarioBF {
		public BotonCalendarioHasta() {
			super();
		}

		public BotonCalendarioHasta(Date fechaMinima, Date fechaMaxima) {
			super(fechaMinima, fechaMaxima);
		}
		public void botonCalendarioPresionado() {
			getTxtFechaHasta().requestFocus();
			Date fechaHasta = getFechaHasta();
			if(getTxtFechaHasta().getValue() != null && !fechaHasta.equals(getTxtFechaHasta().getValue())) {
				Date fechaDesde = getFechaDesde();
				if(fechaHasta.before(fechaDesde)) {
					FWJOptionPane.showErrorMessage(GuiFiltroRangoFechas.this, "La fecha 'Hasta' debe ser mayor o igual que la fecha 'Desde'", "Validación de fechas");
					return;
				}
				getTxtFechaHasta().setValue(fechaHasta);
				getRangoSeleccionado().setFechaHasta(fechaHasta);
				fireFilterChangeListener(getRangoSeleccionado());
			} else {
				Date fechaDesde = getFechaDesde();
				if(fechaDesde.getTime() > fechaHasta.getTime()) {
					fechaHasta = fechaDesde; 
				}
				getTxtFechaHasta().setValue(fechaHasta);
				getRangoSeleccionado().setFechaHasta(fechaHasta);
				fireFilterChangeListener(getRangoSeleccionado());
			}
		}
	}

	private RangoFechas getRangoSeleccionado() {
		if (rangoSeleccionado == null) {
			rangoSeleccionado = new RangoFechas();
		}
		return rangoSeleccionado;
	}
	public BotonCalendarioDesde getBtnCalendarioDesde() {
		if(btnCalendarioDesde == null) {
				btnCalendarioDesde = new BotonCalendarioDesde();
				btnCalendarioDesde.setEnabled(false);
			}
		return btnCalendarioDesde;
	}

	public BotonCalendarioHasta getBtnCalendarioHasta() {
		if(btnCalendarioHasta == null) {
				btnCalendarioHasta = new BotonCalendarioHasta();
				btnCalendarioHasta.setEnabled(false);
		}
		return btnCalendarioHasta;
	}
	
	public Date getFechaDesde() {
		return getBtnCalendarioDesde().getSelectedDate();
	}
	
	public Date getFechaHasta() {
		return getBtnCalendarioHasta().getSelectedDate();
	}
	
	private class ChkItemListener implements ItemListener {
		public void itemStateChanged(ItemEvent ie) {
			if (!getJCheckBoxFechasHabilitadas().isSelected()) {
				getRangoSeleccionado().setFechaDesde(null);	
				getRangoSeleccionado().setFechaHasta(null);
				getTxtFechaDesde().setValue(null);
				getTxtFechaHasta().setValue(null);
				getBtnCalendarioDesde().setEnabled(false);
				getBtnCalendarioHasta().setEnabled(false);
			}  else {
				getBtnCalendarioDesde().setEnabled(true);
				getBtnCalendarioHasta().setEnabled(true);
			}
			fireFilterChangeListener(getRangoSeleccionado());
		}
	}

}