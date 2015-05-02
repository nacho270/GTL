package ar.com.textillevel.gui.modulos.personal.abm.antiguedad;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ar.clarin.fwjava.componentes.CLJNumericTextField;
import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.CLJTextField;
import ar.clarin.fwjava.util.GuiUtil;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.modulos.personal.entidades.antiguedad.ValorAntiguedad;

public class JDialogAgregarModificarValorAntiguedad extends JDialog {

	private static final long serialVersionUID = 1149877072683334150L;

	private ValorAntiguedad valorAntiguedadActual;
	private boolean acepto;

	private CLJNumericTextField txtAnios;
	private CLJTextField txtValor;

	private JButton btnAceptar;
	private JButton btnCancelar;
	
	private List<ValorAntiguedad> valoresYaIngresados;
	
	private boolean edicion;

	public JDialogAgregarModificarValorAntiguedad(Dialog padre, List<ValorAntiguedad> valoresYaIngresados) {
		super(padre);
		setValorAntiguedadActual(new ValorAntiguedad());
		setValoresYaIngresados(valoresYaIngresados);
		setAcepto(false);
		setEdicion(false);
		setUpComponentes();
		setUpScreen();
	}

	public JDialogAgregarModificarValorAntiguedad(Dialog padre, ValorAntiguedad valorAntiguedad, List<ValorAntiguedad> valoresYaIngresados) {
		super(padre);
		setValorAntiguedadActual(valorAntiguedad);
		setValoresYaIngresados(valoresYaIngresados);
		setAcepto(false);
		setEdicion(true);
		setUpComponentes();
		setUpScreen();
		loadData();
	}

	private void loadData() {
		getTxtAnios().setValue(getValorAntiguedadActual().getAntiguedad().longValue());
		getTxtValor().setText(GenericUtils.getDecimalFormat().format(getValorAntiguedadActual().getValorDefault().doubleValue()));
	}

	private void setUpScreen() {
		setTitle("Valor para antigüedad");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setModal(true);
		pack();
		setResizable(false);
		GuiUtil.centrar(this);
	}

	private void setUpComponentes() {
		add(getPanelCentro(),BorderLayout.CENTER);
		add(getPanelSur(),BorderLayout.SOUTH);
	}
	
	private JPanel getPanelCentro(){
		JPanel panel = new JPanel(new GridBagLayout());
		panel.add(new JLabel("Años: "), GenericUtils.createGridBagConstraints(0, 0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 1, 1, 0, 0));
		panel.add(getTxtAnios(), GenericUtils.createGridBagConstraints(1, 0,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 1, 1, 1, 0));
		panel.add(new JLabel("Valor: "), GenericUtils.createGridBagConstraints(0, 1,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 1, 1, 0, 0));
		panel.add(getTxtValor(), GenericUtils.createGridBagConstraints(1, 1,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 1, 1, 1, 0));
		return panel;
	}
	
	public JPanel getPanelSur(){
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
		panel.add(getBtnAceptar());
		panel.add(getBtnCancelar());
		return panel;
	}

	public ValorAntiguedad getValorAntiguedadActual() {
		return valorAntiguedadActual;
	}

	public void setValorAntiguedadActual(ValorAntiguedad valorAntiguedadActual) {
		this.valorAntiguedadActual = valorAntiguedadActual;
	}

	public boolean isAcepto() {
		return acepto;
	}

	public void setAcepto(boolean acepto) {
		this.acepto = acepto;
	}

	public CLJNumericTextField getTxtAnios() {
		if(txtAnios == null){
			txtAnios = new CLJNumericTextField();
		}
		return txtAnios;
	}

	public CLJTextField getTxtValor() {
		if(txtValor == null){
			txtValor = new CLJTextField();
		}
		return txtValor;
	}

	public JButton getBtnAceptar() {
		if(btnAceptar == null){
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(validar()){
						getValorAntiguedadActual().setAntiguedad(getTxtAnios().getValue());
						getValorAntiguedadActual().setValorDefault(new BigDecimal(GenericUtils.getDoubleValueInJTextField(getTxtValor())));
						setAcepto(true);
						dispose();
					}
				}
			});
		}
		return btnAceptar;
	}

	private boolean validar() {
		if(getTxtAnios().getValueWithNull()==null){
			CLJOptionPane.showErrorMessage(this, "Debe ingresar los años", "Error");
			getTxtAnios().requestFocus();
			return false;
		}
		if(getTxtValor().getText().trim().length()==0){
			CLJOptionPane.showErrorMessage(this, "Debe ingresar el valor", "Error");
			getTxtValor().requestFocus();
			return false;
		}
		if(!isEdicion() && hayAnioRepetido()){
			CLJOptionPane.showErrorMessage(this, "Ya se existe un valor para el año ingresado.", "Error");
			getTxtValor().requestFocus();
			return false;
		}
		return true;
	}

	private boolean hayAnioRepetido() {
		for(ValorAntiguedad va : getValoresYaIngresados()){
			if(va.getAntiguedad().equals(getTxtAnios().getValue())){
				return true;
			}
		}
		return false;
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

	
	public List<ValorAntiguedad> getValoresYaIngresados() {
		return valoresYaIngresados;
	}

	
	public void setValoresYaIngresados(List<ValorAntiguedad> valoresYaIngresados) {
		this.valoresYaIngresados = valoresYaIngresados;
	}

	
	public boolean isEdicion() {
		return edicion;
	}

	
	public void setEdicion(boolean edicion) {
		this.edicion = edicion;
	}
}
