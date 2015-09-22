package ar.com.textillevel.gui.modulos.abm;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.CLJTextField;
import ar.clarin.fwjava.templates.GuiABMListaTemplate;
import ar.clarin.fwjava.util.GuiUtil;
import ar.com.textillevel.entidades.enums.ETipoProducto;
import ar.com.textillevel.entidades.ventas.articulos.Articulo;
import ar.com.textillevel.entidades.ventas.articulos.Color;
import ar.com.textillevel.entidades.ventas.articulos.DibujoEstampado;
import ar.com.textillevel.entidades.ventas.articulos.GamaColor;
import ar.com.textillevel.entidades.ventas.articulos.VarianteEstampado;
import ar.com.textillevel.entidades.ventas.productos.Producto;
import ar.com.textillevel.entidades.ventas.productos.ProductoEstampado;
import ar.com.textillevel.entidades.ventas.productos.ProductoTenido;
import ar.com.textillevel.facade.api.remote.ArticuloFacadeRemote;
import ar.com.textillevel.facade.api.remote.DibujoEstampadoFacadeRemote;
import ar.com.textillevel.facade.api.remote.GamaColorFacadeRemote;
import ar.com.textillevel.facade.api.remote.ProductoFacadeRemote;
import ar.com.textillevel.gui.util.ProductoFactory;
import ar.com.textillevel.util.GTLBeanFactory;

public class GuiABMProductos extends GuiABMListaTemplate {

	private static final long serialVersionUID = -8651844648013003067L;

	private static final int MAX_LONGITUD_NOMBRE = 50;
	
	private static final String PNL_TENIDO = "Teñido";
	private static final String PNL_ESTAMPADO = "Estampado";

	private JPanel tabDetalle;
	private JPanel panDetalle;
	
	private JPanel pnlControlesExtra;
	
	private CLJTextField txtNombreProducto;
	private JComboBox cmbArticulos;
	private JComboBox cmbTipoProducto;
	
	private CardLayout cardLayout;
	
	private Boolean flagModificarTenido;
	
	//tenido
	private JComboBox cmbGamas;
	private JPanel pnlTenido;
	
	//estampado
	private JComboBox cmbEstampados;
	private JComboBox cmbVariantes;
	private JPanel pnlEstampado;
	
	private ProductoFacadeRemote productoFacade;
	private GamaColorFacadeRemote gamaFacade;
	private DibujoEstampadoFacadeRemote dibujosFacade;
	private ArticuloFacadeRemote articulosFacade;
	
	private Producto productoActual;
	
	public GuiABMProductos(Integer idModulo){
		super();
		setHijoCreado(true);
		setTitle("Administrar Productos");
		constructGui();
		setEstadoInicial();
	}

	private void constructGui() {
		panTabs.addTab("Información del producto", getTabDetalle());		
	}
	
	private JPanel getTabDetalle() {
		if(tabDetalle == null) {
			tabDetalle = new JPanel();
			tabDetalle.setLayout(new BorderLayout());
			tabDetalle.add(getPanDetalle(), BorderLayout.NORTH);
		}
		return tabDetalle;
	}
	
