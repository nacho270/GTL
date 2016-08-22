package ar.com.textillevel.gui.acciones.proveedor.remitosalida;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import main.GTLGlobalCache;

import org.apache.taglibs.string.util.StringW;

import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTable;
import ar.com.fwcommon.componentes.FWJTextField;
import ar.com.fwcommon.componentes.PanelTabla;
import ar.com.fwcommon.componentes.error.validaciones.ValidacionException;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.fwcommon.util.StringUtil;
import ar.com.textillevel.entidades.documentos.factura.proveedor.CorreccionFacturaProveedor;
import ar.com.textillevel.entidades.documentos.factura.proveedor.FacturaProveedor;
import ar.com.textillevel.entidades.documentos.remito.RemitoSalida;
import ar.com.textillevel.entidades.documentos.remito.proveedor.ContenedorMateriaPrima;
import ar.com.textillevel.entidades.documentos.remito.proveedor.ItemOtro;
import ar.com.textillevel.entidades.documentos.remito.proveedor.ItemPrecioMateriaPrima;
import ar.com.textillevel.entidades.documentos.remito.proveedor.ItemRelacionContenedor;
import ar.com.textillevel.entidades.documentos.remito.proveedor.ItemRemitoSalidaProveedor;
import ar.com.textillevel.entidades.documentos.remito.proveedor.RelacionContenedorPrecioMatPrima;
import ar.com.textillevel.entidades.documentos.remito.proveedor.RemitoEntradaProveedor;
import ar.com.textillevel.entidades.documentos.remito.proveedor.visitor.IItemRemitoSalidaVisitor;
import ar.com.textillevel.entidades.gente.Proveedor;
import ar.com.textillevel.entidades.ventas.materiaprima.PrecioMateriaPrima;
import ar.com.textillevel.facade.api.remote.ContenedorMateriaPrimaFacadeRemote;
import ar.com.textillevel.facade.api.remote.FacturaProveedorFacadeRemote;
import ar.com.textillevel.facade.api.remote.RelacionContenedorMatPrimaFacadeRemote;
import ar.com.textillevel.facade.api.remote.RemitoSalidaFacadeRemote;
import ar.com.textillevel.gui.acciones.impresionremsalproveedor.ImprimirRemitoSalidaProveedorHandler;
import ar.com.textillevel.gui.acciones.proveedor.JDialogCargarFacturaProveedor;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.controles.LinkableLabel;
import ar.com.textillevel.gui.util.controles.PanelDatePicker;
import ar.com.textillevel.gui.util.dialogs.JDialogSeleccionarItem;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogAgregarRemitoSalidaProveedor extends JDialog {

	private static final long serialVersionUID = 8707094567428354187L;

	private static final int CANT_MAX_FACTURAS_SELECTED_HARDCODE = 15;

	private JPanel panelDatosProveedor;
	private JPanel panelDatosRemito;
	private JPanel panelBotones;
	private JPanel panDetalle;

	private FWJTextField txtRazonSocial;
	private FWJTextField txtCuit;
	private FWJTextField txtLocalidad;
	private FWJTextField txtDireccion;
	private FWJTextField txtPosicionIva;
	private FWJTextField txtIngBrutos;
	private JTextField txtNroRemito;

	private JButton btnAceptar;
	private JButton btnImprimir;
	private JButton btnSalir;

	private PanelDatePicker panelFecha;
	private PanelTablaItemRetornableProveedor panTablaRelContMatPrima;

	private boolean modoConsulta;

	private Frame padre;

	private RemitoEntradaProveedor remitoEntrada;
	private RemitoSalidaFacadeRemote remitoSalidaFacade;

	protected RemitoSalida remitoSalidaProveedor;
	private ContenedorMateriaPrimaFacadeRemote contenedorFacade;

	private JPanel panFacturas;
	private JButton btnSelFacturas;
	private FacturaLinkeableLabel[] facturaLinkeableLabelList;
	private List<FacturaProveedor> facturasParaGenerarNC;

	public JDialogAgregarRemitoSalidaProveedor(Frame padre, Boolean modoConsulta, RemitoSalida remitoSalidaProveedor) {
		super(padre);
		this.padre = padre;
		this.modoConsulta = modoConsulta;
		this.remitoSalidaProveedor = remitoSalidaProveedor;
		this.facturaLinkeableLabelList = new FacturaLinkeableLabel[CANT_MAX_FACTURAS_SELECTED_HARDCODE];
		this.facturasParaGenerarNC = new ArrayList<FacturaProveedor>();
		setUpComponentes();
		setUpScreen();
		setDatosProveedor();
		if (modoConsulta) {
			GuiUtil.setEstadoPanel(getPanDetalle(), false);
			getPanTablaItemRetornableProveedor().agregarElementos(remitoSalidaProveedor.getItems());
			getPanelFecha().setSelectedDate(remitoSalidaProveedor.getFechaEmision());
		} else if(remitoSalidaProveedor.getId() != null) {
			getPanTablaItemRetornableProveedor().agregarElementos(remitoSalidaProveedor.getItems());
			getPanelFecha().setSelectedDate(remitoSalidaProveedor.getFechaEmision());
		}
	}

	private void setDatosProveedor() {
		Proveedor proveedor = remitoSalidaProveedor.getProveedor();
		getTxtCuit().setText(proveedor.getCuit());
		getTxtDireccion().setText(proveedor.getDireccionFiscal().getDireccion());
		getTxtLocalidad().setText(proveedor.getDireccionFiscal().getLocalidad().getNombreLocalidad());
		getTxtIngBrutos().setText(proveedor.getNroIngresosBrutos());
		if (proveedor.getPosicionIva() != null) {
			getTxtPosicionIva().setText(proveedor.getPosicionIva().getDescripcion());
		}
		getTxtRazonSocial().setText(proveedor.getRazonSocial());
		if(modoConsulta || remitoSalidaProveedor.getId() != null) {
			getTxtNroRemito().setText(String.valueOf(remitoSalidaProveedor.getNroRemito()));
		} else  {
			int nroRemito = getRemitoSalidaFacade().getLastNroRemito() + 1;
			remitoSalidaProveedor.setNroRemito(nroRemito);
			getTxtNroRemito().setText(String.valueOf(nroRemito ));
		}
		for(CorreccionFacturaProveedor cfp : remitoSalidaProveedor.getCorreccionesProvGeneradas()) {
			facturasParaGenerarNC.addAll(cfp.getFacturas());
		}
		setFacturasLabel(facturasParaGenerarNC);
	}

	private void setFacturasLabel(List<FacturaProveedor> facturaList) {
		for(FacturaLinkeableLabel rll : facturaLinkeableLabelList) {
			rll.setVisible(false);
			rll.setFactura(null);
		}
		for(int i=0; i < Math.min(facturaList.size(), CANT_MAX_FACTURAS_SELECTED_HARDCODE); i++) {
			FacturaLinkeableLabel facturaLinkeableLabel = facturaLinkeableLabelList[i];
			facturaLinkeableLabel.setFactura(facturaList.get(i));
			facturaLinkeableLabel.setVisible(true);
		}
	}
	
	private void setUpScreen() {
		setSize(new Dimension(790, 750));
		if(modoConsulta) {
			setTitle("Consulta remito salida proveedor");
		} else {
			if(remitoSalidaProveedor.getId() == null) {
				setTitle("Alta remito salida proveedor");
			} else {
				setTitle("Edición remito salida proveedor");
			}
		}
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setModal(true);
		GuiUtil.centrar(this);
	}

	private void setUpComponentes() {
		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent event) {
				salir();
			}

		});
		setLayout(new GridBagLayout());
		add(getPanDetalle(), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 1, 1, 0, 1));
		add(getPanelBotones(), GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
	}

	private JPanel getPanelDatosProveedor() {
		if (panelDatosProveedor == null) {
			panelDatosProveedor = new JPanel();
			panelDatosProveedor.setLayout(new GridBagLayout());
			panelDatosProveedor.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
			panelDatosProveedor.add(new JLabel("Señor/es: "), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelDatosProveedor.add(getTxtRazonSocial(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelDatosProveedor.add(new JLabel("C.U.I.T: "), GenericUtils.createGridBagConstraints(2, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelDatosProveedor.add(getTxtCuit(), GenericUtils.createGridBagConstraints(3, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 3, 1, 1, 0));
			panelDatosProveedor.add(new JLabel("Direccion: "), GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelDatosProveedor.add(getTxtDireccion(), GenericUtils.createGridBagConstraints(1, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panelDatosProveedor.add(new JLabel("Localidad: "), GenericUtils.createGridBagConstraints(2, 1, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelDatosProveedor.add(getTxtLocalidad(), GenericUtils.createGridBagConstraints(3, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panelDatosProveedor.add(new JLabel("Ing. Brutos: "), GenericUtils.createGridBagConstraints(0, 2, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelDatosProveedor.add(getTxtIngBrutos(), GenericUtils.createGridBagConstraints(1, 2, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panelDatosProveedor.add(new JLabel("Posicion IVA: "), GenericUtils.createGridBagConstraints(2, 2, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelDatosProveedor.add(getTxtPosicionIva(), GenericUtils.createGridBagConstraints(3, 2, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
		}

		return panelDatosProveedor;
	}

	private JPanel getPanDetalle() {
		if (panDetalle == null) {
			panDetalle = new JPanel();
			panDetalle.setLayout(new GridBagLayout());
			panDetalle.add(getPanelDatosProveedor(), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 6, 1, 0, 0));
			panDetalle.add(getPanelDatosRemito(), GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 6, 1, 0, 0));
			panDetalle.add(getPanSeleccionarFacturas(), GenericUtils.createGridBagConstraints(0, 2, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 6, 1, 0, 0));
			panDetalle.add(getPanTablaItemRetornableProveedor(), GenericUtils.createGridBagConstraints(0, 3, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 6, 1, 1, 1));
		}
		return panDetalle;
	}

	private JPanel getPanSeleccionarFacturas() {
		if (panFacturas == null) {
			panFacturas = new JPanel();
			panFacturas.setLayout(new GridBagLayout());
			panFacturas.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
			panFacturas.add(new JLabel("Factura(s): "), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), CANT_MAX_FACTURAS_SELECTED_HARDCODE, 1, 1, 0));
			for(int i = 0; i < CANT_MAX_FACTURAS_SELECTED_HARDCODE; i++) {
				LinkableLabel lblConsultaFactura = new FacturaLinkeableLabel();
				facturaLinkeableLabelList[i] = (FacturaLinkeableLabel)lblConsultaFactura;
				panFacturas.add(lblConsultaFactura, GenericUtils.createGridBagConstraints(i+1, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			}
			panFacturas.add(getBtnSeleccionarFacturas(), GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
		}
		return panFacturas;
	}

	private JButton getBtnSeleccionarFacturas() {
		if(btnSelFacturas == null) {
			btnSelFacturas = new JButton("Seleccionar Facturas");
			btnSelFacturas.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					Proveedor proveedor = remitoSalidaProveedor.getProveedor();
					List<FacturaProveedor> facturas = GTLBeanFactory.getInstance().getBean2(FacturaProveedorFacadeRemote.class).getFacturasConMateriaPrimaIBC(proveedor.getId());
					JDialogSeleccionarItem<FacturaProveedor> dialogo = new JDialogSeleccionarItem<FacturaProveedor>(padre, "Seleccionar Facturas", facturas);
					dialogo.setSelectedItems(facturasParaGenerarNC);
					GuiUtil.centrar(dialogo);
					dialogo.setVisible(true);
					if(dialogo.isAcepto()) {
						facturasParaGenerarNC.clear();
						facturasParaGenerarNC.addAll(dialogo.getItemsList());
						setFacturasLabel(facturasParaGenerarNC);
					}
				}

			});
		}
		return btnSelFacturas;
	}

	private class FacturaLinkeableLabel extends LinkableLabel {

		private static final long serialVersionUID = -4765485631705199316L;

		private FacturaProveedor factura;

		public FacturaLinkeableLabel() {
			super("x");
		}

		@Override
		public void labelClickeada(MouseEvent e) {
			if (e.getClickCount() == 1 && factura!=null) {
				FacturaProveedor facturaEager = GTLBeanFactory.getInstance().getBean2(FacturaProveedorFacadeRemote.class).getByIdEager(this.factura.getId());
				JDialogCargarFacturaProveedor dialogoRemitoEntrada = new JDialogCargarFacturaProveedor(padre, facturaEager, true, new ArrayList<RemitoEntradaProveedor>());
				GuiUtil.centrar(dialogoRemitoEntrada);
				dialogoRemitoEntrada.setVisible(true);
			}
		}

		public void setFactura(FacturaProveedor factura) {
			this.factura = factura;
			if(factura != null) {
				setTexto(factura.getNroFactura());
				refreshLabel();
			}
		}

	}

	private FWJTextField getTxtRazonSocial() {
		if (txtRazonSocial == null) {
			txtRazonSocial = new FWJTextField();
			txtRazonSocial.setEditable(false);
		}
		return txtRazonSocial;
	}

	private FWJTextField getTxtCuit() {
		if (txtCuit == null) {
			txtCuit = new FWJTextField();
			txtCuit.setEditable(false);
		}
		return txtCuit;
	}

	private FWJTextField getTxtDireccion() {
		if (txtDireccion == null) {
			txtDireccion = new FWJTextField();
			txtDireccion.setEditable(false);
		}
		return txtDireccion;
	}

	private FWJTextField getTxtLocalidad() {
		if (txtLocalidad == null) {
			txtLocalidad = new FWJTextField();
			txtLocalidad.setEditable(false);
		}
		return txtLocalidad;
	}

	private FWJTextField getTxtPosicionIva() {
		if (txtPosicionIva == null) {
			txtPosicionIva = new FWJTextField();
			txtPosicionIva.setEditable(false);
		}
		return txtPosicionIva;
	}

	private FWJTextField getTxtIngBrutos() {
		if (txtIngBrutos == null) {
			txtIngBrutos = new FWJTextField();
			txtIngBrutos.setEditable(false);
		}
		return txtIngBrutos;
	}

	private PanelDatePicker getPanelFecha() {
		if (panelFecha == null) {
			panelFecha = new PanelDatePicker();
		}
		return panelFecha;
	}

	private JPanel getPanelDatosRemito() {
		if (panelDatosRemito == null) {
			panelDatosRemito = new JPanel();
			panelDatosRemito.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
			panelDatosRemito.setLayout(new GridBagLayout());
			panelDatosRemito.add(new JLabel("Nro. de Remito: "), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelDatosRemito.add(getTxtNroRemito(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0.1, 0));
			panelDatosRemito.add(new JLabel("Fecha: "), GenericUtils.createGridBagConstraints(2, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelDatosRemito.add(getPanelFecha(), GenericUtils.createGridBagConstraints(3, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
		}
		return panelDatosRemito;
	}

	private JTextField getTxtNroRemito() {
		if(txtNroRemito == null) {
			txtNroRemito = new JTextField();
			txtNroRemito.setEditable(false);
		}
		return txtNroRemito;
	}

	private class PanelTablaItemRetornableProveedor extends PanelTabla<ItemRemitoSalidaProveedor> {

		private static final long serialVersionUID = 4118594187928778745L;

		private static final int CANT_COL_PIEZAS = 4;
		private static final int COL_DESCR_ITEM_RETORNABLE = 0;
		private static final int COL_STOCK_ACTUAL = 1;
		private static final int COL_CANT_SALIDA_ITEM = 2;
		private static final int COL_OBJ = 3;
		
		private JButton btnIngresarOtroItem;

		public PanelTablaItemRetornableProveedor() {
			construirTabla();
			agregarBoton(getBtnIngresarOtroItem());
		}

		private JButton getBtnIngresarOtroItem() {
			if(btnIngresarOtroItem == null) {
				this.btnIngresarOtroItem = new JButton("I");
				this.btnIngresarOtroItem.addActionListener(new  ActionListener() {

					public void actionPerformed(ActionEvent e) {
						ItemOtro itemOtro = new ItemOtro();
						agregarElemento(itemOtro);
					}

				});
			}
			return btnIngresarOtroItem;
		}

		@Override
		public boolean validarAgregar() {
			
			List<ContenedorMateriaPrima> allContenedores = getContenedorFacade().getAllOrderByName();
			List<RelacionContenedorPrecioMatPrima> allRelacionContenedorConStockByIdProveedor = GTLBeanFactory.getInstance().getBean2(RelacionContenedorMatPrimaFacadeRemote.class).getAllRelacionContenedorConStockByIdProveedor(remitoSalidaProveedor.getProveedor().getId(), allContenedores);

			JDialogSeleccionarItemsRetornablesProveedor jdsirp = new JDialogSeleccionarItemsRetornablesProveedor(padre, remitoSalidaProveedor.getProveedor(), extractContenedores(allRelacionContenedorConStockByIdProveedor));
			GuiUtil.centrar(jdsirp);
			jdsirp.setVisible(true);

			ItemRemitoSalidaProveedorFactory itemFactory = new ItemRemitoSalidaProveedorFactory();

			// Lleno los contenedores
			if (!jdsirp.getContenedorListResult().isEmpty()) {
				Collections.sort(allRelacionContenedorConStockByIdProveedor, new RelacionContenedorMPComparator());
				for (RelacionContenedorPrecioMatPrima rel : allRelacionContenedorConStockByIdProveedor) {
					ItemRemitoSalidaProveedor itemRetornable = itemFactory.createItem(rel, null);
					// lo agrego sólo si no existe
					if (notExistsInTabla(itemRetornable)) {
						agregarElemento(itemRetornable);
					}
				}
			}
			// Lleno los precios materias primas
			if (!jdsirp.getPmpListResult().isEmpty()) {
				for (PrecioMateriaPrima pmp : jdsirp.getPmpListResult()) {
					ItemRemitoSalidaProveedor itemRetornable = itemFactory.createItem(null, pmp);
					if(notExistsInTabla(itemRetornable)) {
						agregarElemento(itemRetornable);
					}
				}
			}
			return false;
		}

		private List<ContenedorMateriaPrima> extractContenedores(List<RelacionContenedorPrecioMatPrima> allRelacionContenedorConStockByIdProveedor) {
			Set<ContenedorMateriaPrima> contenedoresSet = new HashSet<ContenedorMateriaPrima>();
			for(RelacionContenedorPrecioMatPrima rel : allRelacionContenedorConStockByIdProveedor) {
				contenedoresSet.add(rel.getContenedor());
			}
			return new ArrayList<ContenedorMateriaPrima>(contenedoresSet);
		}

		private boolean notExistsInTabla(ItemRemitoSalidaProveedor itemRetornable) {
			for(ItemRemitoSalidaProveedor irsp : getPanTablaItemRetornableProveedor().getElementos()) {
				ItemRemitoSalidaValidadorVisitor itemRemitoSalidaValidadorVisitor = new ItemRemitoSalidaValidadorVisitor(irsp);
				itemRetornable.accept(itemRemitoSalidaValidadorVisitor);
				if(itemRemitoSalidaValidadorVisitor.isExiste()) {
					return false;
				}
			}
			return true;
		}

		@Override
		protected void agregarElemento(ItemRemitoSalidaProveedor elemento) {
			Object[] row = new Object[CANT_COL_PIEZAS];
			row[COL_STOCK_ACTUAL] = elemento.getStockActual();
			row[COL_DESCR_ITEM_RETORNABLE] = elemento.toString();
			if(elemento.getCantSalida() ==  null) {
				row[COL_CANT_SALIDA_ITEM] = 0;
				elemento.setCantSalida(elemento.getStockActual());
			} else {
				row[COL_CANT_SALIDA_ITEM] = elemento.getCantSalida().intValue();
			}
			row[COL_OBJ] = elemento;
			getTabla().addRow(row);
			
			if(elemento instanceof ItemOtro) {
				getTabla().unlockCell(getTabla().getRowCount() - 1, COL_DESCR_ITEM_RETORNABLE);
			} else {
				getTabla().lockCell(getTabla().getRowCount() - 1, COL_DESCR_ITEM_RETORNABLE);
			}
		}

		@Override
		protected FWJTable construirTabla() {
			FWJTable tabla = new FWJTable(0, CANT_COL_PIEZAS) {

				private static final long serialVersionUID = 1L;

				@Override
				public void cellEdited(int cell, int row) {
					if(cell == COL_CANT_SALIDA_ITEM) {
						ItemRemitoSalidaProveedor itemRetornable = getElemento(row);
						Integer cantSalida = (Integer) getTabla().getTypedValueAt(row, COL_CANT_SALIDA_ITEM);
						itemRetornable.setCantSalida(new BigDecimal(cantSalida));
					}
					if(cell == COL_DESCR_ITEM_RETORNABLE) {
						ItemRemitoSalidaProveedor itemRetornable = getElemento(row);
						String descripcion = (String) getTabla().getValueAt(row, COL_DESCR_ITEM_RETORNABLE);
						((ItemOtro)itemRetornable).setDescripcion(descripcion);
					}
				}

			};
			tabla.setReorderingAllowed(false);
			tabla.setAllowHidingColumns(false);
			tabla.setAllowSorting(false);
			tabla.setFloatColumn(COL_STOCK_ACTUAL, "STOCK ACTUAL", 120, true);
			tabla.setStringColumn(COL_DESCR_ITEM_RETORNABLE, "MAT. PRIMA / BIDON", 300, 300, false);
			tabla.setIntColumn(COL_CANT_SALIDA_ITEM, "SALIDA", 120, false);
			tabla.setStringColumn(COL_OBJ, "", 0, 0, true);
			tabla.setHeaderAlignment(COL_STOCK_ACTUAL, FWJTable.CENTER_ALIGN);
			tabla.setHeaderAlignment(COL_DESCR_ITEM_RETORNABLE, FWJTable.CENTER_ALIGN);
			tabla.setHeaderAlignment(COL_CANT_SALIDA_ITEM, FWJTable.CENTER_ALIGN);
			return tabla;
		}

		@Override
		protected ItemRemitoSalidaProveedor getElemento(int fila) {
			return (ItemRemitoSalidaProveedor) getTabla().getValueAt(fila, COL_OBJ);
		}

		@Override
		protected String validarElemento(int fila) {
			ItemRemitoSalidaProveedor itemRetornable = (ItemRemitoSalidaProveedor) getTabla().getValueAt(fila, COL_OBJ);
			if (getTabla().getValueAt(fila, COL_CANT_SALIDA_ITEM) == null) {
				return "Falta completar la 'Cantidad a dar salida' para el item correspondiente a '" + itemRetornable.toString() + "'.";
			}
			Integer cantSalida = (Integer) getTabla().getTypedValueAt(fila, COL_CANT_SALIDA_ITEM);
			if(cantSalida == 0) {
				return "La cantidad a dar salida no puede ser 0.";
			}
			if(itemRetornable instanceof ItemOtro) {
				if(StringUtil.isNullOrEmpty(((ItemOtro)itemRetornable).getDescripcion())) {
					return "Debe completar la descripción del item.";
				}
			}
			if(!(itemRetornable instanceof ItemOtro)) {
				if(cantSalida > itemRetornable.getStockActual().intValue()){
					return "La cantidad a dar salida no puede sobrepasar al stock actual en el item '" + itemRetornable.toString() + "'.";
				}
			}
			return null;
		}

		@Override
		protected void botonQuitarPresionado() {
			int r = 0;
			for(ItemRemitoSalidaProveedor irsp : getElementos()) {
				if(irsp instanceof ItemOtro) {
					getTabla().unlockCell(r, COL_DESCR_ITEM_RETORNABLE);
				} else {
					getTabla().lockCell(r, COL_DESCR_ITEM_RETORNABLE);
				}
				r++;
			}
		}
	}

	private PanelTablaItemRetornableProveedor getPanTablaItemRetornableProveedor() {
		if (panTablaRelContMatPrima == null) {
			panTablaRelContMatPrima = new PanelTablaItemRetornableProveedor();
		}
		return panTablaRelContMatPrima;
	}

	private JPanel getPanelBotones() {
		if (panelBotones == null) {
			panelBotones = new JPanel();
			panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER));
			panelBotones.add(getBtnAceptar());
			panelBotones.add(getBtnSalir());
			if(modoConsulta) {
				panelBotones.add(getBtnImprimir());
			}
		}
		return panelBotones;
	}

	private JButton getBtnImprimir() {
		if (btnImprimir == null) {
			btnImprimir = new JButton("Imprimir");
			btnImprimir.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					ImprimirRemitoSalidaProveedorHandler imprimirHandler = new ImprimirRemitoSalidaProveedorHandler(remitoSalidaProveedor, JDialogAgregarRemitoSalidaProveedor.this);
					imprimirHandler.imprimir();
				}

			});
		}
		return btnImprimir;
	}

	private JButton getBtnAceptar() {
		if (btnAceptar == null) {
			btnAceptar = new JButton("Aceptar");
			btnAceptar.setEnabled(!modoConsulta);
			btnAceptar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if (validar()) {
						remitoSalidaProveedor.setFechaEmision(new java.sql.Date(getPanelFecha().getDate().getTime()));
						remitoSalidaProveedor.getItems().clear();
						remitoSalidaProveedor.getItems().addAll(getPanTablaItemRetornableProveedor().getElementos());
						
						RemitoSalida remitoSalidaSaved = null;
						try {
							remitoSalidaSaved = getRemitoSalidaFacade().ingresarRemitoSalidaProveedor(remitoSalidaProveedor, facturasParaGenerarNC, GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName());
						} catch (ValidacionException e1) {
							FWJOptionPane.showInformationMessage(JDialogAgregarRemitoSalidaProveedor.this, StringW.wordWrap(e1.getMensajeError()), "Error");
							return;
						}
						FWJOptionPane.showInformationMessage(JDialogAgregarRemitoSalidaProveedor.this, "El remito se ha guardado con éxito", "Información");

						if(FWJOptionPane.showQuestionMessage(JDialogAgregarRemitoSalidaProveedor.this, "¿Desea imprimir el remito de salida?", "Confirmación") == FWJOptionPane.YES_OPTION) {
							remitoSalidaSaved = getRemitoSalidaFacade().getByIdConPiezasYProductos(remitoSalidaSaved.getId());
							ImprimirRemitoSalidaProveedorHandler imprimirRemitoHandler = new ImprimirRemitoSalidaProveedorHandler(remitoSalidaSaved, JDialogAgregarRemitoSalidaProveedor.this);
							imprimirRemitoHandler.imprimir();
						}

						dispose();
					}
				}

			});
		}
		return btnAceptar;
	}

	private RemitoSalidaFacadeRemote getRemitoSalidaFacade() {
		if(remitoSalidaFacade == null) {
			remitoSalidaFacade = GTLBeanFactory.getInstance().getBean2(RemitoSalidaFacadeRemote.class);
		}
		return remitoSalidaFacade;
	}

	private ContenedorMateriaPrimaFacadeRemote getContenedorFacade() {
		if(contenedorFacade == null) {
			contenedorFacade = GTLBeanFactory.getInstance().getBean2(ContenedorMateriaPrimaFacadeRemote.class);
		}
		return contenedorFacade;
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

	private void salir() {
		if (!modoConsulta) {
			int ret = FWJOptionPane.showQuestionMessage(this, "Va a salir sin grabar, está seguro?", "Pregunta");
			if (ret == FWJOptionPane.YES_OPTION) {
				dispose();
			}
		} else {
			dispose();
		}
	}

	private boolean validar() {
		if(getPanTablaItemRetornableProveedor().getElementos().size()==0){
			FWJOptionPane.showErrorMessage(this, StringW.wordWrap("Debe existir al menos un contenedor para dar salida."), "Error");
			return false;
		}
		String textoValidacion = getPanTablaItemRetornableProveedor().validarElementos();
		if (textoValidacion == null) {
			return true;
		} else {
			FWJOptionPane.showErrorMessage(this, StringW.wordWrap(textoValidacion), "Error");
			return false;
		}
	}

	public RemitoEntradaProveedor getRemitoEntrada() {
		return remitoEntrada;
	}

	public void setRemitoEntrada(RemitoEntradaProveedor remitoEntrada) {
		this.remitoEntrada = remitoEntrada;
	}

	private class RelacionContenedorMPComparator implements Comparator<RelacionContenedorPrecioMatPrima> {

		public int compare(RelacionContenedorPrecioMatPrima o1, RelacionContenedorPrecioMatPrima o2) {
			if(o1.getContenedor().equals(o2.getContenedor())) {
				return (-1)*o1.getStockActual().compareTo(o2.getStockActual());
			} else {
				return o1.getContenedor().toString().compareTo(o2.getContenedor().toString());
			}
		}
	}

	private static class ItemRemitoSalidaValidadorVisitor implements IItemRemitoSalidaVisitor {

		private boolean existe;
		private ItemRemitoSalidaProveedor irsp;

		public ItemRemitoSalidaValidadorVisitor(ItemRemitoSalidaProveedor irsp) {
			this.irsp = irsp;
		}

		public void visit(ItemPrecioMateriaPrima ipmp) {
			if(irsp instanceof ItemPrecioMateriaPrima) {
				this.existe = ((ItemPrecioMateriaPrima)irsp).getPrecioMateriaPrima().equals(ipmp.getPrecioMateriaPrima());
			}
		}

		public void visit(ItemRelacionContenedor irc) {
			if(irsp instanceof ItemRelacionContenedor) {
				this.existe = ((ItemRelacionContenedor)irsp).getRelacionContPrecioMP().equals(irc.getRelacionContPrecioMP());
			}
		}

		public boolean isExiste() {
			return existe;
		}

		public void visit(ItemOtro io) {
			if(irsp instanceof ItemOtro) {
				this.existe = ((ItemOtro)irsp).getDescripcion().equalsIgnoreCase(io.getDescripcion());
			}
		}

	}

	private static class ItemRemitoSalidaProveedorFactory {

		public ItemRemitoSalidaProveedor createItem(RelacionContenedorPrecioMatPrima rel, PrecioMateriaPrima precioMateriaPrima) {
			if((rel == null && precioMateriaPrima == null) || (rel != null && precioMateriaPrima != null)) {
				throw new RuntimeException("No se puede construir un item de proveedor de remito de salida. Parámetros inválidos. ");
			}
			if(rel != null) {
				ItemRelacionContenedor irc = new ItemRelacionContenedor();
				irc.setRelacionContPrecioMP(rel);
				return irc;
			}
			if(precioMateriaPrima != null) {
				ItemPrecioMateriaPrima irsp = new ItemPrecioMateriaPrima();
				irsp.setPrecioMateriaPrima(precioMateriaPrima);
				return irsp;
			}
			return null;
		}
		
	}

}
