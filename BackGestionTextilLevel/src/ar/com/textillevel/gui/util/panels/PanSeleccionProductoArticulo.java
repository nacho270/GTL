package ar.com.textillevel.gui.util.panels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.ventas.ProductoArticulo;
import ar.com.textillevel.entidades.ventas.articulos.Articulo;
import ar.com.textillevel.entidades.ventas.articulos.Color;
import ar.com.textillevel.entidades.ventas.articulos.DibujoEstampado;
import ar.com.textillevel.entidades.ventas.articulos.GamaColor;
import ar.com.textillevel.entidades.ventas.articulos.TipoArticulo;
import ar.com.textillevel.entidades.ventas.articulos.VarianteEstampado;
import ar.com.textillevel.entidades.ventas.productos.Producto;
import ar.com.textillevel.facade.api.remote.ArticuloFacadeRemote;
import ar.com.textillevel.facade.api.remote.DibujoEstampadoFacadeRemote;
import ar.com.textillevel.facade.api.remote.GamaColorFacadeRemote;
import ar.com.textillevel.facade.api.remote.ProductoFacadeRemote;
import ar.com.textillevel.facade.api.remote.TipoArticuloFacadeRemote;
import ar.com.textillevel.gui.util.ProductoArticuloHelper;
import ar.com.textillevel.gui.util.ProductosAndPreciosHelper;
import ar.com.textillevel.util.GTLBeanFactory;

public class PanSeleccionProductoArticulo extends JPanel {

	private static final long serialVersionUID = -42170115610350329L;

	private JComboBox cmbTipoProducto;
	private JComboBox cmbTipoArticulo;
	private JComboBox cmbArticulo;
	private FWCheckBoxList<ProductoArticulo> checkBoxListProductoArticulo;
	private PanelTablaProductoArticulo panProdArtSel;

	private boolean acepto;
	private List<ProductoArticulo> productoSelectedList;
	private List<Producto> allProductoList;

	private JPanel subPanGama;
	private JComboBox cmbGama;

	private JPanel subPanDibujo;
	private JComboBox cmbDibujo;

	private JDialog owner;

	private ArticuloFacadeRemote articuloFacade;

	private List<TipoArticulo> allTipoArticuloList;
	private List<Articulo> allArticulosList;
	private List<Articulo> articuloFilterList;
	private List<GamaColor> allGamas;
	private List<DibujoEstampado> allDibujos;
	private Cliente cliente;

	public PanSeleccionProductoArticulo(JDialog owner, Cliente cliente, List<ProductoArticulo> productoSelectedList) {
		this.owner = owner;
		this.cliente = cliente;
		this.productoSelectedList = new ArrayList<ProductoArticulo>(productoSelectedList);
		this.allGamas = GTLBeanFactory.getInstance().getBean2(GamaColorFacadeRemote.class).getAllOrderByName();
		this.allDibujos = GTLBeanFactory.getInstance().getBean2(DibujoEstampadoFacadeRemote.class).getAllOrderByNombre();
		setUpComponentes();
		setDatos();
	}

	public PanSeleccionProductoArticulo(JDialog owner, Cliente cliente, List<ProductoArticulo> productoSelectedList, List<Articulo> articuloFilterList) {
		this.owner = owner;
		this.cliente = cliente;
		this.productoSelectedList = new ArrayList<ProductoArticulo>(productoSelectedList);
		this.articuloFilterList = articuloFilterList;
		this.allGamas = GTLBeanFactory.getInstance().getBean2(GamaColorFacadeRemote.class).getAllOrderByName();
		this.allDibujos = GTLBeanFactory.getInstance().getBean2(DibujoEstampadoFacadeRemote.class).getAllOrderByNombre();
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
		gridBagConstraints.insets = new Insets(5,5,5,5);
		gridBagConstraints.weightx = 0.1;
		add(getSubPanGama(), gridBagConstraints);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 0.1;
		gridBagConstraints.insets = new Insets(5,5,5,5);
		add(getSubPanDibujo(), gridBagConstraints);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 5;
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
		gridBagConstraints.gridy = 6;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.weighty = 0.3;			
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.insets = new Insets(5,5,5,5);
		add(getPanProdArtSel(), gridBagConstraints);			
	}

	private JPanel getSubPanGama() {
		if(subPanGama == null) {
			this.subPanGama = new JPanel();
			this.subPanGama.setLayout(new GridBagLayout());
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.weightx = 0.45;
			gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
			this.subPanGama.add(new JLabel("GAMA: "), gridBagConstraints);
			gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.weightx = 0.55;
			gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
			this.subPanGama.add(getCmbGama(), gridBagConstraints);
			this.subPanGama.setVisible(false);
		}
		return subPanGama;
	}

	private JPanel getSubPanDibujo() {
		if(subPanDibujo == null) {
			this.subPanDibujo = new JPanel();
			this.subPanDibujo.setLayout(new GridBagLayout());
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.weightx = 0.6;
			gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
			this.subPanDibujo.add(new JLabel("DIBUJO: "), gridBagConstraints);
			gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.weightx = 0.45;
			gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
			this.subPanDibujo.add(getCmbDibujo(), gridBagConstraints);
			this.subPanDibujo.setVisible(false);
		}
		return subPanDibujo;
	}

