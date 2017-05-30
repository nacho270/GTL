package ar.com.textillevel.gui.util.panels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import ar.com.fwcommon.componentes.FWCheckBoxList;
import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTable;
import ar.com.fwcommon.componentes.PanelTabla;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.entidades.enums.ETipoProducto;
import ar.com.textillevel.entidades.ventas.ProductoArticulo;
import ar.com.textillevel.entidades.ventas.ProductoArticuloParcial;
import ar.com.textillevel.entidades.ventas.articulos.Articulo;
import ar.com.textillevel.entidades.ventas.articulos.TipoArticulo;
import ar.com.textillevel.entidades.ventas.productos.Producto;
import ar.com.textillevel.facade.api.remote.ArticuloFacadeRemote;
import ar.com.textillevel.facade.api.remote.ProductoFacadeRemote;
import ar.com.textillevel.facade.api.remote.TipoArticuloFacadeRemote;
import ar.com.textillevel.util.GTLBeanFactory;

public class PanSeleccionProductoArticuloParcial extends JPanel {

	private static final long serialVersionUID = -42170115610350329L;

	private JComboBox cmbTipoProducto;
	private JComboBox cmbTipoArticulo;
	private JComboBox cmbArticulo;
	private FWCheckBoxList<ProductoArticulo> checkBoxListProductoArticulo;
	private PanelTablaProductoArticulo panProdArtSel;

	private boolean acepto;
	private List<ProductoArticuloParcial> productoSelectedList;
	private List<Producto> allProductoList;

	private JDialog owner;

	private ArticuloFacadeRemote articuloFacade;

	private List<TipoArticulo> allTipoArticuloList;
	private List<Articulo> allArticulosList;
	private List<Articulo> articuloFilterList;

	public PanSeleccionProductoArticuloParcial(JDialog owner, List<ProductoArticuloParcial> productoSelectedList, List<Articulo> articuloFilterList) {
		this.owner = owner;
		this.productoSelectedList = new ArrayList<ProductoArticuloParcial>(productoSelectedList);
		this.articuloFilterList = articuloFilterList;
		setUpComponentes();
		setDatos();
	}

	private void setDatos() {
		getPanProdArtSel().agregarElementos(productoSelectedList);
	}

	private void setUpComponentes() {
		setLayout(new GridBagLayout());
		
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		add(new JLabel("TIPO DE ARTÍCULO: "), gridBagConstraints);
		
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.insets = new Insets(5,5,5,5);
		add(getCmbTipoArticulo(), gridBagConstraints);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.insets = new Insets(5,5,5,5);
		add(new JLabel("ARTÍCULO: "), gridBagConstraints);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new Insets(5,5,5,5);
		add(getCmbArticulo(), gridBagConstraints);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.insets = new Insets(5,5,5,5);
		add(new JLabel("TIPO DE PRODUCTO: "), gridBagConstraints);
		
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new Insets(5,5,5,5);
		add(getCmbTipoProducto(), gridBagConstraints);			

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 0.8;
		gridBagConstraints.weighty = 0.7;
		JScrollPane scrollPane = new JScrollPane(getFWCheckBoxList());
		scrollPane.setBorder(BorderFactory.createTitledBorder("PRODUCTOS"));
		add(scrollPane, gridBagConstraints);

		getPanProdArtSel().setBorder(BorderFactory.createTitledBorder("PRODUCTOS - ARTÍCULOS SELECCIONADOS:"));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.weighty = 0.3;			
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.insets = new Insets(5,5,5,5);
		add(getPanProdArtSel(), gridBagConstraints);			
	}

