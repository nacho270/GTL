package ar.com.textillevel.gui.modulos.personal.modulos.legajos.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;

import main.GTLGlobalCache;
import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.error.validaciones.ValidacionException;
import ar.clarin.fwjava.util.GuiUtil;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.controles.PanelDatePicker;
import ar.com.textillevel.modulos.personal.entidades.legajos.valesdeatencion.ValePreocupacional;
import ar.com.textillevel.modulos.personal.facade.api.remote.ValeAtencionFacadeRemote;
import ar.com.textillevel.modulos.personal.utils.GTLPersonalBeanFactory;

public class JDialogValePreocupacional extends JDialog {

	private static final long serialVersionUID = 1L;

	private PanelDatePicker fechaVale;
	private JTextArea txtObservaciones;

	private Frame padre;
	private JButton btnAceptar;
	private JButton btnSalir;
	private JPanel panelBotones;
	private JPanel panelGeneral;

	private JCheckBox chkAsistioAlTrabajo;
	private JPanel panHorarioIngreso;
	private JSpinner jsHorasIngreso;
	private JSpinner jsMinutosIngreso;

	private ValePreocupacional valePreocupacional;
	private boolean acepto;
	
	private ValeAtencionFacadeRemote valeAtencionFacade;

	public JDialogValePreocupacional(Frame padre, ValePreocupacional valePreocupacional) {
		super(padre);
		setPadre(padre);
		setUpComponentes();
		setValePreocupacional(valePreocupacional);
		setUpScreen();
	}

	private void setUpScreen() {
		setSize(new Dimension(460, 330));
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		setTitle("Ingreso de Vale Preocupacional");
		GuiUtil.centrar(this);
		setResizable(true);
		setModal(true);
	}

