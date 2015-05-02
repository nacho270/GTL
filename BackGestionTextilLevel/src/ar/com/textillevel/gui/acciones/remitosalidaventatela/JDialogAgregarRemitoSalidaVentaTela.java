package ar.com.textillevel.gui.acciones.remitosalidaventatela;

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
import ar.com.textillevel.entidades.to.remitosalida.PiezaRemitoSalidaTO.EnumTipoPiezaRE;
import ar.com.textillevel.entidades.to.remitosalida.RemitoSalidaConBajaStockTO;
import ar.com.textillevel.entidades.ventas.DetallePiezaFisicaTO;
import ar.com.textillevel.entidades.ventas.articulos.Articulo;
import ar.com.textillevel.entidades.ventas.productos.Producto;
import ar.com.textillevel.facade.api.remote.ParametrosGeneralesFacadeRemote;
import ar.com.textillevel.facade.api.remote.RemitoEntradaFacadeRemote;
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

public class JDialogAgregarRemitoSalidaVentaTela extends JDialog {

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
	private RemitoEntradaFacadeRemote remitoEntradaFacade;
	private ParametrosGeneralesFacadeRemote parametrosGeneralesFacade;

	private JButton btnSelProductos;
	private JMenu menuODT;
	private List<OrdenDeTrabajo> odtList;
	private List<Producto> productoList;
	private Frame owner;

	private List<RemitoEntrada> remitoEntradaList;
	private Map<Integer, PiezaRemitoSalidaTO> piezasElegidasStock; //Nro de fila en la tabla
	private boolean ventaTela;
	private boolean modoConsulta;
	
	public JDialogAgregarRemitoSalidaVentaTela(Frame owner, RemitoSalida remitoSalida, boolean ventaTela, boolean modoConsulta) {
		super(owner);
		this.owner = owner; 
		this.remitoSalida = remitoSalida;
		this.modoConsulta = modoConsulta;
		this.odtList = new ArrayList<OrdenDeTrabajo>();
		this.productoList = new ArrayList<Producto>();
		this.remitoEntradaList = new ArrayList<RemitoEntrada>();
		this.piezasElegidasStock = new HashMap<Integer, PiezaRemitoSalidaTO>();
		this.ventaTela = ventaTela;
		setSize(new Dimension(850, 750));
		if(ventaTela) {
			if(modoConsulta) {
				setTitle("Consulta de Remito de Salida - Venta de Tela");
				llenarPiezasRemitoSalidaExistente(remitoSalida);
			} else {
				setTitle("Alta de Remito de Salida - Venta de Tela");
				if(remitoSalida.getId() != null) {
					llenarPiezasRemitoSalidaExistente(remitoSalida);
				}
			}
		} else {
			if(modoConsulta) {
				setTitle("Consulta de Remito de Salida 01");
				llenarPiezasRemitoSalidaExistente(remitoSalida);
			} else {
				setTitle("Alta de Remito de Salida 01");
				if(remitoSalida.getId() != null) {
					llenarPiezasRemitoSalidaExistente(remitoSalida);
				}
			}
		}
		construct();
		setDatos();
		setModal(true);
	}

	private void llenarPiezasRemitoSalidaExistente(RemitoSalida remitoSalida) {
		getPanTablaPieza().llenarTablaPiezas(toResultMap(remitoSalida));
		for(PiezaRemito pr : remitoSalida.getPiezas()) {
			if(pr.getPiezaEntrada() == null) {//Se trata de una pieza de stock inicial
				PiezaRemitoSalidaTO prto = new PiezaRemitoSalidaTO();
				prto.setPrecioMateriaPrimaRE(pr.getPmpDescuentoStock());
				prto.setTipoPiezaRE(EnumTipoPiezaRE.PIEZA_STOCK_INICIAL);
				prto.setObservaciones(pr.getObservaciones());
				prto.setNroPieza(pr.getOrdenPieza());
				prto.addPmpStockConsumido(pr.getPmpDescuentoStock(), pr.getMetros());
				if(!pr.getPiezasPadreODT().isEmpty()) {
					prto.setOdt(pr.getPiezasPadreODT().get(0).getOdt());
				}
				getPanTablaPieza().agregarPiezaRemitoTO(prto);
			}
		}
	}

