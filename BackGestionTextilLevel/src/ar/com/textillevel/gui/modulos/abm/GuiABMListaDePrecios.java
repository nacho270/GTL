package ar.com.textillevel.gui.modulos.abm;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import ar.clarin.fwjava.boss.BossEstilos;
import ar.clarin.fwjava.componentes.CLCheckBoxListDialog;
import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.CLJTable;
import ar.clarin.fwjava.componentes.CLJTextField;
import ar.clarin.fwjava.templates.GuiABMListaTemplate;
import ar.clarin.fwjava.util.DateUtil;
import ar.clarin.fwjava.util.GuiUtil;
import ar.clarin.fwjava.util.NumUtil;
import ar.com.textillevel.entidades.enums.ETipoProducto;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.ventas.articulos.Articulo;
import ar.com.textillevel.entidades.ventas.articulos.GamaColor;
import ar.com.textillevel.entidades.ventas.productos.ListaDePrecios;
import ar.com.textillevel.entidades.ventas.productos.PrecioProducto;
import ar.com.textillevel.entidades.ventas.productos.Producto;
import ar.com.textillevel.facade.api.remote.ArticuloFacadeRemote;
import ar.com.textillevel.facade.api.remote.ClienteFacadeRemote;
import ar.com.textillevel.facade.api.remote.GamaColorFacadeRemote;
import ar.com.textillevel.facade.api.remote.ListaDePreciosFacadeRemote;
import ar.com.textillevel.facade.api.remote.ProductoFacadeRemote;
import ar.com.textillevel.gui.util.dialogs.JDialogEditarPrecioProducto;
import ar.com.textillevel.util.GTLBeanFactory;

/**
 *	Clase ABM de Lista de precios. 
 */
public class GuiABMListaDePrecios extends GuiABMListaTemplate {

	private static final long serialVersionUID = 8012369007737291095L;
	private static final int MAX_LONGITUD_RAZ_SOCIAL = 50;
	private static final String ICONO_BOTON_MODIF = "ar/clarin/fwjava/imagenes/b_modificar_fila.png";
	private static final String ICONO_BOTON_MODIF_DES = "ar/clarin/fwjava/imagenes/b_modificar_fila_des.png";
	private static final String ICONO_BOTON_VOLVER_A_DEFAULT = "ar/clarin/fwjava/imagenes/b_autocomp.png";
	private static final String ICONO_BOTON_VOLVER_A_DEFAULT_DES = "ar/clarin/fwjava/imagenes/b_autocomp_des.png";

	private JPanel tabDetalle;
	private JPanel panDetalle;
	private CLJTextField txtRazonSocial;
	private CLJTable tablaProductoPrecios;
	private static final int CANT_COLS = 6;
	private static final int COL_PRODUCTO = 0;
	private static final int COL_ARTICULO = 1;
	private static final int COL_FECHA_ULT_MODIF = 2;
	private static final int COL_PRECIO = 3;
	private static final int COL_PORC_AUMENTO = 4;
	private static final int COL_OBJ = 5;

	private List<Producto> allProductoList;
	private List<Articulo> allArticuloList;
	private List<GamaColor> allGamaList;

	private ListaDePrecios listaDePrecios;
	private ClienteFacadeRemote clienteFacadeRemote;
	private ListaDePreciosFacadeRemote listaDePreciosFacadeRemote;
	private ProductoFacadeRemote productoFacadeRemote;
	private ArticuloFacadeRemote articuloFacadeRemote;
	private GamaColorFacadeRemote gamaColorFacadeRemote;

	private JButton btnModificarPrecio;
	private JTextField txtProductos;
	private JButton btnSelProductos;
	private CLCheckBoxListDialog checkBoxListDialogTipoProducto;
	private JButton btnSelArticulo;
	private JTextField txtArticulos;
	private CLCheckBoxListDialog checkBoxListDialogArticulos;
	private JButton btnVolverAPrecioDefault;
	private JButton btnSelGama;
	private JTextField txtGama;
	private CLCheckBoxListDialog checkBoxListDialogGamas;

	private JMenuItem menuItemLimpiarPrecio;
	private JMenuItem menuItemEditarPrecio;

