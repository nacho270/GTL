package ar.com.textillevel.gui.acciones;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ar.clarin.fwjava.componentes.CLJNumericTextField;
import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.CLJTextField;
import ar.clarin.fwjava.util.GuiUtil;
import ar.clarin.fwjava.util.StringUtil;
import ar.com.textillevel.entidades.documentos.factura.itemfactura.ItemFactura;
import ar.com.textillevel.entidades.documentos.factura.itemfactura.ItemFacturaBonificacion;
import ar.com.textillevel.entidades.documentos.factura.itemfactura.ItemFacturaOtro;
import ar.com.textillevel.entidades.documentos.factura.itemfactura.ItemFacturaPercepcion;
import ar.com.textillevel.entidades.documentos.factura.itemfactura.ItemFacturaPrecioMateriaPrima;
import ar.com.textillevel.entidades.documentos.factura.itemfactura.ItemFacturaProducto;
import ar.com.textillevel.entidades.documentos.factura.itemfactura.ItemFacturaRecargo;
import ar.com.textillevel.entidades.documentos.factura.itemfactura.ItemFacturaSeguro;
import ar.com.textillevel.entidades.documentos.factura.itemfactura.ItemFacturaTelaCruda;
import ar.com.textillevel.entidades.documentos.factura.itemfactura.ItemFacturaTubo;
import ar.com.textillevel.entidades.enums.ETipoItemFactura;
import ar.com.textillevel.entidades.enums.ETipoVentaStock;
import ar.com.textillevel.entidades.enums.EUnidad;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.ventas.articulos.Articulo;
import ar.com.textillevel.entidades.ventas.materiaprima.PrecioMateriaPrima;
import ar.com.textillevel.entidades.ventas.productos.Producto;
import ar.com.textillevel.entidades.ventas.productos.ProductoReprocesoSinCargo;
import ar.com.textillevel.facade.api.remote.ArticuloFacadeRemote;
import ar.com.textillevel.facade.api.remote.PrecioMateriaPrimaFacadeRemote;
import ar.com.textillevel.facade.api.remote.ProductoFacadeRemote;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.util.GTLBeanFactory;
import ar.com.textillevel.util.GestorDeFacturas;

public class JDialogAgregarItemFactura extends JDialog {

	private static final long serialVersionUID = -2986909559712451245L;

	private static final String PANEL_BONIFICACION = "bonificacion";
	private static final String PANEL_RECARGO = "recargo";
	private static final String PANEL_TUBOS = "tubos";
	private static final String PANEL_PRODUCTOS = "productos";
	private static final String PANEL_SEGURO = "seguro";
	private static final String PANEL_PERCEPCION = "percepcion";
	private static final String PANEL_STOCK = "stock";
	private static final String PANEL_TELA_CRUDA = "telaCruda";
	private static final String PANEL_OTRO = "otro";

	private JComboBox cmbTipoItemFactura;

	private ItemFactura itemFacturaSeleccionado;

	private boolean isAcepto;
	private Integer nroPiezas;
	private Cliente cliente;
	private BigDecimal precioTubo;
	private BigDecimal porcentajeSeguro;
	private BigDecimal stockActual;

	private JPanel panGeneral;
	private JPanel panelControlesExtra;
	private JPanel panelCombo;
	private JPanel panelBotones;
	private JPanel panelBonificacion;
	private JPanel panelRecargo;
	private JPanel panelProducto;
	private JPanel panelTubos;
	private JPanel panelSeguro;
	private JPanel panelPercepcion;
	private JPanel panelStock;
	private JPanel panelOtro;
	private JPanel panelTelaCruda;

	private CLJTextField txtDescripcion;
	
	private CLJTextField txtBonificacion;
	private CLJTextField txtRecargo;
	private CLJTextField txtCantMetros;
	private CLJTextField txtPrecioUnitario;
	private CLJTextField txtPorcentajeSeguro;
	private CLJTextField txtPrecioTubo;
	private CLJTextField txtPorcentajePercepcion;
	private CLJNumericTextField txtTubos;
	private JComboBox cmbProductos;
	private JComboBox cmbArticulos;
	
	//PANEL STOCK
	private JComboBox cmbTipoVentaStock; //tela o cilindro
	private JComboBox cmbPrecioMateriaPrima;
	private CLJTextField txtStockInput;
	private JLabel lblStockActual;
	
	//PANEL TELA CRUDA
	private CLJTextField txtPrecioUnitarioTelaCruda;
	private CLJTextField txtCantidadMetrosTelaCruda;
	
	private CardLayout cardLayout;

	private JButton btnAceptar;
	private JButton btnSalir;

	private ProductoFacadeRemote productoFacade;
	private PrecioMateriaPrimaFacadeRemote precioMateriaPrimaFacade;
	private ArticuloFacadeRemote articulosFacade;

	public JDialogAgregarItemFactura(Dialog padre, Integer nroPiezas, Cliente cliente, BigDecimal precioTubo, BigDecimal porcentajeSeguro) {
		super(padre);
		this.setPorcentajeSeguro(porcentajeSeguro);
		this.setPrecioTubo(precioTubo);
		this.setNroPiezas(nroPiezas);
		this.setCliente(cliente);
		setUpComponentes();
		setUpScreen();
		cambiarPanel(ETipoItemFactura.PRODUCTO);
		pack();
	}