	public JDialogAgregarRemitoSalidaVentaTela(Frame owner, RemitoSalida remitoSalida, boolean ventaTela, List<DetallePiezaFisicaTO> piezasSeleccionadas) {
		super(owner);
		this.owner = owner; 
		this.remitoSalida = remitoSalida;
		this.modoConsulta = false;
		this.odtList = new ArrayList<OrdenDeTrabajo>();
		this.productoList = new ArrayList<Producto>();
		this.remitoEntradaList = new ArrayList<RemitoEntrada>();
		this.piezasElegidasStock = new HashMap<Integer, PiezaRemitoSalidaTO>();
		this.ventaTela = ventaTela;
		setSize(new Dimension(850, 750));
		if(ventaTela) {
			if(modoConsulta) {
				setTitle("Consulta de Remito de Salida - Venta de Tela");
			} else {
				setTitle("Alta de Remito de Salida - Venta de Tela");
			}
		} else {
			if(modoConsulta) {
				setTitle("Consulta de Remito de Salida 01");
			} else {
				setTitle("Alta de Remito de Salida 01");
			}
		}
		construct();
		setDatos();
		getPanTablaPieza().llenarTablaPiezas(toResultMap(piezasSeleccionadas));
		setModal(true);
	}

	private Map<RemitoEntrada, List<PiezaRemito>> toResultMap(List<DetallePiezaFisicaTO> piezasSeleccionadas) {
		Map<RemitoEntrada, List<PiezaRemito>> resultMap = new HashMap<RemitoEntrada, List<PiezaRemito>>();
		for(DetallePiezaFisicaTO dpfto : piezasSeleccionadas) {
			PiezaRemito piezaRemito = getRemitoEntradaFacade().getPiezaRemitoById(dpfto.getIdPiezaRemito());
			RemitoEntrada remitoEntrada = getRemitoEntradaFacade().getByIdEager(dpfto.getIdRemito());
			List<PiezaRemito> piezaList = resultMap.get(remitoEntrada);
			if(piezaList == null) {
				piezaList = new ArrayList<PiezaRemito>();
				resultMap.put(remitoEntrada, piezaList);
			}
			piezaList.add(piezaRemito);
		}
		return resultMap;
	}

