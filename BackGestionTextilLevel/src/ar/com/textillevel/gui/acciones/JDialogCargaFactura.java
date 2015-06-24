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
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
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
import main.acciones.facturacion.OperacionSobreRemitoSalidaHandler;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

import org.apache.taglibs.string.util.StringW;

import ar.clarin.fwjava.boss.BossError;
import ar.clarin.fwjava.boss.BossEstilos;
import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.CLJTable;
import ar.clarin.fwjava.componentes.CLJTextField;
import ar.clarin.fwjava.componentes.VerticalFlowLayout;
import ar.clarin.fwjava.componentes.error.CLException;
import ar.clarin.fwjava.componentes.error.validaciones.ValidacionException;
import ar.clarin.fwjava.componentes.error.validaciones.ValidacionExceptionSinRollback;
import ar.clarin.fwjava.util.DateUtil;
import ar.clarin.fwjava.util.GuiUtil;
import ar.clarin.fwjava.util.NumUtil;
import ar.clarin.fwjava.util.StringUtil;
import ar.com.textillevel.entidades.config.ParametrosGenerales;
import ar.com.textillevel.entidades.documentos.factura.CondicionDeVenta;
import ar.com.textillevel.entidades.documentos.factura.CorreccionFactura;
import ar.com.textillevel.entidades.documentos.factura.Factura;
import ar.com.textillevel.entidades.documentos.factura.NotaCredito;
import ar.com.textillevel.entidades.documentos.factura.NotaDebito;
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
import ar.com.textillevel.entidades.documentos.remito.PiezaRemito;
import ar.com.textillevel.entidades.documentos.remito.RemitoSalida;
import ar.com.textillevel.entidades.enums.EEstadoFactura;
import ar.com.textillevel.entidades.enums.EEstadoImpresionDocumento;
import ar.com.textillevel.entidades.enums.EPosicionIVA;
import ar.com.textillevel.entidades.enums.ETipoCorreccionFactura;
import ar.com.textillevel.entidades.enums.ETipoFactura;
import ar.com.textillevel.entidades.enums.EUnidad;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.to.ClienteTO;
import ar.com.textillevel.entidades.to.FacturaTO;
import ar.com.textillevel.entidades.to.ItemFacturaTO;
import ar.com.textillevel.entidades.to.facturab.ClienteBTO;
import ar.com.textillevel.entidades.to.facturab.FacturaBTO;
import ar.com.textillevel.entidades.to.facturab.ItemFacturaBTO;
import ar.com.textillevel.entidades.ventas.articulos.Articulo;
import ar.com.textillevel.entidades.ventas.materiaprima.PrecioMateriaPrima;
import ar.com.textillevel.entidades.ventas.productos.Producto;
import ar.com.textillevel.facade.api.remote.CondicionDeVentaFacadeRemote;
import ar.com.textillevel.facade.api.remote.CorreccionFacadeRemote;
import ar.com.textillevel.facade.api.remote.DocumentoContableFacadeRemote;
import ar.com.textillevel.facade.api.remote.FacturaFacadeRemote;
import ar.com.textillevel.facade.api.remote.ParametrosGeneralesFacadeRemote;
import ar.com.textillevel.facade.api.remote.PrecioMateriaPrimaFacadeRemote;
import ar.com.textillevel.facade.api.remote.RemitoEntradaFacadeRemote;
import ar.com.textillevel.facade.api.remote.RemitoSalidaFacadeRemote;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.JasperHelper;
import ar.com.textillevel.gui.util.controles.LinkableLabel;
import ar.com.textillevel.gui.util.controles.PanelDatePicker;
import ar.com.textillevel.modulos.odt.entidades.PiezaODT;
import ar.com.textillevel.util.GTLBeanFactory;
import ar.com.textillevel.util.GestorDeFacturas;

@SuppressWarnings("unchecked")
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

	private CLJTable tablaProductos;

	private CLJTextField txtSubTotal;
	private CLJTextField txtImpuestos;
	private CLJTextField txtSubTotalConImpuestos;
	private CLJTextField txtPorcentajeIVA;
	private CLJTextField txtPorcentajeIVANoInscripto;
	private CLJTextField txtImporteIVAInscripto;
	private CLJTextField txtImporteIVANoInscripto;
	private CLJTextField txtTotal;

	private CLJTextField txtRazonSocial;
	private CLJTextField txtDireccion;
	private CLJTextField txtLocalidad;
	private CLJTextField txtCondicionIVA;
	private CLJTextField txtCuit;
	private JComboBox cmbCondicionVenta;
	private CLJTextField txtNrosGenericos;

	private PanelDatePicker panelFecha;
	private CLJTextField txtNroFactura;
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
			CLJOptionPane.showErrorMessage(this, "Faltan configurar los parametros generales", "Error");
			dispose();
		}
		setCliente(correccion.getCliente());
		setParametrosGenerales(parametrosGenerales2);
		setCorrecionFactura(correccion);
		construct();
		getPanelBotonesFactura().setVisible(false);
		llenarDatosCliente(correccion.getCliente());
		if(correccion instanceof NotaCredito){
			getTxtNrosGenericos().setText(getNrosFacturasRelacionadas( ((NotaCredito) correccion).getFacturasRelacionadas()));
		}
