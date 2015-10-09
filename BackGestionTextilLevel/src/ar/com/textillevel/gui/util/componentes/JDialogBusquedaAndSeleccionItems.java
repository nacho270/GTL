package ar.com.textillevel.gui.util.componentes;

import static ar.com.textillevel.gui.util.GenericUtils.createGridBagConstraints;

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
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ar.clarin.fwjava.boss.BossEstilos;
import ar.clarin.fwjava.componentes.CLJTable;
import ar.clarin.fwjava.componentes.PanelTabla;
import ar.clarin.fwjava.util.GuiUtil;
import ar.clarin.fwjava.util.StringUtil;

public abstract class JDialogBusquedaAndSeleccionItems<T> extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel panDetalle;
	private JTextField txtBusqueda;
	private JButton btnAceptar;
	private JButton btnCancelar;
	private JPanel pnlBotones;
	private JButton btnBuscar;
	private PanelTablaItems panTablaItems;
	private String titleBusquedaTextbox;
	private List<T> items;
	private T selectedItem;

	public JDialogBusquedaAndSeleccionItems(Frame owner, String titleDialog, String titleBusquedaTextbox, List<T> items) {
		super(owner);
		this.items = items;
		this.titleBusquedaTextbox = titleBusquedaTextbox;
		setSize(new Dimension(400, 550));
		setTitle(titleDialog);
		construct();
		setModal(true);
		GuiUtil.centrarEnPadre(this);
		getPanTablaItems().agregarElementos(items);
	}

	private void construct() {
		setLayout(new GridBagLayout());
		add(getPanDetalle(), createGridBagConstraints(0, 0,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 1, 1, 1, 1));
		add(getPanelBotones(), createGridBagConstraints(0, 1,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
	}

	private JPanel getPanDetalle() {
		if(panDetalle == null) {
			panDetalle = new JPanel();
			panDetalle.setLayout(new GridBagLayout());
			panDetalle.add(new JLabel(titleBusquedaTextbox +": "), createGridBagConstraints(0, 0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getTxtBusqueda(), createGridBagConstraints(1, 0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panDetalle.add(getBtnBuscar(), createGridBagConstraints(2, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getPanTablaItems(), createGridBagConstraints(0, 1,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 3, 1, 1, 1));
		}
		return panDetalle;
	}

	private PanelTablaItems getPanTablaItems() {
		if(panTablaItems == null) {
			panTablaItems = new PanelTablaItems();
		}
		return panTablaItems;
	}

	private class PanelTablaItems extends PanelTabla<T> {

		private static final long serialVersionUID = 8463871049853081745L;

		private static final int CANT_COLS = 2;
		private static final int COL_DESCR_ITEM=0;
		private static final int COL_OBJ=1;
		
		public PanelTablaItems() {
			getBotonAgregar().setVisible(false);
			getBotonEliminar().setVisible(false);
		}
		
		@Override
		protected CLJTable construirTabla() {
			CLJTable tabla  = new CLJTable(0, CANT_COLS);
			tabla.setStringColumn(COL_DESCR_ITEM, "ÍTEM", 320, 320, true);
			tabla.setHeaderAlignment(COL_DESCR_ITEM, CLJTable.CENTER_ALIGN);
			tabla.setStringColumn(COL_OBJ, "", 0);
			tabla.setSelectionMode(CLJTable.SINGLE_SELECTION);
			
			tabla.addMouseListener(new MouseAdapter () {

				@Override
				public void mouseClicked(MouseEvent e) {

					getBtnAceptar().setEnabled(getTabla().getSelectedRow() != -1);

					if(e.getClickCount() == 2) {
						handleSeleccionItem();
					}
				}

			});
			
			return tabla;
		}

		@Override
		protected void agregarElemento(T elemento) {
			Object[] row = new Object[CANT_COLS];
			row[COL_DESCR_ITEM] = elemento.toString();
			row[COL_OBJ] = elemento;
			getTabla().addRow(row);
		}

		@Override
		@SuppressWarnings("unchecked")
		protected T getElemento(int fila) {
			return (T)getTabla().getValueAt(fila, COL_OBJ);
		}

		@Override
		protected String validarElemento(int fila) {
			return null;
		}

		public T getSelectedItem() {
			if(getTabla().getSelectedRow() != -1) {
				return getElemento(getTabla().getSelectedRow());
			}
			return null;
		}

	}

	private void handleSeleccionItem() {
		selectedItem = getPanTablaItems().getSelectedItem();
		dispose();
	}

	private JTextField getTxtBusqueda() {
		if(txtBusqueda == null) {
			txtBusqueda = new JTextField();
			txtBusqueda.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					getBtnBuscar().doClick();
				}
			});

		}
		return txtBusqueda;
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
					selectedItem = null;
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
					selectedItem = getPanTablaItems().getSelectedItem();
					dispose();
				}

			});
			btnAceptar.setEnabled(false);
		}
		return btnAceptar;
	}

	private JButton getBtnBuscar() {
		if(btnBuscar == null) {
			
			btnBuscar = BossEstilos.createButton("ar/com/textillevel/imagenes/b_buscar.png", "ar/com/textillevel/imagenes/b_buscar_des.png");
			btnBuscar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					String textIngresado = getTxtBusqueda().getText().trim();
					List<T> matchItems = new ArrayList<T>();
					for(T i : items) {
						if(i.toString().toLowerCase().contains(textIngresado.toLowerCase())) {
							matchItems.add(i);
						}
					}
					getPanTablaItems().limpiar();
					if(matchItems.isEmpty() && StringUtil.isNullOrEmpty(textIngresado) ) {
						getPanTablaItems().agregarElementos(items);
					} else {
						getPanTablaItems().agregarElementos(matchItems);	
					}
				}

			});
		}
		return btnBuscar;
	}
	
	public T getSelectedItem() {
		return selectedItem;
	}

}