	private void setUpComponentes() {
		addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				salir();
			}
		});
		
		add(getPanelGeneral(), BorderLayout.CENTER);
		add(getPanelBotones(),BorderLayout.SOUTH);
	}

	private JPanel getPanelBotones(){
		if(panelBotones == null){
			panelBotones = new JPanel();
			panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 2));
			panelBotones.add(getBtnAceptar());
			panelBotones.add(getBtnSalir());
		}
		return panelBotones;
	}
	
	private JPanel getPanelGeneral(){
		if(panelGeneral == null){
			panelGeneral = new JPanel();
			panelGeneral.setLayout(new GridBagLayout());
			panelGeneral.add(getFechaVale(),  GenericUtils.createGridBagConstraints(0, 0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 5), 1, 1, 0, 0));
			panelGeneral.add(getTxtObservaciones(), GenericUtils.createGridBagConstraints(0, 1,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 2, 1, 1, 0.5));
			panelGeneral.add(getChkAsistioAlTrabajo(), GenericUtils.createGridBagConstraints(0, 2,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 2, 1, 0, 0));
			panelGeneral.add(getPanHorarioIngreso(),  GenericUtils.createGridBagConstraints(0, 3,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
		}
		return panelGeneral;
	}

	public ValePreocupacional getValePreocupacional() {
		return valePreocupacional;
	}

	private void setValePreocupacional(ValePreocupacional valePreocupacional) {
		this.valePreocupacional = valePreocupacional;
	}

	public boolean isAcepto() {
		return acepto;
	}

	public void setAcepto(boolean acepto) {
		this.acepto = acepto;
	}

	public Frame getPadre() {
		return padre;
	}

	public void setPadre(Frame padre) {
		this.padre = padre;
	}

	private JButton getBtnAceptar() {
		if (btnAceptar == null) {
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					grabarValeEnfermedad();
				}
			});
		}
		return btnAceptar;
	}

	private void grabarValeEnfermedad() {
		if(validar()){
			capturarDatos();
			try {
				ValePreocupacional valePreSaved = getValeAtencionFacade().ingresarValePreocupacional(getValePreocupacional(), GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName());
				setValePreocupacional(valePreSaved);
			} catch (ValidacionException e) {
				CLJOptionPane.showErrorMessage(JDialogValePreocupacional.this, e.getMensajeError(), "Error");
				setValePreocupacional(null);
				setAcepto(false);
				dispose();
				return;
			}
			setAcepto(true);
			dispose();
		}
	}

	private ValeAtencionFacadeRemote getValeAtencionFacade() {
		if(valeAtencionFacade == null) {
			valeAtencionFacade = GTLPersonalBeanFactory.getInstance().getBean2(ValeAtencionFacadeRemote.class);
		}
		return valeAtencionFacade;
	}

	private boolean validar() {
		if(getFechaVale().getDate()==null){
			CLJOptionPane.showErrorMessage(this, "Debe ingresar la fecha del vale.", "Error");
			getFechaVale().requestFocus();
			return false;
		}
		return true;
	}

	private void capturarDatos() {
		getValePreocupacional().setFechaVale(new java.sql.Date(getFechaVale().getDate().getTime()));
		getValePreocupacional().setAsistioAlTrabajo(getChkAsistioAlTrabajo().isSelected());
		if(getChkAsistioAlTrabajo().isSelected()) {
			getValePreocupacional().getHorarioIngreso().setHoras((Integer)getJsHorasIngreso().getValue());
			getValePreocupacional().getHorarioIngreso().setMinutos((Integer)getJsMinutosIngreso().getValue());
			getValePreocupacional().setAsistioAlTrabajo(true);
		}
	}

	private JButton getBtnSalir() {
		if (btnSalir == null) {
			btnSalir = new JButton("Salir");
			btnSalir.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					salir();
				}
			});
		}
		return btnSalir;
	}

	private void salir() {
		setAcepto(false);
		dispose();
	}

	private JPanel getPanHorarioIngreso() {
		if(panHorarioIngreso == null) {
			panHorarioIngreso = new JPanel(new GridBagLayout());
			panHorarioIngreso.add(new JLabel("Horario de Ingreso: "), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
			panHorarioIngreso.add(getJsHorasIngreso(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 0));
			panHorarioIngreso.add(new JLabel(":"), GenericUtils.createGridBagConstraints(2, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));			
			panHorarioIngreso.add(getJsMinutosIngreso(), GenericUtils.createGridBagConstraints(3, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		}
		return panHorarioIngreso;
	}

	private JSpinner getJsHorasIngreso() {
		if (jsHorasIngreso == null) {
			jsHorasIngreso = new JSpinner(new SpinnerNumberModel(0, 0, 23, 1));
			jsHorasIngreso.setEditor(new JSpinner.NumberEditor(jsHorasIngreso));
			jsHorasIngreso.setPreferredSize(new Dimension(40, 23));
			JFormattedTextField ftf = ((JSpinner.DefaultEditor) jsHorasIngreso.getEditor()).getTextField();
			ftf.setEditable(true);
			ftf.setBackground(Color.WHITE);
		}
		return jsHorasIngreso;
	}

	private JSpinner getJsMinutosIngreso() {
		if (jsMinutosIngreso == null) {
			jsMinutosIngreso = new JSpinner(new SpinnerNumberModel(0, 0, 59, 1));
			jsMinutosIngreso.setEditor(new JSpinner.NumberEditor(jsMinutosIngreso));
			jsMinutosIngreso.setPreferredSize(new Dimension(40, 23));
			JFormattedTextField ftf = ((JSpinner.DefaultEditor) jsMinutosIngreso.getEditor()).getTextField();
			ftf.setEditable(true);
			ftf.setBackground(Color.WHITE);
		}
		return jsMinutosIngreso;
	}

	private JCheckBox getChkAsistioAlTrabajo() {
		if(chkAsistioAlTrabajo == null) {
			chkAsistioAlTrabajo = new JCheckBox("Asistió al trabajo");
			chkAsistioAlTrabajo.setSelected(true);
			chkAsistioAlTrabajo.addItemListener(new ItemListener() {

				public void itemStateChanged(ItemEvent e) {
					GuiUtil.setEstadoPanel(getPanHorarioIngreso(), e.getStateChange() == ItemEvent.SELECTED);
				}

			});
		}
		return chkAsistioAlTrabajo;
	}
	
	private JTextArea getTxtObservaciones() {
		if (txtObservaciones == null) {
			txtObservaciones = new JTextArea();
			txtObservaciones.setBorder(BorderFactory.createTitledBorder("Observaciones"));
			if(getValePreocupacional()!=null){
				txtObservaciones.setText(getValePreocupacional().getObservaciones());
			}
		}
		return txtObservaciones;
	}

	private PanelDatePicker getFechaVale() {
		if (fechaVale == null) {
			fechaVale = new PanelDatePicker();
			fechaVale.setCaption("Fecha:");
			if(getValePreocupacional()!=null){
				fechaVale.setSelectedDate(getValePreocupacional().getFechaVale());
			}
		}
		return fechaVale;
	}
	
}