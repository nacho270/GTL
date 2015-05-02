package ar.com.textillevel.gui.modulos.personal.modulos.legajos.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import main.GTLGlobalCache;
import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.error.validaciones.ValidacionException;
import ar.clarin.fwjava.util.GuiUtil;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.controles.PanelDatePicker;
import ar.com.textillevel.modulos.personal.entidades.legajos.sanciones.Apercibimiento;
import ar.com.textillevel.modulos.personal.facade.api.remote.SancionFacadeRemote;
import ar.com.textillevel.modulos.personal.utils.GTLPersonalBeanFactory;

public class JDialogApercibimiento extends JDialog {

	private static final long serialVersionUID = 1L;

	private PanelDatePicker fechaApercibimiento;
	private JTextArea txtMotivo;
	private JTextArea txtObservaciones;
	
	private Frame padre;
	private JButton btnAceptar;
	private JButton btnSalir;
	private JPanel panelBotones;
	private JPanel panelGeneral;
	
	private Apercibimiento apercibimiento;
	private boolean acepto;
	
	private SancionFacadeRemote sancionFacade;

	public JDialogApercibimiento(Frame padre, Apercibimiento apercibimiento) {
		super(padre);
		setPadre(padre);
		setUpComponentes();
		setApercibimiento(apercibimiento);
		setUpScreen();
	}

	private void setUpScreen() {
		setSize(new Dimension(440, 330));
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		setTitle("Ingreso de Apercibimiento");
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
			panelGeneral.add(new JLabel(""), GenericUtils.createGridBagConstraints(0, 0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 10, 0, 5), 1, 1, 0, 0));
			panelGeneral.add(getFechaApercibimiento(),  GenericUtils.createGridBagConstraints(1, 0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 5), 1, 1, 0, 0));
			panelGeneral.add(new JLabel("Motivo: "),  GenericUtils.createGridBagConstraints(0, 1,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelGeneral.add(getTxtMotivo(), GenericUtils.createGridBagConstraints(1, 1,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 1, 1, 1, 0.5));
			panelGeneral.add(new JLabel("Observaciones: "), GenericUtils.createGridBagConstraints(0, 2,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelGeneral.add(getTxtObservaciones(),  GenericUtils.createGridBagConstraints(1, 2,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 1, 1, 1, 0.5));
		}
		return panelGeneral;
	}
	
	public Apercibimiento getApercibimiento() {
		return apercibimiento;
	}

	private void setApercibimiento(Apercibimiento apercibimiento) {
		this.apercibimiento = apercibimiento;
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
					grabarApercibimiento();
				}
			});
		}
		return btnAceptar;
	}

	private void grabarApercibimiento() {
		if(validar()){
			capturarDatos();
			try {
				Apercibimiento apercSaved = getSancionFacade().ingresarApercibimiento(getApercibimiento(), GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName());
				setApercibimiento(apercSaved);
			} catch (ValidacionException e) {
				CLJOptionPane.showErrorMessage(JDialogApercibimiento.this, e.getMensajeError(), "Error");
				setApercibimiento(null);
				setAcepto(false);
				dispose();
				return;
			}
			setAcepto(true);
			dispose();
		}
	}

	private SancionFacadeRemote getSancionFacade() {
		if(sancionFacade == null) {
			sancionFacade = GTLPersonalBeanFactory.getInstance().getBean2(SancionFacadeRemote.class);
		}
		return sancionFacade;
	}

	private boolean validar() {
		if(getTxtMotivo().getText().trim().length() == 0){
			CLJOptionPane.showErrorMessage(this, "Debe ingresar el motivo", "Error");
			getTxtMotivo().requestFocus();
			return false;
		}

		if(getFechaApercibimiento().getDate()==null){
			CLJOptionPane.showErrorMessage(this, "Debe ingresar la fecha", "Error");
			getFechaApercibimiento().requestFocus();
			return false;
		}
		return true;
	}

	private void capturarDatos() {
		getApercibimiento().setFechaSancion(new java.sql.Date(getFechaApercibimiento().getDate().getTime()));
		getApercibimiento().setMotivo(getTxtMotivo().getText().trim());
		getApercibimiento().setObservaciones(getTxtObservaciones().getText().trim());
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

	private JTextArea getTxtMotivo() {
		if (txtMotivo == null) {
			txtMotivo = new JTextArea();
			if(getApercibimiento()!=null){
				txtMotivo.setText(getApercibimiento().getMotivo());
			}
		}
		return txtMotivo;
	}
	
	private JTextArea getTxtObservaciones() {
		if (txtObservaciones == null) {
			txtObservaciones = new JTextArea();
			if(getApercibimiento()!=null){
				txtObservaciones.setText(getApercibimiento().getObservaciones());
			}
		}
		return txtObservaciones;
	}
	
	private PanelDatePicker getFechaApercibimiento() {
		if (fechaApercibimiento == null) {
			fechaApercibimiento = new PanelDatePicker();
			fechaApercibimiento.setCaption("Fecha:");
			if(getApercibimiento()!=null){
				fechaApercibimiento.setSelectedDate(getApercibimiento().getFechaSancion());
			}
		}
		return fechaApercibimiento;
	}
	
}