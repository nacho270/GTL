package ar.com.textillevel.gui.modulos.personal.abm.tareas;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.fwcommon.util.StringUtil;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.Categoria;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.Puesto;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.ValorPuesto;

public class JDialogIngresarValorPuesto extends JDialog {

	private static final long serialVersionUID = -2764887208326768990L;

	private Frame owner;
	private JButton btnAceptar;
	private JButton btnSalir;

	private JPanel panelCentral;
	private JPanel panelBotones;
	private JComboBox cmbPuesto;
	private JTextField txtValorHora;
	private ValorPuesto valorPuesto;
	private Categoria categoria;
	private List<Puesto> puestosUsados;
	private boolean acepto;

	public JDialogIngresarValorPuesto(Frame owner, Categoria categoria, List<Puesto> puestosUsados, ValorPuesto valorPuesto) {
		super(owner);
		this.owner = owner;
		this.categoria = categoria;
		this.puestosUsados = puestosUsados;
		this.valorPuesto = valorPuesto;
		setUpComponentes();
		setUpScreen();
		GuiUtil.centrarEnFramePadre(this);
		setDatos();
	}

	private void setDatos() {
		getCmbPuesto().setSelectedItem(getValorPuesto().getPuesto());
		getTxtValorHora().setText(GenericUtils.getDecimalFormat().format(getValorPuesto().getValorHora().doubleValue()).replaceAll("\\.", ","));
	}

	private void setUpScreen() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle("Ingresar valor a puesto");
		setResizable(false);
		setSize(new Dimension(300, 150));
		setModal(true);
	}

	private void setUpComponentes() {
		add(getPanelCentral(),BorderLayout.CENTER);
		add(getPanelBotones(),BorderLayout.SOUTH);
	}

	private JPanel getPanelCentral() {
		if(panelCentral == null){
			panelCentral = new JPanel();
			panelCentral.setLayout(new GridBagLayout());
			panelCentral.add(new JLabel("Puesto: "), GenericUtils.createGridBagConstraints(0, 0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 1, 1, 0, 0));
			panelCentral.add(getCmbPuesto(),  GenericUtils.createGridBagConstraints(1, 0,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,new Insets(10, 10, 10, 10), 1, 1, 1, 0));
			panelCentral.add(new JLabel("Valor Hora: "), GenericUtils.createGridBagConstraints(0, 1,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 1, 1, 0, 0));
			panelCentral.add(getTxtValorHora(),  GenericUtils.createGridBagConstraints(1, 1,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 1, 1, 1, 0));
		}
		return panelCentral;
	}

	private JComboBox getCmbPuesto() {
		if(cmbPuesto == null) {
			cmbPuesto = new JComboBox();
			List<Puesto> allPuestos = new ArrayList<Puesto>(categoria.getPuestos());
			allPuestos.removeAll(puestosUsados);
			GuiUtil.llenarCombo(cmbPuesto, allPuestos, true);
			cmbPuesto.setSelectedIndex(allPuestos.size() - 1);
		}
		return cmbPuesto;
	}

	private JPanel getPanelBotones() {
		if(panelBotones == null){
			panelBotones = new JPanel();
			panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			panelBotones.add(getBtnAceptar());
			panelBotones.add(getBtnSalir());
		}
		return panelBotones;
	}

	private JButton getBtnAceptar() {
		if (btnAceptar == null) {
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if(validar()) {
						acepto = true;
						BigDecimal valorHora = new BigDecimal(GenericUtils.getDoubleValueInJTextField(getTxtValorHora()));
						getValorPuesto().setPuesto((Puesto)getCmbPuesto().getSelectedItem());
						getValorPuesto().setValorHora(valorHora);
						dispose();
					}
				}
			});
		}
		return btnAceptar;
	}

	private boolean validar() {
		if(getCmbPuesto().getSelectedItem() == null) {
			FWJOptionPane.showErrorMessage(owner, "Debe seleccionar un puesto.", "Error");
			return false;
		}
		String valorHoraStr = getTxtValorHora().getText();
		if(StringUtil.isNullOrEmpty(valorHoraStr)) {
			FWJOptionPane.showErrorMessage(owner, "Debe ingresar un valor por hora.", "Error");
			getTxtValorHora().requestFocus();
			return false;
		}
		if(!GenericUtils.esNumerico(valorHoraStr)) {
			FWJOptionPane.showErrorMessage(owner, "El valor de la hora debe ser numérico.", "Error");
			getTxtValorHora().requestFocus();
			return false;
		}
		double valorHora = GenericUtils.getDoubleValueInJTextField(getTxtValorHora());
		if(valorHora <= 0) {
			FWJOptionPane.showErrorMessage(owner, "El valor de la hora debe ser mayor a cero.", "Error");
			getTxtValorHora().requestFocus();
			return false;
		}
		return true;
	}

	private JButton getBtnSalir() {
		if (btnSalir == null) {
			btnSalir = new JButton("Cancelar");
			btnSalir.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
		}
		return btnSalir;
	}
	
	private JTextField getTxtValorHora() {
		if(txtValorHora == null) {
			txtValorHora = new JTextField();
		}
		return txtValorHora;
	}

	public ValorPuesto getValorPuesto() {
		return valorPuesto;
	}

	public boolean isAcepto() {
		return acepto;
	}

}