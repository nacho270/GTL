package ar.com.textillevel.gui.modulos.personal.modulos.legajos.gui.wfvaleatencion;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ar.com.fwcommon.componentes.FWDateField;
import ar.com.fwcommon.util.DateUtil;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.modulos.personal.entidades.legajos.valesdeatencion.ValeAtencion;
import ar.com.textillevel.modulos.personal.entidades.legajos.valesdeatencion.ValeEnfermedad;

public class JDialogInputJustifAltaValEnfermedad extends JDialog {

	private static final long serialVersionUID = 1L;

	private FWDateField txtFecha;
	private JTextField txtDiagnostico;
	private JButton btnAceptar;
	private JButton btnCancelar;
	private ValeAtencion valeAtencion;
	private boolean acepto;
	
	public JDialogInputJustifAltaValEnfermedad(Frame padre, ValeAtencion valeAtencion) {
		super(padre);
		this.valeAtencion = valeAtencion;
		setTitle("Ingrese fecha de alta y diagnóstico");
		setUpComponentes();
		setUpScreen();
		getTxtFecha().requestFocus();
	}

	private void setUpScreen() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setModal(true);
		pack();
		GuiUtil.centrar(this);
		setSize(new Dimension(300, 200));
	}

	private void setUpComponentes() {
		add(getPanelCentral(),BorderLayout.CENTER);
		add(getPanelSur(),BorderLayout.SOUTH);
	}

	private JPanel getPanelCentral(){
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.add(new JLabel("Fecha de alta: "),  GenericUtils.createGridBagConstraints(0, 0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 10, 0, 5), 1, 1, 0, 0));
		panel.add(getTxtFecha(), GenericUtils.createGridBagConstraints(1, 0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0.5, 0.5));
		panel.add(new JLabel("Diagnóstico: "), GenericUtils.createGridBagConstraints(0, 1,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
		panel.add(getTxtDiagnostico(),  GenericUtils.createGridBagConstraints(1, 1,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0.5, 0.5));
		return panel;
	}

	private JPanel getPanelSur(){
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panel.add(getBtnAceptar());
		panel.add(getBtnCancelar());
		return panel;
	}

	private FWDateField getTxtFecha() {
		if(txtFecha==null){
			txtFecha = new FWDateField();
			txtFecha.setFecha(DateUtil.getHoy());
		}
		return txtFecha;
	}
	
	private JTextField getTxtDiagnostico() {
		if(txtDiagnostico == null) {
			txtDiagnostico = new JTextField();
		}
		return txtDiagnostico;
	}

	private JButton getBtnAceptar() {
		if(btnAceptar == null){
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ValeEnfermedad ve = (ValeEnfermedad)valeAtencion;
					ve.setFechaAlta(getTxtFecha().getFecha());
					ve.setDiagnostico(getTxtDiagnostico().getText().trim());
					acepto = true;
					dispose();
				}
			});
		}
		return btnAceptar;
	}
	
	private JButton getBtnCancelar() {
		if(btnCancelar == null){
			btnCancelar = new JButton("Cancelar");
			btnCancelar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					acepto = false;
					dispose();
				}
			});
		}
		return btnCancelar;
	}

	public boolean isAcepto() {
		return acepto;
	}

}