	private void filtrar() {
		List<ProductoArticulo> productoMatchedList = new ArrayList<ProductoArticulo>();
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
		GamaColor gama = getCmbGama().getSelectedIndex() == 0 ? null : (GamaColor)getCmbGama().getSelectedItem();
		if(articulo == null || tipoProd == null) {
			return;
		}
		
		DibujoEstampado dibujo = getCmbDibujo().getSelectedIndex() == 0 ? null : (DibujoEstampado)getCmbDibujo().getSelectedItem();
		if(tipoProd == ETipoProducto.ESTAMPADO) {
			if(dibujo != null) {
				dibujo = GTLBeanFactory.getInstance().getBean2(DibujoEstampadoFacadeRemote.class).getByIdEager(dibujo.getId());
				for(VarianteEstampado ve : dibujo.getVariantes()) {
					ProductoArticulo pa = new ProductoArticulo();
					pa.setArticulo(articulo);
					pa.setDibujo(dibujo);
					pa.setVariante(ve);
					pa.setProducto(producto);
					productoMatchedList.add(pa);
				}
			}
		} else if(tipoProd == ETipoProducto.TENIDO) {
			if(gama!= null) {
				for(Color c : gama.getColores()) {
					ProductoArticulo pa = new ProductoArticulo();
					pa.setArticulo(articulo);
					pa.setGamaColor(gama);
					pa.setColor(c);
					pa.setProducto(producto);
					productoMatchedList.add(pa);
				}
			}
		} else {
			ProductoArticulo pa = new ProductoArticulo();
			pa.setArticulo(articulo);
			pa.setProducto(producto);
			productoMatchedList.add(pa);
		}
		if(!productoMatchedList.isEmpty()) {
			getFWCheckBoxList().setValues(productoMatchedList.toArray(new Object[productoMatchedList.size()]));
			for(ProductoArticulo p : productoMatchedList) {
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
					ProductoArticulo pa = (ProductoArticulo)item;
					ProductosAndPreciosHelper helper = new ProductosAndPreciosHelper(owner, cliente);
					if(seleccionado) {
						BigDecimal precio = helper.getPrecio(pa);
						if(precio == null) {
							getFWCheckBoxList().setAllSelectedItems(false);
						} else {
							if(!productoSelectedList.contains(pa)) {
								pa.setPrecioCalculado(precio.floatValue());
								productoSelectedList.add(pa);
								getPanProdArtSel().limpiar();
								getPanProdArtSel().agregarElementos(productoSelectedList);
							}
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
						ETipoProducto tpSel = getCmbTipoProducto().getSelectedIndex() == 0 ? null : (ETipoProducto)getCmbTipoProducto().getSelectedItem();
						boolean esTenidoOrEstampado = tpSel != null && (tpSel == ETipoProducto.TENIDO || tpSel == ETipoProducto.ESTAMPADO);
						getSubPanGama().setVisible(esTenidoOrEstampado);
						getSubPanDibujo().setVisible(tpSel != null && tpSel == ETipoProducto.ESTAMPADO);
						filtrar();
					}
				}
			});
		}
		return cmbTipoProducto;
	}

	private JComboBox getCmbDibujo() {
		if(cmbDibujo == null) {
			cmbDibujo = new JComboBox();
			cmbDibujo.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if(e.getStateChange() == ItemEvent.SELECTED) {
						filtrar();
					}
				}
			});
			GuiUtil.llenarCombo(cmbDibujo, allDibujos, false);
			cmbDibujo.insertItemAt("", 0);
			cmbDibujo.setSelectedIndex(0);
			
		}
		return cmbDibujo;
	}

	private JComboBox getCmbGama() {
		if(cmbGama == null) {
			cmbGama = new JComboBox();
			cmbGama.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if(e.getStateChange() == ItemEvent.SELECTED) {
						filtrar();
					}
				}
			});
			GuiUtil.llenarCombo(cmbGama, allGamas, false);
			cmbGama.insertItemAt("", 0);
			cmbGama.setSelectedIndex(0);
		}
		return cmbGama;
	}
	
	private List<ETipoProducto> getTipoProductos() {
		Set<ETipoProducto> tpSet = new HashSet<ETipoProducto>();
		for(ETipoProducto tp : ETipoProducto.values()) {
			tpSet.add(tp);
		}
		tpSet.remove(ETipoProducto.REPROCESO_SIN_CARGO);
		return new ArrayList<ETipoProducto>(tpSet);
	}

	private ArticuloFacadeRemote getArticuloFacade() {
		if(articuloFacade == null) {
			articuloFacade = GTLBeanFactory.getInstance().getBean2(ArticuloFacadeRemote.class);
		}
		return articuloFacade;
	}

	public List<ProductoArticulo> getProductoSelectedList() {
		ProductoArticuloHelper prodArtHelper = new ProductoArticuloHelper();
		return prodArtHelper.getPersistentInstances(productoSelectedList);
	}

	private class PanelTablaProductoArticulo extends PanelTabla<ProductoArticulo> {
		
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
		protected void agregarElemento(ProductoArticulo elemento) {
			Object[] row = new Object[CANT_COLS];
			row[COL_PA] = elemento.toString();
			row[COL_OBJ] = elemento;
			getTabla().addRow(row);
		}

		@Override
		protected ProductoArticulo getElemento(int fila) {
			return (ProductoArticulo)getTabla().getValueAt(fila, COL_OBJ);
		}

		@Override
		protected String validarElemento(int fila) {
			return null;
		}

		@Override
		public boolean validarQuitar() {
			int selectedRow = getTabla().getSelectedRow();
			if(selectedRow != -1) {
				ProductoArticulo elemento = getElemento(selectedRow);
				PanSeleccionProductoArticulo.this.productoSelectedList.remove(elemento);
				PanSeleccionProductoArticulo.this.getFWCheckBoxList().setAllSelectedItems(false);
			}
			return true;
		}

	}

}