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
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ar.com.fwcommon.componentes.FWJNumericTextField;
import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.fwcommon.util.StringUtil;
import ar.com.textillevel.entidades.documentos.factura.Factura;
import ar.com.textillevel.facade.api.remote.FacturaFacadeRemote;
import ar.com.textillevel.facade.api.remote.ParametrosGeneralesFacadeRemote;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogParamBusquedaFactura extends JDialog {

	private static final long serialVersionUID = 8877955399644325580L;

	private Frame owner;
	private JButton btnAceptar;
	private JButton btnSalir;

	private JPanel panelCentral;
	private JPanel panelBotones;
	private FWJNumericTextField txtNroSucursal;
	private FWJNumericTextField txtNroFactura;
	private Factura factura;
	private ParametrosGeneralesFacadeRemote paramGenerales;
	private FacturaFacadeRemote ffr;

	public JDialogParamBusquedaFactura(Frame owner) {
		super(owner);
		this.owner = owner;
		paramGenerales = GTLBeanFactory.getInstance().getBean2(ParametrosGeneralesFacadeRemote.class);
		ffr = GTLBeanFactory.getInstance().getBean2(FacturaFacadeRemote.class);
		setUpComponentes();
		setUpScreen();
		GuiUtil.centrarEnFramePadre(this);
	}

	private void setUpScreen() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle("Buscar factura");
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
			panelCentral.add(new JLabel("Ingrese el número de sucursal: "), GenericUtils.createGridBagConstraints(0, 0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 1, 1, 0, 0));
			panelCentral.add(getTxtNroSucursal(),  GenericUtils.createGridBagConstraints(1, 0,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,new Insets(10, 10, 10, 10), 1, 1, 1, 0));
			panelCentral.add(new JLabel("Ingrese el número de factura: "), GenericUtils.createGridBagConstraints(0, 1,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 1, 1, 0, 0));
			panelCentral.add(getTxtNroFactura(),  GenericUtils.createGridBagConstraints(1, 1,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 1, 1, 1, 0));
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
						factura = ffr.getByNroFacturaConItems(Integer.valueOf(getTxtNroFactura().getText()), Integer.valueOf(getTxtNroSucursal().getText()));
						if(factura == null) {
							FWJOptionPane.showErrorMessage(owner, "Factura no encontrada.", "Error");
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
		if(StringUtil.isNullOrEmpty(getTxtNroSucursal().getText())) {
			FWJOptionPane.showErrorMessage(owner, "Debe ingresar el número de sucursal.", "Error");
			getTxtNroSucursal().requestFocus();
			return false;
		}
		if(StringUtil.isNullOrEmpty(getTxtNroFactura().getText())) {
			FWJOptionPane.showErrorMessage(owner, "Debe ingresar el número de factura.", "Error");
			getTxtNroFactura().requestFocus();
			return false;
		}
		return true;
	}

	private JButton getBtnSalir() {
		if (btnSalir == null) {
			btnSalir = new JButton("Cancelar");
			btnSalir.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					factura=null;
					dispose();
				}
			});
		}
		return btnSalir;
	}

	private JTextField getTxtNroSucursal() {
		if(txtNroSucursal == null) {
			txtNroSucursal = new FWJNumericTextField();
			txtNroSucursal.setValue(new Long(paramGenerales.getParametrosGenerales().getNroSucursal()));
		}
		return txtNroSucursal;
	}

	private JTextField getTxtNroFactura() {
		if(txtNroFactura == null) {
			txtNroFactura = new FWJNumericTextField();
		}
		return txtNroFactura;
	}

	public Factura getFactura() {
		return factura;
	}

}