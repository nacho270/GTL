package ar.com.textillevel.gui.acciones;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;

import org.apache.taglibs.string.util.StringW;

import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTable;
import ar.com.fwcommon.componentes.PanelTabla;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.entidades.ventas.productos.Producto;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;
import ar.com.textillevel.modulos.odt.facade.api.remote.OrdenDeTrabajoFacadeRemote;
import ar.com.textillevel.util.GTLBeanFactory;
import ar.com.textillevel.util.ODTCodigoHelper;

public class JDialogSeleccionarCrearODT extends JDialog {

	private static final long serialVersionUID = 5722108060830162182L;

	private List<Producto> productoList;
	private List<OrdenDeTrabajo> odtList;
	private JPanel pnlBotones;
	private JButton btnCancelar;
	private JButton btnAceptar;
	private PanelTablaODT panelTablaODT;
	private OrdenDeTrabajoFacadeRemote odtFacade;
	private OrdenDeTrabajo selectedODT;
	private boolean acepto;

	public JDialogSeleccionarCrearODT(Frame owner, List<Producto> productoList, List<OrdenDeTrabajo> odtList) {
		super(owner);
		setAcepto(false);
		this.productoList = productoList;
		this.odtList = new ArrayList<OrdenDeTrabajo>(odtList);
		this.odtFacade = GTLBeanFactory.getInstance().getBean2(OrdenDeTrabajoFacadeRemote.class);
		setTitle("Seleccionar/Crear ODT");
		setModal(true);
		setSize(new Dimension(380, 300));
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
			panelTablaODT = new PanelTablaODT(productoList);
		}
		return panelTablaODT;
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
					getOdtList().clear();
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
					String txtValidacion = getPanTablaODTs().validarElementos();
					if(txtValidacion != null) {
						FWJOptionPane.showErrorMessage(JDialogSeleccionarCrearODT.this, StringW.wordWrap(txtValidacion), "Error");
						return;
					}
					OrdenDeTrabajo selectedODT = getPanTablaODTs().getSelectedODT();
					setSelectedODT(selectedODT);
					dispose();
					setAcepto(true);
				}

			});
		}
		return btnAceptar;
	}

	private void setSelectedODT(OrdenDeTrabajo selectedODT) {
		this.selectedODT = selectedODT;
	}

	public OrdenDeTrabajo getSelectedODT() {
		return selectedODT;
	}

	@SuppressWarnings("unused")
	private class PanelTablaODT extends PanelTabla<OrdenDeTrabajo> {

		private static final long serialVersionUID = 1721812977586062151L;

		private static final int CANT_COLS = 3;
		private static final int COL_ODT = 0;
		private static final int COL_PRODUCTO = 1;
		private static final int COL_OBJ = 2;

		private JComboBox cmbProducto;
		private String ultimoCodigo;
		private List<Producto> productoList;

		public PanelTablaODT(List<Producto> productoList) {
			this.productoList = productoList;
			GuiUtil.llenarCombo(getCmbProducto(), productoList, true);
			getBotonEliminar().setVisible(false);
		}

		public OrdenDeTrabajo getSelectedODT() {
			getPanTablaODTs().capturarSetearDatos();
			int selectedRow = getTabla().getSelectedRow();
			if(selectedRow == -1) {
				return null;
			} else {
				return getElemento(selectedRow); 
			}
		}

		private void capturarSetearDatos() {
			for(int row = 0; row < getTabla().getRowCount(); row++) {
				OrdenDeTrabajo elemento = getElemento(row);
				elemento.setProducto((Producto)getTabla().getValueAt(row, COL_PRODUCTO));
			}
		}

		@Override
		protected void agregarElemento(OrdenDeTrabajo elemento) {
			Object[] row = new Object[CANT_COLS];
			row[COL_ODT] = ODTCodigoHelper.getInstance().formatCodigo(elemento.getCodigo());
			row[COL_PRODUCTO] = elemento.getProducto();
			row[COL_OBJ] = elemento;
			getTabla().addRow(row);
		}

		@Override
		protected FWJTable construirTabla() {
			FWJTable tablaODT = new FWJTable(0, CANT_COLS);
			tablaODT.setStringColumn(COL_ODT, "ODT", 80, 80, true);
			tablaODT.setComboColumn(COL_PRODUCTO, "PRODUCTO", getCmbProducto(), 200, false);
			tablaODT.setStringColumn(COL_OBJ, "", 0, 0, true);
			return tablaODT;
		}

		private JComboBox getCmbProducto() {
			if(cmbProducto == null) {
				cmbProducto = new JComboBox();
			}
			return cmbProducto;
		}

		@Override
		public boolean validarAgregar() {
			String codODT = getProximoCodODT();
			OrdenDeTrabajo odt = new OrdenDeTrabajo();
			odt.setCodigo(codODT);
			getOdtList().add(odt);
			agregarElemento(odt);
			return false;
		}

		@Override
		protected OrdenDeTrabajo getElemento(int fila) {
			return (OrdenDeTrabajo)getTabla().getValueAt(fila, COL_OBJ);
		}

		@Override
		protected String validarElemento(int fila) {
			if(getTabla().getValueAt(fila, COL_PRODUCTO) == null) {
				return "Debe seleccionar el producto para la ODT " + getTabla().getValueAt(fila, COL_ODT);
			}
			return null;
		}

		private String getProximoCodODT() {
			String proximoCodigo = null;
			if(odtList.isEmpty()) {
				proximoCodigo = getPrimerCodigoAUtilizar();
			} else {
				OrdenDeTrabajo odt = getOdtList().get(getOdtList().size()-1);
				proximoCodigo = ODTCodigoHelper.getInstance().getProximoCodigo(odt.getCodigo());
			}
			return proximoCodigo;
		}

		private String getPrimerCodigoAUtilizar() {
			return ODTCodigoHelper.getInstance().getProximoCodigo(getUltimoCodigo());
		}

		private String getUltimoCodigo() {
			if(ultimoCodigo == null) {
				ultimoCodigo = odtFacade.getUltimoCodigoODT();
			}
			return ultimoCodigo;
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

}
