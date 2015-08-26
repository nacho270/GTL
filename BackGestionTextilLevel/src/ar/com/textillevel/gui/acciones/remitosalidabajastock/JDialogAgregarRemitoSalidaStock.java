package ar.com.textillevel.gui.acciones.remitosalidabajastock;

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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import main.GTLGlobalCache;

import org.apache.taglibs.string.util.StringW;

import ar.clarin.fwjava.componentes.CLDateField;
import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.CLJTable;
import ar.clarin.fwjava.componentes.CLJTextField;
import ar.clarin.fwjava.componentes.PanelTabla;
import ar.clarin.fwjava.componentes.error.validaciones.ValidacionException;
import ar.clarin.fwjava.util.DateUtil;
import ar.clarin.fwjava.util.GuiUtil;
import ar.clarin.fwjava.util.StringUtil;
import ar.com.textillevel.entidades.documentos.remito.PiezaRemito;
import ar.com.textillevel.entidades.documentos.remito.RemitoEntrada;
import ar.com.textillevel.entidades.documentos.remito.RemitoSalida;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.to.remitosalida.PiezaRemitoSalidaTO;
import ar.com.textillevel.entidades.to.remitosalida.RemitoSalidaConBajaStockTO;
import ar.com.textillevel.entidades.ventas.articulos.Articulo;
import ar.com.textillevel.entidades.ventas.productos.Producto;
import ar.com.textillevel.facade.api.remote.ParametrosGeneralesFacadeRemote;
import ar.com.textillevel.facade.api.remote.RemitoSalidaFacadeRemote;
import ar.com.textillevel.gui.acciones.JDialogSeleccionarCrearODT;
import ar.com.textillevel.gui.acciones.JDialogSeleccionarProducto;
import ar.com.textillevel.gui.acciones.impresionremito.ImprimirRemitoHandler;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;
import ar.com.textillevel.modulos.odt.entidades.PiezaODT;
import ar.com.textillevel.modulos.odt.facade.api.remote.OrdenDeTrabajoFacadeRemote;
import ar.com.textillevel.util.GTLBeanFactory;
import ar.com.textillevel.util.ODTCodigoHelper;

public class JDialogAgregarRemitoSalidaStock extends JDialog {

	private static final long serialVersionUID = 1L;

	private static final int MAX_LONGITUD_RAZ_SOCIAL = 50;

	private JPanel panDetalle;
	private CLJTextField txtRazonSocial;
	private PanelTablaPieza panTablaPieza;
	private JPanel pnlBotones;
	private JButton btnAceptar;
	private JButton btnCancelar;

	private JTextField txtNroRemito;
	private CLDateField txtFechaEmision;
	private JTextField txtRemitosEntrada;
	private JTextField txtCodODT;
	private CLJTextField txtPesoTotal;
	private JTextField txtProductos;
	private RemitoSalida remitoSalida;
	private JPanel panTotales; 
	private JTextField txtMetros;
	private JTextField txtPiezas;
	private JMenuItem menuItemEliminarFilas;
	private JMenuItem menuItemAgregarPiezas;

	private JPanel panelDatosCliente; 
	private JTextField txtLocalidad;
	private JTextField txtDireccion;
	private JPanel panelDatosFactura;
	private JTextField txtCondicionVenta;
	private JTextField txtCUIT;
	private JTextField txtCondicionIVA;

	private RemitoSalidaFacadeRemote remitoSalidaFacade;
	private ParametrosGeneralesFacadeRemote parametrosGeneralesFacade;

	private JButton btnSelProductos;
	private JMenu menuODT;
	private List<OrdenDeTrabajo> odtList;
	private List<Producto> productoList;
	private Frame owner;

	private List<RemitoEntrada> remitoEntradaList;
	
	public JDialogAgregarRemitoSalidaStock(Frame owner, RemitoSalida remitoSalida) {
		super(owner);
		this.owner = owner; 
		this.remitoSalida = remitoSalida;
		this.odtList = new ArrayList<OrdenDeTrabajo>();
		this.productoList = new ArrayList<Producto>();
		this.remitoEntradaList = new ArrayList<RemitoEntrada>();
		setSize(new Dimension(720, 750));
		setTitle("Alta de Remito de Salida - Baja de Stock");
		construct();
		setDatos();
		setModal(true);
	}

