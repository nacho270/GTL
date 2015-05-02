package ar.com.textillevel.gui.modulos.personal.modulos.legajos.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;

import ar.clarin.fwjava.componentes.CLJLetterTextField;
import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.util.GuiUtil;

public class JDialogInputFirmante extends JDialog {

	private static final long serialVersionUID = 6608151259865894905L;

	private JComboBox cmbPrefijo;
	private CLJLetterTextField txtNombre;

	private JButton btnAceptar;
	private JButton btnCancelar;
	
	private String resultado;
	
	public JDialogInputFirmante(Frame padre) {
		super(padre);
		setUpComponentes();
		setUpScreen();
		getTxtNombre().requestFocus();
	}

	private void setUpScreen() {
		setTitle("Ingrese el firmante");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setModal(true);
		pack();
		GuiUtil.centrar(this);
	}

	private void setUpComponentes() {
		add(getPanelCentral(),BorderLayout.CENTER);
		add(getPanelSur(),BorderLayout.SOUTH);
	}
	
	private JPanel getPanelCentral(){
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panel.add(getCmbPrefijo());
		panel.add(getTxtNombre());
		return panel;
	}
	
	private JPanel getPanelSur(){
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panel.add(getBtnAceptar());
		panel.add(getBtnCancelar());
		return panel;
	}

	public JComboBox getCmbPrefijo() {
		if(cmbPrefijo==null){
			cmbPrefijo = new JComboBox();
			GuiUtil.llenarCombo(cmbPrefijo, Arrays.asList(EPrefijo.values()), true);
		}
		return cmbPrefijo;
	}

	public CLJLetterTextField getTxtNombre() {
		if(txtNombre == null){
			txtNombre = new CLJLetterTextField(30);
			txtNombre.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					getBtnAceptar().doClick();
				}
			});
		}
		return txtNombre;
	}
	
	private enum EPrefijo{
		SR("Sr. "),
		SRA("Sra. "),
		SRTA("Srta. ");
		
		private EPrefijo(String desc){
			this.descripcion = desc;
		}
		
		private String descripcion;
		
		@Override
		public String toString(){
			return descripcion;
		}
	}
	
	public JButton getBtnAceptar() {
		if(btnAceptar == null){
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(getTxtNombre().getText().trim().length()>0){
						setResultado(((EPrefijo)getCmbPrefijo().getSelectedItem()).descripcion+""+getTxtNombre().getText().trim());
						dispose();
					}else{
						CLJOptionPane.showErrorMessage(JDialogInputFirmante.this, "Debe ingresar el nombre del firmante", "Error");
						getTxtNombre().requestFocus();
						return;
					}
				}
			});
		}
		return btnAceptar;
	}
	
	public JButton getBtnCancelar() {
		if(btnCancelar == null){
			btnCancelar = new JButton("Cancelar");
			btnCancelar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setResultado(null);
					dispose();
				}
			});
		}
		return btnCancelar;
	}
	
	public static void main(String[]args){
		new JDialogInputFirmante(null).setVisible(true);
	}
	
	public String getResultado() {
		return resultado;
	}
	
	public void setResultado(String resultado) {
		this.resultado = resultado;
	}
}
