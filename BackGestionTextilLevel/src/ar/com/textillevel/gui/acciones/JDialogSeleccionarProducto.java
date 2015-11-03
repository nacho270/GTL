package ar.com.textillevel.gui.acciones;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import ar.com.fwcommon.componentes.FWCheckBoxList;
import ar.com.fwcommon.componentes.FWJTable;
import ar.com.fwcommon.componentes.PanelTabla;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.entidades.enums.ETipoProducto;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.ventas.ProductoArticulo;
import ar.com.textillevel.entidades.ventas.articulos.Articulo;
import ar.com.textillevel.entidades.ventas.articulos.GamaColor;
import ar.com.textillevel.entidades.ventas.articulos.TipoArticulo;
import ar.com.textillevel.entidades.ventas.articulos.VarianteEstampado;
import ar.com.textillevel.entidades.ventas.productos.Producto;
import ar.com.textillevel.entidades.ventas.productos.ProductoEstampado;
import ar.com.textillevel.entidades.ventas.productos.ProductoTenido;
import ar.com.textillevel.facade.api.remote.ArticuloFacadeRemote;
import ar.com.textillevel.facade.api.remote.TipoArticuloFacadeRemote;
import ar.com.textillevel.gui.util.ProductoArticuloHelper;
import ar.com.textillevel.gui.util.ProductosAndPreciosHelper;
import ar.com.textillevel.gui.util.ProductosAndPreciosHelper.ResultProductosTO;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogSeleccionarProducto extends JDialog {

	private static final long serialVersionUID = 7364390484648139031L;

	private JButton btnAceptar;
	private JButton btnCancelar;

	private JComboBox cmbTipoArticulo;
	private JComboBox cmbArticulo;
	private JComboBox cmbTipoProducto;
	private JComboBox cmbGama;
	private FWCheckBoxList<Producto> checkBoxList;
	private PanelTablaProductoArticulo panProdArtSel;

	private boolean acepto;
	private List<ProductoArticulo> productoSelectedList;
	private List<Producto> allProductoList = new ArrayList<Producto>();

	private JPanel pnlBotones;
	private JPanel pnlDatos;
	private JLabel lblGama;

	private ArticuloFacadeRemote articuloFacade;

	private List<TipoArticulo> allTipoArticuloList;
	private List<Articulo> allArticulosList;
	private List<Articulo> articuloFilterList;
	private Cliente cliente;

	public JDialogSeleccionarProducto(JDialog owner, Cliente cliente, List<ProductoArticulo> productoSelectedList) {
		super(owner);
		this.cliente = cliente;
		this.productoSelectedList = new ArrayList<ProductoArticulo>(productoSelectedList);
		setUpComponentes();
		setUpScreen();
		setDatos();
	}

	public JDialogSeleccionarProducto(JDialog owner, Cliente cliente, List<ProductoArticulo> productoSelectedList, List<Articulo> articuloFilterList) {
		super(owner);
		this.cliente = cliente;
		this.productoSelectedList = new ArrayList<ProductoArticulo>(productoSelectedList);
		this.articuloFilterList = articuloFilterList;
		setUpComponentes();
		setUpScreen();
		setDatos();
	}

	private void setDatos() {
		for(ProductoArticulo productoArticulo : productoSelectedList) {
			getFWCheckBoxList().setSelectedValue(productoArticulo.getProducto(), false);
		}
		getPanProdArtSel().agregarElementos(productoSelectedList);
	}

	private void setUpScreen(){
		setTitle("Selección de Productos");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setSize(new Dimension(400, 550));
		setResizable(false);
		setModal(true);
	}

	private void setUpComponentes(){
		add(getPanelDatos(),BorderLayout.CENTER);
		add(getPanelBotones(),BorderLayout.SOUTH);
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

	private JPanel getPanelDatos() {
		if(pnlDatos == null){
			pnlDatos = new JPanel();
			pnlDatos.setLayout(new GridBagLayout());
			
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
			pnlDatos.add(new JLabel("TIPO DE ARTÍCULO: "), gridBagConstraints);
			
			gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.insets = new Insets(5,5,5,5);
			pnlDatos.add(getCmbTipoArticulo(), gridBagConstraints);

			gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 1;
			gridBagConstraints.insets = new Insets(5,5,5,5);
			pnlDatos.add(new JLabel("ARTÍCULO: "), gridBagConstraints);

			gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 1;
			gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints.insets = new Insets(5,5,5,5);
			pnlDatos.add(getCmbArticulo(), gridBagConstraints);

			gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 2;
			gridBagConstraints.insets = new Insets(5,5,5,5);
			pnlDatos.add(new JLabel("TIPO DE PRODUCTO: "), gridBagConstraints);
			
			gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 2;
			gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints.insets = new Insets(5,5,5,5);
			pnlDatos.add(getCmbTipoProducto(), gridBagConstraints);			
			
			gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 3;
			gridBagConstraints.insets = new Insets(5,5,5,5);
			pnlDatos.add(getLblGama(), gridBagConstraints);

			gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 3;
			gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints.insets = new Insets(5,5,5,5);
			pnlDatos.add(getCmbGama(), gridBagConstraints);

			gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 4;
			gridBagConstraints.gridwidth = 2;
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.weightx = 1;
			gridBagConstraints.weighty = 0.7;
			JScrollPane scrollPane = new JScrollPane(getFWCheckBoxList());
			scrollPane.setBorder(BorderFactory.createTitledBorder("PRODUCTOS"));
			pnlDatos.add(scrollPane, gridBagConstraints);

			getPanProdArtSel().setBorder(BorderFactory.createTitledBorder("PRODUCTOS - ARTÍCULOS SELECCIONADOS:"));
			gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 5;
			gridBagConstraints.gridwidth = 2;
			gridBagConstraints.weighty = 0.3;			
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.insets = new Insets(5,5,5,5);
			pnlDatos.add(getPanProdArtSel(), gridBagConstraints);			
			
		}
		return pnlDatos;
	}

	private JLabel getLblGama() {
		if(lblGama == null) {
			lblGama = new JLabel("GAMA: ");
			lblGama.setVisible(false);
		}
		return lblGama;
	}

	private void filtrar() {
		ETipoProducto tipoProd = getCmbTipoProducto().getSelectedIndex() == 0 ? null : (ETipoProducto)getCmbTipoProducto().getSelectedItem();
		GamaColor gama = getCmbGama().getSelectedIndex() == 0 ? null : (GamaColor)getCmbGama().getSelectedItem();
		List<Producto> productoMatchedList = new ArrayList<Producto>();
		getFWCheckBoxList().setAllSelectedItems(false);
		for(Producto p : allProductoList) {
			boolean cumpleTipoProd = tipoProd == null || tipoProd == p.getTipo();
			boolean cumpleGama = true;
			if(p.getTipo() == ETipoProducto.TENIDO) {
				cumpleGama = gama==null || gama.equals(((ProductoTenido)p).getGamaColor()); 
			}
			if(p.getTipo() == ETipoProducto.ESTAMPADO) {
				VarianteEstampado variante = ((ProductoEstampado)p).getVariante();
				cumpleGama = gama == null || (variante.getGama() != null && gama.equals(variante.getGama())); 
			}
			if(cumpleTipoProd && cumpleGama) {
				productoMatchedList.add(p);
			}
		}
		checkBoxList.setValues(productoMatchedList.toArray(new Object[productoMatchedList.size()]));
		for(ProductoArticulo p : productoSelectedList) {
			checkBoxList.setSelectedValue(p.getProducto(), false);
		}
	}

	private JButton getBtnAceptar() {
		if(btnAceptar == null){
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					acepto = true;
					dispose();
				}

			});
		}
		return btnAceptar;
	}

	private JButton getBtnCancelar() {
		if(btnCancelar == null){
			btnCancelar = new JButton("Cancelar");
			btnCancelar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
		}
		return btnCancelar;
	}

	private FWCheckBoxList<Producto> getFWCheckBoxList() {
		if(checkBoxList == null) {
			checkBoxList = new FWCheckBoxList<Producto>() {

				private static final long serialVersionUID = -8028977693425752374L;

				@Override
				public void itemListaSeleccionado(Object item, boolean seleccionado) {
					Producto prod = (Producto)item;
					Articulo articulo = (Articulo)getCmbArticulo().getSelectedItem();
					if(seleccionado) {
						if(!existeProductoEnLista(productoSelectedList, prod, articulo)) {
							ProductoArticulo pa = new ProductoArticulo();
							pa.setProducto(prod);
							if(ETipoProducto.dependienteDeArticulo(prod.getTipo())) {
								pa.setArticulo(articulo);
							}
							productoSelectedList.add(pa);
						}
					} else {
						List<ProductoArticulo> prodsToRemove = new ArrayList<ProductoArticulo>();
						for(ProductoArticulo pa : productoSelectedList) {
							if(pa.getProducto().equals(item) && (pa.getArticulo() !=null &&  pa.getArticulo().equals(getCmbArticulo().getSelectedItem()))) {
								prodsToRemove.add(pa);
							}
						}
						productoSelectedList.remove(prodsToRemove);
					}

					getPanProdArtSel().limpiar();
					getPanProdArtSel().agregarElementos(productoSelectedList);
				}

				private boolean existeProductoEnLista(List<ProductoArticulo> productoSelectedList, Producto prod, Articulo articulo) {
					for(ProductoArticulo pa : productoSelectedList) {
						if(pa.getProducto().equals(prod) && (pa.getArticulo() == null || pa.getArticulo().equals(articulo))) {
							return true;
						}
					}
					return false;
				}

			};
			checkBoxList.setValues(allProductoList.toArray(new Object[allProductoList.size()]));
		}
		return checkBoxList;
	}

	private PanelTablaProductoArticulo getPanProdArtSel() {
		if(panProdArtSel == null) {
			this.panProdArtSel = new PanelTablaProductoArticulo(); 
		}
		return panProdArtSel;
	}
	
	private void resetProductoList() {
		List<Producto> productoResultList = new ArrayList<Producto>();
		if(getCmbArticulo().getSelectedIndex() != 0) {
			ProductosAndPreciosHelper helper = new ProductosAndPreciosHelper(JDialogSeleccionarProducto.this, (Articulo)getCmbArticulo().getSelectedItem(), cliente);
			ResultProductosTO result = helper.getInfoProductosAndListaDePrecios();
			if(result != null) {
				this.allProductoList = result.productos;
			}
			Collections.sort(productoResultList, new Comparator<Producto>() {
				public int compare(Producto o1, Producto o2) {
					return o1.getDescripcion().compareTo(o2.getDescripcion());
				}
			});
		} else {
			this.allProductoList = productoResultList;
		}
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
						resetProductoList();
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
						resetProductoList();
						recargarComboTipoProducto();
						getFWCheckBoxList().setAllSelectedItems(false);
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

	private void recargarComboGama() {
		ETipoProducto tpSel = getCmbTipoProducto().getSelectedIndex() == 0 ? null : (ETipoProducto)getCmbTipoProducto().getSelectedItem();
		Set<GamaColor> resultGamas = new HashSet<GamaColor>();
		for(Producto producto : allProductoList) {
			if(tpSel == ETipoProducto.ESTAMPADO && producto.getTipo() == ETipoProducto.ESTAMPADO) {
				VarianteEstampado variante = ((ProductoEstampado)producto).getVariante();
				if(variante.getGama() != null) {
					resultGamas.add(variante.getGama());
				}
			} else if(tpSel == ETipoProducto.TENIDO && producto.getTipo() == ETipoProducto.TENIDO) {
				GamaColor gamaColor = ((ProductoTenido)producto).getGamaColor();
				if(gamaColor !=null) {
					resultGamas.add(gamaColor);
				}
			}
		}
		GuiUtil.llenarCombo(getCmbGama(), new ArrayList<GamaColor>(resultGamas), true);
		getCmbGama().insertItemAt("", 0);
		getCmbGama().setSelectedIndex(0);
	}

	public boolean isAcepto() {
		return acepto;
	}

	private JComboBox getCmbTipoProducto() {
		if(cmbTipoProducto == null) {
			cmbTipoProducto = new JComboBox();
			GuiUtil.llenarCombo(cmbTipoProducto, getTipoProductosEnListaDePrecios(), false);
			cmbTipoProducto.insertItemAt("", 0);
			cmbTipoProducto.setSelectedIndex(0);
			cmbTipoProducto.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if(e.getStateChange() == ItemEvent.SELECTED) {
						ETipoProducto tpSel = getCmbTipoProducto().getSelectedIndex() == 0 ? null : (ETipoProducto)getCmbTipoProducto().getSelectedItem();
						boolean esTenidoOrEstampado = tpSel != null && (tpSel == ETipoProducto.TENIDO || tpSel == ETipoProducto.ESTAMPADO);
						if(esTenidoOrEstampado) {
							recargarComboGama();
						}
						getCmbGama().setVisible(esTenidoOrEstampado);
						getLblGama().setVisible(esTenidoOrEstampado);
						filtrar();
					}
				}
			});
		}
		return cmbTipoProducto;
	}

	private JComboBox getCmbGama() {
		if(cmbGama == null) {
			cmbGama = new JComboBox();
			cmbGama.setVisible(false);
			cmbGama.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if(e.getStateChange() == ItemEvent.SELECTED) {
						filtrar();
					}
				}
			});
		}
		return cmbGama;
	}
	
	private void recargarComboTipoProducto() {
		GuiUtil.llenarCombo(getCmbTipoProducto(), getTipoProductosEnListaDePrecios(), true);
		getCmbTipoProducto().insertItemAt("", 0);
		getCmbTipoProducto().setSelectedIndex(0);
	}
	
	private List<ETipoProducto> getTipoProductosEnListaDePrecios() {
		Set<ETipoProducto> tpSet = new HashSet<ETipoProducto>();
		for(Producto pr : allProductoList) {
			tpSet.add(pr.getTipo());
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
				JDialogSeleccionarProducto.this.productoSelectedList.remove(elemento);
				JDialogSeleccionarProducto.this.getFWCheckBoxList().setAllSelectedItems(false);
			}
			return true;
		}

	}

}