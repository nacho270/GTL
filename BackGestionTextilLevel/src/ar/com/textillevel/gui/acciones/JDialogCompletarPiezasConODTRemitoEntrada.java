package ar.com.textillevel.gui.acciones;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import main.GTLGlobalCache;

import org.apache.taglibs.string.util.StringW;

import ar.com.fwcommon.componentes.FWDateField;
import ar.com.fwcommon.componentes.FWJNumericTextField;
import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTable;
import ar.com.fwcommon.componentes.FWJTextField;
import ar.com.fwcommon.componentes.PanelTabla;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.fwcommon.util.StringUtil;
import ar.com.textillevel.entidades.documentos.factura.CondicionDeVenta;
import ar.com.textillevel.entidades.documentos.remito.PiezaRemito;
import ar.com.textillevel.entidades.documentos.remito.RemitoEntrada;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.ventas.ProductoArticulo;
import ar.com.textillevel.facade.api.remote.CondicionDeVentaFacadeRemote;
import ar.com.textillevel.facade.api.remote.RemitoEntradaFacadeRemote;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;
import ar.com.textillevel.modulos.odt.entidades.PiezaODT;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogCompletarPiezasConODTRemitoEntrada extends JDialog {

	private static final long serialVersionUID = 1L;

	private static final int MAX_LONGITUD_RAZ_SOCIAL = 50;

	private JPanel panDetalle;
	private FWJTextField txtRazonSocial;
	private PanelTablaPieza panTablaPieza;
	private JPanel pnlBotones;
	private JButton btnAceptar;
	private JButton btnCancelar;
	private FWJNumericTextField txtNroRemito;
	private FWDateField txtFechaEmision;
	private FWJTextField txtPesoTotal;
	private JButton btnSelProductos;
	private JTextField txtProductos;
	private RemitoEntrada remitoEntrada;
	private JPanel panTotales; 
	private JTextField txtMetros;
	private JTextField txtPiezas;
	private JMenuItem menuItemEliminarFilas;
	private JMenuItem menuItemAgregarPiezas;

	private JPanel panelDatosCliente; 

	private JPanel panOpcionPiezasODT;
	private JRadioButton rbtOpcionConPiezasODT;
	private JRadioButton rbtOpcionSinPiezasODT;

	private JTextField txtLocalidad;
	private JTextField txtDireccion;
	private JPanel panelDatosFactura;
	private JComboBox cmbCondicionVenta;
	private JTextField txtCUIT;
	private JTextField txtCondicionIVA;
	private JMenu menuODT;
	private Frame owner;
	private List<OrdenDeTrabajo> odtList;	
	private RemitoEntradaFacadeRemote remitoEntradaFacade;
	private CondicionDeVentaFacadeRemote condicionDeVentaFacade;

	public JDialogCompletarPiezasConODTRemitoEntrada(Frame owner, RemitoEntrada remitoEntrada) {
		super(owner);
		this.owner = owner;
		this.remitoEntrada = remitoEntrada;
		this.odtList = new ArrayList<OrdenDeTrabajo>();
		setSize(new Dimension(730, 750));
		setTitle("Completar Piezas de Remito de Entrada");
		construct();
		setDatos();
		setModal(true);
	}

	private void setDatos() {
		remitoEntrada.getProductoArticuloList().clear();

		Cliente cliente = remitoEntrada.getCliente();
		getTxtRazonSocial().setText(cliente.getRazonSocial());
		getTxtFechaEmision().setFecha(remitoEntrada.getFechaEmision());
		getTxtCuit().setText(cliente.getCuit());
		if(cliente.getDireccionReal() != null) {
			getTxtDireccion().setText(cliente.getDireccionReal().getDireccion());
			if(cliente.getDireccionReal().getLocalidad() != null) {
				getTxtLocalidad().setText(cliente.getDireccionReal().getLocalidad().getNombreLocalidad());
			}
		}
		if(cliente.getPosicionIva() != null) {
			getTxtCondicionIVA().setText(cliente.getPosicionIva().getDescripcion());
		}
		getCmbCondicionVenta().setEnabled(false);
	}

	private void construct() {
		setLayout(new GridBagLayout());
		add(getPanDetalle(), GenericUtils.createGridBagConstraints(0, 0,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 1, 1, 0, 1));
		add(getPanTotales(), GenericUtils.createGridBagConstraints(0, 1,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
		add(getPanelBotones(), GenericUtils.createGridBagConstraints(0, 2,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
	}

	private JPanel getPanTotales() {
		if(panTotales == null) {
			panTotales = new JPanel();
			panTotales.setLayout(new GridBagLayout());
			panTotales.add(new JLabel(" PIEZAS:"), GenericUtils.createGridBagConstraints(0, 0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panTotales.add(getTxtPiezas(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0.5, 0));
			panTotales.add(new JLabel(" METROS:"), GenericUtils.createGridBagConstraints(2, 0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panTotales.add(getTxtMetros(), GenericUtils.createGridBagConstraints(3, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0.5, 0));
		}
		return panTotales;
	}

	private JTextField getTxtPiezas() {
		if(txtPiezas == null) {
			txtPiezas = new JTextField();
			txtPiezas.setEditable(false);
			if(remitoEntrada.getId() != null) {
				getTxtPiezas().setText(String.valueOf(remitoEntrada.getPiezas().size()));
			}
		}
		return txtPiezas;
	}

	private JTextField getTxtMetros() {
		if(txtMetros == null) {
			txtMetros = new JTextField();
			txtMetros.setEditable(false);
			if(remitoEntrada.getId() != null) {
				txtMetros.setText(remitoEntrada.getTotalMetros().toString());
			}
		}
		return txtMetros;
	}

	private JPanel getPanDetalle() {
		if(panDetalle == null) {
			panDetalle = new JPanel();
			panDetalle.setLayout(new GridBagLayout());
			
			panDetalle.add(getPanOpcionPiezasODT(), GenericUtils.createGridBagConstraints(0, 0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 6, 1, 0, 0));
			
			panDetalle.add(getPanelDatosCliente(), GenericUtils.createGridBagConstraints(0, 1,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 6, 1, 0, 0));

			panDetalle.add(getPanelDatosFactura(), GenericUtils.createGridBagConstraints(0, 2,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 6, 1, 0, 0));

			panDetalle.add(new JLabel(" PESO TOTAL:"), GenericUtils.createGridBagConstraints(0, 3,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getTxtPesoTotal(), GenericUtils.createGridBagConstraints(1, 3, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0.5, 0));

			panDetalle.add(new JLabel(" FECHA:"), GenericUtils.createGridBagConstraints(2, 3,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getTxtFechaEmision(), GenericUtils.createGridBagConstraints(3, 3, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0.5, 0));

			panDetalle.add(getBtnSelProductos(), GenericUtils.createGridBagConstraints(0, 4,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getTxtProductos(), GenericUtils.createGridBagConstraints(1, 4, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 5, 1, 1, 0));

			panDetalle.add(getPanTablaPieza(), GenericUtils.createGridBagConstraints(0, 5, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 6, 1, 1, 1));
		}
	
		GuiUtil.setEstadoPanel(panDetalle, true);

		return panDetalle;
	}

	private JPanel getPanOpcionPiezasODT() {
		if(panOpcionPiezasODT == null) {
			panOpcionPiezasODT = new JPanel();
			panOpcionPiezasODT.setLayout(new FlowLayout());
			panOpcionPiezasODT.add(getRbtOpcionConPiezasODT());
			panOpcionPiezasODT.add(getRbtOpcionSinPiezasODT());
			ButtonGroup bgOpcionProceso = new ButtonGroup();
			bgOpcionProceso.add(getRbtOpcionConPiezasODT());
			bgOpcionProceso.add(getRbtOpcionSinPiezasODT());
		}
		panOpcionPiezasODT.setVisible(false);
		return panOpcionPiezasODT;
	}

	private JRadioButton getRbtOpcionConPiezasODT() {
		if (rbtOpcionConPiezasODT == null) {
			rbtOpcionConPiezasODT = new JRadioButton();
			rbtOpcionConPiezasODT.setText("Con Piezas ODT");
		}
		rbtOpcionConPiezasODT.setSelected(true);
		return rbtOpcionConPiezasODT;
	}

	private JRadioButton getRbtOpcionSinPiezasODT() {
		if (rbtOpcionSinPiezasODT == null) {
			rbtOpcionSinPiezasODT = new JRadioButton();
			rbtOpcionSinPiezasODT.setText("Sin Piezas ODT");
		}
		return rbtOpcionSinPiezasODT;
	}

	private JPanel getPanelDatosCliente() {
		if(panelDatosCliente == null){
			panelDatosCliente = new JPanel();
			panelDatosCliente.setLayout(new GridBagLayout());
			panelDatosCliente.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
			panelDatosCliente.add(new JLabel("Señor/es: "), GenericUtils.createGridBagConstraints(0, 0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelDatosCliente.add(getTxtRazonSocial(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 3, 1, 1, 0));
			panelDatosCliente.add(new JLabel("Direccion: "), GenericUtils.createGridBagConstraints(0, 1,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelDatosCliente.add(getTxtDireccion(), GenericUtils.createGridBagConstraints(1, 1,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panelDatosCliente.add(new JLabel("Localidad: "), GenericUtils.createGridBagConstraints(2, 1,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelDatosCliente.add(getTxtLocalidad(), GenericUtils.createGridBagConstraints(3, 1,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
		}
		return panelDatosCliente;
	}

	private JTextField getTxtLocalidad() {
		if(txtLocalidad == null) {
			txtLocalidad = new JTextField();
			txtLocalidad.setEditable(false);
		}
		return txtLocalidad;
	}

	private JTextField getTxtDireccion() {
		if(txtDireccion == null) {
			txtDireccion = new JTextField();
			txtDireccion.setEditable(false);
		}
		return txtDireccion;
	}

	private JPanel getPanelDatosFactura() {
		if(panelDatosFactura == null){
			panelDatosFactura = new JPanel();
			panelDatosFactura.setLayout(new GridBagLayout());
			panelDatosFactura.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
			panelDatosFactura.add(new JLabel("IVA: "), GenericUtils.createGridBagConstraints(0, 0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelDatosFactura.add(getTxtCondicionIVA(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panelDatosFactura.add(new JLabel("C.U.I.T: "), GenericUtils.createGridBagConstraints(2, 0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelDatosFactura.add(getTxtCuit(), GenericUtils.createGridBagConstraints(3, 0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panelDatosFactura.add(new JLabel("Condicion de venta: "), GenericUtils.createGridBagConstraints(0, 1,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelDatosFactura.add(getCmbCondicionVenta(), GenericUtils.createGridBagConstraints(1, 1,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panelDatosFactura.add(new JLabel("Remito Nº: "), GenericUtils.createGridBagConstraints(2, 1,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelDatosFactura.add(getTxtNroRemito(), GenericUtils.createGridBagConstraints(3, 1,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));

		}
		return panelDatosFactura;
	}

	private JComboBox getCmbCondicionVenta() {
		if(cmbCondicionVenta == null) {
			cmbCondicionVenta = new JComboBox();
			List<CondicionDeVenta> condicionDeVentaList = getCondicionDeVentaFacade().getAllOrderByName();
			GuiUtil.llenarCombo(cmbCondicionVenta, condicionDeVentaList, true);
			if(remitoEntrada.getId() != null) {
				cmbCondicionVenta.setSelectedItem(remitoEntrada.getCondicionDeVenta());
			}
		}
		return cmbCondicionVenta;
	}

	private JTextField getTxtCuit() {
		if(txtCUIT == null) {
			txtCUIT = new JTextField();
			txtCUIT.setEditable(false);
		}
		return txtCUIT;
	}

	private JTextField getTxtCondicionIVA() {
		if(txtCondicionIVA == null) {
			txtCondicionIVA = new JTextField();
			txtCondicionIVA.setEditable(false);
		}
		return txtCondicionIVA;
	}

	private JButton getBtnSelProductos() {
		if(btnSelProductos == null) {
			btnSelProductos = new JButton("PRODUCTOS: ");
			btnSelProductos.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					JDialogSeleccionarProducto dialogSeleccionarProducto = null;
					if(remitoEntrada.getArticuloStock() == null) { //Es un remito de entrada normal 
						dialogSeleccionarProducto = new JDialogSeleccionarProducto(JDialogCompletarPiezasConODTRemitoEntrada.this, remitoEntrada.getCliente(), remitoEntrada.getProductoArticuloList());
					} else { //Es un remito de entrada 01
						dialogSeleccionarProducto = new JDialogSeleccionarProducto(JDialogCompletarPiezasConODTRemitoEntrada.this, remitoEntrada.getCliente(), remitoEntrada.getProductoArticuloList(), Collections.singletonList(remitoEntrada.getArticuloStock()));
					}
					GuiUtil.centrar(dialogSeleccionarProducto);
					dialogSeleccionarProducto.setVisible(true);
					if(dialogSeleccionarProducto.isAcepto()) {
						remitoEntrada.getProductoArticuloList().clear();
						List<ProductoArticulo> productoSelectedList = dialogSeleccionarProducto.getProductoSelectedList();
						remitoEntrada.getProductoArticuloList().addAll(productoSelectedList);
						getTxtProductos().setText(StringUtil.getCadena(productoSelectedList, ", "));
					}
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

	private FWJTextField getTxtPesoTotal() {
		if(txtPesoTotal == null) {
			txtPesoTotal = new FWJTextField();
		}
		return txtPesoTotal;
	}

	private FWDateField getTxtFechaEmision() {
		if(txtFechaEmision == null) {
			txtFechaEmision = new FWDateField();
			if(remitoEntrada.getId() != null) {
				txtFechaEmision.setFecha(remitoEntrada.getFechaEmision());
			}
			txtFechaEmision.setEditable(false);
		}
		return txtFechaEmision;
	}

	private FWJNumericTextField getTxtNroRemito() {
		if(txtNroRemito == null) {
			txtNroRemito = new FWJNumericTextField(new Long(0), Long.MAX_VALUE);
			if(remitoEntrada.getId() != null) {
				getTxtNroRemito().setText(remitoEntrada.getNroRemito().toString());
			}
		}
		txtNroRemito.setEditable(false);
		return txtNroRemito;
	}

	private FWJTextField getTxtRazonSocial() {
		if(txtRazonSocial == null) {
			txtRazonSocial = new FWJTextField(MAX_LONGITUD_RAZ_SOCIAL);
			txtRazonSocial.setEditable(false);
		}
		return txtRazonSocial;
	}

	private JPanel getPanelBotones() {
		if(pnlBotones == null){
			pnlBotones = new JPanel();
			pnlBotones.setLayout(new FlowLayout(FlowLayout.CENTER));
			pnlBotones.add(getBtnAceptar());
			pnlBotones.add(getBtnCancelar());
			
			getBtnCancelar().setEnabled(true);
		}
		return pnlBotones;
	}

	
	private JButton getBtnCancelar() {
		if(btnCancelar == null) {
			btnCancelar = new JButton("Cancelar");
			btnCancelar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					setRemitoEntrada(null);
					dispose();
				}

			});

		}
		return btnCancelar;
	}

	private JButton getBtnAceptar() {
		if(btnAceptar == null) {
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if(validar()) {
						List<OrdenDeTrabajo> odtCapturedList = capturarSetearDatos();
						getRemitoEntradaFacade().completarPiezasRemitoEntrada(remitoEntrada, odtCapturedList, GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName());
						FWJOptionPane.showInformationMessage(JDialogCompletarPiezasConODTRemitoEntrada.this, "El remito se ha grabado con éxito.", "Atención");
						dispose();
					} 
				}

			});
		}
		return btnAceptar;
	}

	private void setRemitoEntrada(RemitoEntrada remitoEntradaSaved) {
		this.remitoEntrada = remitoEntradaSaved;
	}

	private RemitoEntradaFacadeRemote getRemitoEntradaFacade() {
		if(remitoEntradaFacade == null) {
			remitoEntradaFacade = GTLBeanFactory.getInstance().getBean2(RemitoEntradaFacadeRemote.class);
		}
		return remitoEntradaFacade;
	}

	private CondicionDeVentaFacadeRemote getCondicionDeVentaFacade() {
		if(condicionDeVentaFacade == null) {
			condicionDeVentaFacade = GTLBeanFactory.getInstance().getBean2(CondicionDeVentaFacadeRemote.class);
		}
		return condicionDeVentaFacade;
	}

	private List<OrdenDeTrabajo> capturarSetearDatos() {
		remitoEntrada.setFechaEmision(getTxtFechaEmision().getFecha());
		BigDecimal pesoTotal = new BigDecimal(getTxtPesoTotal().getText().trim().replace(',', '.'));
		remitoEntrada.setPesoTotal(pesoTotal);
		remitoEntrada.setCondicionDeVenta((CondicionDeVenta)getCmbCondicionVenta().getSelectedItem());
		remitoEntrada.setNroRemito(getTxtNroRemito().getValue());
		getPanTablaPieza().capturarSetearDatos();
		return getPanTablaPieza().getODTs();
	}

	private boolean validar() {
		String pesoTotalStr = getTxtPesoTotal().getText();
		if(StringUtil.isNullOrEmpty(pesoTotalStr) || !GenericUtils.esNumerico(pesoTotalStr)) {
			FWJOptionPane.showErrorMessage(JDialogCompletarPiezasConODTRemitoEntrada.this, "Debe ingresar un peso total válido.", "Error");
			getTxtPesoTotal().requestFocus();
			return false;
		}
		Integer nroRemito = getTxtNroRemito().getValue();
		if(nroRemito == null || nroRemito <= 0) {
			FWJOptionPane.showErrorMessage(JDialogCompletarPiezasConODTRemitoEntrada.this, "Debe ingresar un número de remito.", "Error");
			getTxtNroRemito().requestFocus();
			return false;
		}
		if(!esRemitoEntrada01() && getCmbCondicionVenta().getSelectedIndex() == -1) {
			FWJOptionPane.showErrorMessage(JDialogCompletarPiezasConODTRemitoEntrada.this, "Debe seleccionar una condición de venta.", "Error");
			return false;
		}
		if(getRbtOpcionConPiezasODT().isSelected() && StringUtil.isNullOrEmpty(getTxtProductos().getText())) {
			FWJOptionPane.showErrorMessage(JDialogCompletarPiezasConODTRemitoEntrada.this, "Debe seleccionar al menos un producto.", "Error");
			return false;
		}
		String msgValidacionPiezas = getPanTablaPieza().validar();
		if(msgValidacionPiezas != null) {
			FWJOptionPane.showErrorMessage(JDialogCompletarPiezasConODTRemitoEntrada.this, StringW.wordWrap(msgValidacionPiezas), "Error");
			return false;
		}
		return true;
	}

	private boolean esRemitoEntrada01() {
		return remitoEntrada.getArticuloStock() != null;
	}

	private PanelTablaPieza getPanTablaPieza() {
		if(panTablaPieza == null) {
			panTablaPieza = new PanelTablaPieza(remitoEntrada);
		}
		return panTablaPieza;
	}

	private class ODTSelectedAction implements Action {
		
		private OrdenDeTrabajo odt;
		
		public ODTSelectedAction(OrdenDeTrabajo odt) {
			this.odt = odt;
		}

		public void addPropertyChangeListener(PropertyChangeListener listener) {
		}

		public Object getValue(String key) {
			return null;
		}

		public boolean isEnabled() {
			return true;
		}

		public void putValue(String key, Object value) {
		}

		public void removePropertyChangeListener(PropertyChangeListener listener) {
		}

		public void setEnabled(boolean b) {
		}

		public void actionPerformed(ActionEvent e) {
			if(odt!=null){
				getPanTablaPieza().asignarODT(odt);
			}else{
				getPanTablaPieza().getBtnSelODTs().doClick();
			}
		}

		public String toString() {
			return odt.toString();
		}

	}

	private class PanelTablaPieza extends PanelTabla<PiezaRemito> {

		private static final long serialVersionUID = 1L;

		private static final int CANT_COLS = 5;
		private static final int COL_NRO_PIEZA = 0;
		private static final int COL_METROS_PIEZA = 1;
		private static final int COL_OBSERVACIONES = 2;
		private static final int COL_ODT = 3;
		private static final int COL_OBJ = 4;

		private static final int CANT_PIEZAS_INICIALES = 30;
		private final int CANT_FILAS_MAX = GenericUtils.isSistemaTest() ? 48 : 53;

		private JButton btnSelODTs;
		
		private RemitoEntrada remitoEntrada;
		private Map<PiezaRemito, OrdenDeTrabajo> odtPiezaMap;

		public PanelTablaPieza(RemitoEntrada remitoEntrada) {
			agregarBoton(getBtnSelODTs());
			initializePopupMenu();
			this.remitoEntrada = remitoEntrada;
			this.odtPiezaMap = new HashMap<PiezaRemito, OrdenDeTrabajo>();
			if(remitoEntrada.getPiezas().isEmpty()) {
				addRowsInTabla(CANT_PIEZAS_INICIALES);
			} else {
				agregarElementos(remitoEntrada.getPiezas());
			}
		}

		public List<OrdenDeTrabajo> getODTs() {
			Set<OrdenDeTrabajo> odtSet = new HashSet<OrdenDeTrabajo>();
			for(int i = 0; i < getTabla().getRowCount(); i++) {
				PiezaRemito elemento = getElemento(i);
				String metrosStr = (String)getTabla().getValueAt(i, COL_METROS_PIEZA);
				OrdenDeTrabajo odt = (OrdenDeTrabajo)getTabla().getValueAt(i, COL_ODT);
				if(metrosStr != null && !metrosStr.equals("0")) {
					if(odt != null) {
						elemento.setMetros(new BigDecimal(metrosStr));
						if(odt.getId() == null) {
							odt.setRemito(remitoEntrada);
						}
						if(noExistePieza(elemento, odt)) {
							PiezaODT piezaODT = new PiezaODT();
							piezaODT.setPiezaRemito(elemento);
							odt.getPiezas().add(piezaODT);
						}
						odtSet.add(odt);
					} else {
						elemento.setPiezaSinODT(true);
					}
				}
				elemento.setObservaciones((String)getTabla().getValueAt(i, COL_OBSERVACIONES));
			}
			return new ArrayList<OrdenDeTrabajo>(odtSet);
		}

		private boolean noExistePieza(PiezaRemito elemento, OrdenDeTrabajo odt) {
			for(PiezaODT podt : odt.getPiezas()) {
				if(podt.getPiezaRemito() != null && podt.getPiezaRemito().equals(elemento)) {
					return false;
				}
			}
			return true;
		}

		private JButton getBtnSelODTs() {
			if(btnSelODTs == null) {
				btnSelODTs = new JButton("ODT");
				btnSelODTs.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e) {
						JDialogSeleccionarCrearODT dialogSeleccionarCrearODT = new JDialogSeleccionarCrearODT(owner, remitoEntrada.getProductoArticuloList(), odtList);
						GuiUtil.centrarEnFramePadre(dialogSeleccionarCrearODT);
						dialogSeleccionarCrearODT.setVisible(true);
						if(dialogSeleccionarCrearODT.isAcepto()){
							OrdenDeTrabajo selectedODT = dialogSeleccionarCrearODT.getSelectedODT();
							List<OrdenDeTrabajo> odtListAgregadas = dialogSeleccionarCrearODT.getOdtList();
							getMenuODT().removeAll();
							for(OrdenDeTrabajo odt : odtListAgregadas) {
								if(!odtList.contains(odt)) {
									odtList.add(odt);
								}
								agregarODTEnMenu(odt);
							}
							asignarODT(selectedODT);
							ODTSelectedAction odtAction = new ODTSelectedAction(null);
							JMenuItem menuItem = new JMenuItem(odtAction);
							menuItem.setText("Crear ODT");
							getMenuODT().add(menuItem);
						}
					}
				});
			}
			return btnSelODTs;
		}

		private void agregarODTEnMenu(OrdenDeTrabajo odt) {
			ODTSelectedAction odtAction = new ODTSelectedAction(odt);
			JMenuItem menuItem = new JMenuItem(odtAction);
			menuItem.setText(odt.toString());
			getMenuODT().add(menuItem);
		}

		public void asignarODT(OrdenDeTrabajo selectedODT) {
			int[] selectedRows = getTabla().getSelectedRows();
			for(int row : selectedRows) {
				getTabla().setValueAt(selectedODT, row, COL_ODT);
				odtPiezaMap.put((PiezaRemito)getTabla().getValueAt(row, COL_OBJ), selectedODT);
			}
		}

		public String validar() {
			String ret = null;
			for(int i = 0; i < getTabla().getRowCount(); i++) {
				String metrosStr = (String)getTabla().getValueAt(i, COL_METROS_PIEZA);
				OrdenDeTrabajo odt = (OrdenDeTrabajo)getTabla().getValueAt(i, COL_ODT);
				if(metrosStr != null && !metrosStr.equals("0")) {
					if(odt == null && !getRbtOpcionSinPiezasODT().isSelected()) {
						ret = "Debe cargar las ODT";
						break;
					}
				}
			}
			return ret;
		}

		public void capturarSetearDatos() {
			table2Objects();
			List<PiezaRemito> piezasToRemove = new ArrayList<PiezaRemito>();
			for(PiezaRemito pe : remitoEntrada.getPiezas()) {
				if(pe.getMetros() == null || pe.getMetros().equals(new BigDecimal(0))) {
					piezasToRemove.add(pe);
				}
			}
			remitoEntrada.getPiezas().removeAll(piezasToRemove);
			remitoEntrada.recalcularOrdenes();
		}
		
		@Override
		protected void agregarElemento(PiezaRemito elemento) {
			Object[] row = getRow(elemento);
			getTabla().addRow(row);
		}

		private Object[] getRow(PiezaRemito elemento) {
			String nroPieza = null;
			nroPieza = elemento.getOrdenPieza().toString();
			Object[] row = new Object[CANT_COLS];
			row[COL_NRO_PIEZA] = nroPieza;
			row[COL_METROS_PIEZA] = elemento.getMetros() == null ? null : elemento.getMetros().toString();
			row[COL_OBSERVACIONES] = elemento.getObservaciones();
			row[COL_ODT] = getODTFromPieza(elemento);
			row[COL_OBJ] = elemento;
			return row;
		}

		private OrdenDeTrabajo getODTFromPieza(PiezaRemito pr) {
			if(pr.getId() == null || pr.getId() == 0) {
				return odtPiezaMap.get(pr);
			}
			for(Entry<PiezaRemito, OrdenDeTrabajo> entry : odtPiezaMap.entrySet()) {
				if(entry.getKey().getId().equals(pr.getId())) {
					return entry.getValue();
				}
			}
			return null;
		}
		
		@SuppressWarnings("serial")
		@Override
		protected FWJTable construirTabla() {
			FWJTable tablaPiezaEntrada = new FWJTable(0, CANT_COLS) {

				@Override
				public void cellEdited(int cell, int row) {
					if(cell == COL_METROS_PIEZA) {
						String metrosStr = (String)getTabla().getValueAt(row, COL_METROS_PIEZA);
						if(!StringUtil.isNullOrEmpty(metrosStr)) {
							table2Objects();
							actualizarTotales();
						}
					}
				}

			};
			tablaPiezaEntrada.setStringColumn(COL_NRO_PIEZA, "NUMERO", 80, 80, true);
			tablaPiezaEntrada.setFloatColumn(COL_METROS_PIEZA, "METROS", 80, false);
			tablaPiezaEntrada.setStringColumn(COL_OBSERVACIONES, "OBSERVACIONES", 205, 205, false);
			tablaPiezaEntrada.setStringColumn(COL_ODT, "ODT", 220, 220, true);
			tablaPiezaEntrada.setStringColumn(COL_OBJ, "", 0, 0, true);

			tablaPiezaEntrada.addMouseListener(new MouseAdapter() {

				@Override
				public void mousePressed(MouseEvent e) {
					handleMouseEvent(e);
				}

				@Override
				public void mouseReleased(MouseEvent e) {
					handleMouseEvent(e);
				}

				private void handleMouseEvent(MouseEvent e) {
					if(e.isPopupTrigger()) {
						getMenuItemEliminarFilas().setEnabled(getTabla().getSelectedRow() != -1);
						getComponentPopupMenu().show(e.getComponent(), e.getX(), e.getY());
					}
				}

			});

			return tablaPiezaEntrada;
		}

		private void initializePopupMenu() {
			setComponentPopupMenu(new JPopupMenu());
			getComponentPopupMenu().add(getMenuItemAgregarPiezas());
			getComponentPopupMenu().add(getMenuItemEliminarFilas());
			getComponentPopupMenu().add(getMenuODT());
		}

		private JMenuItem getMenuItemEliminarFilas() {
			if(menuItemEliminarFilas == null) {
				menuItemEliminarFilas = new JMenuItem("Eliminar Fila(s)");
				menuItemEliminarFilas.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						getBotonEliminar().doClick();
					}
				});
			}
			return menuItemEliminarFilas;
		}

		private JMenuItem getMenuItemAgregarPiezas() {
			if(menuItemAgregarPiezas == null) {
				menuItemAgregarPiezas = new JMenuItem("Agregar Pieza(s)");
				menuItemAgregarPiezas.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						getBotonAgregar().doClick();
					}
				});
			}
			return menuItemAgregarPiezas;
		}

		private void actualizarTotales() {
			BigDecimal tm = new BigDecimal(0);
			int piezas = 0;
			for(PiezaRemito pe : remitoEntrada.getPiezas()) {
				tm = tm.add(pe.getTotalMetros());
				if(piezaValida(pe)) {
					piezas++;
				}
			}
			getTxtMetros().setText(tm.toString());
			getTxtPiezas().setText(String.valueOf(piezas));
		}

		private boolean piezaValida(PiezaRemito pe) {
			BigDecimal cero = new BigDecimal(0);
			return pe.getMetros() != null && pe.getMetros().compareTo(cero) > 0;
		}

		
		
		@Override
		protected void botonQuitarPresionado() {
			getTabla().setNumRows(0);
			agregarElementos(remitoEntrada.getPiezas());
			actualizarTotales();
		}

		@Override
		public boolean validarAgregar() {
			JDialogCantFilasInput dialogCantFilasInput = new JDialogCantFilasInput(JDialogCompletarPiezasConODTRemitoEntrada.this, "Cantidad de Piezas");
			GuiUtil.centrarEnPadre(JDialogCompletarPiezasConODTRemitoEntrada.this);			
			GuiUtil.centrarEnPadre(dialogCantFilasInput);
			dialogCantFilasInput.setVisible(true);
			Integer cantFilas = dialogCantFilasInput.getCantFilas();
			if(cantFilas != null) {
				if(getTabla().getRowCount() + cantFilas > CANT_FILAS_MAX) {
					FWJOptionPane.showErrorMessage(JDialogCompletarPiezasConODTRemitoEntrada.this, "La cantidad de piezas debe ser menor a " + CANT_FILAS_MAX, "Error");
				} else {
					addRowsInTabla(cantFilas);
				}
			}
			return false;
		}

		private void addRowsInTabla(Integer cantFilas) {
			for(int i = 0; i < cantFilas; i++) {
				getTabla().addRow();
				PiezaRemito piezaEntrada = new PiezaRemito();
				remitoEntrada.getPiezas().add(piezaEntrada);
				int rowCount = remitoEntrada.getPiezas().size();
				piezaEntrada.setOrdenPieza(rowCount);
				getTabla().setValueAt(piezaEntrada.getOrdenPieza(), rowCount - 1, COL_NRO_PIEZA);
				getTabla().setValueAt(piezaEntrada, rowCount - 1, COL_OBJ);
			}
		}

		@Override
		public boolean validarQuitar() {
			table2Objects();
			int[] selectedRows = getTabla().getSelectedRows();
			List<PiezaRemito> piezaRemitoList = new ArrayList<PiezaRemito>();
			for(int sr : selectedRows) {
				piezaRemitoList.add(getElemento(sr));
			}
			for(PiezaRemito elemento : piezaRemitoList) {
				remitoEntrada.getPiezas().remove(elemento);

				for(OrdenDeTrabajo odt : odtPiezaMap.values()) {
					List<PiezaODT> podtToBorrarList = new ArrayList<PiezaODT>();
					for(PiezaODT podt : odt.getPiezas()) {
						if(podt.getPiezaRemito() != null && podt.getPiezaRemito().equals(elemento)) {
							podtToBorrarList.add(podt);
						}
					}
					odt.getPiezas().removeAll(podtToBorrarList);
				}
				
				odtPiezaMap.remove(elemento);
			}
			remitoEntrada.recalcularOrdenes();
			return true;
		}

		private void table2Objects() {
			for(int i = 0; i < getTabla().getRowCount(); i++) {
				PiezaRemito elemento = getElemento(i);
				String metrosStr = (String)getTabla().getValueAt(i, COL_METROS_PIEZA);
				if(metrosStr != null) {
					elemento.setMetros(new BigDecimal(metrosStr));
				}
				elemento.setObservaciones((String)getTabla().getValueAt(i, COL_OBSERVACIONES));
			}
		}

		@Override
		protected PiezaRemito getElemento(int fila) {
			return (PiezaRemito)getTabla().getValueAt(fila, COL_OBJ);
		}

		@Override
		protected String validarElemento(int fila) {
			return null;
		}

	}

	private JMenu getMenuODT() {
		if(menuODT == null) {
			menuODT = new JMenu("ODTs");
			ODTSelectedAction odtAction = new ODTSelectedAction(null);
			JMenuItem menuItem = new JMenuItem(odtAction);
			menuItem.setText("Crear ODT");
			menuODT.add(menuItem);
		}
		return menuODT;
	}

}