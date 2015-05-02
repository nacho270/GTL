package ar.com.textillevel.gui.modulos.personal.modulos.legajos.gui.wfvaleatencion;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import ar.clarin.fwjava.componentes.CLDateField;
import ar.clarin.fwjava.util.DateUtil;
import ar.clarin.fwjava.util.GuiUtil;

public class JDialogInputFecha extends JDialog {

	private static final long serialVersionUID = 1L;

	private CLDateField txtFecha;
	private JButton btnAceptar;
	private JButton btnCancelar;
	
	private Date fecha;
	
	public JDialogInputFecha(Frame padre, String title) {
		super(padre);
		setTitle(title);
		setUpComponentes();
		setUpScreen();
		getTxtFecha().requestFocus();
	}

	private void setUpScreen() {
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
		panel.add(getTxtFecha());
		return panel;
	}
	
	private JPanel getPanelSur(){
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panel.add(getBtnAceptar());
		panel.add(getBtnCancelar());
		return panel;
	}

	public CLDateField getTxtFecha() {
		if(txtFecha==null){
			txtFecha = new CLDateField();
			txtFecha.setFecha(DateUtil.getHoy());
		}
		return txtFecha;
	}
	
	public JButton getBtnAceptar() {
		if(btnAceptar == null){
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					fecha = getTxtFecha().getFecha();
					dispose();
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
					fecha = null;
					dispose();
				}
			});
		}
		return btnCancelar;
	}
	
	public Date getFecha() {
		return fecha;
	}

}