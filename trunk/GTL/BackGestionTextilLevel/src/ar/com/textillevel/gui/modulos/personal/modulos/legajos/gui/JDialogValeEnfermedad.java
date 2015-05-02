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
import java.sql.Date;

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

import org.apache.taglibs.string.util.StringW;

import main.GTLGlobalCache;
import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.error.validaciones.ValidacionException;
import ar.clarin.fwjava.util.GuiUtil;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.controles.PanelDatePicker;
import ar.com.textillevel.modulos.personal.entidades.legajos.LegajoEmpleado;
import ar.com.textillevel.modulos.personal.entidades.legajos.RangoHorario;
import ar.com.textillevel.modulos.personal.entidades.legajos.valesdeatencion.ValeEnfermedad;
import ar.com.textillevel.modulos.personal.facade.api.remote.ValeAtencionFacadeRemote;
import ar.com.textillevel.modulos.personal.utils.GTLPersonalBeanFactory;

public class JDialogValeEnfermedad extends JDialog {

	private static final long serialVersionUID = 1L;

	private PanelDatePicker fechaVale;
	private JTextArea txtDescrEnfermedad;

	private Frame padre;
	private JButton btnAceptar;
	private JButton btnSalir;
	private JPanel panelBotones;
	private JPanel panelGeneral;

	private JCheckBox chkAsistioAlTrabajo;
	private JPanel panHorarioSalida;
	private JSpinner jsHorasSalida;
	private JSpinner jsMinutosSalida;

	private ValeEnfermedad valeEnfermedad;
	private LegajoEmpleado legajo;
	private boolean acepto;
	
	private ValeAtencionFacadeRemote valeAtencionFacade;

	public JDialogValeEnfermedad(Frame padre, LegajoEmpleado legajo, ValeEnfermedad valeEnfermedad) {
		super(padre);
		this.legajo = legajo;
		setPadre(padre);
		setUpComponentes();
		setValeEnfermedad(valeEnfermedad);
		setUpScreen();
	}

