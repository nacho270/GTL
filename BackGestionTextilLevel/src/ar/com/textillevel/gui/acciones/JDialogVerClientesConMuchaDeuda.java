package ar.com.textillevel.gui.acciones;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ar.com.fwcommon.componentes.FWJTable;
import ar.com.fwcommon.componentes.PanelTablaSinBotones;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.entidades.to.ClienteDeudaTO;
import ar.com.textillevel.gui.util.GenericUtils;

public class JDialogVerClientesConMuchaDeuda extends JDialog {

	private static final long serialVersionUID = -8902172799078457735L;
	private PanelTablaClientes panelTablaClientes;
	private List<ClienteDeudaTO> clientes;
	private BigDecimal montoMinimoDeuda;

	public JDialogVerClientesConMuchaDeuda(Frame padre, List<ClienteDeudaTO> clientesAMostrar, BigDecimal montoMinimoDeuda) {
		super(padre);
		setClientes(clientesAMostrar);
		setMontoMinimoDeuda(montoMinimoDeuda);
		setUpComponentes();
		setUpScreen();
		llenarTabla();
	}

	private void setUpScreen() {
		setTitle("Clientes con deuda");
		setSize(new Dimension(450, 300));
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		GuiUtil.centrar(this);
		setModal(true);
	}

	private void setUpComponentes() {
		JLabel label = new JLabel("Los siguientes clientes deben mas de " + GenericUtils.getDecimalFormat().format(getMontoMinimoDeuda()) + " :");
		Font fuente = label.getFont();
		label.setFont(new Font(fuente.getName(), Font.BOLD, fuente.getSize() + 1));
		JPanel panelLabel = new JPanel();
		panelLabel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		panelLabel.add(label);
		this.add(panelLabel, BorderLayout.NORTH);
		this.add(getPanelTablaClientes(), BorderLayout.CENTER);
		this.add(getPanelSur(), BorderLayout.SOUTH);
	}

	private void llenarTabla() {
		for (ClienteDeudaTO c : getClientes()) {
			getPanelTablaClientes().agregarElemento(c);
		}
	}

	private JPanel getPanelSur() {
		JButton btnSalir = new JButton("Aceptar");
		btnSalir.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.CENTER));
		panel.add(btnSalir);
		return panel;
	}

	private class PanelTablaClientes extends PanelTablaSinBotones<ClienteDeudaTO> {

		private static final long serialVersionUID = 4247457498635763788L;

		private static final int CANT_COLS_TBL_CLIENTES = 3;
		private static final int COL_RAZON_SOCIAL = 0;
		private static final int COL_DEUDA = 1;
		private static final int COL_OBJ = 2;

		@Override
		protected void agregarElemento(ClienteDeudaTO cliente) {
			Object[] row = new Object[CANT_COLS_TBL_CLIENTES];
			row[COL_RAZON_SOCIAL] = cliente.getRazonSocial();
			row[COL_DEUDA] = GenericUtils.getDecimalFormat().format(cliente.getDeuda());
			row[COL_OBJ] = cliente;
			getTabla().addRow(row);
		}

		@Override
		protected FWJTable construirTabla() {
			FWJTable tabla = new FWJTable(0, CANT_COLS_TBL_CLIENTES);
			tabla.setStringColumn(COL_RAZON_SOCIAL, "Razón social", 220, 220, true);
			tabla.setStringColumn(COL_DEUDA, "Deuda", 100, 100, true);
			tabla.setStringColumn(COL_OBJ, "", 0, 0, true);
			tabla.setAllowHidingColumns(false);
			tabla.setAllowSorting(true);
			tabla.setReorderingAllowed(false);
			return tabla;
		}

		@Override
		protected ClienteDeudaTO getElemento(int fila) {
			return (ClienteDeudaTO) getTabla().getValueAt(fila, COL_OBJ);
		}

		@Override
		protected String validarElemento(int fila) {
			return null;
		}
	}

	private PanelTablaClientes getPanelTablaClientes() {
		if (panelTablaClientes == null) {
			panelTablaClientes = new PanelTablaClientes();
		}
		return panelTablaClientes;
	}

	public static void main(String[] args) {
		new JDialogListaDeChequesPorVencer(null, null);
	}

	public List<ClienteDeudaTO> getClientes() {
		return clientes;
	}

	public void setClientes(List<ClienteDeudaTO> clientes) {
		this.clientes = clientes;
	}

	public BigDecimal getMontoMinimoDeuda() {
		return montoMinimoDeuda;
	}

	public void setMontoMinimoDeuda(BigDecimal montoMinimoDeuda) {
		this.montoMinimoDeuda = montoMinimoDeuda;
	}
}