	private void filtrar() {
		List<ProductoArticuloParcial> productoMatchedList = new ArrayList<ProductoArticuloParcial>();
		getFWCheckBoxList().setAllSelectedItems(false);
		ETipoProducto tipoProd = getCmbTipoProducto().getSelectedIndex() == 0 ? null : (ETipoProducto)getCmbTipoProducto().getSelectedItem();
		Articulo articulo = getCmbArticulo().getSelectedIndex() == 0 ? null : (Articulo)getCmbArticulo().getSelectedItem();
		if(tipoProd == null) {
			return;
		}
		Producto producto = buscarProducto(tipoProd);
		if(producto == null) {
			return;
		}
		
		ProductoArticuloParcial pa = new ProductoArticuloParcial();
		pa.setArticulo(articulo);
		pa.setProducto(producto);
		productoMatchedList.add(pa);
		
		if(!productoMatchedList.isEmpty()) {
			getFWCheckBoxList().setValues(productoMatchedList.toArray(new Object[productoMatchedList.size()]));
			for(ProductoArticuloParcial p : productoMatchedList) {
				getFWCheckBoxList().setSelectedValue(p.getProducto(), false);
			}
		}
	}

	private Producto buscarProducto(ETipoProducto tipoProd) {
		for(Producto p : getAllProductoList()) {
			if(p.getTipo() == tipoProd) {
				return p;
			}
		}
		FWJOptionPane.showErrorMessage(owner, "Debe agregar el Producto '" + tipoProd + "' desde el módulo 'Administrar Productos'.", "Error");
		return null;
	}

	private List<Producto> getAllProductoList() {
		if(allProductoList == null) {
			allProductoList = GTLBeanFactory.getInstance().getBean2(ProductoFacadeRemote.class).getAllOrderByName();
		}
		return allProductoList;
	}
	
	private FWCheckBoxList<ProductoArticulo> getFWCheckBoxList() {
		if(checkBoxListProductoArticulo == null) {
			checkBoxListProductoArticulo = new FWCheckBoxList<ProductoArticulo>() {

				private static final long serialVersionUID = -8028977693425752374L;

				@Override
				public void itemListaSeleccionado(Object item, boolean seleccionado) {
					ProductoArticuloParcial pa = (ProductoArticuloParcial)item;
					if(seleccionado) {
						if(!productoSelectedList.contains(pa)) {
							productoSelectedList.add(pa);
							getPanProdArtSel().limpiar();
							getPanProdArtSel().agregarElementos(productoSelectedList);
						}
					}

				}

			};
		}
		return checkBoxListProductoArticulo;
	}

	private PanelTablaProductoArticulo getPanProdArtSel() {
		if(panProdArtSel == null) {
			this.panProdArtSel = new PanelTablaProductoArticulo(); 
		}
		return panProdArtSel;
	}

	private JComboBox getCmbTipoArticulo() {
		if(cmbTipoArticulo == null) {
			cmbTipoArticulo = new JComboBox();
			GuiUtil.llenarCombo(cmbTipoArticulo, getTipoArticuloList(), false);
			cmbTipoArticulo.insertItemAt("",0);
			cmbTipoArticulo.setSelectedIndex(0);
			cmbTipoArticulo.addItemListener(new ItemListener() {

				public void itemStateChanged(ItemEvent e) {
					if(e.getStateChange() == ItemEvent.SELECTED) {
						recargarComboArticulos();
						filtrar();
					}
				}

			});

		}
		return cmbTipoArticulo;
	}

	private JComboBox getCmbArticulo() {
		if(cmbArticulo == null) {
			cmbArticulo = new JComboBox();
			GuiUtil.llenarCombo(cmbArticulo, getArticuloList(), false);
			cmbArticulo.insertItemAt("",0);
			cmbArticulo.setSelectedIndex(0);
			cmbArticulo.addItemListener(new ItemListener() {

				public void itemStateChanged(ItemEvent e) {
					if(e.getStateChange() == ItemEvent.SELECTED) {
						filtrar();
					}
				}

			});
			
		}
		return cmbArticulo;
	}

	private List<TipoArticulo> getTipoArticuloList() {
		if(allTipoArticuloList == null) {
			this.allTipoArticuloList = GTLBeanFactory.getInstance().getBean2(TipoArticuloFacadeRemote.class).getAllTipoArticulos();
		}
		return allTipoArticuloList;
	}

