package ar.com.textillevel.gui.acciones;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EtchedBorder;

import main.GTLGlobalCache;

import org.apache.commons.collections.CollectionUtils;
import org.apache.taglibs.string.util.StringW;

import ar.com.fwcommon.boss.BossError;
import ar.com.fwcommon.componentes.FWDobleLista;
import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTable;
import ar.com.fwcommon.componentes.FWJTextField;
import ar.com.fwcommon.componentes.PanelTabla;
import ar.com.fwcommon.componentes.VerticalFlowLayout;
import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.util.DateUtil;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.entidades.cheque.Cheque;
import ar.com.textillevel.entidades.documentos.factura.CondicionDeVentaDiferida;
import ar.com.textillevel.entidades.documentos.factura.proveedor.FacturaProveedor;
import ar.com.textillevel.entidades.documentos.factura.proveedor.NotaCreditoProveedor;
import ar.com.textillevel.entidades.documentos.factura.proveedor.NotaDebitoProveedor;
import ar.com.textillevel.entidades.documentos.ordendepago.OrdenDePago;
import ar.com.textillevel.entidades.documentos.ordendepago.formapago.FormaPagoOrdenDePago;
import ar.com.textillevel.entidades.documentos.ordendepago.formapago.FormaPagoOrdenDePagoCheque;
import ar.com.textillevel.entidades.documentos.ordendepago.formapago.FormaPagoOrdenDePagoEfectivo;
import ar.com.textillevel.entidades.documentos.ordendepago.formapago.FormaPagoOrdenDePagoNotaCredito;
import ar.com.textillevel.entidades.documentos.ordendepago.formapago.FormaPagoOrdenDePagoRetencionIVA;
import ar.com.textillevel.entidades.documentos.ordendepago.formapago.FormaPagoOrdenDePagoRetencionIngresosBrutos;
import ar.com.textillevel.entidades.documentos.ordendepago.formapago.FormaPagoOrdenDePagoRetencionesGanancias;
import ar.com.textillevel.entidades.documentos.ordendepago.formapago.FormaPagoOrdenDePagoTransferencia;
import ar.com.textillevel.entidades.documentos.ordendepago.formapago.IFormaPagoVisitor;
import ar.com.textillevel.entidades.documentos.ordendepago.pagos.PagoOrdenDePago;
import ar.com.textillevel.entidades.documentos.ordendepago.pagos.PagoOrdenDePagoACuenta;
import ar.com.textillevel.entidades.documentos.ordendepago.pagos.PagoOrdenDePagoFactura;
import ar.com.textillevel.entidades.documentos.ordendepago.pagos.PagoOrdenDePagoNotaDebito;
import ar.com.textillevel.entidades.documentos.ordendepago.pagos.visitor.IPagoOrdenPagoVisitor;
import ar.com.textillevel.entidades.enums.EDescripcionPagoFactura;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.gente.Proveedor;
import ar.com.textillevel.facade.api.remote.ChequeFacadeRemote;
import ar.com.textillevel.facade.api.remote.ClienteFacadeRemote;
import ar.com.textillevel.facade.api.remote.FacturaProveedorFacadeRemote;
import ar.com.textillevel.facade.api.remote.OrdenDePagoFacadeRemote;
import ar.com.textillevel.gui.acciones.impresionordendepago.ImprimirOrdenDePagoHandler;
import ar.com.textillevel.gui.modulos.cheques.gui.JDialogAgregarCheque;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.controles.PanelDatePicker;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogCargaOrdenDePago extends JDialog {

	private static final long serialVersionUID = -4389641508487964200L;

	private static final int CANT_COLS = 4;
	private static final int COL_FECHA = 0;
	private static final int COL_DOCUMENTO = 1;
	private static final int COL_IMPORTE = 2;
	private static final int COL_OBJ = 3;

	private static final int CANT_COLS_NOTA_CREDITO = 4;
	private static final int COL_FECHA_NOTA_CREDITO = 0;
	private static final int COL_DOCUMENTO_NOTA_CREDITO = 1;
	private static final int COL_IMPORTE_NOTA_CREDITO = 2;
	private static final int COL_OBJ_NOTA_CREDITO = 3;
	
	private Proveedor proveedor;

	private JPanel panTransferencias;
	private PanelTablaFacturas panelTablaFacturas;
	private PanelTablaFormaPagoOrdenDePagoNotaCredito panelTablaNotasDeCredito;
	private FWDobleLista dobleListaCheques;
	private PanelDatePicker panelFecha;

	private FWJTextField txtNroOrden;
	private FWJTextField txtRetencionesIngresosBrutos;
	private FWJTextField txtRetencionesIva;
	private FWJTextField txtRetencionesGanancias;
	private FWJTextField txtEfectivo;
	private FWJTextField txtTotal;
	private FWJTextField txtTotalLetras;
	private FWJTextField txtTotalFacturas;
	private FWJTextField txtTotalCheques;
	private FWJTextField txtTotalNotasDeCredito;
	private FWJTextField txtImporteTransf;
	private FWJTextField txtNroTransf;
	private FWJTextField txtObservacionesTransf;
	
	private FWJTextField txtRazonSocial;
	private FWJTextField txtCuit;
	private FWJTextField txtDireccion;
	private FWJTextField txtCondicionDeVenta;
	private FWJTextField txtNroRecibo;
	
	private FWJTextField txtUsuarioCreador;

	private JButton btnSugerirCheques;
	private JButton btnLimpiar;
	private JButton btnBuscarCheques;
	private JButton btnAgregarChequeTerceros;
	private JButton btnVerificar;
	private JButton btnSalir;

	private JButton btnImprimir;
	private JButton btnGuardar;

	private JPanel pnlNorte;
	private JPanel pnlCentro;
	private JPanel pnlSur;

	private ModeloDialogoAltaOrdenDePago modelo;

	private EEstadoPantalla estadoPantalla;
	
	private boolean consulta;

	private OrdenDePagoFacadeRemote ordenDePagoFacade;
	private FacturaProveedorFacadeRemote facturaProveedorFacade;
	private ChequeFacadeRemote chequeFacade;
	
	private OrdenDePago ordenFinal;
	
	private boolean edicion;
	private boolean iniciando = false;
	
	/**
	 * Constructor de alta
	 * @param padre
	 * @param proveedor
	 */
	public JDialogCargaOrdenDePago(Frame padre, Proveedor proveedor) {
		super(padre);
		this.proveedor = proveedor;
		this.modelo = new ModeloDialogoAltaOrdenDePago();
		this.consulta =false;
		this.edicion = false;
		setEstadoPantalla(EEstadoPantalla.NO_VERIFICADO);
		getBtnImprimir().setEnabled(false);
		setUpScreen();
		setUpComponentes();
		cargarDatosProveedor();
		getTxtNroOrden().setText(String.valueOf(getOrdenDePagoFacade().getNewNroOrdenDePago()));
		refrescarModelo();
	}
	
	/**
	 * Constructor de consulta
	 * @param padre
	 * @param orden
	 */
	public JDialogCargaOrdenDePago(Frame padre, OrdenDePago orden, boolean consulta) {
		super(padre);
		this.proveedor = orden.getProveedor();
		this.consulta =consulta;
		this.ordenFinal = orden;
		this.edicion=!consulta;
		this.iniciando = true;
		setUpScreen();
		setUpComponentes();
		if(consulta){
			GuiUtil.setEstadoPanel(getPanelCentro(), false);
			GuiUtil.setEstadoPanel(getPanelNorte(), false);
			getBtnVerificar().setEnabled(false);
			getBtnGuardar().setEnabled(false);
		}else{
			hidratarModelo(orden);
		}
		getBtnImprimir().setEnabled(true);
		cargarDatosConsulta(orden);
		this.iniciando = false;
	}

	private void hidratarModelo(OrdenDePago orden) {
		this.modelo = new ModeloDialogoAltaOrdenDePago();
		this.modelo.getPagos().addAll(orden.getPagos());
		this.modelo.getFormasDePago().addAll(orden.getFormasDePago());
		for(PagoOrdenDePago p : orden.getPagos()){
			if(p instanceof PagoOrdenDePagoFactura){
				PagoOrdenDePagoFactura pagoOrdenDePagoFactura = (PagoOrdenDePagoFactura)p;
				FacturaProveedor factura = pagoOrdenDePagoFactura.getFactura();
				factura.setMontoFaltantePorPagar(pagoOrdenDePagoFactura.getMontoPagado());
				this.modelo.getIdFacturasUtilizadas().add(factura.getId());
			}else if(p instanceof PagoOrdenDePagoNotaDebito){
				PagoOrdenDePagoNotaDebito pagoOrdenDePagoNotaDebito = (PagoOrdenDePagoNotaDebito)p;
				NotaDebitoProveedor notaDebito = pagoOrdenDePagoNotaDebito.getNotaDebito();
				notaDebito.setMontoFaltantePorPagar(pagoOrdenDePagoNotaDebito.getMontoPagado());
				this.modelo.getIdFacturasUtilizadas().add(notaDebito.getId());
			}
		}
		for(FormaPagoOrdenDePago fp : orden.getFormasDePago()){
			if(fp instanceof FormaPagoOrdenDePagoCheque){
				this.modelo.getChequesUtilizados().add(((FormaPagoOrdenDePagoCheque)fp).getCheque());
			}else if(fp instanceof FormaPagoOrdenDePagoNotaCredito){
				this.modelo.getIdNotasCreditoUtilizadas().add(((FormaPagoOrdenDePagoNotaCredito)fp).getNotaCredito().getId());
			}
		}
	}

	private void cargarDatosConsulta(OrdenDePago orden) {
		cargarDatosProveedor();
		getTxtNroOrden().setText(String.valueOf(orden.getNroOrden()));
		if(orden.getNroReciboProveedor()!=null){
			getTxtNroRecibo().setText(String.valueOf(orden.getNroReciboProveedor()));
		}
		getPanelFecha().setSelectedDate(orden.getFechaEmision());
		cargarFormasDePago(orden);
		cargarPagos(orden);
	}
	
	private void cargarPagos(OrdenDePago orden) {
		CrearFilaPagoOrdenDePagoVisitor visitor = new CrearFilaPagoOrdenDePagoVisitor();
		BigDecimal suma = new BigDecimal(0d);
		for(PagoOrdenDePago p : orden.getPagos()){
			p.accept(visitor);
			suma = suma.add(p.getMontoPagado());
		}
		getTxtTotalFacturas().setText(GenericUtils.getDecimalFormatTablaMovimientos().format(suma.doubleValue()));
	}

	private void cargarFormasDePago(OrdenDePago orden) {
 		FormaPagoOrdenDePagoConsultaVisitor visitor = new FormaPagoOrdenDePagoConsultaVisitor();
		BigDecimal suma = new BigDecimal(0d);
		for(FormaPagoOrdenDePago fp : orden.getFormasDePago()){
			suma = suma.add(fp.getImporte());
			fp.accept(visitor);
		}
		getDobleListaCheques().setListaOriginal(visitor.getCheques());
		getTxtTotalCheques().setText(GenericUtils.getDecimalFormatTablaMovimientos().format(visitor.getSumaCheques().doubleValue()));
		getTxtTotal().setText(GenericUtils.getDecimalFormatTablaMovimientos().format(suma.doubleValue()));
		getTxtTotalNotasDeCredito().setText(GenericUtils.getDecimalFormatTablaMovimientos().format(visitor.getSumaNC().doubleValue()));
		getTxtTotalLetras().setText(GenericUtils.convertirNumeroATexto(suma.doubleValue()));
	}
	
	private class FormaPagoOrdenDePagoConsultaVisitor implements IFormaPagoVisitor{

		private final List<Cheque> cheques;
		private BigDecimal sumaCheques;
		private BigDecimal sumaNC;
		
		public FormaPagoOrdenDePagoConsultaVisitor() {
			cheques = new ArrayList<Cheque>();
			sumaCheques = new BigDecimal(0d);
			sumaNC = new BigDecimal(0d);
		}

		public void visit(FormaPagoOrdenDePagoCheque formaPago) {
			cheques.add(formaPago.getCheque());
			sumaCheques = sumaCheques.add(formaPago.getCheque().getImporte());
		}

		public void visit(FormaPagoOrdenDePagoEfectivo formaPagoEfectivo) {
			getTxtEfectivo().setText(GenericUtils.getDecimalFormatTablaMovimientos().format(formaPagoEfectivo.getImporte().doubleValue()));
		}

		public void visit(FormaPagoOrdenDePagoRetencionIngresosBrutos formaPagoRetencionIngresosBrutos) {
			getTxtRetencionesIngresosBrutos().setText(GenericUtils.getDecimalFormatTablaMovimientos().format(formaPagoRetencionIngresosBrutos.getImporte().doubleValue()));
		}

		public void visit(FormaPagoOrdenDePagoRetencionIVA formaPagoRetencionIVA) {
			getTxtRetencionesIva().setText(GenericUtils.getDecimalFormatTablaMovimientos().format(formaPagoRetencionIVA.getImporte().doubleValue()));
		}

		public void visit(FormaPagoOrdenDePagoTransferencia formaPagoTransferencia) {
			getTxtObservacionesTransf().setText(formaPagoTransferencia.getObservaciones());
			getTxtNroTransf().setText(String.valueOf(formaPagoTransferencia.getNroTx()));
			getTxtImporteTransf().setText(GenericUtils.getDecimalFormatTablaMovimientos().format(formaPagoTransferencia.getImporte().doubleValue()));
		}

		public void visit(FormaPagoOrdenDePagoNotaCredito formaPagoNotaCredito) {
			getPanelTablaNotasDeCredito().agregarElemento(formaPagoNotaCredito);
			sumaNC = sumaNC.add(formaPagoNotaCredito.getImporteNC());
		}

		public void visit(FormaPagoOrdenDePagoRetencionesGanancias formaPagoGanancias) {
			getTxtRetencionesGanancias().setText(GenericUtils.getDecimalFormatTablaMovimientos().format(formaPagoGanancias.getImporte().doubleValue()));
		}

		public List<Cheque> getCheques() {
			return cheques;
		}

		public BigDecimal getSumaCheques() {
			return sumaCheques;
		}

		
		public BigDecimal getSumaNC() {
			return sumaNC;
		}
	}

	private void cargarDatosProveedor() {
		getTxtRazonSocial().setText(getProveedor().getRazonSocial());
		getTxtCuit().setText(getProveedor().getCuit());
		getTxtDireccion().setText(getProveedor().getDireccionFiscal().getDireccion() + " - " + getProveedor().getDireccionFiscal().getLocalidad());
		getTxtCondicionDeVenta().setText(getProveedor().getCondicionVenta().toString());
	}

	private void setUpComponentes() {
		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				salir();
			}
		});
		this.add(getPanelNorte(), BorderLayout.NORTH);
		
		JScrollPane jsp = new JScrollPane(getPanelCentro(), JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jsp.setPreferredSize(new Dimension(1024, 768));
		this.add(jsp, BorderLayout.CENTER);
		this.add(getPanelSur(), BorderLayout.SOUTH);
	}

	private void setUpScreen() {
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setTitle("Alta Orden de Pago");
		setModal(true);
		setSize(GenericUtils.getDimensionPantalla());
//		setSize(new Dimension(1024, 768));
//		setResizable(false);
		GuiUtil.centrar(this);
	}

	private JPanel getPanelSur() {
		if (pnlSur == null) {
			pnlSur = new JPanel();
			pnlSur.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			pnlSur.add(getBtnVerificar());
			pnlSur.add(getBtnGuardar());
			pnlSur.add(getBtnImprimir());
			pnlSur.add(getBtnSalir());
			pnlSur.add(new JLabel("Usuario: "));
			pnlSur.add(getTxtUsuarioCreador());
		}
		return pnlSur;
	}

	private JPanel getPanelCentro() {
		if (pnlCentro == null) {
			pnlCentro = new JPanel();
			pnlCentro.setLayout(new VerticalFlowLayout(VerticalFlowLayout.TOP,100,0));

			JPanel panelContenedor = new JPanel();
			panelContenedor.setLayout(new GridBagLayout());
			
			JPanel panelIzquierda = new JPanel();
			panelIzquierda.setLayout(new VerticalFlowLayout(VerticalFlowLayout.TOP, 5, 5));
			
			JPanel panelCentroIzquierda = new JPanel();
			panelCentroIzquierda.setBorder(BorderFactory.createTitledBorder("Facturas"));
			panelCentroIzquierda.setLayout(new VerticalFlowLayout(VerticalFlowLayout.TOP, 5, 5));
			panelCentroIzquierda.add(getPanelTablaFacturas());

			JPanel panelSurCentroIzquierda = new JPanel();
			panelSurCentroIzquierda.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			panelSurCentroIzquierda.add(new JLabel("Total: "));
			panelSurCentroIzquierda.add(getTxtTotalFacturas());
			panelCentroIzquierda.add(panelSurCentroIzquierda);
			
			JPanel panelSurIzquierda = new JPanel();
			panelSurIzquierda.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			panelSurIzquierda.add(new JLabel("Total: "));
			panelSurIzquierda.add(getTxtTotalNotasDeCredito());

			panelIzquierda.add(panelCentroIzquierda);
			
			JPanel panelNc = new JPanel();
			panelNc.setLayout(new VerticalFlowLayout(VerticalFlowLayout.TOP, 5, 5));
			panelNc.setBorder(BorderFactory.createTitledBorder("Notas de crédito"));
			panelNc.add(getPanelTablaNotasDeCredito());
			panelNc.add(panelSurIzquierda);
			
			panelIzquierda.add(panelCentroIzquierda);
			panelIzquierda.add(panelNc);
			
			JPanel panelDerechaCheques = new JPanel();
			panelDerechaCheques.setLayout(new GridLayout(3, 2, 5, 5));
			panelDerechaCheques.add(new JLabel("Total cheques: "));
			panelDerechaCheques.add(getTxtTotalCheques());
			panelDerechaCheques.add(getBtnSugerirCheques());
			panelDerechaCheques.add(getBtnLimpiar());
			panelDerechaCheques.add(getBtnBuscarCheques());
			panelDerechaCheques.add(getBtnAgregarChequeTerceros());

			JPanel panelCentroDerecha = new JPanel();
			panelCentroDerecha.setLayout(new GridBagLayout());
			panelCentroDerecha.setBorder(BorderFactory.createTitledBorder("Cheques"));
			panelCentroDerecha.add(getDobleListaCheques(), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0,5,0,5), 3, 1, 1, 1));
			panelCentroDerecha.add(panelDerechaCheques, GenericUtils.createGridBagConstraints(1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 3, 1, 1, 0));
			
			panelContenedor.add(panelIzquierda, GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0,5,0,5), 1, 1, 1, 1));
			panelContenedor.add(panelCentroDerecha, GenericUtils.createGridBagConstraints(2, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0,5,0,5), 2, 1, 1, 1));
			panelContenedor.add(getPanTransferencias(), GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0,5,0,5), 4, 1, 1, 1));
			
			JPanel panelAbajoTotales = new JPanel();
			panelAbajoTotales.setBorder(BorderFactory.createTitledBorder("Totales"));
			panelAbajoTotales.setLayout(new GridBagLayout());
			panelAbajoTotales.add(new JLabel("Ret. Ingresos Brutos: "), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 1));
			panelAbajoTotales.add(getTxtRetencionesIngresosBrutos(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 1));
			panelAbajoTotales.add(new JLabel("Ret. IVA: "), GenericUtils.createGridBagConstraints(2, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 1));
			panelAbajoTotales.add(getTxtRetencionesIva(), GenericUtils.createGridBagConstraints(3, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 1));
			panelAbajoTotales.add(new JLabel("Ret. Ganancias: "), GenericUtils.createGridBagConstraints(4, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 1));
			panelAbajoTotales.add(getTxtRetencionesGanancias(), GenericUtils.createGridBagConstraints(5, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 1));
			panelAbajoTotales.add(new JLabel("Efectivo: "), GenericUtils.createGridBagConstraints(6, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 1));
			panelAbajoTotales.add(getTxtEfectivo(), GenericUtils.createGridBagConstraints(7, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 1));
			panelAbajoTotales.add(new JLabel("Pesos: "), GenericUtils.createGridBagConstraints(1, 1, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,new Insets(5, 5, 5, 5), 1, 1, 1, 1));
			panelAbajoTotales.add(getTxtTotalLetras(), GenericUtils.createGridBagConstraints(2, 1, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,new Insets(5, 5, 5, 5), 3, 1, 1, 1));
			panelAbajoTotales.add(new JLabel("Total: "), GenericUtils.createGridBagConstraints(5, 1, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 1));
			panelAbajoTotales.add(getTxtTotal(), GenericUtils.createGridBagConstraints(6, 1, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,new Insets(5, 5, 5, 5), 1, 1, 1, 1));
			
			pnlCentro.add(panelContenedor);
			pnlCentro.add(panelAbajoTotales);
		}
		return pnlCentro;
	}

	private JPanel getPanelNorte() {
		if (pnlNorte == null) {
			pnlNorte = new JPanel();
			pnlNorte.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			pnlNorte.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));

			JPanel pnlContainer = new JPanel();
			pnlContainer.setLayout(new VerticalFlowLayout(VerticalFlowLayout.CENTER, 5, 5));

			JPanel p1 = new JPanel();
			p1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			p1.add(new JLabel("Proveedor: "));
			p1.add(getTxtRazonSocial());
			p1.add(new JLabel("C.U.I.T: "));
			p1.add(getTxtCuit());
			p1.add(new JLabel("Dirección: "));
			p1.add(getTxtDireccion());

			JPanel p2 = new JPanel();
			p2.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			p2.add(new JLabel("Condición de venta: "));
			p2.add(getTxtCondicionDeVenta());
			p2.add(new JLabel("Nº de orden: "));
			p2.add(getTxtNroOrden());
			p2.add(new JLabel("Fecha: "));
			p2.add(getPanelFecha());
			if(getOrdenFinal()!=null && getOrdenFinal().getNroReciboProveedor()!=null){
				p2.add(new JLabel("Recibo Nº: "));
				p2.add(getTxtNroRecibo());
			}

			pnlContainer.add(p1);
			pnlContainer.add(p2);
			pnlNorte.add(pnlContainer);
		}
		return pnlNorte;
	}

	public Proveedor getProveedor() {
		return proveedor;
	}

	public void setProveedor(Proveedor proveedor) {
		this.proveedor = proveedor;
	}

	private class PanelTablaFacturas extends PanelTabla<PagoOrdenDePago> {

		private static final long serialVersionUID = 7088641132965760957L;

		private final CrearFilaPagoOrdenDePagoVisitor filaVisitor = new CrearFilaPagoOrdenDePagoVisitor();

		public PanelTablaFacturas(){
			super();
			setPreferredSize(new Dimension(550, 240));
		}
		
		@Override
		protected FWJTable construirTabla() {
			FWJTable tablaFacturas = new FWJTable(0, CANT_COLS) {

				private static final long serialVersionUID = 868319830800206960L;

				@Override
				public void cellEdited(int cell, int row) {
					if (cell == COL_IMPORTE) {
						String valor = ((String) getValueAt(row, cell)).trim();
						if (GenericUtils.esNumerico(valor)) {
							Double importe = Double.valueOf(valor);
							PagoOrdenDePago pago = (PagoOrdenDePago) getValueAt(row, COL_OBJ);
							ActualizarValorPagoOrdenDePagoVisitor actualizarVisitor = new ActualizarValorPagoOrdenDePagoVisitor(importe);
							pago.accept(actualizarVisitor);
							if (actualizarVisitor.isErrorImporte()) {
								FWJOptionPane.showErrorMessage(JDialogCargaOrdenDePago.this, "El importe debe ser menor al monto faltante por pagar", "Error");
							}
							refrescarModelo();
						} else {
							FWJOptionPane.showErrorMessage(JDialogCargaOrdenDePago.this, "Solo puede ingresar números", "Error");
							setEstadoPantalla(EEstadoPantalla.ESTADO_INVALIDO);
						}
					}
				}
			};
			tablaFacturas.setDateColumn(COL_FECHA, "Fecha", 90, true);
			tablaFacturas.setMultilineColumn(COL_DOCUMENTO, "Documento", 280, true, true);
			tablaFacturas.setFloatColumn(COL_IMPORTE, "Importe", 90, true);
			tablaFacturas.setStringColumn(COL_OBJ, "", 0);
			tablaFacturas.setHeaderAlignment(COL_FECHA, FWJTable.CENTER_ALIGN);
			tablaFacturas.setHeaderAlignment(COL_DOCUMENTO, FWJTable.CENTER_ALIGN);
			tablaFacturas.setHeaderAlignment(COL_IMPORTE, FWJTable.CENTER_ALIGN);
			tablaFacturas.setReorderingAllowed(false);
			tablaFacturas.setAllowSorting(false);
			tablaFacturas.setAllowHidingColumns(false);
			tablaFacturas.setRowHeight(tablaFacturas.getRowHeight() + 15);
			return tablaFacturas;
		}

		@Override
		protected PagoOrdenDePago getElemento(int fila) {
			return (PagoOrdenDePago) getTabla().getValueAt(fila, COL_OBJ);
		}

		@Override
		protected String validarElemento(int fila) {
			return null;
		}

		@Override
		protected void agregarElemento(PagoOrdenDePago elemento) {
			elemento.accept(filaVisitor);
		}

		@Override
		public boolean validarAgregar() {
			List<Integer> idFacturasUtilizadas = modelo.getIdFacturasUtilizadas();
			idFacturasUtilizadas.removeAll(modelo.getIdsFacturasQuitadas());
			
			List<Integer> idNotasDebitoUtilizadas = modelo.getIdNotasDebitoUtilizadas();
			idNotasDebitoUtilizadas.removeAll(modelo.getIdsNotasDebitoQuitadas());
			
			JDialogBuscarFacturasYNotasDebitoImpagasProveedor dialog = new JDialogBuscarFacturasYNotasDebitoImpagasProveedor(JDialogCargaOrdenDePago.this, getProveedor(), idFacturasUtilizadas, idNotasDebitoUtilizadas);
			dialog.setVisible(true);
			if (dialog.isAcepto()) {
				List<FacturaProveedor> facturas = dialog.getFacturasSeleccionadas();
				for (FacturaProveedor f : facturas) {
					PagoOrdenDePagoFactura popf = new PagoOrdenDePagoFactura();
					popf.setFactura(f);
					popf.setMontoPagado(f.getMontoFaltantePorPagar());
					modelo.getPagosFijos().add(popf);
					idFacturasUtilizadas.add(f.getId());
					modelo.getIdsFacturasQuitadas().remove(f.getId());
				}
				List<NotaDebitoProveedor> nds = dialog.getNotasDebitoSeleccionadas();
				for(NotaDebitoProveedor nd : nds){
					PagoOrdenDePagoNotaDebito popnd = new PagoOrdenDePagoNotaDebito();
					popnd.setNotaDebito(nd);
					popnd.setMontoPagado(nd.getMontoFaltantePorPagar());
					modelo.getPagosFijos().add(popnd);
					idNotasDebitoUtilizadas.add(nd.getId());
					modelo.getIdsNotasDebitoQuitadas().remove(nd.getId());
				}
				bloquearCeldas();
				refrescarModelo();
				sugerirCheques();
			}
			return false;
		}

		private void bloquearCeldas() {
			for (int i = 0; i < getPanelTablaFacturas().getTabla().getRowCount(); i++) {
				PagoOrdenDePago p = (PagoOrdenDePago) getPanelTablaFacturas().getTabla().getValueAt(i, COL_OBJ);
				if (p instanceof PagoOrdenDePagoACuenta) {
					getPanelTablaFacturas().getTabla().lockCell(i, COL_IMPORTE);
				}
			}
		}

		@Override
		public boolean validarQuitar() {
			FWJTable tabla = getPanelTablaFacturas().getTabla();
			PagoOrdenDePago p = (PagoOrdenDePago) tabla.getValueAt(tabla.getSelectedRow(), COL_OBJ);
			if (p instanceof PagoOrdenDePagoACuenta) {
				return false;
			}
			if(isEdicion()){
				modelo.getPagos().remove(p);
			}else{
				modelo.getPagosFijos().remove(p);
			}
			if(p instanceof PagoOrdenDePagoFactura){
				modelo.getIdFacturasUtilizadas().remove(((PagoOrdenDePagoFactura) p).getFactura().getId());
				modelo.getIdsFacturasQuitadas().add(((PagoOrdenDePagoFactura) p).getFactura().getId());
			}else if (p instanceof PagoOrdenDePagoNotaDebito){
				modelo.getIdNotasDebitoUtilizadas().remove(((PagoOrdenDePagoNotaDebito)p).getNotaDebito().getId());
				modelo.getIdsNotasDebitoQuitadas().add(((PagoOrdenDePagoNotaDebito)p).getNotaDebito().getId());
			}
			refrescarModelo();
			return false;
		}
	}
	
	private class PanelTablaFormaPagoOrdenDePagoNotaCredito extends PanelTabla<FormaPagoOrdenDePagoNotaCredito> {

		private static final long serialVersionUID = 7088641132965760957L;


		public PanelTablaFormaPagoOrdenDePagoNotaCredito(){
			super();
			setPreferredSize(new Dimension(550, 140));
		}
		
		@Override
		protected FWJTable construirTabla() {
			FWJTable tablaNC = new FWJTable(0, CANT_COLS_NOTA_CREDITO) {

				private static final long serialVersionUID = 868319830800206960L;

				@Override
				public void cellEdited(int cell, int row) {
					if (cell == COL_IMPORTE_NOTA_CREDITO) {
						String valor = ((String) getValueAt(row, cell)).trim();
						valor = valor.replace(".", "").replace(",", ".");
						if (GenericUtils.esNumerico(valor)) {
							Double importe = Double.valueOf(valor);
							FormaPagoOrdenDePagoNotaCredito formaPagoNotaCredito = (FormaPagoOrdenDePagoNotaCredito) getValueAt(row, COL_OBJ_NOTA_CREDITO);
							formaPagoNotaCredito.setImporteNC(new BigDecimal(importe));
							refrescarModelo();
						} else {
							FWJOptionPane.showErrorMessage(JDialogCargaOrdenDePago.this, "Solo puede ingresar números", "Error");
							setEstadoPantalla(EEstadoPantalla.ESTADO_INVALIDO);
						}
					}
				}
			};
			tablaNC.setDateColumn(COL_FECHA_NOTA_CREDITO, "Fecha", 90, true);
			tablaNC.setStringColumn(COL_DOCUMENTO_NOTA_CREDITO, "Nota de crédito", 250, 250, true);
			tablaNC.setFloatColumn(COL_IMPORTE_NOTA_CREDITO, "Importe", 90, false);
			tablaNC.setStringColumn(COL_OBJ_NOTA_CREDITO, "", 0);
			tablaNC.setHeaderAlignment(COL_FECHA_NOTA_CREDITO, FWJTable.CENTER_ALIGN);
			tablaNC.setHeaderAlignment(COL_DOCUMENTO_NOTA_CREDITO, FWJTable.CENTER_ALIGN);
			tablaNC.setHeaderAlignment(COL_IMPORTE_NOTA_CREDITO, FWJTable.CENTER_ALIGN);
			tablaNC.setReorderingAllowed(false);
			tablaNC.setAllowSorting(false);
			tablaNC.setAllowHidingColumns(false);
			return tablaNC;
		}

		@Override
		protected FormaPagoOrdenDePagoNotaCredito getElemento(int fila) {
			return (FormaPagoOrdenDePagoNotaCredito) getTabla().getValueAt(fila, COL_OBJ_NOTA_CREDITO);
		}

		@Override
		protected String validarElemento(int fila) {
			return null;
		}

		@Override
		protected void agregarElemento(FormaPagoOrdenDePagoNotaCredito elemento) {
			Object[] row = new Object[CANT_COLS_NOTA_CREDITO];
			row[COL_FECHA_NOTA_CREDITO] = DateUtil.dateToString(getPanelFecha().getDate());
			row[COL_DOCUMENTO_NOTA_CREDITO] = "Nota de crédito Nº " + elemento.getNotaCredito().getNroCorreccion();
			row[COL_IMPORTE_NOTA_CREDITO] = GenericUtils.getDecimalFormatTablaMovimientos().format(elemento.getImporteNC());
			row[COL_OBJ_NOTA_CREDITO] = elemento;
			getPanelTablaNotasDeCredito().getTabla().addRow(row); 
		}

		@Override
		public boolean validarAgregar() {
			JDialogBuscarNotasDeCreditoNoUtilizadas dialog = new JDialogBuscarNotasDeCreditoNoUtilizadas(JDialogCargaOrdenDePago.this, getProveedor(), modelo.getIdNotasCreditoUtilizadas());
			dialog.setVisible(true);
			if(dialog.isAcepto()){
				for(NotaCreditoProveedor nc : dialog.getNotasCreditoSeleccionadas()){
					FormaPagoOrdenDePagoNotaCredito fp = new FormaPagoOrdenDePagoNotaCredito();
					fp.setImporteNC(nc.getMontoSobrantePorUtilizar());
					fp.setNotaCredito(nc);
					fp.setFechaEmision(new Timestamp(getPanelFecha().getDate().getTime()));
					modelo.getFormasDePago().add(fp);
					modelo.getIdNotasCreditoUtilizadas().add(nc.getId());
				}
				actualizarVista();
				refrescarModelo();
			}
			return false;
		}

		@Override
		public boolean validarQuitar() {
			FWJTable tabla = getPanelTablaNotasDeCredito().getTabla();
			FormaPagoOrdenDePagoNotaCredito fp = (FormaPagoOrdenDePagoNotaCredito)tabla.getValueAt(tabla.getSelectedRow(), COL_OBJ_NOTA_CREDITO);
			tabla.removeRow(tabla.getSelectedRow());
			modelo.getFormasDePago().remove(fp);
			modelo.getIdNotasCreditoUtilizadas().remove(fp.getNotaCredito().getId());
			refrescarModelo();
			return false;
		}
	}
		

	private FWDobleLista getDobleListaCheques() {
		if (dobleListaCheques == null) {
			dobleListaCheques = new FWDobleLista("Cheques seleccionados(Numeración - Banco - Número - Fecha - Importe - Cap o Int)", "Cheques descartados", FWDobleLista.VERTICAL_LAYOUT) {

				private static final long serialVersionUID = -3442173748412249528L;

				@Override
				public void agregarItemDestino(Object item) {
					super.agregarItemDestino(item);
					if(!iniciando){
						modelo.getChequesUtilizados().remove(item);
						modelo.getChequesDescartados().add((Cheque) item);
						refrescarModelo();
					}
				}

				@Override
				public void agregarItemOriginal(Object item) {
					super.agregarItemOriginal(item);
					if(!iniciando){
						modelo.getChequesDescartados().remove(item);
						modelo.getChequesUtilizados().add((Cheque) item);
						refrescarModelo();
					}
				}
			};
			dobleListaCheques.getListaOrigen().getParent().setPreferredSize(new Dimension(410, 150));
		}
		return dobleListaCheques;
	}

	private PanelDatePicker getPanelFecha() {
		if (panelFecha == null) {
			panelFecha = new PanelDatePicker();
		}
		return panelFecha;
	}

	private FWJTextField getTxtNroOrden() {
		if (txtNroOrden == null) {
			txtNroOrden = new FWJTextField();
			txtNroOrden.setPreferredSize(new Dimension(80, 20));
			txtNroOrden.setEditable(false);
		}
		return txtNroOrden;
	}

	private FWJTextField getTxtRetencionesIngresosBrutos() {
		if (txtRetencionesIngresosBrutos == null) {
			txtRetencionesIngresosBrutos = new FWJTextField();
			txtRetencionesIngresosBrutos.setPreferredSize(new Dimension(100, 20));
			txtRetencionesIngresosBrutos.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					setEstadoPantalla(EEstadoPantalla.NO_VERIFICADO);
				}
			});
		}
		return txtRetencionesIngresosBrutos;
	}

	private FWJTextField getTxtEfectivo() {
		if (txtEfectivo == null) {
			txtEfectivo = new FWJTextField();
			txtEfectivo.setPreferredSize(new Dimension(100, 20));
			txtEfectivo.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					setEstadoPantalla(EEstadoPantalla.NO_VERIFICADO);
				}
			});
		}
		return txtEfectivo;
	}

	private FWJTextField getTxtTotal() {
		if (txtTotal == null) {
			txtTotal = new FWJTextField();
			txtTotal.setPreferredSize(new Dimension(100, 20));
			txtTotal.setEditable(false);
		}
		return txtTotal;
	}

	private FWJTextField getTxtTotalLetras() {
		if (txtTotalLetras == null) {
			txtTotalLetras = new FWJTextField();
			txtTotalLetras.setPreferredSize(new Dimension(100, 20));
		}
		return txtTotalLetras;
	}

	private FWJTextField getTxtRazonSocial() {
		if (txtRazonSocial == null) {
			txtRazonSocial = new FWJTextField();
			txtRazonSocial.setPreferredSize(new Dimension(270, 20));
			txtRazonSocial.setEditable(false);
		}
		return txtRazonSocial;
	}

	private FWJTextField getTxtCuit() {
		if (txtCuit == null) {
			txtCuit = new FWJTextField();
			txtCuit.setPreferredSize(new Dimension(120, 20));
			txtCuit.setEditable(false);
		}
		return txtCuit;
	}

	private FWJTextField getTxtDireccion() {
		if (txtDireccion == null) {
			txtDireccion = new FWJTextField();
			txtDireccion.setPreferredSize(new Dimension(340, 20));
			txtDireccion.setEditable(false);
		}
		return txtDireccion;
	}

	private JButton getBtnSugerirCheques() {
		if (btnSugerirCheques == null) {
			btnSugerirCheques = new JButton("Sugerir cheques");
			btnSugerirCheques.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					sugerirCheques();
				}
			});
		}
		return btnSugerirCheques;
	}

	private void sugerirCheques(){
		if(modelo.getPagos().isEmpty()){
			FWJOptionPane.showErrorMessage(this, "Debe elegir las facutas a pagar", "Error");
			return;
		}
		BigDecimal montoHasta = new BigDecimal(modelo.getTotalFacturas());
		Integer diasDesde = null;
		Integer diasHasta = null;
		if(getProveedor().getCondicionVenta() instanceof CondicionDeVentaDiferida){
			diasDesde = ((CondicionDeVentaDiferida)getProveedor().getCondicionVenta()).getDiasIniciales();
			diasHasta = ((CondicionDeVentaDiferida)getProveedor().getCondicionVenta()).getDiasFinales();
		}else{
			diasHasta = 30;
		}
		Date fechaPromedio = modelo.getFechaPromedio();
		List<Cheque> chequesSugeridos = getChequeFacade().getChequeSugeridos(montoHasta, diasDesde, diasHasta, fechaPromedio, modelo.getChequesDescartados());
		if(chequesSugeridos!=null && !chequesSugeridos.isEmpty()){
			for(Cheque cheque : chequesSugeridos) {
				if(!modelo.getChequesUtilizados().contains(cheque)) {
					modelo.getChequesUtilizados().add(cheque);
				}
			}
			refrescarModelo();
		}else{
			FWJOptionPane.showWarningMessage(this, "No hay cheques para sugerir", "Advertencia");
		}
	}
	
	private JButton getBtnLimpiar() {
		if (btnLimpiar == null) {
			btnLimpiar = new JButton("Limpiar");
			btnLimpiar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					modelo.getChequesUtilizados().clear();
					modelo.getChequesDescartados().clear();
					modelo.getIdFacturasUtilizadas().clear();
					modelo.getPagosFijos().clear();
					modelo.getPagos().clear();
					resetTextBoxes();
					refrescarModelo();
				}
			});
		}
		return btnLimpiar;
	}

	private void resetTextBoxes() {
		getTxtEfectivo().setText("");
		getTxtRetencionesGanancias().setText("");
		getTxtRetencionesIngresosBrutos().setText("");
		getTxtRetencionesIva().setText("");
		getTxtObservacionesTransf().setText("");
		getTxtNroTransf().setText("");
		getTxtImporteTransf().setText("");
	}

	private JButton getBtnImprimir() {
		if (btnImprimir == null) {
			btnImprimir = new JButton("Imprimir");
			btnImprimir.setMnemonic(KeyEvent.VK_I);
			btnImprimir.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					imprimirOrden();
				}
			});
		}
		return btnImprimir;
	}
	
	private void imprimirOrden(){
		ImprimirOrdenDePagoHandler impresionHandler = new ImprimirOrdenDePagoHandler(getOrdenFinal(), JDialogCargaOrdenDePago.this);
		impresionHandler.imprimir();
	}

	private JButton getBtnGuardar() {
		if (btnGuardar == null) {
			btnGuardar = new JButton("Guardar");
			btnGuardar.setMnemonic(KeyEvent.VK_G);
			btnGuardar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if (getEstadoPantalla() == EEstadoPantalla.VERIFICADO) {
						if(!modelo.getPagos().isEmpty() && !modelo.getFormasDePago().isEmpty()){
							if (Math.round(modelo.getTotalFacturas().doubleValue()) == Math.round(modelo.getTotalGeneral().doubleValue())) {
								try{
									getBtnGuardar().setEnabled(false);
									OrdenDePago orden = isEdicion()?getOrdenFinal(): new OrdenDePago();
									orden.setFormasDePago(modelo.getFormasDePago());
									orden.setMonto(new BigDecimal(modelo.getTotalGeneral()-modelo.getTotalNotasCredito()));
									orden.setPagos(modelo.getPagos());
									orden.setProveedor(getProveedor());
									orden.setTxtCantidadPesos(GenericUtils.convertirNumeroATexto(modelo.getTotalGeneral()));
									orden.setFechaEmision(new Timestamp(getPanelFecha().getDate().getTime()));
									String usuario = GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName();
									orden.setUsuarioConfirmacion(usuario);
									orden.setNroOrden(Integer.valueOf(getTxtNroOrden().getText()));
									setOrdenFinal(isEdicion()?getOrdenDePagoFacade().editarOrdenDePago(orden, usuario):getOrdenDePagoFacade().guardarOrdenDePago(orden, usuario));
									FWJOptionPane.showInformationMessage(JDialogCargaOrdenDePago.this, "Se ha guardado correctamente la órden de pago", "Información");
									if(FWJOptionPane.showQuestionMessage(JDialogCargaOrdenDePago.this, "¿Desea imprimir la Orden?", "Confirmación") == FWJOptionPane.YES_OPTION) {
										imprimirOrden();
									}
									dispose();
								}catch(FWException cle){
									BossError.gestionarError(cle);
								}
							} else {
								FWJOptionPane.showErrorMessage(JDialogCargaOrdenDePago.this, "El monto generado por los cheques, retenciones y efectivo, debe ser igual al total de las facturas.", "Error");
							}
						}else if(modelo.getPagos().isEmpty()){
							FWJOptionPane.showErrorMessage(JDialogCargaOrdenDePago.this, "Debe elegir las facturas que desea pagar antes de guardar", "Error");
							return;
						}else if(modelo.getFormasDePago().isEmpty()){
							FWJOptionPane.showErrorMessage(JDialogCargaOrdenDePago.this, "Debe indicar las formas de pago antes de guardar", "Error");
							return;
						}
					} else {
						FWJOptionPane.showErrorMessage(JDialogCargaOrdenDePago.this, "Debe presionar 'Verificar' antes de guardar", "Error");
						return;
					}
				}
			});
		}
		return btnGuardar;
	}

	private JButton getBtnBuscarCheques() {
		if (btnBuscarCheques == null) {
			btnBuscarCheques = new JButton("Buscar cheques");
			btnBuscarCheques.addActionListener(new ActionListener() {

				@SuppressWarnings("unchecked")
				public void actionPerformed(ActionEvent e) {
					JDialogBuscarAgregarCheques dialog = new JDialogBuscarAgregarCheques(JDialogCargaOrdenDePago.this, getProveedor().getCondicionVenta(),(List<Cheque>)CollectionUtils.union(modelo.getChequesUtilizados(),modelo.getChequesDescartados()));
					dialog.setVisible(true);
					if (dialog.isAcepto()) {
						for (Cheque c : dialog.getChequesSeleccionados()) {
							modelo.getChequesUtilizados().add(c);
						}
						refrescarModelo();
					}
				}
			});
		}
		return btnBuscarCheques;
	}

	private JButton getBtnAgregarChequeTerceros() {
		if (btnAgregarChequeTerceros == null) {
			btnAgregarChequeTerceros = new JButton("Agregar cheque propio");
			btnAgregarChequeTerceros.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					Cliente cliente01 = GTLBeanFactory.getInstance().getBean2(ClienteFacadeRemote.class).getClienteByNumero(1);
					JDialogAgregarCheque dialogAgregarCheque = new JDialogAgregarCheque(null,cliente01);
					boolean acepto = dialogAgregarCheque.isAcepto();
					if (acepto) {
						ChequeFacadeRemote cfr = GTLBeanFactory.getInstance().getBean2(ChequeFacadeRemote.class);
						try {
							Cheque cheque = dialogAgregarCheque.getCheque();
							cheque = cfr.grabarCheque(cheque, GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName());
							modelo.getChequesUtilizados().add(cheque);
							boolean agregaOtro = dialogAgregarCheque.isAgregaOtro();
							if (agregaOtro) {
								do {
									dialogAgregarCheque = new JDialogAgregarCheque(null, cloneCheque(cheque), false, true,cliente01);
									cheque = dialogAgregarCheque.getCheque();
									acepto = dialogAgregarCheque.isAcepto();
									agregaOtro = dialogAgregarCheque.isAgregaOtro();
									if (acepto) {
										cheque = cfr.grabarCheque(cheque, GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName());
										modelo.getChequesUtilizados().add(cheque);
										FWJOptionPane.showInformationMessage(JDialogCargaOrdenDePago.this, "El cheque se ha guardado con éxito", "Alta de cheque");
									}
								} while (agregaOtro && acepto);
							} else {
								FWJOptionPane.showInformationMessage(JDialogCargaOrdenDePago.this, "El cheque se ha guardado con éxito", "Alta de cheque");
							}
							refrescarModelo();
						} catch (FWException cle) {
							BossError.gestionarError(cle);
						}
					}
				}
			});
		}
		return btnAgregarChequeTerceros;
	}

	private Cheque cloneCheque(Cheque cheque) {
		Cheque newCheque = new Cheque();
		newCheque.setBanco(cheque.getBanco());
		newCheque.setBancoSalida(cheque.getBancoSalida());
		newCheque.setCapitalOInterior(cheque.getCapitalOInterior());
		newCheque.setCliente(cheque.getCliente());
		newCheque.setClienteSalida(cheque.getClienteSalida());
		newCheque.setCuit(cheque.getCuit());
		newCheque.setEstadoCheque(cheque.getEstadoCheque());
		newCheque.setFechaDeposito(cheque.getFechaDeposito());
		newCheque.setFechaEntrada(cheque.getFechaEntrada());
		newCheque.setFechaSalida(cheque.getFechaSalida());
		newCheque.setImporte(cheque.getImporte());
		newCheque.setNombreProveedorSalida(cheque.getNombreProveedorSalida());
		newCheque.setNumeracion(cheque.getNumeracion());
		newCheque.setNumero(cheque.getNumero());
		newCheque.setPersonaSalida(cheque.getPersonaSalida());
		newCheque.setProveedorSalida(cheque.getProveedorSalida());
		return newCheque;
	}
	
	private void refrescarModelo() {
		resetModelo();
		if(!calcularYValidarFormasDePago()){
			return;
		}
		this.modelo.getPagos().addAll(this.modelo.getPagosFijos());
		if(modelo.getTotalGeneral()>0){
			ActualizarDescripcionPagoOrdenDePagoVisitor actualizador = new ActualizarDescripcionPagoOrdenDePagoVisitor();
			Collections.sort(modelo.getPagos(), new PagoOrdenDePagoFechaComparator());
			int index = 0;
			for(PagoOrdenDePago p : modelo.getPagos()){
				if(!actualizador.isFreno()){
					p.accept(actualizador);
					modelo.setTotalFacturas(modelo.getTotalFacturas() + p.getMontoPagado().doubleValue());
					index++;
				}else{
					break;
				}
			}
			
			PagoOrdenDePagoACuenta pagoACuenta = buscarPagoACuenta();
			Double suma = actualizador.getSuma();
			if(suma>0){ //sobro plata
				BigDecimal tope = new BigDecimal(suma);
				List<FacturaProveedor> facturas = getFacturaProveedorFacade().getFacturasImpagas(getProveedor().getId(), modelo.getIdFacturasUtilizadas(),tope);
				if(facturas.isEmpty()){
					if(pagoACuenta ==null){
						pagoACuenta = new PagoOrdenDePagoACuenta();
						modelo.getPagos().add(pagoACuenta);
					}
					pagoACuenta.setMontoPagado(new BigDecimal(suma));
				}else{
					for (FacturaProveedor f : facturas) {
						PagoOrdenDePagoFactura popf = new PagoOrdenDePagoFactura();
						popf.setFactura(f);
						
						if(suma.doubleValue() >= f.getMontoFaltantePorPagar().doubleValue()){
							suma -= f.getMontoFaltantePorPagar().doubleValue();
							popf.setMontoPagado(f.getMontoFaltantePorPagar());
							if(f.getMontoFaltantePorPagar().equals(f.getMontoTotal())){
								popf.setDescrPagoFactura(EDescripcionPagoFactura.FACTURA);
							}else{
								popf.setDescrPagoFactura(EDescripcionPagoFactura.SALDO);
							}
						}else{
							popf.setMontoPagado(new BigDecimal(suma));
							popf.setDescrPagoFactura(EDescripcionPagoFactura.A_CUENTA);
							suma = 0d;
						}
						
						modelo.getPagos().add(popf);
						modelo.getIdFacturasUtilizadas().add(f.getId());
					}
					if(suma>0){
						if(pagoACuenta ==null){
							pagoACuenta = new PagoOrdenDePagoACuenta();
							modelo.getPagos().add(pagoACuenta);
						}
						pagoACuenta.setMontoPagado(new BigDecimal(suma));
					}
				}
			}else{
				if(pagoACuenta!=null){
					modelo.setTotalFacturas(modelo.getTotalFacturas() - pagoACuenta.getMontoPagado().doubleValue());
					modelo.getPagos().remove(pagoACuenta);
				}
				for(int i = index; i<modelo.getPagos().size();i++){
					if(modelo.getPagos().get(i) instanceof PagoOrdenDePagoFactura){
						FacturaProveedor f = ((PagoOrdenDePagoFactura)modelo.getPagos().get(i)).getFactura();
						modelo.getIdFacturasUtilizadas().remove(f.getId());
					}
					if(!modelo.getPagosFijos().contains(modelo.getPagos().get(i))){
						modelo.getPagos().remove(i);
					}
				}
			}
			
			CalculadorFechaPromedioVisitor calculador = new CalculadorFechaPromedioVisitor();
			Collections.sort(modelo.getPagos(), new PagoOrdenDePagoFechaComparator());
			modelo.setDivisorFechaPromeido(0);
			modelo.setFechaPromedio(null);
			modelo.setTotalFacturas(0d);
			for (PagoOrdenDePago p : modelo.getPagos()) {
				p.accept(calculador);
				modelo.setTotalFacturas(modelo.getTotalFacturas() + p.getMontoPagado().doubleValue());
			}
		}else if(modelo.getPagos()!=null && !modelo.getPagos().isEmpty()){
			CalculadorFechaPromedioVisitor calculador = new CalculadorFechaPromedioVisitor();
			ResetPagoOrdenDePagoVisitor reseteador = new ResetPagoOrdenDePagoVisitor();
			Collections.sort(modelo.getPagos(), new PagoOrdenDePagoFechaComparator());
			modelo.setDivisorFechaPromeido(0);
			modelo.setFechaPromedio(null);
			modelo.setTotalFacturas(0d);
			for (PagoOrdenDePago p : modelo.getPagos()) {
				p.accept(calculador);
				p.accept(reseteador);
				modelo.setTotalFacturas(modelo.getTotalFacturas() + p.getMontoPagado().doubleValue());
			}
		}
		setEstadoPantalla(EEstadoPantalla.ESTADO_VALIDO);
		actualizarVista();
	}

	private class ResetPagoOrdenDePagoVisitor implements IPagoOrdenPagoVisitor{

		public void visit(PagoOrdenDePagoACuenta popac) {

		}

		public void visit(PagoOrdenDePagoFactura popf) {
			popf.setMontoPagado(popf.getFactura().getMontoFaltantePorPagar());
			popf.setDescrPagoFactura(EDescripcionPagoFactura.FACTURA);
		}

		public void visit(PagoOrdenDePagoNotaDebito popnd) {
			popnd.setMontoPagado(popnd.getNotaDebito().getMontoFaltantePorPagar());
			popnd.setDescrPagoFactura(EDescripcionPagoFactura.FACTURA);
		}
	}
	
	private void resetModelo() {
		modelo.setTotalCheques(0d);
		modelo.setTotalGeneral(0d);
		modelo.setTotalEfectivo(0d);
		modelo.setTotalRetenciones(0d);
		modelo.setTotalFacturas(0d);
		modelo.getFormasDePago().clear();
		modelo.getIdFacturasUtilizadas().clear();
		modelo.getIdNotasDebitoUtilizadas().clear();
		modelo.setTotalNotasCredito(0d);
		modelo.setTotalTransferencias(0d);
		modelo.getIdFacturasUtilizadas().addAll(modelo.getIdsFacturasQuitadas());
		modelo.getIdNotasDebitoUtilizadas().addAll(modelo.getIdsNotasDebitoQuitadas());
		if(!isEdicion()){
			modelo.getPagos().clear();
		}
		for(PagoOrdenDePago p : modelo.getPagosFijos()){
			if(p instanceof PagoOrdenDePagoNotaDebito){
				modelo.getIdNotasDebitoUtilizadas().add(((PagoOrdenDePagoNotaDebito)p).getNotaDebito().getId());
			}else if(p instanceof PagoOrdenDePagoFactura){
				modelo.getIdFacturasUtilizadas().add(((PagoOrdenDePagoFactura)p).getFactura().getId());
			}
			p.setMontoPagado(new BigDecimal(0d));
		}
	}

	private boolean calcularYValidarFormasDePago() {
		actualizarCheques();
		if(!calcularYValidarRetencionesIngresosBrutos() || !calcularYValidarRetencionesIVA() ||
		   !calcularYValidarRetencionesGanancias() || !calcularYValidarEfectivo() || 
		   !calcularYValidarNotasDeCredito() || !calcularYValidarTransferencia()){
			return false;
		}
		modelo.updateTotalGeneral();
		return true;
	}

	private boolean calcularYValidarNotasDeCredito() {
		FWJTable tabla = getPanelTablaNotasDeCredito().getTabla();
		Double suma = 0d;
		for(int i = 0; i < tabla.getRowCount();i++){
			NotaCreditoProveedor nc = ((FormaPagoOrdenDePagoNotaCredito)tabla.getValueAt(i, COL_OBJ_NOTA_CREDITO)).getNotaCredito();
			String strValor = ((String)tabla.getValueAt(i, COL_IMPORTE_NOTA_CREDITO)).replace(".", "");
			if(GenericUtils.esNumerico(strValor)){
				suma += Double.valueOf(strValor.replace(',', '.'));
				FormaPagoOrdenDePagoNotaCredito fpopnc = new FormaPagoOrdenDePagoNotaCredito();
				fpopnc.setNotaCredito(nc);
				fpopnc.setImporteNC(new BigDecimal(Double.valueOf(strValor.replace(',', '.'))));
				fpopnc.setFechaEmision(new Timestamp(getPanelFecha().getDate().getTime()));
				modelo.getFormasDePago().add(fpopnc);
			}else{
				FWJOptionPane.showErrorMessage(this, StringW.wordWrap("El valor de la columna importe en la fila " + (i+1)+" de la tabla de notas de crédito no es numérico"), "Error");
				setEstadoPantalla(EEstadoPantalla.ESTADO_INVALIDO);
				return false;
			}
		}
		modelo.setTotalNotasCredito(suma);
		return true;
	}

	private boolean calcularYValidarTransferencia() {
		if(getTxtNroTransf().getText().trim().length()>0 && getTxtImporteTransf().getText().trim().length() > 0){
			if (GenericUtils.esNumerico(getTxtNroTransf().getText().trim()) && GenericUtils.esNumerico(getTxtImporteTransf().getText().trim())) {
				modelo.setTotalTransferencias(Double.valueOf(getTxtImporteTransf().getText().trim().replace(",", ".")));
				FormaPagoOrdenDePagoTransferencia fpodpt = new FormaPagoOrdenDePagoTransferencia();
				fpodpt.setImportePagoSimple(new BigDecimal(modelo.getTotalTransferencias()));
				fpodpt.setNroTx(Integer.valueOf(getTxtNroTransf().getText().trim()));
				if(getTxtObservacionesTransf().getText().trim().length()>0){
					fpodpt.setObservaciones(getTxtObservacionesTransf().getText().trim().toUpperCase());
				}
				modelo.getFormasDePago().add(fpodpt);
			}else if(GenericUtils.esNumerico(getTxtImporteTransf().getText().trim())){
				FWJOptionPane.showErrorMessage(this, "El campo es numérico", "Error");
				getTxtNroTransf().requestFocus();
				setEstadoPantalla(EEstadoPantalla.ESTADO_INVALIDO);
				return false;
			}else{
				FWJOptionPane.showErrorMessage(this, "El campo es numérico", "Error");
				getTxtImporteTransf().requestFocus();
				setEstadoPantalla(EEstadoPantalla.ESTADO_INVALIDO);
				return false;
			}
		}else if(getTxtNroTransf().getText().trim().length()>0){
			FWJOptionPane.showErrorMessage(this, "Para ingresar una transferencia, debe ingresar el importe.", "Error");
			getTxtImporteTransf().requestFocus();
			setEstadoPantalla(EEstadoPantalla.ESTADO_INVALIDO);
			return false;
		}else if(getTxtImporteTransf().getText().trim().length() > 0){
			FWJOptionPane.showErrorMessage(this, "Para ingresar una transferencia, debe ingresar el número de transferencia.", "Error");
			getTxtNroTransf().requestFocus();
			setEstadoPantalla(EEstadoPantalla.ESTADO_INVALIDO);
			return false;
		}
		return true;
	}
	
	private boolean calcularYValidarEfectivo() {
		if (getTxtEfectivo().getText().trim().length() > 0) {
			if (GenericUtils.esNumerico(getTxtEfectivo().getText().trim())) {
				modelo.setTotalEfectivo(Double.valueOf(getTxtEfectivo().getText().trim().replace(",", ".")));
				FormaPagoOrdenDePagoEfectivo fpope = new FormaPagoOrdenDePagoEfectivo();
				fpope.setImportePagoSimple(new BigDecimal(modelo.getTotalEfectivo()));
				modelo.getFormasDePago().add(fpope);
			} else {
				FWJOptionPane.showErrorMessage(this, "El campo es numérico", "Error");
				getTxtEfectivo().requestFocus();
				setEstadoPantalla(EEstadoPantalla.ESTADO_INVALIDO);
				return false;
			}
		}
		return true;
	}

	private boolean calcularYValidarRetencionesGanancias() {
		if (getTxtRetencionesGanancias().getText().trim().length() > 0) {
			if (GenericUtils.esNumerico(getTxtRetencionesGanancias().getText().trim())) {
				modelo.setTotalRetencionesGanancias(Double.valueOf(getTxtRetencionesGanancias().getText().trim().replace(",", ".")));
				FormaPagoOrdenDePagoRetencionesGanancias fpoprg = new FormaPagoOrdenDePagoRetencionesGanancias();
				fpoprg.setImportePagoSimple(new BigDecimal(modelo.getTotalRetencionesGanancias()));
				modelo.getFormasDePago().add(fpoprg);
			} else {
				FWJOptionPane.showErrorMessage(this, "El campo es numérico", "Error");
				getTxtRetencionesGanancias().requestFocus();
				setEstadoPantalla(EEstadoPantalla.ESTADO_INVALIDO);
				return false;
			}
		}
		return true;
	}

	private boolean calcularYValidarRetencionesIVA() {
		if (getTxtRetencionesIva().getText().trim().length() > 0) {
			if (GenericUtils.esNumerico(getTxtRetencionesIva().getText().trim())) {
				modelo.setTotalRetencionesIva(Double.valueOf(getTxtRetencionesIva().getText().trim().replace(",", ".")));
				FormaPagoOrdenDePagoRetencionIVA fpopri = new FormaPagoOrdenDePagoRetencionIVA();
				fpopri.setImportePagoSimple(new BigDecimal(modelo.getTotalRetencionesIva()));
				modelo.getFormasDePago().add(fpopri);
			} else {
				FWJOptionPane.showErrorMessage(this, "El campo es numérico", "Error");
				getTxtRetencionesIva().requestFocus();
				setEstadoPantalla(EEstadoPantalla.ESTADO_INVALIDO);
				return false;
			}
		}
		return true;
	}

	private boolean calcularYValidarRetencionesIngresosBrutos() {
		if (getTxtRetencionesIngresosBrutos().getText().trim().length() > 0) {
			if (GenericUtils.esNumerico(getTxtRetencionesIngresosBrutos().getText().trim())) {
				modelo.setTotalRetencionesIngresosBrutos(Double.valueOf(getTxtRetencionesIngresosBrutos().getText().trim().replace(",", ".")));
				FormaPagoOrdenDePagoRetencionIngresosBrutos fpoprib = new FormaPagoOrdenDePagoRetencionIngresosBrutos();
				fpoprib.setImportePagoSimple(new BigDecimal(modelo.getTotalRetencionesIngresosBrutos()));
				modelo.getFormasDePago().add(fpoprib);
			} else {
				FWJOptionPane.showErrorMessage(this, "El campo es numérico", "Error");
				getTxtRetencionesIngresosBrutos().requestFocus();
				setEstadoPantalla(EEstadoPantalla.ESTADO_INVALIDO);
				return false;
			}
		}
		return true;
	}

	private void actualizarCheques() {
		((DefaultListModel) getDobleListaCheques().getListaOrigen().getModel()).clear();
		List<Cheque> chequesUtilizados = modelo.getChequesUtilizados();
		Collections.sort(chequesUtilizados, new Comparator<Cheque>() {
			public int compare(Cheque o1, Cheque o2) {
				return o1.getNumeracion().getNumero().compareTo(o2.getNumeracion().getNumero());
			}
		});
		for (Cheque c : chequesUtilizados) {
			((DefaultListModel) getDobleListaCheques().getListaOrigen().getModel()).addElement(c);
			modelo.setTotalCheques(modelo.getTotalCheques() + c.getImporte().doubleValue());

			FormaPagoOrdenDePagoCheque fpopc = new FormaPagoOrdenDePagoCheque();
			fpopc.setCheque(c);
			modelo.getFormasDePago().add(fpopc);
		}

		((DefaultListModel) getDobleListaCheques().getListaDestino().getModel()).clear();
		for (Cheque c : modelo.getChequesDescartados()) {
			((DefaultListModel) getDobleListaCheques().getListaDestino().getModel()).addElement(c);
		}
	}
	
	private class ActualizarDescripcionPagoOrdenDePagoVisitor implements IPagoOrdenPagoVisitor{

		private Double suma;
		private boolean freno;
		
		public ActualizarDescripcionPagoOrdenDePagoVisitor(){
			suma = modelo.getTotalGeneral();
			setFreno(false);
		}
		
		public void visit(PagoOrdenDePagoACuenta popac) {
			
		}

		public void visit(PagoOrdenDePagoFactura popf) {
			//Double total = modelo.getTotalGeneral();
			FacturaProveedor factura = popf.getFactura();
			if(suma>=factura.getMontoFaltantePorPagar().doubleValue()){
				if(factura.getMontoFaltantePorPagar().equals(factura.getMontoTotal())){
					popf.setDescrPagoFactura(EDescripcionPagoFactura.FACTURA);
					suma = suma-factura.getMontoTotal().doubleValue();
					popf.setMontoPagado(factura.getMontoTotal());
				}else{
					popf.setDescrPagoFactura(EDescripcionPagoFactura.SALDO);	
					suma = suma - factura.getMontoFaltantePorPagar().doubleValue();
					popf.setMontoPagado(factura.getMontoFaltantePorPagar());
				}
			}else if(suma > 0){
				popf.setDescrPagoFactura(EDescripcionPagoFactura.A_CUENTA);
				popf.setMontoPagado(new BigDecimal(suma));
				suma = 0d;
				setFreno(true);
			}
		}

		public void visit(PagoOrdenDePagoNotaDebito popnd) {
			NotaDebitoProveedor factura = popnd.getNotaDebito();
			if(suma>=factura.getMontoFaltantePorPagar().doubleValue()){
				if(factura.getMontoFaltantePorPagar().equals(factura.getMontoTotal())){
					popnd.setDescrPagoFactura(EDescripcionPagoFactura.FACTURA);
					suma = suma-factura.getMontoTotal().doubleValue();
					popnd.setMontoPagado(factura.getMontoTotal());
				}else{
					popnd.setDescrPagoFactura(EDescripcionPagoFactura.SALDO);	
					suma = suma - factura.getMontoFaltantePorPagar().doubleValue();
					popnd.setMontoPagado(factura.getMontoFaltantePorPagar());
				}
			}else if(suma > 0){
				popnd.setDescrPagoFactura(EDescripcionPagoFactura.A_CUENTA);
				popnd.setMontoPagado(new BigDecimal(suma));
				suma = 0d;
				setFreno(true);
			}
		}
		
		public boolean isFreno() {
			return freno;
		}

		
		public void setFreno(boolean freno) {
			this.freno = freno;
		}

		public Double getSuma() {
			return suma;
		}
		
	}
	
	private PagoOrdenDePagoACuenta buscarPagoACuenta() {
		for(PagoOrdenDePago p : modelo.getPagos()){
			if(p instanceof PagoOrdenDePagoACuenta){
				return (PagoOrdenDePagoACuenta)p;
			}
		}
		return null;
	}

	private void actualizarVista() {
		getTxtTotal().setText(String.valueOf(modelo.getTotalGeneral()));
		getTxtTotalCheques().setText(String.valueOf(modelo.getTotalCheques()));
		getTxtTotalLetras().setText(GenericUtils.convertirNumeroATexto(modelo.getTotalGeneral()));
		getTxtTotalFacturas().setText(GenericUtils.fixPrecioCero(GenericUtils.getDecimalFormatTablaMovimientos().format(modelo.getTotalFacturas())));
		getTxtTotalNotasDeCredito().setText(GenericUtils.getDecimalFormatTablaMovimientos().format(modelo.getTotalNotasCredito()));
		getPanelTablaFacturas().getTabla().removeAllRows();
		for (PagoOrdenDePago p : modelo.getPagos()) {
			getPanelTablaFacturas().agregarElemento(p);
		}
		getPanelTablaNotasDeCredito().getTabla().removeAllRows();
		for(FormaPagoOrdenDePago fp : modelo.getFormasDePago()){
			if(fp instanceof FormaPagoOrdenDePagoNotaCredito){
				getPanelTablaNotasDeCredito().agregarElemento((FormaPagoOrdenDePagoNotaCredito)fp);
			}
		}
	}

	private class CalculadorFechaPromedioVisitor implements IPagoOrdenPagoVisitor{

		public void visit(PagoOrdenDePagoACuenta popac) {
			
		}

		public void visit(PagoOrdenDePagoFactura popf) {
			if(modelo.getFechaPromedio()==null){
				modelo.setFechaPromedio(popf.getFactura().getFechaIngreso());
				modelo.setDivisorFechaPromeido(1);
			}else{
				int cantidad = modelo.getDivisorFechaPromeido()+1;
				modelo.setDivisorFechaPromeido(cantidad);
				long sumaLongs = modelo.getFechaPromedio().getTime() + popf.getFactura().getFechaIngreso().getTime();
				modelo.setFechaPromedio(new Date(sumaLongs/cantidad));
			}
		}

		public void visit(PagoOrdenDePagoNotaDebito popnd) {
			if(modelo.getFechaPromedio()==null){
				modelo.setFechaPromedio(popnd.getNotaDebito().getFechaIngreso());
				modelo.setDivisorFechaPromeido(1);
			}else{
				int cantidad = modelo.getDivisorFechaPromeido()+1;
				modelo.setDivisorFechaPromeido(cantidad);
				long sumaLongs = modelo.getFechaPromedio().getTime() + popnd.getNotaDebito().getFechaIngreso().getTime();
				modelo.setFechaPromedio(new Date(sumaLongs/cantidad));
			}
		}
	}
	
	private void salir() {
		if(isConsulta()){
			dispose();
		}else if (FWJOptionPane.showQuestionMessage(this, "Seguro que desea salir?", "Pregunta") == FWJOptionPane.YES_OPTION) {
			dispose();
		}
	}

	private FWJTextField getTxtTotalFacturas() {
		if (txtTotalFacturas == null) {
			txtTotalFacturas = new FWJTextField();
			txtTotalFacturas.setPreferredSize(new Dimension(120, 20));
			txtTotalFacturas.setEditable(false);
		}
		return txtTotalFacturas;
	}

	private FWJTextField getTxtCondicionDeVenta() {
		if (txtCondicionDeVenta == null) {
			txtCondicionDeVenta = new FWJTextField();
			txtCondicionDeVenta.setPreferredSize(new Dimension(150, 20));
			txtCondicionDeVenta.setEditable(false);
		}
		return txtCondicionDeVenta;
	}

	private PanelTablaFacturas getPanelTablaFacturas() {
		if (panelTablaFacturas == null) {
			panelTablaFacturas = new PanelTablaFacturas();
		}
		return panelTablaFacturas;
	}

	private JButton getBtnVerificar() {
		if (btnVerificar == null) {
			btnVerificar = new JButton("Verificar");
			btnVerificar.setMnemonic(KeyEvent.VK_V);
			btnVerificar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					refrescarModelo();
					if (getEstadoPantalla() == EEstadoPantalla.ESTADO_VALIDO) {
						setEstadoPantalla(EEstadoPantalla.VERIFICADO);
					}
				}
			});
		}
		return btnVerificar;
	}

	private class ModeloDialogoAltaOrdenDePago {

		private final List<Cheque> chequesUtilizados;
		private final List<Cheque> chequesdDescartados;
		private final List<FormaPagoOrdenDePago> formasDePago;
		private final List<PagoOrdenDePago> pagos;
		private final List<PagoOrdenDePago> pagosFijos;

		private final List<Integer> idsFacturas;
		private final List<Integer> idsNotasDebito;
		private final List<Integer> idsNotasCredito;

		private final Set<Integer> idsFacturasQuitadas;
		private final Set<Integer> idsNotasDebitoQuitadas;
		
		private Double totalFacturas;
		private Double totalCheques;
		private Double totalRetencionesIngresosBrutos;
		private Double totalRetencionesIva;
		private Double totalRetencionesGanancias;
		private Double totalEfectivo;
		private Double totalNotasCredito;
		private Double totalGeneral;
		private Double totalTransferencias;

		private Date fechaPromedio;
		private int divisorFechaPromeido; //es la suma de los pagos en los que se pondera la fecha

		public ModeloDialogoAltaOrdenDePago() {
			this.chequesUtilizados = new ArrayList<Cheque>();
			this.chequesdDescartados = new ArrayList<Cheque>();
			this.formasDePago = new ArrayList<FormaPagoOrdenDePago>();
			this.pagos = new ArrayList<PagoOrdenDePago>();
			this.idsFacturas = new ArrayList<Integer>();
			this.idsNotasDebito = new ArrayList<Integer>();
			this.idsNotasCredito = new ArrayList<Integer>();
			this.pagosFijos = new ArrayList<PagoOrdenDePago>();
			this.fechaPromedio = null;
			this.idsFacturasQuitadas = new HashSet<Integer>();
			this.idsNotasDebitoQuitadas = new HashSet<Integer>();
			totalCheques = 0d;
			totalGeneral = 0d;
			totalRetencionesIngresosBrutos = 0d;
			totalRetencionesGanancias = 0d;
			totalRetencionesIva = 0d;
			totalEfectivo = 0d;
			totalFacturas = 0d;
			divisorFechaPromeido = 0;
			totalNotasCredito = 0d;
			totalTransferencias = 0d;
		}
		
		public List<Integer> getIdFacturasUtilizadas(){
			return idsFacturas;
		}
		
		public List<Integer> getIdNotasDebitoUtilizadas(){
			return idsNotasDebito;
		}
		
		public List<Integer> getIdNotasCreditoUtilizadas(){
			return idsNotasCredito;
		}

		public List<Cheque> getChequesUtilizados() {
			return chequesUtilizados;
		}

		public List<Cheque> getChequesDescartados() {
			return chequesdDescartados;
		}

		public Double getTotalCheques() {
			return totalCheques;
		}

		public void setTotalCheques(Double totalCheques) {
			this.totalCheques = totalCheques;
		}

		public Double getTotalGeneral() {
			return totalGeneral;
		}

		public void setTotalGeneral(Double totalGeneral) {
			this.totalGeneral = totalGeneral;
		}

		public Double getTotalRetencionesIngresosBrutos() {
			return totalRetencionesIngresosBrutos;
		}

		public void setTotalRetenciones(Double totalRetenciones) {
			this.totalRetencionesIngresosBrutos = totalRetenciones;
		}

		public Double getTotalEfectivo() {
			return totalEfectivo;
		}

		public void setTotalEfectivo(Double totalEfectivo) {
			this.totalEfectivo = totalEfectivo;
		}

		public void updateTotalGeneral() {
			double suma = this.getTotalGeneral() + this.getTotalCheques() + this.getTotalRetencionesIngresosBrutos() + 
								this.getTotalEfectivo() + this.getTotalRetencionesGanancias() + this.getTotalRetencionesIva()+
								this.getTotalNotasCredito() + this.getTotalTransferencias();
			suma *=100;
			suma = Math.ceil(suma)/100;
			this.setTotalGeneral(suma);
		}

		public List<FormaPagoOrdenDePago> getFormasDePago() {
			return formasDePago;
		}

		public List<PagoOrdenDePago> getPagos() {
			return pagos;
		}

		public Double getTotalFacturas() {
			return totalFacturas;
		}

		public void setTotalFacturas(Double totalFacturas) {
			this.totalFacturas = totalFacturas;
		}

		public Date getFechaPromedio() {
			return fechaPromedio;
		}

		public void setFechaPromedio(Date fechaPromedio) {
			this.fechaPromedio = fechaPromedio;
		}

		public int getDivisorFechaPromeido() {
			return divisorFechaPromeido;
		}

		public void setDivisorFechaPromeido(int divisorFechaPromeido) {
			this.divisorFechaPromeido = divisorFechaPromeido;
		}
		
		public Double getTotalRetencionesIva() {
			return totalRetencionesIva;
		}
		
		public Double getTotalRetencionesGanancias() {
			return totalRetencionesGanancias;
		}

		public void setTotalRetencionesIngresosBrutos(Double totalRetencionesIngresosBrutos) {
			this.totalRetencionesIngresosBrutos = totalRetencionesIngresosBrutos;
		}

		public void setTotalRetencionesIva(Double totalRetencionesIva) {
			this.totalRetencionesIva = totalRetencionesIva;
		}
		
		public void setTotalRetencionesGanancias(Double totalRetencionesGanancias) {
			this.totalRetencionesGanancias = totalRetencionesGanancias;
		}

		public Double getTotalNotasCredito() {
			return totalNotasCredito;
		}
		
		public void setTotalNotasCredito(Double totalNotasCredito) {
			this.totalNotasCredito = totalNotasCredito;
		}

		public List<PagoOrdenDePago> getPagosFijos() {
			return pagosFijos;
		}

		public Double getTotalTransferencias() {
			return totalTransferencias;
		}
		
		public void setTotalTransferencias(Double totalTransferencias) {
			this.totalTransferencias = totalTransferencias;
		}

		
		public Set<Integer> getIdsFacturasQuitadas() {
			return idsFacturasQuitadas;
		}
		
		public Set<Integer> getIdsNotasDebitoQuitadas() {
			return idsNotasDebitoQuitadas;
		}
	}

	private class PagoOrdenDePagoFechaComparator implements Comparator<PagoOrdenDePago> {

		public int compare(PagoOrdenDePago o1, PagoOrdenDePago o2) {
			PagoOrdenDePagoSorterVisitor sorterVisitor = new PagoOrdenDePagoSorterVisitor();
			o1.accept(sorterVisitor);
			Date fechaO1 = sorterVisitor.getFecha();
			o2.accept(sorterVisitor);
			Date fechaO2 = sorterVisitor.getFecha();
			return fechaO1.compareTo(fechaO2);
		}
	}

	private class PagoOrdenDePagoSorterVisitor implements IPagoOrdenPagoVisitor {

		private Date fecha;

		public void visit(PagoOrdenDePagoACuenta popac) {
			setFecha(new Date(Long.MAX_VALUE));
		}

		public void visit(PagoOrdenDePagoFactura popf) {
			setFecha(popf.getFactura().getFechaIngreso());
		}

		public void visit(PagoOrdenDePagoNotaDebito popnd) {
			setFecha(popnd.getNotaDebito().getFechaIngreso());
		}

		public Date getFecha() {
			return fecha;
		}

		public void setFecha(Date fecha) {
			this.fecha = fecha;
		}
	}

	private FWJTextField getTxtTotalCheques() {
		if (txtTotalCheques == null) {
			txtTotalCheques = new FWJTextField();
			txtTotalCheques.setPreferredSize(new Dimension(120, 20));
			txtTotalCheques.setEditable(false);
		}
		return txtTotalCheques;
	}

	private enum EEstadoPantalla {
		NO_VERIFICADO, ESTADO_INVALIDO, ESTADO_VALIDO, VERIFICADO;
	}

	public EEstadoPantalla getEstadoPantalla() {
		return estadoPantalla;
	}

	public void setEstadoPantalla(EEstadoPantalla estadoPantalla) {
		this.estadoPantalla = estadoPantalla;
	}

	private JButton getBtnSalir() {
		if (btnSalir == null) {
			btnSalir = new JButton("Salir");
			btnSalir.setMnemonic(KeyEvent.VK_S);
			btnSalir.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					salir();
				}
			});
		}
		return btnSalir;
	}

	public OrdenDePagoFacadeRemote getOrdenDePagoFacade() {
		if (ordenDePagoFacade == null) {
			ordenDePagoFacade = GTLBeanFactory.getInstance().getBean2(OrdenDePagoFacadeRemote.class);
		}
		return ordenDePagoFacade;
	}

	private class CrearFilaPagoOrdenDePagoVisitor implements IPagoOrdenPagoVisitor {

		public void visit(PagoOrdenDePagoACuenta popac) {
			Object[] row = new Object[CANT_COLS];
			row[COL_FECHA] = DateUtil.dateToString(getPanelFecha().getDate());
			row[COL_DOCUMENTO] = "A Cuenta";
			row[COL_IMPORTE] = GenericUtils.getDecimalFormatTablaMovimientos().format(popac.getMontoPagado());
			row[COL_OBJ] = popac;
			getPanelTablaFacturas().getTabla().addRow(row);
		}

		public void visit(PagoOrdenDePagoFactura popf) {
			FacturaProveedor fact = popf.getFactura();
			Object[] row = new Object[CANT_COLS];
			row[COL_FECHA] =  DateUtil.dateToString(fact.getFechaIngreso(), DateUtil.SHORT_DATE);
			if(isConsulta()){
				row[COL_DOCUMENTO] = "<html><b>Factura - Nro.:</b> " + fact.getNroFactura() + "<br><b>Monto total:</b> " + GenericUtils.getDecimalFormatTablaMovimientos().format(fact.getMontoTotal().doubleValue()) +
									 "<br><b>Faltante:</b> " +fact.getMontoFaltantePorPagar() + getDescrPago(popf) + "</html>";
			}else{
				row[COL_DOCUMENTO] = "<html><b>Factura - Nro.:</b> " + fact.getNroFactura() + "<br><b>Monto total:</b> " + GenericUtils.getDecimalFormatTablaMovimientos().format(fact.getMontoTotal().doubleValue()) +
									 "<br><b>Faltante:</b> " + GenericUtils.getDecimalFormatTablaMovimientos().format(fact.getMontoFaltantePorPagar().subtract(popf.getMontoPagado())) + getDescrPago(popf) + "</html>";
			}
			row[COL_IMPORTE] = GenericUtils.getDecimalFormatTablaMovimientos().format(popf.getMontoPagado());
			row[COL_OBJ] = popf;
			getPanelTablaFacturas().getTabla().addRow(row);
		}

		public void visit(PagoOrdenDePagoNotaDebito popnd) {
			NotaDebitoProveedor nd = popnd.getNotaDebito();
			Object[] row = new Object[CANT_COLS];
			row[COL_FECHA] =  DateUtil.dateToString(nd.getFechaIngreso(), DateUtil.SHORT_DATE);
			if(isConsulta()){
				row[COL_DOCUMENTO] = "<html><b>Nota de débito - Nro.:</b> " + nd.getNroCorreccion() + "<br><b>Monto total:</b> " + GenericUtils.getDecimalFormatTablaMovimientos().format(nd.getMontoTotal().doubleValue()) +
									 "<br><b>Faltante:</b> " +nd.getMontoFaltantePorPagar() + getDescrPago(popnd) + "</html>";
			}else{
				row[COL_DOCUMENTO] = "<html><b>Nota de débito - Nro.:</b> " + nd.getNroCorreccion() + "<br><b>Monto total:</b> " + GenericUtils.getDecimalFormatTablaMovimientos().format(nd.getMontoTotal().doubleValue()) +
									 "<br><b>Faltante:</b> " + GenericUtils.getDecimalFormatTablaMovimientos().format(nd.getMontoFaltantePorPagar().subtract(popnd.getMontoPagado())) + getDescrPago(popnd) + "</html>";
			}
			row[COL_IMPORTE] = GenericUtils.getDecimalFormatTablaMovimientos().format(popnd.getMontoPagado());
			row[COL_OBJ] = popnd;
			getPanelTablaFacturas().getTabla().addRow(row);
		}
		
		private String getDescrPago(PagoOrdenDePagoFactura prf) {
			return getDecripcionPago(prf.getDescrPagoFactura());
		}
		
		private String getDecripcionPago(EDescripcionPagoFactura descrPagoFactura) {
			if (descrPagoFactura == null) {
				return "";
			}
			if (descrPagoFactura == EDescripcionPagoFactura.FACTURA) {
				return "";
			} else {
				return " <b>(" + descrPagoFactura.getDescripcion() + ")</b> ";
			}
		}

		private String getDescrPago(PagoOrdenDePagoNotaDebito prf) {
			return getDecripcionPago(prf.getDescrPagoFactura());
		}
	}

	private class ActualizarValorPagoOrdenDePagoVisitor implements IPagoOrdenPagoVisitor {

		private Double nuevoImporte;
		private boolean errorImporte;

		public ActualizarValorPagoOrdenDePagoVisitor(Double nuevoImporte) {
			setNuevoImporte(nuevoImporte);
			setErrorImporte(false);
		}

		public void visit(PagoOrdenDePagoACuenta popac) {

		}

		public void visit(PagoOrdenDePagoFactura popf) {
			FacturaProveedor factura = popf.getFactura();
			if (factura.getMontoFaltantePorPagar().doubleValue() >= getNuevoImporte().doubleValue()) {
				popf.setMontoPagado(new BigDecimal(getNuevoImporte()));
			} else {
				setErrorImporte(true);
				return;
			}
		}

		public void visit(PagoOrdenDePagoNotaDebito popnd) {

		}
		
		public Double getNuevoImporte() {
			return nuevoImporte;
		}

		public void setNuevoImporte(Double nuevoImporte) {
			this.nuevoImporte = nuevoImporte;
		}

		public boolean isErrorImporte() {
			return errorImporte;
		}

		public void setErrorImporte(boolean errorImporte) {
			this.errorImporte = errorImporte;
		}
	}

	private FWJTextField getTxtRetencionesIva() {
		if(txtRetencionesIva == null){
			txtRetencionesIva = new FWJTextField();
			txtRetencionesIva.setPreferredSize(new Dimension(100, 20));
			txtRetencionesIva.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					setEstadoPantalla(EEstadoPantalla.NO_VERIFICADO);
				}
			});
		}
		return txtRetencionesIva;
	}

	
	private FWJTextField getTxtRetencionesGanancias() {
		if(txtRetencionesGanancias == null){
			txtRetencionesGanancias = new FWJTextField();
			txtRetencionesGanancias.setPreferredSize(new Dimension(100, 20));
			txtRetencionesGanancias.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					setEstadoPantalla(EEstadoPantalla.NO_VERIFICADO);
				}
			});
		}
		return txtRetencionesGanancias;
	}
	
	public FacturaProveedorFacadeRemote getFacturaProveedorFacade() {
		if(facturaProveedorFacade == null){
			facturaProveedorFacade = GTLBeanFactory.getInstance().getBean2(FacturaProveedorFacadeRemote.class);
		}
		return facturaProveedorFacade;
	}
	
	public ChequeFacadeRemote getChequeFacade() {
		if(chequeFacade == null){
			chequeFacade = GTLBeanFactory.getInstance().getBean2(ChequeFacadeRemote.class);
		}
		return chequeFacade;
	}
	
	public boolean isConsulta() {
		return consulta;
	}
	
	public void setConsulta(boolean consulta) {
		this.consulta = consulta;
	}

	public OrdenDePago getOrdenFinal() {
		return ordenFinal;
	}

	public void setOrdenFinal(OrdenDePago ordenFinal) {
		this.ordenFinal = ordenFinal;
	}
	
	private PanelTablaFormaPagoOrdenDePagoNotaCredito getPanelTablaNotasDeCredito() {
		if(panelTablaNotasDeCredito == null){
			panelTablaNotasDeCredito = new PanelTablaFormaPagoOrdenDePagoNotaCredito();
		}
		return panelTablaNotasDeCredito;
	}

	private FWJTextField getTxtTotalNotasDeCredito() {
		if(txtTotalNotasDeCredito == null){
			txtTotalNotasDeCredito = new FWJTextField();
			txtTotalNotasDeCredito.setPreferredSize(new Dimension(120, 20));
			txtTotalNotasDeCredito.setEditable(false);
		}
		return txtTotalNotasDeCredito;
	}
	
	private FWJTextField getTxtNroRecibo() {
		if(txtNroRecibo == null){
			txtNroRecibo = new FWJTextField();
			txtNroRecibo.setPreferredSize(new Dimension(270, 20));
			txtNroRecibo.setEditable(false);
		}
		return txtNroRecibo;
	}
	
	private FWJTextField getTxtUsuarioCreador() {
		if(txtUsuarioCreador == null){
			txtUsuarioCreador = new FWJTextField();
			txtUsuarioCreador.setPreferredSize(new Dimension(120, 20));
			txtUsuarioCreador.setEditable(false);
			if(getOrdenFinal()!=null){
				txtUsuarioCreador.setText(getOrdenFinal().getUsuarioCreador());
			}else{
				txtUsuarioCreador.setText(GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName());
			}
		}
		return txtUsuarioCreador;
	}

	
	public boolean isEdicion() {
		return edicion;
	}

	
	public void setEdicion(boolean edicion) {
		this.edicion = edicion;
	}
	
	private JPanel getPanTransferencias() {
		if(panTransferencias == null) {
			panTransferencias = new JPanel();
			panTransferencias.setLayout(new GridBagLayout());
			panTransferencias.setBorder(BorderFactory.createTitledBorder("Transferencia"));
			panTransferencias.add(new JLabel("Importe: "), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 5, 5), 1, 1, 0, 1));
			panTransferencias.add(getTxtImporteTransf(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 1, 1, 1, 1));
			panTransferencias.add(new JLabel("Nº Transf: "), GenericUtils.createGridBagConstraints(2, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 5, 5), 1,1, 0, 1));
			panTransferencias.add(getTxtNroTransf(), GenericUtils.createGridBagConstraints(3, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 1,1, 1, 1));
			panTransferencias.add(new JLabel("Observaciones: "), GenericUtils.createGridBagConstraints(4, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 5, 5), 1,1, 0, 1));
			panTransferencias.add(getTxtObservacionesTransf(), GenericUtils.createGridBagConstraints(5, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 3, 1, 1, 1));
		}
		return panTransferencias;
	}
	
	private FWJTextField getTxtObservacionesTransf() {
		if(txtObservacionesTransf == null) {
			txtObservacionesTransf = new FWJTextField();
		}
		return txtObservacionesTransf;
	}
	
	private FWJTextField getTxtImporteTransf() {
		if(txtImporteTransf == null) {
			txtImporteTransf = new FWJTextField();
		}
		return txtImporteTransf;
	}

	private FWJTextField getTxtNroTransf() {
		if(txtNroTransf == null) {
			txtNroTransf = new FWJTextField();
		}
		return txtNroTransf;
	}
}