	private Map<RemitoEntrada, List<PiezaRemito>> toResultMap(RemitoSalida rs) {
		Map<RemitoEntrada, List<PiezaRemito>> resultMap = new HashMap<RemitoEntrada, List<PiezaRemito>>();
		for(PiezaRemito dpfto : rs.getPiezas()) {
			if(dpfto.getPiezaEntrada() != null) { //No es una pieza de stock inicial
				PiezaRemito piezaRemito = dpfto.getPiezaEntrada();
				RemitoEntrada remitoEntrada = getRemitoEntradaFacade().getByIdPiezaRemitoEntradaEager(piezaRemito.getId());
				List<PiezaRemito> piezaList = resultMap.get(remitoEntrada);
				if(piezaList == null) {
					piezaList = new ArrayList<PiezaRemito>();
					resultMap.put(remitoEntrada, piezaList);
				}
				piezaList.add(piezaRemito);
			}
		}
		return resultMap;
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
			OrdenDeTrabajo odt = odts.get(0);
			if(odt.getRemito() != null && odt.getRemito().getCondicionDeVenta() != null) {
				getTxtCondicionVenta().setText(odt.getRemito().getCondicionDeVenta().getNombre());
			}
		}
		getTxtCodODT().setText(StringUtil.getCadena(extractCodigos(odts), ", "));
		Set<Producto> productoList = new HashSet<Producto>();
		for(OrdenDeTrabajo odt : odts) {
			productoList.add(odt.getProducto());
		}
		getTxtProductos().setText(StringUtil.getCadena(productoList, ", "));
		getTxtRemitosEntrada().setText(StringUtil.getCadena(extractRemitosEntrada(odts), ", "));
		getTxtPiezas().setText(remitoSalida.getCantidadPiezas().toString());
		getTxtMetros().setText(remitoSalida.getTotalMetros().toString());
	}

	private Set<String> extractRemitosEntrada(List<OrdenDeTrabajo> odts) {
		Set<String> nroRemitoEntradaList = new HashSet<String>();
		for(OrdenDeTrabajo odt : odts) {
			if(odt.getRemito() != null) {
				nroRemitoEntradaList.add(String.valueOf(odt.getRemito().getNroRemito()));
			}
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
					JDialogSeleccionarProducto dialogSeleccionarProducto = new JDialogSeleccionarProducto(JDialogAgregarRemitoSalidaVentaTela.this, productoList, getArticuloElegidos());
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

	private List<Articulo> getArticuloElegidos() {
		Set<Articulo> articuloSet = new HashSet<Articulo>();
		for(RemitoEntrada re : remitoEntradaList) {
			articuloSet.add(re.getArticuloStock());
		}
		for(Integer i : piezasElegidasStock.keySet()) {
			articuloSet.add(piezasElegidasStock.get(i).getArticulo());
		}
		return new ArrayList<Articulo>(articuloSet);
	}
	
	private CLJTextField getTxtPesoTotal() {
		if(txtPesoTotal == null) {
			txtPesoTotal = new CLJTextField();
			if(modoConsulta) {
				txtPesoTotal.setEnabled(false);
			}
			txtPesoTotal.setText(remitoSalida.getPesoTotal().toString());
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
			if(modoConsulta) {
				txtFechaEmision.setEditable(false);
			}
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
						RemitoSalida remitoSalidaSaved = null;
						try {
							if(ventaTela) { //Es un remito de venta de tela
									remitoSalidaSaved = getRemitoSalidaFacade().ingresarRemitoSalidaPorVentaDeTela(remitoSalidaTO);
							} else { //Es un remito que baja stock (salida 01)
								remitoSalidaSaved = getRemitoSalidaFacade().ingresarRemitoSalidaPorSalida01(remitoSalidaTO);
							}
							setRemitoSalida(remitoSalidaSaved);
							CLJOptionPane.showInformationMessage(JDialogAgregarRemitoSalidaVentaTela.this, "El remito se ha grabado con éxito", "Información");
	
							if(CLJOptionPane.showQuestionMessage(JDialogAgregarRemitoSalidaVentaTela.this, "¿Desea imprimir el remito?", "Confirmación") == CLJOptionPane.YES_OPTION) {
								ImprimirRemitoHandler imprimirRemitoHandler = new ImprimirRemitoHandler(remitoSalidaSaved, getParametrosGeneralesFacade().getParametrosGenerales().getNroSucursal(), JDialogAgregarRemitoSalidaVentaTela.this);
								imprimirRemitoHandler.imprimir();
							}
							dispose();
						} catch (ValidacionException e1) {
							CLJOptionPane.showErrorMessage(JDialogAgregarRemitoSalidaVentaTela.this, StringW.wordWrap(e1.getMensajeError()), "Error");
						}
					} 
				}
			});
		}
		if(modoConsulta) {
			btnAceptar.setEnabled(false);
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

	private RemitoEntradaFacadeRemote getRemitoEntradaFacade() {
		if(remitoEntradaFacade == null) {
			remitoEntradaFacade = GTLBeanFactory.getInstance().getBean2(RemitoEntradaFacadeRemote.class);
		}
		return remitoEntradaFacade;
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
			CLJOptionPane.showErrorMessage(JDialogAgregarRemitoSalidaVentaTela.this, "Debe ingresar un peso total válido.", "Error");
			getTxtPesoTotal().requestFocus();
			return false;
		}
		BigDecimal pesoTotal = new BigDecimal(getTxtPesoTotal().getText().trim().replace(',', '.'));
		if(pesoTotal.longValue() <= 0) {
			CLJOptionPane.showErrorMessage(JDialogAgregarRemitoSalidaVentaTela.this, "Debe ingresar un peso total mayor a cero.", "Error");
			getTxtPesoTotal().requestFocus();
			return false;
		}
		
		if(getPanTablaPieza().getTabla().getRowCount() == 0) {
			CLJOptionPane.showErrorMessage(JDialogAgregarRemitoSalidaVentaTela.this, "Debe ingresar al menos una pieza", "Error");
			return false;
		}
		String msgValidacionPiezas = getPanTablaPieza().validarElementos();
		if(msgValidacionPiezas != null) {
			CLJOptionPane.showErrorMessage(JDialogAgregarRemitoSalidaVentaTela.this, StringW.wordWrap(msgValidacionPiezas), "Error");
			return false;
		}
//		boolean existRepeatedValues = getPanTablaPieza().getTabla().existRepeatedValues(new int[]{1});
//		if(existRepeatedValues) {
//			CLJOptionPane.showErrorMessage(JDialogAgregarRemitoSalidaVentaTela.this, "Los números de piezas deben ser distintos.", "Error");
//			return false;
//		}
		return true;
	}

	private PanelTablaPieza getPanTablaPieza() {
		if(panTablaPieza == null) {
			panTablaPieza = new PanelTablaPieza(remitoSalida, modoConsulta);
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
		private static final int COL_ARTICULO = 0;
		private static final int COL_NRO_PIEZA = 1;
		private static final int COL_METROS_PIEZA = 2;
		private static final int COL_OBSERVACIONES = 3;
		private static final int COL_ODT = 4;
		private static final int COL_OBJ = 5;
		private JButton btnSelODTs;
		private JButton btnSelStock;
		private Map<PiezaRemito, OrdenDeTrabajo> odtPiezaMap = new HashMap<PiezaRemito, OrdenDeTrabajo>();
		private Map<PiezaRemito, RemitoEntrada> piezaRemitoEntradaMap = new HashMap<PiezaRemito, RemitoEntrada>();

		private OrdenDeTrabajoFacadeRemote odtFacade;

		public PanelTablaPieza(RemitoSalida remitoSalida, boolean modoConsulta) {
			setModoConsulta(modoConsulta);
			initializePopupMenu();
			agregarBoton(getBtnSelODTs());
			agregarBoton(getBtnAgregarDeStock());
			habilitarBotonesExtra(true);
			getBtnSelODTs().setEnabled(false);
			if(modoConsulta) {
				getBtnAgregarDeStock().setEnabled(false);
			}
		}

		private JButton getBtnAgregarDeStock() {
			if(btnSelStock == null) {
				btnSelStock = new JButton("S");
				btnSelStock.setToolTipText("Descontar de stock inicial");
				btnSelStock.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e) {
						JDialogSeleccionarStockTela dialogo = new JDialogSeleccionarStockTela(owner, piezasElegidasStock.values());
						GuiUtil.centrar(dialogo);
						dialogo.setVisible(true);
						PiezaRemitoSalidaTO piezaRemitoSalidaTO = dialogo.getPiezaRemitoSalidaTO();
						if(piezaRemitoSalidaTO != null) {
							agregarPiezaRemitoTO(piezaRemitoSalidaTO);
							actualizarTotales();
							getBtnSelProductos().setEnabled(true);
						}
					}

				});
			}
			return btnSelStock;
		}

		private void agregarPiezaRemitoTO(PiezaRemitoSalidaTO piezaRemitoSalidaTO) {
			PiezaRemito pr = new PiezaRemito();
			pr.setMetros(piezaRemitoSalidaTO.getTotalMetrosStockConsumido());
			agregarElementoEnTabla(piezaRemitoSalidaTO.getArticulo(), pr, new ArrayList<OrdenDeTrabajo>());
			piezasElegidasStock.put(getTabla().getRowCount() - 1, piezaRemitoSalidaTO);
			if(piezaRemitoSalidaTO.getOdt() != null) {
				getTabla().setValueAt(piezaRemitoSalidaTO.getOdt(), getTabla().getRowCount() - 1, COL_ODT);
			}
			if(piezaRemitoSalidaTO.getNroPieza() != null) {
				getTabla().setValueAt(piezaRemitoSalidaTO.getNroPieza(), getTabla().getRowCount() - 1, COL_NRO_PIEZA);
			}
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
					CLJOptionPane.showInformationMessage(JDialogAgregarRemitoSalidaVentaTela.this, "No se puede cambiar la ODT de la pieza de " + pr.getMetros(), "Información");
					continue;
				} else {
					getTabla().setValueAt(selectedODT, row, COL_ODT);
					odtPiezaMap.put(pr, selectedODT);

					PiezaRemitoSalidaTO piezaRemitoSalidaTO = piezasElegidasStock.get(row);
					if(piezaRemitoSalidaTO != null) {
						piezaRemitoSalidaTO.setOdt(selectedODT);
					}
				}
			}
		}

		public List<PiezaRemitoSalidaTO> capturarSetearDatos() {
			List<PiezaRemitoSalidaTO> piezasResult = new ArrayList<PiezaRemitoSalidaTO>();
			for(int row = 0; row < getTabla().getRowCount(); row++) {
				PiezaRemitoSalidaTO pr = piezasElegidasStock.get(row);
				if(pr == null) {
					pr = new PiezaRemitoSalidaTO();
				}	
				OrdenDeTrabajo odt = (OrdenDeTrabajo)getTabla().getValueAt(row, COL_ODT);
				pr.setPiezaRemitoEntrada(getElemento(row));
				pr.setOdt(odt);
				RemitoEntrada remitoEntrada = piezaRemitoEntradaMap.get(pr.getPiezaRemitoEntrada());
				if(pr.getPiezaRemitoEntrada() != null && odt != null && odt.getRemito() == null) {
					odt.setRemito(remitoEntrada);
				}
				if(remitoEntrada != null) {
					pr.setPrecioMateriaPrimaRE(remitoEntrada.getPrecioMatPrima());
				}
				pr.setObservaciones((String)getTabla().getValueAt(row, COL_OBSERVACIONES));
				Integer nroPieza = (Integer)getTabla().getTypedValueAt(row, COL_NRO_PIEZA);
				pr.setNroPieza(nroPieza);
				piezasResult.add(pr);

				if(!pr.isPiezaStockInicial()) {
					pr.setArticulo((Articulo)getTabla().getValueAt(row, COL_ARTICULO));
				}
				
				//Seteo el tipo de pieza de remito de entrada para saber como tratarla 
				if(remitoEntrada == null) {
					pr.setTipoPiezaRE(EnumTipoPiezaRE.PIEZA_STOCK_INICIAL);
				} else if(remitoEntrada.getPrecioMatPrima() != null && remitoEntrada.getProveedor() != null) {
					pr.setTipoPiezaRE(EnumTipoPiezaRE.COMPRA_DE_TELA);
				} else {
					pr.setTipoPiezaRE(EnumTipoPiezaRE.ENTRADA_01);
				}
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
			row[COL_NRO_PIEZA] = elemento.getOrdenPieza();
			row[COL_METROS_PIEZA] = elemento.getMetros() == null ? null : elemento.getMetros().toString();
			row[COL_OBSERVACIONES] = elemento.getObservaciones();
			row[COL_ARTICULO] = null;
			row[COL_ODT] = modoConsulta ? getOdt(elemento) : null;
			row[COL_OBJ] = elemento;
			return row;
		}

		private OrdenDeTrabajo getOdt(PiezaRemito pr) {
			if(pr.getPiezasPadreODT().isEmpty()) {
				return null;
			} else {
				return pr.getPiezasPadreODT().get(0).getOdt();
			}
		}

		@Override
		protected CLJTable construirTabla() {
			CLJTable tablaPiezaEntrada = new CLJTable(0, CANT_COLS);
			tablaPiezaEntrada.setIntColumn(COL_NRO_PIEZA, "NUMERO", 1, 10000, 50, false);
			tablaPiezaEntrada.setFloatColumn(COL_METROS_PIEZA, "METROS", 0, Float.MAX_VALUE, 50, true);
			tablaPiezaEntrada.setStringColumn(COL_OBSERVACIONES, "OBSERVACIONES", 230, 230, false);
			tablaPiezaEntrada.setStringColumn(COL_ODT, "ODT", 220, 220, true);
			tablaPiezaEntrada.setStringColumn(COL_ARTICULO, "TELA", 150, 150, true);
			tablaPiezaEntrada.setStringColumn(COL_OBJ, "", 0, 0, true);
			tablaPiezaEntrada.setReorderingAllowed(false);

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
			actualizarTotales();
		}

		@Override
		public boolean validarQuitar() {
			for(int fila : getTabla().getSelectedRows()) {
				piezasElegidasStock.remove(fila);
			}
			return true;
		}

		@Override
		public boolean validarAgregar() {
			JDialogSelRemitoEntradaConPiezasParaVender dialogo = new JDialogSelRemitoEntradaConPiezasParaVender(owner, getElementos(), extractPiezasEntradaPersistedFromRemitoSalida()); 
			GuiUtil.centrar(dialogo);
			dialogo.setVisible(true);
			Map<RemitoEntrada, List<PiezaRemito>> resultMap = dialogo.getResult();

			if(resultMap != null && !resultMap.isEmpty()) {
				llenarTablaPiezas(resultMap);
			}

			//Pongo las piezas que estaban pertenecientes al stock inicial (no garantiza el orden original)
			Map<Integer, PiezaRemitoSalidaTO> mapTmp = new HashMap<Integer, PiezaRemitoSalidaTO>(piezasElegidasStock);
			piezasElegidasStock.clear();
			for(Integer k : mapTmp.keySet()) {
				agregarPiezaRemitoTO(mapTmp.get(k));
			}
			return false;
		}

		private List<PiezaRemito> extractPiezasEntradaPersistedFromRemitoSalida() {
			List<PiezaRemito> prList = new ArrayList<PiezaRemito>();
			for(PiezaRemito pr : remitoSalida.getPiezas()) {
				if(pr.getPiezaEntrada() != null) {
					prList.add(pr.getPiezaEntrada());
				}
 			}
			return prList;
		}

		private void llenarTablaPiezas(Map<RemitoEntrada, List<PiezaRemito>> resultPiezasMap) {
			getBtnSelProductos().setEnabled(true);
			remitoEntradaList.clear();
			piezaRemitoEntradaMap.clear();
			for(RemitoEntrada re : resultPiezasMap.keySet()) {
				List<OrdenDeTrabajo> odtAsociadasARemitoList = getODTFacade().getOdtEagerByRemitoList(re.getId());
				Articulo articulo = re.getArticuloStock();
				List<OrdenDeTrabajo> odtAsociadasAPiezasList = new ArrayList<OrdenDeTrabajo>(); 
				for(PiezaRemito pr : resultPiezasMap.get(re)) {
					agregarElementoEnTabla(articulo, pr, odtAsociadasARemitoList);
					OrdenDeTrabajo odtAsociadaAPieza = getODTFromPieza(pr, odtAsociadasARemitoList);
					if(odtAsociadaAPieza != null) {
						odtAsociadasAPiezasList.add(odtAsociadaAPieza);
					}
					piezaRemitoEntradaMap.put(pr, re);
				}
				handleODTSelected(odtAsociadasAPiezasList);
				remitoEntradaList.add(re);
			}

			List<Integer> filasToRemove = new ArrayList<Integer>();
			for(int i = 0; i < getTabla().getRowCount(); i++) {
				PiezaRemito pr = (PiezaRemito)getTabla().getValueAt(i, COL_OBJ);
				if(!piezaRemitoEntradaMap.keySet().contains(pr)) {
					filasToRemove.add(i);
				}
			}
			int[] arrayFilasToRemove = new int[filasToRemove.size()];
			for(int i = 0; i < filasToRemove.size(); i++) {
				arrayFilasToRemove[i] = filasToRemove.get(i);
			}
			getTabla().removeRows(arrayFilasToRemove);
			
			actualizarTotales();
			getTxtRemitosEntrada().setText(extractRemitosEntrada());
		}

		private OrdenDeTrabajoFacadeRemote getODTFacade() {
			if(odtFacade == null) {
				odtFacade = GTLBeanFactory.getInstance().getBean2(OrdenDeTrabajoFacadeRemote.class);
			}
			return odtFacade;
		}

		private void agregarElementoEnTabla(Articulo articulo, PiezaRemito pr, List<OrdenDeTrabajo> odtAsociadasList) {
			if(getTabla().getFirstRowWithValue(COL_OBJ, pr) == -1) {
				agregarElemento(pr);
				int row = getTabla().getRowCount() - 1;
				getTabla().setValueAt(articulo, row, COL_ARTICULO);
				OrdenDeTrabajo odt = getODTFromPieza(pr, odtAsociadasList);
				getTabla().setValueAt(odt, row, COL_ODT);
				if(odt != null) {
					odtPiezaMap.put(pr, odt);
				}
			}
		}

		private OrdenDeTrabajo getODTFromPieza(PiezaRemito pr, List<OrdenDeTrabajo> odtAsociadasList) {
			for(OrdenDeTrabajo odt : odtAsociadasList) {
				for(PiezaODT podt : odt.getPiezas()) {
					if(podt.getPiezaRemito()!= null && podt.getPiezaRemito().equals(pr)) {
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
			Integer nroPieza = (Integer)getTabla().getTypedValueAt(fila, COL_NRO_PIEZA);
			if(nroPieza == null) {
				return "Debe ingresar el número de pieza.";
			}
			if(nroPieza <= 0) {
				return "El número de pieza debe ser mayor a cero.";
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
					agregarODTEnMenu(odt);
				}
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