	private void setUpComponentes() {
		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				salir();
			}
		});
		this.add(getPanGeneral(), BorderLayout.CENTER);
		this.add(getPanelBotones(), BorderLayout.SOUTH);
	}

	private void setUpScreen() {
		this.setTitle("Agregar item de factura");
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setSize(new Dimension(400, 190));
		GuiUtil.centrar(this);
		this.setResizable(false);
		this.setModal(true);
	}

	public ItemFactura getItemFacturaSeleccionado() {
		return itemFacturaSeleccionado;
	}

	private void setItemFacturaSeleccionado(ItemFactura itemFacturaSeleccionado) {
		this.itemFacturaSeleccionado = itemFacturaSeleccionado;
	}

	public boolean isAcepto() {
		return isAcepto;
	}

	private void setAcepto(boolean isAcepto) {
		this.isAcepto = isAcepto;
	}

	private Integer getNroPiezas() {
		return nroPiezas;
	}

	private void setNroPiezas(Integer nroPiezas) {
		this.nroPiezas = nroPiezas;
	}

	private JComboBox getCmbTipoItemFactura() {
		if (cmbTipoItemFactura == null) {
			cmbTipoItemFactura = new JComboBox();
			GuiUtil.llenarCombo(cmbTipoItemFactura, Arrays.asList(ETipoItemFactura.values()), true);
			cmbTipoItemFactura.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						ETipoItemFactura itemSeleccionado = (ETipoItemFactura) cmbTipoItemFactura.getSelectedItem();
						cambiarPanel(itemSeleccionado);
						JDialogAgregarItemFactura.this.pack();
					}
				}
			});
		}
		return cmbTipoItemFactura;
	}

	private void cambiarPanel(ETipoItemFactura itemSeleccionado) {
		switch (itemSeleccionado) {
			case BONIFICACION: {
				getPanelControlesExtra().setVisible(true);
				getCardLayout().show(getPanelControlesExtra(), PANEL_BONIFICACION);
				setItemFacturaSeleccionado(new ItemFacturaBonificacion());
				this.setSize(new Dimension(370, 160));
				break;
			}
			case RECARGO: {
				getPanelControlesExtra().setVisible(true);
				getCardLayout().show(getPanelControlesExtra(), PANEL_RECARGO);
				setItemFacturaSeleccionado(new ItemFacturaRecargo());
				this.setSize(new Dimension(370, 160));
				break;
			}
			case PRODUCTO: {
				getPanelControlesExtra().setVisible(true);
				getCardLayout().show(getPanelControlesExtra(), PANEL_PRODUCTOS);
				setItemFacturaSeleccionado(new ItemFacturaProducto());
				BigDecimal precio = GestorDeFacturas.getInstance().getPrecio((Producto) getCmbProductos().getSelectedItem(), getCliente().getId());
				getTxtPrecioUnitario().setText(String.valueOf(precio));
				this.setSize(new Dimension(400, 190));
				break;
			}
			case TUBOS: {
				getPanelControlesExtra().setVisible(true);
				getCardLayout().show(getPanelControlesExtra(), PANEL_TUBOS);
				setItemFacturaSeleccionado(new ItemFacturaTubo());
				this.setSize(new Dimension(400, 190));
				break;
			}
			case SEGURO: {
				getPanelControlesExtra().setVisible(true);
				getCardLayout().show(getPanelControlesExtra(), PANEL_SEGURO);
				setItemFacturaSeleccionado(new ItemFacturaSeguro());
				this.setSize(new Dimension(370, 160));
				break;
			}
			case PERCEPCION: {
				getPanelControlesExtra().setVisible(true);
				getCardLayout().show(getPanelControlesExtra(), PANEL_PERCEPCION);
				setItemFacturaSeleccionado(new ItemFacturaPercepcion());
				this.setSize(new Dimension(370, 160));
				break;
			}
			case STOCK:{
				getPanelControlesExtra().setVisible(true);
				getCardLayout().show(getPanelControlesExtra(), PANEL_STOCK);
				setItemFacturaSeleccionado(new ItemFacturaPrecioMateriaPrima());
				llenarComboPrecioMateriaPrima((ETipoVentaStock)getCmbTipoVentaStock().getSelectedItem());
				this.setSize(new Dimension(400, 250));
				break;
			}
			case TELA_CRUDA:{
				getPanelControlesExtra().setVisible(true);
				getCardLayout().show(getPanelControlesExtra(), PANEL_TELA_CRUDA);
				setItemFacturaSeleccionado(new ItemFacturaTelaCruda());
				llenarComboPrecioMateriaPrima((ETipoVentaStock)getCmbTipoVentaStock().getSelectedItem());
				this.setSize(new Dimension(400, 250));
				break;
			}
			case OTRO:{
				getPanelControlesExtra().setVisible(true);
				getCardLayout().show(getPanelControlesExtra(), PANEL_OTRO);
				setItemFacturaSeleccionado(new ItemFacturaOtro());
				getTxtPrecioUnitario().setText("");
				this.setSize(new Dimension(400, 250));
				break;
			}
		}
	}

	private JPanel getPanGeneral() {
		if (panGeneral == null) {
			panGeneral = new JPanel();
			panGeneral.setLayout(new GridBagLayout());
			panGeneral.add(getPanelCombo(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 2, 1, 1, 0));
			panGeneral.add(getPanelControlesExtra(), GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 5), 3, 1, 1, 0));
		}
		return panGeneral;
	}

	private JPanel getPanelBonificacion() {
		if (panelBonificacion == null) {
			panelBonificacion = new JPanel();
			panelBonificacion.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 2));
			panelBonificacion.add(new JLabel("Monto: "));
			panelBonificacion.add(getTxtBonificacion());
		}
		return panelBonificacion;
	}

	private JPanel getPanelRecargo() {
		if (panelRecargo == null) {
			panelRecargo = new JPanel();
			panelRecargo.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 2));
			panelRecargo.add(new JLabel("Monto: "));
			panelRecargo.add(getTxtRecargo());
		}
		return panelRecargo;
	}

	private JPanel getPanelProducto() {
		if (panelProducto == null) {
			panelProducto = new JPanel();
			panelProducto.setLayout(new GridBagLayout());
			panelProducto.add(new JLabel("Producto: "), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panelProducto.add(getCmbProductos(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panelProducto.add(new JLabel("Cantidad (mts): "), GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panelProducto.add(getTxtCantMetros(), GenericUtils.createGridBagConstraints(1, 1, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
		}
		return panelProducto;
	}

	private JPanel getPanelTelaCruda() {
		if (panelTelaCruda == null) {
			panelTelaCruda = new JPanel();
			panelTelaCruda.setLayout(new GridBagLayout());
			panelTelaCruda.add(new JLabel("Art�culo: "), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panelTelaCruda.add(getCmbArticulos(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panelTelaCruda.add(new JLabel("Cantidad (mts): "), GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panelTelaCruda.add(getTxtCantidadMetrosTelaCruda(), GenericUtils.createGridBagConstraints(1, 1, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panelTelaCruda.add(new JLabel("Precio unitario: "), GenericUtils.createGridBagConstraints(0, 2, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panelTelaCruda.add(getTxtPrecioUnitarioTelaCruda(), GenericUtils.createGridBagConstraints(1, 2, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));

		}
		return panelTelaCruda;
	}
	
	private JPanel getPanelOtro() {
		if(panelOtro == null){
			panelOtro = new JPanel(new GridBagLayout());
			panelOtro.add(new JLabel("Descripcion: "), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panelOtro.add(getTxtDescripcion(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panelOtro.add(new JLabel("Precio unitario: "), GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panelOtro.add(getTxtPrecioUnitario(), GenericUtils.createGridBagConstraints(1, 1, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
		}
		return panelOtro;
	}
	
	private JPanel getPanelTubos() {
		if (panelTubos == null) {
			panelTubos = new JPanel();
			panelTubos.setLayout(new GridBagLayout());
			panelTubos.add(new JLabel("Tubos: "), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panelTubos.add(getTxtTubos(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panelTubos.add(new JLabel("Precio: "), GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panelTubos.add(getTxtPrecioTubo(), GenericUtils.createGridBagConstraints(1, 1, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
		}
		return panelTubos;
	}

	private CardLayout getCardLayout() {
		return cardLayout;
	}

	private JButton getBtnAceptar() {
		if (btnAceptar == null) {
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (validar()) {
						actualizarItemSeleccionado();
						setAcepto(true);
						dispose();
					}
				}
			});
		}
		return btnAceptar;
	}

	private void actualizarItemSeleccionado() {
		ETipoItemFactura itemSeleccionado = (ETipoItemFactura) getCmbTipoItemFactura().getSelectedItem();
		switch (itemSeleccionado) {
			case BONIFICACION: {
				getItemFacturaSeleccionado().setCantidad(new BigDecimal(1));
				getItemFacturaSeleccionado().setImporte(new BigDecimal(Double.valueOf(getTxtBonificacion().getText().trim().replace(',', '.')) * -1));
				getItemFacturaSeleccionado().setPrecioUnitario(new BigDecimal(Double.valueOf(getTxtBonificacion().getText().trim().replace(',', '.')) * -1));
				getItemFacturaSeleccionado().setUnidad(EUnidad.UNIDAD);
				getItemFacturaSeleccionado().setDescripcion("BONIFICACION");
				break;
			}
			case RECARGO: {
				getItemFacturaSeleccionado().setCantidad(new BigDecimal(1));
				getItemFacturaSeleccionado().setImporte(new BigDecimal(Double.valueOf(getTxtRecargo().getText().trim().replace(',', '.'))));
				getItemFacturaSeleccionado().setPrecioUnitario(new BigDecimal(Double.valueOf(getTxtRecargo().getText().trim().replace(',', '.'))));
				getItemFacturaSeleccionado().setUnidad(EUnidad.UNIDAD);
				getItemFacturaSeleccionado().setDescripcion("RECARGO");
				break;
			}
			case PRODUCTO: {
				((ItemFacturaProducto) getItemFacturaSeleccionado()).setProducto((Producto) getCmbProductos().getSelectedItem());
				getItemFacturaSeleccionado().setDescripcion(((Producto) getCmbProductos().getSelectedItem()).getDescripcion());
				getItemFacturaSeleccionado().setUnidad(((Producto) getCmbProductos().getSelectedItem()).getTipo().getUnidad());
				BigDecimal cantidad = new BigDecimal(getTxtCantMetros().getText().trim().replace(',', '.'));
				//BigDecimal precioUnitario = new BigDecimal(getTxtPrecioUnitario().getText().trim().replace(',', '.'));
				BigDecimal precioUnitario = GestorDeFacturas.getInstance().getPrecio((Producto) getCmbProductos().getSelectedItem(), getCliente().getId());
				getItemFacturaSeleccionado().setCantidad(cantidad);
				getItemFacturaSeleccionado().setPrecioUnitario(precioUnitario);
				getItemFacturaSeleccionado().setImporte(new BigDecimal(cantidad.doubleValue() * precioUnitario.doubleValue()));
				break;
			}
			case TELA_CRUDA:{
				((ItemFacturaTelaCruda) getItemFacturaSeleccionado()).setArticulo((Articulo)getCmbArticulos().getSelectedItem());
				getItemFacturaSeleccionado().setDescripcion( ((Articulo)getCmbArticulos().getSelectedItem()).getDescripcion());
				getItemFacturaSeleccionado().setUnidad(EUnidad.METROS);
				BigDecimal cantidad = new BigDecimal(getTxtCantidadMetrosTelaCruda().getText().trim().replace(',', '.'));
				BigDecimal precioUnitario = new BigDecimal(getTxtPrecioUnitarioTelaCruda().getText().trim().replace(',', '.'));
				getItemFacturaSeleccionado().setCantidad(cantidad);
				getItemFacturaSeleccionado().setPrecioUnitario(precioUnitario);
				getItemFacturaSeleccionado().setImporte(new BigDecimal(cantidad.doubleValue() * precioUnitario.doubleValue()));
				break;
			}
			case TUBOS: {
				BigDecimal cantidad = new BigDecimal(getNroPiezas());
				getItemFacturaSeleccionado().setPrecioUnitario(getPrecioTubo());
				((ItemFacturaTubo)getItemFacturaSeleccionado()).setPrecioTubo(getPrecioTubo());
				getItemFacturaSeleccionado().setCantidad(cantidad);
				getItemFacturaSeleccionado().setImporte(new BigDecimal(getPrecioTubo().doubleValue() * cantidad.doubleValue()));
				getItemFacturaSeleccionado().setUnidad(EUnidad.UNIDAD);
				getItemFacturaSeleccionado().setDescripcion("TUBOS PARA MERCADERIA");
				break;
			}
			case SEGURO: {
				getItemFacturaSeleccionado().setCantidad(new BigDecimal(1));
				getItemFacturaSeleccionado().setUnidad(EUnidad.UNIDAD);
				getItemFacturaSeleccionado().setDescripcion("SEGURO DE MERCADERIA");
				((ItemFacturaSeguro) getItemFacturaSeleccionado()).setPorcentajeSeguro(new BigDecimal(getTxtPorcentajeSeguro().getText().replace(',', '.')));
				break;
			}
			case PERCEPCION: {
				getItemFacturaSeleccionado().setCantidad(new BigDecimal(1));
				getItemFacturaSeleccionado().setUnidad(EUnidad.UNIDAD);
				getItemFacturaSeleccionado().setDescripcion("PERCEPCION");
				((ItemFacturaPercepcion) getItemFacturaSeleccionado()).setPorcentajePercepcion(new BigDecimal(getTxtPorcentajePercepcion().getText().replace(',', '.')));
				break;
			}
			case STOCK:{
				BigDecimal stockIngresado = new BigDecimal(getTxtStockInput().getText().replace(',', '.'));
				PrecioMateriaPrima precioMateriaPrima = (PrecioMateriaPrima)getCmbPrecioMateriaPrima().getSelectedItem();
				getItemFacturaSeleccionado().setCantidad(stockIngresado);
				getItemFacturaSeleccionado().setUnidad(precioMateriaPrima.getMateriaPrima().getUnidad());
				getItemFacturaSeleccionado().setDescripcion(precioMateriaPrima.getMateriaPrima().getDescripcion() + " - " + precioMateriaPrima.getAlias());
				getItemFacturaSeleccionado().setPrecioUnitario(precioMateriaPrima.getPrecio());
				getItemFacturaSeleccionado().setImporte(stockIngresado.multiply(precioMateriaPrima.getPrecio()));
				((ItemFacturaPrecioMateriaPrima)getItemFacturaSeleccionado()).setPrecioMateriaPrima(precioMateriaPrima);
				break;
			}
			case OTRO:{
				getItemFacturaSeleccionado().setCantidad(new BigDecimal(1));
				getItemFacturaSeleccionado().setUnidad(EUnidad.UNIDAD);
				getItemFacturaSeleccionado().setDescripcion(getTxtDescripcion().getText().trim().toUpperCase());
				getItemFacturaSeleccionado().setImporte(new BigDecimal(Double.valueOf(getTxtPrecioUnitario().getText().trim().replace(',', '.'))));
				getItemFacturaSeleccionado().setPrecioUnitario(new BigDecimal(Double.valueOf(getTxtPrecioUnitario().getText().trim().replace(',', '.'))));
				break;
			}
		}
	}

	private boolean validar() {
		ETipoItemFactura itemSeleccionado = (ETipoItemFactura) getCmbTipoItemFactura().getSelectedItem();
		switch (itemSeleccionado) {
			case BONIFICACION: {
				if(StringUtil.isNullOrEmpty(getTxtBonificacion().getText())){
					CLJOptionPane.showErrorMessage(this, "Debe completar todos los campos", "Error");
					getTxtBonificacion().requestFocus();
					return false;
				} else {
					if (!GenericUtils.esNumerico((getTxtBonificacion().getText()))) {
						CLJOptionPane.showErrorMessage(this, "El campo es numerico", "Error");
						getTxtBonificacion().requestFocus();
						return false;
					}
				}
				return true;
			}
			case RECARGO: {
				if(StringUtil.isNullOrEmpty(getTxtRecargo().getText())){
					CLJOptionPane.showErrorMessage(this, "Debe completar todos los campos", "Error");
					getTxtRecargo().requestFocus();
					return false;
				} else {
					if (!GenericUtils.esNumerico((getTxtRecargo().getText()))) {
						CLJOptionPane.showErrorMessage(this, "El campo es numerico", "Error");
						getTxtRecargo().requestFocus();
						return false;
					}
				}
				return true;
			}
			case PRODUCTO: {
				if(StringUtil.isNullOrEmpty(getTxtCantMetros().getText())){
					CLJOptionPane.showErrorMessage(this, "Debe completar todos los campos", "Error");
					getTxtCantMetros().requestFocus();
					return false;
				} else {
					if (!GenericUtils.esNumerico((getTxtCantMetros().getText()))) {
						CLJOptionPane.showErrorMessage(this, "El campo es numerico", "Error");
						getTxtCantMetros().requestFocus();
						return false;
					}
				}
				return true;
			}
			case TELA_CRUDA: {
				if(StringUtil.isNullOrEmpty(getTxtCantidadMetrosTelaCruda().getText())){
					CLJOptionPane.showErrorMessage(this, "Debe completar todos los campos", "Error");
					getTxtCantidadMetrosTelaCruda().requestFocus();
					return false;
				}				
				if(StringUtil.isNullOrEmpty(getTxtPrecioUnitarioTelaCruda().getText())){
					CLJOptionPane.showErrorMessage(this, "Debe completar todos los campos", "Error");
					getTxtPrecioUnitarioTelaCruda().requestFocus();
					return false;
				}
				if (!GenericUtils.esNumerico((getTxtCantidadMetrosTelaCruda().getText()))) {
					CLJOptionPane.showErrorMessage(this, "El campo es numerico", "Error");
					getTxtCantMetros().requestFocus();
					return false;
				}
				if (!GenericUtils.esNumerico((getTxtPrecioUnitarioTelaCruda().getText()))) {
					CLJOptionPane.showErrorMessage(this, "El campo es numerico", "Error");
					getTxtCantMetros().requestFocus();
					return false;
				}
				return true;
			}
			case TUBOS: {
				if(StringUtil.isNullOrEmpty(getTxtPrecioTubo().getText())){
					CLJOptionPane.showErrorMessage(this, "Debe completar todos los campos", "Error");
					getTxtPrecioTubo().requestFocus();
					return false;
				} else {
					if (!GenericUtils.esNumerico((getTxtPrecioTubo().getText()))) {
						CLJOptionPane.showErrorMessage(this, "El campo es numerico", "Error");
						getTxtPrecioTubo().requestFocus();
						return false;
					}
				}
				if(StringUtil.isNullOrEmpty(getTxtTubos().getText())){
					CLJOptionPane.showErrorMessage(this, "Debe completar todos los campos", "Error");
					getTxtTubos().requestFocus();
					return false;
				} else {
					if (!GenericUtils.esNumerico((getTxtTubos().getText()))) {
						CLJOptionPane.showErrorMessage(this, "El campo es numerico", "Error");
						getTxtTubos().requestFocus();
						return false;
					}
				}
				return true;
			}
			case SEGURO: {
				if(StringUtil.isNullOrEmpty(getTxtPorcentajeSeguro().getText())){
					CLJOptionPane.showErrorMessage(this, "Debe completar todos los campos", "Error");
					getTxtPorcentajeSeguro().requestFocus();
					return false;
				} else {
					if (!GenericUtils.esNumerico((getTxtPorcentajeSeguro().getText()))) {
						CLJOptionPane.showErrorMessage(this, "El campo es numerico", "Error");
						getTxtPorcentajeSeguro().requestFocus();
						return false;
					}
				}
				return true;
			}
			case PERCEPCION: {
				if(StringUtil.isNullOrEmpty(getTxtPorcentajePercepcion().getText())){
					CLJOptionPane.showErrorMessage(this, "Debe completar todos los campos", "Error");
					getTxtPorcentajePercepcion().requestFocus();
					return false;
				} else {
					if (!GenericUtils.esNumerico((getTxtPorcentajePercepcion().getText()))) {
						CLJOptionPane.showErrorMessage(this, "El campo es numerico", "Error");
						getTxtPorcentajePercepcion().requestFocus();
						return false;
					}
				}
				return true;
			}
			case STOCK:{
				if(StringUtil.isNullOrEmpty(getTxtStockInput().getText())){
					CLJOptionPane.showErrorMessage(this, "Debe completar todos los campos", "Error");
					getTxtStockInput().requestFocus();
					return false;
				} else {
					if (!GenericUtils.esNumerico((getTxtStockInput().getText()))) {
						CLJOptionPane.showErrorMessage(this, "El campo es numerico", "Error");
						getTxtStockInput().requestFocus();
						return false;
					}
					BigDecimal stockIngresado = new BigDecimal(getTxtStockInput().getText().replace(',', '.'));
					if(stockIngresado.compareTo(getStockActual()) == 1){
						CLJOptionPane.showErrorMessage(this, "El stock ingresado debe ser menor a " + getStockActual().doubleValue(), "Error");
						getTxtStockInput().requestFocus();
						return false;
					}
				}
				return true;
			}
			case OTRO:{
				if(StringUtil.isNullOrEmpty(getTxtDescripcion().getText())){
					CLJOptionPane.showErrorMessage(this, "Debe ingresar la descripci�n del item", "Error");
					getTxtDescripcion().requestFocus();
					return false;
				}
				if(StringUtil.isNullOrEmpty(getTxtPrecioUnitario().getText())){
					CLJOptionPane.showErrorMessage(this, "Debe ingresar el precio del item", "Error");
					getTxtPrecioUnitario().requestFocus();
					return false;
				}
				if(!GenericUtils.esNumerico(getTxtPrecioUnitario().getText())){
					CLJOptionPane.showErrorMessage(this, "El precio debe ser num�rico", "Error");
					getTxtPrecioUnitario().requestFocus();
					return false;
				}
				return true;
			}
		}
		return false;
	}

	private JButton getBtnSalir() {
		if (btnSalir == null) {
			btnSalir = new JButton("Salir");
			btnSalir.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					salir();
				}
			});
		}
		return btnSalir;
	}

	private JPanel getPanelControlesExtra() {
		if (panelControlesExtra == null) {
			panelControlesExtra = new JPanel();
			cardLayout = new CardLayout(5, 2);
			panelControlesExtra.setVisible(false);
			panelControlesExtra.setLayout(cardLayout);
			panelControlesExtra.add(PANEL_BONIFICACION, getPanelBonificacion());
			panelControlesExtra.add(PANEL_PRODUCTOS, getPanelProducto());
			panelControlesExtra.add(PANEL_RECARGO, getPanelRecargo());
			panelControlesExtra.add(PANEL_TUBOS, getPanelTubos());
			panelControlesExtra.add(PANEL_SEGURO, getPanelSeguro());
			panelControlesExtra.add(PANEL_PERCEPCION, getPanelPercepcion());
			panelControlesExtra.add(PANEL_STOCK, getPanelStock());
			panelControlesExtra.add(PANEL_TELA_CRUDA, getPanelTelaCruda());
			panelControlesExtra.add(PANEL_OTRO, getPanelOtro());
		}
		return panelControlesExtra;
	}

	private JPanel getPanelCombo() {
		if (panelCombo == null) {
			panelCombo = new JPanel();
			panelCombo.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 2));
			panelCombo.add(new JLabel("Tipo de item: "));
			panelCombo.add(getCmbTipoItemFactura());
		}
		return panelCombo;
	}

	private JPanel getPanelBotones() {
		if (panelBotones == null) {
			panelBotones = new JPanel();
			panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 2));
			panelBotones.add(getBtnAceptar());
			panelBotones.add(getBtnSalir());
		}
		return panelBotones;
	}

	private CLJTextField getTxtBonificacion() {
		if (txtBonificacion == null) {
			txtBonificacion = new CLJTextField();
			txtBonificacion.setPreferredSize(new Dimension(100, 20));
		}
		return txtBonificacion;
	}

	private CLJTextField getTxtRecargo() {
		if (txtRecargo == null) {
			txtRecargo = new CLJTextField();
			txtRecargo.setPreferredSize(new Dimension(100, 20));
		}
		return txtRecargo;
	}

	private CLJNumericTextField getTxtTubos() {
		if (txtTubos == null) {
			txtTubos = new CLJNumericTextField();
			txtTubos.setPreferredSize(new Dimension(350, 20));
			txtTubos.setSize(new Dimension(350, 20));
			txtTubos.setText(String.valueOf(getNroPiezas()));
			txtTubos.addKeyListener(new KeyAdapter() {

				@Override
				public void keyReleased(KeyEvent e) {
					setNroPiezas(getTxtTubos().getValueWithNull());
				}
			});
		}
		return txtTubos;
	}

	private JComboBox getCmbProductos() {
		if (cmbProductos == null) {
			cmbProductos = new JComboBox();
			List<Producto> allOrderByName = getProductoFacade().getAllOrderByName();
			List<Producto> allOrderByNameSinReproceso = new ArrayList<Producto>();
			for(Producto p : allOrderByName){
				if(!(p instanceof ProductoReprocesoSinCargo)){
					allOrderByNameSinReproceso.add(p);
				}
			}
			GuiUtil.llenarCombo(cmbProductos, allOrderByNameSinReproceso, true);
		}
		return cmbProductos;
	}

	private ProductoFacadeRemote getProductoFacade() {
		if (productoFacade == null) {
			productoFacade = GTLBeanFactory.getInstance().getBean2(ProductoFacadeRemote.class);
		}
		return productoFacade;
	}

	private void salir() {
		setAcepto(false);
		dispose();
	}

	private CLJTextField getTxtCantMetros() {
		if (txtCantMetros == null) {
			txtCantMetros = new CLJTextField();
			txtCantMetros.setPreferredSize(new Dimension(100, 20));
		}
		return txtCantMetros;
	}

	private CLJTextField getTxtPrecioUnitario() {
		if (txtPrecioUnitario == null) {
			txtPrecioUnitario = new CLJTextField();
			txtPrecioUnitario.setPreferredSize(new Dimension(100, 20));
		}
		return txtPrecioUnitario;
	}

	private Cliente getCliente() {
		return cliente;
	}

	private void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	private BigDecimal getPrecioTubo() {
		return precioTubo;
	}

	private void setPrecioTubo(BigDecimal precioTubo) {
		this.precioTubo = precioTubo;
	}

	private BigDecimal getPorcentajeSeguro() {
		return porcentajeSeguro;
	}

	private void setPorcentajeSeguro(BigDecimal porcentajeSeguro) {
		this.porcentajeSeguro = porcentajeSeguro;
	}

	private CLJTextField getTxtPorcentajeSeguro() {
		if (txtPorcentajeSeguro == null) {
			txtPorcentajeSeguro = new CLJTextField();
			txtPorcentajeSeguro.setText(String.valueOf(getPorcentajeSeguro().doubleValue()));
			txtPorcentajeSeguro.setPreferredSize(new Dimension(100, 20));
		}
		return txtPorcentajeSeguro;
	}

	private JPanel getPanelSeguro() {
		if (panelSeguro == null) {
			panelSeguro = new JPanel();
			panelSeguro.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 2));
			panelSeguro.add(new JLabel("Porcentaje: "));
			panelSeguro.add(getTxtPorcentajeSeguro());
		}
		return panelSeguro;
	}

	private CLJTextField getTxtPrecioTubo() {
		if (txtPrecioTubo == null) {
			txtPrecioTubo = new CLJTextField();
			txtPrecioTubo.setPreferredSize(new Dimension(350, 20));
			txtPrecioTubo.setSize(new Dimension(350, 20));
			txtPrecioTubo.setText(String.valueOf(getPrecioTubo().doubleValue()));
			txtPrecioTubo.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					String text = getTxtPrecioTubo().getText().trim().replace(',', '.');
					if (GenericUtils.esNumerico(text)) {
						setPrecioTubo(new BigDecimal(Double.valueOf(text)));
					}
				}
			});
		}
		return txtPrecioTubo;
	}
	
	private JPanel getPanelPercepcion() {
		if(panelPercepcion == null){
			panelPercepcion = new JPanel();
			panelPercepcion.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 2));
			panelPercepcion.add(new JLabel("Porcentaje: "));
			panelPercepcion.add(getTxtPorcentajePercepcion());
		}
		return panelPercepcion;
	}
	
	private CLJTextField getTxtPorcentajePercepcion() {
		if(txtPorcentajePercepcion == null){
			txtPorcentajePercepcion = new CLJTextField();
			txtPorcentajePercepcion.setPreferredSize(new Dimension(100, 20));
		}
		return txtPorcentajePercepcion;
	}

	private JPanel getPanelStock(){
		if(panelStock == null){
			panelStock = new JPanel();
			panelStock.setLayout(new GridBagLayout());
			panelStock.add(new JLabel("Tipo de venta: "), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.EAST,GridBagConstraints.NONE, new Insets(10, 10, 5, 5),1, 1, 0, 0));
			panelStock.add(getCmbTipoVentaStock(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.EAST,GridBagConstraints.HORIZONTAL, new Insets(10, 10,5, 5), 2, 1, 1, 0));
			panelStock.add(new JLabel("Materia prima: "), GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.EAST,GridBagConstraints.NONE, new Insets(10, 10, 5, 5),1, 1, 0, 0));
			panelStock.add(getCmbPrecioMateriaPrima(), GenericUtils.createGridBagConstraints(1, 1, GridBagConstraints.EAST,GridBagConstraints.HORIZONTAL, new Insets(10, 10,5, 5), 2, 1, 1, 0));
			JLabel lblTxtStockActual = new JLabel("Stock actual: ");
			lblTxtStockActual.setFont(new Font(lblTxtStockActual.getFont().getName(), Font.BOLD, lblTxtStockActual.getFont().getSize()));
			panelStock.add(lblTxtStockActual, GenericUtils.createGridBagConstraints(0, 2, GridBagConstraints.EAST,GridBagConstraints.NONE, new Insets(10, 10, 5, 5),1, 1, 0, 0));
			panelStock.add(getLblStockActual(), GenericUtils.createGridBagConstraints(1, 2, GridBagConstraints.EAST,GridBagConstraints.HORIZONTAL, new Insets(10, 10,5, 5), 1, 1, 0, 0));
			JLabel lblIngreseStock = new JLabel("Ingrese stock: ");
			lblIngreseStock.setFont(new Font(lblIngreseStock.getFont().getName(), Font.BOLD, lblIngreseStock.getFont().getSize()));
			panelStock.add(lblIngreseStock, GenericUtils.createGridBagConstraints(0, 3, GridBagConstraints.EAST,GridBagConstraints.NONE, new Insets(10, 10, 5, 5),1,0, 0, 0));
			panelStock.add(getTxtStockInput(), GenericUtils.createGridBagConstraints(1, 3, GridBagConstraints.EAST,GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5),2, 1, 1, 0));
		}
		return panelStock;
	}
	
	private JComboBox getCmbTipoVentaStock() {
		if(cmbTipoVentaStock == null){
			cmbTipoVentaStock = new JComboBox();
			GuiUtil.llenarCombo(cmbTipoVentaStock, Arrays.asList(ETipoVentaStock.values()), true);
			cmbTipoVentaStock.addItemListener(new ItemListener(){
				public void itemStateChanged(ItemEvent e) {
					if(e.getStateChange() == ItemEvent.SELECTED){
						llenarComboPrecioMateriaPrima((ETipoVentaStock)cmbTipoVentaStock.getSelectedItem());
					}
				}
			});
		}
		return cmbTipoVentaStock;
	}

	private JComboBox getCmbPrecioMateriaPrima() {
		if(cmbPrecioMateriaPrima == null){
			cmbPrecioMateriaPrima = new JComboBox();
			cmbPrecioMateriaPrima.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if(e.getStateChange() == ItemEvent.SELECTED){
						PrecioMateriaPrima precioMp = (PrecioMateriaPrima)cmbPrecioMateriaPrima.getSelectedItem();
						BigDecimal stockActual = getPrecioMateriaPrimaFacade().getStockByPrecioMateriaPrima(precioMp);
						if(stockActual!=null){
							getLblStockActual().setText(String.valueOf(stockActual.doubleValue()) + " " + precioMp.getMateriaPrima().getUnidad().getDescripcion());
							setStockActual(stockActual);
						}else{
							getLblStockActual().setText("-");
							setStockActual(null);
						}
					}
				}
			});
		}
		return cmbPrecioMateriaPrima;
	}
	
	private CLJTextField getTxtStockInput() {
		if(txtStockInput == null){
			txtStockInput = new CLJTextField();
		}
		return txtStockInput;
	}

	private JLabel getLblStockActual() {
		if(lblStockActual == null){
			lblStockActual = new JLabel("-");
			lblStockActual.setFont(new Font(lblStockActual.getFont().getName(), Font.BOLD, lblStockActual.getFont().getSize()));
		}
		return lblStockActual;
	}
	
	private PrecioMateriaPrimaFacadeRemote getPrecioMateriaPrimaFacade(){
		if(precioMateriaPrimaFacade == null){
			precioMateriaPrimaFacade = GTLBeanFactory.getInstance().getBean2(PrecioMateriaPrimaFacadeRemote.class);
		}
		return precioMateriaPrimaFacade;
	}

	public BigDecimal getStockActual() {
		return stockActual;
	}
	
	public void setStockActual(BigDecimal stockActual) {
		this.stockActual = stockActual;
	}

	private void llenarComboPrecioMateriaPrima(ETipoVentaStock tipoVenta) {
		List<PrecioMateriaPrima> preciosMateriaPrimaByTipoVentaStock = getPrecioMateriaPrimaFacade().getPreciosMateriaPrimaByTipoVentaStock(tipoVenta);
		if(preciosMateriaPrimaByTipoVentaStock!=null && preciosMateriaPrimaByTipoVentaStock.size() ==0){
			getLblStockActual().setText("-");
			setStockActual(null);
		}
		GuiUtil.llenarCombo(getCmbPrecioMateriaPrima(), preciosMateriaPrimaByTipoVentaStock, true);
	}
	
	private CLJTextField getTxtDescripcion() {
		if(txtDescripcion == null){
			txtDescripcion = new CLJTextField();
			txtDescripcion.setPreferredSize(new Dimension(350, 20));
			txtDescripcion.setSize(new Dimension(350, 20));
		}
		return txtDescripcion;
	}

	private JComboBox getCmbArticulos() {
		if(cmbArticulos == null){
			cmbArticulos = new JComboBox();
			GuiUtil.llenarCombo(cmbArticulos, getArticulosFacade().getAllOrderByName(), true);
		}
		return cmbArticulos;
	}

	private ArticuloFacadeRemote getArticulosFacade() {
		if(articulosFacade == null){
			articulosFacade = GTLBeanFactory.getInstance().getBean2(ArticuloFacadeRemote.class);
		}
		return articulosFacade;
	}

	private CLJTextField getTxtPrecioUnitarioTelaCruda() {
		if (txtPrecioUnitarioTelaCruda == null) {
			txtPrecioUnitarioTelaCruda = new CLJTextField();
			txtPrecioUnitarioTelaCruda.setPreferredSize(new Dimension(100, 20));
		}
		return txtPrecioUnitarioTelaCruda;
	}

	private CLJTextField getTxtCantidadMetrosTelaCruda() {
		if (txtCantidadMetrosTelaCruda == null) {
			txtCantidadMetrosTelaCruda = new CLJTextField();
			txtCantidadMetrosTelaCruda.setPreferredSize(new Dimension(100, 20));
		}
		return txtCantidadMetrosTelaCruda;
	}
}