//		getCmbCondicionVenta().setSelectedItem(factura.getCondicionDeVenta());
		getCmbCondicionVenta().setEnabled(!consulta);
		getPanelTotales().setVisible(true);
		// getLblTipoFactura().setVisible(false);
		
		if(getCorrecionFactura().getAnulada()==true){
			setConsulta(consulta);
			GenericUtils.ponerFondoAnulada(getTablaProductos().getScrollPane());
			getPanelBotonesFactura().setVisible(false);
			getBtnGuardar().setVisible(false);
			getBtnImprimir().setVisible(false);
			getPanelFecha().setSelectedDate(correccion.getFechaEmision());
			getPanelFecha().setEnabled(false);
			getCmbCondicionVenta().setEnabled(false);
			getTxtPorcentajeIVA().setText("");
		}else{
			BigDecimal montoSubtotal = getCorrecionFactura().getMontoSubtotal();
			if(montoSubtotal!=null){
				montoSubtotal = montoSubtotal.multiply(new BigDecimal(getCorrecionFactura() instanceof NotaCredito?-1:1));
				getTxtSubTotal().setText(getDecimalFormat().format(montoSubtotal));
			}
			updateTotalesCorrecion();
			
			getTablaProductos().addRow(getFilaCorreccion(correccion,false));
			// GuiUtil.setEstadoPanel(getPanelAcciones(), false);
			if(consulta){
				GuiUtil.setEstadoPanel(getPanelBotonesFactura(), false);
			}
			GuiUtil.setEstadoPanel(getPanelTablaProductos(), !consulta);
			getBtnGuardar().setVisible(!consulta);
			//getBtnImprimir().setVisible(false);
			setConsulta(consulta);
			getLblTipoFactura().setVisible(false);
			setTitle("Consultar " + getTipoCorrecion().getDescripcion());
			//getTxtFechaHoy().setText(DateUtil.dateToString(correccion.getFechaEmision(), DateUtil.SHORT_DATE));
			getPanelFecha().setSelectedDate(correccion.getFechaEmision());
			getPanelFecha().setEnabled(!consulta);
		}
		
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
			CLJOptionPane.showErrorMessage(this, "Faltan configurar los parametros generales", "Error");
			dispose();
		}
		setParametrosGenerales(parametrosGenerales2);
		construct();
		getTxtNroFactura().setText(
				StringUtil.fillLeftWithZeros(String.valueOf(getParametrosGenerales().getNroSucursal()), 4) + "-"
						+ StringUtil.fillLeftWithZeros(String.valueOf(getCorrecionFactura().getNroFactura()), 8));
		llenarDatosCliente(getCliente());
		// getTxtNroRemito().setText(String.valueOf(factura.getRemito().getNroRemito()));
		// getCmbCondicionVenta().setSelectedItem(factura.getCondicionDeVenta());
		getCmbCondicionVenta().setEnabled(false);
		getPanelTotales().setVisible(true);
		getPanelBotonesFactura().setVisible(false);
		getTablaProductos().addRow(getFilaCorreccion(getCorrecionFactura(),true));
		getLblTipoFactura().setVisible(false);
		setTitle("Agregar " + getTipoCorrecion().getDescripcion());
		getBtnImprimir().setVisible(false);
		setEdicion(false);
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
			CLJOptionPane.showErrorMessage(this, "Faltan configurar los parametros generales", "Error");
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
			getTxtPorcentajeIVA().setText("");
			//getLblConsultaRemito().setVisible(false);
		}else{
			llenarDatosCliente(getCliente());
			if(getCliente().getPosicionIva() == EPosicionIVA.EXPORTACION){
				getBtnImprimir().setVisible(false);
			}
			if(factura.getRemitos()!=null){
				//getPnlNroRemito().setText(String.valueOf(factura.getRemito().getNroRemito()));
			}
			if (factura.getMontoImpuestos() != null) {
				getTxtImpuestos().setText(String.valueOf(factura.getMontoImpuestos().doubleValue()));
			}
			getTxtImpuestos().setEditable(false);
			llenarTablaProductos();
			getCmbCondicionVenta().setSelectedItem(factura.getCondicionDeVenta());
			getCmbCondicionVenta().setEnabled(!consulta);
			// GuiUtil.setEstadoPanel(getPanelAcciones(), false);
			if(consulta){
				GuiUtil.setEstadoPanel(getPanelBotonesFactura(), false);
			}
			GuiUtil.setEstadoPanel(getPanelTablaProductos(), !consulta);
			calcularSubTotal();
			setConsulta(consulta);
			getBtnGuardar().setVisible(!consulta);
			//getBtnImprimir().setVisible(false);
			//setCliente(factura.getRemito().getCliente());
			setTitle("Consultar factura");
			//getTxtFechaHoy().setText(DateUtil.dateToString(factura.getFechaEmision(), DateUtil.SHORT_DATE));
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
			CLJOptionPane.showErrorMessage(this, "Faltan configurar los parametros generales", "Error");
			dispose();
		}
		setParametrosGenerales(parametrosGenerales2);
		inicializarFactura(nroFactura);
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
			CLJOptionPane.showErrorMessage(this, "Faltan configurar los parametros generales", "Error");
			dispose();
		}
		setParametrosGenerales(parametrosGenerales2);
		setCliente(cliente);
		inicializarFactura(nroFactura);
		getFactura().setItems(new ArrayList<ItemFactura>());
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
			if (CLJOptionPane.showQuestionMessage(this, "Desea agregar seguro?", "Alta de factura") == CLJOptionPane.YES_OPTION) {
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
			getCorrecionFactura().setNroFactura(getDocumentoContableFacade().getProximoNroDocumentoContable(getCliente().getPosicionIva()));
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

	private void llenarItemsFacturaConRemito() {
		//Armo un map con los totales de metros por producto en base a las piezas de los remitos
		Map<Producto, BigDecimal> mapTotalPorProducto = new HashMap<Producto, BigDecimal>();

		//Armo un map con los totales de metros por tela (
		Map<Articulo, BigDecimal> mapTotalPorTelaCruda = new HashMap<Articulo, BigDecimal>();
		Map<PrecioMateriaPrima, BigDecimal> mapTotalStockInicial = new HashMap<PrecioMateriaPrima, BigDecimal>();
		List<RemitoSalida> remitos = this.getRemitos();
		for(RemitoSalida r : remitos){
			for(PiezaRemito pr : r.getPiezas()) {
				if(pr.getPmpDescuentoStock() == null) { //Son piezas normales, es decir, distintas a la de stock inicial
					Producto producto = getProducto(pr);
					if(producto == null) {//Es una pieza de tela cruda (i.e. no tiene ODT/Producto)
						Articulo articulo = getRemitoEntradaFacade().getArticuloByPiezaSalidaCruda(pr.getId());
						if(articulo == null) {
							throw new IllegalArgumentException("No existe artículo relacionado para la pieza de salida con id " + pr.getId() + " y metros "  + pr.getMetros());
						}
						incrementarCantEnMap(mapTotalPorTelaCruda, articulo, pr.getMetros());
					} else {
						incrementarCantEnMap(mapTotalPorProducto, producto, getTotalMetros(pr.getMetros(), producto));
					}
				} else {//Son piezas de stock inicial
					Producto p = getProducto(pr);
					if(p == null) { //Es una pieza de stock inicial pero sin ODT
						incrementarCantEnMap(mapTotalStockInicial, pr.getPmpDescuentoStock(), pr.getMetros());
					} else { //Tiene ODT
						incrementarCantEnMap(mapTotalPorProducto, p, getTotalMetros(pr.getMetros(), p));
					}
				}
			}
		}
		//Por cada par <producto, total> del map genero un item factura producto y lo pongo en la tabla
		getFactura().setItems(new ArrayList<ItemFactura>());
		for (Producto p : mapTotalPorProducto.keySet()) {
			ItemFacturaProducto itp = new ItemFacturaProducto();
			BigDecimal totalByProducto = mapTotalPorProducto.get(p);
			itp.setCantidad(totalByProducto);
			itp.setImporte(totalByProducto.multiply(getPrecio(p)));
			itp.setDescripcion(p.getDescripcion());
			itp.setProducto(p);
			itp.setUnidad(p.getTipo().getUnidad());
			itp.setPrecioUnitario(getPrecio(p));
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
			iftc.setDescripcion(""); //los items crudos se sugieren sin descripción
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
	
	private BigDecimal getTotalMetros(BigDecimal metros, Producto producto) {
		if (producto.getTipo().getUnidad() == EUnidad.KILOS) {
			if(producto.getArticulo().getGramaje() == null) {
				throw new IllegalArgumentException("Falta definir el gramaje para la tela " + producto.getArticulo() + ".");
			}
			return metros.multiply(producto.getArticulo().getGramaje());
		} else  {
			return metros;
		}
	}

	private Producto getProducto(PiezaRemito pr) {
		if(pr.getPiezasPadreODT().isEmpty()) {
			return null;
		}
		PiezaODT piezaODT = pr.getPiezasPadreODT().get(0);
		return piezaODT.getOdt().getProducto();
	}

	private void calcularSubTotal() {
		double suma = 0;
		for (ItemFactura it : getFactura().getItems()) {
			suma += it.getImporte().doubleValue();
		}
		getFactura().setMontoSubtotal(new BigDecimal(String.valueOf(suma)));
		getTxtSubTotal().setText(getDecimalFormat().format(suma));
		// double impuestos = (getTxtImpuestos().getText().length() > 0 &&
		// GenericUtils.esNumerico(getTxtImpuestos().getText())) ?
		// Double.valueOf(getTxtImpuestos().getText()) : 0;
		// getTxtSubTotalConImpuestos().setText(String.valueOf(suma +
		// impuestos));
		updateTotales();
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
		for (ItemFactura ifactura : getFactura().getItems()) {
			if (ifactura instanceof ItemFacturaProducto) {
				getTablaProductos().addRow(getFilaProducto((ItemFacturaProducto) ifactura));
				continue;
			}
			if (ifactura instanceof ItemFacturaSeguro) {
				double valorAntesSeguro = getValorParaSeguro();
				ItemFacturaSeguro itemSeguro = (ItemFacturaSeguro) ifactura;
//				if(itemSeguro.getImporte() == null) {
				itemSeguro.setImporte(new BigDecimal(valorAntesSeguro * getParametrosGenerales().getPorcentajeSeguro().doubleValue() / 100));
//				}
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
		}
		bloquearFilasSeguroYPercepcion();
		calcularSubTotal();
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
	
	private Object[] getFilaCorreccion(CorreccionFactura cf, boolean isAgregar) {
		Object[] fila = new Object[CANT_COLS_TBL_FACTURA];
		fila[COL_CANTIDAD] = 1;
		if(!isAgregar){
			fila[COL_DESCRIPCION] = cf.getDescripcion();
		}else{
			fila[COL_DESCRIPCION] = "";
			/*CeldaTablaProductos celda = new CeldaTablaProductos(0, COL_DESCRIPCION);
			getMapFilaCantidadErrores().put(celda, 1);*/
			getTablaProductos().setBackgroundCell(0, COL_DESCRIPCION, Color.YELLOW);
		}
		fila[COL_CANTIDAD] = new BigDecimal(1);
		BigDecimal montoSubtotal = cf.getMontoSubtotal();
		if(montoSubtotal!=null){
			montoSubtotal = montoSubtotal.multiply(new BigDecimal(getCorrecionFactura() instanceof NotaCredito?-1:1));
			fila[COL_PRECIO_UNITARIO] = montoSubtotal;// getDecimalFormat().format(montoSubtotal);
			fila[COL_IMPORTE] = montoSubtotal != null ? getDecimalFormat().format(montoSubtotal.doubleValue()) : null;
		} else {
			BigDecimal monto = cf.getMontoTotal();
			if (monto!=null){
				monto = monto.multiply(new BigDecimal(getCorrecionFactura() instanceof NotaCredito?-1:1));
				fila[COL_PRECIO_UNITARIO] = monto; //getDecimalFormat().format(monto);
				fila[COL_IMPORTE] = getDecimalFormat().format(monto);
			}
		}
		fila[COL_OBJ_FACTURA] = cf;
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
		if(itf.getProducto().getArticulo()!=null){
			fila[COL_ARTICULO] = itf.getProducto().getArticulo().getNombre();
		}else{
			fila[COL_ARTICULO] = " - ";
		}
		fila[COL_CANTIDAD] =itf.getCantidad();// getDecimalFormat().format(itf.getCantidad());
		fila[COL_DESCRIPCION] = itf.getDescripcion();
		fila[COL_UNIDAD] = itf.getUnidad().getDescripcion();
		fila[COL_PRECIO_UNITARIO] = itf.getPrecioUnitario();// getDecimalFormat().format(itf.getPrecioUnitario());
		fila[COL_IMPORTE] = getDecimalFormat().format(itf.getImporte().doubleValue());
		fila[COL_OBJ_FACTURA] = itf;
		return fila;
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

	private BigDecimal getPrecio(Producto p) {
		return GestorDeFacturas.getInstance().getPrecio(p, getCliente().getId());
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
		panel.add(new JLabel("Nº: "), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
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

	private CLJTable getTablaProductos() {
		if (tablaProductos == null) {
			tablaProductos = new CLJTable(0, CANT_COLS_TBL_FACTURA) {

				private static final long serialVersionUID = -7960992084800303941L;

				@Override
				public void newRowSelected(int newRow, int oldRow) {
					getBtnQuitarProducto().setEnabled(true);
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
								if (getCorrecionFactura() == null) {
									ItemFactura itemModificado = (ItemFactura) getValueAt(row, COL_OBJ_FACTURA);
									actualizarItemFactura(pu * cantidad,cantidad,pu, itemModificado);
									actualizarSeguro();
									calcularSubTotal();
								} else {
									if(getCorrecionFactura()!=null && getCorrecionFactura() instanceof NotaCredito){
										pu = pu *-1;
									}
									BigDecimal montoSubtotal = new BigDecimal(pu * cantidad);
									getCorrecionFactura().setMontoSubtotal(montoSubtotal);
									getTxtSubTotal().setText(String.valueOf(montoSubtotal.doubleValue()));
									updateTotalesCorrecion();
								}
//								if(getCorrecionFactura()!=null && getCorrecionFactura() instanceof NotaCredito){
//									setValueAt(getDecimalFormat().format(new BigDecimal(pu)), row, COL_PRECIO_UNITARIO);
//								}
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
									CLJOptionPane.showErrorMessage(JDialogCargaFactura.this, "Se debe especificar la descripción", "Error");
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
							} else if (getValueAt(row, COL_OBJ_FACTURA) instanceof CorreccionFactura) {
								String descripcion = (String) getValueAt(row, COL_DESCRIPCION);
								if (descripcion.trim().length() == 0) {
									CLJOptionPane.showErrorMessage(JDialogCargaFactura.this, "Se debe especificar la descripción", "Error");
									return;
								}
								/*CeldaTablaProductos celda = new CeldaTablaProductos(0, COL_DESCRIPCION);
								Integer cantErrorFila = getMapFilaCantidadErrores().get(celda);
								if(cantErrorFila != null && cantErrorFila > 0){
									cantErrorFila--;
									getMapFilaCantidadErrores().put(celda, cantErrorFila);
								}*/
								setBackgroundCell(0, COL_DESCRIPCION, Color.WHITE);
								getCorrecionFactura().setDescripcion(descripcion);
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
									newValue = new BigDecimal(Double.valueOf((String)valueAt));
								}else{
									newValue = (BigDecimal)valueAt;
								}
								double pu = newValue.doubleValue();
								if (getCorrecionFactura() == null){
									ItemFactura itemModificado = (ItemFactura) getValueAt(row, COL_OBJ_FACTURA);
									if(itemModificado instanceof ItemFacturaPrecioMateriaPrima){
										PrecioMateriaPrima pm = ((ItemFacturaPrecioMateriaPrima)itemModificado).getPrecioMateriaPrima();
										BigDecimal cant = new BigDecimal(valor);
										BigDecimal stockGuardado = getPrecioMateriaPrimaFacade().getStockByPrecioMateriaPrima(pm);
										if(cant.compareTo(stockGuardado)==1){
											CLJOptionPane.showErrorMessage(JDialogCargaFactura.this, "El stock debe ser menor o igual a " + stockGuardado.doubleValue(), "Error");
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
						System.out.println("error de formateo de numerooooooooooooooooo");
					}
				}
			};
			tablaProductos.setReorderingAllowed(false);
			tablaProductos.setStringColumn(COL_ARTICULO, "Articulo", 230, 130, true);
			tablaProductos.setFloatColumn(COL_CANTIDAD, "Cantidad",0f,9999999999999f, 50,  getCorrecionFactura()!=null);
			tablaProductos.setStringColumn(COL_UNIDAD, "Unidad", 150, 50, true);
			tablaProductos.setStringColumn(COL_DESCRIPCION, "Descripcion", 150, 280, false);
			tablaProductos.setFloatColumn(COL_PRECIO_UNITARIO, "Precio Unitario",0f,9999999999999f, 100, false);
			tablaProductos.setStringColumn(COL_IMPORTE, "Importe", 150, 100, true);
			tablaProductos.setStringColumn(COL_OBJ_FACTURA, "", 0, 0, true);
		}
		return tablaProductos;
	}

	private CLJTextField getTxtSubTotal() {
		if (txtSubTotal == null) {
			txtSubTotal = new CLJTextField();
			txtSubTotal.setEditable(false);
		}
		return txtSubTotal;
	}

	private CLJTextField getTxtImpuestos() {
		if (txtImpuestos == null) {
			txtImpuestos = new CLJTextField();
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

	private CLJTextField getTxtSubTotalConImpuestos() {
		if (txtSubTotalConImpuestos == null) {
			txtSubTotalConImpuestos = new CLJTextField();
			txtSubTotalConImpuestos.setEditable(false);
		}
		return txtSubTotalConImpuestos;
	}

	private CLJTextField getTxtPorcentajeIVA() {
		if (txtPorcentajeIVA == null) {
			txtPorcentajeIVA = new CLJTextField();
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

	private CLJTextField getTxtImporteIVAInscripto() {
		if (txtImporteIVAInscripto == null) {
			txtImporteIVAInscripto = new CLJTextField();
			txtImporteIVAInscripto.setEditable(false);
		}
		return txtImporteIVAInscripto;
	}

	private CLJTextField getTxtImporteIVANoInscripto() {
		if (txtImporteIVANoInscripto == null) {
			txtImporteIVANoInscripto = new CLJTextField();
			txtImporteIVANoInscripto.setEditable(false);
		}
		return txtImporteIVANoInscripto;
	}

	private CLJTextField getTxtPorcentajeIVANoInscripto() {
		if (txtPorcentajeIVANoInscripto == null) {
			txtPorcentajeIVANoInscripto = new CLJTextField();
			txtPorcentajeIVANoInscripto.setPreferredSize(new Dimension(30, 20));
			// txtPorcentajeIVANoInscripto.setText(String.valueOf(getParametrosGeneralesFacade().getParametrosGenerales().getPorcentajeIVANoInscripto().intValue()));
			txtPorcentajeIVANoInscripto.setEditable(false);
		}
		return txtPorcentajeIVANoInscripto;
	}

	private CLJTextField getTxtTotal() {
		if (txtTotal == null) {
			txtTotal = new CLJTextField();
			txtTotal.setEditable(false);
		}
		return txtTotal;
	}

	private CLJTextField getTxtRazonSocial() {
		if (txtRazonSocial == null) {
			txtRazonSocial = new CLJTextField();
			txtRazonSocial.setEditable(false);
		}
		return txtRazonSocial;
	}

	private CLJTextField getTxtDireccion() {
		if (txtDireccion == null) {
			txtDireccion = new CLJTextField();
			txtDireccion.setPreferredSize(new Dimension(200, 20));
			txtDireccion.setEditable(false);
		}
		return txtDireccion;
	}

	private CLJTextField getTxtLocalidad() {
		if (txtLocalidad == null) {
			txtLocalidad = new CLJTextField();
			txtLocalidad.setPreferredSize(new Dimension(200, 20));
			txtLocalidad.setEditable(false);
		}
		return txtLocalidad;
	}

	private CLJTextField gettxtCondicionIVA() {
		if (txtCondicionIVA == null) {
			txtCondicionIVA = new CLJTextField();
			txtCondicionIVA.setPreferredSize(new Dimension(150, 20));
			txtCondicionIVA.setEditable(false);
		}
		return txtCondicionIVA;
	}

	private CLJTextField getTxtCuit() {
		if (txtCuit == null) {
			txtCuit = new CLJTextField();
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
		private RemitoLinkeableLabel[] remitoLinkeableLabelList;
		private static final int CANT_MAX_REMITOS_SELECTED_HARDCODE = 3;
		
		private PanelRemitos(List<RemitoSalida> remitos){
			this.remitos = remitos;
			if(remitos!=null){
				this.remitoLinkeableLabelList = new RemitoLinkeableLabel[CANT_MAX_REMITOS_SELECTED_HARDCODE];
				for(int i = 0; i < CANT_MAX_REMITOS_SELECTED_HARDCODE; i++) {
					LinkableLabel lblConsultaRemito = new RemitoLinkeableLabel();
					remitoLinkeableLabelList[i] = (RemitoLinkeableLabel)lblConsultaRemito;
				}
				JPanel pnlRemitos = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
				for (int i = 0; i < Math.min(getRemitos().size(), CANT_MAX_REMITOS_SELECTED_HARDCODE); i++) {
					RemitoLinkeableLabel remitoLinkeableLabel = remitoLinkeableLabelList[i];
					remitoLinkeableLabel.setRemito(getRemitos().get(i));
					remitoLinkeableLabel.setVisible(true);
					pnlRemitos.add(remitoLinkeableLabel);
				}
				add(pnlRemitos,BorderLayout.CENTER);
				setBorder(BorderFactory.createLineBorder(Color.RED.darker()));
			}
		}
		
		public String getNrosRemitos(){
			if(remitos!=null){
				List<String> lista = new ArrayList<String>();
				for(RemitoSalida r : remitos){
					lista.add(String.valueOf(r.getNroRemito()));
				}
				return StringUtil.getCadena(lista, " / ");
			}
			return "";
		}
		
		public List<RemitoSalida> getRemitos() {
			return remitos;
		}
	}
	
	private CLJTextField getTxtNroFactura() {
		if (txtNroFactura == null) {
			txtNroFactura = new CLJTextField();
			txtNroFactura.setPreferredSize(new Dimension(150, 20));
			String nro = StringUtil.fillLeftWithZeros(String.valueOf(getParametrosGenerales().getNroSucursal()), 4) + "-";
			if (getFactura() != null) {
				nro += StringUtil.fillLeftWithZeros(String.valueOf(getFactura().getNroFactura()), 8);
			} else {
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
			if (getCorrecionFactura() != null) {
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
				label = new JLabel("Remito Nº: ");
			}else if((getCorrecionFactura()!= null && getTipoCorrecion() == ETipoCorreccionFactura.NOTA_DEBITO) || (getFactura()!=null && getFactura().getRemitos()==null)){
				label = new JLabel("");
			}else if(getCorrecionFactura()!= null && getTipoCorrecion() == ETipoCorreccionFactura.NOTA_CREDITO){
				label = getLblElegirFactura();
			}
			panelDatosFactura.add(label, GenericUtils.createGridBagConstraints(2, 1, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			if ((getCorrecionFactura() != null && getTipoCorrecion() == ETipoCorreccionFactura.NOTA_DEBITO) || (getFactura()!=null && getFactura().getRemitos()!=null) ) {
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
		sb.append(StringUtil.fillLeftWithZeros(String.valueOf(getParametrosGenerales().getNroSucursal()), 4) + "-");
		boolean primera = true;
		for(Factura f : facturas){
			if(primera){
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
			panelDatosCliente.add(new JLabel("Señor/es: "), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
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
		return panel;
	}

	private Integer getCantidadPiezasRemito(){
		Integer suma = 0;
		for(RemitoSalida r : getRemitos()){
			suma += r.getCantidadPiezas();
		}
		return suma;
	}
	
	private JButton getBtnAgregarProducto() {
		if (btnAgregarProducto == null) {
			btnAgregarProducto = BossEstilos.createButton("ar/clarin/fwjava/imagenes/b_agregar.png", "ar/clarin/fwjava/imagenes/b_agregar_des.png");
			btnAgregarProducto.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					JDialogAgregarItemFactura jdaif = new JDialogAgregarItemFactura(JDialogCargaFactura.this, getRemitos()==null?1:getCantidadPiezasRemito(), 
																		getCliente(), getParametrosGenerales().getPrecioPorTubo(), 
																		getParametrosGenerales().getPorcentajeSeguro());
					jdaif.setVisible(true);
					if (jdaif.isAcepto()) {
						ItemFactura it = jdaif.getItemFacturaSeleccionado();

						if (it instanceof ItemFacturaSeguro) {
							if (haySeguro()) {
								CLJOptionPane.showErrorMessage(JDialogCargaFactura.this, "Ya se ha cargado el seguro.", "Error");
								return;
							}
						}

						if (it instanceof ItemFacturaTubo) {
							if (hayTubos()) {
								CLJOptionPane.showErrorMessage(JDialogCargaFactura.this, "Ya se han cargado los tubos.", "Error");
								return;
							}
						}
						
						if (it instanceof ItemFacturaPercepcion) {
							if (hayPercepcion()) {
								CLJOptionPane.showErrorMessage(JDialogCargaFactura.this, "Ya se ha cargado la percepción.", "Error");
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
						llenarTablaProductos();
					}
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
			btnQuitarProducto = BossEstilos.createButton("ar/clarin/fwjava/imagenes/b_eliminar.png", "ar/clarin/fwjava/imagenes/b_eliminar_des.png");
			btnQuitarProducto.setEnabled(false);
			btnQuitarProducto.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if (getTablaProductos().getSelectedRow() > -1) {
						ItemFactura itf = (ItemFactura) getTablaProductos().getValueAt(getTablaProductos().getSelectedRow(), COL_OBJ_FACTURA);
						getFactura().getItems().remove(itf);
						getTablaProductos().removeRow(getTablaProductos().getSelectedRow());
						calcularSubTotal();
					} else {
						getBtnQuitarProducto().setEnabled(false);
					}
				}
			});
		}
		return btnQuitarProducto;
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
		return panBotones;
	}

	private JButton getBtnGuardar() {
		if (btnGuardar == null) {
			btnGuardar = new JButton("Guardar e imprimir");
			btnGuardar.setMnemonic(KeyEvent.VK_G);
			btnGuardar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(!hayErrores()){
						boolean okImprimir = false;
						if (getCorrecionFactura() == null) {
							if(validarFecha()){
								if(getFactura().getItems().size()>0){
								okImprimir = guardarFactura();
								if(okImprimir){
									CLJOptionPane.showInformationMessage(JDialogCargaFactura.this, "La factura se ha guardado con éxito", "Alta de factura");
									}else{
										return;
									}
								}else{
									CLJOptionPane.showErrorMessage(JDialogCargaFactura.this, "Debe elegir al menos un item para la factura", "Error");
								}
							}
						} else {
							if(validarFecha()){
								okImprimir = guardarCorreccion();
								if(okImprimir){
									CLJOptionPane.showInformationMessage(JDialogCargaFactura.this, "La " + getCorrecionFactura().getTipo().getDescripcion() + " se ha guardado con éxito", "Alta de nota de crédito y débito");
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
								if(CLJOptionPane.showQuestionMessage(JDialogCargaFactura.this, "Desea imprimir?", "Pregunta")==CLJOptionPane.YES_OPTION){
									imprimir();
								}
							}catch(JRException jre){
								jre.printStackTrace();
								CLJOptionPane.showErrorMessage(JDialogCargaFactura.this, "Se ha producido un error al imprimir.", "Error");
							}
							dispose();
						}
					}else{
						CLJOptionPane.showErrorMessage(JDialogCargaFactura.this, "Hay errores en la tabla de productos. Asegurese de corregirlos y vuelva a intentarlo", "Error");
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
			CLJOptionPane.showErrorMessage(this, "Debe especificar el monto", "Error");
			return false;
		}
		if(getCorrecionFactura() instanceof NotaCredito && ((NotaCredito)getCorrecionFactura()).getFacturasRelacionadas().size()==0){
			CLJOptionPane.showErrorMessage(this, "Debe elegir las facturas relacionadas", "Error");
			return false;
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
		getCorrecionFactura().setDescripcion((String)getTablaProductos().getValueAt(0, COL_DESCRIPCION));
		long longFecha = 0;
		if(GenericUtils.esHoy(new java.sql.Date(getPanelFecha().getDate().getTime()))){//hoy
			longFecha = DateUtil.getAhora().getTime();
		}else{
			longFecha=getPanelFecha().getDate().getTime();
		}
		arreglarCorreccion();
		getCorrecionFactura().setFechaEmision(new Timestamp(longFecha));
		getCorrecionFactura().setCliente(getCliente());
		getCorrecionFactura().setTipoFactura(getCliente().getPosicionIva().getTipoFactura());
		String usuario = GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName();
		try {
			setCorrecionFactura(isEdicion()?getCorreccionFacade().editarCorreccion(getCorrecionFactura(), usuario):getCorreccionFacade().guardarCorreccionYGenerarMovimiento(getCorrecionFactura(), usuario));
		} catch (ValidacionException e) {
			CLJOptionPane.showErrorMessage(this, StringW.wordWrap(e.getMensajeError()), "Error");
			return false;
		} catch (ValidacionExceptionSinRollback e) {
			CLJOptionPane.showErrorMessage(this, StringW.wordWrap(e.getMensajeError()), "Error");
		}
		
		return true;
	}

	private void arreglarCorreccion() {
		if(getCorrecionFactura() instanceof NotaCredito){
			if(getCorrecionFactura().getMontoTotal() != null){
				getCorrecionFactura().setMontoTotal(getCorrecionFactura().getMontoTotal().multiply(new BigDecimal(-1)));
			}
			if(getCorrecionFactura().getMontoSubtotal() != null){
				getCorrecionFactura().setMontoSubtotal(getCorrecionFactura().getMontoSubtotal().multiply(new BigDecimal(-1)));
			}
			if(((NotaCredito)getCorrecionFactura()).getMontoSobrante()!=null){
				((NotaCredito)getCorrecionFactura()).setMontoSobrante(((NotaCredito)getCorrecionFactura()).getMontoSobrante().multiply(new BigDecimal(-1)));
			}
		}
	}

	private boolean guardarFactura() {
		// getFactura().setMontoFaltantePorPagar(new
		// BigDecimal(getTxtTotal().getText().trim().replace(',', '.')));
		// getFactura().setMontoTotal(new
		// BigDecimal(getTxtTotal().getText().trim().replace(',', '.')));
		if (getTxtPorcentajeIVA().getText().trim().length() > 0) {
			getFactura().setPorcentajeIVAInscripto(getFactura().getPorcentajeIVAInscripto());
		}
		// getFactura().setPorcentajeIVANoInscripto(new
		// BigDecimal(getTxtPorcentajeIVANoInscripto().getText()));
		long longFecha = 0;
		if(GenericUtils.esHoy(new java.sql.Date(getPanelFecha().getDate().getTime()))){//hoy
			longFecha = DateUtil.getAhora().getTime();
		}else{
			longFecha=getPanelFecha().getDate().getTime();
		}
		getFactura().setFechaEmision(new Timestamp(longFecha));
		getFactura().setCondicionDeVenta((CondicionDeVenta)getCmbCondicionVenta().getSelectedItem());
		// getFactura().setMontoImpuestos(new
		// BigDecimal(getTxtImpuestos().getText().trim().replace(',',
		// '.')));
		String usuario = GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName();
		try {
			setFactura(isEdicion()?getFacturaFacade().editarFactura(getFactura(),usuario):getFacturaFacade().guardarFacturaYGenerarMovimiento(getFactura(), usuario));
		} catch (ValidacionException e) {
			CLJOptionPane.showErrorMessage(this, StringW.wordWrap(e.getMensajeError()), "Error");
			return false;
		} catch (ValidacionExceptionSinRollback e) {
			CLJOptionPane.showErrorMessage(this, StringW.wordWrap(e.getMensajeError()), "Error");
			return false;
		}
		return true;
	}

	private JButton getBtnImprimir() {
		if (btnImprimir == null) {
			btnImprimir = new JButton("Imprimir");
			btnImprimir.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try{
						if(getCliente().getPosicionIva() == EPosicionIVA.EXPORTACION){
							return;
						}
						imprimir();
					}catch(JRException jre){
						jre.printStackTrace();
						CLJOptionPane.showErrorMessage(JDialogCargaFactura.this, "Se ha producido un error al imprimir.", "Error");
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
		// double subtotalConImpuestos = subTotal;
		// if (getTxtImpuestos().getText().trim().length() > 0) {
		// double impuestos = Double.valueOf(getTxtImpuestos().getText());
		// subtotalConImpuestos += impuestos;
		// }
		double total = subTotal;
		// getTxtSubTotalConImpuestos().setText(String.valueOf(subtotalConImpuestos));
		if (getFactura().getPorcentajeIVAInscripto() != null) {
			double valIVAInsc = getFactura().getPorcentajeIVAInscripto().doubleValue() / 100;
			// double valIVANoInsc =
			// Double.valueOf(getTxtPorcentajeIVANoInscripto().getText()) / 100;
			double ivaInsc = subTotal * valIVAInsc;
			// double ivaNoInsc = Math.round(subtotalConImpuestos *
			// valIVANoInsc);
			getTxtImporteIVAInscripto().setText(getDecimalFormat().format(ivaInsc));
			// getTxtImporteIVANoInscripto().setText(String.valueOf(ivaNoInsc));
			total += ivaInsc;// + ivaNoInsc;
		}
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
		// double subtotalConImpuestos = subTotal;
		// if (getTxtImpuestos().getText().trim().length() > 0) {
		// double impuestos = Double.valueOf(getTxtImpuestos().getText());
		// subtotalConImpuestos += impuestos;
		// }
		double total = subTotal;
		// getTxtSubTotalConImpuestos().setText(String.valueOf(subtotalConImpuestos));
		if (getCorrecionFactura().getPorcentajeIVAInscripto() != null) {
			double valIVAInsc = getCorrecionFactura().getPorcentajeIVAInscripto().doubleValue() / 100;
			// double valIVANoInsc =
			// Double.valueOf(getTxtPorcentajeIVANoInscripto().getText()) / 100;
			double ivaInsc = 0;
			if(getCorrecionFactura() instanceof NotaDebito && ( ((NotaDebito)getCorrecionFactura()).getChequeRechazado()!=null || (( ((NotaDebito)getCorrecionFactura()).getIsParaRechazarCheque()!=null) && ( ((NotaDebito)getCorrecionFactura()).getIsParaRechazarCheque()==true)) )&& ((NotaDebito)getCorrecionFactura()).getGastos()!=null){
				ivaInsc = valIVAInsc * ((NotaDebito)getCorrecionFactura()).getGastos().doubleValue();
			}else{
				ivaInsc =  subTotal * valIVAInsc;
			}
			// double ivaNoInsc = Math.round(subtotalConImpuestos *
			// valIVANoInsc);
			getTxtImporteIVAInscripto().setText( (getCorrecionFactura() instanceof NotaCredito && ivaInsc >0?"-":"") +  getDecimalFormat().format(ivaInsc));
			// getTxtImporteIVANoInscripto().setText(String.valueOf(ivaNoInsc));
			total += ivaInsc;// + ivaNoInsc;
		}
		getCorrecionFactura().setMontoTotal(new BigDecimal(total * (getCorreccionFacade() instanceof NotaCredito?-1:1)));
		// getFactura().setMontoImpuestos(montoImpuestos)
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
			int ret = CLJOptionPane.showQuestionMessage(this, "Va a salir sin guardar, esta seguro?", "Factura");
			if (ret == CLJOptionPane.YES_OPTION) {
				dispose();
			}
		} else {
			dispose();
		}
	}

	public Factura getFactura() {
		return factura;
	}

	private void inicializarFactura(Integer nroFactura) {
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
		for (ItemFactura it : getFactura().getItems()) {
			if (it.equals(itemModificado)) {
				it.setImporte(new BigDecimal(importe));
				it.setCantidad(new BigDecimal(cantidad));
				it.setPrecioUnitario(new BigDecimal(precioUnitario));
				break;
			}
		}
	}
	
	private void actualizarItemFactura(String descripcion, ItemFactura itemModificado) {
		for (ItemFactura it : getFactura().getItems()) {
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

	@SuppressWarnings("rawtypes")
	private void imprimir() throws JRException {
		boolean ok = false;
		do {
			String input = JOptionPane.showInputDialog(JDialogCargaFactura.this, "Ingrese la cantidad de copias: ", "Imprimir", JOptionPane.INFORMATION_MESSAGE);
			if(input == null){
				break;
			}
			if (input.trim().length()==0 || !GenericUtils.esNumerico(input)) {
				CLJOptionPane.showErrorMessage(JDialogCargaFactura.this, "Ingreso incorrecto", "error");
			} else {
				try{
					ok = true;
					if(getFactura()!=null && getFactura().getTipoFactura() == ETipoFactura.B){
						FacturaBTO facturaB = armarFacturaBTO();
						Map parameters = getParametrosB(facturaB);
						JasperReport reporte = JasperHelper.loadReporte("/ar/com/textillevel/reportes/facturab.jasper");
						JasperPrint jasperPrint = JasperHelper.fillReport(reporte, parameters, facturaB.getItems());
						CLJOptionPane.showWarningMessage(this, "RECUERDE UTILIZAR EL FORMULARIO PARA FACTURAS B", "Advertencia");
						Integer cantidadAImprimir = Integer.valueOf(input);
						Integer cantidadImpresa = JasperHelper.imprimirReporte(jasperPrint, true, true, cantidadAImprimir);
						if(cantidadImpresa.equals(cantidadAImprimir)){
							getFactura().setEstadoImpresion(EEstadoImpresionDocumento.IMPRESO);
						}else{
							getFactura().setEstadoImpresion(EEstadoImpresionDocumento.PENDIENTE);
						}
						setFactura(getFacturaFacade().actualizarFactura(getFactura()));
					}else{
						FacturaTO factura = armarFacturaTO();
						Map parameters = getParametros(factura);
						JasperReport reporte = JasperHelper.loadReporte("/ar/com/textillevel/reportes/factura_electronica.jasper");
						JasperPrint jasperPrint = JasperHelper.fillReport(reporte, parameters, factura.getItems());
						Integer cantidadAImprimir = Integer.valueOf(input);
						Integer cantidadImpresa = JasperHelper.imprimirReporte(jasperPrint, true, true, cantidadAImprimir);
						//JasperViewer.viewReport(jasperPrint, false);
						if (getCorrecionFactura() == null) {
							if(cantidadImpresa.equals(cantidadAImprimir)){
								getFactura().setEstadoImpresion(EEstadoImpresionDocumento.IMPRESO);
							}else{
								getFactura().setEstadoImpresion(EEstadoImpresionDocumento.PENDIENTE);
							}
							setFactura(getFacturaFacade().actualizarFactura(getFactura()));
						} else {
							if(cantidadImpresa.equals(cantidadAImprimir)){
								getCorrecionFactura().setEstadoImpresion(EEstadoImpresionDocumento.IMPRESO);
							}else{
								getCorrecionFactura().setEstadoImpresion(EEstadoImpresionDocumento.PENDIENTE);
							}
							setCorrecionFactura(getCorreccionFacade().actualizarCorreccion(getCorrecionFactura()));
						}
					}
				}catch(CLException e){
					BossError.gestionarError(e);
				}
			}
		} while (!ok);
	}

	private FacturaBTO armarFacturaBTO() {
		FacturaBTO factura = new FacturaBTO();
		factura.setCliente(getClienteBTO());
		factura.setItems(getItemsBTO());
		if(((CondicionDeVenta) getCmbCondicionVenta().getSelectedItem()).getNombre().equalsIgnoreCase("efectivo")){
			factura.setVentaContado("X");
		}else{
			factura.setVentaCtaCTe("X");
		}
		String fecha = DateUtil.dateToString(getPanelFecha().getDate(),DateUtil.SHORT_DATE);
		String[] partesFecha = fecha.split("/");
		factura.setAnioFecha(partesFecha[2]);
		factura.setDiaFecha(partesFecha[0]);
		factura.setMesFecha(partesFecha[1]);
		factura.setNroRemito(getPnlNroRemito().getNrosRemitos());
		factura.setTotalFactura(getCorrecionFactura() != null ? getDecimalFormat().format(getCorrecionFactura().getMontoTotal().doubleValue()).replace(',', '.') : getDecimalFormat().format(getFactura().getMontoTotal()));
		return factura;
	}
	
	private ClienteBTO getClienteBTO() {
		ClienteBTO cliente = new ClienteBTO();
		Cliente clientePosta = getCliente();
		if(clientePosta.getPosicionIva() == EPosicionIVA.CONSUMIDOR_FINAL){
			cliente.setCondicionIVAConsFinal("X");
		}else if(clientePosta.getPosicionIva() == EPosicionIVA.EXENTO){
			cliente.setCondicionIVAExento("X");
		}else if(clientePosta.getPosicionIva() == EPosicionIVA.MONOTRIBUTISTA){
			cliente.setCondicionIVARespMonot("X");
		}
		
		cliente.setCuit(clientePosta.getCuit());
		cliente.setDireccion(clientePosta.getDireccionReal().getDireccion());
		cliente.setLocalidad(clientePosta.getDireccionReal().getLocalidad().getNombreLocalidad());
		cliente.setRazonSocial(clientePosta.getRazonSocial());
		return cliente;
	}
	
	private List<ItemFacturaBTO> getItemsBTO() {
		List<ItemFacturaBTO> lista = new ArrayList<ItemFacturaBTO>();
		for (int i = 0; i < getTablaProductos().getRowCount(); i++) {
			ItemFacturaBTO ito = new ItemFacturaBTO();
			if(getTablaProductos().getValueAt(i, COL_CANTIDAD) instanceof BigDecimal){
				ito.setCantidad(getTablaProductos().getValueAt(i, COL_CANTIDAD) != null ? getDecimalFormat().format(getTablaProductos().getValueAt(i, COL_CANTIDAD)) : null);
			}else{
				ito.setCantidad(getTablaProductos().getValueAt(i, COL_CANTIDAD) != null ? getDecimalFormat().format(NumUtil.toBigDecimal(Double.valueOf(((String)getTablaProductos().getValueAt(i, COL_CANTIDAD))))) : null);
			}
			ito.setDescripcion((String) getTablaProductos().getValueAt(i, COL_DESCRIPCION));
			ito.setImporte(((String)(getTablaProductos().getValueAt(i, COL_IMPORTE))));
			ito.setPrecioUnitario(getTablaProductos().getValueAt(i, COL_PRECIO_UNITARIO) != null ? (String.valueOf(getTablaProductos().getValueAt(i, COL_PRECIO_UNITARIO))) : null);
			lista.add(ito);
		}
		return lista;
	}
	
	@SuppressWarnings("rawtypes")
	private Map getParametrosB(FacturaBTO factura) {
		Map parameters = new HashMap();
		parameters.put("RAZON_SOCIAL", factura.getCliente().getRazonSocial());
		parameters.put("DIRECCION", factura.getCliente().getDireccion());
		parameters.put("LOCALIDAD", factura.getCliente().getLocalidad());
		
		if(factura.getCliente().getCondicionIVAConsFinal() != null && factura.getCliente().getCondicionIVAConsFinal().equals("X")){
			parameters.put("IVA_CONS_F", factura.getCliente().getCondicionIVAConsFinal());
		}else if(factura.getCliente().getCondicionIVAExento()!=null && factura.getCliente().getCondicionIVAExento().equals("X")){
			parameters.put("IVA_EXENTO", factura.getCliente().getCondicionIVAExento());
		}else if(factura.getCliente().getCondicionIVARespMonot() != null && factura.getCliente().getCondicionIVARespMonot().equals("X")){
			parameters.put("IVA_MONO", factura.getCliente().getCondicionIVARespMonot());
		}
		parameters.put("CUIT", factura.getCliente().getCuit());
		parameters.put("NRO_REMITO", factura.getNroRemito());
		parameters.put("TOTAL", factura.getTotalFactura());
		parameters.put("FECHA_FACT_DIA", factura.getDiaFecha());
		parameters.put("FECHA_FACT_MES", factura.getMesFecha());
		parameters.put("FECHA_FACT_ANIO", factura.getAnioFecha());
		parameters.put("CV_CONT", factura.getVentaContado());
		parameters.put("CV_CTA_CTE", factura.getVentaCtaCTe());
		return parameters;
	}
	
	@SuppressWarnings("rawtypes")
	private Map getParametros(FacturaTO factura) {
		Map parameters = new HashMap();
		parameters.put("NRO_FACTURA", factura.getNroFactura());
		parameters.put("TIPO_FACTURA", factura.getTipoFactura());
		parameters.put("RAZON_SOCIAL", factura.getCliente().getRazonSocial());
		parameters.put("DIRECCION", factura.getCliente().getDireccion());
		parameters.put("LOCALIDAD", factura.getCliente().getLocalidad());
		parameters.put("IVA", factura.getCliente().getCondicionIVA());
		parameters.put("CUIT", factura.getCliente().getCuit());
		parameters.put("COND_VENTA", factura.getCondicionVenta());
		parameters.put("NRO_REMITO", factura.getNroRemito());
		parameters.put("SUBTOTAL", factura.getSubTotal());
		parameters.put("PORC_IVA", factura.getPorcIvaInsc());
		parameters.put("TOTAL_IVA", factura.getTotalIvaInscr());
		parameters.put("TOTAL", factura.getTotalFactura());
		parameters.put("CAE", factura.getCaeAFIP());
		parameters.put("FECHA_FACT", DateUtil.dateToString(DateUtil.stringToDate(factura.getFecha()),DateUtil.SHORT_DATE));
		parameters.put("TIPO_DOC", factura.getTipoDocumento());
		return parameters;
	}

	private FacturaTO armarFacturaTO() {
		FacturaTO factura = new FacturaTO();
		factura.setCliente(getClienteTO());
		factura.setItems(getItemsTO());
		factura.setCondicionVenta(((CondicionDeVenta) getCmbCondicionVenta().getSelectedItem()).getNombre());
		factura.setFecha(DateUtil.dateToString(getPanelFecha().getDate(),DateUtil.SHORT_DATE));
		factura.setNroFactura(getTxtNroFactura().getText());
		factura.setNroRemito(getStrFacturasRelacionadas()!=null && getStrFacturasRelacionadas().trim().length()>0?getStrFacturasRelacionadas().replaceAll("/", " / "):getPnlNroRemito().getNrosRemitos());
		factura.setPorcIvaInsc(getTxtPorcentajeIVA().getText().length() > 0 ? getTxtPorcentajeIVA().getText() : null);
		factura.setSubTotal( (getCorrecionFactura()!=null && getCorrecionFactura() instanceof NotaCredito?"-":"") + (getCorrecionFactura() != null ? getDecimalFormat().format(getCorrecionFactura().getMontoSubtotal().doubleValue()) : getDecimalFormat().format(getFactura().getMontoSubtotal().doubleValue())));
		factura.setTipoDocumento(getLblTipoDocumento().getText());
		factura.setTipoFactura(getCorrecionFactura() != null ? "" : getFactura().getTipoFactura().getDescripcion());
		factura.setTotalFactura(getCorrecionFactura() != null ? (getCorrecionFactura()!=null && getCorrecionFactura() instanceof NotaCredito?"-":"") +  getDecimalFormat().format(getCorrecionFactura().getMontoTotal().doubleValue()).replace(',', '.') : getDecimalFormat().format(getFactura().getMontoTotal()));
		factura.setCaeAFIP(getCorrecionFactura() != null ? getCorrecionFactura().getCaeAFIP() : getFactura().getCaeAFIP());
		if(getFactura()!=null){
			factura.setTotalIvaInscr(getFactura().getPorcentajeIVAInscripto() != null ? getDecimalFormat().format(getFactura().getPorcentajeIVAInscripto().doubleValue() * getFactura().getMontoSubtotal().doubleValue()/ 100) : null);
		}else{
			factura.setTotalIvaInscr(getCorrecionFactura().getPorcentajeIVAInscripto() != null ? (getCorrecionFactura()!=null && getCorrecionFactura() instanceof NotaCredito?"-":"") +  getTxtImporteIVAInscripto().getText() : null);
			//getCorrecionFactura().getPorcentajeIVAInscripto().doubleValue() * getCorrecionFactura().getMontoSubtotal().doubleValue()/ 100
		}
		
		return factura;
	}

	private ClienteTO getClienteTO() {
		ClienteTO cliente = new ClienteTO();
		Cliente clientePosta = getCliente();
		cliente.setCondicionIVA(clientePosta.getPosicionIva().getDescripcion());
		cliente.setCuit(clientePosta.getCuit());
		cliente.setDireccion(clientePosta.getDireccionReal().getDireccion());
		cliente.setLocalidad(clientePosta.getDireccionReal().getLocalidad().getNombreLocalidad());
		cliente.setRazonSocial(clientePosta.getRazonSocial());
		return cliente;
	}

	private List<ItemFacturaTO> getItemsTO() {
		List<ItemFacturaTO> lista = new ArrayList<ItemFacturaTO>();
		for (int i = 0; i < getTablaProductos().getRowCount(); i++) {
			ItemFacturaTO ito = new ItemFacturaTO();
			ito.setArticulo((String) getTablaProductos().getValueAt(i, COL_ARTICULO));
			if(getTablaProductos().getValueAt(i, COL_CANTIDAD) instanceof BigDecimal){
				ito.setCantidad(getTablaProductos().getValueAt(i, COL_CANTIDAD) != null ? getDecimalFormat().format(getTablaProductos().getValueAt(i, COL_CANTIDAD)) : null);
			}else{
				String valor =GenericUtils.formatDoubleStringArg((String)getTablaProductos().getValueAt(i, COL_CANTIDAD));
				valor = valor.replaceAll(",", ".");
				ito.setCantidad(getTablaProductos().getValueAt(i, COL_CANTIDAD) != null ? getDecimalFormat().format(NumUtil.toBigDecimal(Double.valueOf(valor))) : null);
			}
			ito.setDescripcion((String) getTablaProductos().getValueAt(i, COL_DESCRIPCION));
			ito.setImporte(((String)(getTablaProductos().getValueAt(i, COL_IMPORTE))));
			ito.setPrecioUnitario(getTablaProductos().getValueAt(i, COL_PRECIO_UNITARIO) != null ? (getCorrecionFactura()!=null && getCorrecionFactura() instanceof NotaCredito?"-":"") +  (String.valueOf(getTablaProductos().getValueAt(i, COL_PRECIO_UNITARIO))) : null);
			ito.setUnidad((String) getTablaProductos().getValueAt(i, COL_UNIDAD));
			lista.add(ito);
		}
		return lista;
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
		List<Timestamp> facturaAnteriorYPosterior = getFacturaFacade().getFechasFacturasAnteriorYPosterior(nroFactura,getFactura()!=null?getFactura().getTipoFactura():getCorrecionFactura().getTipoFactura());
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
					CLJOptionPane.showInformationMessage(this, StringW.wordWrap("Atención, esto es una corrección. Se guardará el documento.\n\nLa fecha debe ser: " + DateUtil.dateToString(f1, DateUtil.SHORT_DATE)), "Corrección");
					return true;
				} else {
					if (!fechaIngresada.before(DateUtil.redondearFecha(f1)) && !fechaIngresada.after(DateUtil.redondearFecha(f2))) {
						return true;
					} else {
						CLJOptionPane.showErrorMessage(this, "La fecha ingresada debe estar entre " + DateUtil.dateToString(f1, DateUtil.SHORT_DATE) + " y "
								+ DateUtil.dateToString(f2, DateUtil.SHORT_DATE), "Error");
						return false;
					}
				}
			} else {// por descarte el size es 1
				if (!getPanelFecha().getDate().before(DateUtil.redondearFecha(facturaAnteriorYPosterior.get(0)))) {
					return true;
				}
				CLJOptionPane.showErrorMessage(this, "La fecha debe ser posterior a " + DateUtil.dateToString(facturaAnteriorYPosterior.get(0)), "Error");
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

	private RemitoEntradaFacadeRemote getRemitoEntradaFacade(){
		if(remitoEntradaFacade == null){
			remitoEntradaFacade = GTLBeanFactory.getInstance().getBean2(RemitoEntradaFacadeRemote.class);
		}
		return remitoEntradaFacade;
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
	
	private class RemitoLinkeableLabel extends LinkableLabel {

		private static final long serialVersionUID = -4765485631705199316L;

		private RemitoSalida remito;

		public RemitoLinkeableLabel() {
			super("x");
		}

		@Override
		public void labelClickeada(MouseEvent e) {
			if (e.getClickCount() == 1 && remito!=null) {
				RemitoSalida sremito = GTLBeanFactory.getInstance().getBean2(RemitoSalidaFacadeRemote.class).getByIdConPiezasYProductos(remito.getId());
				OperacionSobreRemitoSalidaHandler handler = new OperacionSobreRemitoSalidaHandler(null, sremito, true);
				handler.showRemitoEntradaDialog();
			}
		}

		public void setRemito(RemitoSalida remito) {
			this.remito = remito;
			if(remito != null) {
				setTexto(String.valueOf(remito.getNroRemito()));
				refreshLabel();
			}
		}

	}
	
	public CLJTextField getTxtNrosGenericos() {
		if(txtNrosGenericos == null){
			txtNrosGenericos = new CLJTextField();
			txtNrosGenericos.setEditable(false);
		}
		return txtNrosGenericos;
	}
}