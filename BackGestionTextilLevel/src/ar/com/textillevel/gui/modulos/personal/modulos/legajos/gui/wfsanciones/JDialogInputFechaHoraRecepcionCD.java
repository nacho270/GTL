package ar.com.textillevel.gui.modulos.personal.modulos.legajos.gui.wfsanciones;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.sql.Timestamp;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import ar.com.fwcommon.componentes.FWDateField;
import ar.com.fwcommon.util.DateUtil;
import ar.com.fwcommon.util.GuiUtil;

public class JDialogInputFechaHoraRecepcionCD extends JDialog {

	private static final long serialVersionUID = 1L;

	private FWDateField txtFecha;
	private JSpinner jsHoras;
	private JSpinner jsMinutos;
	private JButton btnAceptar;
	private JButton btnCancelar;
	
	private Timestamp fechaHora;
	
	public JDialogInputFechaHoraRecepcionCD(Frame padre) {
		super(padre);
		setUpComponentes();
		setUpScreen();
		getTxtFecha().requestFocus();
	}

	private void setUpScreen() {
		setTitle("Fecha y hora de recepción");
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
		panel.add(getJsHoras());
		panel.add(new JLabel(" : "));
		panel.add(getJsMinutos());
		return panel;
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
		}
		return txtFecha;
	}

	private JSpinner getJsHoras() {
		if (jsHoras == null) {
			jsHoras = new JSpinner(new SpinnerNumberModel(0, 0, 23, 1));
			jsHoras.setEditor(new JSpinner.NumberEditor(jsHoras));
			jsHoras.setPreferredSize(new Dimension(40, 23));
			JFormattedTextField ftf = ((JSpinner.DefaultEditor) jsHoras.getEditor()).getTextField();
			ftf.setEditable(true);
			ftf.setBackground(Color.WHITE);
		}
		return jsHoras;
	}

	private JSpinner getJsMinutos() {
		if (jsMinutos == null) {
			jsMinutos = new JSpinner(new SpinnerNumberModel(0, 0, 59, 1));
			jsMinutos.setEditor(new JSpinner.NumberEditor(jsMinutos));
			jsMinutos.setPreferredSize(new Dimension(40, 23));
			JFormattedTextField ftf = ((JSpinner.DefaultEditor) jsMinutos.getEditor()).getTextField();
			ftf.setEditable(true);
			ftf.setBackground(Color.WHITE);
		}
		return jsMinutos;
	}

	
	public JButton getBtnAceptar() {
		if(btnAceptar == null){
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Date fh = DateUtil.redondearFecha(getTxtFecha().getFecha());
					fh = DateUtil.setHoras(fh, (Integer)getJsHoras().getValue());
					fh = DateUtil.setMinutos(fh, (Integer)getJsMinutos().getValue());
					fechaHora = new Timestamp(fh.getTime());
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
					fechaHora = null;
					dispose();
				}
			});
		}
		return btnCancelar;
	}
	
	
	public Timestamp getFechaHora() {
		return fechaHora;
	}

}
