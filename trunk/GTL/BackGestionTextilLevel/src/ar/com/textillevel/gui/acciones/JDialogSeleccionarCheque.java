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

import ar.clarin.fwjava.componentes.CLJTable;
import ar.com.textillevel.entidades.cheque.Cheque;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.gui.util.GenericUtils;

public class JDialogSeleccionarCheque extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel panDetalle;
	private JTextField txtRazSoc;
	private JButton btnAceptar;
	private JButton btnCancelar;
	private JPanel pnlBotones;
	private CLJTable tablaCheque;
	private Cliente cliente;
	private Cheque cheque;
	private List<Cheque> chequeList;
	
	private static final int CANT_COLS_TBL_CHEQUE = 5;
	private static final int COL_BANCO = 0;
	private static final int COL_NRO = 1;
	private static final int COL_CUIT = 2;
	private static final int COL_IMPORTE = 3;
	private static final int COL_OBJ = 4;

	public JDialogSeleccionarCheque(Frame owner, Cliente cliente, List<Cheque> chequeList) {
		super(owner);
		this.chequeList = chequeList;
		this.cliente = cliente;
		setModal(true);
		setSize(new Dimension(520, 550));
		setTitle("Seleccionar Cheque");
		construct();
		llenarTablaCheque();
	}

	private void llenarTablaCheque() {
		getTablaCheque().setNumRows(0);
		for(Cheque c : chequeList) {
			Object[] row = new Object[CANT_COLS_TBL_CHEQUE];
			row[COL_BANCO] = c.getBanco().getNombre();
			row[COL_CUIT] = c.getCuit();
			row[COL_IMPORTE] = c.getImporte().toString();
			row[COL_NRO] = c.getNumero();
			row[COL_OBJ] = c;
			getTablaCheque().addRow(row);
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
			JScrollPane scrollPane = new JScrollPane(getTablaCheque());
			scrollPane.setBorder(BorderFactory.createTitledBorder("Facturas"));
			panDetalle.add(scrollPane, GenericUtils.createGridBagConstraints(0, 1,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 3, 1, 0, 1));
		}
		return panDetalle;
	}

	private CLJTable getTablaCheque() {
		if(tablaCheque == null) {
			tablaCheque = new CLJTable(0, CANT_COLS_TBL_CHEQUE) {
				private static final long serialVersionUID = 1L;

				@Override
				public void newRowSelected(int newRow, int oldRow) {
						getBtnAceptar().setEnabled(newRow != -1);
				}

			};
			
			tablaCheque.setStringColumn(COL_BANCO, "Banco", 50, 100, true);
			tablaCheque.setStringColumn(COL_NRO, "Nº", 50, 100, true);
			tablaCheque.setStringColumn(COL_CUIT, "C.U.I.T", 50, 100, true);
			tablaCheque.setFloatColumn(COL_IMPORTE, "Importe", 100, true);
			tablaCheque.setStringColumn(COL_OBJ, "", 0, 0, true);
			
			tablaCheque.addMouseListener(new MouseAdapter () {

				@Override
				public void mouseClicked(MouseEvent e) {
					if(e.getClickCount() == 2) {
						handleSeleccionCheque();
					}
				}

			});
		}
		return tablaCheque;
	}

	protected void llenarTabla(List<Cliente> clienteList) {
		getTablaCheque().setNumRows(0);
		int row = 0;
		for(Cliente c : clienteList) {
			getTablaCheque().addRow();
			getTablaCheque().setValueAt(c.getRazonSocial(), row, 0);
			getTablaCheque().setValueAt(c, row, 1);
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
					cheque = null;
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
					handleSeleccionCheque();
				}

			});
			btnAceptar.setEnabled(false);
		}
		return btnAceptar;
	}

	private void handleSeleccionCheque() {
		int selectedRow = getTablaCheque().getSelectedRow();
		if(selectedRow != -1) {
			cheque = (Cheque)getTablaCheque().getValueAt(selectedRow, COL_OBJ);
			dispose();
		}
	}

	public Cheque getCheque() {
		return cheque;
	}

}