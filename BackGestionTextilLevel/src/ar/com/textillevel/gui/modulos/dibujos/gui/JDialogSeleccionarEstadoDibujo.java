package ar.com.textillevel.gui.modulos.dibujos.gui;

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
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTextField;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.entidades.ventas.articulos.DibujoEstampado;
import ar.com.textillevel.entidades.ventas.articulos.EEstadoDibujo;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.util.CambioEstadoDibujoHelper;

public class JDialogSeleccionarEstadoDibujo extends JDialog {

	private static final long serialVersionUID = 8877955399644325580L;

	private Frame owner;
	private JButton btnAceptar;
	private JButton btnSalir;

	private JPanel panelCentral;
	private JPanel panelBotones;
	private FWJTextField txtEstadoActual;
	private FWJTextField txtDibujo;
	private JComboBox cmbEstadosPosibles;
	
	private DibujoEstampado dibujo;
	private EEstadoDibujo estadoNuevo;


	public JDialogSeleccionarEstadoDibujo(Frame owner, DibujoEstampado dibujo) {
		super(owner);
		this.dibujo = dibujo;
		this.owner = owner;
		setUpComponentes();
		setUpScreen();
		GuiUtil.centrarEnFramePadre(this);
	}

	private void setUpScreen() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle("Seleccionar nuevo estado de dibujo");
		setResizable(false);
		setSize(new Dimension(400, 190));
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
			panelCentral.add(new JLabel("Dibujo: "), GenericUtils.createGridBagConstraints(0, 0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 1, 1, 0, 0));
			panelCentral.add(getTxtDibujo(),  GenericUtils.createGridBagConstraints(1, 0,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,new Insets(10, 10, 10, 10), 1, 1, 1, 0));
			panelCentral.add(new JLabel("Estado actual: "), GenericUtils.createGridBagConstraints(0, 1,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 1, 1, 0, 0));
			panelCentral.add(getTxtEstadoActual(),  GenericUtils.createGridBagConstraints(1, 1,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,new Insets(10, 10, 10, 10), 1, 1, 1, 0));
			panelCentral.add(new JLabel("Seleccione el nuevo estado: "), GenericUtils.createGridBagConstraints(0, 2,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 1, 1, 0, 0));
			panelCentral.add(getCmbEstadosPosibles(),  GenericUtils.createGridBagConstraints(1, 2,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,new Insets(10, 10, 10, 10), 1, 1, 1, 0));
		}
		return panelCentral;
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
						setEstadoNuevo((EEstadoDibujo)getCmbEstadosPosibles().getSelectedItem());
						dispose();
					}
				}
			});
		}
		return btnAceptar;
	}

	private boolean validar() {
		if(getCmbEstadosPosibles().getSelectedIndex() == -1) {
			FWJOptionPane.showErrorMessage(owner, "Debe seleccionar un nuevo estado", "Error");
			return false;
		}
		return true;
	}

	private JButton getBtnSalir() {
		if (btnSalir == null) {
			btnSalir = new JButton("Cancelar");
			btnSalir.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setEstadoNuevo(null);
					dispose();
				}
			});
		}
		return btnSalir;
	}

	private JTextField getTxtEstadoActual() {
		if(txtEstadoActual == null) {
			txtEstadoActual = new FWJTextField();
			txtEstadoActual.setText(dibujo.getEstado().toString());
			txtEstadoActual.setEditable(false);
		}
		return txtEstadoActual;
	}

	private JTextField getTxtDibujo() {
		if(txtDibujo == null) {
			txtDibujo = new FWJTextField();
			txtDibujo.setText(dibujo.toString());
			txtDibujo.setEditable(false);
		}
		return txtDibujo;
	}

	private JComboBox getCmbEstadosPosibles() {
		if(cmbEstadosPosibles == null) {
			cmbEstadosPosibles = new JComboBox();
			GuiUtil.llenarCombo(cmbEstadosPosibles, CambioEstadoDibujoHelper.getInstance().getEstadosPosibles(dibujo.getEstado()), true);
			cmbEstadosPosibles.setSelectedIndex(-1);
		}
		return cmbEstadosPosibles;
	}
	
	private void setEstadoNuevo(EEstadoDibujo estadoNuevo) {
		this.estadoNuevo = estadoNuevo;
	}

	public EEstadoDibujo getEstadoNuevo() {
		return estadoNuevo;
	}

}