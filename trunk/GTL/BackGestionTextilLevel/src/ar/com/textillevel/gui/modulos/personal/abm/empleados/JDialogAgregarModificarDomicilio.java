package ar.com.textillevel.gui.modulos.personal.abm.empleados;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.util.GuiUtil;
import ar.com.textillevel.entidades.gente.InfoDireccion;
import ar.com.textillevel.gui.util.controles.PanelDatePicker;
import ar.com.textillevel.modulos.personal.entidades.legajos.domicilio.InfoDomicilio;

public class JDialogAgregarModificarDomicilio extends JDialog {

	private static final long serialVersionUID = 4922757376415094464L;

	private PanelDomicilioCompleto panelDomicilio;
	private PanelDatePicker panelFecha;

	private JButton btnAceptar;
	private JButton btnCancelar;
	
	private final Frame padre;

	private boolean acepto = false;
	
	private InfoDomicilio infoDomicilio;
	
	public JDialogAgregarModificarDomicilio(Frame owner) throws HeadlessException {
		super(owner);
		this.padre = owner;
		setUpComponentes();
		setUpScreen();
		setInfoDomicilio(new InfoDomicilio());
	}

	public JDialogAgregarModificarDomicilio(Frame frame, InfoDomicilio info) {
		super(frame);
		this.padre = frame;
		setInfoDomicilio(info);
		setUpComponentes();
		setUpScreen();
		loadInfo();
	}

	private void loadInfo() {
		getPanelFecha().setSelectedDate(getInfoDomicilio().getFecha());
		getPanelDomicilio().setDatos(getInfoDomicilio().getDomicilio());
	}

	private void setUpScreen() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("Agregar/Modificar domicilio");
		setModal(true);
		setResizable(false);
		setSize(new Dimension(600, 270));
		GuiUtil.centrar(this);
	}

	private void setUpComponentes() {
		add(getPanelNorte(),BorderLayout.NORTH);
		add(getPanelDomicilio(), BorderLayout.CENTER);
		add(getPanelSur(),BorderLayout.SOUTH);
	}
	
	private JPanel getPanelNorte(){
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
		panel.add(getPanelFecha());
		return panel;
	}
	
	private JPanel getPanelSur(){
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panel.add(getBtnAceptar());
		panel.add(getBtnCancelar());
		return panel;
	}

	public PanelDatePicker getPanelFecha() {
		if(panelFecha == null){
			panelFecha = new PanelDatePicker();
			panelFecha.setCaption("Fecha: " );
		}
		return panelFecha;
	}
	
	public JButton getBtnAceptar() {
		if(btnAceptar == null){
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(validar()){
						capturarDatos();
						setAcepto(true);
						dispose();
					}
				}
			});
		}
		return btnAceptar;
	}

	private void capturarDatos() {
		getInfoDomicilio().setFecha( new java.sql.Date(getPanelFecha().getDate().getTime()));
		InfoDireccion infoDireccion = getPanelDomicilio().getPanelDomicilio().getPanelDireccion().getInfoDireccion();
		getInfoDomicilio().getDomicilio().setCalle(infoDireccion.getDireccion());
		getInfoDomicilio().getDomicilio().setInfoLocalidad(infoDireccion.getLocalidad());
		getInfoDomicilio().getDomicilio().setDepartamento(getPanelDomicilio().getTxtDto().getText().trim().toUpperCase());
		getInfoDomicilio().getDomicilio().setNumero(getPanelDomicilio().getTxtNumero().getValue());
		getInfoDomicilio().getDomicilio().setPiso(getPanelDomicilio().getTxtPiso().getText().trim().toUpperCase());
		getInfoDomicilio().getDomicilio().setTelefono(getPanelDomicilio().getPanelDomicilio().getPanelTelefono().getDatos());
	}

	private boolean validar() {
		if(getPanelFecha().getDate() == null){
			CLJOptionPane.showErrorMessage(this, "Debe elegir una fecha válida", "Error");
			getPanelFecha().requestFocus();
			return false;
		}
		return getPanelDomicilio().validar();
	}
	
	public JButton getBtnCancelar() {
		if(btnCancelar == null){
			btnCancelar = new JButton("Cancelar");
			btnCancelar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setAcepto(false);
					dispose();
				}
			});
		}
		return btnCancelar;
	}

	
	public boolean isAcepto() {
		return acepto;
	}

	public void setAcepto(boolean acepto) {
		this.acepto = acepto;
	}

	public InfoDomicilio getInfoDomicilio() {
		return infoDomicilio;
	}
	
	public void setInfoDomicilio(InfoDomicilio infoDomicilio) {
		this.infoDomicilio = infoDomicilio;
	}
	
	public PanelDomicilioCompleto getPanelDomicilio() {
		if(panelDomicilio == null){
			panelDomicilio = new PanelDomicilioCompleto(padre);
		}
		return panelDomicilio;
	}
}
