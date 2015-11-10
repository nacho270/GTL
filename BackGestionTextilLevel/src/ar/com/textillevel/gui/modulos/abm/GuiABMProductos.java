package ar.com.textillevel.gui.modulos.abm;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Arrays;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTextField;
import ar.com.fwcommon.templates.GuiABMListaTemplate;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.entidades.enums.ETipoProducto;
import ar.com.textillevel.entidades.ventas.productos.Producto;
import ar.com.textillevel.facade.api.remote.ProductoFacadeRemote;
import ar.com.textillevel.gui.util.ProductoFactory;
import ar.com.textillevel.util.GTLBeanFactory;

public class GuiABMProductos extends GuiABMListaTemplate {

	private static final long serialVersionUID = -8651844648013003067L;

	private static final int MAX_LONGITUD_NOMBRE = 50;
	
	private JPanel tabDetalle;
	private JPanel panDetalle;
	
	private FWJTextField txtNombreProducto;
	private JComboBox cmbTipoProducto;

	private ProductoFacadeRemote productoFacade;
	
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
			panDetalle.add(new JLabel("Tipo de producto: "), createGridBagConstraints(0, 1,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getCmbTipoProducto(),  createGridBagConstraints(1, 1,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
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
	}

	@Override
	public void botonCancelarPresionado(int nivelNodoSeleccionado) {
		setModoEdicion(false);
		itemSelectorSeleccionado(lista.getSelectedIndex());
	}

	@Override
	public void botonEliminarPresionado(int nivelNodoSeleccionado) {
		if(lista.getSelectedIndex() >= 0) {
			if(FWJOptionPane.showQuestionMessage(this, "¿Está seguro que desea eliminar el producto seleccionado?", "Confirmación") == FWJOptionPane.YES_OPTION) {
				getProductosFacade().remove(getProductoActual());
				itemSelectorSeleccionado(-1);
			}
		}
	}

	@Override
	public boolean botonGrabarPresionado(int nivelNodoSeleccionado) {
		if(validar()) {
			capturarSetearDatos();
			Producto prod = getProductosFacade().save(getProductoActual());
			cargarLista();
			lista.setSelectedValue(prod, true);
			return true;
		}
		return false;
	}

	private void capturarSetearDatos() {
		ETipoProducto tipoArticulo = (ETipoProducto)getCmbTipoProducto().getSelectedItem();
		setProductoActual(ProductoFactory.createProducto(tipoArticulo));
		ETipoProducto tipoProducto = tipoArticulo;
		if(tipoArticulo != ETipoProducto.REPROCESO_SIN_CARGO && tipoArticulo != ETipoProducto.DEVOLUCION){
			getProductoActual().setDescripcion((tipoArticulo.getDescripcion()).trim().toUpperCase());
		}else{
			getProductoActual().setDescripcion(tipoProducto.getDescripcion().toUpperCase());
		}
	}

	private boolean validar() {
		return true;
	}

	@Override
	public boolean botonModificarPresionado(int nivelNodoSeleccionado) {
		if(nivelNodoSeleccionado >= 0) {
			setModoEdicion(true);
			getCmbTipoProducto().setEditable(false);
			getTxtNombreProducto().requestFocus();
			return true;
		} else {
			FWJOptionPane.showErrorMessage(this, "Debe seleccionar un producto", "Error");
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
			getCmbTipoProducto().setSelectedItem(getProductoActual().getTipo());
			getTxtNombreProducto().setText(getProductoActual().getDescripcion());
		}
	}

	@Override
	public void limpiarDatos() {
		getTxtNombreProducto().setText("");
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

	private FWJTextField getTxtNombreProducto() {
		if(txtNombreProducto == null){
			txtNombreProducto = new FWJTextField(MAX_LONGITUD_NOMBRE);
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
							getTxtNombreProducto().setText(ETipoProducto.REPROCESO_SIN_CARGO.getDescripcion());
							getTxtNombreProducto().setEditable(false);
						}else if(itemSeleccionado == ETipoProducto.DEVOLUCION){
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


}
