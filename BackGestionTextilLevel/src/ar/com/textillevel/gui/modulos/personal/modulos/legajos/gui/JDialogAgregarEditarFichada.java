package ar.com.textillevel.gui.modulos.personal.modulos.legajos.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import ar.com.fwcommon.util.DateUtil;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.modulos.personal.entidades.fichadas.FichadaLegajo;
import ar.com.textillevel.modulos.personal.entidades.fichadas.enums.EFormaIngresoFichada;
import ar.com.textillevel.modulos.personal.entidades.fichadas.enums.ETipoFichada;

public class JDialogAgregarEditarFichada extends JDialog {

	private static final long serialVersionUID = 8199402300447555195L;

	private JSpinner jsHoras;
	private JSpinner jsMinutos;
	private JComboBox cmbTipoFichada;
	private JButton btnAceptar;
	private JButton btnCancelar;

	private FichadaLegajo fichada;

	private boolean acepto;

	public JDialogAgregarEditarFichada(Dialog padre, Date fecha){
		super(padre);
		setFichada(new FichadaLegajo());
		getFichada().setHorario(new Timestamp(fecha.getTime()));
		getFichada().setTipoFichada(ETipoFichada.ENTRADA);
		setUpComponentes();
		setUpScreen();
		loadData();
	}
	
	public JDialogAgregarEditarFichada(Dialog padre, FichadaLegajo fichada){
		super(padre);
		setFichada(fichada.clone());
		setUpComponentes();
		setUpScreen();
		loadData();
	}
	
	private void loadData() {
		getCmbTipoFichada().setSelectedItem(getFichada().getTipoFichada());
		getJsHoras().setValue(DateUtil.getHoras(getFichada().getHorario()));
		getJsMinutos().setValue(DateUtil.getMinutos(getFichada().getHorario()));
	}

	private void setUpScreen(){
		setTitle("Agregar/editar fichada");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setModal(true);
		setResizable(false);
		pack();
		GuiUtil.centrar(this);
	}
	
	private void setUpComponentes(){
		add(getPanelCentral(),BorderLayout.CENTER);
		add(getPanelSur(),BorderLayout.SOUTH);
	}
	
	private JPanel getPanelCentral(){
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
		panel.add(new JLabel("Tipo de fichada: "));
		panel.add(getCmbTipoFichada());
		panel.add(new JLabel("Hora: "));
		panel.add(getJsHoras());
		panel.add(new JLabel("Minutos: "));
		panel.add(getJsMinutos());
		return panel;
	}
	
	private JPanel getPanelSur(){
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
		panel.add(getBtnAceptar());
		panel.add(getBtnCancelar());
		return panel;
	}
	
	public JSpinner getJsHoras() {
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

	public JSpinner getJsMinutos() {
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

	public JComboBox getCmbTipoFichada() {
		if (cmbTipoFichada == null) {
			cmbTipoFichada = new JComboBox();
			GuiUtil.llenarCombo(cmbTipoFichada, Arrays.asList(ETipoFichada.values()), true);
		}
		return cmbTipoFichada;
	}

	public JButton getBtnAceptar() {
		if(btnAceptar == null){
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					getFichada().setFormaIngresoFichada(EFormaIngresoFichada.MANUAL);
					getFichada().setTipoFichada((ETipoFichada)getCmbTipoFichada().getSelectedItem());
					getFichada().setHorario(DateUtil.setHoras(getFichada().getHorario(), (Integer)getJsHoras().getValue()));
					getFichada().setHorario(DateUtil.setMinutos(getFichada().getHorario(), (Integer)getJsMinutos().getValue()));
					setAcepto(true);
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
					setAcepto(false);
					dispose();
				}
			});
		}
		return btnCancelar;
	}

	public FichadaLegajo getFichada() {
		return fichada;
	}

	public void setFichada(FichadaLegajo fichada) {
		this.fichada = fichada;
	}

	public boolean isAcepto() {
		return acepto;
	}

	public void setAcepto(boolean acepto) {
		this.acepto = acepto;
	}
}
