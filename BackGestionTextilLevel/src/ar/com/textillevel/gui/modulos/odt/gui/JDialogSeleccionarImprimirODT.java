package ar.com.textillevel.gui.modulos.odt.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTable;
import ar.com.fwcommon.componentes.PanelTabla;
import ar.com.textillevel.entidades.ventas.ProductoArticulo;
import ar.com.textillevel.entidades.ventas.productos.ProductoTenido;
import ar.com.textillevel.gui.modulos.odt.impresion.ImprimirODTHandler;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;

public class JDialogSeleccionarImprimirODT extends JDialog {

	private static final long serialVersionUID = 5722108060830162182L;

	private List<OrdenDeTrabajo> odtList;
	private JPanel pnlBotones;
	private JButton btnCancelar;
	private JButton btnImprimir;
	private PanelTablaODT panelTablaODT;
	private boolean acepto;

	public JDialogSeleccionarImprimirODT(Frame owner, List<OrdenDeTrabajo> odtList) {
		super(owner);
		setAcepto(false);
		this.odtList = new ArrayList<OrdenDeTrabajo>(odtList);
		setTitle("Selección de ODTs");
		setModal(true);
		setSize(new Dimension(450, 300));
		setResizable(false);
		construct();
		llenarTablaODTs();
	}

	private void llenarTablaODTs() {
		getPanTablaODTs().agregarElementos(odtList);
	}

	private void construct() {
		setLayout(new GridBagLayout());
		add(getPanTablaODTs(), GenericUtils.createGridBagConstraints(0, 0,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 1, 1, 1, 1));
		add(getPanelBotones(), GenericUtils.createGridBagConstraints(0, 1,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
	}

	private PanelTablaODT getPanTablaODTs() {
		if(panelTablaODT == null) {
			panelTablaODT = new PanelTablaODT();
		}
		return panelTablaODT;
	}

	private JPanel getPanelBotones() {
		if(pnlBotones == null){
			pnlBotones = new JPanel();
			pnlBotones.setLayout(new FlowLayout(FlowLayout.CENTER));
			pnlBotones.add(getBtnImprimir());
			pnlBotones.add(getBtnCancelar());
		}
		return pnlBotones;
	}

	private JButton getBtnCancelar() {
		if(btnCancelar == null) {
			btnCancelar = new JButton("Cancelar");
			btnCancelar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					getOdtList().clear();
					dispose();
				}

			});
		}
		return btnCancelar;
	}

	private JButton getBtnImprimir() {
		if(btnImprimir == null) {
			btnImprimir = new JButton("Imprimir");
			btnImprimir.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					List<OrdenDeTrabajo> selectedODTs = getPanTablaODTs().getSelectedODTs();
					if(selectedODTs.isEmpty()) {
						FWJOptionPane.showErrorMessage(JDialogSeleccionarImprimirODT.this, "Por favor, seleccione al menos una ODT.", "Error");
						return;
					}
					for(OrdenDeTrabajo odt : selectedODTs) {
						ImprimirODTHandler handler = new ImprimirODTHandler(odt, JDialogSeleccionarImprimirODT.this);
						try {
							handler.imprimir();
						} catch (IOException e1) {
							e1.printStackTrace();
							FWJOptionPane.showErrorMessage(JDialogSeleccionarImprimirODT.this, "Ha ocurrido un error al imprimir", "Error");
						}
					}
					dispose();
					setAcepto(true);
				}

			});
		}
		return btnImprimir;
	}

	@SuppressWarnings("unused")
	private class PanelTablaODT extends PanelTabla<OrdenDeTrabajo> {

		private static final long serialVersionUID = 1721812977586062151L;

		private static final int CANT_COLS = 3;
		private static final int COL_ODT = 0;
		private static final int COL_CHK_SEL_IMP_ODT = 1;
		private static final int COL_OBJ = 2;

		private String ultimoCodigo;

		public PanelTablaODT() {
			getBotonEliminar().setVisible(false);
			getBotonAgregar().setVisible(false);
		}

		public List<OrdenDeTrabajo> getSelectedODTs() {
			List<OrdenDeTrabajo> odts = new ArrayList<OrdenDeTrabajo>();
			for(int i = 0; i < getTabla().getRowCount(); i++) {
				Boolean chkImpODT = (Boolean)getTabla().getValueAt(i, COL_CHK_SEL_IMP_ODT);
				if(chkImpODT) {
					odts.add(getElemento(i));
				}
			}
			return odts;
		}

		@Override
		protected void agregarElemento(OrdenDeTrabajo elemento) {
			Object[] row = new Object[CANT_COLS];
			row[COL_ODT] = elemento.toString();
			row[COL_CHK_SEL_IMP_ODT] = true;
			row[COL_OBJ] = elemento;
			getTabla().addRow(row);
		}

		@Override
		protected FWJTable construirTabla() {
			FWJTable tablaODT = new FWJTable(0, CANT_COLS);
			tablaODT.setStringColumn(COL_ODT, "ODT", 290, 290, true);
			tablaODT.setHeaderAlignment(COL_ODT, FWJTable.CENTER_ALIGN);
			tablaODT.setCheckColumn(COL_CHK_SEL_IMP_ODT, "IMPRIMIR", 80, false);
			tablaODT.setHeaderAlignment(COL_CHK_SEL_IMP_ODT, FWJTable.CENTER_ALIGN);
			tablaODT.setStringColumn(COL_OBJ, "", 0, 0, true);
			return tablaODT;
		}

		@Override
		public boolean validarAgregar() {
			return false;
		}

		@Override
		protected OrdenDeTrabajo getElemento(int fila) {
			return (OrdenDeTrabajo)getTabla().getValueAt(fila, COL_OBJ);
		}

		@Override
		protected String validarElemento(int fila) {
			return null;
		}

	}

	public List<OrdenDeTrabajo> getOdtList() {
		return odtList;
	}

	
	public boolean isAcepto() {
		return acepto;
	}

	
	public void setAcepto(boolean acepto) {
		this.acepto = acepto;
	}


	public static void main(String[] args) {
		List<OrdenDeTrabajo> odts = new ArrayList<OrdenDeTrabajo>();
		ProductoTenido pt = new ProductoTenido();
		pt.setDescripcion("sarasas");
		ProductoArticulo pa = new ProductoArticulo();
		pa.setProducto(pt);
		OrdenDeTrabajo odt = new OrdenDeTrabajo();
		odt.setProductoArticulo(pa);
		odt.setCodigo("1213434");
		odts.add(odt);
		odt = new OrdenDeTrabajo();
		odt.setProductoArticulo(pa);
		odt.setCodigo("121342222");
		odts.add(odt);
		JDialogSeleccionarImprimirODT d = new JDialogSeleccionarImprimirODT(null, odts);
		d.setVisible(true);

	}

}
