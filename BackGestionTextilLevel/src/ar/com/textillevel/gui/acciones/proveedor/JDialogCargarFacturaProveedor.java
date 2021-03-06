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
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
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
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;
import javax.swing.text.MaskFormatter;

import main.GTLGlobalCache;

import org.apache.taglibs.string.util.StringW;

import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTable;
import ar.com.fwcommon.componentes.FWJTextField;
import ar.com.fwcommon.componentes.PanelTabla;
import ar.com.fwcommon.util.DateUtil;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.fwcommon.util.StringUtil;
import ar.com.textillevel.entidades.documentos.factura.proveedor.FacturaProveedor;
import ar.com.textillevel.entidades.documentos.factura.proveedor.ImpuestoItemProveedor;
import ar.com.textillevel.entidades.documentos.factura.proveedor.ItemFacturaMateriaPrima;
import ar.com.textillevel.entidades.documentos.factura.proveedor.ItemFacturaProveedor;
import ar.com.textillevel.entidades.documentos.remito.proveedor.PiezaRemitoEntradaProveedor;
import ar.com.textillevel.entidades.documentos.remito.proveedor.RemitoEntradaProveedor;
import ar.com.textillevel.entidades.enums.ETipoImpuesto;
import ar.com.textillevel.entidades.enums.ETipoMoneda;
import ar.com.textillevel.entidades.gente.Proveedor;
import ar.com.textillevel.entidades.ventas.materiaprima.ListaDePreciosProveedor;
import ar.com.textillevel.entidades.ventas.materiaprima.PrecioMateriaPrima;
import ar.com.textillevel.facade.api.remote.FacturaProveedorFacadeRemote;
import ar.com.textillevel.facade.api.remote.ListaDePreciosProveedorFacadeRemote;
import ar.com.textillevel.facade.api.remote.RemitoEntradaProveedorFacadeRemote;
import ar.com.textillevel.gui.acciones.JDialogAgregarRemitoEntradaProveedor;
import ar.com.textillevel.gui.acciones.JDialogAltaNuevaMateriaPrimaYPrecioMateriaPrima;
import ar.com.textillevel.gui.acciones.JDialogSeleccionarPrecioMateriaPrima;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.controles.LinkableLabel;
import ar.com.textillevel.gui.util.controles.PanelDatePicker;
import ar.com.textillevel.gui.util.dialogs.JDialogSeleccionarRemitoEntradaProveedor;
import ar.com.textillevel.util.DocumentoProveedorHelper;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogCargarFacturaProveedor extends JDialog {

	private static final long serialVersionUID = 3133488183144001174L;

	private JButton btnGuardar;
	private JButton btnSalir;
	private JFormattedTextField txtNroFactura;
	private PanelDatePicker panelFecha;
	private JTextField txtRazonSocial;
	private PanTablaItemFactura panTablaItemFactura;

	private FWJTextField txtSubtotal;
	private JTextField txtSubtotalConFactor;
	private FWJTextField txtTotal;
	private JTextField txtTotalConFactor;
	private FWJTextField txtDescuento;
	private FWJTable tablaImpuestos;
	private Frame padre;
	private boolean modoConsulta;
	
	private RemitoLinkeableLabel[] remitoLinkeableLabelList;
	
	private FacturaProveedor factura;
	private List<RemitoEntradaProveedor> remitoEntradaList;
	private Map<Integer, PrecioMateriaPrima> mapPrecioMatPrima;
	private FacturaProveedorFacadeRemote facturaProveedorFacade;

	private JButton btnSeleccionarRemitos;

	private JTextField txtUsuarioCreador;

	private FWJTextField txtImpVarios;
	private FWJTextField txtPercepIVA;

	private static final int COL_NOMBRE_IMPUESTO = 0;
	private static final int COL_VALOR_IMPUESTO = 1;
	private static final int COL_VALOR_IMPUESTO_CON_FACTOR = 2;

	private static final int CANT_MAX_REMITOS_SELECTED_HARDCODE = 15;

	public JDialogCargarFacturaProveedor(Frame padre, FacturaProveedor factura, boolean modoConsulta, List<RemitoEntradaProveedor> remitoEntradaList) {
		super(padre);
		this.remitoEntradaList = remitoEntradaList;
		this.padre = padre;
		this.factura = factura;
		this.modoConsulta = modoConsulta;
		this.remitoLinkeableLabelList = new RemitoLinkeableLabel[CANT_MAX_REMITOS_SELECTED_HARDCODE];
		armarMapListaDePrecios(factura.getProveedor().getId());
		setModal(true);
		construct();
		setDatos();
	}

	private void setDatos() {
		Proveedor proveedor = factura.getProveedor();
		getTxtRazonSocial().setText(proveedor.getRazonSocial());
		getTxtUsuarioCreador().setText(StringUtil.isNullOrEmpty(factura.getUsuarioCreador()) ? "" : factura.getUsuarioCreador());
		if(factura.getFechaIngreso() != null) {
			getPanelFecha().setSelectedDate(factura.getFechaIngreso());
		}
		getTxtDescuento().setText(formatBigDecimal(factura.getDescuento()));
		getTxtSubtotal().setText(formatBigDecimal(factura.getMontoSubtotal()));
		getTxtTotal().setText(formatBigDecimal(factura.getMontoTotal()));
		getTxtImpVarios().setText(formatBigDecimal(factura.getImpVarios()));
		getTxtPercepIVA().setText(formatBigDecimal(factura.getPercepIVA()));
		getTxtNroFactura().setText(factura.getNroFactura());
		getPanTablaItemFactura().agregarElementos(factura.getItems());
		if(modoConsulta) {
			getBtnGuardar().setEnabled(false);
			getTxtNroFactura().setEditable(false);
			getTxtDescuento().setEditable(false);
			getTxtImpVarios().setEditable(false);
			getTxtPercepIVA().setEditable(false);
			getTxtTotal().setEditable(false);
			getTxtSubtotal().setEditable(false);
			getPanelFecha().setEnabled(false);
			getBtnSeleccionarRemitos().setVisible(false);
		} else {
			if(factura.getId() == null) { //Solo si es un alta
				llenarTablaMatPrimas();
			}
		}
		List<ItemFacturaProveedor> itemFacturaList = getPanTablaItemFactura().getElementos();
		Float factorMoneda = DocumentoProveedorHelper.getInstance().getFactorMoneda(itemFacturaList);
		updateTablaImpuestos(itemFacturaList, factorMoneda);
		updateTotales(itemFacturaList);
		setRemitosEntradaLabel(factura.getRemitoList());
	}

	private String formatBigDecimal(BigDecimal value) {
		return GenericUtils.getDecimalFormat3().format(value.floatValue());
	}

	private void setRemitosEntradaLabel(List<RemitoEntradaProveedor> remitoList) {
		for(RemitoLinkeableLabel rll : remitoLinkeableLabelList) {
			rll.setVisible(false);
			rll.setRemito(null);
		}
		for(int i=0; i < Math.min(remitoList.size(), CANT_MAX_REMITOS_SELECTED_HARDCODE); i++) {
			RemitoLinkeableLabel remitoLinkeableLabel = remitoLinkeableLabelList[i];
			remitoLinkeableLabel.setRemito(remitoList.get(i));
			remitoLinkeableLabel.setVisible(true);
		}
	}

	private void armarMapListaDePrecios(Integer id) {
		mapPrecioMatPrima = new HashMap<Integer, PrecioMateriaPrima>();
		ListaDePreciosProveedor listaByIdProveedor = GTLBeanFactory.getInstance().getBean2(ListaDePreciosProveedorFacadeRemote.class).getListaByIdProveedor(id);
		if(listaByIdProveedor != null) {
			for(PrecioMateriaPrima pmp : listaByIdProveedor.getPrecios()) {
				mapPrecioMatPrima.put(pmp.getMateriaPrima().getId(), pmp);
			}
		}
	}

	private void llenarTablaMatPrimas() {
		getPanTablaItemFactura().getTabla().removeAllRows();
		List<ItemFacturaProveedor> ifProvList = new ArrayList<ItemFacturaProveedor>();
		for(RemitoEntradaProveedor rep : factura.getRemitoList()) {
			for(PiezaRemitoEntradaProveedor prep : rep.getPiezas()) {
				ItemFacturaMateriaPrima ifmp = new ItemFacturaMateriaPrima();
				ifmp.setPorcDescuento(new BigDecimal(0));
				ifmp.setFactorConversionMoneda(new BigDecimal(1));
				ifmp.setCantidad(prep.getCantidad());
				ifmp.setPrecioMateriaPrima(prep.getPrecioMateriaPrima());
				PrecioMateriaPrima precioMateriaPrima = prep.getPrecioMateriaPrima();
				ifmp.setPrecioUnitario(precioMateriaPrima == null ? null : precioMateriaPrima.getPrecio());
				ifmp.setDescripcion(precioMateriaPrima == null ? null : precioMateriaPrima.toString());
				if(ifmp.getPrecioUnitario() != null) {
					ifmp.setImporte(ifmp.getPrecioUnitario().multiply(ifmp.getCantidad()));
				}
				ifProvList.add(ifmp);
			}
		}
		getPanTablaItemFactura().agregarElementos(ifProvList);
	}

	private void construct() {
		setUpScreen();
		setUpComponentes();
	}

	private void setUpScreen() {
		setSize(new Dimension(980, 600));
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		if(modoConsulta) {
			setTitle("Consulta de Factura de Proveedor");
		} else if(factura.getId() == null) {
			setTitle("Alta de Factura de Proveedor");
		} else {
			setTitle("Modificaci�n de Factura de Proveedor");
		}
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
		panCenter.add(getPanTablaItemFactura(), gc);
		gc = GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 1, 1, 0, 0.3);
		panCenter.add(getPanelTotal(), gc);
		return panCenter;
	}

	private JPanel getPanelTotal() {
		JPanel panTotal = new JPanel();
		panTotal.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		panTotal.setLayout(new GridBagLayout());
		GridBagConstraints gc = GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0);
		panTotal.add(new JLabel("Subtotal (Pesos): "), gc);
		gc = GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0.15, 0);
		panTotal.add(getTxtSubtotal(), gc);

		gc = GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0, 0);
		panTotal.add(new JLabel("Subtotal (Dolares): "), gc);
		gc = GenericUtils.createGridBagConstraints(1, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0.1, 0);
		panTotal.add(getTxtSubtotalConFactor(), gc);

		gc = GenericUtils.createGridBagConstraints(0, 2, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0, 0);
		panTotal.add(new JLabel("Descuento (%): "), gc);
		gc = GenericUtils.createGridBagConstraints(1, 2, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0.1, 0);
		panTotal.add(getTxtDescuento(), gc);

		gc = GenericUtils.createGridBagConstraints(0, 3, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0, 0);
		panTotal.add(new JLabel("Impuestos Varios: "), gc);
		gc = GenericUtils.createGridBagConstraints(1, 3, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0.1, 0);
		panTotal.add(getTxtImpVarios(), gc);

		gc = GenericUtils.createGridBagConstraints(0, 4, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0, 0);
		panTotal.add(new JLabel("Perc. IVA: "), gc);
		gc = GenericUtils.createGridBagConstraints(1, 4, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0.1, 0);
		panTotal.add(getTxtPercepIVA(), gc);

		gc = GenericUtils.createGridBagConstraints(2, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 1, 5, 0.5, 1);
		JScrollPane sp = new JScrollPane(getTablaImpuestos());
		sp.setBorder(BorderFactory.createTitledBorder("Impuestos"));
		panTotal.add(sp, gc);

		gc = GenericUtils.createGridBagConstraints(3, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0);
		panTotal.add(new JLabel("Total (Pesos): "), gc);
		gc = GenericUtils.createGridBagConstraints(4, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0.1, 0);		
		panTotal.add(getTxtTotal(), gc);

		gc = GenericUtils.createGridBagConstraints(3, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0, 0);
		panTotal.add(new JLabel("Total (Dolares): "), gc);
		gc = GenericUtils.createGridBagConstraints(4, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0.1, 0);		
		panTotal.add(getTxtTotalConFactor(), gc);

		return panTotal;
	}

	private FWJTextField getTxtImpVarios() {
		if (txtImpVarios == null) {
			txtImpVarios = new FWJTextField();
			addKeyAndFocusLostAdapter(txtImpVarios);
		}
		return txtImpVarios;
	}

	private FWJTextField getTxtPercepIVA() {
		if (txtPercepIVA == null) {
			txtPercepIVA = new FWJTextField();
			addKeyAndFocusLostAdapter(txtPercepIVA);
		}
		return txtPercepIVA;
	}

	private void handleIngresoTextNumerico(FWJTextField txt) {
		String valor = txt.getText();
		if(!StringUtil.isNullOrEmpty(valor)) {
			if(!GenericUtils.esNumerico(valor)) {
				GuiUtil.showTooltipText(txt, "El texto debe ser num�rico.");
			}
		}
	}
	
	private void addKeyAndFocusLostAdapter(final FWJTextField txt) {
		txt.addFocusListener(new FocusAdapter() {

			@Override
			public void focusLost(FocusEvent e) {
				handleIngresoTextNumerico(txt);
			}

		});

		txt.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					handleIngresoTextNumerico(txt);
				}
			}

		});
	}
	
	private void handleIngresoTextNumericoAndUpdateTotales(FWJTextField txt) {
		String valor = txt.getText();
		if(!StringUtil.isNullOrEmpty(valor)) {
			if(!GenericUtils.esNumerico(valor)) {
				GuiUtil.showTooltipText(txt, "El texto debe ser num�rico.");
			} else {
				updateTotales(getPanTablaItemFactura().getElementos());
			}
		}
	}

	private void addKeyAndFocusLostAdapterWithUpdateTotales(final FWJTextField txt) {
		txt.addFocusListener(new FocusAdapter() {

			@Override
			public void focusLost(FocusEvent e) {
				handleIngresoTextNumericoAndUpdateTotales(txt);
			}

		});

		txt.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					handleIngresoTextNumericoAndUpdateTotales(txt);
				}
			}

		});
	}
	
	private FWJTextField getTxtTotal() {
		if(txtTotal == null) {
			txtTotal = new FWJTextField();
			addKeyAndFocusLostAdapterWithUpdateTotales(txtTotal);
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

	private FWJTextField getTxtSubtotal() {
		if(txtSubtotal == null) {
			txtSubtotal = new FWJTextField();
			addKeyAndFocusLostAdapterWithUpdateTotales(txtSubtotal);
		}
		return txtSubtotal;
	}

	private JTextField getTxtSubtotalConFactor() {
		if(txtSubtotalConFactor == null) {
			txtSubtotalConFactor = new JTextField();
			txtSubtotalConFactor.setEditable(false);
		}
		return txtSubtotalConFactor;
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

		panelDatosProveedor.add(getTxtRazonSocial(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), CANT_MAX_REMITOS_SELECTED_HARDCODE, 1, 1, 0));
		panelDatosProveedor.add(new JLabel("Remito(s): "), GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));

		for(int i = 0; i < CANT_MAX_REMITOS_SELECTED_HARDCODE; i++) {
			LinkableLabel lblConsultaRemito = new RemitoLinkeableLabel();
			remitoLinkeableLabelList[i] = (RemitoLinkeableLabel)lblConsultaRemito;
			panelDatosProveedor.add(lblConsultaRemito, GenericUtils.createGridBagConstraints(i+1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
		}
		
		panelDatosProveedor.add(getBtnSeleccionarRemitos(), GenericUtils.createGridBagConstraints(0, 2, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
		
		return panelDatosProveedor;
	}

	private JButton getBtnSeleccionarRemitos() {
		if(btnSeleccionarRemitos == null) {
			btnSeleccionarRemitos = new JButton("Seleccionar Remitos");
			btnSeleccionarRemitos.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					JDialogSeleccionarRemitoEntradaProveedor jdsrep = new JDialogSeleccionarRemitoEntradaProveedor(padre, factura.getProveedor(), remitoEntradaList);
					jdsrep.setSelectedRemitos(factura.getRemitoList());
					GuiUtil.centrar(jdsrep);
					jdsrep.setVisible(true);
					if(jdsrep.isAcepto()) {
						List<RemitoEntradaProveedor> remitoEntradaSelected = toEager(jdsrep.getRemitoEntradaList());
						factura.getRemitoList().clear();
						factura.getRemitoList().addAll(remitoEntradaSelected);
						setRemitosEntradaLabel(remitoEntradaSelected);
						llenarTablaMatPrimas();
//						updateTotales(getPanTablaItemFactura().getElementos());
					}
				}

			});

			btnSeleccionarRemitos.setVisible(!isFacturaSinRemito());
		}
		return btnSeleccionarRemitos;
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
		panel.add(new JLabel("N�: "), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
		panel.add(getTxtNroFactura(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0.3, 0));
		panel.add(getLblTipoDocumento(), GenericUtils.createGridBagConstraints(2, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0.5, 0));
		panel.add(getLblTipoFactura(), GenericUtils.createGridBagConstraints(3, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 20), 1, 1, 0.2, 0));
		panel.add(new JLabel("Fecha: "), GenericUtils.createGridBagConstraints(4, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 1, 1, 0, 0));
		panel.add(getPanelFecha(), GenericUtils.createGridBagConstraints(5, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 1, 1, 0, 0));
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
		lblTipoDocumento.setText("FACTURA");
		lblTipoDocumento.setFont(new Font("Tahoma", Font.BOLD, 18));
		return lblTipoDocumento;
	}

	private JLabel getLblTipoFactura() {
		JLabel lblTipoFactura = new JLabel();
		lblTipoFactura.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		lblTipoFactura.setHorizontalAlignment(JLabel.CENTER);
		lblTipoFactura.setText("A");
		lblTipoFactura.setFont(new Font("Tahoma", Font.BOLD, 20));
		return lblTipoFactura;
	}

	private JFormattedTextField getTxtNroFactura() {
		if(txtNroFactura == null) {
			try {
				txtNroFactura = new JFormattedTextField(new MaskFormatter("####-########"));
				txtNroFactura.setFocusLostBehavior(JFormattedTextField.PERSIST);
				txtNroFactura.addFocusListener(new FocusAdapter() {
					public void focusLost(FocusEvent e) {
						try {
							txtNroFactura.commitEdit();
						} catch (ParseException e1) {
							e1.printStackTrace();
						}
					}
				});
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return txtNroFactura;
	}

	private JPanel getPanelAcciones() {
		JPanel panBotones = new JPanel();
		panBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panBotones.add(getBtnGuardar());
		panBotones.add(getBtnSalir());
		panBotones.add(new JLabel("Usuario: "));
		panBotones.add(getTxtUsuarioCreador());
		return panBotones;
	}

	private JTextField getTxtUsuarioCreador() {
		if(txtUsuarioCreador == null) {
			txtUsuarioCreador = new JTextField();
			txtUsuarioCreador.setPreferredSize(new Dimension(120, 20));			
			txtUsuarioCreador.setEditable(false);
		}
		return txtUsuarioCreador;
	}

	private JButton getBtnGuardar() {
		if(btnGuardar == null) {
			btnGuardar = new JButton("Guardar");
			btnGuardar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if(validar()) {
						capturarSetearDatos();
						getFacturaProveedorFacade().ingresarFactura(factura, GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName());
						FWJOptionPane.showInformationMessage(JDialogCargarFacturaProveedor.this, "La factura ha sido grabada con �xito.", "Alta de Factura de Proveedor");
						dispose();
					}
				}

			});

		}
		return btnGuardar;
	}

	private void capturarSetearDatos() {
		factura.getItems().clear();
		factura.getItems().addAll(getPanTablaItemFactura().getElementos());
		java.sql.Date fechaSQL = new java.sql.Date(getPanelFecha().getDate().getTime());
		Date fechaEnGMT_3 = DateUtil.getDateInDefaultTimeZone(fechaSQL);
		java.sql.Date fechaSQLEnGMT_3 = new java.sql.Date(fechaEnGMT_3.getTime());
		factura.setFechaIngreso(fechaSQLEnGMT_3);
		factura.setNroFactura(getTxtNroFactura().getText().trim());
		
		//totales
		double subtotal = getValueInTextField(getTxtSubtotal());
		factura.setMontoSubtotal(new BigDecimal(subtotal));
		
		double descuento = getValueInTextField(getTxtDescuento());
		factura.setDescuento(new BigDecimal(descuento));
		
		double total = getValueInTextField(getTxtTotal());
		factura.setMontoTotal(new BigDecimal(total));
		factura.setMontoFaltantePorPagar(new BigDecimal(total));

		double totalImpVarios = getValueInTextField(getTxtImpVarios());
		factura.setImpVarios(new BigDecimal(totalImpVarios));

		double totalPercepIVA = getValueInTextField(getTxtPercepIVA());
		factura.setPercepIVA(new BigDecimal(totalPercepIVA));
	}

	private boolean validar() {
		if(getPanelFecha().getDate() == null) {
			FWJOptionPane.showErrorMessage(JDialogCargarFacturaProveedor.this, "Debe ingresar una fecha v�lida", getTitle());
			return false;
		}
		if(StringUtil.isNullOrEmpty(getTxtNroFactura().getText())) {
			FWJOptionPane.showErrorMessage(JDialogCargarFacturaProveedor.this, "Debe ingresar el n�mero de factura.", getTitle());
			getTxtNroFactura().requestFocus();
			return false;
		}
		String regExpNroFactura = "[0-9]{4}-[0-9]{8}";
		Pattern p = Pattern.compile(regExpNroFactura);
		Matcher matcher = p.matcher(getTxtNroFactura().getText().trim());
		if(!matcher.matches()) {
			FWJOptionPane.showErrorMessage(JDialogCargarFacturaProveedor.this, "Debe ingresar un n�mero de factura v�lido.", getTitle());
			getTxtNroFactura().requestFocus();
			return false;
		}
		String txtValidacion = getPanTablaItemFactura().validarElementos();
		if(!StringUtil.isNullOrEmpty(txtValidacion)) {
			FWJOptionPane.showErrorMessage(JDialogCargarFacturaProveedor.this, StringW.wordWrap(txtValidacion), getTitle());
			return false;
		}
		txtValidacion = getPanTablaItemFactura().validarFactorConversion();
		if(!StringUtil.isNullOrEmpty(txtValidacion)) {
			FWJOptionPane.showErrorMessage(JDialogCargarFacturaProveedor.this, StringW.wordWrap(txtValidacion), getTitle());
			return false;
		}
		
		RemitoEntradaProveedor rep = getRemitoWithMaxFecha(factura.getRemitoList());
		if(rep != null && DateUtil.getManiana(DateUtil.redondearFecha(getPanelFecha().getDate())).before(DateUtil.redondearFecha(rep.getFechaEmision()))) {
			FWJOptionPane.showErrorMessage(JDialogCargarFacturaProveedor.this, StringW.wordWrap("La fecha de la factura es menor a la del remito '" + rep.getNroRemito() + " - " + rep.getFechaEmision() + "."), getTitle());
			return false;
		}
		boolean existeNroFactura = getFacturaProveedorFacade().existeNroFacturaByProveedor(factura.getId(), getTxtNroFactura().getText().trim(), factura.getProveedor().getId());
		if(existeNroFactura) {
			getTxtNroFactura().requestFocus();
			FWJOptionPane.showErrorMessage(JDialogCargarFacturaProveedor.this, StringW.wordWrap("El n�mero de factura ya existe para el mismo proveedor."), getTitle());
			return false;
		}
		
		//valido completitud de los valores decimales
		if(!isValorDecimalValido(getTxtSubtotal())) {
			FWJOptionPane.showErrorMessage(JDialogCargarFacturaProveedor.this, StringW.wordWrap("El subtotal debe ser un n�mero v�lido"), getTitle());
			return false;
		}
		if(!isValorDecimalValido(getTxtDescuento())) {
			FWJOptionPane.showErrorMessage(JDialogCargarFacturaProveedor.this, StringW.wordWrap("Descuento debe ser un n�mero v�lido"), getTitle());
			return false;
		}
		if(!isValorDecimalValido(getTxtImpVarios())) {
			FWJOptionPane.showErrorMessage(JDialogCargarFacturaProveedor.this, StringW.wordWrap("Imp Varios debe ser un n�mero v�lido"), getTitle());
			return false;
		}
		if(!isValorDecimalValido(getTxtPercepIVA())) {
			FWJOptionPane.showErrorMessage(JDialogCargarFacturaProveedor.this, StringW.wordWrap("Percepci�n de Iva debe ser un n�mero v�lido"), getTitle());
			return false;
		}
		if(!isValorDecimalValido(getTxtTotal())) {
			FWJOptionPane.showErrorMessage(JDialogCargarFacturaProveedor.this, StringW.wordWrap("El total debe ser un n�mero v�lido"), getTitle());
			return false;
		}
		//valido correctitud de subtotal, impuestos, total, etc..
		List<ItemFacturaProveedor> itemFacturaList = getPanTablaItemFactura().getElementos();
		Float factorMoneda = DocumentoProveedorHelper.getInstance().getFactorMoneda(itemFacturaList);
		double subtotal = getValueInTextField(getTxtSubtotal());
		double descuento = (getValueInTextField(getTxtDescuento())/100)*subtotal; 

		double totalImpuestos = updateTablaImpuestos(itemFacturaList, factorMoneda);
		totalImpuestos = Math.floor(totalImpuestos * 1000) / 1000d;//redondeo a tres decimales

		double totalImpVarios = getValueInTextField(getTxtImpVarios());
		double totalPercepIVA = getValueInTextField(getTxtPercepIVA());
		double total = getValueInTextField(getTxtTotal());
		if(total != (subtotal - descuento + totalImpuestos + totalImpVarios + totalPercepIVA)) {
			if(FWJOptionPane.showQuestionMessage(JDialogCargarFacturaProveedor.this, StringW.wordWrap("El subtotal, impuestos, descuento sumados no dan lo mismo que el total �Desea Continuar?"), getTitle()) == FWJOptionPane.NO_OPTION) {
				return false;
			}
		}
		return true;
	}

	private RemitoEntradaProveedor getRemitoWithMaxFecha(List<RemitoEntradaProveedor> remitoList) {
		if(remitoList.isEmpty()) {
			return null;
		} else {
			List<RemitoEntradaProveedor> repList = new ArrayList<RemitoEntradaProveedor>(remitoList);
			Collections.sort(repList, new Comparator<RemitoEntradaProveedor>() {

				public int compare(RemitoEntradaProveedor o1, RemitoEntradaProveedor o2) {
					return o1.getFechaEmision().compareTo(o2.getFechaEmision());
				}

			});
			return repList.get(repList.size() - 1);
		}
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

	private PanTablaItemFactura getPanTablaItemFactura() {
		if(panTablaItemFactura == null) {
			List<PrecioMateriaPrima> allMateriaPrimaList = new ArrayList<PrecioMateriaPrima>(mapPrecioMatPrima.values());
			Collections.sort(allMateriaPrimaList, new Comparator<PrecioMateriaPrima>() {
				public int compare(PrecioMateriaPrima o1, PrecioMateriaPrima o2) {
					return o1.toString().compareTo(o2.toString());
				}
			});
			panTablaItemFactura = new PanTablaItemFactura(isFacturaSinRemito(), factura.getProveedor(), allMateriaPrimaList);
			panTablaItemFactura.addItemFacturaProveedorListener(new ItemFacturaProveedorEventListener() {

				public void changeItemFactura(ItemFacturaProveedorEvent evt) {
					List<ItemFacturaProveedor> itemFacturaList = getPanTablaItemFactura().getElementos();
					Float factorMoneda = DocumentoProveedorHelper.getInstance().getFactorMoneda(itemFacturaList);
					updateTablaImpuestos(itemFacturaList, factorMoneda);
				}

			});

			panTablaItemFactura.setModoConsulta(modoConsulta);
		}
		return panTablaItemFactura;
	}

	private void updateTotales(List<ItemFacturaProveedor> itemFacturaList) {
		Float factorMoneda = DocumentoProveedorHelper.getInstance().getFactorMoneda(itemFacturaList);
		double total = getValueInTextField(getTxtTotal());
		double subtotal = getValueInTextField(getTxtSubtotal());
		if(factorMoneda == null || factorMoneda == 0f || factorMoneda == 1f) {
			getTxtTotalConFactor().setText("");
			getTxtSubtotalConFactor().setText("");
		} else {
			getTxtSubtotalConFactor().setText(GenericUtils.getDecimalFormat3().format(subtotal/factorMoneda));
			getTxtTotalConFactor().setText(GenericUtils.getDecimalFormat3().format(total/factorMoneda));
		}
	}

	private double getValueInTextField(JTextField txt) {
		String strValue = txt.getText().trim();
		if (isValorDecimalValido(txt)) {
			strValue = strValue.replaceAll(",", "");
			return new Double(strValue.replace(',', '.')).doubleValue();
		} else {
			return 0D;
		}
	}

	private boolean isValorDecimalValido(JTextField txt) {
		String strValue = txt.getText().trim();
		return !StringUtil.isNullOrEmpty(strValue) && GenericUtils.esNumerico(strValue);
	}

	private double updateTablaImpuestos(List<ItemFacturaProveedor> itemFacturaList, Float factorMoneda) {
		Double total = 0D;
		BigDecimal porcDescGlobal = new BigDecimal(getValueInTextField(getTxtDescuento()));
		Map<ImpuestoItemProveedor, Double> mapImpuestos = DocumentoProveedorHelper.getInstance().calcularMapImpuestos(porcDescGlobal, itemFacturaList);
		//vuelco el map en la tabla
		getTablaImpuestos().removeAllRows();
		int row = 0;
		for(Entry<ImpuestoItemProveedor, Double> e : mapImpuestos.entrySet()) {
			getTablaImpuestos().addRow();
			getTablaImpuestos().setValueAt(e.getKey(), row, COL_NOMBRE_IMPUESTO);
			getTablaImpuestos().setValueAt(GenericUtils.getDecimalFormat3().format(e.getValue()), row, COL_VALOR_IMPUESTO);
			if(factorMoneda == null || factorMoneda == 0f || factorMoneda == 1f) {
				getTablaImpuestos().setValueAt(null, row, COL_VALOR_IMPUESTO_CON_FACTOR);
			} else {
				getTablaImpuestos().setValueAt(GenericUtils.getDecimalFormat3().format(e.getValue()/factorMoneda), row, COL_VALOR_IMPUESTO_CON_FACTOR);
			}
			row ++;
			total += e.getValue();
		}
		//fila del total
		getTablaImpuestos().addRow();
		getTablaImpuestos().setValueAt("TOTAL", row, COL_NOMBRE_IMPUESTO);
		getTablaImpuestos().setValueAt(GenericUtils.getDecimalFormat3().format(total), row, COL_VALOR_IMPUESTO);
		if(factorMoneda == null || factorMoneda == 0f || factorMoneda == 1f) {
			getTablaImpuestos().setValueAt(null, row, COL_VALOR_IMPUESTO_CON_FACTOR);
		} else {
			getTablaImpuestos().setValueAt(GenericUtils.getDecimalFormat3().format(total/factorMoneda), row, COL_VALOR_IMPUESTO_CON_FACTOR);
		}
		getTablaImpuestos().setBackgroundRow(row, Color.YELLOW);
		
		return total;
	}

	private boolean isFacturaSinRemito() {
		return factura.getRemitoList().isEmpty();
	}

	private class PanTablaItemFactura extends PanelTabla<ItemFacturaProveedor> {

		private static final long serialVersionUID = -7198713632884388418L;

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
		private JButton btnSelPMP;
		
		private boolean showActionsFacturaSinRemito;
		private Proveedor proveedor;
		private List<PrecioMateriaPrima> allMateriaPrimaList;

		public PanTablaItemFactura(boolean showActionsFacturaSinRemito, Proveedor proveedor, List<PrecioMateriaPrima> allMateriaPrimaList) {
			this.proveedor = proveedor;
			this.allMateriaPrimaList = allMateriaPrimaList;
			this.showActionsFacturaSinRemito = showActionsFacturaSinRemito;
			getBotonAgregar().setVisible(showActionsFacturaSinRemito);
			agregarBoton(getBtnSelImpuestos());
			if(showActionsFacturaSinRemito) {
				agregarBoton(getBtnSelPMP());
			}
		}

		private JButton getBtnSelPMP() {
			if(btnSelPMP == null) {
				btnSelPMP = new JButton("S");
				btnSelPMP.setToolTipText("Seleccionar materias primas existentes");
				btnSelPMP.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e) {
						JDialogSeleccionarPrecioMateriaPrima jDialogSeleccionarMateriaPrima = new JDialogSeleccionarPrecioMateriaPrima(padre, allMateriaPrimaList);
						GuiUtil.centrar(jDialogSeleccionarMateriaPrima);
						jDialogSeleccionarMateriaPrima.setVisible(true);
						if(jDialogSeleccionarMateriaPrima.isAcepto()) {
							List<PrecioMateriaPrima> valoresSeleccionados = jDialogSeleccionarMateriaPrima.getPrecioMateriaPrimaSelectedList();
							for (PrecioMateriaPrima pmp : valoresSeleccionados) {
								handlePMPSelected(pmp);
							}
							fireChangeItemFacturaEvent(getElementos());
						}
					}
					
				});
			}
			return btnSelPMP;
		}

		@Override
		public void setModoConsulta(boolean modoConsulta) {
			super.setModoConsulta(modoConsulta);
			getBtnSelImpuestos().setEnabled(false);
		}

		public void addItemFacturaProveedorListener(ItemFacturaProveedorEventListener l) {
			listenerList.add(ItemFacturaProveedorEventListener.class, l);
		}

		protected final void fireChangeItemFacturaEvent(List<ItemFacturaProveedor> itemList) {
			final ItemFacturaProveedorEvent e = new ItemFacturaProveedorEvent(this, itemList);
			final ItemFacturaProveedorEventListener listeners[] = listenerList.getListeners(ItemFacturaProveedorEventListener.class);
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					for (int i = 0; i < listeners.length; i++) {
						listeners[i].changeItemFactura(e);
					}
				}
			});
		}

		@Override
		protected void agregarElemento(ItemFacturaProveedor elemento) {
			Object[] row = new Object[CANT_COLS];
			row[COL_CANTIDAD] = elemento.getCantidad();
			PrecioMateriaPrima precioMateriaPrima = ((ItemFacturaMateriaPrima)elemento).getPrecioMateriaPrima();
			row[COL_UNIDAD] = precioMateriaPrima.getMateriaPrima().getUnidad().toString();
			row[COL_DESCRIPCION] = elemento.getDescripcion();
			row[COL_FACTOR_MONEDA] = elemento.getFactorConversionMoneda().floatValue();
			row[COL_PRECIO_UNITARIO] = elemento.getPrecioUnitario().floatValue();
			row[COL_IMPORTE] = formatBigDecimal(elemento.getImporte());
			row[COL_IMPUESTO] = StringUtil.getCadena(elemento.getImpuestos(), ", ");
			row[COL_OBJ] = elemento;
			getTabla().addRow(row);
			if(!showActionsFacturaSinRemito) {
				getTabla().lockCell(getTabla().getRowCount() - 1, COL_CANTIDAD);
			}
		}

		@Override
		protected void botonQuitarPresionado() {
			fireChangeItemFacturaEvent(getElementos());
		}

		@Override
		protected FWJTable construirTabla() {
			FWJTable tabla = new FWJTable(0, CANT_COLS) {

				private static final long serialVersionUID = 6653836690455151776L;

				@Override
				public void newRowSelected(int newRow, int oldRow) {
					getBtnSelImpuestos().setEnabled(!modoConsulta && newRow != -1);
				}

				@Override
				public void cellEdited(int cell, int row) {
					if(!modoConsulta) {
						if(cell == COL_DESCUENTO) {
							ItemFacturaProveedor elemento = getElemento(row);
							Float descuento = (Float)getTabla().getTypedValueAt(row, cell);
							if(descuento == null) {
								descuento = 0f;
							}
							elemento.setPorcDescuento(new BigDecimal(descuento));
							fireChangeItemFacturaEvent(getElementos());
						}
						if(cell == COL_PRECIO_UNITARIO) {
							ItemFacturaProveedor elemento = getElemento(row);
							Float precioUnitario = (Float)getTabla().getTypedValueAt(row, cell);
							if(precioUnitario == null) {
								precioUnitario = 0f;
							}
							elemento.setPrecioUnitario(new BigDecimal(precioUnitario));
							fireChangeItemFacturaEvent(getElementos());
						}
						if(cell == COL_FACTOR_MONEDA) {
							ItemFacturaProveedor elemento = getElemento(row);
							Float factorMoneda = (Float)getTabla().getTypedValueAt(row, cell);
							if(factorMoneda == null) {
								factorMoneda = 0f;
							}
							elemento.setFactorConversionMoneda(new BigDecimal(factorMoneda));
							fireChangeItemFacturaEvent(getElementos());
						}
						if(cell == COL_CANTIDAD) {
							ItemFacturaProveedor elemento = getElemento(row);
							Float cantidad = (Float)getTabla().getTypedValueAt(row, cell);
							if(cantidad == null) {
								cantidad = 0f;
							}
							elemento.setCantidad(new BigDecimal(cantidad));
							fireChangeItemFacturaEvent(getElementos());
						}
						if(cell == COL_IMPORTE) {
							ItemFacturaProveedor elemento = getElemento(row);
							Float importe = (Float)getTabla().getTypedValueAt(row, cell);
							if(importe == null) {
								importe = 0f;
							}
							elemento.setImporte(new BigDecimal(importe));
							fireChangeItemFacturaEvent(getElementos());
						}

						if(cell == COL_DESCRIPCION) {
							ItemFacturaProveedor elemento = getElemento(row);
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

				@Override
				public void mouseClicked(MouseEvent e) {
					if(e.getClickCount() == 2 && !modoConsulta) {
						handleSeleccionImpuestos();
					}
				}

			});

			tabla.setFloatColumn(COL_CANTIDAD, "Cantidad", 0F, 1000F, 70, false);
			tabla.setStringColumn(COL_UNIDAD, "Unidad", 150, 50, true);
			tabla.setStringColumn(COL_DESCRIPCION, "Descripcion", 280, 280, false);
			tabla.setFloatColumn(COL_DESCUENTO, "Descuento (%)", 0F, 1000F, 80, false);
			tabla.setFloatColumn(COL_PRECIO_UNITARIO, "Precio Unitario", 0F, Float.MAX_VALUE, 80, false);
			tabla.setFloatColumn(COL_FACTOR_MONEDA, "Factor ($)", 0F, Float.MAX_VALUE, 60, false);
			tabla.setFloatColumn(COL_IMPORTE, "Importe Total", 0F, Float.MAX_VALUE, 60, false);
			tabla.setStringColumn(COL_IMPUESTO, "Impuestos", 200, 200, true);
			tabla.setStringColumn(COL_OBJ, "", 0, 0, true);

			tabla.setReorderingAllowed(false);
			tabla.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			
			return tabla;
		}

		@Override
		protected ItemFacturaProveedor getElemento(int fila) {
			return (ItemFacturaProveedor)getTabla().getValueAt(fila, COL_OBJ);
		}

		@Override
		protected String validarElemento(int fila) {
			ItemFacturaProveedor elemento = getElemento(fila);
			if(elemento.getImpuestos().isEmpty()) {
				return "Todos los items de las facturas deben tener al menos un impuesto cargado.";
			}
			if(StringUtil.isNullOrEmpty(elemento.getDescripcion())) {
				return "Debe completar la descripci�n del item de la fila " + (fila + 1);
			}
			if(elemento.getFactorConversionMoneda() == null || elemento.getFactorConversionMoneda().doubleValue() <= 0) {
				return "El factor de conversi�n debe ser mayor a cero";
			}
			if(elemento.getCantidad() == null || elemento.getCantidad().compareTo(new BigDecimal(0)) <= 0) {
				return "La cantidad debe ser mayor a cero";
			}
			PrecioMateriaPrima precioMateriaPrima = ((ItemFacturaMateriaPrima)elemento).getPrecioMateriaPrima();
			if(precioMateriaPrima.getTipoMoneda() == ETipoMoneda.DOLAR && elemento.getFactorConversionMoneda().floatValue() == 1f) {
				return "El factor de conversi�n del item '" + elemento.getDescripcion() + "' no puede ser de 1 cuando la moneda es DOLAR.";
			}
			
			return null;
		}

		public String validarFactorConversion() {
			Set<Float> factorsSet = new HashSet<Float>();
			for(ItemFacturaProveedor ifp : getElementos()) {
				if(ifp.getFactorConversionMoneda().floatValue() != 1f) {
					factorsSet.add(ifp.getFactorConversionMoneda().floatValue());
				}
			}
			if(factorsSet.size() > 1) {
				return "No pueden haber dos factores de conversi�n distintos";
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
			int selectedRowCount = getTabla().getSelectedRowCount();
			if(selectedRowCount > 0) {
				List<ItemFacturaProveedor> elementos = new ArrayList<ItemFacturaProveedor>();
				List<ImpuestoItemProveedor> impuestos = new ArrayList<ImpuestoItemProveedor>();
				int[] selectedRows = getTabla().getSelectedRows();
				for(int i = 0; i < selectedRows.length; i ++) {
					ItemFacturaProveedor itemFactura = getElemento(selectedRows[i]);
					elementos.add(itemFactura);
					impuestos.addAll(itemFactura.getImpuestos());
				}
				JDialogSeleccionarCrearImpuesto dialogSeleccionarCrearImpuesto = new JDialogSeleccionarCrearImpuesto(padre, proveedor, impuestos);
				dialogSeleccionarCrearImpuesto.setVisible(true);
				if(dialogSeleccionarCrearImpuesto.isAcepto()) {
					for(ItemFacturaProveedor elemento : elementos) {
						elemento.getImpuestos().clear();
					}
					List<ImpuestoItemProveedor> impuestosSelectedResult = dialogSeleccionarCrearImpuesto.getImpuestosSelectedResult();
					if(impuestosPorPciaOK(impuestosSelectedResult)) {
						int indexRows = 0;
						for(ItemFacturaProveedor elemento : elementos) {
							elemento.getImpuestos().addAll(impuestosSelectedResult);
							getTabla().setValueAt(formatBigDecimal(elemento.recalcularImporteTotal()), selectedRows[indexRows], COL_IMPORTE);
							getTabla().setValueAt(StringUtil.getCadena(elemento.getImpuestos(), ", "), selectedRows[indexRows], COL_IMPUESTO);
							indexRows++;
						}
						fireChangeItemFacturaEvent(getElementos());
					}
				}
			}
		}

		private boolean impuestosPorPciaOK(List<ImpuestoItemProveedor> impuestosSelectedResult) {
			for(ImpuestoItemProveedor iip : impuestosSelectedResult) {
				if(iip.getTipoImpuesto() == ETipoImpuesto.INGRESOS_BRUTOS && !iip.getProvincia().getId().equals(proveedor.getProvincia().getId())) {
					FWJOptionPane.showErrorMessage(JDialogCargarFacturaProveedor.this, StringW.wordWrap("El impuesto " + iip.toString() + " no puede elegirse porque no aplica en la provincia de origen del proveedor."), "Atenci�n");
					return false;
				}
			}
			return true;
		}

		@Override
		public boolean validarAgregar() {
			JDialogAltaNuevaMateriaPrimaYPrecioMateriaPrima dialogo = new JDialogAltaNuevaMateriaPrimaYPrecioMateriaPrima(padre, proveedor);
			dialogo.setVisible(true);
			if(dialogo.isAcepto()) {
				PrecioMateriaPrima prep = dialogo.getPrecioMateriaPrima();
				handlePMPSelected(prep);
				fireChangeItemFacturaEvent(getElementos());
			}
			return false;
		}

		private void handlePMPSelected(PrecioMateriaPrima prep) {
			ItemFacturaMateriaPrima ifmp = new ItemFacturaMateriaPrima();
			ifmp.setPorcDescuento(new BigDecimal(0));
			ifmp.setFactorConversionMoneda(new BigDecimal(1));
			ifmp.setCantidad(new BigDecimal(1));
			ifmp.setPrecioMateriaPrima(prep);
			ifmp.setPrecioUnitario(prep.getPrecio());
			ifmp.setDescripcion(prep.toString());
			if(ifmp.getPrecioUnitario() != null) {
				ifmp.setImporte(ifmp.getPrecioUnitario().multiply(ifmp.getCantidad()));
			}
			agregarElemento(ifmp);
		}

	}

	private class RemitoLinkeableLabel extends LinkableLabel {

		private static final long serialVersionUID = -4765485631705199316L;

		private RemitoEntradaProveedor remito;

		public RemitoLinkeableLabel() {
			super("x");
		}

		@Override
		public void labelClickeada(MouseEvent e) {
			if (e.getClickCount() == 1 && remito!=null) {
				RemitoEntradaProveedor rep = GTLBeanFactory.getInstance().getBean2(RemitoEntradaProveedorFacadeRemote.class).getByIdEager(remito.getId());
				JDialogAgregarRemitoEntradaProveedor dialogoRemitoEntrada = new JDialogAgregarRemitoEntradaProveedor(padre, true, rep);
				GuiUtil.centrar(dialogoRemitoEntrada);
				dialogoRemitoEntrada.setVisible(true);
			}
		}

		public void setRemito(RemitoEntradaProveedor remito) {
			this.remito = remito;
			if(remito != null) {
				setTexto(remito.getNroRemito());
				refreshLabel();
			}
		}

	}

	private FWJTextField getTxtDescuento() {
		if(txtDescuento == null) {
			txtDescuento = new FWJTextField();
			addKeyAndFocusLostAdapter(txtDescuento);
		}
		return txtDescuento;
	}

	private FWJTable getTablaImpuestos() {
		if(tablaImpuestos == null) {
			tablaImpuestos = new FWJTable(0, 3);
			tablaImpuestos.setStringColumn(COL_NOMBRE_IMPUESTO, "IMPUESTO", 100);
			tablaImpuestos.setStringColumn(COL_VALOR_IMPUESTO, "IMPORTE PESOS", 90);
			tablaImpuestos.setStringColumn(COL_VALOR_IMPUESTO_CON_FACTOR, "IMPORTE DOLARES", 90);
		}
		return tablaImpuestos;
	}

	private FacturaProveedorFacadeRemote getFacturaProveedorFacade() {
		if(facturaProveedorFacade == null) {
			facturaProveedorFacade = GTLBeanFactory.getInstance().getBean2(FacturaProveedorFacadeRemote.class);
		}
		return facturaProveedorFacade;
	}

	private List<RemitoEntradaProveedor> toEager(List<RemitoEntradaProveedor> remitoEntradaList) {
		List<RemitoEntradaProveedor> remitoListEager = new ArrayList<RemitoEntradaProveedor>();
		RemitoEntradaProveedorFacadeRemote repfr = GTLBeanFactory.getInstance().getBean2(RemitoEntradaProveedorFacadeRemote.class);
		for(RemitoEntradaProveedor rep : remitoEntradaList) {
			RemitoEntradaProveedor repEager = repfr.getByIdEager(rep.getId());
			repEager.setProveedor(factura.getProveedor());
			remitoListEager.add(repEager);
		}
		return remitoListEager;
	}

}