package ar.com.textillevel.gui.util.dialogs;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import ar.com.fwcommon.componentes.FWBotonCalendario;
import ar.com.fwcommon.componentes.FWDateField;
import ar.com.fwcommon.util.DateUtil;
import ar.com.fwcommon.util.GuiUtil;

public class JDialogInputFecha extends JDialog {

	private static final long serialVersionUID = 1L;

	private FWDateField txtFecha;
	private JButton btnAceptar;
	private JButton btnCancelar;
	private FWBotonCalendario btnFecha;
	private JPanel panelCentral;
	
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

	protected JPanel getPanelCentral(){
		if(panelCentral == null) {
			panelCentral = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
			panelCentral.add(getTxtFecha());
			panelCentral.add(getBtnFecha());
		}
		return panelCentral;
	}
	
	private JPanel getPanelSur(){
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panel.add(getBtnAceptar());
		panel.add(getBtnCancelar());
		return panel;
	}

	public FWDateField getTxtFecha() {
		if(txtFecha==null){
			txtFecha = new FWDateField();
			txtFecha.setFecha(DateUtil.getHoy());
			txtFecha.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					getBtnAceptar().doClick();
				}
			});
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

	private FWBotonCalendario getBtnFecha() {
		if(btnFecha == null) {
			btnFecha = new FWBotonCalendario(DateUtil.getHoy()) {

				private static final long serialVersionUID = 1L;

				@Override
				public void botonCalendarioPresionado() {
					Date selectedDate = getBtnFecha().getCalendario().getSelectedDate();
					if(selectedDate != null) {
						getTxtFecha().setFecha(selectedDate);
					}
				}

			};

		}
		return btnFecha;
	}
}