	private void setDatos() {
		Cliente cliente = remitoSalida.getCliente();
		getTxtRazonSocial().setText(cliente.getRazonSocial());
		getTxtFechaEmision().setFecha(DateUtil.getHoy());
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
		getTxtNroRemito().setText(remitoSalida.getNroRemito().toString());
		List<OrdenDeTrabajo> odts = remitoSalida.getOdts();
		if(!odts.isEmpty()) {
			getTxtCondicionVenta().setText(odts.get(0).getRemito().getCondicionDeVenta().getNombre());
		}
		getTxtCodODT().setText(StringUtil.getCadena(extractCodigos(odts), ", "));
		Set<Producto> productoList = new HashSet<Producto>();
		for(OrdenDeTrabajo odt : odts) {
			productoList.add(odt.getProducto());
		}
		getTxtProductos().setText(StringUtil.getCadena(productoList, ", "));
		getTxtRemitosEntrada().setText(StringUtil.getCadena(extractRemitosEntrada(odts), ", "));
	}

	private Set<String> extractRemitosEntrada(List<OrdenDeTrabajo> odts) {
		Set<String> nroRemitoEntradaList = new HashSet<String>();
		for(OrdenDeTrabajo odt : odts) {
			nroRemitoEntradaList.add(String.valueOf(odt.getRemito().getNroRemito()));
		}
		return nroRemitoEntradaList;
	}

