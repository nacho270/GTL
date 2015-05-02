package ar.com.textillevel.gui.modulos.personal.abm.empleados;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.CLJTextField;
import ar.clarin.fwjava.util.GuiUtil;
import ar.com.textillevel.entidades.gente.InfoDireccion;
import ar.com.textillevel.modulos.personal.entidades.legajos.Referencia;

public class JDialogAgregarModificarReferencia extends JDialog {

	private static final long serialVersionUID = 2321649030764682533L;

	private PanelDomicilioCompleto panelDomicilio;
	private CLJTextField txtReferencia;

	private JButton btnAceptar;
	private JButton btnCancelar;

	private final Frame padre;

	private boolean acepto = false;

	private Referencia referencia;

	public JDialogAgregarModificarReferencia(Frame padre) {
		super(padre);
		this.padre = padre;
		setReferencia(new Referencia());
		setUpComponentes();
		setUpScreen();
	}
	
	public JDialogAgregarModificarReferencia(Frame padre, Referencia referencia) {
		super(padre);
		this.padre = padre;
		this.referencia = referencia;
		setUpComponentes();
		setUpScreen();
		loadData();
	}
	
	private void loadData() {
		getTxtReferencia().setText(getReferencia().getNombre());
		getPanelDomicilio().setDatos(getReferencia().getDomicilio());
	}

	private void setUpScreen() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("Agregar/Modificar referencia");
		setModal(true);
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
		panel.add(new JLabel("Referencia: "));
		panel.add(getTxtReferencia());
		return panel;
	}
	
	private JPanel getPanelSur(){
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panel.add(getBtnAceptar());
		panel.add(getBtnCancelar());
		return panel;
	}

	public boolean isAcepto() {
		return acepto;
	}

	public void setAcepto(boolean acepto) {
		this.acepto = acepto;
	}

	public PanelDomicilioCompleto getPanelDomicilio() {
		if(panelDomicilio == null){
			panelDomicilio = new PanelDomicilioCompleto(padre);
		}
		return panelDomicilio;
	}

	public CLJTextField getTxtReferencia() {
		if(txtReferencia == null){
			txtReferencia = new CLJTextField();
			txtReferencia.setPreferredSize(new Dimension(240, 20));
		}
		return txtReferencia;
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
		getReferencia().setNombre(getTxtReferencia().getText().trim().toUpperCase());
		InfoDireccion infoDireccion = getPanelDomicilio().getPanelDomicilio().getPanelDireccion().getInfoDireccion();
		getReferencia().getDomicilio().setCalle(infoDireccion.getDireccion());
		getReferencia().getDomicilio().setInfoLocalidad(infoDireccion.getLocalidad());
		getReferencia().getDomicilio().setDepartamento(getPanelDomicilio().getTxtDto().getText().trim().toUpperCase());
		getReferencia().getDomicilio().setNumero(getPanelDomicilio().getTxtNumero().getValue());
		getReferencia().getDomicilio().setPiso(getPanelDomicilio().getTxtPiso().getText().trim().toUpperCase());
		getReferencia().getDomicilio().setTelefono(getPanelDomicilio().getPanelDomicilio().getPanelTelefono().getDatos());
	}

	private boolean validar() {
		if(getTxtReferencia().getText().trim().length()==0){
			CLJOptionPane.showErrorMessage(this, "Debe completar el nombre de la referencia", "Error");
			getTxtReferencia().requestFocus();
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


	public Referencia getReferencia() {
		return referencia;
	}

	
	public void setReferencia(Referencia referencia) {
		this.referencia = referencia;
	}
}
