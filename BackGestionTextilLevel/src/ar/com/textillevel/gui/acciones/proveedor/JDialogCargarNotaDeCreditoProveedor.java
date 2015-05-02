package ar.com.textillevel.gui.acciones.proveedor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;
import javax.swing.text.MaskFormatter;

import main.GTLGlobalCache;

import org.apache.taglibs.string.util.StringW;

import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.CLJTable;
import ar.clarin.fwjava.componentes.CLJTextField;
import ar.clarin.fwjava.componentes.PanelTabla;
import ar.clarin.fwjava.util.DateUtil;
import ar.clarin.fwjava.util.GuiUtil;
import ar.clarin.fwjava.util.StringUtil;
import ar.com.textillevel.entidades.documentos.factura.proveedor.CorreccionFacturaProveedor;
import ar.com.textillevel.entidades.documentos.factura.proveedor.FacturaProveedor;
import ar.com.textillevel.entidades.documentos.factura.proveedor.ImpuestoItemProveedor;
import ar.com.textillevel.entidades.documentos.factura.proveedor.ItemCorreccionFacturaProveedor;
import ar.com.textillevel.entidades.documentos.factura.proveedor.ItemCorreccionMateriaPrima;
import ar.com.textillevel.entidades.documentos.factura.proveedor.ItemCorreccionResumen;
import ar.com.textillevel.entidades.documentos.factura.proveedor.ItemFacturaMateriaPrima;
import ar.com.textillevel.entidades.documentos.factura.proveedor.ItemFacturaProveedor;
import ar.com.textillevel.entidades.enums.ETipoImpuesto;
import ar.com.textillevel.entidades.enums.ETipoMoneda;
import ar.com.textillevel.entidades.gente.Proveedor;
import ar.com.textillevel.entidades.ventas.materiaprima.PrecioMateriaPrima;
import ar.com.textillevel.facade.api.remote.CorreccionFacturaProveedorFacadeRemote;
import ar.com.textillevel.facade.api.remote.FacturaProveedorFacadeRemote;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.controles.PanelDatePicker;
import ar.com.textillevel.gui.util.panels.PanelSeleccionarElementoEvent;
import ar.com.textillevel.gui.util.panels.PanelSeleccionarElementoEventListener;
import ar.com.textillevel.gui.util.panels.PanelSeleccionarElementos;
import ar.com.textillevel.util.DocumentoProveedorHelper;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogCargarNotaDeCreditoProveedor extends JDialog {

	private static final long serialVersionUID = 113122569655114902L;

	private JButton btnGuardar;
	private JButton btnSalir;
	private JFormattedTextField txtNroCorreccion;
	private PanelDatePicker panelFecha;
	private JTextField txtRazonSocial;
	private PanTablaItemFacturaCorreccion panTablaItemFacturaCorreccion;
	private PanelSeleccionarElementos<FacturaProvedorWrapper> panSelFacturasProveedor;

	private JTextField txtTotal;
	private Frame padre;
	private boolean modoConsulta;

	private CorreccionFacturaProveedor correccionFactura;
	private Proveedor proveedor;

	private CorreccionFacturaProveedorFacadeRemote correccionFacade;
	private String tipoCorreccionFactura;
	
	private JTextField txtTotalConFactor;
	private CLJTextField txtImpVarios;
	private CLJTextField txtPercepIVA;
	private CLJTable tablaImpuestos;


	private static final int COL_NOMBRE_IMPUESTO = 0;
	private static final int COL_VALOR_IMPUESTO = 1;
	private static final int COL_VALOR_IMPUESTO_CON_FACTOR = 2;


	public JDialogCargarNotaDeCreditoProveedor(Frame padre, Proveedor proveedor, CorreccionFacturaProveedor correccionFactura, String tipoCorreccionFactura, boolean modoConsulta) {
		super(padre);
		this.padre = padre;
		this.modoConsulta = modoConsulta;
		this.tipoCorreccionFactura = tipoCorreccionFactura;
		this.correccionFactura = correccionFactura;
		this.proveedor = proveedor;
		setModal(true);
		construct();
		setDatos();
	}

	private void setDatos() {
		getTxtRazonSocial().setText(proveedor.getRazonSocial());
		if(modoConsulta) {
			getBtnGuardar().setEnabled(false);
			getTxtNroCorreccion().setEditable(false);
			getTxtImpVarios().setEditable(false);
			getTxtPercepIVA().setEditable(false);
			getPanelFecha().setEnabled(false);
			getPanelFecha().setSelectedDate(correccionFactura.getFechaIngreso());
			getPanTablaItemFacturaCorreccion().agregarElementos(correccionFactura.getItemsCorreccion());
			List<FacturaProvedorWrapper> facturaProveedorWrapperList = FacturaProvedorWrapper.toFacturaProveedorWrapperList(correccionFactura.getFacturas());
			getPanSelFacturasProveedor().setElementsAndSelectedElements(facturaProveedorWrapperList, facturaProveedorWrapperList);
			getTxtNroCorreccion().setText(correccionFactura.getNroCorreccion());
			getTxtImpVarios().setText(GenericUtils.getDecimalFormat().format(correccionFactura.getImpVarios().longValue()));
			getTxtPercepIVA().setText(GenericUtils.getDecimalFormat().format(correccionFactura.getPercepIVA().longValue()));
			GuiUtil.setEstadoPanel(getPanSelFacturasProveedor(), false);

			updateTablaImpuestos(correccionFactura.getItemsCorreccion(), getFactorMoneda(correccionFactura.getItemsCorreccion()));
			updateTotal(correccionFactura.getItemsCorreccion());
		}
	}

	private void construct() {
		setUpScreen();
		setUpComponentes();
	}

	private void setUpScreen() {
		setSize(new Dimension(975, 600));
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle("Alta de " + tipoCorreccionFactura + " de Proveedor");
		setResizable(false);
		GuiUtil.centrar(this);
	}

	private void setUpComponentes() {
		add(getPanelSuperior(), BorderLayout.NORTH);
		add(getPanelCenter(), BorderLayout.CENTER);
		add(getPanelAcciones(), BorderLayout.SOUTH);
	}

	private JPanel getPanelCenter() {
		JPanel panCenter = new JPanel();
		panCenter.setLayout(new GridBagLayout());
		GridBagConstraints gc = GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 1, 1, 1, 0.7);
		panCenter.add(getPanTablaItemFacturaCorreccion(), gc);
		gc = GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 1, 1, 0, 0.3);
		panCenter.add(getPanelTotal(), gc);
		return panCenter;
	}

	private JPanel getPanelTotal() {
		JPanel panTotal = new JPanel();
		panTotal.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		panTotal.setLayout(new GridBagLayout());

		GridBagConstraints gc = GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 1, 3, 0.6, 1);
		JScrollPane sp = new JScrollPane(getTablaImpuestos());
		sp.setBorder(BorderFactory.createTitledBorder("Impuestos"));
		panTotal.add(sp, gc);

		gc = GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0);
		panTotal.add(new JLabel("Impuestos Varios: "), gc);
		gc = GenericUtils.createGridBagConstraints(2, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0.2, 0);
		panTotal.add(getTxtImpVarios(), gc);
		
		gc = GenericUtils.createGridBagConstraints(1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0);
		panTotal.add(new JLabel("Perc. IVA: "), gc);
		gc = GenericUtils.createGridBagConstraints(2, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0.2, 0);
		panTotal.add(getTxtPercepIVA(), gc);
		
		gc = GenericUtils.createGridBagConstraints(1, 2, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0);
		panTotal.add(new JLabel("Total (Pesos): "), gc);
		gc = GenericUtils.createGridBagConstraints(2, 2, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0.2, 0);
		panTotal.add(getTxtTotal(), gc);
		
		gc = GenericUtils.createGridBagConstraints(1, 3, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0);
		panTotal.add(new JLabel("Total (Dolares): "), gc);
		gc = GenericUtils.createGridBagConstraints(2, 3, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0.2, 0);
		panTotal.add(getTxtTotalConFactor(), gc);
		
		return panTotal;
	}

	private CLJTextField getTxtImpVarios() {
		if (txtImpVarios == null) {
			txtImpVarios = new CLJTextField();
			txtImpVarios.addFocusListener(new FocusAdapter() {

				@Override
				public void focusLost(FocusEvent e) {
					handleIngresoImpVarios();
				}

			});
			
			txtImpVarios.addKeyListener(new KeyAdapter() {

				@Override
				public void keyPressed(KeyEvent e) {
					if(e.getKeyCode() == KeyEvent.VK_ENTER) {
						handleIngresoImpVarios();
					}
				}

			});

			
		}
		return txtImpVarios;
	}

	private CLJTextField getTxtPercepIVA() {
		if (txtPercepIVA == null) {
			txtPercepIVA = new CLJTextField();
			txtPercepIVA.addFocusListener(new FocusAdapter() {

				@Override
				public void focusLost(FocusEvent e) {
					handleIngresoPercepIVA();
				}

			});
			
			txtPercepIVA.addKeyListener(new KeyAdapter() {

				@Override
				public void keyPressed(KeyEvent e) {
					if(e.getKeyCode() == KeyEvent.VK_ENTER) {
						handleIngresoPercepIVA();
					}
				}

			});
			
		}
		return txtPercepIVA;
	}

	protected void handleIngresoPercepIVA() {
		String percepIVAStr = getTxtPercepIVA().getText();
		if(!StringUtil.isNullOrEmpty(percepIVAStr)) {
			if(GenericUtils.esNumerico(percepIVAStr)) {
				updateTotal(getPanTablaItemFacturaCorreccion().getElementos());
			} else {
				GuiUtil.showTooltipText(getTxtPercepIVA(), "El texto debe ser numérico.");
			}
		}
		updateTotal(getPanTablaItemFacturaCorreccion().getElementos());
	}

	
	protected void handleIngresoImpVarios() {
		String impVariosStr = getTxtImpVarios().getText();
		if(!StringUtil.isNullOrEmpty(impVariosStr)) {
			if(GenericUtils.esNumerico(impVariosStr)) {
				updateTotal(getPanTablaItemFacturaCorreccion().getElementos());
			} else {
				GuiUtil.showTooltipText(getTxtImpVarios(), "El texto debe ser numérico.");
			}
		}
		updateTotal(getPanTablaItemFacturaCorreccion().getElementos());
	}

	private JTextField getTxtTotal() {
		if(txtTotal == null) {
			txtTotal = new JTextField();
			txtTotal.setEditable(false);
		}
		return txtTotal;
	}

	private JTextField getTxtTotalConFactor() {
		if(txtTotalConFactor == null) {
			txtTotalConFactor = new JTextField();
			txtTotalConFactor.setEditable(false);
		}
		return txtTotalConFactor;
	}

	private JPanel getPanelSuperior() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.add(getPanelHeader(), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
		panel.add(getPanelDatosProveedor(), GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 1, 1, 0, 1));
		return panel;
	}

	private JPanel getPanelDatosProveedor() {
		JPanel panelDatosProveedor = new JPanel();
		panelDatosProveedor.setLayout(new GridBagLayout());
		panelDatosProveedor.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		panelDatosProveedor.add(new JLabel("Proveedor: "), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
		panelDatosProveedor.add(getTxtRazonSocial(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
		panelDatosProveedor.add(getPanSelFacturasProveedor(), GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 2, 1, 0, 0));
		return panelDatosProveedor;
	}

	private PanelSeleccionarElementos<FacturaProvedorWrapper> getPanSelFacturasProveedor() {
		if(panSelFacturasProveedor == null) {
			List<FacturaProveedor> facturasParaNotasCredito = GTLBeanFactory.getInstance().getBean2(FacturaProveedorFacadeRemote.class).getFacturasParaNotasCredito(proveedor.getId());
			panSelFacturasProveedor = new PanelSeleccionarElementos<FacturaProvedorWrapper>(padre, FacturaProvedorWrapper.toFacturaProveedorWrapperList(facturasParaNotasCredito), "Facturas");
			
			panSelFacturasProveedor.addPanelSeleccionarElementosListener(new PanelSeleccionarElementoEventListener<FacturaProvedorWrapper>() {

				public void elementsSelected(PanelSeleccionarElementoEvent<FacturaProvedorWrapper> evt) {
					correccionFactura.getFacturas().clear();
					List<FacturaProvedorWrapper> elements = evt.getElements();
					for(FacturaProvedorWrapper fpw : elements) {
						FacturaProveedor fp = fpw.getFacturaProveedor();
						for(ItemFacturaProveedor item : fp.getItems()) {
							ItemCorreccionMateriaPrima icmp = new ItemCorreccionMateriaPrima();
							icmp.setCantidad(item.getCantidad());
							icmp.setDescripcion(item.getDescripcion());
							icmp.setFactorConversionMoneda(item.getFactorConversionMoneda());
							icmp.setImporte(item.getImporte());
							icmp.setPorcDescuento(item.getPorcDescuento());
							icmp.setPrecioMateriaPrima(((ItemFacturaMateriaPrima)item).getPrecioMateriaPrima());
							icmp.setPrecioUnitario(item.getPrecioUnitario());
							icmp.getImpuestos().addAll(item.getImpuestos());
							getPanTablaItemFacturaCorreccion().agregarElemento(icmp);
						}
						correccionFactura.getFacturas().add(fp);
					}
					updateTotal(getPanTablaItemFacturaCorreccion().getElementos());
				}

			});

		}
		return panSelFacturasProveedor;
	}

	private JTextField getTxtRazonSocial() {
		if(txtRazonSocial == null) {
			txtRazonSocial = new JTextField();
			txtRazonSocial.setEditable(false);
		}
		return txtRazonSocial;
	}

	private JPanel getPanelHeader() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.add(new JLabel("Nº: "), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
		panel.add(getTxtNroCorreccion(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0.3, 0));
		panel.add(getLblTipoDocumento(), GenericUtils.createGridBagConstraints(2, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0.8, 0));
		panel.add(new JLabel("Fecha: "), GenericUtils.createGridBagConstraints(3, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 1, 1, 0, 0));
		panel.add(getPanelFecha(), GenericUtils.createGridBagConstraints(4, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 1, 1, 0, 0));
		return panel;
	}

	private PanelDatePicker getPanelFecha() {
		if(panelFecha == null){
			panelFecha = new PanelDatePicker();
			panelFecha.setSelectedDate(DateUtil.getHoy());
		}
		return panelFecha;
	}

	private JLabel getLblTipoDocumento() {
		JLabel lblTipoDocumento = new JLabel(); 
		lblTipoDocumento.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		lblTipoDocumento.setHorizontalAlignment(JLabel.CENTER);
		lblTipoDocumento.setText(tipoCorreccionFactura.toUpperCase());
		lblTipoDocumento.setFont(new Font("Tahoma", Font.BOLD, 18));
		return lblTipoDocumento;
	}

	private JFormattedTextField getTxtNroCorreccion() {
		if(txtNroCorreccion == null) {
			try {
				txtNroCorreccion = new JFormattedTextField(new MaskFormatter("####-########"));
				txtNroCorreccion.setFocusLostBehavior(JFormattedTextField.PERSIST);
				txtNroCorreccion.addFocusListener(new FocusAdapter() {
					public void focusLost(FocusEvent e) {
						try {
							txtNroCorreccion.commitEdit();
						} catch (ParseException e1) {
							e1.printStackTrace();
						}
					}
				});
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return txtNroCorreccion;
	}

	private JPanel getPanelAcciones() {
		JPanel panBotones = new JPanel();
		panBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panBotones.add(getBtnGuardar());
		panBotones.add(getBtnSalir());
		return panBotones;
	}

	private JButton getBtnGuardar() {
		if(btnGuardar == null) {
			btnGuardar = new JButton("Guardar");
			btnGuardar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if(validar()) {
						capturarSetearDatos();
						getCorreccionFacade().guardarCorreccionYGenerarMovimiento(correccionFactura, GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName(), null);
						CLJOptionPane.showInformationMessage(JDialogCargarNotaDeCreditoProveedor.this, "La " + tipoCorreccionFactura + " ha sido grabada con éxito.", "Alta de " + tipoCorreccionFactura + " de Proveedor");
						dispose();
					}
				}

			});

		}
		return btnGuardar;
	}

	
	private CorreccionFacturaProveedorFacadeRemote getCorreccionFacade() {
		if(correccionFacade == null) {
			this.correccionFacade = GTLBeanFactory.getInstance().getBean2(CorreccionFacturaProveedorFacadeRemote.class);
		}
		return correccionFacade;
	}

	private void capturarSetearDatos() {
		correccionFactura.getItemsCorreccion().clear();
		correccionFactura.getItemsCorreccion().addAll(getPanTablaItemFacturaCorreccion().getElementos());
		correccionFactura.setFechaIngreso(new java.sql.Date(getPanelFecha().getDate().getTime()));
		correccionFactura.setNroCorreccion(getTxtNroCorreccion().getText().trim());
		correccionFactura.setProveedor(proveedor);
	}

	private boolean validar() {
		if(getPanelFecha().getDate() == null) {
			CLJOptionPane.showErrorMessage(JDialogCargarNotaDeCreditoProveedor.this, "Debe ingresar una fecha válida", getTitle());
			return false;
		}
		if(StringUtil.isNullOrEmpty(getTxtNroCorreccion().getText())) {
			CLJOptionPane.showErrorMessage(JDialogCargarNotaDeCreditoProveedor.this, "Debe ingresar el número de " + tipoCorreccionFactura + ".", getTitle());
			getTxtNroCorreccion().requestFocus();
			return false;
		}
		String regExpNroCorreccion = "[0-9]{4}-[0-9]{8}";
		Pattern p = Pattern.compile(regExpNroCorreccion);
		Matcher matcher = p.matcher(getTxtNroCorreccion().getText().trim());
		if(!matcher.matches()) {
			CLJOptionPane.showErrorMessage(JDialogCargarNotaDeCreditoProveedor.this, "Debe ingresar un número de " + tipoCorreccionFactura + " válido.", getTitle());
			getTxtNroCorreccion().requestFocus();
			return false;
		}
		String txtValidacion = getPanTablaItemFacturaCorreccion().validarElementos();
		if(!StringUtil.isNullOrEmpty(txtValidacion)) {
			CLJOptionPane.showErrorMessage(JDialogCargarNotaDeCreditoProveedor.this, StringW.wordWrap(txtValidacion), getTitle());
			return false;
		}
		txtValidacion = getPanTablaItemFacturaCorreccion().validarFactorConversion();
		if(!StringUtil.isNullOrEmpty(txtValidacion)) {
			CLJOptionPane.showErrorMessage(JDialogCargarNotaDeCreditoProveedor.this, StringW.wordWrap(txtValidacion), getTitle());
			return false;
		}
		boolean existeNroCorreccion = getCorreccionFacade().existeNroCorreccionByProveedor(correccionFactura.getId(), getTxtNroCorreccion().getText().trim(), proveedor.getId());
		if(existeNroCorreccion) {
			getTxtNroCorreccion().requestFocus();
			CLJOptionPane.showErrorMessage(JDialogCargarNotaDeCreditoProveedor.this, StringW.wordWrap("El número de "  + tipoCorreccionFactura + " ya existe para el mismo proveedor."), getTitle());
			return false;
		}
		return true;
	}

	private JButton getBtnSalir() {
		if(btnSalir == null) {
			btnSalir = new JButton("Salir");
			btnSalir.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					dispose();
				}

			});
		}
		return btnSalir;
	}

	private PanTablaItemFacturaCorreccion getPanTablaItemFacturaCorreccion() {
		if(panTablaItemFacturaCorreccion == null) {
			panTablaItemFacturaCorreccion = new PanTablaItemFacturaCorreccion();
			panTablaItemFacturaCorreccion.addItemFacturaProveedorListener(new ItemCorreccionFacturaProveedorEventListener() {

				public void changeItemFactura(ItemCorreccionFacturaProveedorEvent evt) {
					updateTotal(evt.getItemList());
				}

			});

			panTablaItemFacturaCorreccion.setModoConsulta(modoConsulta);
		}
		return panTablaItemFacturaCorreccion;
	}

	private void updateTotal(List<ItemCorreccionFacturaProveedor> itemList) {
		Float factorMoneda = getFactorMoneda(itemList);
		double totalImpuestos = updateTablaImpuestos(itemList, factorMoneda);
		double totalImpVarios = getValueInTextField(getTxtImpVarios());
		double totalPercepIVA = getValueInTextField(getTxtPercepIVA());
		BigDecimal total = new BigDecimal(0);
		for(ItemCorreccionFacturaProveedor ifp : itemList) {
			total = total.add(ifp.recalcularImporteTotal());
		}
		total = total.add(new BigDecimal(totalImpuestos));
		total = total.add(new BigDecimal(totalImpVarios));
		total = total.add(new BigDecimal(totalPercepIVA));
		getTxtTotal().setText(GenericUtils.getDecimalFormat().format(total.doubleValue()));
		if(factorMoneda == null || factorMoneda == 0f) {
			getTxtTotalConFactor().setText("");
		} else {
			getTxtTotalConFactor().setText(GenericUtils.getDecimalFormat().format(total.doubleValue()/factorMoneda));
		}
		if(!modoConsulta) {
			correccionFactura.setImpVarios(new BigDecimal(totalImpVarios));
			correccionFactura.setPercepIVA(new BigDecimal(totalPercepIVA));
			correccionFactura.setMontoTotal(total);
		}
	}

	private double getValueInTextField(JTextField txt) {
		String strValue = txt.getText().trim();
		if (StringUtil.isNullOrEmpty(strValue) || !GenericUtils.esNumerico(strValue)) {
			return 0D;
		} else {
			return new Double(strValue.replace(',', '.')).doubleValue();
		}
	}
	
	private Float getFactorMoneda(List<ItemCorreccionFacturaProveedor> itemList) {
		Set<Float> factorSet  = new HashSet<Float>();
		boolean estaElUno = false;
		for(ItemCorreccionFacturaProveedor ifp : itemList) {
			if(ifp.getFactorConversionMoneda().floatValue() != 1f) {
				factorSet.add(ifp.getFactorConversionMoneda().floatValue());
			} else {
				estaElUno = true;
			}
		}
		if(factorSet.isEmpty() && estaElUno) {
			return 1f;
		}
		if(factorSet.isEmpty() || factorSet.size() > 1) {
			return null;
		}
		return factorSet.iterator().next();
	}
	
	private CLJTable getTablaImpuestos() {
		if(tablaImpuestos == null) {
			tablaImpuestos = new CLJTable(0, 3);
			tablaImpuestos.setStringColumn(COL_NOMBRE_IMPUESTO, "IMPUESTO", 200, 200, true);
			tablaImpuestos.setStringColumn(COL_VALOR_IMPUESTO, "IMPORTE PESOS", 100);
			tablaImpuestos.setStringColumn(COL_VALOR_IMPUESTO_CON_FACTOR, "IMPORTE DOLARES", 100);
		}
		return tablaImpuestos;
	}

	private double updateTablaImpuestos(List<ItemCorreccionFacturaProveedor> itemFacturaList, Float factorMoneda) {
		Double total = 0D;
		//armo un map con los impuestos y los totales por impuesto
		Map<ImpuestoItemProveedor, Double> mapImpuestos = DocumentoProveedorHelper.getInstance().calcularMapImpuestosCorreccFact(itemFacturaList);
		//vuelco el map en la tabla
		getTablaImpuestos().removeAllRows();
		int row = 0;
		for(Entry<ImpuestoItemProveedor, Double> e : mapImpuestos.entrySet()) {
			getTablaImpuestos().addRow();
			getTablaImpuestos().setValueAt(e.getKey(), row, COL_NOMBRE_IMPUESTO);
			getTablaImpuestos().setValueAt(GenericUtils.getDecimalFormat().format(e.getValue()), row, COL_VALOR_IMPUESTO);
			if(factorMoneda == null || factorMoneda == 0f) {
				getTablaImpuestos().setValueAt(null, row, COL_VALOR_IMPUESTO_CON_FACTOR);
			} else {
				getTablaImpuestos().setValueAt(GenericUtils.getDecimalFormat().format(e.getValue()/factorMoneda), row, COL_VALOR_IMPUESTO_CON_FACTOR);
			}
			row ++;
			total += e.getValue();
		}
		//fila del total
		getTablaImpuestos().addRow();
		getTablaImpuestos().setValueAt("TOTAL", row, COL_NOMBRE_IMPUESTO);
		getTablaImpuestos().setValueAt(GenericUtils.getDecimalFormat().format(total), row, COL_VALOR_IMPUESTO);
		if(factorMoneda == null || factorMoneda == 0f) {
			getTablaImpuestos().setValueAt(null, row, COL_VALOR_IMPUESTO_CON_FACTOR);
		} else {
			getTablaImpuestos().setValueAt(GenericUtils.getDecimalFormat().format(total/factorMoneda), row, COL_VALOR_IMPUESTO_CON_FACTOR);
		}
		getTablaImpuestos().setBackgroundRow(row, Color.YELLOW);
		
		return total;
	}

	
	private class PanTablaItemFacturaCorreccion extends PanelTabla<ItemCorreccionFacturaProveedor> {

		private static final long serialVersionUID = -1106975628853468764L;

		private static final int CANT_COLS = 9;
		private static final int COL_CANTIDAD = 0;
		private static final int COL_UNIDAD = 1;
		private static final int COL_DESCRIPCION = 2;
		private static final int COL_DESCUENTO = 3;
		private static final int COL_PRECIO_UNITARIO = 4;
		private static final int COL_FACTOR_MONEDA = 5;
		private static final int COL_IMPORTE = 6;
		private static final int COL_IMPUESTO = 7;
		private static final int COL_OBJ = 8;

		private JButton btnSelImpuestos;

		public PanTablaItemFacturaCorreccion() {
			agregarBoton(getBtnSelImpuestos());
		}

		public void setModoConsulta(boolean modoConsulta) {
			super.setModoConsulta(modoConsulta);
			getBtnSelImpuestos().setEnabled(false);
		}

		public void addItemFacturaProveedorListener(ItemCorreccionFacturaProveedorEventListener l) {
			listenerList.add(ItemCorreccionFacturaProveedorEventListener.class, l);
		}

		protected final void fireChangeItemCorreccionEvent(List<ItemCorreccionFacturaProveedor> itemList) {
			final ItemCorreccionFacturaProveedorEvent e = new ItemCorreccionFacturaProveedorEvent(this, itemList);
			final ItemCorreccionFacturaProveedorEventListener listeners[] = listenerList.getListeners(ItemCorreccionFacturaProveedorEventListener.class);
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					for (int i = 0; i < listeners.length; i++) {
						listeners[i].changeItemFactura(e);
					}
				}
			});
		}

		protected void agregarElemento(ItemCorreccionFacturaProveedor elemento) {
			Object[] row = new Object[CANT_COLS];
			row[COL_CANTIDAD] = elemento.getCantidad();
			if(elemento instanceof ItemCorreccionMateriaPrima) {
				PrecioMateriaPrima precioMateriaPrima = ((ItemCorreccionMateriaPrima)elemento).getPrecioMateriaPrima();
				row[COL_UNIDAD] = precioMateriaPrima.getMateriaPrima().getUnidad().toString();
				//Bloqueo la columna si el precio materia prima se vende en PESOS.
				if(!modoConsulta && precioMateriaPrima.getTipoMoneda() == ETipoMoneda.PESOS) {
					getTabla().lockCell(getTabla().getRowCount() - 1, COL_FACTOR_MONEDA);
				}
			}
			row[COL_DESCRIPCION] = elemento.getDescripcion();
			row[COL_FACTOR_MONEDA] = elemento.getFactorConversionMoneda().floatValue();
			row[COL_PRECIO_UNITARIO] = elemento.getPrecioUnitario().floatValue();
			row[COL_DESCUENTO] = elemento.getPorcDescuento().floatValue();
			row[COL_IMPORTE] = GenericUtils.getDecimalFormat().format(elemento.getImporte().floatValue());
			row[COL_IMPUESTO] = StringUtil.getCadena(elemento.getImpuestos(), ", ");
			row[COL_OBJ] = elemento;
			getTabla().addRow(row);
			
		}

		protected void botonQuitarPresionado() {
			fireChangeItemCorreccionEvent(getElementos());
		}

		public boolean validarAgregar() {
			ItemCorreccionResumen itemCorreccionResumen = new ItemCorreccionResumen();
			agregarElemento(itemCorreccionResumen);
			updateTotal(getElementos());
			return false;
		}

		public String validarFactorConversion() {
			Set<Float> factorsSet = new HashSet<Float>();
			for(ItemCorreccionFacturaProveedor ifp : getElementos()) {
				if(ifp.getFactorConversionMoneda().floatValue() != 1f) {
					factorsSet.add(ifp.getFactorConversionMoneda().floatValue());
				}
			}
			if(factorsSet.size() > 1) {
				return "No pueden haber dos factores de conversión distintos";
			}
			return null;
		}

		
		protected CLJTable construirTabla() {
			@SuppressWarnings("serial")
			CLJTable tabla = new CLJTable(0, CANT_COLS) {

				@Override
				public void newRowSelected(int newRow, int oldRow) {
					getBtnSelImpuestos().setEnabled(!modoConsulta && newRow != -1);
				}

				public void cellEdited(int cell, int row) {
					if(!modoConsulta) {
						if(cell == COL_CANTIDAD) {
							ItemCorreccionFacturaProveedor elemento = getElemento(row);
							Float cantidad = (Float)getTabla().getTypedValueAt(row, cell);
							if(cantidad == null) {
								cantidad = 0f;
							}
							elemento.setCantidad(new BigDecimal(cantidad));
							getTabla().setValueAt(GenericUtils.getDecimalFormat().format(elemento.recalcularImporteTotal().floatValue()), row, COL_IMPORTE);
							fireChangeItemCorreccionEvent(getElementos());							
						}
						if(cell == COL_DESCUENTO) {
							ItemCorreccionFacturaProveedor elemento = getElemento(row);
							Float descuento = (Float)getTabla().getTypedValueAt(row, cell);
							if(descuento == null) {
								descuento = 0f;
							}
							elemento.setPorcDescuento(new BigDecimal(descuento));
							getTabla().setValueAt(GenericUtils.getDecimalFormat().format(elemento.recalcularImporteTotal().floatValue()), row, COL_IMPORTE);
							fireChangeItemCorreccionEvent(getElementos());							
						}
						if(cell == COL_PRECIO_UNITARIO) {
							ItemCorreccionFacturaProveedor elemento = getElemento(row);
							Float precioUnitario = (Float)getTabla().getTypedValueAt(row, cell);
							if(precioUnitario == null) {
								precioUnitario = 0f;
							}
							elemento.setPrecioUnitario(new BigDecimal(precioUnitario));
							getTabla().setValueAt(GenericUtils.getDecimalFormat().format(elemento.recalcularImporteTotal().floatValue()), row, COL_IMPORTE);
							fireChangeItemCorreccionEvent(getElementos());				
						}
						if(cell == COL_FACTOR_MONEDA) {
							ItemCorreccionFacturaProveedor elemento = getElemento(row);
							Float factorMoneda = (Float)getTabla().getTypedValueAt(row, cell);
							if(factorMoneda == null) {
								factorMoneda = 0f;
							}
							elemento.setFactorConversionMoneda(new BigDecimal(factorMoneda));
							getTabla().setValueAt(GenericUtils.getDecimalFormat().format(elemento.recalcularImporteTotal().floatValue()), row, COL_IMPORTE);
							fireChangeItemCorreccionEvent(getElementos());				
						}
						if(cell == COL_DESCRIPCION) {
							ItemCorreccionFacturaProveedor elemento = getElemento(row);
							String desc = (String)getTabla().getValueAt(row, cell);
							if(desc != null) {
								desc = desc.trim().toUpperCase();
							}
							elemento.setDescripcion(desc);
						}
					}
				}

			};

			tabla.addMouseListener(new MouseAdapter () {

				public void mouseClicked(MouseEvent e) {
					if(e.getClickCount() == 2 && !modoConsulta) {
						handleSeleccionImpuestos();
					}
				}

			});

			tabla.setFloatColumn(COL_CANTIDAD, "Cantidad", 0F, 1000F, 50, false);
			tabla.setStringColumn(COL_UNIDAD, "Unidad", 150, 50, true);
			tabla.setStringColumn(COL_DESCRIPCION, "Descripcion", 280, 280, false);
			tabla.setFloatColumn(COL_DESCUENTO, "Descuento (%)", 0F, 1000F, 80, false);
			tabla.setFloatColumn(COL_PRECIO_UNITARIO, "Precio Unitario", 0F, Float.MAX_VALUE, 80, false);
			tabla.setFloatColumn(COL_FACTOR_MONEDA, "Factor ($)", 0F, Float.MAX_VALUE, 60, false);
			tabla.setStringColumn(COL_IMPORTE, "Importe Total", 70, 70, true);
			tabla.setStringColumn(COL_IMPUESTO, "Impuestos", 200, 200, true);
			tabla.setStringColumn(COL_OBJ, "", 0, 0, true);

			tabla.setReorderingAllowed(false);
			
			return tabla;
		}

		protected ItemCorreccionFacturaProveedor getElemento(int fila) {
			return (ItemCorreccionFacturaProveedor)getTabla().getValueAt(fila, COL_OBJ);
		}

		protected String validarElemento(int fila) {
			ItemCorreccionFacturaProveedor elemento = getElemento(fila);
			if(StringUtil.isNullOrEmpty(elemento.getDescripcion())) {
				return "Debe completar la descripción del item de la fila " + (fila + 1);
			}
			if(elemento.getImpuestos().isEmpty()) {
				return "Todos los items deben tener impuestos";
			}
			if(elemento.getFactorConversionMoneda() == null || elemento.getFactorConversionMoneda().doubleValue() <= 0) {
				return "El factor de conversión debe ser mayor a cero";
			}
			return null;
		}

		private JButton getBtnSelImpuestos() {
			if(btnSelImpuestos == null) {
				btnSelImpuestos = new JButton("I");
				btnSelImpuestos.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e) {
						handleSeleccionImpuestos();
					}

				});

			}
			return btnSelImpuestos;
		}

		private void handleSeleccionImpuestos() {
			int[] selectedRows = getTabla().getSelectedRows();
			if(selectedRows.length != 0) {
				List<ImpuestoItemProveedor> impuestosSelected = Collections.emptyList();
				if(selectedRows.length == 1) {
					ItemCorreccionFacturaProveedor elemento = getElemento(selectedRows[0]);
					impuestosSelected = elemento.getImpuestos(); 
				}
				JDialogSeleccionarCrearImpuesto dialogSeleccionarCrearImpuesto = new JDialogSeleccionarCrearImpuesto(padre, proveedor, impuestosSelected);
				dialogSeleccionarCrearImpuesto.setVisible(true);
				if(dialogSeleccionarCrearImpuesto.isAcepto()) {
					List<ImpuestoItemProveedor> impuestosSelectedResult = dialogSeleccionarCrearImpuesto.getImpuestosSelectedResult();
					if(impuestosPorPciaOK(impuestosSelectedResult)) {
						for(int selectedRow : selectedRows) {
							ItemCorreccionFacturaProveedor elemento = getElemento(selectedRow);
							elemento.getImpuestos().clear();
							elemento.getImpuestos().addAll(impuestosSelectedResult);
							getTabla().setValueAt(GenericUtils.getDecimalFormat().format(elemento.recalcularImporteTotal().floatValue()), selectedRow, COL_IMPORTE);
							getTabla().setValueAt(StringUtil.getCadena(elemento.getImpuestos(), ", "), selectedRow, COL_IMPUESTO);
						}
						fireChangeItemCorreccionEvent(getElementos());
					}
				}
			}
		}

		private boolean impuestosPorPciaOK(List<ImpuestoItemProveedor> impuestosSelectedResult) {
			for(ImpuestoItemProveedor iip : impuestosSelectedResult) {
				if(iip.getTipoImpuesto() == ETipoImpuesto.INGRESOS_BRUTOS && !iip.getProvincia().getId().equals(proveedor.getProvincia().getId())) {
					CLJOptionPane.showErrorMessage(JDialogCargarNotaDeCreditoProveedor.this, StringW.wordWrap("El impuesto " + iip.toString() + " no puede elegirse porque no aplica en la provincia de origen del proveedor."), "Atención");
					return false;
				}
			}
			return true;
		}

	
	}

	private static class FacturaProvedorWrapper {

		private FacturaProveedor facturaProveedor;

		public FacturaProvedorWrapper(FacturaProveedor facturaProveedor) {
			this.facturaProveedor = facturaProveedor;  
		}

		public FacturaProveedor getFacturaProveedor() {
			return facturaProveedor;
		}

		public static List<FacturaProvedorWrapper> toFacturaProveedorWrapperList(List<FacturaProveedor> fpList) {
			List<FacturaProvedorWrapper> resultList = new ArrayList<JDialogCargarNotaDeCreditoProveedor.FacturaProvedorWrapper>();
			for(FacturaProveedor fp : fpList) {
				resultList.add(new FacturaProvedorWrapper(fp));
			}
			return resultList;
		}

		public String toString() {
			FacturaProveedor fp = getFacturaProveedor();
			return "Fecha: " + DateUtil.dateToString(fp.getFechaIngreso(), DateUtil.SHORT_DATE) + " - Nro: " + fp.getNroFactura();
		}

	}

}