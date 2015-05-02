package ar.com.textillevel.gui.modulos.personal.modulos.recibossueldo.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.util.GuiUtil;
import ar.com.textillevel.gui.util.controles.PanelDatePicker;

public class JDialogInputFechasImpresion extends JDialog {

	private static final long serialVersionUID = 1L;

	private PanelDatePicker panFechaUltDeposito;
	private PanelDatePicker panFechaPago;
	private JButton btnAceptar;
	private JButton btnCancelar;
	
	private Date fechaPago;
	private Date fechaUltDeposito;
	private boolean acepto;

	public JDialogInputFechasImpresion(Frame padre, String title) {
		super(padre);
		setTitle(title);
		setUpComponentes();
		setUpScreen();
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
		panel.add(new JLabel("Fecha de último depósito:"));
		panel.add(getPanFechaUltDeposito());
		panel.add(new JLabel("Fecha de pago:"));
		panel.add(getPanFechaPago());
		return panel;
	}
	
	private JPanel getPanelSur(){
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panel.add(getBtnAceptar());
		panel.add(getBtnCancelar());
		return panel;
	}

	public JButton getBtnAceptar() {
		if(btnAceptar == null){
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(validar()) {
						acepto = true;
						fechaPago = new java.sql.Date(getPanFechaPago().getDate().getTime());
						fechaUltDeposito = new java.sql.Date(getPanFechaUltDeposito().getDate().getTime());
						dispose();
					}
				}

			});
		}
		return btnAceptar;
	}

	private boolean validar() {
		if(getPanFechaPago().getDate() == null) {
			CLJOptionPane.showErrorMessage(JDialogInputFechasImpresion.this, "Debe ingresar una fecha de pago.", "Error");
			return false;
		}
		if(getPanFechaUltDeposito().getDate() == null) {
			CLJOptionPane.showErrorMessage(JDialogInputFechasImpresion.this, "Debe ingresar una fecha de último depósito.", "Error");
			return false;
		}
		return true;
	}

	public JButton getBtnCancelar() {
		if(btnCancelar == null){
			btnCancelar = new JButton("Cancelar");
			btnCancelar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					acepto = false;
					fechaPago = null;
					fechaUltDeposito = null;
					dispose();
				}
			});
		}
		return btnCancelar;
	}

	private PanelDatePicker getPanFechaUltDeposito() {
		if(panFechaUltDeposito == null) {
			panFechaUltDeposito = new PanelDatePicker();
		}
		return panFechaUltDeposito;
	}

	private PanelDatePicker getPanFechaPago() {
		if(panFechaPago == null) {
			panFechaPago = new PanelDatePicker();
		}
		return panFechaPago;
	}

	public Date getFechaUltDeposito() {
		return fechaUltDeposito;
	}

	public Date getFechaPago() {
		return fechaPago;
	}

	public boolean isAcepto() {
		return acepto;
	}

}