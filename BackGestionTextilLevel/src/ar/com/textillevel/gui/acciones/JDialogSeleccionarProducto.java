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

import ar.clarin.fwjava.componentes.CLCheckBoxList;
import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.util.GuiUtil;
import ar.com.textillevel.entidades.enums.ETipoProducto;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.ventas.articulos.Articulo;
import ar.com.textillevel.entidades.ventas.articulos.GamaColor;
import ar.com.textillevel.entidades.ventas.articulos.VarianteEstampado;
import ar.com.textillevel.entidades.ventas.cotizacion.DefinicionPrecio;
import ar.com.textillevel.entidades.ventas.cotizacion.VersionListaDePrecios;
import ar.com.textillevel.entidades.ventas.productos.Producto;
import ar.com.textillevel.entidades.ventas.productos.ProductoEstampado;
import ar.com.textillevel.entidades.ventas.productos.ProductoTenido;
import ar.com.textillevel.facade.api.remote.ArticuloFacadeRemote;
import ar.com.textillevel.gui.util.ProductosAndPreciosHelper;
import ar.com.textillevel.gui.util.ProductosAndPreciosHelper.ResultProductosTO;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogSeleccionarProducto extends JDialog {

	private static final long serialVersionUID = 7364390484648139031L;

	private JButton btnAceptar;
	private JButton btnCancelar;
	
	private JComboBox cmbArticulo;
	private JComboBox cmbTipoProducto;
	private JComboBox cmbGama;
	private CLCheckBoxList<Producto> checkBoxList;
	private boolean acepto;
	private List<Producto> productoSelectedList;
	private List<Producto> allProductoList;

	private JPanel pnlBotones;
	private JPanel pnlDatos;
	private JLabel lblGama;

	private ArticuloFacadeRemote articuloFacade;

	private List<Articulo> allArticulosList;
	private List<Articulo> articuloFilterList;
	private VersionListaDePrecios versionListaDePrecios;
	private Cliente cliente;

	public JDialogSeleccionarProducto(JDialog owner, Cliente cliente, List<Producto> productoSelectedList) {
		super(owner);
		this.cliente = cliente;
		this.productoSelectedList = new ArrayList<Producto>(productoSelectedList);
		allProductoList = getProductoList();
		setUpComponentes();
		setUpScreen();
		setDatos();
	}

	public JDialogSeleccionarProducto(JDialog owner, Cliente cliente, List<Producto> productoSelectedList, List<Articulo> articuloFilterList) {
		super(owner);
		this.cliente = cliente;
		this.productoSelectedList = new ArrayList<Producto>(productoSelectedList);
		allProductoList = getProductoList();		
		this.articuloFilterList = articuloFilterList;
		setUpComponentes();
		setUpScreen();
		setDatos();
	}

	private void setDatos() {
		for(Producto producto : productoSelectedList) {
			getClCheckBoxList().setSelectedValue(producto, false);
		}
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
			pnlDatos.add(new JLabel("TIPO DE PRODUCTO: "), gridBagConstraints);
			gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.insets = new Insets(5,5,5,5);
			pnlDatos.add(getCmbTipoProducto(), gridBagConstraints);

			gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 1;
			gridBagConstraints.insets = new Insets(5,5,5,5);
			pnlDatos.add(new JLabel("ARTICULO: "), gridBagConstraints);

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
			pnlDatos.add(getLblGama(), gridBagConstraints);

			gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 2;
			gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints.insets = new Insets(5,5,5,5);
			pnlDatos.add(getCmbGama(), gridBagConstraints);
			
			
			gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 3;
			gridBagConstraints.gridwidth = 2;
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.weightx = 1;
			gridBagConstraints.weighty = 1;
			JScrollPane scrollPane = new JScrollPane(getClCheckBoxList());
			scrollPane.setBorder(BorderFactory.createTitledBorder("PRODUCTOS"));
			pnlDatos.add(scrollPane, gridBagConstraints);
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
		Articulo artSelected = getCmbArticulo().getSelectedIndex() == 0 ? null : (Articulo)getCmbArticulo().getSelectedItem();
		ETipoProducto tipoProd = getCmbTipoProducto().getSelectedIndex() == 0 ? null : (ETipoProducto)getCmbTipoProducto().getSelectedItem();
		GamaColor gama = getCmbGama().getSelectedIndex() == 0 ? null : (GamaColor)getCmbGama().getSelectedItem();
		List<Producto> productoMatchedList = new ArrayList<Producto>();
		getClCheckBoxList().setAllSelectedItems(false);
		for(Producto p : allProductoList) {
			boolean cumpleTipoProd = tipoProd == null || tipoProd == p.getTipo();
			boolean cumpleArticulo = artSelected == null || artSelected.equals(p.getArticulo());
			boolean cumpleGama = true;
			if(p.getTipo() == ETipoProducto.TENIDO) {
				cumpleGama = gama==null || gama.equals(((ProductoTenido)p).getGamaColor()); 
			}
			if(p.getTipo() == ETipoProducto.ESTAMPADO) {
				VarianteEstampado variante = ((ProductoEstampado)p).getVariante();
				cumpleGama = gama == null || (variante.getGama() != null && gama.equals(variante.getGama())); 
			}
			if(cumpleArticulo && cumpleTipoProd && cumpleGama) {
				productoMatchedList.add(p);
			}
		}
		checkBoxList.setValues(productoMatchedList.toArray(new Object[productoMatchedList.size()]));
	}

	private JButton getBtnAceptar() {
		if(btnAceptar == null){
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(validar()) {
						acepto = true;
						dispose();
					}
				}

				private boolean validar() {
					if(getClCheckBoxList().getSelectedValues().length == 0){
						CLJOptionPane.showErrorMessage(JDialogSeleccionarProducto.this, "Debe seleccionar al menos un producto.", JDialogSeleccionarProducto.this.getTitle());
						return false;
					}
					return true;
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

	private CLCheckBoxList<Producto> getClCheckBoxList() {
		if(checkBoxList == null) {
			checkBoxList = new CLCheckBoxList<Producto>() {

				private static final long serialVersionUID = -8028977693425752374L;

				@Override
				public void itemListaSeleccionado(Object item, boolean seleccionado) {
					if(seleccionado) {
						Producto prod = (Producto)item;
						if(!productoSelectedList.contains(prod)) {
							productoSelectedList.add(prod);
						}
					} else {
						productoSelectedList.remove(item);
					}
				}

			};
			checkBoxList.setValues(allProductoList.toArray(new Object[allProductoList.size()]));
		}
		return checkBoxList;
	}

	private List<Producto> getProductoList() {
		List<Producto> allOrderByName = null;
		List<Producto> productoResultList = new ArrayList<Producto>();
		allArticulosList = getArticuloFacade().getAllOrderByName();
		ProductosAndPreciosHelper helper = new ProductosAndPreciosHelper(JDialogSeleccionarProducto.this, cliente);
		ResultProductosTO result = helper.getInfoProductosAndListaDePrecios();
		if(result != null) {
			versionListaDePrecios = result.versionListaDePrecios;
			allOrderByName = result.productos;
		} else {
			return productoResultList;
		}
		if(articuloFilterList == null) {
			return allOrderByName;
		} else {
			for(Producto p : allOrderByName) {
				if(articuloFilterList.contains(p.getArticulo())) {
					productoResultList.add(p);
				}
			}
		}
		return productoResultList;
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

	private List<Articulo> getArticuloList() {
		if(articuloFilterList == null) {
			return allArticulosList;
		} else {
			List<Articulo> articuloResultList = new ArrayList<Articulo>();
			for(Articulo a : allArticulosList) {
				if(articuloFilterList.contains(a)) {
					articuloResultList.add(a);
				}
			}
			return articuloResultList;
		}
	}

	private void recargarComboArticulos() {
		List<Articulo> result = new ArrayList<Articulo>();
		Set<Integer> tpSet = new HashSet<Integer>();

		ETipoProducto tpSel = getCmbTipoProducto().getSelectedIndex() == 0 ? null : (ETipoProducto)getCmbTipoProducto().getSelectedItem();
		List<Producto> subsetProductos = new ArrayList<Producto>();

		for(Producto producto : allProductoList) {
			if(tpSel == null || producto.getTipo() == tpSel) {
				if(producto.getArticulo() != null) {
					tpSet.add(producto.getArticulo().getTipoArticulo().getId());
					subsetProductos.add(producto);
				}
			}
		}

		if(tpSel != null) {
			tpSet.clear();
			tpSet.add(tpSel.getId());
		}

		//Filtro por tipo de articulo y ancho
		for(Articulo a : getArticuloList()) {
			if(a.getTipoArticulo()!= null && anchoValido(a, tpSet)) {
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

	private boolean anchoValido(Articulo art, Set<Integer> tpSet) {
		for(Integer idTipoProd : tpSet) {
			DefinicionPrecio def = versionListaDePrecios.getDefinicionPorTipoProducto(ETipoProducto.getById(idTipoProd));
			if(def != null && def.estaDefinido(art)) {
				return true;
			}
		}
		return false;
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
						recargarComboArticulos();
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
	
	private List<ETipoProducto> getTipoProductosEnListaDePrecios() {
		Set<ETipoProducto> tpSet = new HashSet<ETipoProducto>();
		for(Producto pr : allProductoList) {
			tpSet.add(pr.getTipo());
		}
		return new ArrayList<ETipoProducto>(tpSet);
	}

	private ArticuloFacadeRemote getArticuloFacade() {
		if(articuloFacade == null) {
			articuloFacade = GTLBeanFactory.getInstance().getBean2(ArticuloFacadeRemote.class);
		}
		return articuloFacade;
	}

	public List<Producto> getProductoSelectedList() {
		return productoSelectedList;
	}

}