	private List<Articulo> getArticuloList() {
		if(allArticulosList == null) {
			this.allArticulosList = getArticuloFacade().getAllOrderByName();
		}
		List<Articulo> articuloResultList = new ArrayList<Articulo>();
		if(articuloFilterList == null) {
			articuloResultList.addAll(allArticulosList);
		} else {
			articuloResultList.addAll(articuloFilterList);
		}
		return articuloResultList;
	}

	private void recargarComboArticulos() {
		TipoArticulo selectedItem = getCmbTipoArticulo().getSelectedIndex() == 0 ? null : (TipoArticulo)getCmbTipoArticulo().getSelectedItem();
		List<Articulo> result = new ArrayList<Articulo>();
		//Filtro por tipo de articulo
		for(Articulo a : getArticuloList()) {
			if(selectedItem == null || selectedItem.equals(a.getTipoArticulo())) {
				result.add(a);
			}
		}
		GuiUtil.llenarCombo(getCmbArticulo(), result, true);
		getCmbArticulo().insertItemAt("", 0);
		getCmbArticulo().setSelectedIndex(0);
	}

	public boolean isAcepto() {
		return acepto;
	}

	private JComboBox getCmbTipoProducto() {
		if(cmbTipoProducto == null) {
			cmbTipoProducto = new JComboBox();
			GuiUtil.llenarCombo(cmbTipoProducto, getTipoProductos(), false);
			cmbTipoProducto.insertItemAt("", 0);
			cmbTipoProducto.setSelectedIndex(0);
			cmbTipoProducto.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if(e.getStateChange() == ItemEvent.SELECTED) {
						filtrar();
					}
				}
			});
		}
		return cmbTipoProducto;
	}

	private List<ETipoProducto> getTipoProductos() {
		List<ETipoProducto> tpList = new ArrayList<ETipoProducto>();
		tpList.add(ETipoProducto.ESTAMPADO);
		tpList.add(ETipoProducto.TENIDO);
		return tpList;
	}

	private ArticuloFacadeRemote getArticuloFacade() {
		if(articuloFacade == null) {
			articuloFacade = GTLBeanFactory.getInstance().getBean2(ArticuloFacadeRemote.class);
		}
		return articuloFacade;
	}

	private class PanelTablaProductoArticulo extends PanelTabla<ProductoArticuloParcial> {
		
		private static final long serialVersionUID = 1L;
		
		private static final int CANT_COLS = 2;
		private static final int COL_PA = 0;
		private static final int COL_OBJ = 1;

		public PanelTablaProductoArticulo() {
			getBotonAgregar().setVisible(false);
		}

		@Override
		protected FWJTable construirTabla() {
			FWJTable tablaPA = new FWJTable(0, CANT_COLS);
			tablaPA.setStringColumn(COL_PA, "PRODUCTO - ARTÍCULO", 300, 300, true);
			tablaPA.setStringColumn(COL_OBJ, "", 0, 0, true);
			tablaPA.setHeaderAlignment(COL_PA, FWJTable.CENTER_ALIGN);
			return tablaPA;
		}

		@Override
		protected void agregarElemento(ProductoArticuloParcial elemento) {
			Object[] row = new Object[CANT_COLS];
			row[COL_PA] = elemento.toString();
			row[COL_OBJ] = elemento;
			getTabla().addRow(row);
		}

		@Override
		protected ProductoArticuloParcial getElemento(int fila) {
			return (ProductoArticuloParcial)getTabla().getValueAt(fila, COL_OBJ);
		}

		@Override
		protected String validarElemento(int fila) {
			return null;
		}

		@Override
		public boolean validarQuitar() {
			int selectedRow = getTabla().getSelectedRow();
			if(selectedRow != -1) {
				ProductoArticuloParcial elemento = getElemento(selectedRow);
				PanSeleccionProductoArticuloParcial.this.productoSelectedList.remove(elemento);
				PanSeleccionProductoArticuloParcial.this.getFWCheckBoxList().setAllSelectedItems(false);
			}
			return true;
		}

	}

	public List<ProductoArticuloParcial> getProductoSelectedList() {
		return productoSelectedList;
	}

}