package main.acciones.facturacion.gui;

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

import ar.clarin.fwjava.componentes.CLJNumericTextField;
import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.util.GuiUtil;
import ar.clarin.fwjava.util.StringUtil;
import ar.com.textillevel.entidades.documentos.factura.CorreccionFactura;
import ar.com.textillevel.entidades.enums.ETipoCorreccionFactura;
import ar.com.textillevel.facade.api.remote.CorreccionFacadeRemote;
import ar.com.textillevel.facade.api.remote.ParametrosGeneralesFacadeRemote;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.util.GTLBeanFactory;
import edu.emory.mathcs.backport.java.util.Arrays;

public class JDialogParamBusquedaCorreccionFactura extends JDialog {

	private static final long serialVersionUID = 8877955399644325580L;

	private Frame owner;
	private JButton btnAceptar;
	private JButton btnSalir;

	private JPanel panelCentral;
	private JPanel panelBotones;
	private CLJNumericTextField txtNroSucursal;
	private JComboBox cmbTipoCorreccion;
	private CLJNumericTextField txtNroCorreccion;
	private CorreccionFactura correccionFactura;
	private ParametrosGeneralesFacadeRemote paramGenerales;
	private CorreccionFacadeRemote cfr;

	public JDialogParamBusquedaCorreccionFactura(Frame owner) {
		super(owner);
		this.owner = owner;
		paramGenerales = GTLBeanFactory.getInstance().getBean2(ParametrosGeneralesFacadeRemote.class);
		cfr = GTLBeanFactory.getInstance().getBean2(CorreccionFacadeRemote.class);
		setUpComponentes();
		setUpScreen();
		GuiUtil.centrarEnFramePadre(this);
	}

	private void setUpScreen() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle("Buscar notas de débito/crédito");
		setResizable(false);
		setSize(new Dimension(330, 190));
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
			panelCentral.add(new JLabel("Ingrese el número de sucursal: "), GenericUtils.createGridBagConstraints(0, 0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 1, 1, 0, 0));
			panelCentral.add(getTxtNroSucursal(),  GenericUtils.createGridBagConstraints(1, 0,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,new Insets(10, 10, 10, 10), 1, 1, 1, 0));
			panelCentral.add(new JLabel("Seleccione el tipo de nota: "), GenericUtils.createGridBagConstraints(0, 1,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 1, 1, 0, 0));
			panelCentral.add(getCmbTipoCorreccion(),  GenericUtils.createGridBagConstraints(1, 1,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,new Insets(10, 10, 10, 10), 1, 1, 1, 0));
			panelCentral.add(new JLabel("Ingrese el número: "), GenericUtils.createGridBagConstraints(0, 2,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 1, 1, 0, 0));
			panelCentral.add(getTxtNroCorreccion(),  GenericUtils.createGridBagConstraints(1, 2,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 1, 1, 1, 0));
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
						ETipoCorreccionFactura tipoCorrecion = ETipoCorreccionFactura.getByDescripcion((String)getCmbTipoCorreccion().getSelectedItem());
						correccionFactura = cfr.getCorreccionByNumero(Integer.valueOf(getTxtNroCorreccion().getText()), tipoCorrecion, Integer.valueOf(getTxtNroSucursal().getText()));
						if(correccionFactura == null) {
							CLJOptionPane.showErrorMessage(owner, tipoCorrecion.getDescripcion() + " no encontrada.", "Error");
						} else {
							dispose();
						}
					}
				}
			});
		}
		return btnAceptar;
	}

	private boolean validar() {
		ETipoCorreccionFactura tipoCorrecion = ETipoCorreccionFactura.getByDescripcion((String)getCmbTipoCorreccion().getSelectedItem());
		if(StringUtil.isNullOrEmpty(getTxtNroSucursal().getText())) {
			CLJOptionPane.showErrorMessage(owner, "Debe ingresar el número de sucursal.", "Error");
			getTxtNroSucursal().requestFocus();
			return false;
		}
		if(StringUtil.isNullOrEmpty(getTxtNroCorreccion().getText())) {
			CLJOptionPane.showErrorMessage(owner, "Debe ingresar el número de " + tipoCorrecion.getDescripcion() + ".", "Error");
			getTxtNroCorreccion().requestFocus();
			return false;
		}
		return true;
	}

	private JButton getBtnSalir() {
		if (btnSalir == null) {
			btnSalir = new JButton("Cancelar");
			btnSalir.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					correccionFactura=null;
					dispose();
				}
			});
		}
		return btnSalir;
	}

	private JTextField getTxtNroSucursal() {
		if(txtNroSucursal == null) {
			txtNroSucursal = new CLJNumericTextField();
			txtNroSucursal.setValue(new Long(paramGenerales.getParametrosGenerales().getNroSucursal()));
		}
		return txtNroSucursal;
	}

	private JTextField getTxtNroCorreccion() {
		if(txtNroCorreccion == null) {
			txtNroCorreccion = new CLJNumericTextField();
		}
		return txtNroCorreccion;
	}

	private JComboBox getCmbTipoCorreccion() {
		if(cmbTipoCorreccion == null) {
			cmbTipoCorreccion = new JComboBox();
			String[] correcs= new String[ETipoCorreccionFactura.values().length];
			for(int i = 0 ; i<ETipoCorreccionFactura.values().length;i++){
				correcs[i] = ETipoCorreccionFactura.values()[i].getDescripcion();
			}
			GuiUtil.llenarCombo(cmbTipoCorreccion, Arrays.asList(correcs), true);
			cmbTipoCorreccion.setSelectedIndex(0);
		}
		return cmbTipoCorreccion;
	}
	
	public CorreccionFactura getCorreccionFactura() {
		return correccionFactura;
	}

}