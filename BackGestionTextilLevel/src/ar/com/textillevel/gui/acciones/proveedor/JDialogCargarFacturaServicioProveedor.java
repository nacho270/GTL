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

import ar.clarin.fwjava.componentes.CLCheckBoxListDialog;
import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.CLJTable;
import ar.clarin.fwjava.componentes.CLJTextField;
import ar.clarin.fwjava.componentes.PanelTabla;
import ar.clarin.fwjava.util.DateUtil;
import ar.clarin.fwjava.util.GuiUtil;
import ar.clarin.fwjava.util.StringUtil;
import ar.com.textillevel.entidades.documentos.factura.proveedor.FacturaProveedor;
import ar.com.textillevel.entidades.documentos.factura.proveedor.ImpuestoItemProveedor;
import ar.com.textillevel.entidades.documentos.factura.proveedor.ItemFacturaProveedor;
import ar.com.textillevel.entidades.documentos.factura.proveedor.ItemFacturaServicio;
import ar.com.textillevel.entidades.documentos.factura.proveedor.Servicio;
import ar.com.textillevel.entidades.enums.ETipoImpuesto;
import ar.com.textillevel.entidades.gente.Proveedor;
import ar.com.textillevel.facade.api.remote.FacturaProveedorFacadeRemote;
import ar.com.textillevel.facade.api.remote.ServicioFacadeRemote;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.controles.PanelDatePicker;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogCargarFacturaServicioProveedor extends JDialog {

	private static final long serialVersionUID = 3133488183144001174L;

	private JButton btnGuardar;
	private JButton btnSalir;
	private JFormattedTextField txtNroFactura;
	private PanelDatePicker panelFecha;
	private JTextField txtRazonSocial;
	private PanTablaItemFactura panTablaItemFactura;

	private JTextField txtSubtotal;
	private JTextField txtSubtotalConFactor;
	private JTextField txtTotal;
	private JTextField txtTotalConFactor;
	private JTextField txtDescuento;
	private CLJTable tablaImpuestos;
	private Frame padre;
	private boolean modoConsulta;
	
	private FacturaProveedor factura;
	private FacturaProveedorFacadeRemote facturaProveedorFacade;

	private JTextField txtUsuarioCreador;

	private CLJTextField txtImpVarios;
	private CLJTextField txtPercepIVA;

	private static final int COL_NOMBRE_IMPUESTO = 0;
	private static final int COL_VALOR_IMPUESTO = 1;
	private static final int COL_VALOR_IMPUESTO_CON_FACTOR = 2;

	private static final int CANT_MAX_REMITOS_SELECTED_HARDCODE = 15;

	public JDialogCargarFacturaServicioProveedor(Frame padre, FacturaProveedor factura, boolean modoConsulta) {
		super(padre);
		this.padre = padre;
		this.factura = factura;
		this.modoConsulta = modoConsulta;
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
		getTxtDescuento().setText(GenericUtils.getDecimalFormat().format(factura.getDescuento().longValue()));
		getTxtSubtotal().setText(GenericUtils.getDecimalFormat().format(factura.getMontoSubtotal().longValue()));
		getTxtTotal().setText(GenericUtils.getDecimalFormat().format(factura.getMontoTotal().longValue()));
		getTxtImpVarios().setText(GenericUtils.getDecimalFormat().format(factura.getImpVarios().longValue()));
		getTxtPercepIVA().setText(GenericUtils.getDecimalFormat().format(factura.getPercepIVA().longValue()));
		getTxtNroFactura().setText(factura.getNroFactura());
		getPanTablaItemFactura().agregarElementos(factura.getItems());
		if(modoConsulta) {
			getBtnGuardar().setEnabled(false);
			getTxtNroFactura().setEditable(false);
			getTxtDescuento().setEditable(false);
			getTxtImpVarios().setEditable(false);
			getTxtPercepIVA().setEditable(false);
			getPanelFecha().setEnabled(false);
		}
		updateTotales(getPanTablaItemFactura().getElementos());
	}

	private void construct() {
		setUpScreen();
		setUpComponentes();
	}

	private void setUpScreen() {
		setSize(new Dimension(980, 600));
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		if(modoConsulta) {
			setTitle("Consulta de Factura de Servicios");
		} else if(factura.getId() == null) {
			setTitle("Alta de Factura de Servicios");
		} else {
			setTitle("Modificación de Factura de Servicios");
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

	private double getValueInTextField(JTextField txt) {
		String strValue = txt.getText().trim();
		if (StringUtil.isNullOrEmpty(strValue) || !GenericUtils.esNumerico(strValue)) {
			return 0D;
		} else {
			return new Double(strValue.replace(',', '.')).doubleValue();
		}
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
				updateTotales(getPanTablaItemFactura().getElementos());
			} else {
				GuiUtil.showTooltipText(getTxtPercepIVA(), "El texto debe ser numérico.");
			}
		}
		updateTotales(getPanTablaItemFactura().getElementos());
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

	protected void handleIngresoImpVarios() {
		String impVariosStr = getTxtImpVarios().getText();
		if(!StringUtil.isNullOrEmpty(impVariosStr)) {
			if(GenericUtils.esNumerico(impVariosStr)) {
				updateTotales(getPanTablaItemFactura().getElementos());
			} else {
				GuiUtil.showTooltipText(getTxtImpVarios(), "El texto debe ser numérico.");
			}
		}
		updateTotales(getPanTablaItemFactura().getElementos());
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

	private JTextField getTxtSubtotal() {
		if(txtSubtotal == null) {
			txtSubtotal = new JTextField();
			txtSubtotal.setEditable(false);
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
		return panelDatosProveedor;
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
						CLJOptionPane.showInformationMessage(JDialogCargarFacturaServicioProveedor.this, "La factura ha sido grabada con éxito.", "Alta de Factura de Proveedor");
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
	}

	private boolean validar() {
		if(getPanelFecha().getDate() == null) {
			CLJOptionPane.showErrorMessage(JDialogCargarFacturaServicioProveedor.this, "Debe ingresar una fecha válida", getTitle());
			return false;
		}
		if(StringUtil.isNullOrEmpty(getTxtNroFactura().getText())) {
			CLJOptionPane.showErrorMessage(JDialogCargarFacturaServicioProveedor.this, "Debe ingresar el número de factura.", getTitle());
			getTxtNroFactura().requestFocus();
			return false;
		}
		String regExpNroFactura = "[0-9]{4}-[0-9]{8}";
		Pattern p = Pattern.compile(regExpNroFactura);
		Matcher matcher = p.matcher(getTxtNroFactura().getText().trim());
		if(!matcher.matches()) {
			CLJOptionPane.showErrorMessage(JDialogCargarFacturaServicioProveedor.this, "Debe ingresar un número de factura válido.", getTitle());
			getTxtNroFactura().requestFocus();
			return false;
		}
		String txtValidacion = getPanTablaItemFactura().validarElementos();
		if(!StringUtil.isNullOrEmpty(txtValidacion)) {
			CLJOptionPane.showErrorMessage(JDialogCargarFacturaServicioProveedor.this, StringW.wordWrap(txtValidacion), getTitle());
			return false;
		}
		txtValidacion = getPanTablaItemFactura().validarFactorConversion();
		if(!StringUtil.isNullOrEmpty(txtValidacion)) {
			CLJOptionPane.showErrorMessage(JDialogCargarFacturaServicioProveedor.this, StringW.wordWrap(txtValidacion), getTitle());
			return false;
		}
		boolean existeNroFactura = getFacturaProveedorFacade().existeNroFacturaByProveedor(factura.getId(), getTxtNroFactura().getText().trim(), factura.getProveedor().getId());
		if(existeNroFactura) {
			getTxtNroFactura().requestFocus();
			CLJOptionPane.showErrorMessage(JDialogCargarFacturaServicioProveedor.this, StringW.wordWrap("El número de factura ya existe para el mismo proveedor."), getTitle());
			return false;
		}
		if(getPanTablaItemFactura().getElementos().isEmpty()) {
			CLJOptionPane.showErrorMessage(JDialogCargarFacturaServicioProveedor.this, StringW.wordWrap("Debe agregar al menos un servicio."), getTitle());
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

	private PanTablaItemFactura getPanTablaItemFactura() {
		if(panTablaItemFactura == null) {
			panTablaItemFactura = new PanTablaItemFactura(factura.getProveedor());
			panTablaItemFactura.addItemFacturaProveedorListener(new ItemFacturaProveedorEventListener() {

				public void changeItemFactura(ItemFacturaProveedorEvent evt) {
					updateTotales(evt.getItemFacturaList());
				}

			});

			panTablaItemFactura.setModoConsulta(modoConsulta);
		}
		return panTablaItemFactura;
	}

	private void updateTotales(List<ItemFacturaProveedor> itemFacturaList) {
		Float factorMoneda = getFactorMoneda(itemFacturaList);
		double subtotal = updateSubtotal(itemFacturaList, factorMoneda);
		double descuento = (getValueInTextField(getTxtDescuento())/100)*subtotal; 
		double totalImpuestos = updateTablaImpuestos(itemFacturaList, factorMoneda);
		double totalImpVarios = getValueInTextField(getTxtImpVarios());
		double totalPercIVA = getValueInTextField(getTxtPercepIVA());
		double total = subtotal - descuento + totalImpuestos + totalImpVarios + totalPercIVA;
		getTxtTotal().setText(GenericUtils.getDecimalFormat().format(total));
		if(factorMoneda == null || factorMoneda == 0f) {
			getTxtTotalConFactor().setText("");
		} else {
			getTxtTotalConFactor().setText(GenericUtils.getDecimalFormat().format(total/factorMoneda));
		}
		if(!modoConsulta) {
			factura.setDescuento(new BigDecimal(getValueInTextField(getTxtDescuento())));
			factura.setMontoSubtotal(new BigDecimal(subtotal));
			factura.setMontoTotal(new BigDecimal(total));
			factura.setMontoFaltantePorPagar(new BigDecimal(total));
			factura.setImpVarios(new BigDecimal(totalImpVarios));
			factura.setPercepIVA(new BigDecimal(totalPercIVA));
		}
	}

	private Float getFactorMoneda(List<ItemFacturaProveedor> itemFacturaList) {
		Set<Float> factorSet  = new HashSet<Float>();
		boolean estaElUno = false;
		for(ItemFacturaProveedor ifp : itemFacturaList) {
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

	private double updateSubtotal(List<ItemFacturaProveedor> itemFacturaList, Float factorMoneda) {
		BigDecimal subtotal = new BigDecimal(0);
		for(ItemFacturaProveedor ifp : itemFacturaList) {
			subtotal = subtotal.add(ifp.getImporteSinImpuestos());
		}
		getTxtSubtotal().setText(GenericUtils.getDecimalFormat().format(subtotal.doubleValue()));
		if(factorMoneda == null || factorMoneda == 0f) {
			getTxtSubtotalConFactor().setText("");
		} else {
			getTxtSubtotalConFactor().setText(GenericUtils.getDecimalFormat().format(subtotal.doubleValue()/factorMoneda));
		}
		return subtotal.doubleValue();
	}

	private double updateTablaImpuestos(List<ItemFacturaProveedor> itemFacturaList, Float factorMoneda) {
		Double total = 0D;
		//armo un map con los impuestos y los totales por impuesto
		Map<ImpuestoItemProveedor, Double> mapImpuestos = new HashMap<ImpuestoItemProveedor, Double>();
		for(ItemFacturaProveedor ifp : itemFacturaList) {
			List<ImpuestoItemProveedor> impuestos = ifp.getImpuestos();
			for(ImpuestoItemProveedor impuesto : impuestos) {
				if(impuesto != null) {
					Double impValue = mapImpuestos.get(impuesto);
					if(impValue == null) {
						impValue = 0D;
					}
					BigDecimal importeOrig = ifp.getPrecioUnitario().multiply(ifp.getCantidad()).multiply(ifp.getFactorConversionMoneda());
					impValue += importeOrig.doubleValue() * (impuesto.getPorcDescuento()/100);
					mapImpuestos.put(impuesto, impValue);
				}
			}
		}
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

	private class PanTablaItemFactura extends PanelTabla<ItemFacturaProveedor> {

		private static final long serialVersionUID = -7198713632884388418L;

		private static final int CANT_COLS = 8;
		private static final int COL_CANTIDAD = 0;
		private static final int COL_DESCRIPCION = 1;
		private static final int COL_DESCUENTO = 2;
		private static final int COL_PRECIO_UNITARIO = 3;
		private static final int COL_FACTOR_MONEDA = 4;
		private static final int COL_IMPORTE = 5;
		private static final int COL_IMPUESTO = 6;
		private static final int COL_OBJ = 7;

		private JButton btnSelImpuestos;
		private Proveedor proveedor;
		private ServicioFacadeRemote servicioFacade;

		public PanTablaItemFactura(Proveedor proveedor) {
			this.proveedor = proveedor;
			agregarBoton(getBtnSelImpuestos());
			servicioFacade = GTLBeanFactory.getInstance().getBean2(ServicioFacadeRemote.class);
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
			Servicio servicio = ((ItemFacturaServicio)elemento).getServicio();
			row[COL_DESCRIPCION] = servicio.getNombre();
			row[COL_FACTOR_MONEDA] = elemento.getFactorConversionMoneda().floatValue();
			row[COL_PRECIO_UNITARIO] = elemento.getPrecioUnitario().floatValue();
			row[COL_IMPORTE] = GenericUtils.getDecimalFormat().format(elemento.getImporte().floatValue());
			row[COL_IMPUESTO] = StringUtil.getCadena(elemento.getImpuestos(), ", ");
			row[COL_OBJ] = elemento;
			getTabla().addRow(row);
		}

		@Override
		protected void botonQuitarPresionado() {
			fireChangeItemFacturaEvent(getElementos());
		}

		@Override
		protected CLJTable construirTabla() {
			CLJTable tabla = new CLJTable(0, CANT_COLS) {

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
							getTabla().setValueAt(GenericUtils.getDecimalFormat().format(elemento.recalcularImporteTotal().floatValue()), row, COL_IMPORTE);
							fireChangeItemFacturaEvent(getElementos());							
						}
						if(cell == COL_PRECIO_UNITARIO) {
							ItemFacturaProveedor elemento = getElemento(row);
							Float precioUnitario = (Float)getTabla().getTypedValueAt(row, cell);
							if(precioUnitario == null) {
								precioUnitario = 0f;
							}
							elemento.setPrecioUnitario(new BigDecimal(precioUnitario));
							getTabla().setValueAt(GenericUtils.getDecimalFormat().format(elemento.recalcularImporteTotal().floatValue()), row, COL_IMPORTE);
							fireChangeItemFacturaEvent(getElementos());				
						}
						if(cell == COL_FACTOR_MONEDA) {
							ItemFacturaProveedor elemento = getElemento(row);
							Float factorMoneda = (Float)getTabla().getTypedValueAt(row, cell);
							if(factorMoneda == null) {
								factorMoneda = 0f;
							}
							elemento.setFactorConversionMoneda(new BigDecimal(factorMoneda));
							getTabla().setValueAt(GenericUtils.getDecimalFormat().format(elemento.recalcularImporteTotal().floatValue()), row, COL_IMPORTE);
							fireChangeItemFacturaEvent(getElementos());				
						}
						if(cell == COL_CANTIDAD) {
							ItemFacturaProveedor elemento = getElemento(row);
							Float cantidad = (Float)getTabla().getTypedValueAt(row, cell);
							if(cantidad == null) {
								cantidad = 0f;
							}
							elemento.setCantidad(new BigDecimal(cantidad));
							getTabla().setValueAt(GenericUtils.getDecimalFormat().format(elemento.recalcularImporteTotal().floatValue()), row, COL_IMPORTE);
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

			tabla.setFloatColumn(COL_CANTIDAD, "Cantidad", 0F, 1000F, 100, false);
			tabla.setStringColumn(COL_DESCRIPCION, "Descripcion", 280, 280, false);
			tabla.setFloatColumn(COL_DESCUENTO, "Descuento (%)", 0F, 1000F, 80, false);
			tabla.setFloatColumn(COL_PRECIO_UNITARIO, "Precio Unitario", 0F, Float.MAX_VALUE, 80, false);
			tabla.setFloatColumn(COL_FACTOR_MONEDA, "Factor ($)", 0F, Float.MAX_VALUE, 60, false);
			tabla.setStringColumn(COL_IMPORTE, "Importe Total", 70, 70, true);
			tabla.setStringColumn(COL_IMPUESTO, "Impuestos", 200, 200, true);
			tabla.setStringColumn(COL_OBJ, "", 0, 0, true);

			tabla.setReorderingAllowed(false);
			tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			
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
				return "Debe completar la descripción del item de la fila " + (fila + 1);
			}
			if(elemento.getFactorConversionMoneda() == null || elemento.getFactorConversionMoneda().doubleValue() <= 0) {
				return "El factor de conversión debe ser mayor a cero";
			}
			if(elemento.getCantidad() == null || elemento.getCantidad().compareTo(new BigDecimal(0)) <= 0) {
				return "La cantidad debe ser mayor a cero";
			}
			if(elemento.getPrecioUnitario() == null || elemento.getPrecioUnitario().compareTo(new BigDecimal(0)) <= 0) {
				return "Debe ingresar un precio unitario";
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
				return "No pueden haber dos factores de conversión distintos";
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
			int selectedRow = getTabla().getSelectedRow();
			if(selectedRow != -1) {
				ItemFacturaProveedor elemento = getElemento(selectedRow);
				JDialogSeleccionarCrearImpuesto dialogSeleccionarCrearImpuesto = new JDialogSeleccionarCrearImpuesto(padre, proveedor, elemento.getImpuestos());
				dialogSeleccionarCrearImpuesto.setVisible(true);
				if(dialogSeleccionarCrearImpuesto.isAcepto()) {
					List<ImpuestoItemProveedor> impuestosSelectedResult = dialogSeleccionarCrearImpuesto.getImpuestosSelectedResult();
					if(impuestosPorPciaOK(impuestosSelectedResult)) {
						elemento.getImpuestos().clear();
						elemento.getImpuestos().addAll(impuestosSelectedResult);
						getTabla().setValueAt(GenericUtils.getDecimalFormat().format(elemento.recalcularImporteTotal().floatValue()), selectedRow, COL_IMPORTE);
						getTabla().setValueAt(StringUtil.getCadena(elemento.getImpuestos(), ", "), selectedRow, COL_IMPUESTO);
						fireChangeItemFacturaEvent(getElementos());
					}
				}
			}
		}

		private boolean impuestosPorPciaOK(List<ImpuestoItemProveedor> impuestosSelectedResult) {
			for(ImpuestoItemProveedor iip : impuestosSelectedResult) {
				if(iip.getTipoImpuesto() == ETipoImpuesto.INGRESOS_BRUTOS && !iip.getProvincia().getId().equals(proveedor.getProvincia().getId())) {
					CLJOptionPane.showErrorMessage(JDialogCargarFacturaServicioProveedor.this, StringW.wordWrap("El impuesto " + iip.toString() + " no puede elegirse porque no aplica en la provincia de origen del proveedor."), "Atención");
					return false;
				}
			}
			return true;
		}

		
		@Override
		@SuppressWarnings("unchecked")
		public boolean validarAgregar() {
			CLCheckBoxListDialog chkDialogServicios = new CLCheckBoxListDialog(JDialogCargarFacturaServicioProveedor.this, servicioFacade.getServiciosByProveedor(proveedor.getId())); 
			chkDialogServicios.setVisible(true);
			List<Servicio> servicioList = chkDialogServicios.getValoresSeleccionados();
			for(Servicio s : servicioList) {
				if(!existeServicio(s)) {
					handleServicioSelected(s);
					fireChangeItemFacturaEvent(getElementos());
				}
			}
			return false;
		}

		private boolean existeServicio(Servicio s) {
			for(ItemFacturaProveedor ii : getElementos()) {
				if(((ItemFacturaServicio)ii).getServicio().getId().equals(s.getId())) {
					return true;
				}
			}
			return false;
		}

		private void handleServicioSelected(Servicio s) {
			ItemFacturaServicio ifmp = new ItemFacturaServicio();
			ifmp.setPorcDescuento(new BigDecimal(0));
			ifmp.setFactorConversionMoneda(new BigDecimal(1));
			ifmp.setCantidad(new BigDecimal(1));
			ifmp.setPrecioUnitario(new BigDecimal(0));
			ifmp.setDescripcion(s.toString());
			ifmp.setServicio(s);
			if(ifmp.getPrecioUnitario() != null) {
				ifmp.setImporte(ifmp.getPrecioUnitario().multiply(ifmp.getCantidad()));
			}
			agregarElemento(ifmp);
		}

	}

	private JTextField getTxtDescuento() {
		if(txtDescuento == null) {
			txtDescuento = new JTextField();
			txtDescuento.addFocusListener(new FocusAdapter() {

				@Override
				public void focusLost(FocusEvent e) {
					handleIngresoDescuento();
				}

			});

			txtDescuento.addKeyListener(new KeyAdapter() {

				@Override
				public void keyPressed(KeyEvent e) {
					if(e.getKeyCode() == KeyEvent.VK_ENTER) {
						handleIngresoDescuento();
					}
				}

			});

		}
		return txtDescuento;
	}

	private void handleIngresoDescuento() {
		String descStr = getTxtDescuento().getText();
		if(!StringUtil.isNullOrEmpty(descStr)) {
			if(GenericUtils.esNumerico(descStr)) {
				updateTotales(getPanTablaItemFactura().getElementos());
			} else {
				GuiUtil.showTooltipText(getTxtDescuento(), "El texto debe ser numérico.");
			}
		}
		updateTotales(getPanTablaItemFactura().getElementos());
	}

	private CLJTable getTablaImpuestos() {
		if(tablaImpuestos == null) {
			tablaImpuestos = new CLJTable(0, 3);
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

}