	private void setUpScreen() {
		setSize(new Dimension(460, 330));
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		setTitle("Ingreso de Vale de Enfermedad");
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
			panelGeneral.add(getTxtDescrEnfermedad(), GenericUtils.createGridBagConstraints(0, 1,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 2, 1, 1, 0.5));
			panelGeneral.add(getChkAsistioAlTrabajo(), GenericUtils.createGridBagConstraints(0, 2,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 2, 1, 0, 0));
			panelGeneral.add(getPanHorarioSalida(),  GenericUtils.createGridBagConstraints(0, 3,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
		}
		return panelGeneral;
	}

	private JPanel getPanHorarioSalida() {
		if(panHorarioSalida == null) {
			panHorarioSalida = new JPanel(new GridBagLayout());
			panHorarioSalida.add(new JLabel("Horario de Salida: "), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
			panHorarioSalida.add(getJsHorasSalida(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 0));
			panHorarioSalida.add(new JLabel(":"), GenericUtils.createGridBagConstraints(2, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));			
			panHorarioSalida.add(getJsMinutosSalida(), GenericUtils.createGridBagConstraints(3, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		}
		return panHorarioSalida;
	}

	private JCheckBox getChkAsistioAlTrabajo() {
		if(chkAsistioAlTrabajo == null) {
			chkAsistioAlTrabajo = new JCheckBox("Asistió al trabajo");
			chkAsistioAlTrabajo.setSelected(true);
			chkAsistioAlTrabajo.addItemListener(new ItemListener() {

				public void itemStateChanged(ItemEvent e) {
					GuiUtil.setEstadoPanel(getPanHorarioSalida(), e.getStateChange() == ItemEvent.SELECTED);
				}

			});
		}
		return chkAsistioAlTrabajo;
	}

	private JSpinner getJsHorasSalida() {
		if (jsHorasSalida == null) {
			jsHorasSalida = new JSpinner(new SpinnerNumberModel(0, 0, 23, 1));
			jsHorasSalida.setEditor(new JSpinner.NumberEditor(jsHorasSalida));
			jsHorasSalida.setPreferredSize(new Dimension(40, 23));
			JFormattedTextField ftf = ((JSpinner.DefaultEditor) jsHorasSalida.getEditor()).getTextField();
			ftf.setEditable(true);
			ftf.setBackground(Color.WHITE);
		}
		return jsHorasSalida;
	}

	private JSpinner getJsMinutosSalida() {
		if (jsMinutosSalida == null) {
			jsMinutosSalida = new JSpinner(new SpinnerNumberModel(0, 0, 59, 1));
			jsMinutosSalida.setEditor(new JSpinner.NumberEditor(jsMinutosSalida));
			jsMinutosSalida.setPreferredSize(new Dimension(40, 23));
			JFormattedTextField ftf = ((JSpinner.DefaultEditor) jsMinutosSalida.getEditor()).getTextField();
			ftf.setEditable(true);
			ftf.setBackground(Color.WHITE);
		}
		return jsMinutosSalida;
	}

	
	public ValeEnfermedad getValeEnfermedad() {
		return valeEnfermedad;
	}

	private void setValeEnfermedad(ValeEnfermedad valeEnfermedad) {
		this.valeEnfermedad = valeEnfermedad;
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
				ValeEnfermedad valeEnfSaved = getValeAtencionFacade().ingresarValeEnfermedad(getValeEnfermedad(), GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName());
				setValeEnfermedad(valeEnfSaved);
			} catch (ValidacionException e) {
				CLJOptionPane.showErrorMessage(JDialogValeEnfermedad.this, e.getMensajeError(), "Error");
				setValeEnfermedad(null);
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
		if(getTxtDescrEnfermedad().getText().trim().length() == 0){
			CLJOptionPane.showErrorMessage(this, "Debe ingresar la descripción de la enfermedad.", "Error");
			getTxtDescrEnfermedad().requestFocus();
			return false;
		}

		if(getFechaVale().getDate()==null){
			CLJOptionPane.showErrorMessage(this, "Debe ingresar la fecha del vale.", "Error");
			getFechaVale().requestFocus();
			return false;
		}
		if(getChkAsistioAlTrabajo().isSelected()) {
			RangoHorario horario = legajo.getHorario(new Date(getFechaVale().getDate().getTime()));
			if(horario == null) {
				CLJOptionPane.showErrorMessage(this, StringW.wordWrap("No se puede ingresar el vale para esa fecha porque el empleado no tiene un horario definido para ese día."), "Error");
				return false;
			}
			if(horario.getHoraHasta() < (Integer)getJsHorasSalida().getValue()) {
				CLJOptionPane.showErrorMessage(this, StringW.wordWrap("El horario de salida debe ser menor al configurado: " + horario.toString()), "Error");
				return false;
			}
			if(horario.getHoraHasta().equals((Integer)getJsHorasSalida().getValue())  && horario.getMinutosHasta() < (Integer)getJsMinutosSalida().getValue()) {
				CLJOptionPane.showErrorMessage(this, StringW.wordWrap("El horario de salida debe ser menor al configurado: " + horario.toString()), "Error");
				return false;
			}
		}
		return true;
	}

	private void capturarDatos() {
		getValeEnfermedad().setFechaVale(new java.sql.Date(getFechaVale().getDate().getTime()));
		getValeEnfermedad().setDescrEnfermedad(getTxtDescrEnfermedad().getText().trim());
		getValeEnfermedad().setAsistioAlTrabajo(getChkAsistioAlTrabajo().isSelected());
		if(getChkAsistioAlTrabajo().isSelected()) {
			getValeEnfermedad().getHorarioSalida().setHoras((Integer)getJsHorasSalida().getValue());
			getValeEnfermedad().getHorarioSalida().setMinutos((Integer)getJsMinutosSalida().getValue());
			RangoHorario horario = legajo.getHorario(new Date(getFechaVale().getDate().getTime()));
			getValeEnfermedad().getHorarioTrabajo().setHoras(horario.getHoraHasta());
			getValeEnfermedad().getHorarioTrabajo().setMinutos(horario.getMinutosHasta());
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

	private JTextArea getTxtDescrEnfermedad() {
		if (txtDescrEnfermedad == null) {
			txtDescrEnfermedad = new JTextArea();
			txtDescrEnfermedad.setBorder(BorderFactory.createTitledBorder("Descripción de la enfermedad"));
			if(getValeEnfermedad()!=null){
				txtDescrEnfermedad.setText(getValeEnfermedad().getDescrEnfermedad());
			}
		}
		return txtDescrEnfermedad;
	}

	private PanelDatePicker getFechaVale() {
		if (fechaVale == null) {
			fechaVale = new PanelDatePicker();
			fechaVale.setCaption("Fecha:");
			if(getValeEnfermedad()!=null){
				fechaVale.setSelectedDate(getValeEnfermedad().getFechaVale());
			}
		}
		return fechaVale;
	}
	
}