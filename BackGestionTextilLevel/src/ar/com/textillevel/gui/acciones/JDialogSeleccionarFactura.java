package ar.com.textillevel.gui.acciones;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTable;
import ar.com.fwcommon.util.DateUtil;
import ar.com.textillevel.entidades.documentos.factura.Factura;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.gui.util.GenericUtils;

public class JDialogSeleccionarFactura extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel panDetalle;
	private JTextField txtRazSoc;
	private JButton btnAceptar;
	private JButton btnCancelar;
	private JPanel pnlBotones;
	private FWJTable tablaFactura;
	private Cliente cliente;
	private Factura factura;
	private List<Factura> facturaList;
	
	private static final int CANT_COLS_TBL_FACTURA = 5;
	private static final int COL_FECHA = 0;
	private static final int COL_NRO_FACTURA = 1;
	private static final int COL_IMPORTE_FALTANTE = 2;
	private static final int COL_IMPORTE_TOTAL = 3;
	private static final int COL_OBJ_FACTURA = 4;

	public JDialogSeleccionarFactura(Frame owner, Cliente cliente, List<Factura> facturaList) {
		super(owner);
		this.facturaList = facturaList;
		this.cliente = cliente;
		setModal(true);
		setSize(new Dimension(520, 550));
		setTitle("Seleccionar Factura");
		construct();
		llenarTablaFactura();
	}

	private void llenarTablaFactura() {
		getTablaFactura().setNumRows(0);
		int row = 0;
		for(Factura f : facturaList) {
			getTablaFactura().addRow();
			getTablaFactura().setValueAt(DateUtil.dateToString(f.getFechaEmision()), row, COL_FECHA);
			getTablaFactura().setValueAt(f.getMontoFaltantePorPagar(), row, COL_IMPORTE_FALTANTE);
			getTablaFactura().setValueAt(f.getNroFactura(), row, COL_NRO_FACTURA);
			getTablaFactura().setValueAt(f.getMontoTotal(), row, COL_IMPORTE_TOTAL);
			getTablaFactura().setValueAt(f, row, COL_OBJ_FACTURA);
			row ++;
		}

	}

	private void construct() {
		setLayout(new GridBagLayout());
		add(getPanDetalle(), GenericUtils.createGridBagConstraints(0, 0,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 1, 1, 1, 1));
		add(getPanelBotones(), GenericUtils.createGridBagConstraints(0, 1,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
	}

	private JPanel getPanDetalle() {
		if(panDetalle == null) {
			panDetalle = new JPanel();
			panDetalle.setLayout(new GridBagLayout());
			panDetalle.add(new JLabel(" RAZON SOCIAL:"), GenericUtils.createGridBagConstraints(0, 0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getTxtRazSoc(), GenericUtils.createGridBagConstraints(1, 0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 2, 1, 1, 0));
			JScrollPane scrollPane = new JScrollPane(getTablaFactura());
			scrollPane.setBorder(BorderFactory.createTitledBorder("Facturas"));
			panDetalle.add(scrollPane, GenericUtils.createGridBagConstraints(0, 1,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 3, 1, 0, 1));
		}
		return panDetalle;
	}

	private FWJTable getTablaFactura() {
		if(tablaFactura == null) {
			tablaFactura = new FWJTable(0, CANT_COLS_TBL_FACTURA) {
				private static final long serialVersionUID = 1L;

				@Override
				public void newRowSelected(int newRow, int oldRow) {
						getBtnAceptar().setEnabled(newRow != -1);
				}

			};
			tablaFactura.setStringColumn(COL_FECHA, "Fecha", 230, 100, true);
			tablaFactura.setStringColumn(COL_NRO_FACTURA, "Número", 100, 150, true);
			tablaFactura.setStringColumn(COL_IMPORTE_TOTAL, "Importe Total", 150, 100, true);
			tablaFactura.setStringColumn(COL_IMPORTE_FALTANTE, "Pendiente de Pagar", 150, 100, true);
			tablaFactura.setStringColumn(COL_OBJ_FACTURA, "", 0, 0, true);
			tablaFactura.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			tablaFactura.addMouseListener(new MouseAdapter () {

				@Override
				public void mouseClicked(MouseEvent e) {
					if(e.getClickCount() == 2) {
						handleSeleccionFactura();
					}
				}

			});
		}
		return tablaFactura;
	}

	protected void llenarTabla(List<Cliente> clienteList) {
		getTablaFactura().setNumRows(0);
		int row = 0;
		for(Cliente c : clienteList) {
			getTablaFactura().addRow();
			getTablaFactura().setValueAt(c.getRazonSocial(), row, 0);
			getTablaFactura().setValueAt(c, row, 1);
			row ++;
		}
	}

	private JTextField getTxtRazSoc() {
		if(txtRazSoc == null) {
			txtRazSoc = new JTextField();
			txtRazSoc.setEditable(false);
			txtRazSoc.setText(cliente.getRazonSocial());
		}
		return txtRazSoc;
	}

	private JPanel getPanelBotones() {
		if(pnlBotones == null){
			pnlBotones = new JPanel();
			pnlBotones.setLayout(new FlowLayout(FlowLayout.CENTER));
			pnlBotones.add(getBtnAceptar());
			pnlBotones.add(getBtnCancelar());
		}
		return pnlBotones;
	}

	private JButton getBtnCancelar() {
		if(btnCancelar == null) {
			btnCancelar = new JButton("Cancelar");
			btnCancelar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					factura = null;
					dispose();
				}

			});
		}
		return btnCancelar;
	}

	private JButton getBtnAceptar() {
		if(btnAceptar == null) {
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					handleSeleccionFactura();
				}

			});
			btnAceptar.setEnabled(false);
		}
		return btnAceptar;
	}

	private void handleSeleccionFactura() {
		int selectedRow = getTablaFactura().getSelectedRow();
		if(selectedRow == 0) {
			factura = (Factura)getTablaFactura().getValueAt(selectedRow, COL_OBJ_FACTURA);
			dispose();
		} else {
			FWJOptionPane.showErrorMessage(JDialogSeleccionarFactura.this, "Debe seleccionar la factura más antigua.", "Atención");
			return;
		}
	}

	public Factura getFactura() {
		return factura;
	}

}