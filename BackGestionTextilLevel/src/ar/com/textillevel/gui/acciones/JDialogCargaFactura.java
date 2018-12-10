package ar.com.textillevel.gui.acciones;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EtchedBorder;

import main.GTLGlobalCache;
import net.sf.jasperreports.engine.JRException;

import org.apache.taglibs.string.util.StringW;

import ar.com.fwcommon.boss.BossError;
import ar.com.fwcommon.boss.BossEstilos;
import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTable;
import ar.com.fwcommon.componentes.FWJTextField;
import ar.com.fwcommon.componentes.VerticalFlowLayout;
import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.componentes.error.validaciones.ValidacionException;
import ar.com.fwcommon.componentes.error.validaciones.ValidacionExceptionSinRollback;
import ar.com.fwcommon.util.DateUtil;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.fwcommon.util.StringUtil;
import ar.com.textillevel.entidades.config.ParametrosGenerales;
import ar.com.textillevel.entidades.cuenta.to.ETipoDocumento;
import ar.com.textillevel.entidades.documentos.factura.CondicionDeVenta;
import ar.com.textillevel.entidades.documentos.factura.CorreccionFactura;
import ar.com.textillevel.entidades.documentos.factura.DocumentoContableCliente;
import ar.com.textillevel.entidades.documentos.factura.Factura;
import ar.com.textillevel.entidades.documentos.factura.NotaCredito;
import ar.com.textillevel.entidades.documentos.factura.NotaDebito;
import ar.com.textillevel.entidades.documentos.factura.itemfactura.ItemFactura;
import ar.com.textillevel.entidades.documentos.factura.itemfactura.ItemFacturaBonificacion;
import ar.com.textillevel.entidades.documentos.factura.itemfactura.ItemFacturaCorreccion;
import ar.com.textillevel.entidades.documentos.factura.itemfactura.ItemFacturaOtro;
import ar.com.textillevel.entidades.documentos.factura.itemfactura.ItemFacturaPercepcion;
import ar.com.textillevel.entidades.documentos.factura.itemfactura.ItemFacturaPrecioMateriaPrima;
import ar.com.textillevel.entidades.documentos.factura.itemfactura.ItemFacturaProducto;
import ar.com.textillevel.entidades.documentos.factura.itemfactura.ItemFacturaRecargo;
import ar.com.textillevel.entidades.documentos.factura.itemfactura.ItemFacturaSeguro;
import ar.com.textillevel.entidades.documentos.factura.itemfactura.ItemFacturaTelaCruda;
import ar.com.textillevel.entidades.documentos.factura.itemfactura.ItemFacturaTubo;
import ar.com.textillevel.entidades.documentos.remito.PiezaRemito;
import ar.com.textillevel.entidades.documentos.remito.RemitoEntrada;
import ar.com.textillevel.entidades.documentos.remito.RemitoEntradaDibujo;
import ar.com.textillevel.entidades.documentos.remito.RemitoSalida;
import ar.com.textillevel.entidades.enums.EEstadoFactura;
import ar.com.textillevel.entidades.enums.EPosicionIVA;
import ar.com.textillevel.entidades.enums.ETipoCorreccionFactura;
import ar.com.textillevel.entidades.enums.ETipoFactura;
import ar.com.textillevel.entidades.enums.ETipoMateriaPrima;
import ar.com.textillevel.entidades.enums.EUnidad;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.portal.UsuarioSistema;
import ar.com.textillevel.entidades.ventas.ProductoArticulo;
import ar.com.textillevel.entidades.ventas.articulos.Articulo;
import ar.com.textillevel.entidades.ventas.articulos.DibujoEstampado;
import ar.com.textillevel.entidades.ventas.articulos.EEstadoDibujo;
import ar.com.textillevel.entidades.ventas.cotizacion.DefinicionPrecio;
import ar.com.textillevel.entidades.ventas.cotizacion.ListaDePrecios;
import ar.com.textillevel.entidades.ventas.cotizacion.VersionListaDePrecios;
import ar.com.textillevel.entidades.ventas.materiaprima.PrecioMateriaPrima;
import ar.com.textillevel.facade.api.remote.CondicionDeVentaFacadeRemote;
import ar.com.textillevel.facade.api.remote.CorreccionFacadeRemote;
import ar.com.textillevel.facade.api.remote.DocumentoContableFacadeRemote;
import ar.com.textillevel.facade.api.remote.FacturaFacadeRemote;
import ar.com.textillevel.facade.api.remote.ListaDePreciosFacadeRemote;
import ar.com.textillevel.facade.api.remote.ParametrosGeneralesFacadeRemote;
import ar.com.textillevel.facade.api.remote.PrecioMateriaPrimaFacadeRemote;
import ar.com.textillevel.facade.api.remote.RemitoEntradaDibujoFacadeRemote;
import ar.com.textillevel.facade.api.remote.RemitoEntradaFacadeRemote;
import ar.com.textillevel.facade.api.remote.RemitoSalidaFacadeRemote;
import ar.com.textillevel.facade.api.remote.UsuarioSistemaFacadeRemote;
import ar.com.textillevel.gui.acciones.impresionfactura.ImpresionFacturaHandler;
import ar.com.textillevel.gui.acciones.remitosalida.RemitoSalidaLinkeableLabel;
import ar.com.textillevel.gui.modulos.dibujos.gui.JDialogAgregarModificarDibujoEstampado;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.NroDibujoEstampadoTracker;
import ar.com.textillevel.gui.util.ProductosAndPreciosHelper;
import ar.com.textillevel.gui.util.controles.LinkableLabel;
import ar.com.textillevel.gui.util.controles.PanelDatePicker;
import ar.com.textillevel.gui.util.dialogs.JDialogPasswordInput;
import ar.com.textillevel.modulos.odt.entidades.PiezaODT;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogCargaFactura extends JDialog {

	private static final long serialVersionUID = -6724610019577922453L;

	private static final int CANT_COLS_TBL_FACTURA = 7;
	private static final int COL_ARTICULO = 0;
	private static final int COL_CANTIDAD = 1;
	private static final int COL_UNIDAD = 2;
	private static final int COL_DESCRIPCION = 3;
	private static final int COL_PRECIO_UNITARIO = 4;
	private static final int COL_IMPORTE = 5;
	private static final int COL_OBJ_FACTURA = 6;

	private FWJTable tablaProductos;

	private FWJTextField txtSubTotal;
	private FWJTextField txtImpuestos;
	private FWJTextField txtSubTotalConImpuestos;
	private FWJTextField txtPorcentajeIVA;
	private FWJTextField txtPorcentajeIVANoInscripto;
	private FWJTextField txtImporteIVAInscripto;
	private FWJTextField txtImporteIVANoInscripto;
	private FWJTextField txtTotal;

	private FWJTextField txtRazonSocial;
	private FWJTextField txtDireccion;
	private FWJTextField txtLocalidad;
	private FWJTextField txtCondicionIVA;
	private FWJTextField txtCuit;
	private JComboBox cmbCondicionVenta;
	private FWJTextField txtNrosGenericos;

	private PanelDatePicker panelFecha;
	private FWJTextField txtNroFactura;
	private JLabel lblTipoDocumento;
	private JLabel lblTipoFactura;
	private LinkableLabel lblElegirFactura;
	
	private JPanel panelDatosFactura;
	private JPanel panelDatosCliente;
	private JPanel panelTablaProductos;
	private JPanel panelTotales;

	private JButton btnGuardar;
	private JButton btnImprimir;
	private JButton btnSalir;
	private JButton btnAgregarProducto;
	private JButton btnQuitarProducto;
	private JButton btnBuscarListaDePrecios;
	private JButton btnQuitarListaDePrecios;
	private JLabel lblVersionListaPrecios;

	private List<RemitoSalida> remitos;
	private ETipoCorreccionFactura tipoCorrecion;
	private Boolean llevaSeguro;
	private Integer cantTubos;

	private CondicionDeVentaFacadeRemote condicionDeVentaFacade;
	private FacturaFacadeRemote facturaFacade;
	private CorreccionFacadeRemote correccionFacade;
	private ParametrosGeneralesFacadeRemote parametrosGeneralesFacade;
	private PrecioMateriaPrimaFacadeRemote precioMatariaPrimaFacade;
	private RemitoEntradaFacadeRemote remitoEntradaFacade;
	private RemitoEntradaDibujoFacadeRemote remitoEntradaDibujoFacade;

	private ParametrosGenerales parametrosGenerales;

	private Factura factura;

	private CorreccionFactura correcionFactura;

	private boolean consulta;

	private Cliente cliente;

	private PanelRemitos pnlNroRemito;
	
	private Map<CeldaTablaProductos, Integer> mapFilaCantidadErrores = new HashMap<CeldaTablaProductos, Integer>();
	
	private String strFacturasRelacionadas;
	
	private boolean edicion;

	private DocumentoContableFacadeRemote docContableFacade;
	
	private Map<Integer, Float> gramajes = new HashMap<Integer, Float>();
	private NroDibujoEstampadoTracker nroDibujoTracker;

	private RemitoEntradaDibujo reDibujo;
	
	private ListaDePrecios listaDePrecios;
	private VersionListaDePrecios versionListaPreciosActual;
	
	/**
	 * Constructor para consulta de correcciones
	 * 
	 * @param padre
	 * @param factura
	 * @param correcciones
	 */
	public JDialogCargaFactura(Frame padre, CorreccionFactura correccion, boolean consulta) {
		super(padre);
		ParametrosGenerales parametrosGenerales2 = getParametrosGeneralesFacade().getParametrosGenerales();
		if (parametrosGenerales2 == null) {
			FWJOptionPane.showErrorMessage(this, "Faltan configurar los parametros generales", "Error");
			dispose();
		}
		setCliente(correccion.getCliente());
		setParametrosGenerales(parametrosGenerales2);
		setCorrecionFactura(correccion);
		construct();
		if(consulta) {
			getPanelBotonesFactura().setVisible(false);
		}
		llenarDatosCliente(correccion.getCliente());
		if(correccion instanceof NotaCredito){
			getTxtNrosGenericos().setText(getNrosFacturasRelacionadas( ((NotaCredito) correccion).getFacturasRelacionadas()));
		}
		getCmbCondicionVenta().setEnabled(!consulta);
		getPanelTotales().setVisible(true);

		if(getCorrecionFactura().getAnulada()==true){
			GenericUtils.ponerFondoAnulada(getTablaProductos().getScrollPane());
			getPanelBotonesFactura().setVisible(false);
			getBtnGuardar().setVisible(false);
			getBtnImprimir().setVisible(false);
			getPanelFecha().setEnabled(false);
			getCmbCondicionVenta().setEnabled(false);
			getTxtPorcentajeIVA().setText("");
		}else{
			getBtnQuitarProducto().setEnabled(false);
			getBtnGuardar().setVisible(!consulta);
			getLblTipoFactura().setVisible(false);
			getPanelFecha().setEnabled(!consulta);
			getBtnImprimir().setVisible(consulta);
		}
		setConsulta(consulta);
		setTitle("Consultar " + getTipoCorrecion().getDescripcion());
		getPanelFecha().setSelectedDate(correccion.getFechaEmision());
		GuiUtil.setEstadoPanel(getPanelTablaProductos(), !consulta);
		llenarTablaProductos();
		BigDecimal montoSubtotal = getCorrecionFactura().getMontoSubtotal();
		if(montoSubtotal!=null){
			montoSubtotal = montoSubtotal.multiply(new BigDecimal(getCorrecionFactura() instanceof NotaCredito?-1:1));
			getTxtSubTotal().setText(getDecimalFormat().format(montoSubtotal));
		}
		updateTotalesCorrecion();
		
		setEdicion(!consulta);
	}

	/**
	 * Constructor para agregar correccion
	 * 
	 * @param padre
	 * @param factura
	 * @param tipoCorrecion
	 */
	public JDialogCargaFactura(Frame padre, Cliente cliente, ETipoCorreccionFactura tipoCorrecion) {
		super(padre);
		setTipoCorrecion(tipoCorrecion);
		setCliente(cliente);
		iniciarCorreccion();
		ParametrosGenerales parametrosGenerales2 = getParametrosGeneralesFacade().getParametrosGenerales();
		if (parametrosGenerales2 == null) {
			FWJOptionPane.showErrorMessage(this, "Faltan configurar los parametros generales", "Error");
			dispose();
		}
		setParametrosGenerales(parametrosGenerales2);
		construct();
		getTxtNroFactura().setText(
				StringUtil.fillLeftWithZeros(String.valueOf(getCorrecionFactura().getNroSucursal()!=null?getCorrecionFactura().getNroSucursal():parametrosGenerales2.getNroSucursal()), 4) + "-"
						+ StringUtil.fillLeftWithZeros(String.valueOf(getCorrecionFactura().getNroFactura()), 8));
		llenarDatosCliente(getCliente());
		// getTxtNroRemito().setText(String.valueOf(factura.getRemito().getNroRemito()));
		// getCmbCondicionVenta().setSelectedItem(factura.getCondicionDeVenta());
		getCmbCondicionVenta().setEnabled(false);
		getPanelTotales().setVisible(true);

		addItemFacturaCorreccionVacio();
		llenarTablaProductos();

		getLblTipoFactura().setVisible(false);
		setTitle("Agregar " + getTipoCorrecion().getDescripcion());
		getBtnImprimir().setVisible(false);
		setEdicion(false);
	}

	private void addItemFacturaCorreccionVacio() {
		ItemFacturaCorreccion ifo = new ItemFacturaCorreccion();
		ifo.setCantidad(new BigDecimal(1));
		ifo.setUnidad(EUnidad.UNIDAD);
		ifo.setPrecioUnitario(BigDecimal.ZERO);
		ifo.setImporte(BigDecimal.ZERO);
		getCorrecionFactura().getItems().add(ifo);
	}

	/**
	 * Constructor para consultar factura
	 * 
	 * @param padre
	 * @param factura
	 */
	public JDialogCargaFactura(Frame padre, Factura factura, boolean consulta) {
		super(padre);
		setFactura(factura);
		setCliente(factura.getCliente());
		setRemitos(GTLBeanFactory.getInstance().getBean2(RemitoSalidaFacadeRemote.class).getByIdsConPiezasYProductos(extractIds(factura.getRemitos())));
		ParametrosGenerales parametrosGenerales2 = getParametrosGeneralesFacade().getParametrosGenerales();
		if (parametrosGenerales2 == null) {
			FWJOptionPane.showErrorMessage(this, "Faltan configurar los parametros generales", "Error");
			dispose();
		}
		setParametrosGenerales(parametrosGenerales2);
		construct();
		if(factura.getEstadoFactura()==EEstadoFactura.ANULADA){
			GenericUtils.ponerFondoAnulada(getTablaProductos().getScrollPane());
			getPanelBotonesFactura().setVisible(false);
			getBtnGuardar().setVisible(false);
			getBtnImprimir().setVisible(false);
			getPanelFecha().setSelectedDate(factura.getFechaEmision());
			getPanelFecha().setEnabled(false);
			getCmbCondicionVenta().setEnabled(false);
			GuiUtil.setEstadoPanel(getPanelTablaProductos(), !consulta);
			getTxtPorcentajeIVA().setText("");
			calcularSubTotal();
			llenarDatosCliente(getCliente());
			llenarTablaProductos();
		}else{
			llenarDatosCliente(getCliente());
			if(getCliente().getPosicionIva() == EPosicionIVA.EXPORTACION){
				getBtnImprimir().setVisible(false);
			}
			if (factura.getMontoImpuestos() != null) {
				getTxtImpuestos().setText(String.valueOf(factura.getMontoImpuestos().doubleValue()));
			}
			getTxtImpuestos().setEditable(false);
			llenarTablaProductos();
			getCmbCondicionVenta().setSelectedItem(factura.getCondicionDeVenta());
			getCmbCondicionVenta().setEnabled(!consulta);
			if(consulta){
				GuiUtil.setEstadoPanel(getPanelBotonesFactura(), false);
			}
			GuiUtil.setEstadoPanel(getPanelTablaProductos(), !consulta);
			calcularSubTotal();
			setConsulta(consulta);
			getBtnGuardar().setVisible(!consulta);
			setTitle("Consultar factura");
			getPanelFecha().setSelectedDate(factura.getFechaEmision());
			getPanelFecha().setEnabled(!consulta);
			setEdicion(!consulta);
		}
	}

	private List<Integer> extractIds(List<RemitoSalida> rsList) {
		List<Integer> remitosIds = new ArrayList<Integer>(rsList.size());
		for(RemitoSalida rs : rsList) {
			remitosIds.add(rs.getId());
		}
		return remitosIds;
	}

	/**
	 * Constructor para agregar factura
	 * 
	 * @param padre
	 * @param remito
	 * @param nroFactura
	 * @param cantTubos
	 * @param llevaSeguro
	 */
	public JDialogCargaFactura(Frame padre, List<RemitoSalida> remitos, Integer nroFactura, Integer cantTubos, Boolean llevaSeguro, Cliente cliente) {
		super(padre);
		setCliente(cliente);
		setRemitos(remitos);
		setLlevaSeguro(llevaSeguro);
		setCantTubos(cantTubos);
		ParametrosGenerales parametrosGenerales2 = getParametrosGeneralesFacade().getParametrosGenerales();
		if (parametrosGenerales2 == null) {
			FWJOptionPane.showErrorMessage(this, "Faltan configurar los parametros generales", "Error");
			dispose();
			return;
		}
		setParametrosGenerales(parametrosGenerales2);
		try {
			inicializarFactura(nroFactura);
		} catch(ProductoSinPrecioException e) {
			dispose();
			return;
		}
		construct();
		agregarSeguroYTubos(llevaSeguro);
		llenarDatosCliente(getCliente());
		//getPnlNroRemito().setText(String.valueOf(getRemito().getNroRemito()));
		llenarTablaProductos();
		setTitle("Agregar factura");
		setCliente(factura.getCliente());
		getBtnImprimir().setVisible(false);
		setEdicion(false);
	}
	
	/**
	 * Constructor para agregar factura sin remito
	 * @param padre
	 * @param nroFactura
	 * @param cliente
	 */
	public JDialogCargaFactura(Frame padre, Integer nroFactura, Cliente cliente) {
		super(padre);
		ParametrosGenerales parametrosGenerales2 = getParametrosGeneralesFacade().getParametrosGenerales();
		if (parametrosGenerales2 == null) {
			FWJOptionPane.showErrorMessage(this, "Faltan configurar los parametros generales", "Error");
			dispose();
			return;
		}
		setParametrosGenerales(parametrosGenerales2);
		setCliente(cliente);
		try {
			inicializarFactura(nroFactura);
		} catch(ProductoSinPrecioException e) {
			dispose();
			return;
		}
		getFactura().setCliente(getCliente());
		construct();
		//agregarSeguroYTubos(llevaSeguro);
		llenarDatosCliente(getCliente());
		//getTxtNroRemito().setText(String.valueOf(getRemito().getNroRemito()));
		//llenarTablaProductos();
		setTitle("Agregar factura");
		getPnlNroRemito().setVisible(false);
		getBtnImprimir().setVisible(false);
		setEdicion(false);
	}

	private void agregarSeguroYTubos(Boolean llevaSeguro) {
		if (getCantTubos() != null && getCantTubos() > 0) {
			getFactura().getItems().add(getItemFacturaTubo());
		}
		if (!haySeguro()) {
			if (FWJOptionPane.showQuestionMessage(this, "Desea agregar seguro?", "Alta de factura") == FWJOptionPane.YES_OPTION) {
				getFactura().getItems().add(getItemFacturaSeguro());
			}
		}
	}

	private void iniciarCorreccion() {
		switch (getTipoCorrecion()) {
			case NOTA_CREDITO: {
				setCorrecionFactura(new NotaCredito());
				break;
			}
			case NOTA_DEBITO: {
				setCorrecionFactura(new NotaDebito());
				break;
			}
		}
		ETipoFactura tipoFactura = null;
		if(getCliente().getPosicionIva() != null) {
			tipoFactura = getCliente().getPosicionIva().getTipoFactura();
			if(tipoFactura == ETipoFactura.A){
				getCorrecionFactura().setPorcentajeIVAInscripto(getParametrosGeneralesFacade().getParametrosGenerales().getPorcentajeIVAInscripto());
			}
			getCorrecionFactura().setNroFactura(getDocumentoContableFacade().getProximoNroDocumentoContable(getCliente().getPosicionIva(), getCorrecionFactura().getTipoDocumento()));
			getCorrecionFactura().setTipoFactura(tipoFactura);
		}else{
			throw new RuntimeException("El cliente no tiene posicion de IVA");
		}
	}

	private ItemFactura getItemFacturaSeguro() {
		ItemFacturaSeguro itfs = new ItemFacturaSeguro();
		itfs.setCantidad(new BigDecimal(1));
		itfs.setUnidad(EUnidad.UNIDAD);
		itfs.setDescripcion("SEGURO DE MERCADERIA");
		itfs.setPorcentajeSeguro(getParametrosGenerales().getPorcentajeSeguro());
		return itfs;
	}

	private ItemFactura getItemFacturaTubo() {
		ItemFacturaTubo itft = new ItemFacturaTubo();
		itft.setCantidad(new BigDecimal(getCantTubos().intValue()));
		BigDecimal precioPorTubo = getParametrosGenerales().getPrecioPorTubo();
		itft.setPrecioTubo(precioPorTubo);
		itft.setDescripcion("TUBOS PARA MECADERIA");
		itft.setImporte(new BigDecimal(precioPorTubo.doubleValue() * getCantTubos().intValue()));
		itft.setUnidad(EUnidad.UNIDAD);
		itft.setPrecioUnitario(precioPorTubo);
		return itft;
	}

	private void llenarItemsFacturaConRemito() throws ProductoSinPrecioException {
		//Armo un map con los totales de metros por producto en base a las piezas de los remitos
		Map<ProductoArticulo, BigDecimal> mapTotalPorProducto = new HashMap<ProductoArticulo, BigDecimal>();

		//Armo un map con los totales de metros por tela (
		Map<Articulo, BigDecimal> mapTotalPorTelaCruda = new HashMap<Articulo, BigDecimal>();
		Map<PrecioMateriaPrima, BigDecimal> mapTotalStockInicial = new HashMap<PrecioMateriaPrima, BigDecimal>();
		List<RemitoSalida> remitos = this.getRemitos();
		for(RemitoSalida r : remitos){
			for(PiezaRemito pr : r.getPiezas()) {
				if(pr.getPmpDescuentoStock() == null) { //Son piezas normales, es decir, distintas a la de stock inicial
					ProductoArticulo producto = getProducto(pr);
					if(producto == null) {//Es una pieza de tela cruda (i.e. no tiene ODT/Producto)
						Articulo articulo = getRemitoEntradaFacade().getArticuloByPiezaSalidaCruda(pr.getId());
						if(articulo == null) {
							throw new IllegalArgumentException("No existe art�culo relacionado para la pieza de salida con id " + pr.getId() + " y metros "  + pr.getMetros());
						}
						incrementarCantEnMap(mapTotalPorTelaCruda, articulo, pr.getMetros());
					} else {
						incrementarCantEnMap(mapTotalPorProducto, producto, getTotalMetros(pr, producto));
					}
				} else {//Son piezas de stock inicial
					ProductoArticulo p = getProducto(pr);
					if(p == null) { //Es una pieza de stock inicial pero sin ODT
						incrementarCantEnMap(mapTotalStockInicial, pr.getPmpDescuentoStock(), pr.getMetros());
					} else { //Tiene ODT
						incrementarCantEnMap(mapTotalPorProducto, p, getTotalMetros(pr, p));
					}
				}
			}
		}
		//Por cada par <producto, total> del map genero un item factura producto y lo pongo en la tabla
		for (ProductoArticulo p : mapTotalPorProducto.keySet()) {
			ProductosAndPreciosHelper helper = new ProductosAndPreciosHelper(JDialogCargaFactura.this, getCliente());
			ItemFacturaProducto itp = new ItemFacturaProducto();
			BigDecimal totalByProducto = mapTotalPorProducto.get(p);
			itp.setCantidad(totalByProducto);
			BigDecimal precio = helper.getPrecio(p);
			if(precio == null) {
				throw new ProductoSinPrecioException();
			}
			itp.setImporte(totalByProducto.multiply(precio));
			itp.setDescripcion(p.toString());
			itp.setProductoArticulo(p);
			itp.setUnidad(p.getTipo().getUnidad());
			itp.setPrecioUnitario(precio);
			getFactura().getItems().add(itp);
		}
		//coloco los de tela cruda
		for(Articulo a : mapTotalPorTelaCruda.keySet()) {
			ItemFacturaTelaCruda iftc = new ItemFacturaTelaCruda();
			BigDecimal totalByTela = mapTotalPorTelaCruda.get(a);
			iftc.setCantidad(totalByTela);
			BigDecimal precioMasRecienteTela = getPrecioMateriaPrimaFacade().getPrecioMasRecienteTela(a.getId());
			precioMasRecienteTela = precioMasRecienteTela == null ? new BigDecimal(0f) : precioMasRecienteTela;
			iftc.setPrecioUnitario(precioMasRecienteTela);
			iftc.setImporte(precioMasRecienteTela.multiply(totalByTela));
			iftc.setArticulo(a);
			iftc.setDescripcion(""); //los items crudos se sugieren sin descripci�n
			iftc.setUnidad(EUnidad.METROS);
			getFactura().getItems().add(iftc);
		}
		//coloco los de stock inicial y que son crudos
		for(PrecioMateriaPrima pmp : mapTotalStockInicial.keySet()) {
			ItemFacturaPrecioMateriaPrima ifpmp = new ItemFacturaPrecioMateriaPrima();
			BigDecimal totalByPMP = mapTotalStockInicial.get(pmp);
			ifpmp.setCantidad(totalByPMP);
			ifpmp.setPrecioUnitario(pmp.getPrecio());
			ifpmp.setImporte(pmp.getPrecio().multiply(totalByPMP));
			ifpmp.setDescripcion(pmp.getMateriaPrima().getDescripcion() + " - " + pmp.getAlias());
			ifpmp.setUnidad(pmp.getMateriaPrima().getUnidad());
			ifpmp.setPrecioMateriaPrima(pmp);
			getFactura().getItems().add(ifpmp);
		}
	}

	private <T> void incrementarCantEnMap(Map<T, BigDecimal> map, T elem, BigDecimal cant) {
		BigDecimal total = map.get(elem);
		if(total == null) {
			total = cant;
		} else {
			total = total.add(cant);
		}
		map.put(elem, total);
	}

	private BigDecimal getTotalMetros(PiezaRemito pr, ProductoArticulo producto) {
		if (producto.getTipo().getUnidad() == EUnidad.KILOS) {
			Float gramajeRE = getGramajeRE(pr);
			if(gramajeRE == null) {
				return pr.getMetros();
			} else {
				return pr.getMetros().multiply(new BigDecimal(gramajeRE.floatValue())); 
			}
		} else {//estampado se cobra por METROS
			return pr.getMetros();
		}
	}

	private ProductoArticulo getProducto(PiezaRemito pr) {
		if(pr.getPiezasPadreODT().isEmpty()) {
			return null;
		}
		PiezaODT piezaODT = pr.getPiezasPadreODT().get(0);
		return piezaODT.getOdt().getProductoArticulo();
	}

	private Float getGramajeRE(PiezaRemito pr) {
		if(pr.getPiezaPadreODT() != null) {
			return getGramajeFromDB(pr.getPiezaPadreODT().getOdt().getRemito().getId());
		} else {
			return null;
		}
	}

	private Float getGramajeFromDB(Integer idRE) {
		if(gramajes.get(idRE) == null) {
			RemitoEntrada re = GTLBeanFactory.getInstance().getBean2(RemitoEntradaFacadeRemote.class).getByIdEagerConPiezasODTYRemito(idRE);
			gramajes.put(idRE, re.getGramaje());
		}
		return gramajes.get(idRE);
	}

	private void calcularSubTotal() {
		double suma = 0;
		for (ItemFactura it : getDocContable().getItems()) {
			if(it instanceof ItemFacturaProducto) {
				BigDecimal precioProductoLista = getPrecioProducto((ItemFacturaProducto) it);
				if(precioProductoLista == null) {
					suma += it.getImporte().doubleValue();
				} else {
					suma += precioProductoLista.doubleValue();
				}
			}else {
				suma += it.getImporte().doubleValue();
			}
		}
		getDocContable().setMontoSubtotal(new BigDecimal(String.valueOf(Math.abs(suma))));
		getTxtSubTotal().setText((getCorrecionFactura() instanceof NotaCredito && suma >0?"-":"") +  getDecimalFormat().format(suma));
		if(isFactura()) {
			updateTotales();
		} else {
			updateTotalesCorrecion();
		}
	}

	private void llenarDatosCliente(Cliente cliente) {
		getTxtRazonSocial().setText(cliente.getRazonSocial());
		getTxtDireccion().setText(cliente.getDireccionReal().getDireccion());
		if (cliente.getDireccionReal().getLocalidad() != null) {
			getTxtLocalidad().setText(cliente.getDireccionReal().getLocalidad().getNombreLocalidad());
		}
		gettxtCondicionIVA().setText(cliente.getPosicionIva().getDescripcion());
		getTxtCuit().setText(cliente.getCuit());
	}

	private void llenarTablaProductos() {
		getTablaProductos().removeAllRows();
		for (ItemFactura ifactura : getDocContable().getItems()) {
			if (ifactura instanceof ItemFacturaProducto) {
				getTablaProductos().addRow(getFilaProducto((ItemFacturaProducto) ifactura));
				continue;
			}
			if (ifactura instanceof ItemFacturaSeguro) {
				double valorAntesSeguro = getValorParaSeguro();
				ItemFacturaSeguro itemSeguro = (ItemFacturaSeguro) ifactura;
				if(itemSeguro.getImporte() == null) {
					itemSeguro.setImporte(new BigDecimal(valorAntesSeguro * getParametrosGenerales().getPorcentajeSeguro().doubleValue() / 100));
				}
				getTablaProductos().addRow(getFilaSeguro(itemSeguro));
				continue;
			}
			if (ifactura instanceof ItemFacturaTubo) {
				getTablaProductos().addRow(getFilaTubo((ItemFacturaTubo) ifactura));
				continue;
			}
			if (ifactura instanceof ItemFacturaRecargo) {
				getTablaProductos().addRow(getFilaRecargo((ItemFacturaRecargo) ifactura));
				continue;
			}
			if (ifactura instanceof ItemFacturaBonificacion) {
				getTablaProductos().addRow(getFilaBonificacion((ItemFacturaBonificacion) ifactura));
				continue;
			}
			if (ifactura instanceof ItemFacturaPercepcion) {
				double valorAntesSeguro = getValorParaSeguro();
				double subTotal = 0;
				if(haySeguro()){
					subTotal = valorAntesSeguro * getParametrosGenerales().getPorcentajeSeguro().doubleValue() / 100 + valorAntesSeguro;
				}else{
					subTotal = valorAntesSeguro;
				}
				ItemFacturaPercepcion itemPercepcion = (ItemFacturaPercepcion) ifactura;
				itemPercepcion.setImporte(new BigDecimal(subTotal*itemPercepcion.getPorcentajePercepcion().doubleValue()/100));
				getTablaProductos().addRow(getFilaPercepcion(itemPercepcion));
				continue;
			}
			if(ifactura instanceof ItemFacturaPrecioMateriaPrima){
				getTablaProductos().addRow(getFilaItemPrecioMateriaPrima((ItemFacturaPrecioMateriaPrima) ifactura));
			}
			if(ifactura instanceof ItemFacturaTelaCruda){
				getTablaProductos().addRow(getFilaItemTelaCruda((ItemFacturaTelaCruda) ifactura));
				getTablaProductos().lockCell(getTablaProductos().getRowCount() - 1, COL_CANTIDAD); //No se puede cambiar la cantidad de metros de la pieza cruda
			}
			if(ifactura instanceof ItemFacturaOtro){
				getTablaProductos().addRow(getFilaItemOtro((ItemFacturaOtro)ifactura));
			}
			if(ifactura instanceof ItemFacturaCorreccion){
				getTablaProductos().addRow(getFilaItemCorreccion((ItemFacturaCorreccion)ifactura));
			}
		}
		bloquearFilasSeguroYPercepcion();
		calcularSubTotal();
	}

	private Object[] getFilaItemCorreccion(ItemFacturaCorreccion ifactura) {
		Object[] fila = new Object[CANT_COLS_TBL_FACTURA];
		fila[COL_CANTIDAD] = ifactura.getCantidad();
		fila[COL_ARTICULO] = "";
		fila[COL_DESCRIPCION] = ifactura.getDescripcion();
		fila[COL_UNIDAD] = ifactura.getUnidad().getDescripcion();
		fila[COL_PRECIO_UNITARIO] = ifactura.getPrecioUnitario();
		fila[COL_IMPORTE] =(ifactura.getImporte().doubleValue()<1?String.valueOf(ifactura.getImporte()): getDecimalFormat().format(ifactura.getImporte().doubleValue()));
		fila[COL_OBJ_FACTURA] = ifactura;
		return fila;
	}

	private Object[] getFilaItemOtro(ItemFacturaOtro ifactura) {
		Object[] fila = new Object[CANT_COLS_TBL_FACTURA];
		fila[COL_CANTIDAD] = ifactura.getCantidad();
		fila[COL_ARTICULO] = "OTRO";
		fila[COL_DESCRIPCION] = ifactura.getDescripcion();
		fila[COL_UNIDAD] = ifactura.getUnidad().getDescripcion();
		fila[COL_PRECIO_UNITARIO] = ifactura.getPrecioUnitario();// (ifactura.getPrecioUnitario().doubleValue()<1?String.valueOf(ifactura.getPrecioUnitario()): getDecimalFormat().format(ifactura.getPrecioUnitario().doubleValue()));
		fila[COL_IMPORTE] =(ifactura.getImporte().doubleValue()<1?String.valueOf(ifactura.getImporte()): getDecimalFormat().format(ifactura.getImporte().doubleValue()));
		fila[COL_OBJ_FACTURA] = ifactura;
		return fila;
	}

	private Object[] getFilaItemTelaCruda(ItemFacturaTelaCruda ifactura) {
		Object[] fila = new Object[CANT_COLS_TBL_FACTURA];
		fila[COL_CANTIDAD] = ifactura.getCantidad();
		fila[COL_ARTICULO] = ifactura.getArticulo().toString().toUpperCase();
		fila[COL_DESCRIPCION] = ifactura.getDescripcion();
		fila[COL_UNIDAD] = ifactura.getUnidad().getDescripcion();
		fila[COL_PRECIO_UNITARIO] = ifactura.getPrecioUnitario();// (ifactura.getPrecioUnitario().doubleValue()<1?String.valueOf(ifactura.getPrecioUnitario()): getDecimalFormat().format(ifactura.getPrecioUnitario().doubleValue()));
		fila[COL_IMPORTE] =(ifactura.getImporte().doubleValue()<1?String.valueOf(ifactura.getImporte()): getDecimalFormat().format(ifactura.getImporte().doubleValue()));
		fila[COL_OBJ_FACTURA] = ifactura;
		return fila;
	}

	private void bloquearFilasSeguroYPercepcion() {
		int filaSeguro = getNumeroFilaSeguro();
		if(filaSeguro > -1){
			getTablaProductos().lockCell(filaSeguro, COL_CANTIDAD);
			getTablaProductos().lockCell(filaSeguro, COL_PRECIO_UNITARIO);
		}
		int filaPercepcion = getNumeroFilaPercepcion();
		if(filaPercepcion > -1){
			getTablaProductos().lockCell(filaPercepcion, COL_CANTIDAD);
			getTablaProductos().lockCell(filaPercepcion, COL_PRECIO_UNITARIO);
		}
	}

	private int getNumeroFilaSeguro() {
		for (int i = 0; i < getTablaProductos().getRowCount(); i++) {
			if (getTablaProductos().getValueAt(i, COL_ARTICULO).equals("SEGURO")) {
				return i;
			}
		}
		return -1;
	}
	
	private int getNumeroFilaPercepcion() {
		for (int i = 0; i < getTablaProductos().getRowCount(); i++) {
			if (getTablaProductos().getValueAt(i, COL_ARTICULO).equals("PERCEP.")) {
				return i;
			}
		}
		return -1;
	}

	private double getValorParaSeguro() {
		double suma = 0;
		for (ItemFactura it : getFactura().getItems()) {
			if (!(it instanceof ItemFacturaSeguro) && !(it instanceof ItemFacturaPercepcion)) {
				suma += it.getImporte().doubleValue();
			}
		}
		return suma;
	}

	private Object[] getFilaItemPrecioMateriaPrima(ItemFacturaPrecioMateriaPrima ifactura) {
		Object[] fila = new Object[CANT_COLS_TBL_FACTURA];
		fila[COL_CANTIDAD] = ifactura.getCantidad();
		fila[COL_ARTICULO] = ifactura.getPrecioMateriaPrima().getMateriaPrima().getTipo().getDescripcion().toUpperCase();
		fila[COL_DESCRIPCION] = ifactura.getDescripcion();
		fila[COL_UNIDAD] = ifactura.getUnidad().getDescripcion();
		fila[COL_PRECIO_UNITARIO] = ifactura.getPrecioUnitario();// getDecimalFormat().format(ifactura.getPrecioUnitario());
		fila[COL_IMPORTE] =(ifactura.getImporte().doubleValue()<1?String.valueOf(ifactura.getImporte()): getDecimalFormat().format(ifactura.getImporte().doubleValue()));
		fila[COL_OBJ_FACTURA] = ifactura;
		return fila;
	}

	private Object[] getFilaSeguro(ItemFacturaSeguro itfs) {
		Object[] fila = new Object[CANT_COLS_TBL_FACTURA];
		fila[COL_CANTIDAD] = new BigDecimal(1);
		fila[COL_ARTICULO] = "SEGURO";
		fila[COL_DESCRIPCION] = itfs.getDescripcion();
		fila[COL_UNIDAD] = itfs.getUnidad().getDescripcion();
		// fila[COL_PRECIO_UNITARIO] = itfs.getPorcentajeSeguro();
		double doubleValue = itfs.getImporte().doubleValue();
		fila[COL_IMPORTE] = GenericUtils.getDecimalFormat().format(doubleValue);
		fila[COL_OBJ_FACTURA] = itfs;
		return fila;
	}

	private Object[] getFilaTubo(ItemFacturaTubo itft) {
		Object[] fila = new Object[CANT_COLS_TBL_FACTURA];
		fila[COL_CANTIDAD] = itft.getCantidad();
		fila[COL_ARTICULO] = "TUBOS";
		fila[COL_DESCRIPCION] = itft.getDescripcion();
		fila[COL_UNIDAD] = itft.getUnidad().getDescripcion();
		fila[COL_PRECIO_UNITARIO] =itft.getPrecioUnitario();// getDecimalFormat().format(itft.getPrecioUnitario());
		fila[COL_IMPORTE] =(itft.getImporte().doubleValue()<1?String.valueOf(itft.getImporte()): getDecimalFormat().format(itft.getImporte().doubleValue()));
		fila[COL_OBJ_FACTURA] = itft;
		return fila;
	}

	private Object[] getFilaProducto(ItemFacturaProducto itf) {
		Object[] fila = new Object[CANT_COLS_TBL_FACTURA];
		if(itf.getProductoArticulo().getArticulo()!=null){
			fila[COL_ARTICULO] = itf.getProductoArticulo().getArticulo().getNombre();
		}else{
			fila[COL_ARTICULO] = " - ";
		}
		fila[COL_CANTIDAD] =itf.getCantidad();// getDecimalFormat().format(itf.getCantidad());
		fila[COL_DESCRIPCION] = itf.getDescripcion();
		fila[COL_UNIDAD] = itf.getUnidad().getDescripcion();
		BigDecimal precioProducto = getPrecioProducto(itf);
		itf.setImporte(precioProducto.multiply(itf.getCantidad()));
		fila[COL_PRECIO_UNITARIO] = getDecimalFormat().format(precioProducto.doubleValue());
		fila[COL_IMPORTE] = getDecimalFormat().format(itf.getImporte().doubleValue());
		fila[COL_OBJ_FACTURA] = itf;
		return fila;
	}
	
	private BigDecimal getPrecioProducto(ItemFacturaProducto itf) {
		ListaDePreciosFacadeRemote listaDePreciosFacadeRemote = GTLBeanFactory.getInstance().getBean2(ListaDePreciosFacadeRemote.class);
		if(versionListaPreciosActual == null) {
			versionListaPreciosActual = listaDePrecios.getVersionActual();
		}
		DefinicionPrecio definicionPorTipoProducto = versionListaPreciosActual.getDefinicionPorTipoProducto(itf.getProductoArticulo().getProducto().getTipo());
		if(definicionPorTipoProducto == null) {
			return itf.getPrecioUnitario();
		}
		Float precioLista = listaDePreciosFacadeRemote.getPrecioProductoPorVersion(itf.getProductoArticulo(), versionListaPreciosActual.getId());
		if(precioLista == null){
			return itf.getPrecioUnitario();
		}
		return new BigDecimal(precioLista);
	}

	private Object[] getFilaBonificacion(ItemFacturaBonificacion itf) {
		Object[] fila = new Object[CANT_COLS_TBL_FACTURA];
		fila[COL_ARTICULO] = "BONIFICACION";
		fila[COL_CANTIDAD] = itf.getCantidad();
		fila[COL_DESCRIPCION] = itf.getDescripcion();
		fila[COL_UNIDAD] = itf.getUnidad().getDescripcion();
		fila[COL_PRECIO_UNITARIO] = itf.getPrecioUnitario();//getDecimalFormat().format(itf.getPrecioUnitario());
		fila[COL_IMPORTE] = getDecimalFormat().format(itf.getImporte().doubleValue());
		fila[COL_OBJ_FACTURA] = itf;
		return fila;
	}

	private Object[] getFilaRecargo(ItemFacturaRecargo itf) {
		Object[] fila = new Object[CANT_COLS_TBL_FACTURA];
		fila[COL_ARTICULO] = "RECARGO";
		fila[COL_CANTIDAD] = itf.getCantidad();
		fila[COL_DESCRIPCION] = itf.getDescripcion();
		fila[COL_PRECIO_UNITARIO] =itf.getPrecioUnitario();// getDecimalFormat().format(itf.getPrecioUnitario());
		fila[COL_UNIDAD] = itf.getUnidad().getDescripcion();
		fila[COL_IMPORTE] = getDecimalFormat().format(itf.getImporte().doubleValue());
		fila[COL_OBJ_FACTURA] = itf;
		return fila;
	}
	
	private Object[] getFilaPercepcion(ItemFacturaPercepcion itfs) {
		Object[] fila = new Object[CANT_COLS_TBL_FACTURA];
		fila[COL_CANTIDAD] = new BigDecimal(1);
		fila[COL_ARTICULO] = "PERCEP.";
		fila[COL_DESCRIPCION] = itfs.getDescripcion();
		fila[COL_UNIDAD] = itfs.getUnidad().getDescripcion();
		fila[COL_IMPORTE] =itfs.getImporte();// getDecimalFormat().format(itfs.getImporte().doubleValue());
		fila[COL_OBJ_FACTURA] = itfs;
		return fila;
	}

	private void construct() {
		setUpScreen();
		setUpComponentes();
	}

	private void setUpComponentes() {
		add(getPanelSuperior(), BorderLayout.NORTH);
		add(getPanelCentral(), BorderLayout.CENTER);
		add(getPanelAcciones(), BorderLayout.SOUTH);
	}

	private JPanel getPanelCentral() {
		JPanel panel = new JPanel();
		panel.setLayout(new VerticalFlowLayout(VerticalFlowLayout.CENTER, 5, 2));
		panel.add(getPanelTablaProductos());
		panel.add(getPanelTotales());
		return panel;
	}

	private JPanel getPanelSuperior() {
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 2));
		JPanel pnlCon = new JPanel();
		pnlCon.setLayout(new VerticalFlowLayout(VerticalFlowLayout.TOP, 5, 2));
		pnlCon.add(getPanelHeader());
		pnlCon.add(getPanelDatosCliente());
		pnlCon.add(getPanelDatosFactura());
		panel.add(pnlCon);
		return panel;
	}

	private JPanel getPanelHeader() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.add(new JLabel("N�: "), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
		panel.add(getTxtNroFactura(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
		panel.add(getLblTipoDocumento(), GenericUtils.createGridBagConstraints(2, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
		panel.add(getLblTipoFactura(), GenericUtils.createGridBagConstraints(3, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
		panel.add(new JLabel("Fecha: "), GenericUtils.createGridBagConstraints(4, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
		panel.add(getPanelFecha(), GenericUtils.createGridBagConstraints(5, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
		return panel;
	}

	private void setUpScreen() {
		setSize(new Dimension(850, 650));
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle("Alta de factura");
		setResizable(false);
		setModal(true);
		GuiUtil.centrar(this);
	}

	private FWJTable getTablaProductos() {
		if (tablaProductos == null) {
			tablaProductos = new FWJTable(0, CANT_COLS_TBL_FACTURA) {

				private static final long serialVersionUID = -7960992084800303941L;

				@Override
				public void newRowSelected(int newRow, int oldRow) {
					getBtnQuitarProducto().setEnabled(newRow != -1);
				}
//				
//				LO SAQUE PORQUE NO ME PINTA EL FONDO DE LAS CELDAS
//				
//				public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
//					Component c = super.prepareRenderer(renderer, row, column);
//					if (c instanceof JComponent) {
//						((JComponent) c).setOpaque(false);
//					}
//					return c;
//				}

				@Override
				public void cellEdited(int cell, int row) {
					try{
						if (cell == COL_PRECIO_UNITARIO) {
							String valor = ((String) getValueAt(row, cell)).trim();
							if (GenericUtils.esNumerico(valor)) {
								setBackgroundCell(row, cell, Color.WHITE);
								CeldaTablaProductos celda = new CeldaTablaProductos(row, cell);
								Integer cantErrorFila = getMapFilaCantidadErrores().get(celda);
								if(cantErrorFila != null && cantErrorFila > 0){
									cantErrorFila--;
									getMapFilaCantidadErrores().put(celda, cantErrorFila);
								}
								double pu = Double.valueOf(valor);
								Object valueAt = getValueAt(row, COL_CANTIDAD);
								BigDecimal newValue = null;
								if(valueAt instanceof String){
									String value = ((String)valueAt);
									/*String[] separacionAux = value.split(".")
									String[] separacion = value.split(",");
									if(separacion.length>1){
										value = separacion[0].replace(".", "")+"."+separacion[1];
									}else{
										value = separacion[0].replace(".", "");
									}*/
									newValue = new BigDecimal(Double.valueOf(value));
								}else{
									newValue = (BigDecimal)valueAt;
								}
								double cantidad = newValue.doubleValue();
								ItemFactura itemModificado = (ItemFactura) getValueAt(row, COL_OBJ_FACTURA);
								actualizarItemFactura(pu * cantidad,cantidad,pu, itemModificado);
								if (isFactura()) {
									actualizarSeguro();
								}
								calcularSubTotal();
								setValueAt(getDecimalFormat().format(new BigDecimal(pu * cantidad)), row, COL_IMPORTE);
							}else{
								setBackgroundCell(row, cell, Color.YELLOW);
								CeldaTablaProductos celda = new CeldaTablaProductos(row, cell);
								Integer cantErrorFila = getMapFilaCantidadErrores().get(celda);
								if(cantErrorFila == null){
									cantErrorFila = 1;
								}else if(cantErrorFila == 0){
									cantErrorFila++;
								}
								getMapFilaCantidadErrores().put(celda, cantErrorFila);
							}
							return;
						}
						if (cell == COL_DESCRIPCION) {
							if (getValueAt(row, COL_OBJ_FACTURA) instanceof ItemFactura) {
								ItemFactura itemModificado = (ItemFactura) getValueAt(row, COL_OBJ_FACTURA);
								String descripcion = (String) getValueAt(row, COL_DESCRIPCION);
								if (descripcion.trim().length() == 0) {
									FWJOptionPane.showErrorMessage(JDialogCargaFactura.this, "Se debe especificar la descripci�n", "Error");
									return;
								}
								/*CeldaTablaProductos celda = new CeldaTablaProductos(0, COL_DESCRIPCION);
								Integer cantErrorFila = getMapFilaCantidadErrores().get(celda);
								if(cantErrorFila != null && cantErrorFila > 0){
									cantErrorFila--;
									getMapFilaCantidadErrores().put(celda, cantErrorFila);
								}*/
								setBackgroundCell(0, COL_DESCRIPCION, Color.WHITE);
								actualizarItemFactura(descripcion, itemModificado);
							}
							return;
						}
						if(cell == COL_CANTIDAD){
							String valor = ((String) getValueAt(row, cell)).trim();
							if (GenericUtils.esNumerico(valor)) {
								setBackgroundCell(row, cell, Color.WHITE);
								CeldaTablaProductos celda = new CeldaTablaProductos(row, cell);
								Integer cantErrorFila = getMapFilaCantidadErrores().get(celda);
								if(cantErrorFila != null && cantErrorFila > 0){
									cantErrorFila--;
									getMapFilaCantidadErrores().put(celda, cantErrorFila);
								}
								double cantidad = Double.valueOf(valor);
								Object valueAt = getValueAt(row, COL_PRECIO_UNITARIO);
								BigDecimal newValue = null;
								if(valueAt instanceof String){
									String s = (String) valueAt;
									int lastIndexOf = s.lastIndexOf(",");
									String s2 = s.substring(0, lastIndexOf) + "." + s.substring(lastIndexOf + 1);
									newValue = new BigDecimal(Double.valueOf(s2));
								}else{
									newValue = (BigDecimal)valueAt;
								}
								double pu = newValue.doubleValue();
								if (isFactura()){
									ItemFactura itemModificado = (ItemFactura) getValueAt(row, COL_OBJ_FACTURA);
									if(itemModificado instanceof ItemFacturaPrecioMateriaPrima){
										PrecioMateriaPrima pm = ((ItemFacturaPrecioMateriaPrima)itemModificado).getPrecioMateriaPrima();
										BigDecimal cant = new BigDecimal(valor);
										BigDecimal stockGuardado = getPrecioMateriaPrimaFacade().getStockByPrecioMateriaPrima(pm);
										if(cant.compareTo(stockGuardado)==1){
											FWJOptionPane.showErrorMessage(JDialogCargaFactura.this, "El stock debe ser menor o igual a " + stockGuardado.doubleValue(), "Error");
											setBackgroundCell(row, cell, Color.YELLOW);
											CeldaTablaProductos celda2 = new CeldaTablaProductos(row, cell);
											Integer cantErrorFila2 = getMapFilaCantidadErrores().get(celda);
											if(cantErrorFila2 == null){
												cantErrorFila2 = 1;
											}else if(cantErrorFila2 == 0){
												cantErrorFila2++;
											}
											getMapFilaCantidadErrores().put(celda2, cantErrorFila2);
											return;
										}
									}
									actualizarItemFactura(pu * cantidad, cantidad,pu, itemModificado);
									actualizarSeguro();
									calcularSubTotal();
								} else {
									BigDecimal montoSubtotal = new BigDecimal(pu * cantidad);
									getCorrecionFactura().setMontoSubtotal(montoSubtotal);
									getTxtSubTotal().setText(String.valueOf(montoSubtotal.doubleValue()));
									updateTotalesCorrecion();
								}
								setValueAt(getDecimalFormat().format(new BigDecimal(pu * cantidad)), row, COL_IMPORTE);
							}else{
								setBackgroundCell(row, cell, Color.YELLOW);
								CeldaTablaProductos celda = new CeldaTablaProductos(row, cell);
								Integer cantErrorFila = getMapFilaCantidadErrores().get(celda);
								if(cantErrorFila == null){
									cantErrorFila = 1;
								}else if(cantErrorFila == 0){
									cantErrorFila++;
								}
								getMapFilaCantidadErrores().put(celda, cantErrorFila);
							}
							return;
						}
					}catch(NumberFormatException nfe){
						FWJOptionPane.showErrorMessage(JDialogCargaFactura.this, StringW.wordWrap("Ha ocurrido un error en la conversi�n de precios. Por favor, revise si los datos son correctos."), "Error");
						System.out.println("error de formateo de numerooooooooooooooooo");
					}
				}
			};
			tablaProductos.setReorderingAllowed(false);
			tablaProductos.setStringColumn(COL_ARTICULO, "Articulo", 230, 130, true);
			tablaProductos.setFloatColumn(COL_CANTIDAD, "Cantidad",0f,9999999999999f, 50,  !isFactura());
			tablaProductos.setStringColumn(COL_UNIDAD, "Unidad", 150, 50, true);
			tablaProductos.setStringColumn(COL_DESCRIPCION, "Descripcion", 150, 280, false);
			tablaProductos.setFloatColumn(COL_PRECIO_UNITARIO, "Precio Unitario",0f,9999999999999f, 100, isFactura());
			tablaProductos.setStringColumn(COL_IMPORTE, "Importe", 150, 100, true);
			tablaProductos.setStringColumn(COL_OBJ_FACTURA, "", 0, 0, true);
		}
		return tablaProductos;
	}

	private boolean isFactura() {
		return getCorrecionFactura() == null;
	}

	private FWJTextField getTxtSubTotal() {
		if (txtSubTotal == null) {
			txtSubTotal = new FWJTextField();
			txtSubTotal.setEditable(false);
		}
		return txtSubTotal;
	}

	private FWJTextField getTxtImpuestos() {
		if (txtImpuestos == null) {
			txtImpuestos = new FWJTextField();
			txtImpuestos.setEnabled(false);
			txtImpuestos.setEditable(false);
			txtImpuestos.addKeyListener(new KeyAdapter() {

				@Override
				public void keyReleased(KeyEvent e) {
					if (getTxtImpuestos().getText().length() > 0) {
						if (GenericUtils.esNumerico(getTxtImpuestos().getText())) {
							updateTotales();
						} else {
							getTxtTotal().setText("");
							getTxtTotal().repaint();
						}
					} else {
						getTxtSubTotalConImpuestos().setText(String.valueOf(Double.valueOf(getTxtSubTotal().getText())));
						getTxtSubTotalConImpuestos().repaint();
						getTxtImporteIVAInscripto().setText("");
						getTxtImporteIVAInscripto().repaint();
						getTxtImporteIVANoInscripto().setText("");
						getTxtImporteIVAInscripto().repaint();
					}
				}
			});
		}
		return txtImpuestos;
	}

	private FWJTextField getTxtSubTotalConImpuestos() {
		if (txtSubTotalConImpuestos == null) {
			txtSubTotalConImpuestos = new FWJTextField();
			txtSubTotalConImpuestos.setEditable(false);
		}
		return txtSubTotalConImpuestos;
	}

	private FWJTextField getTxtPorcentajeIVA() {
		if (txtPorcentajeIVA == null) {
			txtPorcentajeIVA = new FWJTextField();
			txtPorcentajeIVA.setPreferredSize(new Dimension(40, 20));
			if(getFactura()!=null){
				txtPorcentajeIVA.setText(getFactura().getPorcentajeIVAInscripto() != null ? getDecimalFormat().format(getFactura().getPorcentajeIVAInscripto()) : "");
			}
			if(getCorrecionFactura()!=null){
				txtPorcentajeIVA.setText(getCorrecionFactura().getPorcentajeIVAInscripto() != null ? getDecimalFormat().format(getCorrecionFactura().getPorcentajeIVAInscripto()) : "");
			}

			txtPorcentajeIVA.setEditable(false);
		}
		return txtPorcentajeIVA;
	}

	private FWJTextField getTxtImporteIVAInscripto() {
		if (txtImporteIVAInscripto == null) {
			txtImporteIVAInscripto = new FWJTextField();
			txtImporteIVAInscripto.setEditable(false);
		}
		return txtImporteIVAInscripto;
	}

	private FWJTextField getTxtImporteIVANoInscripto() {
		if (txtImporteIVANoInscripto == null) {
			txtImporteIVANoInscripto = new FWJTextField();
			txtImporteIVANoInscripto.setEditable(false);
		}
		return txtImporteIVANoInscripto;
	}

	private FWJTextField getTxtPorcentajeIVANoInscripto() {
		if (txtPorcentajeIVANoInscripto == null) {
			txtPorcentajeIVANoInscripto = new FWJTextField();
			txtPorcentajeIVANoInscripto.setPreferredSize(new Dimension(30, 20));
			// txtPorcentajeIVANoInscripto.setText(String.valueOf(getParametrosGeneralesFacade().getParametrosGenerales().getPorcentajeIVANoInscripto().intValue()));
			txtPorcentajeIVANoInscripto.setEditable(false);
		}
		return txtPorcentajeIVANoInscripto;
	}

	private FWJTextField getTxtTotal() {
		if (txtTotal == null) {
			txtTotal = new FWJTextField();
			txtTotal.setEditable(false);
		}
		return txtTotal;
	}

	private FWJTextField getTxtRazonSocial() {
		if (txtRazonSocial == null) {
			txtRazonSocial = new FWJTextField();
			txtRazonSocial.setEditable(false);
		}
		return txtRazonSocial;
	}

	private FWJTextField getTxtDireccion() {
		if (txtDireccion == null) {
			txtDireccion = new FWJTextField();
			txtDireccion.setPreferredSize(new Dimension(200, 20));
			txtDireccion.setEditable(false);
		}
		return txtDireccion;
	}

	private FWJTextField getTxtLocalidad() {
		if (txtLocalidad == null) {
			txtLocalidad = new FWJTextField();
			txtLocalidad.setPreferredSize(new Dimension(200, 20));
			txtLocalidad.setEditable(false);
		}
		return txtLocalidad;
	}

	private FWJTextField gettxtCondicionIVA() {
		if (txtCondicionIVA == null) {
			txtCondicionIVA = new FWJTextField();
			txtCondicionIVA.setPreferredSize(new Dimension(150, 20));
			txtCondicionIVA.setEditable(false);
		}
		return txtCondicionIVA;
	}

	private FWJTextField getTxtCuit() {
		if (txtCuit == null) {
			txtCuit = new FWJTextField();
			txtCuit.setPreferredSize(new Dimension(147, 20));
			txtCuit.setEditable(false);
		}
		return txtCuit;
	}

	private JComboBox getCmbCondicionVenta() {
		if (cmbCondicionVenta == null) {
			cmbCondicionVenta = new JComboBox();
			cmbCondicionVenta.setPreferredSize(new Dimension(200, 20));
			GuiUtil.llenarCombo(cmbCondicionVenta, getCondicionDeVentaFacade().getAllOrderByName(), true);
		}
		return cmbCondicionVenta;
	}

	private PanelRemitos getPnlNroRemito() {
		if (pnlNroRemito == null) {
			pnlNroRemito = new PanelRemitos(getRemitos());
		}
		return pnlNroRemito;
	}

	private class PanelRemitos extends JPanel{

		private static final long serialVersionUID = -1820746456782160664L;
		
		private final List<RemitoSalida> remitos;
		private RemitoSalidaLinkeableLabel[] remitoLinkeableLabelList;
		private static final int CANT_MAX_REMITOS_SELECTED_HARDCODE = 3;
		
		private PanelRemitos(List<RemitoSalida> remitos){
			this.remitos = remitos;
			if(remitos!=null){
				this.remitoLinkeableLabelList = new RemitoSalidaLinkeableLabel[CANT_MAX_REMITOS_SELECTED_HARDCODE];
				for(int i = 0; i < CANT_MAX_REMITOS_SELECTED_HARDCODE; i++) {
					LinkableLabel lblConsultaRemito = new RemitoSalidaLinkeableLabel();
					remitoLinkeableLabelList[i] = (RemitoSalidaLinkeableLabel)lblConsultaRemito;
				}
				JPanel pnlRemitos = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
				for (int i = 0; i < Math.min(getRemitos().size(), CANT_MAX_REMITOS_SELECTED_HARDCODE); i++) {
					RemitoSalidaLinkeableLabel remitoLinkeableLabel = remitoLinkeableLabelList[i];
					remitoLinkeableLabel.setRemito(getRemitos().get(i));
					remitoLinkeableLabel.setVisible(true);
					pnlRemitos.add(remitoLinkeableLabel);
				}
				add(pnlRemitos,BorderLayout.CENTER);
				setBorder(BorderFactory.createLineBorder(Color.RED.darker()));
			}
		}
		
		public List<RemitoSalida> getRemitos() {
			return remitos;
		}
	}
	
	private FWJTextField getTxtNroFactura() {
		if (txtNroFactura == null) {
			txtNroFactura = new FWJTextField();
			txtNroFactura.setPreferredSize(new Dimension(150, 20));
			String nro; //StringUtil.fillLeftWithZeros(String.valueOf(getParametrosGenerales().getNroSucursal()), 4) + "-";
			if (getFactura() != null) {
				if(getFactura().getNroSucursal() == null) {
					nro = StringUtil.fillLeftWithZeros(String.valueOf(getParametrosGenerales().getNroSucursal()), 4) + "-";
				}else{
					nro = StringUtil.fillLeftWithZeros(String.valueOf(getFactura().getNroSucursal()), 4) + "-";
				}
				nro += StringUtil.fillLeftWithZeros(String.valueOf(getFactura().getNroFactura()), 8);
			} else {
				if(getCorrecionFactura().getNroSucursal() == null) {
					nro = StringUtil.fillLeftWithZeros(String.valueOf(getParametrosGenerales().getNroSucursal()), 4) + "-";
				}else{
					nro = StringUtil.fillLeftWithZeros(String.valueOf(getCorrecionFactura().getNroSucursal()), 4) + "-";
				}
				nro += StringUtil.fillLeftWithZeros(String.valueOf(getCorrecionFactura().getNroFactura()), 8);
			}
			txtNroFactura.setText(nro);
			txtNroFactura.setEditable(false);
		}
		return txtNroFactura;
	}

	private JLabel getLblTipoDocumento() {
		if (lblTipoDocumento == null) {
			lblTipoDocumento = new JLabel();
			lblTipoDocumento.setPreferredSize(new Dimension(160, 20));
			lblTipoDocumento.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			lblTipoDocumento.setHorizontalAlignment(JLabel.CENTER);
			if (!isFactura()) {
				lblTipoDocumento.setText(getCorrecionFactura().getTipo().getDescripcion());
			} else if (getFactura() != null || getRemitos() != null) {
				lblTipoDocumento.setText("FACTURA");
			}
			lblTipoDocumento.setText((getRemitos() == null ? (getTipoCorrecion() == null ? (getFactura() == null ? "" : "FACTURA") : getTipoCorrecion().getDescripcion()) : "FACTURA"));
			lblTipoDocumento.setFont(new Font("Tahoma", Font.BOLD, 18));
		}
		return lblTipoDocumento;
	}

	private JPanel getPanelDatosFactura() {
		if (panelDatosFactura == null) {
			panelDatosFactura = new JPanel();
			panelDatosFactura.setLayout(new GridBagLayout());
			panelDatosFactura.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
			panelDatosFactura.add(new JLabel("IVA: "), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelDatosFactura.add(gettxtCondicionIVA(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panelDatosFactura.add(new JLabel("C.U.I.T: "), GenericUtils.createGridBagConstraints(2, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelDatosFactura.add(getTxtCuit(), GenericUtils.createGridBagConstraints(3, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panelDatosFactura.add(new JLabel("Condicion de venta: "), GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0,0));
			panelDatosFactura.add(getCmbCondicionVenta(), GenericUtils.createGridBagConstraints(1, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			JLabel label = null;
			if(getFactura()!=null && getFactura().getRemitos()!=null){
				label = new JLabel("Remito N�: ");
			}else if((getCorrecionFactura()!= null && getTipoCorrecion() == ETipoCorreccionFactura.NOTA_DEBITO) || (getFactura()!=null && getFactura().getRemitos()==null)){
				label = new JLabel("");
			}else if(getCorrecionFactura()!= null && getTipoCorrecion() == ETipoCorreccionFactura.NOTA_CREDITO){
				label = getLblElegirFactura();
			}
			panelDatosFactura.add(label, GenericUtils.createGridBagConstraints(2, 1, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			if ((!isFactura() && getTipoCorrecion() == ETipoCorreccionFactura.NOTA_DEBITO) || (getFactura()!=null && getFactura().getRemitos()!=null) ) {
				panelDatosFactura.add(getPnlNroRemito(), GenericUtils.createGridBagConstraints(3, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			}else{
				panelDatosFactura.add(getTxtNrosGenericos(), GenericUtils.createGridBagConstraints(3, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			}
		}
		return panelDatosFactura;
	}
	
	private LinkableLabel getLblElegirFactura() {
		if (lblElegirFactura == null) {
			lblElegirFactura = new LinkableLabel("Facturas: ") {

				private static final long serialVersionUID = 580819185565135378L;

				@Override
				public void labelClickeada(MouseEvent e) {
					if (e.getClickCount() == 1) {
						JDialogSeleccionarFacturasImpagasCliente dialogSeleccionarFacturas =  new JDialogSeleccionarFacturasImpagasCliente(JDialogCargaFactura.this, 
								((NotaCredito)getCorrecionFactura()).getFacturasRelacionadas(),getCliente().getId());
						
						GuiUtil.centrar(dialogSeleccionarFacturas);
						dialogSeleccionarFacturas.setVisible(true);
						if(dialogSeleccionarFacturas.isAcepto()) {
							List<Factura> facturas = dialogSeleccionarFacturas.getFacturaSelectedList();
							((NotaCredito)getCorrecionFactura()).setFacturasRelacionadas(new ArrayList<Factura>());
							((NotaCredito)getCorrecionFactura()).getFacturasRelacionadas().addAll(facturas);
							getTxtNrosGenericos().setText(getNrosFacturasRelacionadas(facturas));
						}
					}
				}
			};
		}
		return lblElegirFactura;
	}
	
	private String getNrosFacturasRelacionadas(List<Factura> facturas){
		StringBuilder sb = new StringBuilder();
//		sb.append(StringUtil.fillLeftWithZeros(String.valueOf(getParametrosGenerales().getNroSucursal()), 4) + "-");
		boolean primera = true;
		for(Factura f : facturas){
			if(primera){
				sb.append(StringUtil.fillLeftWithZeros(String.valueOf(f.getNroSucursal()), 4) + "-");
				sb.append(StringUtil.fillLeftWithZeros(String.valueOf(f.getNroFactura()), 8));
				primera = false;
				continue;
			}
			sb.append("/");
			sb.append(f.getNroFactura());
		}
		String facRel = sb.toString();
		setStrFacturasRelacionadas(facRel);
		return facRel;
	}

	private JPanel getPanelDatosCliente() {
		if (panelDatosCliente == null) {
			panelDatosCliente = new JPanel();
			panelDatosCliente.setLayout(new GridBagLayout());
			panelDatosCliente.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
			panelDatosCliente.add(new JLabel("Se�or/es: "), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelDatosCliente.add(getTxtRazonSocial(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 3, 1, 1, 0));
			panelDatosCliente.add(new JLabel("Direccion: "), GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelDatosCliente.add(getTxtDireccion(), GenericUtils.createGridBagConstraints(1, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panelDatosCliente.add(new JLabel("Localidad: "), GenericUtils.createGridBagConstraints(2, 1, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelDatosCliente.add(getTxtLocalidad(), GenericUtils.createGridBagConstraints(3, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
		}
		return panelDatosCliente;
	}

	private JPanel getPanelTablaProductos() {
		if (panelTablaProductos == null) {
			panelTablaProductos = new JPanel();
			panelTablaProductos.setLayout(new FlowLayout(FlowLayout.CENTER, 2, 2));
			JScrollPane scrollPane = new JScrollPane(getTablaProductos());
			scrollPane.setPreferredSize(new Dimension(730, 300));
			scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			panelTablaProductos.add(scrollPane, BorderLayout.CENTER);
			panelTablaProductos.add(getPanelBotonesFactura());
		}
		return panelTablaProductos;
	}

	private JPanel getPanelBotonesFactura() {
		JPanel panel = new JPanel();
		panel.setLayout(new VerticalFlowLayout(VerticalFlowLayout.TOP, 2, 2));
		panel.add(getBtnAgregarProducto());
		panel.add(getBtnQuitarProducto());
		panel.add(getBtnBuscarListaDePrecios());
		panel.add(getBtnQuitarListaDePrecios());
		return panel;
	}

	private Integer getCantidadPiezasRemito() {
		Integer suma = 0;
		for(RemitoSalida r : getRemitos()){
			suma += r.getCantidadPiezasParaEstimarTubos();
		}
		return suma;
	}
	
	private JButton getBtnQuitarListaDePrecios() {
		if(btnQuitarListaDePrecios == null) {
			btnQuitarListaDePrecios =BossEstilos.createButton("ar/com/textillevel/imagenes/b_quitar_lista_precios.png", "ar/com/textillevel/imagenes/b_quitar_lista_precios_des.png");
			btnQuitarListaDePrecios.setEnabled(false);
			btnQuitarListaDePrecios.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					btnQuitarListaDePrecios.setEnabled(false);
					versionListaPreciosActual = null;
					lblVersionListaPrecios.setText(DateUtil.dateToString(listaDePrecios.getVersiones().get(listaDePrecios.getVersiones().size() - 1).getInicioValidez(), DateUtil.SHORT_DATE));
					llenarTablaProductos();
				}
			});
		}
		return btnQuitarListaDePrecios;
	}
	
	private JButton getBtnBuscarListaDePrecios(){
		if(btnBuscarListaDePrecios == null){
			btnBuscarListaDePrecios = BossEstilos.createButton("ar/com/textillevel/imagenes/b_buscar_lista_precios.png", "ar/com/textillevel/imagenes/b_buscar_lista_precios_des.png");
			btnBuscarListaDePrecios.setEnabled(false);
			btnBuscarListaDePrecios.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					if(listaDePrecios == null || listaDePrecios.getVersiones() == null || listaDePrecios.getVersiones().isEmpty()){
						FWJOptionPane.showWarningMessage(JDialogCargaFactura.this, "El cliente no tiene cargada una lista de precios", "Advertencia");
						return;
					}
					
					List<VersionListaDePrecios> versiones = new ArrayList<VersionListaDePrecios>(listaDePrecios.getVersiones());
					Collections.sort(versiones, new Comparator<VersionListaDePrecios>() {

						@Override
						public int compare(VersionListaDePrecios v1, VersionListaDePrecios v2) {
							return v2.getInicioValidez().compareTo(v1.getInicioValidez());
						}
					});
					
					VersionListaDePrecios[] versionesArray= new VersionListaDePrecios[versiones.size()];
					for(int i = 0 ; i< versiones.size();i++){
						versionesArray[i] = versiones.get(i);
					}
					Object opcion = JOptionPane.showInputDialog(null, "Seleccione la versi�n que desea utilizar:", "Lista de opciones", JOptionPane.INFORMATION_MESSAGE, null, versionesArray,versionesArray[0]);
					if(opcion!=null){
						versionListaPreciosActual = (VersionListaDePrecios) opcion;
						lblVersionListaPrecios.setText(DateUtil.dateToString(versionListaPreciosActual.getInicioValidez(), DateUtil.SHORT_DATE));
						getBtnQuitarListaDePrecios().setEnabled(true);
						llenarTablaProductos();
					}
				}
			});
		}
		return btnBuscarListaDePrecios;
	}
	private JButton getBtnAgregarProducto() {
		if (btnAgregarProducto == null) {
			btnAgregarProducto = BossEstilos.createButton("ar/com/fwcommon/imagenes/b_agregar.png", "ar/com/fwcommon/imagenes/b_agregar_des.png");
			btnAgregarProducto.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if(isFactura()) {
						JDialogAgregarItemFactura jdaif = new JDialogAgregarItemFactura(JDialogCargaFactura.this, getRemitos()==null?1:getCantidadPiezasRemito(), getCliente(), getParametrosGenerales().getPrecioPorTubo(), getParametrosGenerales().getPorcentajeSeguro());
						GuiUtil.centrar(jdaif);
						jdaif.setVisible(true);
						if (jdaif.isAcepto()) {
							ItemFactura it = jdaif.getItemFacturaSeleccionado();
							
							if (it instanceof ItemFacturaSeguro) {
								if (haySeguro()) {
									FWJOptionPane.showErrorMessage(JDialogCargaFactura.this, "Ya se ha cargado el seguro.", "Error");
									return;
								}
							}
							
							if (it instanceof ItemFacturaTubo) {
								if (hayTubos()) {
									FWJOptionPane.showErrorMessage(JDialogCargaFactura.this, "Ya se han cargado los tubos.", "Error");
									return;
								}
							}
							
							if (it instanceof ItemFacturaPercepcion) {
								if (hayPercepcion()) {
									FWJOptionPane.showErrorMessage(JDialogCargaFactura.this, "Ya se ha cargado la percepci�n.", "Error");
									return;
								}
							}
							List<ItemFactura> items = new ArrayList<ItemFactura>(getFactura().getItems());
							getFactura().getItems().clear();
							ItemFacturaSeguro its = null;
							for(ItemFactura item : items){
								if(item instanceof ItemFacturaSeguro){
									its = (ItemFacturaSeguro) item;
								}else{
									getFactura().getItems().add(item);
								}
							}
							getFactura().getItems().add(it);
							if(its!=null){
								getFactura().getItems().add(its);
							}
						}
					} else {
						addItemFacturaCorreccionVacio();
					}
					actualizarSeguro();
					llenarTablaProductos();
				}
			});
		}
		return btnAgregarProducto;
	}

	private boolean hayPercepcion() {
		for (ItemFactura it : getFactura().getItems()) {
			if (it instanceof ItemFacturaPercepcion) {
				return true;
			}
		}
		return false;
	}

	private boolean hayTubos() {
		for (ItemFactura it : getFactura().getItems()) {
			if (it instanceof ItemFacturaTubo) {
				return true;
			}
		}
		return false;
	}

	private boolean haySeguro() {
		for (ItemFactura it : getFactura().getItems()) {
			if (it instanceof ItemFacturaSeguro) {
				return true;
			}
		}
		return false;
	}

	private JButton getBtnQuitarProducto() {
		if (btnQuitarProducto == null) {
			btnQuitarProducto = BossEstilos.createButton("ar/com/fwcommon/imagenes/b_eliminar.png", "ar/com/fwcommon/imagenes/b_eliminar_des.png");
			btnQuitarProducto.setEnabled(false);
			btnQuitarProducto.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if (getTablaProductos().getSelectedRow() > -1) {
						ItemFactura itf = (ItemFactura) getTablaProductos().getValueAt(getTablaProductos().getSelectedRow(), COL_OBJ_FACTURA);
						getDocContable().getItems().remove(itf);
						getTablaProductos().removeRow(getTablaProductos().getSelectedRow());
						actualizarSeguro();
						calcularSubTotal();

						if(itemIsCilindro(itf)) {
							getRemitoEntradaDibujo().getItems().clear();
							getNroDibujoTracker().clear();
						}

					} else {
						getBtnQuitarProducto().setEnabled(false);
					}
				}

			});
		}
		return btnQuitarProducto;
	}

	private boolean itemIsCilindro(ItemFactura itf) {
		return (itf instanceof ItemFacturaPrecioMateriaPrima) && ((ItemFacturaPrecioMateriaPrima)itf).getPrecioMateriaPrima().getMateriaPrima().getTipo() == ETipoMateriaPrima.CILINDRO;
	}

	private DocumentoContableCliente getDocContable() {
		if(getFactura() != null) {
			return getFactura();
		} else {
			return getCorrecionFactura();
		}
	}
	
	private JPanel getPanelTotales() {
		if (panelTotales == null) {
			panelTotales = new JPanel();
			panelTotales.setLayout(new GridLayout(2, 6, 4, 2));

			// LABELS
			panelTotales.add(new JLabel("Subtotal"));
			panelTotales.add(new JLabel("Impuestos"));
			panelTotales.add(new JLabel("Subtotal"));

			JPanel pnlIvaInsc = new JPanel();
			pnlIvaInsc.setLayout(new FlowLayout(FlowLayout.CENTER, 2, 2));
			pnlIvaInsc.add(new JLabel("Iva inscr. (%): "));
			pnlIvaInsc.add(getTxtPorcentajeIVA());
			panelTotales.add(pnlIvaInsc);

			JPanel pnlIvaNoInsc = new JPanel();
			pnlIvaNoInsc.setLayout(new FlowLayout(FlowLayout.CENTER, 2, 2));
			pnlIvaNoInsc.add(new JLabel("Iva No Insc (%): "));
			pnlIvaNoInsc.add(getTxtPorcentajeIVANoInscripto());
			panelTotales.add(pnlIvaNoInsc);

			panelTotales.add(new JLabel("Total ($)"));

			// TXT
			panelTotales.add(getTxtSubTotal());
			panelTotales.add(getTxtImpuestos());
			panelTotales.add(getTxtSubTotalConImpuestos());
			panelTotales.add(getTxtImporteIVAInscripto());
			panelTotales.add(getTxtImporteIVANoInscripto());
			panelTotales.add(getTxtTotal());
		}
		return panelTotales;
	}

	private JPanel getPanelAcciones() {
		JPanel panBotones = new JPanel();
		panBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panBotones.add(getBtnGuardar());
		panBotones.add(getBtnImprimir());
		panBotones.add(getBtnSalir());
		if(!consulta) {
			panBotones.add(new JLabel("Versi�n de lista de precios: "));
			panBotones.add(getLblVersionListaPrecios());
		}
		return panBotones;
	}

	private JLabel getLblVersionListaPrecios() {
		if(lblVersionListaPrecios == null) {
			lblVersionListaPrecios = new JLabel(" --- ");
			lblVersionListaPrecios.setForeground(Color.RED);
			lblVersionListaPrecios.setFont(lblVersionListaPrecios.getFont().deriveFont(Font.BOLD));
			listaDePrecios = GTLBeanFactory.getInstance().getBean2(ListaDePreciosFacadeRemote.class).getListaByIdCliente(getCliente().getId());
			if(listaDePrecios != null && listaDePrecios.getVersiones() != null && !listaDePrecios.getVersiones().isEmpty()) {
				VersionListaDePrecios versionListaDePrecios = listaDePrecios.getVersiones().get(listaDePrecios.getVersiones().size()-1);
				versionListaPreciosActual = versionListaDePrecios;
				getBtnBuscarListaDePrecios().setEnabled(true);
				lblVersionListaPrecios.setText(DateUtil.dateToString(versionListaDePrecios.getInicioValidez(), DateUtil.SHORT_DATE));
				return lblVersionListaPrecios;
			}
			
		}
		return lblVersionListaPrecios;
	}
	private JButton getBtnGuardar() {
		if (btnGuardar == null) {
			btnGuardar = new JButton("Guardar e imprimir");
			btnGuardar.setMnemonic(KeyEvent.VK_G);
			btnGuardar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(!hayErrores()){
						boolean okImprimir = false;
						if (isFactura()) {
							if(validarFecha()){
								if (getFactura().getItems().size() > 0) {
									try {
										okImprimir = guardarFactura();
										if (okImprimir) {
											FWJOptionPane.showInformationMessage(JDialogCargaFactura.this,"La factura se ha guardado con �xito", "Alta de factura");
										} else {
											dispose();
											return;
										}
									}catch (FaltanCargarDibujosException f) {
										return;
									}
								} else {
									FWJOptionPane.showErrorMessage(JDialogCargaFactura.this,"Debe elegir al menos un item para la factura","Error");
								}
							}
						} else {
							if(validarFecha()){
								okImprimir = guardarCorreccion();
								if(okImprimir){
									FWJOptionPane.showInformationMessage(JDialogCargaFactura.this, "La " + getCorrecionFactura().getTipo().getDescripcion() + " se ha guardado con �xito", "Alta de nota de cr�dito y d�bito");
								}else{
									return;
								}
							}
						}
						if(getRemitos()!= null && getCliente().getPosicionIva() == EPosicionIVA.EXPORTACION){
							dispose();
							return;
						}
						if(okImprimir){
							try{
								if(FWJOptionPane.showQuestionMessage(JDialogCargaFactura.this, "Desea imprimir?", "Pregunta")==FWJOptionPane.YES_OPTION){
									imprimir();
								}
							}catch(JRException jre){
								jre.printStackTrace();
								FWJOptionPane.showErrorMessage(JDialogCargaFactura.this, "Se ha producido un error al imprimir.", "Error");
							}
							dispose();
						}
					}else{
						FWJOptionPane.showErrorMessage(JDialogCargaFactura.this, "Hay errores en la tabla de productos. Asegurese de corregirlos y vuelva a intentarlo", "Error");
					}
				}
			});
		}
		return btnGuardar;
	}

	private boolean hayErrores() {
		for(CeldaTablaProductos key : getMapFilaCantidadErrores().keySet()){
			if(getMapFilaCantidadErrores().get(key) != null && getMapFilaCantidadErrores().get(key) > 0){
				return true;
			}
		}
		return false;
	}

	private boolean guardarCorreccion() {
		if (getCorrecionFactura().getMontoTotal() == null) {
			FWJOptionPane.showErrorMessage(this, "Debe especificar el monto", "Error");
			return false;
		}
		if(getCorrecionFactura() instanceof NotaCredito && ((NotaCredito)getCorrecionFactura()).getFacturasRelacionadas().isEmpty()){
			FWJOptionPane.showErrorMessage(this, "Debe elegir las facturas relacionadas", "Error");
			return false;
		}
		if(getCorrecionFactura().getItems().isEmpty()) {
			FWJOptionPane.showErrorMessage(this, "Debe ingresar al menos un �tem.", "Error");
			return false;
		}
		for(ItemFactura i : getCorrecionFactura().getItems()) {
			if(StringUtil.isNullOrEmpty(i.getDescripcion())) {
				FWJOptionPane.showErrorMessage(this, "Debe completar la Descripci�n de todos los �tems.", "Error");
				return false;
			}
		}
		if(getCorrecionFactura() instanceof NotaCredito) {
			if (!GTLGlobalCache.getInstance().getUsuarioSistema().getPerfil().getIsAdmin()) {
				UsuarioSistema usrAdmin = null;
				do {
					JDialogPasswordInput jDialogPasswordInput = new JDialogPasswordInput(JDialogCargaFactura.this, "Autorizar alta nota de credito");
					boolean acepto = jDialogPasswordInput.isAcepto();
					if (!acepto) {
						return false;
					}
					String pass = new String(jDialogPasswordInput.getPassword());
					usrAdmin = GTLBeanFactory.getInstance().getBean2(UsuarioSistemaFacadeRemote.class).esPasswordDeAdministrador(pass);
					if (usrAdmin == null) {
						FWJOptionPane.showErrorMessage(JDialogCargaFactura.this, "La clave ingresada no peternece a un usuario administrador", "Error");
					}
				} while (usrAdmin == null);
			}
		}
		if (getTxtPorcentajeIVA().getText().trim().length() > 0) {
			getCorrecionFactura().setPorcentajeIVAInscripto(new BigDecimal(getTxtPorcentajeIVA().getText().replace(',', '.')));
		}
		getCorrecionFactura().setMontoTotal(new BigDecimal(getCorrecionFactura().getMontoTotal().doubleValue()));
		if(getCorrecionFactura() instanceof NotaDebito) {
			((NotaDebito)getCorrecionFactura()).setMontoFaltantePorPagar(new BigDecimal(getCorrecionFactura().getMontoTotal().doubleValue()));
		}else{
			((NotaCredito)getCorrecionFactura()).setMontoSobrante(new BigDecimal(getCorrecionFactura().getMontoTotal().doubleValue()));
		}
		//Seteo la descripci�n de la CF a partir de la de los items
		List<String> descrItemList = new ArrayList<String>();
		for(ItemFactura i : getCorrecionFactura().getItems()) {
			descrItemList.add(i.getDescripcion());
		}
		getCorrecionFactura().setDescripcion(StringUtil.getCadena(descrItemList, " / "));

		long longFecha = 0;
		if(GenericUtils.esHoy(new java.sql.Date(getPanelFecha().getDate().getTime()))){//hoy
			longFecha = DateUtil.getAhora().getTime();
		}else{
			longFecha=getPanelFecha().getDate().getTime();
		}
		getCorrecionFactura().setFechaEmision(new Timestamp(longFecha));
		getCorrecionFactura().setCliente(getCliente());
		getCorrecionFactura().setTipoFactura(getCliente().getPosicionIva().getTipoFactura());
		String usuario = GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName();
		try {
			setCorrecionFactura(isEdicion()?getCorreccionFacade().editarCorreccion(getCorrecionFactura(), usuario):getCorreccionFacade().guardarCorreccionYGenerarMovimiento(getCorrecionFactura(), usuario));
		} catch (ValidacionException e) {
			FWJOptionPane.showErrorMessage(this, StringW.wordWrap(e.getMensajeError()), "Error");
			return false;
		} catch (ValidacionExceptionSinRollback e) {
			FWJOptionPane.showErrorMessage(this, StringW.wordWrap(e.getMensajeError()), "Error");
			return false;
		}
		
		return true;
	}

	private boolean guardarFactura() {
		if (getTxtPorcentajeIVA().getText().trim().length() > 0) {
			getFactura().setPorcentajeIVAInscripto(getFactura().getPorcentajeIVAInscripto());
		}
		long longFecha = 0;
		if(GenericUtils.esHoy(new java.sql.Date(getPanelFecha().getDate().getTime()))){//hoy
			longFecha = DateUtil.getAhora().getTime();
		}else{
			longFecha=getPanelFecha().getDate().getTime();
		}
		getFactura().setFechaEmision(new Timestamp(longFecha));
		getFactura().setCondicionDeVenta((CondicionDeVenta)getCmbCondicionVenta().getSelectedItem());
		boolean cilindrosValidos = true;
		if (getRemitos() == null || getRemitos().isEmpty()) {
			int contador = 0;
			ItemFacturaPrecioMateriaPrima itemCilindro = null;
			for (ItemFactura itf : getFactura().getItems()) {
				if (itemIsCilindro(itf)) {
					contador += itf.getCantidad().intValue();
					itemCilindro = (ItemFacturaPrecioMateriaPrima)itf;
				}
			}
			if (contador > 0) {
				FWJOptionPane.showWarningMessage(JDialogCargaFactura.this, "Se han detectado " + contador + " cilindro/s. Debe cargar los dibujos.", "Advertencia");
				do {
					JDialogAgregarModificarDibujoEstampado dialog = new JDialogAgregarModificarDibujoEstampado(GuiUtil.getFrameForComponent(JDialogCargaFactura.this), contador, null, getNroDibujoTracker());
					dialog.seleccionDibujoExistente(getFactura().getCliente(), getRemitoEntradaDibujo().getDibujosPersited());
					dialog.setVisible(true);
					if (dialog.isAcepto()) {
						DibujoEstampado de = dialog.getDibujoActual();
						de.setEstado(EEstadoDibujo.EN_STOCK);
						contador -= de.getCantidadColores();
						getNroDibujoTracker().putNro(de.getNroDibujo());
						getRemitoEntradaDibujo().addItem(itemCilindro, de);
					} else {
						if (dialog.isAcepto()) {
							DibujoEstampado de = dialog.getDibujoActual();
							getRemitoEntradaDibujo().addItem(itemCilindro, de);
							contador -= de.getCantidadColores();
						} else {
							cilindrosValidos = false;
						}
					}
				}while(contador > 0 && cilindrosValidos);
				getNroDibujoTracker().clear(); //por las dudas!
			}
		}
		if (!cilindrosValidos) {
			FWJOptionPane.showErrorMessage(this, "Debe cargar todos los cilindros para guardar esta factura.", "Error");
			throw new FaltanCargarDibujosException();
		}

		String usuario = GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName();
		try {
			setFactura(isEdicion()?getFacturaFacade().editarFactura(getFactura(), getRemitoEntradaDibujo() ,usuario):getFacturaFacade().guardarFacturaYGenerarMovimiento(getFactura(), getRemitoEntradaDibujo(), usuario));
		} catch (ValidacionException e) {
			FWJOptionPane.showErrorMessage(this, StringW.wordWrap(e.getMensajeError()), "Error");
			return false;
		} catch (ValidacionExceptionSinRollback e) {
			FWJOptionPane.showErrorMessage(this, StringW.wordWrap(e.getMensajeError()), "Error");
			return false;
		}
		return true;
	}

	private class FaltanCargarDibujosException extends RuntimeException {

		private static final long serialVersionUID = -2248855494604124346L;

	}
	
	private JButton getBtnImprimir() {
		if (btnImprimir == null) {
			btnImprimir = new JButton("Imprimir");
			btnImprimir.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try{
						if(getCliente().getPosicionIva() == EPosicionIVA.EXPORTACION) {
							return;
						}
						imprimir();
					}catch(JRException jre){
						jre.printStackTrace();
						FWJOptionPane.showErrorMessage(JDialogCargaFactura.this, "Se ha producido un error al imprimir.", "Error");
					}
				}
			});
		}
		return btnImprimir;
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

	public List<RemitoSalida> getRemitos() {
		return remitos;
	}

	public void setRemitos(List<RemitoSalida> remitos) {
		this.remitos = remitos;
	}

	public ETipoCorreccionFactura getTipoCorrecion() {
		return tipoCorrecion;
	}

	public void setTipoCorrecion(ETipoCorreccionFactura tipoCorrecion) {
		this.tipoCorrecion = tipoCorrecion;
	}

	private JLabel getLblTipoFactura() {
		if (lblTipoFactura == null) {
			lblTipoFactura = new JLabel();
			lblTipoFactura.setPreferredSize(new Dimension(20, 20));
			lblTipoFactura.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			lblTipoFactura.setHorizontalAlignment(JLabel.CENTER);
			if(getFactura()!=null){
				lblTipoFactura.setText(getFactura().getTipoFactura().getDescripcion());
			}
			lblTipoFactura.setFont(new Font("Tahoma", Font.BOLD, 20));
		}
		return lblTipoFactura;
	}

	private CondicionDeVentaFacadeRemote getCondicionDeVentaFacade() {
		if (condicionDeVentaFacade == null) {
			condicionDeVentaFacade = GTLBeanFactory.getInstance().getBean2(CondicionDeVentaFacadeRemote.class);
		}
		return condicionDeVentaFacade;
	}

	private FacturaFacadeRemote getFacturaFacade() {
		if (facturaFacade == null) {
			facturaFacade = GTLBeanFactory.getInstance().getBean2(FacturaFacadeRemote.class);
		}
		return facturaFacade;
	}

	private DocumentoContableFacadeRemote getDocumentoContableFacade() {
		if (docContableFacade == null) {
			docContableFacade = GTLBeanFactory.getInstance().getBean2(DocumentoContableFacadeRemote.class);
		}
		return docContableFacade;
	}

	private ParametrosGeneralesFacadeRemote getParametrosGeneralesFacade() {
		if (parametrosGeneralesFacade == null) {
			parametrosGeneralesFacade = GTLBeanFactory.getInstance().getBean2(ParametrosGeneralesFacadeRemote.class);
		}
		return parametrosGeneralesFacade;
	}

	public Boolean getLlevaSeguro() {
		return llevaSeguro;
	}

	public void setLlevaSeguro(Boolean llevaSeguro) {
		this.llevaSeguro = llevaSeguro;
	}

	private void updateTotales() {
		double subTotal = getFactura().getMontoSubtotal().doubleValue();
		double total = subTotal;
		double ivaInsc = getFactura().getTotalIVA();
		getTxtImporteIVAInscripto().setText(getDecimalFormat().format(ivaInsc));
		total += ivaInsc;
		getFactura().setMontoFaltantePorPagar(new BigDecimal(total));
		getFactura().setMontoTotal(new BigDecimal(total));
		// getFactura().setMontoImpuestos(montoImpuestos)
		getTxtTotal().setText(getDecimalFormat().format(total));
	}

	private void updateTotalesCorrecion() {
		double subTotal =0;
		if(getCorrecionFactura().getMontoSubtotal()!=null){
			subTotal += getCorrecionFactura().getMontoSubtotal().doubleValue();
		}
		double total = subTotal;
		double ivaInsc = getCorrecionFactura().getTotalIVA();
		getTxtImporteIVAInscripto().setText( (getCorrecionFactura() instanceof NotaCredito && ivaInsc >0?"-":"") +  getDecimalFormat().format(ivaInsc));
		total += ivaInsc;
		getCorrecionFactura().setMontoTotal(new BigDecimal(total));
		getTxtTotal().setText((getCorrecionFactura() instanceof NotaCredito && total > 0?"-":"") + getDecimalFormat().format(total));
	}

	public ParametrosGenerales getParametrosGenerales() {
		return parametrosGenerales;
	}

	public void setParametrosGenerales(ParametrosGenerales parametrosGenerales) {
		this.parametrosGenerales = parametrosGenerales;
	}

	private void salir() {
		if (!isConsulta()) {
			int ret = FWJOptionPane.showQuestionMessage(this, "Va a salir sin guardar, esta seguro?", "Factura");
			if (ret == FWJOptionPane.YES_OPTION) {
				dispose();
			}
		} else {
			dispose();
		}
	}

	public Factura getFactura() {
		return factura;
	}

	private void inicializarFactura(Integer nroFactura) throws ProductoSinPrecioException {
		this.factura = new Factura();
		this.factura.setRemitos(this.getRemitos());
		ETipoFactura tipoFactura = null;
		Cliente cl = getCliente();
		this.factura.setCliente(cl);
		if(cl.getPosicionIva() != null) {
			tipoFactura = cl.getPosicionIva().getTipoFactura();
			this.factura.setTipoFactura(tipoFactura);
		}
		if (tipoFactura == ETipoFactura.A || cl.getPosicionIva() == EPosicionIVA.EXPORTACION_ARG) {
			this.factura.setPorcentajeIVAInscripto(getParametrosGeneralesFacade().getParametrosGenerales().getPorcentajeIVAInscripto());
		}
		if(tipoFactura == null) {
			this.factura.setTipoFactura(ETipoFactura.A);
		}
		this.factura.setNroFactura(nroFactura);
		this.factura.setCondicionDeVenta((CondicionDeVenta) getCmbCondicionVenta().getSelectedItem());
		if(getRemitos()!=null){
			this.llenarItemsFacturaConRemito();
		}
	}

	private DecimalFormat getDecimalFormat() {
		DecimalFormat df = new DecimalFormat("#,###.00");
		df.setMaximumFractionDigits(2);
		df.setGroupingUsed(true);
		return df;
	}

	public Integer getCantTubos() {
		return cantTubos;
	}

	public void setCantTubos(Integer cantTubos) {
		this.cantTubos = cantTubos;
	}

	public void setFactura(Factura factura) {
		this.factura = factura;
	}

	private void actualizarSeguro() {
		if(getFactura() == null) {
			return;
		}
		int rowSeguro = getNumeroFilaSeguro();
		BigDecimal importeSeguro = new BigDecimal(getValorParaSeguro() * getParametrosGenerales().getPorcentajeSeguro().doubleValue() / 100);
		for (ItemFactura itf : getFactura().getItems()) {
			if (itf instanceof ItemFacturaSeguro) {
				itf.setImporte(importeSeguro);
				break;
			}
		}
		getTablaProductos().setValueAt(GenericUtils.getDecimalFormat().format(importeSeguro), rowSeguro, COL_IMPORTE);
		getTablaProductos().repaint();
	}

	private void actualizarItemFactura(double importe, double cantidad, double precioUnitario, ItemFactura itemModificado) {
		for (ItemFactura it : getDocContable().getItems()) {
			if (it.equals(itemModificado)) {
				it.setImporte(new BigDecimal(importe));
				it.setCantidad(new BigDecimal(cantidad));
				it.setPrecioUnitario(new BigDecimal(precioUnitario));
				break;
			}
		}
	}
	
	private void actualizarItemFactura(String descripcion, ItemFactura itemModificado) {
		for (ItemFactura it : getDocContable().getItems()) {
			if (it.equals(itemModificado)) {
				it.setDescripcion(descripcion);
				break;
			}
		}
	}

	public CorreccionFactura getCorrecionFactura() {
		return correcionFactura;
	}

	public void setCorrecionFactura(CorreccionFactura correcionFactura) {
		this.correcionFactura = correcionFactura;
		if(correcionFactura instanceof NotaCredito){
			setTipoCorrecion(ETipoCorreccionFactura.NOTA_CREDITO);
		}else{
			setTipoCorrecion(ETipoCorreccionFactura.NOTA_DEBITO);
		}
	}

	private void imprimir() throws JRException {
		boolean ok = false;
		do {
			String input = JOptionPane.showInputDialog(JDialogCargaFactura.this, "Ingrese la cantidad de copias: ", "Imprimir", JOptionPane.INFORMATION_MESSAGE);
			if(input == null){
				break;
			}
			if (input.trim().length()==0 || !GenericUtils.esNumerico(input)) {
				FWJOptionPane.showErrorMessage(JDialogCargaFactura.this, "Ingreso incorrecto", "error");
			} else if(Integer.valueOf(input) > 3) {
				FWJOptionPane.showErrorMessage(JDialogCargaFactura.this, "Solo se pueden imprimir hasta 3 copias.", "error");
			} else {
				ok = true;
				try{
					ImpresionFacturaHandler ifHandler = new ImpresionFacturaHandler(getFactura()!=null?getFactura():getCorrecionFactura(), input);
					ifHandler.imprimir();
					if (getFactura() != null) {
						setFactura(ifHandler.getFactura());
					}else{
						setCorrecionFactura(ifHandler.getCorreccionFactura());
					}
				}catch(FWException cle){
					BossError.gestionarError(cle);
				} catch (ValidacionException e) {
					FWJOptionPane.showErrorMessage(JDialogCargaFactura.this, StringW.wordWrap(e.getMensajeError()), "Error");
				}catch(IOException ioe) {
					ioe.printStackTrace();
					FWJOptionPane.showErrorMessage(JDialogCargaFactura.this, "Se ha producido un error al generar el c�digo de barras.", "Error");
				}
			}
		} while (!ok);
	}


	public boolean isConsulta() {
		return consulta;
	}

	public void setConsulta(boolean consulta) {
		this.consulta = consulta;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public CorreccionFacadeRemote getCorreccionFacade() {
		if(correccionFacade == null){
			correccionFacade = GTLBeanFactory.getInstance().getBean2(CorreccionFacadeRemote.class);
		}
		return correccionFacade;
	}
	
	public Map<CeldaTablaProductos, Integer> getMapFilaCantidadErrores() {
		return mapFilaCantidadErrores;
	}
	
	public void setMapFilaCantidadErrores(Map<CeldaTablaProductos, Integer> mapFilaCantidadErrores) {
		this.mapFilaCantidadErrores = mapFilaCantidadErrores;
	}
	
	private class CeldaTablaProductos{
		private final Integer fila;
		private final Integer columna;
		
		public CeldaTablaProductos(Integer fila, Integer columna) {
			this.fila = fila;
			this.columna = columna;
		}
		
		@Override
		public String toString(){
			return "[fila: " + fila + " Columna: " + columna + "]";
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((columna == null) ? 0 : columna.hashCode());
			result = prime * result + ((fila == null) ? 0 : fila.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			CeldaTablaProductos other = (CeldaTablaProductos) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (columna == null) {
				if (other.columna != null)
					return false;
			} else if (!columna.equals(other.columna))
				return false;
			if (fila == null) {
				if (other.fila != null)
					return false;
			} else if (!fila.equals(other.fila))
				return false;
			return true;
		}

		private JDialogCargaFactura getOuterType() {
			return JDialogCargaFactura.this;
		}
	}
	
	private PanelDatePicker getPanelFecha() {
		if(panelFecha == null){
			panelFecha = new PanelDatePicker(){

				private static final long serialVersionUID = -3469206449521152132L;
				
				@Override
				public void accionBotonCalendarioAdicional() {
					validarFecha();
				}
			};
			panelFecha.setSelectedDate(DateUtil.getHoy());
		}
		return panelFecha;
	}

	private boolean validarFecha() {
		Integer nroFactura = getFactura()!=null?getFactura().getNroFactura():getCorrecionFactura().getNroFactura();
		ETipoFactura eTipoFactura = getFactura()!=null?getFactura().getTipoFactura():getCorrecionFactura().getTipoFactura();
		ETipoDocumento tipoDocumento = getFactura()!=null?getFactura().getTipoDocumento():getCorrecionFactura().getTipoDocumento();
		List<Timestamp> facturaAnteriorYPosterior = getFacturaFacade().getFechasFacturasAnteriorYPosterior(nroFactura,eTipoFactura, tipoDocumento);
		if (facturaAnteriorYPosterior != null && facturaAnteriorYPosterior.size() > 0) {
			if (facturaAnteriorYPosterior.size() == 2) { // hay anterior y posterior, la fecha debe estar entre ambas o si son iguales, debe ser la misma
				Timestamp f1 = facturaAnteriorYPosterior.get(0);
				Timestamp f2 = facturaAnteriorYPosterior.get(1);
				Date fechaIngresada = getPanelFecha().getDate();
				if (f1.equals(f2)) {
					if (fechaIngresada.equals(DateUtil.redondearFecha(f1))) {
						return true;
					}
					getPanelFecha().setSelectedDate(f1);
					FWJOptionPane.showInformationMessage(this, StringW.wordWrap("Atenci�n, esto es una correcci�n. Se guardar� el documento.\n\nLa fecha debe ser: " + DateUtil.dateToString(f1, DateUtil.SHORT_DATE)), "Correcci�n");
					return true;
				} else {
					if (!fechaIngresada.before(DateUtil.redondearFecha(f1)) && !fechaIngresada.after(DateUtil.redondearFecha(f2))) {
						return true;
					} else {
						FWJOptionPane.showErrorMessage(this, "La fecha ingresada debe estar entre " + DateUtil.dateToString(f1, DateUtil.SHORT_DATE) + " y "
								+ DateUtil.dateToString(f2, DateUtil.SHORT_DATE), "Error");
						return false;
					}
				}
			} else {// por descarte el size es 1
				if (!getPanelFecha().getDate().before(DateUtil.redondearFecha(facturaAnteriorYPosterior.get(0)))) {
					return true;
				}
				FWJOptionPane.showErrorMessage(this, "La fecha debe ser posterior a " + DateUtil.dateToString(facturaAnteriorYPosterior.get(0)), "Error");
				return false;
			}
		}
		return true;
	}
	
	private PrecioMateriaPrimaFacadeRemote getPrecioMateriaPrimaFacade(){
		if(precioMatariaPrimaFacade == null){
			precioMatariaPrimaFacade = GTLBeanFactory.getInstance().getBean2(PrecioMateriaPrimaFacadeRemote.class);
		}
		return precioMatariaPrimaFacade;
	}

	private RemitoEntradaFacadeRemote getRemitoEntradaFacade() {
		if(remitoEntradaFacade == null){
			remitoEntradaFacade = GTLBeanFactory.getInstance().getBean2(RemitoEntradaFacadeRemote.class);
		}
		return remitoEntradaFacade;
	}

	private RemitoEntradaDibujoFacadeRemote getRemitoEntradaDibujoFacade() {
		if(remitoEntradaDibujoFacade == null){
			remitoEntradaDibujoFacade = GTLBeanFactory.getInstance().getBean2(RemitoEntradaDibujoFacadeRemote.class);
		}
		return remitoEntradaDibujoFacade;
	}

	public String getStrFacturasRelacionadas() {
		return strFacturasRelacionadas;
	}

	public void setStrFacturasRelacionadas(String strFacturasRelacionadas) {
		this.strFacturasRelacionadas = strFacturasRelacionadas;
	}

	public boolean isEdicion() {
		return edicion;
	}

	public void setEdicion(boolean edicion) {
		this.edicion = edicion;
	}
	
	public FWJTextField getTxtNrosGenericos() {
		if(txtNrosGenericos == null){
			txtNrosGenericos = new FWJTextField();
			txtNrosGenericos.setEditable(false);
		}
		return txtNrosGenericos;
	}

	private static class ProductoSinPrecioException extends Exception {

		private static final long serialVersionUID = 1L;

	}

	private NroDibujoEstampadoTracker getNroDibujoTracker() {
		if(nroDibujoTracker == null) {
			nroDibujoTracker = new NroDibujoEstampadoTracker();
		}
		return nroDibujoTracker;
	}

	private RemitoEntradaDibujo getRemitoEntradaDibujo() {
		if(reDibujo == null) {
			if(getFactura().getId() != null) {
				reDibujo = getRemitoEntradaDibujoFacade().getByFCRelacionada(getFactura());
			}
			if(reDibujo == null) {
				reDibujo = new RemitoEntradaDibujo();
				reDibujo.setCliente(cliente);
				reDibujo.setFactura(getFactura());
			}
		}
		return reDibujo;
	}

}