	public GuiABMListaDePrecios(Integer idModulo) {
		super();
		this.allProductoList = getProductoFacade().getAllOrderByName();
		this.allArticuloList = getArticuloFacadeRemote().getAllOrderByName();
		this.allGamaList = getGamaColorFacadeRemote().getAllOrderByName();
		setHijoCreado(true);
		setTitle("Administrar Listas de Precios");
		constructGui();
		setEstadoInicial();
	}

	private void constructGui() {
		panTabs.addTab("Datos de la lista de precio", getTabDetalle());
		getBtnAgregar().setVisible(false);
		getBtnEliminar().setBounds(getBtnEliminar().getX() - 80, getBtnEliminar().getY(), getBtnEliminar().getWidth(), getBtnEliminar().getHeight());
	}

	private JPanel getTabDetalle() {
		if(tabDetalle == null) {
			tabDetalle = new JPanel();
			tabDetalle.setLayout(new GridBagLayout());
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.weightx = 1;
			gridBagConstraints.weighty = 1;
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			tabDetalle.add(getPanDetalle(), gridBagConstraints);
		}
		return tabDetalle;
	}

	private JPanel getPanDetalle() {
		if(panDetalle == null) {
			panDetalle = new JPanel();
			panDetalle.setLayout(new GridBagLayout());
			panDetalle.add(new JLabel(" CLIENTE:"), createGridBagConstraints(0, 0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getTxtRazonSocial(), createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panDetalle.add(getBtnSelProductos(), createGridBagConstraints(0, 1,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getTxtProductos(), createGridBagConstraints(1, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panDetalle.add(getBtnSelArticulo(), createGridBagConstraints(0, 2,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getTxtArticulos(), createGridBagConstraints(1, 2, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panDetalle.add(getBtnSelGama(), createGridBagConstraints(0, 3,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getTxtGama(), createGridBagConstraints(1, 3, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			JScrollPane scrollPane = new JScrollPane(getTablaProductoPrecio());
			scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			panDetalle.add(scrollPane, createGridBagConstraints(0, 4, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 2, 2, 1, 1));
			panDetalle.add(getBtnModificarPrecio(), createGridBagConstraints(2, 4, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(10, 0, 0, 10), 1, 1, 0, 0));
			panDetalle.add(getBtnVolverAPrecioDefault(), createGridBagConstraints(2, 5, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(10, 0, 0, 10), 1, 1, 0, 0));
		}
		return panDetalle;
	}
	
	private void initializePopupMenu(CLJTable tabla) {
		tabla.setComponentPopupMenu(new JPopupMenu());
		tabla.getComponentPopupMenu().add(getMenuItemLimpiarPrecio());
		tabla.getComponentPopupMenu().add(getMenuItemEditarPrecio());
	}

	private JMenuItem getMenuItemLimpiarPrecio() {
		if(menuItemLimpiarPrecio == null) {
			menuItemLimpiarPrecio = new JMenuItem("Limpiar Precio(s)");
			menuItemLimpiarPrecio.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					getBtnVolverAPrecioDefault().doClick();
				}
			});
		}
		return menuItemLimpiarPrecio;
	}

	private JMenuItem getMenuItemEditarPrecio() {
		if(menuItemEditarPrecio == null) {
			menuItemEditarPrecio = new JMenuItem("Editar Precio");
			menuItemEditarPrecio.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					getBtnModificarPrecio().doClick();
				}
			});
		}
		return menuItemEditarPrecio;
	}

	private JTextField getTxtGama() {
		if(txtGama == null) {
			txtGama = new JTextField();
			txtGama.setEditable(false);
		}
		return txtGama;
	}

	
	private JButton getBtnSelGama() {
		if(btnSelGama == null) {
			btnSelGama = new JButton("GAMAS :");
			btnSelGama.addActionListener(new ActionListener() {

				@SuppressWarnings("unchecked")
				public void actionPerformed(ActionEvent e) {
					getCheckBoxListDialogGamas().setValores(allGamaList, true);
					getCheckBoxListDialogGamas().setVisible(true);
					getTxtGama().setText(getCheckBoxListDialogGamas().getValoresSeleccionadosAsString());
					getTablaProductoPrecio().setNumRows(0);
					resaltarPrecios((List<ETipoProducto>)getCheckBoxListDialogTipoProducto().getValoresSeleccionados(), (List<Articulo>)getCheckBoxListDialogArticulos().getValoresSeleccionados(), (List<GamaColor>)getCheckBoxListDialogGamas().getValoresSeleccionados());
				}

			});

		}
		return btnSelGama;
	}

	private CLCheckBoxListDialog getCheckBoxListDialogGamas() {
		if(checkBoxListDialogGamas == null) {
			checkBoxListDialogGamas = new CLCheckBoxListDialog();
		}
		return checkBoxListDialogGamas;
	}
	
	private JButton getBtnVolverAPrecioDefault() {
		if(btnVolverAPrecioDefault == null) {
			btnVolverAPrecioDefault = BossEstilos.createButton(ICONO_BOTON_VOLVER_A_DEFAULT, ICONO_BOTON_VOLVER_A_DEFAULT_DES);
			btnVolverAPrecioDefault.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					for(int i : getTablaProductoPrecio().getSelectedRows()) {
						PrecioProducto pp = (PrecioProducto)getTablaProductoPrecio().getValueAt(i, COL_OBJ);
						pp.setPrecio(pp.getProducto().getPrecioDefault());
						setDatosPrecioProducto(i, pp, false);
					}
				}

			});
		}
		return btnVolverAPrecioDefault;
	}
	
	private JTextField getTxtArticulos() {
		if(txtArticulos == null) {
			txtArticulos = new JTextField();
			txtArticulos.setEditable(false);
		}
		return txtArticulos;
	}

	private JButton getBtnSelArticulo() {
		if(btnSelArticulo == null) {
			btnSelArticulo = new JButton("ARTICULOS: ");
			btnSelArticulo.addActionListener(new ActionListener() {

				@SuppressWarnings("unchecked")
				public void actionPerformed(ActionEvent e) {
					getCheckBoxListDialogArticulos().setValores(allArticuloList, true);
					getCheckBoxListDialogArticulos().setVisible(true);
					getTxtArticulos().setText(getCheckBoxListDialogArticulos().getValoresSeleccionadosAsString());
					getTablaProductoPrecio().setNumRows(0);
					resaltarPrecios((List<ETipoProducto>)getCheckBoxListDialogTipoProducto().getValoresSeleccionados(), (List<Articulo>)getCheckBoxListDialogArticulos().getValoresSeleccionados(), (List<GamaColor>)getCheckBoxListDialogGamas().getValoresSeleccionados());
				}

			});
		}
		return btnSelArticulo;
	}

	private JButton getBtnSelProductos() {
		if(btnSelProductos == null) {
			btnSelProductos = new JButton("PRODUCTOS: ");
			btnSelProductos.addActionListener(new ActionListener() {

				@SuppressWarnings("unchecked")
				public void actionPerformed(ActionEvent e) {
					getCheckBoxListDialogTipoProducto().setValores(Arrays.asList(ETipoProducto.values()), true);
					getCheckBoxListDialogTipoProducto().setVisible(true);
					getTxtProductos().setText(getCheckBoxListDialogTipoProducto().getValoresSeleccionadosAsString());
					getTablaProductoPrecio().setNumRows(0);
					resaltarPrecios((List<ETipoProducto>)getCheckBoxListDialogTipoProducto().getValoresSeleccionados(), (List<Articulo>)getCheckBoxListDialogArticulos().getValoresSeleccionados(), (List<GamaColor>)getCheckBoxListDialogGamas().getValoresSeleccionados());
				}

			});
		}
		return btnSelProductos;
	}

	private JTextField getTxtProductos() {
		if(txtProductos == null) {
			txtProductos = new JTextField();
			txtProductos.setEditable(false);
		}
		return txtProductos;
	}

	private JButton getBtnModificarPrecio() {
		if(btnModificarPrecio == null) {
			btnModificarPrecio = BossEstilos.createButton(ICONO_BOTON_MODIF, ICONO_BOTON_MODIF_DES);
			btnModificarPrecio.setEnabled(false);
			btnModificarPrecio.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					modificarPrecio();
				}
			});
		}
		return btnModificarPrecio;
	}

	@Override
	public void cargarLista() {
		List<Cliente> clienteList = getClienteFacade().getAllOrderByName();
		lista.removeAll();
		for(Cliente c : clienteList) {
			lista.addItem(c);
		}
	}

	private ClienteFacadeRemote getClienteFacade() {
		if(clienteFacadeRemote == null) {
			clienteFacadeRemote = GTLBeanFactory.getInstance().getBean2(ClienteFacadeRemote.class);
		}
		return clienteFacadeRemote;
	}

	@Override
	public void botonAgregarPresionado(int arg0) {
	}

	@Override
	public void botonCancelarPresionado(int arg0) {
		setModoEdicion(false);
		itemSelectorSeleccionado(lista.getSelectedIndex());
	}

	@Override
	public void botonEliminarPresionado(int arg0) {
		if(lista.getSelectedIndex() >= 0) {
			if(CLJOptionPane.showQuestionMessage(GuiABMListaDePrecios.this, "¿Está seguro que desea eliminar la lista de precios seleccionada?", "Confirmación") == CLJOptionPane.YES_OPTION) {
				getListaDePreciosFacadeRemote().remove(getListaDePreciosActual());
				itemSelectorSeleccionado(-1);
			}
		}
	}

	@Override
	public boolean botonGrabarPresionado(int arg0) {
		if(validar()) {
			capturarSetearDatos();
			getListaDePreciosFacadeRemote().save(getListaDePreciosActual());
			CLJOptionPane.showInformationMessage(this, "Los datos de la lista se han guardado con éxito", "Administrar Lista de Precios");
			lista.setSelectedValue(lista.getSelectedValue(), true);
			return true;
		}
		return false;
	}

	private void capturarSetearDatos() {
		for(int i = 0; i < getTablaProductoPrecio().getRowCount(); i++) {
			PrecioProducto pp = (PrecioProducto)getTablaProductoPrecio().getValueAt(i, COL_OBJ);
			if(!pp.getPrecio().equals(pp.getProducto().getPrecioDefault())) {
				if(pp.getId() == null) {
					getListaDePreciosActual().getPrecios().add(pp);
				}
			} else {
				if(pp.getId() != null) {
					getListaDePreciosActual().getPrecios().remove(pp);
				}
			}
		}
	}

	private boolean validar() {
		return true;
	}

	@Override
	public boolean botonModificarPresionado(int nivelNodoSeleccionado) {
		if(listaDePrecios == null) {
			listaDePrecios = new ListaDePrecios();
			listaDePrecios.setCliente((Cliente)lista.getSelectedValue());
		}
		if(nivelNodoSeleccionado >= 0) {
			setModoEdicion(true);
			return true;
		} else {
			CLJOptionPane.showErrorMessage(this, "Debe seleccionar un cliente", "Error");
			return false;
		}
	}

	@Override
	public void itemComboMaestroSeleccionado() {
	}

	@SuppressWarnings("unchecked")
	@Override
	public void itemSelectorSeleccionado(int arg0) {
		limpiarDatos();
		Cliente cliente = (Cliente)lista.getSelectedValue();
		getTxtRazonSocial().setText(cliente.getRazonSocial());
		ListaDePrecios listaDePrecios = getListaDePreciosFacadeRemote().getListaByIdCliente(cliente.getId());
		setListaDePrecios(listaDePrecios);
		resaltarPrecios((List<ETipoProducto>)getCheckBoxListDialogTipoProducto().getValoresSeleccionados(), (List<Articulo>)getCheckBoxListDialogArticulos().getValoresSeleccionados(), (List<GamaColor>)getCheckBoxListDialogGamas().getValoresSeleccionados());
	}

	private void resaltarPrecios(List<ETipoProducto> tipoProductoList, List<Articulo> articuloSelectedList, List<GamaColor> gamaSelectedList) {
		Map<Integer, PrecioProducto> ppMap = new HashMap<Integer, PrecioProducto>();
		if(listaDePrecios != null) {
			for(PrecioProducto pp : listaDePrecios.getPrecios()) {
				ppMap.put(pp.getProducto().getId(), pp);
			}
		}
		int i = 0;
		for(Producto p : allProductoList) {
			boolean cumpleCondTipoProducto = tipoProductoList.isEmpty() || tipoProductoList.contains(p.getTipo());
			boolean cumpleCondArticulo = articuloSelectedList.isEmpty() || articuloSelectedList.contains(p.getArticulo());
			boolean cumpleCondGama = gamaSelectedList.isEmpty() || coincideGama(gamaSelectedList, p);
			if(cumpleCondTipoProducto && cumpleCondArticulo && cumpleCondGama) {
				PrecioProducto precioProducto = ppMap.get(p.getId());
				boolean pintarFila = false;
				if(precioProducto == null) {
					precioProducto = new PrecioProducto();
					precioProducto.setPrecio(p.getPrecioDefault());
					precioProducto.setProducto(p);
				} else {
					pintarFila = true;
				}
				getTablaProductoPrecio().addRow();
				setDatosPrecioProducto(i, precioProducto, pintarFila);
				i++;
			}
		}
	}

	private boolean coincideGama(List<GamaColor> gamaSelectedList, Producto p) {
		ProductoGamaColorDetectorVisitor pgcdv = new ProductoGamaColorDetectorVisitor(gamaSelectedList);
		p.accept(pgcdv);
		return pgcdv.isCoincideGama();
	}

	private void setDatosPrecioProducto(int i, PrecioProducto precioProducto, boolean pintarFila) {
		getTablaProductoPrecio().setValueAt(precioProducto, i, COL_OBJ);
		getTablaProductoPrecio().setValueAt(precioProducto.getPrecio(), i, COL_PRECIO);
		getTablaProductoPrecio().setValueAt(precioProducto.getProducto(), i, COL_PRODUCTO);
		Timestamp fechaUltModif = precioProducto.getFechaUltModif();
		if(fechaUltModif != null){
			getTablaProductoPrecio().setValueAt(DateUtil.dateToString(precioProducto.getFechaUltModif(), DateUtil.SHORT_DATE_WITH_HOUR), i, COL_FECHA_ULT_MODIF);
		}
		getTablaProductoPrecio().setValueAt(precioProducto.getProducto().getArticulo(), i, COL_ARTICULO);
		if(pintarFila) {
			getTablaProductoPrecio().setBackgroundRow(i, Color.YELLOW);
			BigDecimal dif = precioProducto.getPrecio().subtract(precioProducto.getProducto().getPrecioDefault());
			float porcAumento = NumUtil.getPorcentaje(dif.floatValue(), precioProducto.getProducto().getPrecioDefault().floatValue());
			getTablaProductoPrecio().setValueAt(porcAumento, i, COL_PORC_AUMENTO);
		} else {
			getTablaProductoPrecio().setBackgroundRow(i, getTablaProductoPrecio().getAlternativeColor());
			getTablaProductoPrecio().setValueAt(null, i, COL_FECHA_ULT_MODIF);
			getTablaProductoPrecio().setValueAt(null, i, COL_PORC_AUMENTO);
		}
	}

	@Override
	public void limpiarDatos() {
		getTxtRazonSocial().setText("");
		getTablaProductoPrecio().setNumRows(0);
		getCheckBoxListDialogTipoProducto().getCheckBoxList().setAllSelectedItems(false);
		getTxtProductos().setText("");
		getCheckBoxListDialogArticulos().getCheckBoxList().setAllSelectedItems(false);
		getTxtArticulos().setText("");
	}

	@Override
	public void setEstadoInicial() {
		setModoEdicion(false);
		setModoEdicionTemplate(false);
		cargarLista();
		if(lista.getModel().getSize() > 0) {
			lista.setSelectedIndex(0);
		}
	}

	@Override
	public void setModoEdicion(boolean estado) {
		GuiUtil.setEstadoPanel(getTabDetalle(), estado);
		if(estado) {
			getBtnModificarPrecio().setEnabled(false);
			getBtnVolverAPrecioDefault().setEnabled(false);
			initializePopupMenu(getTablaProductoPrecio());
		} else {
			getTablaProductoPrecio().setComponentPopupMenu(null);
		}
	}

	private GridBagConstraints createGridBagConstraints(int x, int y, int posicion, int fill, Insets insets, int cantX, int cantY, double weightx, double weighty) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = posicion;
		gbc.fill = fill;
		gbc.insets = insets;
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = cantX;
		gbc.gridheight = cantY;
		gbc.weightx = weightx;
		gbc.weighty = weighty;
		return gbc;
	}

	private CLJTextField getTxtRazonSocial() {
		if(txtRazonSocial == null) {
			txtRazonSocial = new CLJTextField(MAX_LONGITUD_RAZ_SOCIAL);
			txtRazonSocial.setEditable(false);
		}
		return txtRazonSocial;
	}

	public void setModoEdicionTemplate(boolean estado) {
		super.setModoEdicionTemplate(estado);
		if(!estado && lista.getSelectedIndex() < 0) {
			getBtnEliminar().setEnabled(false);
			getBtnModificar().setEnabled(false);
		}
	}

	@SuppressWarnings("serial")
	private CLJTable getTablaProductoPrecio() {
		if(tablaProductoPrecios == null) {
			tablaProductoPrecios = new CLJTable(0, CANT_COLS) {

				@Override
				public void newRowSelected(int newRow, int oldRow) {
					getBtnModificarPrecio().setEnabled(newRow != -1 && getTablaProductoPrecio().getSelectedRows().length == 1);
					getBtnVolverAPrecioDefault().setEnabled(newRow != -1);
				}

			};
			tablaProductoPrecios.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					if(e.getClickCount() == 2){
						modificarPrecio();
					}
				}
			});
			tablaProductoPrecios.setStringColumn(COL_PRODUCTO, "Producto", 200, 200, true);
			tablaProductoPrecios.setStringColumn(COL_ARTICULO, "Artículo", 100, 100, true);
			tablaProductoPrecios.setStringColumn(COL_FECHA_ULT_MODIF, "Fecha Última Modificación", 130, 130, true);
			tablaProductoPrecios.setFloatColumn(COL_PRECIO, "Precio", 80, true);
			tablaProductoPrecios.setFloatColumn(COL_PORC_AUMENTO, "% Aumento", 60, true);
			tablaProductoPrecios.setStringColumn(COL_OBJ, "", 0, 0, true);
		}
		return tablaProductoPrecios;
	}

	private ListaDePrecios getListaDePreciosActual() {
		return listaDePrecios;
	}

	private void setListaDePrecios(ListaDePrecios listaDePrecios) {
		this.listaDePrecios = listaDePrecios;
	}

	private ProductoFacadeRemote getProductoFacade() {
		if(productoFacadeRemote == null) {
			productoFacadeRemote = GTLBeanFactory.getInstance().getBean2(ProductoFacadeRemote.class);
		}
		return productoFacadeRemote;
	}

	private ListaDePreciosFacadeRemote getListaDePreciosFacadeRemote() {
		if(listaDePreciosFacadeRemote == null) {
			listaDePreciosFacadeRemote = GTLBeanFactory.getInstance().getBean2(ListaDePreciosFacadeRemote.class);
		}
		return listaDePreciosFacadeRemote;
	}

	private CLCheckBoxListDialog getCheckBoxListDialogTipoProducto() {
		if(checkBoxListDialogTipoProducto == null) {
			checkBoxListDialogTipoProducto = new CLCheckBoxListDialog();
		}
		return checkBoxListDialogTipoProducto;
	}

	private CLCheckBoxListDialog getCheckBoxListDialogArticulos() {
		if(checkBoxListDialogArticulos == null) {
			checkBoxListDialogArticulos = new CLCheckBoxListDialog();
		}
		return checkBoxListDialogArticulos;
	}

	public ArticuloFacadeRemote getArticuloFacadeRemote() {
		if(articuloFacadeRemote == null) {
			articuloFacadeRemote = GTLBeanFactory.getInstance().getBean2(ArticuloFacadeRemote.class);
		}
		return articuloFacadeRemote;
	}

	public GamaColorFacadeRemote getGamaColorFacadeRemote() {
		if(gamaColorFacadeRemote == null) {
			gamaColorFacadeRemote = GTLBeanFactory.getInstance().getBean2(GamaColorFacadeRemote.class);
		}
		return gamaColorFacadeRemote;
	}

	private void modificarPrecio() {
		int selectedRow = getTablaProductoPrecio().getSelectedRow();
		if(selectedRow != -1) {
			PrecioProducto pp = (PrecioProducto)getTablaProductoPrecio().getValueAt(selectedRow, COL_OBJ);
			BigDecimal precioAnterior = pp.getProducto().getPrecioDefault();
			JDialogEditarPrecioProducto dialogEditarPrecioProducto = new JDialogEditarPrecioProducto(GuiABMListaDePrecios.this.getFrame(), pp);
			GuiUtil.centrarEnPadre(dialogEditarPrecioProducto);
			dialogEditarPrecioProducto.setVisible(true);
			if(dialogEditarPrecioProducto.isAcepto()) {
				setDatosPrecioProducto(selectedRow, pp, !precioAnterior.equals(pp.getPrecio()));
			}
		}
	}

}