	private List<String> extractCodigos(List<OrdenDeTrabajo> odts) {
		List<String> codODTList = new ArrayList<String>();
		for(OrdenDeTrabajo odt : odts) {
			codODTList.add(ODTCodigoHelper.getInstance().formatCodigo(odt.getCodigo()));
		}
		return codODTList;
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
		}
		return txtPiezas;
	}

	private JTextField getTxtMetros() {
		if(txtMetros == null) {
			txtMetros = new JTextField();
			txtMetros.setEditable(false);
		}
		return txtMetros;
	}

	private JPanel getPanDetalle() {
		if(panDetalle == null) {
			panDetalle = new JPanel();
			panDetalle.setLayout(new GridBagLayout());
			panDetalle.add(getPanelDatosCliente(), GenericUtils.createGridBagConstraints(0, 0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 6, 1, 0, 0));

			panDetalle.add(getPanelDatosFactura(), GenericUtils.createGridBagConstraints(0, 1,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 6, 1, 0, 0));

			panDetalle.add(new JLabel("ODT(s):"), GenericUtils.createGridBagConstraints(0, 2,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getTxtCodODT(), GenericUtils.createGridBagConstraints(1, 2, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0.5, 0));

			panDetalle.add(new JLabel(" PESO TOTAL:"), GenericUtils.createGridBagConstraints(2, 2,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getTxtPesoTotal(), GenericUtils.createGridBagConstraints(3, 2, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0.5, 0));

			panDetalle.add(new JLabel(" FECHA:"), GenericUtils.createGridBagConstraints(4, 2,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getTxtFechaEmision(), GenericUtils.createGridBagConstraints(5, 2, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0.5, 0));

			panDetalle.add(getBtnSelProductos(), GenericUtils.createGridBagConstraints(0, 3,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getTxtProductos(), GenericUtils.createGridBagConstraints(1, 3, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 5, 1, 1, 0));

			panDetalle.add(getPanTablaPieza(), GenericUtils.createGridBagConstraints(0, 4, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 6, 1, 1, 1));
		}
		return panDetalle;
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
			panelDatosFactura.add(getTxtCondicionVenta(), GenericUtils.createGridBagConstraints(1, 1,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panelDatosFactura.add(new JLabel("Remito Nº: "), GenericUtils.createGridBagConstraints(2, 1,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelDatosFactura.add(getTxtNroRemito(), GenericUtils.createGridBagConstraints(3, 1,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panelDatosFactura.add(new JLabel("Remito(s) de Entrada: "), GenericUtils.createGridBagConstraints(0, 2,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelDatosFactura.add(getTxtRemitosEntrada(), GenericUtils.createGridBagConstraints(1, 2,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 3, 1, 1, 0));
		}
		return panelDatosFactura;
	}

	private JTextField getTxtCondicionVenta() {
		if(txtCondicionVenta == null) {
			txtCondicionVenta = new JTextField();
			txtCondicionVenta.setEditable(false);
		}
		return txtCondicionVenta;
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

	private JTextField getTxtProductos() {
		if(txtProductos == null) {
			txtProductos = new JTextField();
			txtProductos.setEditable(false);
			txtProductos.setText(StringUtil.getCadena(remitoSalida.getProductoList(), ", "));
		}
		return txtProductos;
	}

	private JButton getBtnSelProductos() {
		if(btnSelProductos == null) {
			btnSelProductos = new JButton("PRODUCTOS:");
			btnSelProductos.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					JDialogSeleccionarProducto dialogSeleccionarProducto = new JDialogSeleccionarProducto(JDialogAgregarRemitoSalidaStock.this, remitoSalida.getCliente(), productoList, getArticuloListFromRemitos());
					GuiUtil.centrar(dialogSeleccionarProducto);
					dialogSeleccionarProducto.setVisible(true);
					if(dialogSeleccionarProducto.isAcepto()) {
						List<Producto> productoSelectedList = dialogSeleccionarProducto.getProductoSelectedList();
						getTxtProductos().setText(StringUtil.getCadena(productoSelectedList, ", "));
						productoList.clear();
						productoList.addAll(productoSelectedList);
						getPanTablaPieza().setEnabledBtnODT(true);
					}
				}

			});

			btnSelProductos.setEnabled(false);
		}
		return btnSelProductos;
	}

	private List<Articulo> getArticuloListFromRemitos() {
		Set<Articulo> articuloSet = new HashSet<Articulo>();
		for(RemitoEntrada re : remitoEntradaList) {
			articuloSet.add(re.getArticuloStock());
		}
		return new ArrayList<Articulo>(articuloSet);
	}
	
	private CLJTextField getTxtPesoTotal() {
		if(txtPesoTotal == null) {
			txtPesoTotal = new CLJTextField();
		}
		return txtPesoTotal;
	}

	private JTextField getTxtCodODT() {
		if(txtCodODT == null) {
			txtCodODT = new JTextField();
			txtCodODT.setEditable(false);
		}
		return txtCodODT;
	}

	private CLDateField getTxtFechaEmision() {
		if(txtFechaEmision == null) {
			txtFechaEmision = new CLDateField();
		}
		return txtFechaEmision;
	}

	private JTextField getTxtNroRemito() {
		if(txtNroRemito == null) {
			txtNroRemito = new CLJTextField();
			txtNroRemito.setEditable(false);
		}
		return txtNroRemito;
	}

	private CLJTextField getTxtRazonSocial() {
		if(txtRazonSocial == null) {
			txtRazonSocial = new CLJTextField(MAX_LONGITUD_RAZ_SOCIAL);
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
		}
		return pnlBotones;
	}

	
	private JButton getBtnCancelar() {
		if(btnCancelar == null) {
			btnCancelar = new JButton("Cancelar");
			btnCancelar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					setRemitoSalida(null);
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
						RemitoSalidaConBajaStockTO remitoSalidaTO = new RemitoSalidaConBajaStockTO();
						remitoSalidaTO.getPiezasRemitoSalidaTO().addAll(capturarSetearDatos());
						remitoSalidaTO.getRemitoEntradaModificadosList().addAll(remitoEntradaList);
						remitoSalidaTO.setUserName(GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName());
						remitoSalidaTO.setRemitoSalida(remitoSalida);

						RemitoSalida remitoSalidaSaved;
						try {
							remitoSalidaSaved = getRemitoSalidaFacade().ingresarRemitoSalidaPorSalida01(remitoSalidaTO);
							setRemitoSalida(remitoSalidaSaved);
							CLJOptionPane.showInformationMessage(JDialogAgregarRemitoSalidaStock.this, "El remito se ha grabado con éxito", "Información");
							if(CLJOptionPane.showQuestionMessage(JDialogAgregarRemitoSalidaStock.this, "¿Desea imprimir el remito?", "Confirmación") == CLJOptionPane.YES_OPTION) {
								ImprimirRemitoHandler imprimirRemitoHandler = new ImprimirRemitoHandler(remitoSalidaSaved, getParametrosGeneralesFacade().getParametrosGenerales().getNroSucursal(), JDialogAgregarRemitoSalidaStock.this);
								imprimirRemitoHandler.imprimir();
							}
							dispose();
						} catch (ValidacionException e1) {
							CLJOptionPane.showErrorMessage(JDialogAgregarRemitoSalidaStock.this, StringW.wordWrap(e1.getMensajeError()), "Error");
						}
					} 
				}

			});
		}
		return btnAceptar;
	}

	private void setRemitoSalida(RemitoSalida remitoSalidaSaved) {
		this.remitoSalida = remitoSalidaSaved;
	}

	public RemitoSalida getRemitoSalida() {
		return remitoSalida;
	}

	private RemitoSalidaFacadeRemote getRemitoSalidaFacade() {
		if(remitoSalidaFacade == null) {
			remitoSalidaFacade = GTLBeanFactory.getInstance().getBean2(RemitoSalidaFacadeRemote.class);
		}
		return remitoSalidaFacade;
	}

	private List<PiezaRemitoSalidaTO> capturarSetearDatos() {
		remitoSalida.setFechaEmision(getTxtFechaEmision().getFecha());
		BigDecimal pesoTotal = new BigDecimal(getTxtPesoTotal().getText().trim().replace(',', '.'));
		remitoSalida.setPesoTotal(pesoTotal);
		return getPanTablaPieza().capturarSetearDatos();
	}

	private boolean validar() {
		String pesoTotalStr = getTxtPesoTotal().getText();
		if(StringUtil.isNullOrEmpty(pesoTotalStr) || !GenericUtils.esNumerico(pesoTotalStr)) {
			CLJOptionPane.showErrorMessage(JDialogAgregarRemitoSalidaStock.this, "Debe ingresar un peso total válido.", "Error");
			getTxtPesoTotal().requestFocus();
			return false;
		}
		if(getPanTablaPieza().getTabla().getRowCount() == 0) {
			CLJOptionPane.showErrorMessage(JDialogAgregarRemitoSalidaStock.this, "Debe ingresar al menos una pieza", "Error");
			return false;
		}
		String msgValidacionPiezas = getPanTablaPieza().validarElementos();
		if(msgValidacionPiezas != null) {
			CLJOptionPane.showErrorMessage(JDialogAgregarRemitoSalidaStock.this, StringW.wordWrap(msgValidacionPiezas), "Error");
			return false;
		}
		boolean existRepeatedValues = getPanTablaPieza().getTabla().existRepeatedValues(new int[]{0});
		if(existRepeatedValues) {
			CLJOptionPane.showErrorMessage(JDialogAgregarRemitoSalidaStock.this, "Los números de piezas deben ser distintos.", "Error");
			return false;
		}
		return true;
	}

	private PanelTablaPieza getPanTablaPieza() {
		if(panTablaPieza == null) {
			panTablaPieza = new PanelTablaPieza(remitoSalida);
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

		private static final int CANT_COLS = 6;
		private static final int COL_NRO_PIEZA = 0;
		private static final int COL_METROS_PIEZA = 1;
		private static final int COL_OBSERVACIONES = 2;
		private static final int COL_ODT = 3;
		private static final int COL_ARTICULO = 4;
		private static final int COL_OBJ = 5;
		private JButton btnSelODTs;
		private Map<PiezaRemito, OrdenDeTrabajo> odtPiezaMap = new HashMap<PiezaRemito, OrdenDeTrabajo>();
		private Map<PiezaRemito, RemitoEntrada> piezaRemitoEntradaMap = new HashMap<PiezaRemito, RemitoEntrada>();

		private OrdenDeTrabajoFacadeRemote odtFacade;

		public PanelTablaPieza(RemitoSalida remitoSalida) {
			initializePopupMenu();
			agregarBoton(getBtnSelODTs());
			habilitarBotonesExtra(true);
			getBtnSelODTs().setEnabled(false);
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
				PiezaRemito pr = (PiezaRemito)getTabla().getValueAt(row, COL_OBJ);
				OrdenDeTrabajo odtAssigned = (OrdenDeTrabajo)getTabla().getValueAt(row, COL_ODT);
				if(odtAssigned != null && odtAssigned.getId() != null) {
					CLJOptionPane.showInformationMessage(JDialogAgregarRemitoSalidaStock.this, "No se puede cambiar la ODT de la pieza de " + pr.getMetros(), "Información");
					continue;
				} else {
					getTabla().setValueAt(selectedODT, row, COL_ODT);
					odtPiezaMap.put(pr, selectedODT);
				}
			}
		}

		public List<PiezaRemitoSalidaTO> capturarSetearDatos() {
			List<PiezaRemitoSalidaTO> piezasResult = new ArrayList<PiezaRemitoSalidaTO>();
			for(int row = 0; row < getTabla().getRowCount(); row++) {
				PiezaRemitoSalidaTO pr = new PiezaRemitoSalidaTO();
				pr.setPiezaRemitoEntrada(getElemento(row));
				OrdenDeTrabajo odt = (OrdenDeTrabajo)getTabla().getValueAt(row, COL_ODT);
				pr.setOdt(odt);
				odt.setRemito(piezaRemitoEntradaMap.get(pr.getPiezaRemitoEntrada()));
				pr.setObservaciones((String)getTabla().getValueAt(row, COL_OBSERVACIONES));
				Integer nroPieza = (Integer)getTabla().getTypedValueAt(row, COL_NRO_PIEZA);
				pr.setNroPieza(nroPieza);
				piezasResult.add(pr);
			}
			return piezasResult;
		}

		@Override
		protected void agregarElemento(PiezaRemito elemento) {
			Object[] row = getRow(elemento);
			getTabla().addRow(row);
		}

		private Object[] getRow(PiezaRemito elemento) {
			Object[] row = new Object[CANT_COLS];
			row[COL_NRO_PIEZA] = null;
			row[COL_METROS_PIEZA] = elemento.getMetros() == null ? null : elemento.getMetros().toString();
			row[COL_OBSERVACIONES] = elemento.getObservaciones();
			row[COL_ARTICULO] = null;
			row[COL_OBJ] = elemento;
			return row;
		}

		@Override
		protected CLJTable construirTabla() {
			CLJTable tablaPiezaEntrada = new CLJTable(0, CANT_COLS);
			tablaPiezaEntrada.setIntColumn(COL_NRO_PIEZA, "NUMERO", 1, 10000, 50, false);
			tablaPiezaEntrada.setFloatColumn(COL_METROS_PIEZA, "METROS", 0, Float.MAX_VALUE, 50, true);
			tablaPiezaEntrada.setStringColumn(COL_OBSERVACIONES, "OBSERVACIONES", 220, 220, false);
			tablaPiezaEntrada.setStringColumn(COL_ODT, "ODT", 140, 140, true);
			tablaPiezaEntrada.setStringColumn(COL_ARTICULO, "TELA", 120, 120, true);
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

		@SuppressWarnings("unused")
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

		public void actualizarTotales() {
			BigDecimal tm = new BigDecimal(0);
			int piezas = 0;
			for(PiezaRemito pe : getPanTablaPieza().getElementos()) {
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
			if(getTabla().getRowCount() == 0) {
				resetToEstadoInicial();
			}
		}

		@Override
		public boolean validarAgregar() {
			JDialogSelRemitoEntradaConPiezasEnStock dialogo = new JDialogSelRemitoEntradaConPiezasEnStock(owner); 
			GuiUtil.centrar(dialogo);
			dialogo.setVisible(true);
			Map<RemitoEntrada, List<PiezaRemito>> resultMap = dialogo.getResult();
			if(resultMap != null && !resultMap.isEmpty()) {
				getBtnSelProductos().setEnabled(true);
				remitoEntradaList.clear();
				piezaRemitoEntradaMap.clear();
				for(RemitoEntrada re : resultMap.keySet()) {
					List<OrdenDeTrabajo> odtAsociadasList = getODTFacade().getOdtEagerByRemitoList(re.getId());
					Articulo articulo = re.getArticuloStock(); 
					for(PiezaRemito pr : resultMap.get(re)) {
						agregarElementoEnTabla(articulo, pr, odtAsociadasList);
						piezaRemitoEntradaMap.put(pr, re);
					}
					handleODTSelected(odtAsociadasList);
					remitoEntradaList.add(re);
				}
				actualizarTotales();
				getTxtRemitosEntrada().setText(extractRemitosEntrada());
			}
			return false;
		}

		private OrdenDeTrabajoFacadeRemote getODTFacade() {
			if(odtFacade == null) {
				odtFacade = GTLBeanFactory.getInstance().getBean2(OrdenDeTrabajoFacadeRemote.class);
			}
			return odtFacade;
		}

		private void agregarElementoEnTabla(Articulo articulo, PiezaRemito pr, List<OrdenDeTrabajo> odtAsociadasList) {
			agregarElemento(pr);
			int row = getTabla().getRowCount() - 1;
			getTabla().setValueAt(articulo, row, COL_ARTICULO);
			OrdenDeTrabajo odt = getODTFromPieza(pr, odtAsociadasList);
			getTabla().setValueAt(odt, row, COL_ODT);
			if(odt != null) {
				odtPiezaMap.put(pr, odt);
			}
		}

		private OrdenDeTrabajo getODTFromPieza(PiezaRemito pr, List<OrdenDeTrabajo> odtAsociadasList) {
			for(OrdenDeTrabajo odt : odtAsociadasList) {
				for(PiezaODT podt : odt.getPiezas()) {
					if(podt.getPiezaRemito().equals(pr)) {
						return odt;
					}
				}
			}
			return null;
		}

		private String extractRemitosEntrada() {
			Set<String> nroRemitoEntradaList = new HashSet<String>();
			for(RemitoEntrada re : remitoEntradaList) {
				nroRemitoEntradaList.add(String.valueOf(re.getNroRemito()));
			}
			return StringUtil.getCadena(nroRemitoEntradaList, ", ");
		}

		@Override
		protected PiezaRemito getElemento(int fila) {
			return (PiezaRemito)getTabla().getValueAt(fila, COL_OBJ);
		}

		@Override
		protected String validarElemento(int fila) {
			OrdenDeTrabajo odt = (OrdenDeTrabajo)getTabla().getValueAt(fila, COL_ODT);
			if(odt == null) {
				return "Debe ingresar la ODT";
			}
			Integer nroPieza = (Integer)getTabla().getTypedValueAt(fila, COL_NRO_PIEZA);
			if(nroPieza == null) {
				return "Debe ingresar el número de pieza";
			}
			Articulo articulo = (Articulo)getTabla().getValueAt(fila, COL_ARTICULO);
			if(!articulo.equals(odt.getProducto().getArticulo())) {
				return "La tela del proceso de la ODT asignada debe ser la misma que la de la pieza original. ";
			}
			return null;
		}

		@Override
		protected void filaTablaSeleccionada() {
		}

		private JButton getBtnSelODTs() {
			if(btnSelODTs == null) {
				btnSelODTs = new JButton("ODT");
				btnSelODTs.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e) {
						JDialogSeleccionarCrearODT dialogSeleccionarCrearODT = new JDialogSeleccionarCrearODT(owner, productoList, odtList);
						GuiUtil.centrarEnFramePadre(dialogSeleccionarCrearODT);
						dialogSeleccionarCrearODT.setVisible(true);
						if(dialogSeleccionarCrearODT.isAcepto()) {
							OrdenDeTrabajo selectedODT = dialogSeleccionarCrearODT.getSelectedODT();
							List<OrdenDeTrabajo> odtListAgregadas = dialogSeleccionarCrearODT.getOdtList();
							getMenuODT().removeAll();
							handleODTSelected(odtListAgregadas);
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

		private void handleODTSelected(List<OrdenDeTrabajo> odtListAgregadas) {
			for(OrdenDeTrabajo odt : odtListAgregadas) {
				if(!odtList.contains(odt)) {
					odtList.add(odt);
				}
				agregarODTEnMenu(odt);
			}
			if(!odtListAgregadas.isEmpty()) {
				getTxtCodODT().setText(StringUtil.getCadena(odtListAgregadas, ", "));
			}
		}

		public void setEnabledBtnODT(boolean b) {
			getBtnSelODTs().setEnabled(b);
		}

	}

	private ParametrosGeneralesFacadeRemote getParametrosGeneralesFacade() {
		if(parametrosGeneralesFacade == null) {
			parametrosGeneralesFacade = GTLBeanFactory.getInstance().getBean2(ParametrosGeneralesFacadeRemote.class);
		}
		return parametrosGeneralesFacade;
	}

	private JTextField getTxtRemitosEntrada() {
		if(txtRemitosEntrada == null) {
			txtRemitosEntrada = new JTextField();
			txtRemitosEntrada.setEditable(false);
		}
		return txtRemitosEntrada;
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

	private void resetToEstadoInicial() {
		getBtnSelProductos().setEnabled(false);
		getTxtProductos().setText(null);
		productoList.clear();
		getPanTablaPieza().setEnabledBtnODT(false);
		remitoEntradaList.clear();
		getTxtRemitosEntrada().setText(null);
		getTxtCodODT().setText(null);
		getTxtPesoTotal().setText(null);
		getTxtPiezas().setText(null);
	}


}