	private JPanel getPanDetalle() {
		if(panDetalle == null) {
			panDetalle = new JPanel();
			panDetalle.setLayout(new GridBagLayout());
			panDetalle.add(new JLabel("Nombre: "), createGridBagConstraints(0, 0,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getTxtNombreProducto(),  createGridBagConstraints(1, 0,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panDetalle.add(new JLabel("Artículo: "), createGridBagConstraints(0, 1,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getCmbArticulos(),  createGridBagConstraints(1, 1,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panDetalle.add(new JLabel("Tipo de producto: "), createGridBagConstraints(0, 2,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getCmbTipoProducto(),  createGridBagConstraints(1, 2,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panDetalle.add(getPnlControlesExtra(), createGridBagConstraints(0, 3,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 5, 5), 2, 1, 0, 0));
		}
		return panDetalle;
	}
	
	@Override
	public void cargarLista() {
		List<Producto> prods = getProductosFacade().getAllOrderByName();
		lista.removeAll();
		for(Producto p : prods){
			lista.addItem(p);
		}
	}

	@Override
	public void botonAgregarPresionado(int nivelNodoSeleccionado) {
		limpiarDatos();
		setProductoActual(ProductoFactory.createProducto((ETipoProducto)getCmbTipoProducto().getSelectedItem()));
		getTxtNombreProducto().requestFocus();		
		getCmbArticulos().setEnabled(true);
		setFlagModificarTenido(false);
	}

	@Override
	public void botonCancelarPresionado(int nivelNodoSeleccionado) {
		setModoEdicion(false);
		itemSelectorSeleccionado(lista.getSelectedIndex());
	}

	@Override
	public void botonEliminarPresionado(int nivelNodoSeleccionado) {
		if(lista.getSelectedIndex() >= 0) {
			if(CLJOptionPane.showQuestionMessage(this, "¿Está seguro que desea eliminar el producto seleccionado?", "Confirmación") == CLJOptionPane.YES_OPTION) {
				getProductosFacade().remove(getProductoActual());
				itemSelectorSeleccionado(-1);
			}
		}
	}

	@Override
	public boolean botonGrabarPresionado(int nivelNodoSeleccionado) {
		if(validar()) {
			ETipoProducto tipoProducto = (ETipoProducto)getCmbTipoProducto().getSelectedItem();
			if(tipoProducto == ETipoProducto.TENIDO){
				getTxtNombreProducto().setEditable(false);
				if(getFlagModificarTenido()==null || !getFlagModificarTenido()){
					GamaColor gama = (GamaColor)getCmbGamas().getSelectedItem();
					List<Color> colores = gama.getColores();
					if(CLJOptionPane.showQuestionMessage(this, "Atención: Se grabarán " + colores.size() + " productos. ¿Desea continuar?", "Advertencia")==CLJOptionPane.YES_OPTION){
						List<ProductoTenido> prodsTenido = new ArrayList<ProductoTenido>();
						for(Color c : colores){
							ProductoTenido p = new ProductoTenido();
							Articulo articulo = (Articulo)getCmbArticulos().getSelectedItem();
							p.setColor(c);
							p.setDescripcion((tipoProducto.getDescripcion() + " - " + c.getNombre() + " - " + articulo.getNombre()).trim().toUpperCase());
							p.setArticulo(articulo);
							p.setGamaColor(gama);
							prodsTenido.add(p);
						}
						getProductosFacade().saveAll(prodsTenido);
						cargarLista();
						return true;
					}
				} else {
					Articulo articulo = (Articulo)getCmbArticulos().getSelectedItem();
					GamaColor gama = (GamaColor)getCmbGamas().getSelectedItem();
					getProductoActual().setArticulo(articulo);
					((ProductoTenido)getProductoActual()).setGamaColor(gama);
					getProductoActual().setDescripcion(getProductoActual().getDescripcion());
					Producto prod = getProductosFacade().save(getProductoActual());
					cargarLista();
					lista.setSelectedValue(prod, true);
					return true;
				}
			}else{
				capturarSetearDatos();
				Producto prod = getProductosFacade().save(getProductoActual());
				cargarLista();
				lista.setSelectedValue(prod, true);
				return true;
			}
		}
		return false;
	}

	private void capturarSetearDatos() {
		ETipoProducto tipoArticulo = (ETipoProducto)getCmbTipoProducto().getSelectedItem();
		Articulo articulo = (Articulo)getCmbArticulos().getSelectedItem();
		setProductoActual(ProductoFactory.createProducto(tipoArticulo));
		if(tipoArticulo != ETipoProducto.REPROCESO_SIN_CARGO && tipoArticulo != ETipoProducto.DEVOLUCION){
			getProductoActual().setArticulo(articulo);
		}
		ETipoProducto tipoProducto = tipoArticulo;
		
		if(tipoProducto == ETipoProducto.ESTAMPADO){
			DibujoEstampado dibujo = (DibujoEstampado)getCmbEstampados().getSelectedItem();
			VarianteEstampado variante = (VarianteEstampado)getCmbVariantes().getSelectedItem();
			((ProductoEstampado)getProductoActual()).setDibujo(dibujo);
			((ProductoEstampado)getProductoActual()).setVariante(variante);
			getProductoActual().setDescripcion((tipoArticulo.getDescripcion() + " - " + articulo.getNombre() + " - " + dibujo + " - " + variante).trim().toUpperCase());
		}else{
			if(tipoArticulo != ETipoProducto.REPROCESO_SIN_CARGO && tipoArticulo != ETipoProducto.DEVOLUCION){
				getProductoActual().setDescripcion((tipoArticulo.getDescripcion() + " - " + articulo.getNombre()).trim().toUpperCase());
			}else{
				getProductoActual().setDescripcion(tipoProducto.getDescripcion().toUpperCase());
			}
		}
	}

	private boolean validar() {
		ETipoProducto tipoProducto = (ETipoProducto)getCmbTipoProducto().getSelectedItem();
		
		if(tipoProducto == ETipoProducto.ESTAMPADO){
			if(getCmbEstampados().getSelectedItem() == null){
				CLJOptionPane.showErrorMessage(this, "Debe seleccionar un estampado.", "Advertencia");
				return false;
			}
			
			if(getCmbVariantes().getSelectedItem() == null){
				CLJOptionPane.showErrorMessage(this, "Debe seleccionar una variante.", "Advertencia");
				return false;
			}
		}
		
		if(tipoProducto == ETipoProducto.TENIDO){
			if(getCmbGamas().getSelectedItem() == null){
				CLJOptionPane.showErrorMessage(this, "Debe seleccionar una gama de colores.", "Advertencia");
				return false;
			}
		}
		
		return true;
	}

	@Override
	public boolean botonModificarPresionado(int nivelNodoSeleccionado) {
		if(nivelNodoSeleccionado >= 0) {
			setModoEdicion(true);
			setFlagModificarTenido(true);
			getCmbTipoProducto().setEditable(false);
			getTxtNombreProducto().requestFocus();
			return true;
		} else {
			CLJOptionPane.showErrorMessage(this, "Debe seleccionar un producto", "Error");
			return false;
		}
	}

	@Override
	public void itemComboMaestroSeleccionado() {}

	@Override
	public void itemSelectorSeleccionado(int nivelItemSelector) {
		setProductoActual((Producto)lista.getSelectedValue());
		limpiarDatos();
		if(getProductoActual() != null) {
			getPnlControlesExtra().setVisible(true);
			if(getProductoActual() instanceof ProductoTenido){
				getCmbGamas().setSelectedItem(((ProductoTenido)getProductoActual()).getGamaColor());
				getCmbTipoProducto().setSelectedItem(ETipoProducto.TENIDO);
			}else if(getProductoActual() instanceof ProductoEstampado){
				getCmbEstampados().setSelectedItem(((ProductoEstampado)getProductoActual()).getDibujo());
				getCmbTipoProducto().setSelectedItem(ETipoProducto.ESTAMPADO);
			}else{
				getPnlControlesExtra().setVisible(false);
				getCmbTipoProducto().setSelectedItem(getProductoActual().getTipo());
			}
			getTxtNombreProducto().setText(getProductoActual().getDescripcion());
			getCmbArticulos().setSelectedItem(getProductoActual().getArticulo());
		}
	}

	@Override
	public void limpiarDatos() {
		getTxtNombreProducto().setText("");
		getCmbArticulos().setSelectedIndex(-1);
		getCmbGamas().setSelectedIndex(-1);
		getCmbEstampados().setSelectedIndex(-1);
		getCmbVariantes().setSelectedIndex(-1);
	}

	@Override
	public void setEstadoInicial() {
		setModoEdicion(false);
		setModoEdicionTemplate(false);
		cargarLista();
		ETipoProducto selectedItem = (ETipoProducto)getCmbTipoProducto().getSelectedItem();
		cambiarPanel(selectedItem);
		setProductoActual(ProductoFactory.createProducto(selectedItem));
		if(lista.getModel().getSize() > 0) {
			lista.setSelectedIndex(0);
		}
	}

	@Override
	public void setModoEdicion(boolean estado) {
		GuiUtil.setEstadoPanel(getTabDetalle(), estado);
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

	private JPanel getPnlControlesExtra() {
		if(pnlControlesExtra == null){
			cardLayout = new CardLayout();
			pnlControlesExtra = new JPanel(cardLayout);
			pnlControlesExtra.setBorder(BorderFactory.createTitledBorder("Datos adicionales"));
			pnlControlesExtra.add("Teñido", getPnlTenido());
			pnlControlesExtra.add("Estampado",getPnlEstampado());
		}
		return pnlControlesExtra;
	}

	private CLJTextField getTxtNombreProducto() {
		if(txtNombreProducto == null){
			txtNombreProducto = new CLJTextField(MAX_LONGITUD_NOMBRE);
			txtNombreProducto.setEditable(false);
		}
		return txtNombreProducto;
	}

	private JComboBox getCmbTipoProducto() {
		if(cmbTipoProducto == null){
			cmbTipoProducto = new JComboBox();
			GuiUtil.llenarCombo(cmbTipoProducto, Arrays.asList(ETipoProducto.values()), true);
			cmbTipoProducto.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent evt) {
					if (evt.getStateChange() == ItemEvent.SELECTED){
						ETipoProducto itemSeleccionado = (ETipoProducto)cmbTipoProducto.getSelectedItem();
						if(itemSeleccionado == ETipoProducto.TENIDO){
							getTxtNombreProducto().setEnabled(false);
						}else if(itemSeleccionado == ETipoProducto.REPROCESO_SIN_CARGO){
							getCmbArticulos().setEnabled(false);
							getTxtNombreProducto().setText(ETipoProducto.REPROCESO_SIN_CARGO.getDescripcion());
							getTxtNombreProducto().setEditable(false);
						}else if(itemSeleccionado == ETipoProducto.DEVOLUCION){
							getCmbArticulos().setEnabled(false);
							getTxtNombreProducto().setText(ETipoProducto.DEVOLUCION.getDescripcion());
							getTxtNombreProducto().setEditable(false);
						}
						itemTipoProductoSeleccionado(itemSeleccionado);
					}
				}
			});
		}
		return cmbTipoProducto;
	}

	private void itemTipoProductoSeleccionado(ETipoProducto selectedItem) {
		cambiarPanel(selectedItem);
	}

	private void cambiarPanel(ETipoProducto selectedItem) {
		if(selectedItem == ETipoProducto.TENIDO){
			getPnlControlesExtra().setVisible(true);
			getCardLayout().show(getPnlControlesExtra(), PNL_TENIDO);
			return;
		}
		
		if(selectedItem == ETipoProducto.ESTAMPADO){
			getPnlControlesExtra().setVisible(true);
			getCardLayout().show(getPnlControlesExtra(), PNL_ESTAMPADO);
			return;
		}
		getPnlControlesExtra().setVisible(false);
	}

	private CardLayout getCardLayout() {
		return cardLayout;
	}

	private JComboBox getCmbGamas() {
		if(cmbGamas == null){
			cmbGamas = new JComboBox();
			GuiUtil.llenarCombo(cmbGamas,getGamaFacade().getAllOrderByName(), false);
		}
		return cmbGamas;
	}

	private JPanel getPnlTenido() {
		if(pnlTenido == null){
			pnlTenido = new JPanel();
			pnlTenido.setLayout(new GridBagLayout());
			pnlTenido.add(new JLabel("Nombre: "), createGridBagConstraints(0, 0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			pnlTenido.add(getCmbGamas(),  createGridBagConstraints(1, 0,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
		}
		return pnlTenido;
	}

	private JComboBox getCmbEstampados() {
		if(cmbEstampados == null){
			cmbEstampados = new JComboBox();
			cmbEstampados.addItem(null);
			List<DibujoEstampado> dibujosList = getDibujosFacade().getAllOrderByNombre();
			for(DibujoEstampado d : dibujosList){
				cmbEstampados.addItem(d);
			}
			cmbEstampados.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					llenarVariantes((DibujoEstampado)cmbEstampados.getSelectedItem());
				}
			});
		}
		return cmbEstampados;
	}

	private void llenarVariantes(DibujoEstampado selectedItem) {
		if(selectedItem != null){
			List<VarianteEstampado> variantes = getDibujosFacade().getByIdEager(selectedItem.getId()).getVariantes();
			GuiUtil.llenarCombo(getCmbVariantes(), variantes, false);
		}
	}

	private JComboBox getCmbVariantes() {
		if(cmbVariantes == null){
			cmbVariantes = new JComboBox();
		}
		return cmbVariantes;
	}

	private JPanel getPnlEstampado() {
		if(pnlEstampado == null){
			pnlEstampado = new JPanel();
			pnlEstampado.add(new JLabel("Dibujo: "), createGridBagConstraints(0, 0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			pnlEstampado.add(getCmbEstampados(),  createGridBagConstraints(1, 0,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			pnlEstampado.add(new JLabel("Variante: "), createGridBagConstraints(0, 0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			pnlEstampado.add(getCmbVariantes(),  createGridBagConstraints(1, 0,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
		}
		return pnlEstampado;
	}

	private Producto getProductoActual() {
		return productoActual;
	}

	private void setProductoActual(Producto productoAcutal) {
		this.productoActual = productoAcutal;
	}

	private ProductoFacadeRemote getProductosFacade() {
		if(productoFacade == null){
			productoFacade = GTLBeanFactory.getInstance().getBean2(ProductoFacadeRemote.class);
		}
		return productoFacade;
	}

	private DibujoEstampadoFacadeRemote getDibujosFacade() {
		if(dibujosFacade == null){
			dibujosFacade = GTLBeanFactory.getInstance().getBean2(DibujoEstampadoFacadeRemote.class);
		}
		return dibujosFacade;
	}

	private JComboBox getCmbArticulos() {
		if(cmbArticulos == null){
			cmbArticulos = new JComboBox();
			GuiUtil.llenarCombo(cmbArticulos, getArticulosFacade().getAllOrderByName(), true);
		}
		return cmbArticulos;
	}

	public ArticuloFacadeRemote getArticulosFacade() {
		if(articulosFacade == null){
			articulosFacade = GTLBeanFactory.getInstance().getBean2(ArticuloFacadeRemote.class);
		}
		return articulosFacade;
	}

	public GamaColorFacadeRemote getGamaFacade() {
		if(gamaFacade == null){
			gamaFacade = GTLBeanFactory.getInstance().getBean2(GamaColorFacadeRemote.class);
		}
		return gamaFacade;
	}

	
	public Boolean getFlagModificarTenido() {
		return flagModificarTenido;
	}

	
	public void setFlagModificarTenido(Boolean flagModificarTenido) {
		this.flagModificarTenido = flagModificarTenido;
	}
}
