package ar.com.textillevel.gui.acciones;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.FocusTraversalPolicy;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

import main.GTLGlobalCache;
import main.acciones.compras.OperacionSobreFacturaProveedorHandler;
import main.acciones.facturacion.IngresoRemitoSalidaHandler;

import org.apache.taglibs.string.util.StringW;

import ar.com.fwcommon.boss.BossError;
import ar.com.fwcommon.boss.BossEstilos;
import ar.com.fwcommon.componentes.FWFileSelector;
import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTable;
import ar.com.fwcommon.componentes.FWJTextField;
import ar.com.fwcommon.componentes.PanelTablaSinBotones;
import ar.com.fwcommon.componentes.VerticalFlowLayout;
import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.componentes.error.validaciones.ValidacionException;
import ar.com.fwcommon.util.DateUtil;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.fwcommon.util.StringUtil;
import ar.com.textillevel.entidades.cuenta.CuentaProveedor;
import ar.com.textillevel.entidades.cuenta.movimientos.MovimientoCuenta;
import ar.com.textillevel.entidades.cuenta.movimientos.MovimientoDebeProveedor;
import ar.com.textillevel.entidades.cuenta.movimientos.MovimientoHaberProveedor;
import ar.com.textillevel.entidades.cuenta.movimientos.visitor.IFilaMovimientoVisitor;
import ar.com.textillevel.entidades.documentos.factura.proveedor.CorreccionFacturaProveedor;
import ar.com.textillevel.entidades.documentos.factura.proveedor.FacturaProveedor;
import ar.com.textillevel.entidades.documentos.factura.proveedor.ItemCorreccionCheque;
import ar.com.textillevel.entidades.documentos.factura.proveedor.ItemCorreccionFacturaProveedor;
import ar.com.textillevel.entidades.documentos.factura.proveedor.NotaCreditoProveedor;
import ar.com.textillevel.entidades.documentos.factura.proveedor.NotaDebitoProveedor;
import ar.com.textillevel.entidades.documentos.factura.proveedor.enums.ETipoFacturaProveedor;
import ar.com.textillevel.entidades.documentos.factura.to.InfoCuentaTO;
import ar.com.textillevel.entidades.documentos.ordendepago.OrdenDePago;
import ar.com.textillevel.entidades.documentos.recibo.Recibo;
import ar.com.textillevel.entidades.documentos.remito.RemitoEntrada;
import ar.com.textillevel.entidades.documentos.remito.RemitoSalida;
import ar.com.textillevel.entidades.documentos.remito.proveedor.RemitoEntradaProveedor;
import ar.com.textillevel.entidades.enums.ETipoRemitoSalida;
import ar.com.textillevel.entidades.gente.Proveedor;
import ar.com.textillevel.entidades.portal.UsuarioSistema;
import ar.com.textillevel.facade.api.remote.CorreccionFacturaProveedorFacadeRemote;
import ar.com.textillevel.facade.api.remote.CuentaFacadeRemote;
import ar.com.textillevel.facade.api.remote.FacturaProveedorFacadeRemote;
import ar.com.textillevel.facade.api.remote.OrdenDePagoFacadeRemote;
import ar.com.textillevel.facade.api.remote.RemitoEntradaProveedorFacadeRemote;
import ar.com.textillevel.facade.api.remote.UsuarioSistemaFacadeRemote;
import ar.com.textillevel.gui.acciones.proveedor.JDialogCargarFacturaProveedor;
import ar.com.textillevel.gui.acciones.proveedor.JDialogCargarFacturaServicioProveedor;
import ar.com.textillevel.gui.acciones.proveedor.JDialogCompletarDatosCorreccionFacturaProveedor;
import ar.com.textillevel.gui.acciones.proveedor.remitosalida.JDialogAgregarRemitoSalidaProveedor;
import ar.com.textillevel.gui.acciones.visitor.cuenta.CellRenderer;
import ar.com.textillevel.gui.acciones.visitor.cuenta.PintarFilaReciboVisitor;
import ar.com.textillevel.gui.acciones.visitor.cuenta.PintarRecibosSecondPassVisitor;
import ar.com.textillevel.gui.acciones.visitor.proveedor.AccionAnularCuentaVisitor;
import ar.com.textillevel.gui.acciones.visitor.proveedor.AccionConfirmarCuentaVisitor;
import ar.com.textillevel.gui.acciones.visitor.proveedor.AccionEliminarFacturaCuentaVisitor;
import ar.com.textillevel.gui.acciones.visitor.proveedor.ConsultaDocumentoVisitor;
import ar.com.textillevel.gui.acciones.visitor.proveedor.EditarDocumentoProveedorVisitor;
import ar.com.textillevel.gui.acciones.visitor.proveedor.GenerarFilaMovimientoVisitor;
import ar.com.textillevel.gui.acciones.visitor.proveedor.HabilitarBotonesCuentaVisitor;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.GenericUtils.BackgroundTask;
import ar.com.textillevel.gui.util.JasperHelper;
import ar.com.textillevel.gui.util.controles.LinkableLabel;
import ar.com.textillevel.gui.util.controles.PanelDatePicker;
import ar.com.textillevel.gui.util.dialogs.JDialogPasswordInput;
import ar.com.textillevel.gui.util.dialogs.JDialogSeleccionarProveedor;
import ar.com.textillevel.gui.util.dialogs.JDialogSeleccionarRemitoEntradaProveedor;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;
import ar.com.textillevel.util.GTLBeanFactory;

public class JFrameVerMovimientosProveedor extends JFrame {

	private static final long serialVersionUID = 2857257268987818318L;

	private static final String EXTENSION_EXCEL = ".xls";
	private static final String EXTENSION_PDF = ".pdf";

	private PanelTablaMovimientos tablaMovimientos;
	private JPanel panelCabecera;
	private PanelDatePicker txtFechaDesde;
	private PanelDatePicker txtFechaHasta;
	private FWJTextField txtBusquedaProveedor;
	private JButton btnBuscar;
	private JPanel panelSur;
	private JButton btnSalir;
	private FWJTextField txtTotalCuenta;
	private JPanel panelAcciones;
	private JPanel panelDatosUsuario;
	private JLabel lblNombre;
	private JLabel lblDireccion;
	private JLabel lblCuit;
	private JLabel lblTelefono;
	private LinkableLabel lblElegirProveedor;
	private JCheckBox chkUltimosMovimientos;
	
	private JButton btnAgregarRemitoCompraTela;
	private JButton btnAgregarRemitoVentaTela;
	private JButton btnAgregarRemitoSalidaProveedor;
	private JButton btnEditar;

	private GenerarFilaMovimientoVisitor filaMovimientoVisitor;
	private IFilaMovimientoVisitor consultaDocumentoMovimientoVisitor;
	
	private Proveedor proveedorElegido;

	private CuentaFacadeRemote cuentaFacade;
	private CorreccionFacturaProveedorFacadeRemote correccionFacade;
	private OrdenDePagoFacadeRemote ordenDePagoFacade;
	private FacturaProveedorFacadeRemote facturaFacade;
	
	/** ACCIONES **/

	private JButton btnAgregarOrdenDePago;
	private JButton btnAnular;
	private JButton btnConfirmar;
	private JButton btnAgregarNroReciboOrdenDePago;
	private JButton btnExportarAExcel;
	private JButton btnImprimirListado;
	private JButton btnListadoPDF;
	private JButton btnEliminarFactura;
	private JButton btnCompletarDatosNotaDebitoCredito;
	private JButton btnAgregarObservaciones;
	private JButton btnAgregarRemito;
	private JButton btnAgregarFactura;
	
	private UsuarioSistema usuarioAdministrador;
	
	public JFrameVerMovimientosProveedor(Frame padre) {
		setUpComponentes();
		setUpScreen();
		getDPFechaDesde().setEnabled(!getChkUltimosMovimientos().isSelected());
		getDPFechaHasta().setEnabled(!getChkUltimosMovimientos().isSelected());
		getTxtBusquedaProveedor().requestFocus();
		getBtnAnular().setEnabled(false);
		getBtnConfirmar().setEnabled(false);
		getLblElegirProveedor().labelClickeada(null);
	}

	private void setUpScreen() {
		setSize(new Dimension(1000, 600));
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setTitle("Ver movimientos");
		GuiUtil.centrar(this);
		setExtendedState(MAXIMIZED_BOTH);
		setVisible(true);
	}

	private void llenarTablaMovimientos(List<MovimientoCuenta> movimientos, BigDecimal transporteCuenta) {
		getPanelTablaMovimientos().getTabla().removeAllRows();
		if(transporteCuenta.doubleValue()!=0){
			getPanelTablaMovimientos().agregarTransporte(transporteCuenta);
		}
		if (movimientos != null) {
			for (MovimientoCuenta mov : movimientos) {
				getPanelTablaMovimientos().agregarElemento(mov);
			}
		}
	}

	private void setUpComponentes() {
		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent we) {
				salir();
			}
		});
		this.add(getPanelCabecera(), BorderLayout.NORTH);
		this.add(getPanelTablaMovimientos(), BorderLayout.CENTER);
		this.add(getPanelSur(), BorderLayout.SOUTH);
	}

	private PanelTablaMovimientos getPanelTablaMovimientos() {
		if (tablaMovimientos == null) {
			tablaMovimientos = new PanelTablaMovimientos();
		}
		return tablaMovimientos;
	}

	private void salir() {
		int ret = FWJOptionPane.showQuestionMessage(this, "Va a cerrar el módulo, esta seguro?", "Factura");
		if (ret == FWJOptionPane.YES_OPTION) {
			dispose();
		}
	}

	private class PanelTablaMovimientos extends PanelTablaSinBotones<MovimientoCuenta> {

		private static final long serialVersionUID = -2675346740708514360L;

		private static final int CANT_COLS_TBL_MOVS = 10;
		private static final int COL_PAGOS = 0;
		private static final int COL_DESCR = 1;
		private static final int COL_DEBE = 2;
		private static final int COL_HABER = 3;
		private static final int COL_SALDO = 4;
		private static final int COL_OBJ = 5;
		private static final int COL_VERIFICADO = 6;
		private static final int COL_USUARIO_VERIFICADOR = 7;
		private static final int COL_USUARIO_CREADOR = 8;
		private static final int COL_OBSERVACIONES = 9;

		@Override
		protected void agregarElemento(MovimientoCuenta mov) {
			mov.aceptarVisitor(filaMovimientoVisitor);
		}

		public void agregarTransporte(BigDecimal transporteCuenta) {
			Object[] row = new Object[CANT_COLS_TBL_MOVS];
			row[COL_PAGOS] = "";
			row[COL_DESCR] = "TRANSPORTE CUENTA";
			row[COL_DEBE] = "";
			row[COL_HABER] = "";
			row[COL_SALDO] = getDecimalFormat().format(transporteCuenta);
			row[COL_OBJ] = "";
			row[COL_VERIFICADO] = false;
			row[COL_USUARIO_VERIFICADOR] = "";
			getTabla().addRow(row);
			getTabla().setForegroundCell(getTabla().getRowCount() - 1, COL_SALDO, transporteCuenta.doubleValue() > 0?Color.GREEN.darker():transporteCuenta.doubleValue()<0?Color.RED:Color.BLACK);
		}

		@Override
		protected FWJTable construirTabla() {
			FWJTable tabla = new FWJTable(0, CANT_COLS_TBL_MOVS);
			tabla.setStringColumn(COL_PAGOS, "Pagos", 100);
			tabla.setStringColumn(COL_DESCR, "Descripción", 220, 320, true);
			tabla.setFloatColumn(COL_DEBE, "Debe", 80, true);
			tabla.setFloatColumn(COL_HABER, "Haber", 80, true);
			tabla.setFloatColumn(COL_SALDO, "Saldo", 80, true);
			tabla.setStringColumn(COL_OBJ, "", 0, 0, true);
			tabla.setCheckColumn(COL_VERIFICADO, "Verificado", 60, true);
			tabla.setStringColumn(COL_USUARIO_VERIFICADOR, "Usuario verficador", 100, 100, true);
			tabla.setStringColumn(COL_USUARIO_CREADOR, "Usuario creador", 100, 100, true);
			tabla.setStringColumn(COL_OBSERVACIONES, "Observaciones", 220, 320, true);
			tabla.setReorderingAllowed(false);
			tabla.setHeaderAlignment(COL_PAGOS, FWJTable.CENTER_ALIGN);
			tabla.setHeaderAlignment(COL_DESCR, FWJTable.CENTER_ALIGN);
			tabla.setHeaderAlignment(COL_DEBE, FWJTable.CENTER_ALIGN);
			tabla.setHeaderAlignment(COL_HABER, FWJTable.CENTER_ALIGN);
			tabla.setHeaderAlignment(COL_SALDO, FWJTable.CENTER_ALIGN);
			tabla.setHeaderAlignment(COL_VERIFICADO, FWJTable.CENTER_ALIGN);
			tabla.setHeaderAlignment(COL_USUARIO_VERIFICADOR, FWJTable.CENTER_ALIGN);
			tabla.setHeaderAlignment(COL_USUARIO_CREADOR, FWJTable.CENTER_ALIGN);
			tabla.setHeaderAlignment(COL_OBSERVACIONES, FWJTable.CENTER_ALIGN);
			tabla.setAlignment(COL_USUARIO_VERIFICADOR, FWJTable.CENTER_ALIGN);
			tabla.setAlignment(COL_USUARIO_CREADOR, FWJTable.CENTER_ALIGN);
			tabla.getColumnModel().getColumn(COL_PAGOS).setCellRenderer(getCellRenderer());
			consultaDocumentoMovimientoVisitor = new ConsultaDocumentoVisitor(JFrameVerMovimientosProveedor.this);
			tabla.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseReleased(MouseEvent e) {
					if(getTabla().getSelectedRow()>-1){
						if(getTabla().getValueAt(getTabla().getSelectedRow(), COL_OBJ) instanceof MovimientoCuenta){
							if (e.getClickCount() == 2 && getTabla().getSelectedRow() > -1) {
								((MovimientoCuenta) getTabla().getValueAt(getTabla().getSelectedRow(), COL_OBJ)).aceptarVisitor(consultaDocumentoMovimientoVisitor);
							} else if (e.getClickCount() == 1 && getTabla().getSelectedRow() > -1) {
								HabilitarBotonesCuentaVisitor hbcv = new HabilitarBotonesCuentaVisitor(JFrameVerMovimientosProveedor.this);
								((MovimientoCuenta) getTabla().getValueAt(getTabla().getSelectedRow(), COL_OBJ)).aceptarVisitor(hbcv);
							}
						}else{
							getBtnAnular().setEnabled(false);
							getBtnConfirmar().setEnabled(false);
							getBtnEliminarFactura().setEnabled(false);
						}
					}
				}
			});
			
			tabla.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					int selectedRow = getTabla().getSelectedRow();
					if(selectedRow>-1){
						if(getTabla().getValueAt(selectedRow, COL_OBJ) instanceof MovimientoCuenta){
							if (e.getKeyCode() == KeyEvent.VK_ENTER && selectedRow > -1) {
								if(selectedRow-1<0){
									selectedRow = getTabla().getRowCount() -1;
								}else{
									selectedRow--;
								}
								getTabla().setRowSelectionInterval(selectedRow, selectedRow);
								((MovimientoCuenta) getTabla().getValueAt(selectedRow, COL_OBJ)).aceptarVisitor(consultaDocumentoMovimientoVisitor);
							} else if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN && getTabla().getSelectedRow() > -1) {
								HabilitarBotonesCuentaVisitor hbcv = new HabilitarBotonesCuentaVisitor(JFrameVerMovimientosProveedor.this);
								((MovimientoCuenta) getTabla().getValueAt(getTabla().getSelectedRow(), COL_OBJ)).aceptarVisitor(hbcv);
							} else if (getTabla().getSelectedRow() < 0) {
								getBtnAnular().setEnabled(false);
								getBtnConfirmar().setEnabled(false);
							}
						}
					}
				}
			});
			return tabla;
		}

		public CellRenderer getCellRenderer() {
			return new CellRenderer();
		}

		@Override
		protected MovimientoCuenta getElemento(int fila) {
			return (MovimientoCuenta) getTabla().getValueAt(fila, COL_OBJ);
		}

		@Override
		protected String validarElemento(int fila) {
			return null;
		}

		public GenerarFilaMovimientoVisitor createVisitorFilaMovimientos(BigDecimal transporte) {
			return new GenerarFilaMovimientoVisitor(getTabla(), CANT_COLS_TBL_MOVS, transporte);
		}
	}

	private JPanel getPanelCabecera() {
		if (panelCabecera == null) {
			panelCabecera = new JPanel();
			panelCabecera.setLayout(new GridBagLayout());
			JPanel panel = new JPanel(new VerticalFlowLayout());
			panel.setBorder(BorderFactory.createEtchedBorder());

			final Component[] comps = new Component[] { getTxtBusquedaProveedor(), getDPFechaDesde(), getDPFechaHasta(), getBtnBuscar() };
			FocusTraversalPolicy policy = new FocusTraversalPolicy() {

				List<Component> textList = Arrays.asList(comps);

				@Override
				public Component getFirstComponent(Container focusCycleRoot) {
					return comps[0];
				}

				@Override
				public Component getLastComponent(Container focusCycleRoot) {
					return comps[comps.length - 1];
				}

				@Override
				public Component getComponentAfter(Container focusCycleRoot, Component aComponent) {
					int index = textList.indexOf(aComponent);
					return comps[(index + 1) % comps.length];
				}

				@Override
				public Component getComponentBefore(Container focusCycleRoot, Component aComponent) {
					int index = textList.indexOf(aComponent);
					return comps[(index - 1 + comps.length) % comps.length];
				}

				@Override
				public Component getDefaultComponent(Container focusCycleRoot) {
					return comps[1];
				}
			};

			setFocusTraversalPolicy(policy);

			JPanel panelProveedor = new JPanel();
			panelProveedor.setLayout(new FlowLayout());
			panelProveedor.add(new JLabel("Proveedor: "));
			panelProveedor.add(getLblElegirProveedor());
			panelProveedor.add(getTxtBusquedaProveedor());
			panelProveedor.add(getBtnBuscar());

			JPanel panelFechas = new JPanel();
			panelFechas.setLayout(new FlowLayout());
			panelFechas.add(getChkUltimosMovimientos());
			panelFechas.add(getDPFechaDesde());
			panelFechas.add(getDPFechaHasta());
			
			JPanel panelFiltros = new JPanel();
			panelFiltros.setLayout(new FlowLayout());
			//panelFiltros.add(getCmbOrdenMovimientos());
			
			panel.add(panelProveedor);
			panel.add(panelFechas);
			panel.add(panelFiltros);
		
			panelCabecera.add(getPanelDatosProveedor(), new GridBagConstraints(0, 0, 1, 1, 0.4, 0, GridBagConstraints.WEST, GridBagConstraints.LINE_END, new Insets(15, 5, 15, 5), 0, 0));
			panelCabecera.add(panel, new GridBagConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.LINE_END, new Insets(15, 5, 15, 5), 0, 0));
		}
		return panelCabecera;
	}

	private JButton getBtnBuscar() {
		if (btnBuscar == null) {
			btnBuscar = new JButton("Buscar");
			btnBuscar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent evt) {
					if(getDPFechaDesde().getDate()==null){
						FWJOptionPane.showErrorMessage(JFrameVerMovimientosProveedor.this, "La 'fecha desde' ingresada no es válida", "Error");
						return;
					}
					if(getDPFechaHasta().getDate()==null){
						FWJOptionPane.showErrorMessage(JFrameVerMovimientosProveedor.this, "La 'fecha hasta' ingresada no es válida", "Error");
						return;
					}
					if (!getDPFechaDesde().getDate().after(getDPFechaHasta().getDate())) {
						if (getTxtBusquedaProveedor().getText().trim().length() > 0 && getProveedorElegido()!=null) {
							buscarMovimientos();
						} else {
							FWJOptionPane.showErrorMessage(JFrameVerMovimientosProveedor.this, "Debe ingresar un proveedor", "Error");
						}
					} else {
						FWJOptionPane.showErrorMessage(JFrameVerMovimientosProveedor.this, "La 'fecha desde' no debe ser posterior a la 'fecha hasta'", "Error");
					}
				}
			});
		}
		return btnBuscar;
	}

	private void buscarMovimientos() {
		GenericUtils.realizarOperacionConDialogoDeEspera("Buscando...", new BackgroundTask() {

			public void perform() {
				try{
					Proveedor proveedor = getProveedorElegido();
					if(proveedor != null){
						java.sql.Date fechaDesde = new java.sql.Date(getDPFechaDesde().getDate().getTime());
						java.sql.Date fechaHasta = DateUtil.getManiana(new java.sql.Date(getDPFechaHasta().getDate().getTime() + DateUtil.ONE_DAY));
						//BigDecimal transporteCuenta = getCuentaFacade().getTransporteCuentaProveedor(proveedor.getId(),fechaDesde);
						
						List<MovimientoCuenta> movs = null;
						if(getChkUltimosMovimientos().isSelected()){
							movs = getCuentaFacade().getMovimientosByIdProveedorYFecha(proveedor.getId(),null,null,true	/*,getCmbOrdenMovimientos().getSelectedItem().equals("MAS ANTIGUO PRIMERO")*/);
						}else{
							movs =getCuentaFacade().getMovimientosByIdProveedorYFecha(proveedor.getId(),fechaDesde,fechaHasta,false	/*,getCmbOrdenMovimientos().getSelectedItem().equals("MAS ANTIGUO PRIMERO")*/);
						}
						getPanelTablaMovimientos().getTabla().removeAllRows();
						if (movs != null && !movs.isEmpty()) {
							BigDecimal transporteCuenta = new BigDecimal(0d);
							List<MovimientoCuenta> movimientosTransporteCuenta = getCuentaFacade().getMovimientosTransporteCuentaProveedor(proveedor.getId(), new java.sql.Date(movs.get(0).getFechaHora().getTime()));
							transporteCuenta = calcularTransporte(movimientosTransporteCuenta, movs);
							filaMovimientoVisitor = getPanelTablaMovimientos().createVisitorFilaMovimientos(transporteCuenta);
							boolean habilitarBotonesImpresion = movs.size() > 0;
							getBtnExportarAExcel().setEnabled(habilitarBotonesImpresion);
							getBtnImprimirListado().setEnabled(habilitarBotonesImpresion);
							getBtnListadoPDF().setEnabled(habilitarBotonesImpresion);
							llenarTablaMovimientos(movs,transporteCuenta);
							setSaldoCuenta();
							getBtnAgregarRemitoCompraTela().setEnabled(true);
							Map<Integer, Color> mapaColores = filaMovimientoVisitor.getMapaColores();
							InfoCuentaTO infoCuentaTO = getCuentaFacade().getInfoOrdenDePagoYPagosRecibidos(proveedor.getId());
							pintarOrdenesDePago(mapaColores);
							pintarFacturasPagadas(mapaColores, infoCuentaTO);
							pintarOrdenesDePagoSecondPass(filaMovimientoVisitor.getRowsPagosSaldoAFavor());
						} else {
							getBtnAnular().setEnabled(false);
							getBtnCompletarDatosNotaDebitoCredito().setEnabled(false);
							getBtnConfirmar().setEnabled(false);
							getBtnEditar().setEnabled(false);
							getBtnEliminarFactura().setEnabled(false);
							getBtnExportarAExcel().setEnabled(false);
							getBtnImprimirListado().setEnabled(false);
							getBtnListadoPDF().setEnabled(false);
							getPanelTablaMovimientos().getTabla().removeAllRows();
							getTxtTotalCuenta().setText("");
							getBtnAgregarRemitoCompraTela().setEnabled(false);
							FWJOptionPane.showWarningMessage(JFrameVerMovimientosProveedor.this, "No se han encontrado resultados. Revise el criterio de búsqueda.", "Error");
						}
					}
				}catch(ValidacionException vle){
					FWJOptionPane.showErrorMessage(JFrameVerMovimientosProveedor.this, vle.getMensajeError(), "Error");
				}
			}
		});
	}

	private BigDecimal calcularTransporte(List<MovimientoCuenta> movimientosTransporteCuenta, List<MovimientoCuenta> movs) {
		BigDecimal transporteCuenta = new BigDecimal(0d);
		for(MovimientoCuenta movT : movimientosTransporteCuenta){
			if(!movs.contains(movT)){
				BigDecimal monto = movT.getMonto();
				transporteCuenta = transporteCuenta.add(monto);
			}
		}
		return transporteCuenta;
	}

	private void pintarOrdenesDePagoSecondPass(List<InfoSecondPass> rowsPagosSaldoAFavor) {
		FWJTable tablaMov = getPanelTablaMovimientos().getTabla();
		CellRenderer cellRenderer = (CellRenderer) tablaMov.getColumnModel().getColumn(0).getCellRenderer();
		for (InfoSecondPass isp : rowsPagosSaldoAFavor) {
			for (int i = isp.getFila() + 1; i < tablaMov.getRowCount(); i++) {
				if( tablaMov.getValueAt(i, 5) instanceof MovimientoCuenta){
					PintarRecibosSecondPassVisitor prspv = new PintarRecibosSecondPassVisitor(isp, i, cellRenderer);
					((MovimientoCuenta) tablaMov.getValueAt(i, 5)).aceptarVisitor(prspv);
				}
			}
		}
	}

	private void pintarFacturasPagadas(Map<Integer, Color> mapaColores, InfoCuentaTO infoCuentaTO) {
		FWJTable tabla = getPanelTablaMovimientos().getTabla();
		CellRenderer cellRenderer = (CellRenderer) getPanelTablaMovimientos().getTabla().getColumnModel().getColumn(0).getCellRenderer();
		PintarFilaReciboVisitor pfrv = new PintarFilaReciboVisitor(tabla, mapaColores, cellRenderer, infoCuentaTO);
		for (int i = 0; i < tabla.getRowCount(); i++) {
			if( tabla.getValueAt(i, 5) instanceof MovimientoCuenta){
				pfrv.setFilaActual(i);
				((MovimientoCuenta) tabla.getValueAt(i, 5)).aceptarVisitor(pfrv);
			}
		}
	}

	private void pintarOrdenesDePago(Map<Integer, Color> mapaColores) {
		FWJTable tabla = getPanelTablaMovimientos().getTabla();
		CellRenderer cellRenderer = (CellRenderer) getPanelTablaMovimientos().getTabla().getColumnModel().getColumn(0).getCellRenderer();
		cellRenderer.clear();
		PintarFilaReciboVisitor pfrv = new PintarFilaReciboVisitor(tabla, mapaColores, cellRenderer);
		for (int i = 0; i < tabla.getRowCount(); i++) {
			if( tabla.getValueAt(i, 5) instanceof MovimientoCuenta){
				pfrv.setFilaActual(i);
				((MovimientoCuenta) tabla.getValueAt(i, 5)).aceptarVisitor(pfrv);
			}
		}
	}

	private PanelDatePicker getDPFechaDesde() {
		if (txtFechaDesde == null) {
			txtFechaDesde = new PanelDatePicker();
			txtFechaDesde.setCaption("Fecha desde:");
			txtFechaDesde.setSelectedDate(DateUtil.restarMeses(DateUtil.getHoy(), 6));
		}
		return txtFechaDesde;
	}

	private PanelDatePicker getDPFechaHasta() {
		if (txtFechaHasta == null) {
			txtFechaHasta = new PanelDatePicker();
			txtFechaHasta.setCaption("Fecha hasta:");
		}
		return txtFechaHasta;
	}

	private FWJTextField getTxtBusquedaProveedor() {
		if (txtBusquedaProveedor == null) {
			txtBusquedaProveedor = new FWJTextField();
			txtBusquedaProveedor.setEditable(false);
			txtBusquedaProveedor.setPreferredSize(new Dimension(100, 20));
			txtBusquedaProveedor.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					getBtnBuscar().doClick();
				}
			});
		}
		return txtBusquedaProveedor;
	}

	private JPanel getPanelSur() {
		if (panelSur == null) {
			panelSur = new JPanel();
			panelSur.setLayout(new GridBagLayout());
			panelSur.add(getPanelAcciones(),GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5,5,5,5), 1, 1, 1, 1));
			JPanel panelTotal = new JPanel();
			panelTotal.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
			panelTotal.add(new JLabel("Total: "));
			panelTotal.add(getTxtTotalCuenta());
			panelSur.add(panelTotal, GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5,5,5,5), 1, 1, 1, 1));
		}
		return panelSur;
	}

	private JPanel getPanelAcciones() {
		if (panelAcciones == null) {
			panelAcciones = new JPanel();
			panelAcciones.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 2));
//			panelAcciones.add(getBtnAnular());
			panelAcciones.add(getBtnEliminarFactura());
			panelAcciones.add(getBtnConfirmar());
			panelAcciones.add(getBtnAgregarRemito());
			panelAcciones.add(getBtnAgregarRemitoVentaTela());
			panelAcciones.add(getBtnAgregarRemitoCompraTela());
			panelAcciones.add(getBtnAgregarRemitoSalidaProveedor());
			panelAcciones.add(getBtnAgregarFactura());
			panelAcciones.add(getBtnAgregarOrdenDePago());
			panelAcciones.add(getBtnAgregarNroReciboOrdenDePago());
			panelAcciones.add(getBtnCompletarDatosNotaDebitoCredito());
			panelAcciones.add(getBtnAgregarObservaciones());
			panelAcciones.add(getBtnEditar());
			panelAcciones.add(getBtnExportarAExcel());
			panelAcciones.add(getBtnImprimirListado());
			panelAcciones.add(getBtnListadoPDF());
			panelAcciones.add(getBtnSalir());
		}
		return panelAcciones;
	}

	private JButton getBtnSalir() {
		if (btnSalir == null) {
			btnSalir = BossEstilos.createButton("ar/com/textillevel/imagenes/b_exit.png", "ar/com/textillevel/imagenes/b_exit.png");
			btnSalir.setMnemonic(KeyEvent.VK_S);
			btnSalir.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					salir();
				}
			});
		}
		return btnSalir;
	}

	private FWJTextField getTxtTotalCuenta() {
		if (txtTotalCuenta == null) {
			txtTotalCuenta = new FWJTextField();
			txtTotalCuenta.setPreferredSize(new Dimension(100, 20));
			txtTotalCuenta.setEditable(false);
		}
		return txtTotalCuenta;
	}

	private CuentaFacadeRemote getCuentaFacade() {
		if (cuentaFacade == null) {
			cuentaFacade = GTLBeanFactory.getInstance().getBean2(CuentaFacadeRemote.class);
		}
		return cuentaFacade;
	}

	private void setSaldoCuenta() {
		CuentaProveedor cuenta = getCuentaFacade().getCuentaProveedorByIdProveedor(getProveedorElegido().getId());
		if (cuenta != null) {
			BigDecimal saldo = new BigDecimal(filaMovimientoVisitor.getSaldo());
			if (saldo.doubleValue() == 0) {
				getTxtTotalCuenta().setText(String.valueOf(saldo.doubleValue() + "0"));
			} else {
				getTxtTotalCuenta().setText(getDecimalFormat().format(saldo));
			}
			if (saldo.doubleValue() > 0) {
				getTxtTotalCuenta().setForeground(Color.GREEN.darker());
			} else if (saldo.doubleValue() < 0) {
				getTxtTotalCuenta().setForeground(Color.RED);
			} else {
				getTxtTotalCuenta().setForeground(Color.BLACK);
			}
		}
	}
	
	public JButton getBtnEliminarFactura() {
		if (btnEliminarFactura == null) {
			btnEliminarFactura = BossEstilos.createButton("ar/com/textillevel/imagenes/b_eliminar.png", "ar/com/textillevel/imagenes/b_eliminar_des.png"); 
			btnEliminarFactura.setToolTipText("Eliminar");
			btnEliminarFactura.setEnabled(false);
			btnEliminarFactura.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if (getPanelTablaMovimientos().getTabla().getSelectedRow() > -1) {
						if(FWJOptionPane.showQuestionMessage(JFrameVerMovimientosProveedor.this, "Esta seguro que desea eliminar?", "Pregunta")==FWJOptionPane.YES_OPTION){
							realizarAccionEliminar();
						}
					}
				}
			});
		}
		return btnEliminarFactura;
	}
	
	public JButton getBtnEditar() {
		if(btnEditar == null){
			btnEditar = BossEstilos.createButton("ar/com/textillevel/imagenes/b_modificar_fila.png", "ar/com/textillevel/imagenes/b_modificar_fila_des.png");
			btnEditar.setEnabled(false);
			btnEditar.setToolTipText("Editar");
			btnEditar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int filaSeleccionada = getPanelTablaMovimientos().getTabla().getSelectedRow();
					if (filaSeleccionada > -1) {
						if(!GTLGlobalCache.getInstance().getUsuarioSistema().getPerfil().getIsAdmin()){
							JDialogPasswordInput jDialogPasswordInput = new JDialogPasswordInput(JFrameVerMovimientosProveedor.this,"Editar documento");
							if (jDialogPasswordInput.isAcepto()) {
								String pass = new String(jDialogPasswordInput.getPassword());
								UsuarioSistema usrAdmin = GTLBeanFactory.getInstance().getBean2(UsuarioSistemaFacadeRemote.class).esPasswordDeAdministrador(pass);
								if (usrAdmin != null) {
									if(getUsuarioAdministrador()==null){
										setUsuarioAdministrador(usrAdmin);
									}	
									EditarDocumentoProveedorVisitor racv = new EditarDocumentoProveedorVisitor(JFrameVerMovimientosProveedor.this);
									((MovimientoCuenta) getPanelTablaMovimientos().getTabla().getValueAt(filaSeleccionada, PanelTablaMovimientos.COL_OBJ)).aceptarVisitor(racv);
								} else {
									FWJOptionPane.showErrorMessage(JFrameVerMovimientosProveedor.this, "La clave ingresada no peternece a un usuario administrador", "Error");
								}
							}
						}else{
							setUsuarioAdministrador(GTLGlobalCache.getInstance().getUsuarioSistema());
							EditarDocumentoProveedorVisitor racv = new EditarDocumentoProveedorVisitor(JFrameVerMovimientosProveedor.this);
							((MovimientoCuenta) getPanelTablaMovimientos().getTabla().getValueAt(filaSeleccionada, PanelTablaMovimientos.COL_OBJ)).aceptarVisitor(racv);
						}
						buscarMovimientos();
					}
				}
			});
		}
		return btnEditar;
	}

	
	public JButton getBtnAgregarObservaciones() {
		if(btnAgregarObservaciones == null){
			btnAgregarObservaciones = BossEstilos.createButton("ar/com/textillevel/imagenes/b_obs.png", "ar/com/textillevel/imagenes/b_obs_des.png");
			btnAgregarObservaciones.setEnabled(false);
			btnAgregarObservaciones.setToolTipText("Agregar observaciones");
			btnAgregarObservaciones.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int fila = getPanelTablaMovimientos().getTabla().getSelectedRow();
					if (fila > -1) {
						MovimientoCuenta mov = getPanelTablaMovimientos().getElemento(fila);
						String observaciones = JOptionPane.showInputDialog(JFrameVerMovimientosProveedor.this, "Observaciones", mov.getObservaciones());
						if(observaciones!=null){
							mov.setObservaciones(observaciones);
							cuentaFacade.actualizarMovimiento(mov);
							btnAgregarObservaciones.setEnabled(false);
							buscarMovimientos();
						}
					}
				}
			});
		}
		return btnAgregarObservaciones;
	}

	public JButton getBtnAnular() {
		if (btnAnular == null) {
			btnAnular = BossEstilos.createButton("ar/com/textillevel/imagenes/b_anular_recibo.png", "ar/com/textillevel/imagenes/b_anular_recibo_des.png");
			btnAnular.setEnabled(false);
			btnAnular.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if (getPanelTablaMovimientos().getTabla().getSelectedRow() > -1) {
						realizarAccionAnular();
					}
				}
			});
		}
		return btnAnular;
	}

	public JButton getBtnConfirmar() {
		if (btnConfirmar == null) {
			btnConfirmar = BossEstilos.createButton("ar/com/textillevel/imagenes/b_cancelar_anulacion_recibo.png", "ar/com/textillevel/imagenes/b_cancelar_anulacion_recibo_des.png"); 
			btnConfirmar.setToolTipText("Confirmar");
			btnConfirmar.setEnabled(false);
			btnConfirmar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if (getPanelTablaMovimientos().getTabla().getSelectedRow() > -1) {
						realizarAccionConfirmar();
					}
				}
			});
		}
		return btnConfirmar;
	}

	private void realizarAccionConfirmar() {
		if(!GTLGlobalCache.getInstance().getUsuarioSistema().getPerfil().getIsAdmin()){
			JDialogPasswordInput jDialogPasswordInput = new JDialogPasswordInput(JFrameVerMovimientosProveedor.this,"Confirmar movimiento");
			if (jDialogPasswordInput.isAcepto()) {
				String pass = new String(jDialogPasswordInput.getPassword());
				UsuarioSistema usrAdmin = GTLBeanFactory.getInstance().getBean2(UsuarioSistemaFacadeRemote.class).esPasswordDeAdministrador(pass);
				if (usrAdmin != null) {
					if(getUsuarioAdministrador()==null){
						setUsuarioAdministrador(usrAdmin);
					}
					AccionConfirmarCuentaVisitor racv = new AccionConfirmarCuentaVisitor(JFrameVerMovimientosProveedor.this);
					((MovimientoCuenta) getPanelTablaMovimientos().getTabla().getValueAt(getPanelTablaMovimientos().getTabla().getSelectedRow(), PanelTablaMovimientos.COL_OBJ)).aceptarVisitor(racv);
				} else {
					FWJOptionPane.showErrorMessage(JFrameVerMovimientosProveedor.this, "La clave ingresada no peternece a un usuario administrador", "Error");
				}
			}
		}else{
			setUsuarioAdministrador(GTLGlobalCache.getInstance().getUsuarioSistema());
			AccionConfirmarCuentaVisitor racv = new AccionConfirmarCuentaVisitor(JFrameVerMovimientosProveedor.this);
			((MovimientoCuenta) getPanelTablaMovimientos().getTabla().getValueAt(getPanelTablaMovimientos().getTabla().getSelectedRow(), PanelTablaMovimientos.COL_OBJ)).aceptarVisitor(racv);
		}
	}

	private void realizarAccionAnular() {
		if(!GTLGlobalCache.getInstance().getUsuarioSistema().getPerfil().getIsAdmin()){
			JDialogPasswordInput jDialogPasswordInput = new JDialogPasswordInput(JFrameVerMovimientosProveedor.this,"Anular movimiento");
			if (jDialogPasswordInput.isAcepto()) {
				String pass = new String(jDialogPasswordInput.getPassword());
				UsuarioSistema usrAdmin = GTLBeanFactory.getInstance().getBean2(UsuarioSistemaFacadeRemote.class).esPasswordDeAdministrador(pass);
				if (usrAdmin != null) {
					if(getUsuarioAdministrador()==null){
						setUsuarioAdministrador(usrAdmin);
					}
					AccionAnularCuentaVisitor racv = new AccionAnularCuentaVisitor(JFrameVerMovimientosProveedor.this);
					((MovimientoCuenta) getPanelTablaMovimientos().getTabla().getValueAt(getPanelTablaMovimientos().getTabla().getSelectedRow(), PanelTablaMovimientos.COL_OBJ)).aceptarVisitor(racv);
				} else {
					FWJOptionPane.showErrorMessage(JFrameVerMovimientosProveedor.this, "La clave ingresada no peternece a un usuario administrador", "Error");
				}
			}
		}else{
			setUsuarioAdministrador(GTLGlobalCache.getInstance().getUsuarioSistema());
			AccionConfirmarCuentaVisitor racv = new AccionConfirmarCuentaVisitor(JFrameVerMovimientosProveedor.this);
			((MovimientoCuenta) getPanelTablaMovimientos().getTabla().getValueAt(getPanelTablaMovimientos().getTabla().getSelectedRow(), PanelTablaMovimientos.COL_OBJ)).aceptarVisitor(racv);
		}
	}
	
	private void realizarAccionEliminar() {
		MovimientoCuenta movimientoCuenta = (MovimientoCuenta) getPanelTablaMovimientos().getTabla().getValueAt(getPanelTablaMovimientos().getTabla().getSelectedRow(), PanelTablaMovimientos.COL_OBJ);
		/*boolean clave = false;
		if(movimientoCuenta instanceof MovimientoHaberProveedor){
			MovimientoHaberProveedor m = (MovimientoHaberProveedor)movimientoCuenta;
			if(m.getOrdenDePago()!=null && m.getOrdenDePago().getNroReciboProveedor()!=null){
				clave = true;
			}
		}
		if(clave){*/
		if(!GTLGlobalCache.getInstance().getUsuarioSistema().getPerfil().getIsAdmin()){
			JDialogPasswordInput jDialogPasswordInput = new JDialogPasswordInput(JFrameVerMovimientosProveedor.this, "Eliminar movimiento");
			if (jDialogPasswordInput.isAcepto()) {
				String pass = new String(jDialogPasswordInput.getPassword());
				UsuarioSistema usrAdmin = GTLBeanFactory.getInstance().getBean2(UsuarioSistemaFacadeRemote.class).esPasswordDeAdministrador(pass);
				if (usrAdmin != null) {
					if(getUsuarioAdministrador()==null){
						setUsuarioAdministrador(usrAdmin);
					}
					AccionEliminarFacturaCuentaVisitor racv = new AccionEliminarFacturaCuentaVisitor(JFrameVerMovimientosProveedor.this);
					movimientoCuenta.aceptarVisitor(racv);
				} else {
					FWJOptionPane.showErrorMessage(JFrameVerMovimientosProveedor.this, "La clave ingresada no peternece a un usuario administrador", "Error");
				}
			}
		}else{
			setUsuarioAdministrador(GTLGlobalCache.getInstance().getUsuarioSistema());
			AccionEliminarFacturaCuentaVisitor racv = new AccionEliminarFacturaCuentaVisitor(JFrameVerMovimientosProveedor.this);
			((MovimientoCuenta) getPanelTablaMovimientos().getTabla().getValueAt(getPanelTablaMovimientos().getTabla().getSelectedRow(), PanelTablaMovimientos.COL_OBJ)).aceptarVisitor(racv);
		}
		/*}else{
			AccionEliminarFacturaCuentaVisitor racv = new AccionEliminarFacturaCuentaVisitor(JFrameVerMovimientosProveedor.this);
			movimientoCuenta.aceptarVisitor(racv);
		}*/
	}

	public void editarFactura(FacturaProveedor fp) {
		try {
			getFacturaFacade().checkEliminacionOrEdicionFacturaProveedor(fp);
			OperacionSobreFacturaProveedorHandler operacionHandler = new OperacionSobreFacturaProveedorHandler(JFrameVerMovimientosProveedor.this, fp, false); 
			operacionHandler.showFacturaProveedorDialog();
		} catch (ValidacionException e) {
			FWJOptionPane.showErrorMessage(JFrameVerMovimientosProveedor.this, StringW.wordWrap(e.getMensajeError()), "Imposible Editar");
			return;
		}
	}
	
	public void editarOrdenDePago(OrdenDePago orden){
		try{
			orden = GTLBeanFactory.getInstance().getBean(OrdenDePagoFacadeRemote.class).getOrdenDePagoByNroOrdenEager(orden.getNroOrden());
			JDialogCargaOrdenDePago dialog = new JDialogCargaOrdenDePago(JFrameVerMovimientosProveedor.this, orden,false);
			dialog.setVisible(true);
			buscarMovimientos();
		}catch(FWException cle){
			BossError.gestionarError(cle);
			return;
		}
	}

	public void eliminarOrdenDePago(OrdenDePago ordenDePago) {
		try {
			getOrdenDePagoFacade().borrarOrdenDePago(ordenDePago, getUsuarioAdministrador()!=null?getUsuarioAdministrador().getUsrName():GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName());
			buscarMovimientos();
		} catch (FWException e) {
			FWJOptionPane.showErrorMessage(this, StringW.wordWrap(e.getMensajeError()), "Error");
		}
	}

	public void eliminarFactura(FacturaProveedor facturaProveedor) {
		try {
			getFacturaFacade().borrarFactura(facturaProveedor, getUsuarioAdministrador().getUsrName());
			buscarMovimientos();
		} catch (ValidacionException e) {
			FWJOptionPane.showErrorMessage(this, StringW.wordWrap(e.getMensajeError()), "Error");
		}
	}

	public void eliminarNotaCreditoProveedor(NotaCreditoProveedor notaCreditoProveedor) {
		try {
			getCorreccionFacade().borrarCorreccionFacturaProveedor(notaCreditoProveedor, getUsuarioAdministrador().getUsrName());
			buscarMovimientos();
		} catch (ValidacionException e) {
			FWJOptionPane.showErrorMessage(this, StringW.wordWrap(e.getMensajeError()), "Error");
		}
	}

	public void eliminarNotaDebitoProveedor(NotaDebitoProveedor notaDebitoProveedor) {
		try {
			boolean esRechazoCheque = verSiEsNotaDebitoPorRechazoDeCheque(notaDebitoProveedor);
			if(esRechazoCheque){
				if(FWJOptionPane.showQuestionMessage(this, StringW.wordWrap("Esta es una nota de débito por rechazo de cheque. Volver el cheque implicado al estado anterior y se eliminará la nota de débito del cliente. Desea continuar?"), "Pregunta") == FWJOptionPane.NO_OPTION){
					return;
				}
			}
			getCorreccionFacade().borrarCorreccionFacturaProveedor(notaDebitoProveedor, getUsuarioAdministrador().getUsrName());
			buscarMovimientos();
		} catch (ValidacionException e) {
			FWJOptionPane.showErrorMessage(this, StringW.wordWrap(e.getMensajeError()), "Error");
		}
	}

	private boolean verSiEsNotaDebitoPorRechazoDeCheque(NotaDebitoProveedor notaDebitoProveedor) {
		CorreccionFacturaProveedor correccionFacturaByIdEager = getCorreccionFacade().getCorreccionFacturaByIdEager(notaDebitoProveedor.getId());
		for(ItemCorreccionFacturaProveedor i : correccionFacturaByIdEager.getItemsCorreccion()){
			if(i instanceof ItemCorreccionCheque){
				return true;
			}
		}
		return false;
	}

	public void anularFactura(FacturaProveedor factura) {
		
	}

	public void confirmarFactura(FacturaProveedor factura) {
		getFacturaFacade().confirmarFactura(factura,getUsuarioAdministrador().getUsrName());
		buscarMovimientos();
	}
	
	public void confirmarNotaDebito(NotaDebitoProveedor nd) {
		getCorreccionFacade().confirmarCorreccion(nd,getUsuarioAdministrador().getUsrName() );
		buscarMovimientos();
	}
	
	public void confirmarNotaDeCredito(NotaCreditoProveedor nc) {
		getCorreccionFacade().confirmarCorreccion(nc,getUsuarioAdministrador().getUsrName() );
		buscarMovimientos();
	}

	public void confirmarOrdenDePago(OrdenDePago op) {
		getOrdenDePagoFacade().confirmarOrden(op,getUsuarioAdministrador().getUsrName());
		buscarMovimientos();
	}
	
	public void anularRecibo(Recibo recibo) {
		
	}

	private DecimalFormat getDecimalFormat() {
		DecimalFormat df = new DecimalFormat("#,###.00");
		df.setMaximumFractionDigits(2);
		df.setGroupingUsed(true);
		return df;
	}

	private JButton getBtnExportarAExcel() {
		if (btnExportarAExcel == null) {
			btnExportarAExcel = BossEstilos.createButton("ar/com/fwcommon/imagenes/b_exportar_excel.png", "ar/com/fwcommon/imagenes/b_exportar_excel_des.png"); 
			btnExportarAExcel.setToolTipText("Exportar a Excel");
			btnExportarAExcel.setEnabled(false);
			btnExportarAExcel.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if (getPanelTablaMovimientos().getTabla().getRowCount() > 0) {
						FWJTable tabla = getPanelTablaMovimientos().getTabla();
						mostrarFileChooser("Listado de Movimientos - Cliente Nro - " + getTxtBusquedaProveedor().getText(), EXTENSION_EXCEL);
						File archivoIngresado = FWFileSelector.obtenerArchivo(FWFileSelector.SAVE, FWFileSelector.FILES_ONLY, new FiltroArchivosExcel(), null);
						if (archivoIngresado != null) {
							if (!archivoIngresado.getAbsolutePath().toLowerCase().endsWith(EXTENSION_EXCEL)) {
								archivoIngresado = new File(archivoIngresado.getAbsolutePath().concat(EXTENSION_EXCEL));
							}
							GenericUtils.exportarAExcel(tabla, "  Saldo: " + getTxtTotalCuenta().getText(), "  " + getLblNombre().getText() + " - " + getLblDireccion().getText() + " - "
									+ getLblCuit().getText() + " - " + getLblTelefono().getText(), null, archivoIngresado.getAbsolutePath(), System.getProperty("intercalarColoresFilas") != null
									&& System.getProperty("intercalarColoresFilas").equals(String.valueOf(true)));
						}
					}
				}
			});
		}
		return btnExportarAExcel;
	}

	private static class FiltroArchivosExcel extends FileFilter {

		@Override
		public boolean accept(File archivo) {
			return archivo.getName().endsWith(EXTENSION_EXCEL) || archivo.isDirectory();
		}

		@Override
		public String getDescription() {
			return EXTENSION_EXCEL;
		}
	}

	private static class FiltroArchivosPDF extends FileFilter {

		@Override
		public boolean accept(File archivo) {
			return archivo.getName().endsWith(EXTENSION_PDF) || archivo.isDirectory();
		}

		@Override
		public String getDescription() {
			return EXTENSION_PDF;
		}
	}

	private JButton getBtnImprimirListado() {
		if (btnImprimirListado == null) {
			btnImprimirListado = BossEstilos.createButton("ar/com/textillevel/imagenes/b_imp_listado.png", "ar/com/textillevel/imagenes/b_imp_listado_des.png"); 
			btnImprimirListado.setToolTipText("Imprimir listado");	
			btnImprimirListado.setEnabled(false);
			btnImprimirListado.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					// boolean preview =
					// CLJOptionPane.showQuestionMessage(JFrameVerMovimientosProveedor.this,
					// "Desea previsualizar la impresión?",
					// "Pregunta")==CLJOptionPane.YES_OPTION;
					JasperHelper.imprimirListado(getPanelTablaMovimientos().getTabla(), "  Saldo: " + getTxtTotalCuenta().getText(), "  " + getLblNombre().getText() + " - "
							+ getLblDireccion().getText() + " - " + getLblCuit().getText() + " - " + getLblTelefono().getText(), null, false);
				}
			});
		}
		return btnImprimirListado;
	}

	private JButton getBtnListadoPDF() {
		if (btnListadoPDF == null) {
			btnListadoPDF = BossEstilos.createButton("ar/com/textillevel/imagenes/btn_pdf.png", "ar/com/textillevel/imagenes/btn_pdf_des.png");
			btnListadoPDF.setToolTipText("Exportar a PDF");
			btnListadoPDF.setEnabled(false);
			btnListadoPDF.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if (getPanelTablaMovimientos().getTabla().getRowCount() > 0) {
						FWJTable tabla = getPanelTablaMovimientos().getTabla();
						mostrarFileChooser("Listado de Movimientos - Cliente Nro - " + getTxtBusquedaProveedor().getText(), EXTENSION_PDF);
						File archivoIngresado = FWFileSelector.obtenerArchivo(FWFileSelector.SAVE, FWFileSelector.FILES_ONLY, new FiltroArchivosPDF(), null);
						if (archivoIngresado != null) {
							if (!archivoIngresado.getAbsolutePath().toLowerCase().endsWith(EXTENSION_PDF)) {
								archivoIngresado = new File(archivoIngresado.getAbsolutePath().concat(EXTENSION_PDF));
							}
							JasperHelper.listadoAPDF(tabla, "  Saldo: " + getTxtTotalCuenta().getText(), "  " + getLblNombre().getText() + " - " + getLblDireccion().getText() + " - "
									+ getLblCuit().getText() + " - " + getLblTelefono().getText(), null, false, archivoIngresado.getAbsolutePath());
						}
					}
				}
			});
		}
		return btnListadoPDF;
	}

	private void mostrarFileChooser(String nombreArchivo, String extension) {
		File directorioCorriente = FWFileSelector.getLastSelectedFile();
		if (directorioCorriente != null) {
			String nombreSugerido = null;
			try {
				if (directorioCorriente.isFile()) {
					nombreSugerido = directorioCorriente.getCanonicalPath();
				} else {
					nombreSugerido = directorioCorriente.getCanonicalPath() + File.separator + nombreArchivo;
				}
			} catch (IOException e1) {
				FWJOptionPane.showErrorMessage(JFrameVerMovimientosProveedor.this, "Se ha producido un error al guardar el archivo.\n" + e1.getMessage(), "Error");
				return;
			}
			File archivoSugerido = new File(nombreSugerido.endsWith(extension) ? nombreSugerido : nombreSugerido.concat(extension));
			FWFileSelector.setLastSelectedFile(archivoSugerido);
		}
	}

	private JPanel getPanelDatosProveedor() {
		if (panelDatosUsuario == null) {
			panelDatosUsuario = new JPanel();
			panelDatosUsuario.setLayout(new VerticalFlowLayout(VerticalFlowLayout.TOP, 5, 2));
			panelDatosUsuario.add(getLblNombre());
			panelDatosUsuario.add(getLblDireccion());
			panelDatosUsuario.add(getLblCuit());
			panelDatosUsuario.add(getLblTelefono());
		}
		return panelDatosUsuario;
	}

	private JLabel getLblNombre() {
		if (lblNombre == null) {
			lblNombre = new JLabel();
			Font fuente = lblNombre.getFont();
			lblNombre.setFont(new Font(fuente.getName(), Font.BOLD, fuente.getSize() + 3));
		}
		return lblNombre;
	}

	private JLabel getLblDireccion() {
		if (lblDireccion == null) {
			lblDireccion = new JLabel();
			Font fuente = lblDireccion.getFont();
			lblDireccion.setFont(new Font(fuente.getName(), Font.BOLD, fuente.getSize() + 3));
		}
		return lblDireccion;
	}

	private JLabel getLblCuit() {
		if (lblCuit == null) {
			lblCuit = new JLabel();
			Font fuente = lblCuit.getFont();
			lblCuit.setFont(new Font(fuente.getName(), Font.BOLD, fuente.getSize() + 3));
		}
		return lblCuit;
	}
	
	private JLabel getLblTelefono() {
		if (lblTelefono == null) {
			lblTelefono = new JLabel();
			Font fuente = lblTelefono.getFont();
			lblTelefono.setFont(new Font(fuente.getName(), Font.BOLD, fuente.getSize() + 3));
		}
		return lblTelefono;
	}

//	private JCheckBox getChkIncluirFacturasPagadas() {
//		if(chkIncluirFacturasPagadas == null){
//			chkIncluirFacturasPagadas = new JCheckBox("Incluir facturas pagadas");
//			chkIncluirFacturasPagadas.setSelected(true);
//		}
//		return chkIncluirFacturasPagadas;
//	}
	
	public Proveedor getProveedorElegido() {
		return proveedorElegido;
	}

	
	public void setProveedorElegido(Proveedor proveedorElegido) {
		this.proveedorElegido = proveedorElegido;
	}
	
	private LinkableLabel getLblElegirProveedor() {
		if(lblElegirProveedor == null){
			lblElegirProveedor = new LinkableLabel("Elegir proveeedor") {

				private static final long serialVersionUID = -768672582828765018L;

				@Override
				public void labelClickeada(MouseEvent e) {
					JDialogSeleccionarProveedor dialogSeleccionarProveedor = new JDialogSeleccionarProveedor(JFrameVerMovimientosProveedor.this);
					GuiUtil.centrar(dialogSeleccionarProveedor);
					dialogSeleccionarProveedor.setVisible(true);
					Proveedor proveedorElegido = dialogSeleccionarProveedor.getProveedor();
					if (proveedorElegido != null) {
						getTxtBusquedaProveedor().setText(proveedorElegido.getNombreCorto());
						setProveedorElegido(proveedorElegido);
						getLblNombre().setText(proveedorElegido.getRazonSocial());
						getLblDireccion().setText(proveedorElegido.getDireccionFiscal().getDireccion() + " " + proveedorElegido.getDireccionFiscal().getLocalidad().getNombreLocalidad());
						getLblCuit().setText(proveedorElegido.getCuit());
						getLblTelefono().setText(proveedorElegido.getTelefono());
						getBtnBuscar().doClick();
					}
				};
			};
		}
		return lblElegirProveedor;
	}

	public CorreccionFacturaProveedorFacadeRemote getCorreccionFacade() {
		if(correccionFacade == null){
			correccionFacade = GTLBeanFactory.getInstance().getBean2(CorreccionFacturaProveedorFacadeRemote.class);
		}
		return correccionFacade;
	}
	
	public OrdenDePagoFacadeRemote getOrdenDePagoFacade() {
		if(ordenDePagoFacade == null){
			ordenDePagoFacade = GTLBeanFactory.getInstance().getBean2(OrdenDePagoFacadeRemote.class);
		}
		return ordenDePagoFacade;
	}

	public FacturaProveedorFacadeRemote getFacturaFacade() {
		if(facturaFacade == null){
			facturaFacade = GTLBeanFactory.getInstance().getBean2(FacturaProveedorFacadeRemote.class);
		}
		return facturaFacade;
	}

	
	public JButton getBtnAgregarNroReciboOrdenDePago() {
		if (btnAgregarNroReciboOrdenDePago == null) {
			btnAgregarNroReciboOrdenDePago = BossEstilos.createButton("ar/com/textillevel/imagenes/b_ingresar_nro_recibo.png", "ar/com/textillevel/imagenes/b_ingresar_nro_recibo_des.png");
			btnAgregarNroReciboOrdenDePago.setToolTipText("Agregar Nº Recibo a Orden de pago");
			btnAgregarNroReciboOrdenDePago.setEnabled(false);
			btnAgregarNroReciboOrdenDePago.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if (getPanelTablaMovimientos().getTabla().getSelectedRow() > -1) {
						ingresarNroRecibo();
					}
				}
			});
		}
		return btnAgregarNroReciboOrdenDePago;
	}
	
	private JButton getBtnAgregarOrdenDePago() {
		if(btnAgregarOrdenDePago == null){
			btnAgregarOrdenDePago = BossEstilos.createButton("ar/com/fwcommon/imagenes/b_agregar.png", "ar/com/fwcommon/imagenes/b_agregar.png");
			btnAgregarOrdenDePago.setEnabled(true);
			btnAgregarOrdenDePago.setToolTipText("Agregar órden de pago");
			btnAgregarOrdenDePago.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					new JDialogCargaOrdenDePago(JFrameVerMovimientosProveedor.this, getProveedorElegido()).setVisible(true);
					buscarMovimientos();
				}
			});
		}
		return btnAgregarOrdenDePago;
	}
	
	private JButton getBtnAgregarFactura() {
		if(btnAgregarFactura== null){
			btnAgregarFactura = BossEstilos.createButton("ar/com/textillevel/imagenes/b_agregar_factura.png", "ar/com/textillevel/imagenes/b_agregar_factura_des.png");
			btnAgregarFactura.setToolTipText("Agregar factura");
			btnAgregarFactura.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ETipoFacturaProveedor opcion = (ETipoFacturaProveedor) JOptionPane.showInputDialog(null, "Seleccione el tipo de remito:", "Lista de opciones", JOptionPane.INFORMATION_MESSAGE, null, ETipoFacturaProveedor.values(),ETipoFacturaProveedor.NORMAL);
					if(opcion!=null){
						switch(opcion){
							case NORMAL:{
								List<RemitoEntradaProveedor> remitosNoAsocByProveedor = GTLBeanFactory.getInstance().getBean2(RemitoEntradaProveedorFacadeRemote.class).getRemitosNoAsocByProveedor(getProveedorElegido());
								JDialogSeleccionarRemitoEntradaProveedor jdsrep = new JDialogSeleccionarRemitoEntradaProveedor(JFrameVerMovimientosProveedor.this, getProveedorElegido(), remitosNoAsocByProveedor); 
								GuiUtil.centrar(jdsrep);
								jdsrep.setVisible(true);
								List<RemitoEntradaProveedor> remitoEntradaList = toEager(jdsrep.getRemitoEntradaList(), getProveedorElegido());
								if(!remitoEntradaList.isEmpty()) {
									FacturaProveedor fp = new FacturaProveedor();
									fp.setTipoFacturaProveedor(ETipoFacturaProveedor.NORMAL);
									fp.setUsuarioCreador(GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName());
									fp.setProveedor(getProveedorElegido());
									fp.getRemitoList().addAll(remitoEntradaList);
									JDialogCargarFacturaProveedor jdcfp = new JDialogCargarFacturaProveedor(JFrameVerMovimientosProveedor.this, fp, false, remitosNoAsocByProveedor);
									GuiUtil.centrar(jdcfp);
									jdcfp.setVisible(true);
								}

								break;
							}
							case SIN_REMITO:{
								FacturaProveedor fp = new FacturaProveedor();
								fp.setTipoFacturaProveedor(ETipoFacturaProveedor.SIN_REMITO);			
								fp.setProveedor(getProveedorElegido());
								fp.setUsuarioCreador(GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName());			
								JDialogCargarFacturaProveedor jdcfp = new JDialogCargarFacturaProveedor(JFrameVerMovimientosProveedor.this, fp, false, new ArrayList<RemitoEntradaProveedor>());
								GuiUtil.centrar(jdcfp);
								jdcfp.setVisible(true);
								break;
							}
							case SERVICIO:{
								FacturaProveedor fp = new FacturaProveedor();
								fp.setTipoFacturaProveedor(ETipoFacturaProveedor.SERVICIO);			
								fp.setProveedor(getProveedorElegido());
								fp.setUsuarioCreador(GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName());			
								JDialogCargarFacturaServicioProveedor jdcfp = new JDialogCargarFacturaServicioProveedor(JFrameVerMovimientosProveedor.this, fp, false);
								GuiUtil.centrar(jdcfp);
								jdcfp.setVisible(true);
								break;
							}
						}
					}
				}
				
				private List<RemitoEntradaProveedor> toEager(List<RemitoEntradaProveedor> remitoEntradaList, Proveedor proveedorSel) {
					List<RemitoEntradaProveedor> remitoListEager = new ArrayList<RemitoEntradaProveedor>();
					RemitoEntradaProveedorFacadeRemote repfr = GTLBeanFactory.getInstance().getBean2(RemitoEntradaProveedorFacadeRemote.class);
					for(RemitoEntradaProveedor rep : remitoEntradaList) {
						RemitoEntradaProveedor repEager = repfr.getByIdEager(rep.getId());
						repEager.setProveedor(proveedorSel);
						remitoListEager.add(repEager);
						
					}
					return remitoListEager;
				}
			});
		}
		return btnAgregarFactura;
	}

	
	private JButton getBtnAgregarRemito() {
		if(btnAgregarRemito == null){
			btnAgregarRemito = BossEstilos.createButton("ar/com/textillevel/imagenes/b_rechazar_cheque.png", "ar/com/textillevel/imagenes/b_rechazar_cheque_des.png");
			btnAgregarRemito.setToolTipText("Agregar remito");
			btnAgregarRemito.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ETipoRemitoProveedor opcion = (ETipoRemitoProveedor) JOptionPane.showInputDialog(null, "Seleccione el tipo de remito:", "Lista de opciones", JOptionPane.INFORMATION_MESSAGE, null, ETipoRemitoProveedor.values(),ETipoRemitoProveedor.REMITO_ENTRADA);
					if(opcion!=null){
						switch(opcion){
							case REMITO_ENTRADA:{
								JDialogAgregarRemitoEntradaProveedor jdarep = new JDialogAgregarRemitoEntradaProveedor(JFrameVerMovimientosProveedor.this, false,getProveedorElegido());
								jdarep.setVisible(true);
								RemitoEntradaProveedor remitoEntrada = jdarep.getRemitoEntrada();
								if(remitoEntrada != null) {
									if(FWJOptionPane.showQuestionMessage(JFrameVerMovimientosProveedor.this, "¿Desea Cargar la Factura del Proveedor?", "Confirmación") == FWJOptionPane.YES_OPTION) {
										RemitoEntradaProveedorFacadeRemote repfr = GTLBeanFactory.getInstance().getBean2(RemitoEntradaProveedorFacadeRemote.class);
										remitoEntrada = repfr.getByIdEager(remitoEntrada.getId());
										FacturaProveedor fp = new FacturaProveedor();
										fp.setTipoFacturaProveedor(ETipoFacturaProveedor.NORMAL);					
										fp.setUsuarioCreador(GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName());
										fp.setProveedor(getProveedorElegido());
										fp.getRemitoList().add(remitoEntrada);
										JDialogCargarFacturaProveedor jdcfp = new JDialogCargarFacturaProveedor(JFrameVerMovimientosProveedor.this, fp, false, new ArrayList<RemitoEntradaProveedor>());
										GuiUtil.centrar(jdcfp);
										jdcfp.setVisible(true);
									}
								}

								break;
							}
							case REMITO_SALIDA:{
								RemitoSalida remitoSalidaProveedor = new RemitoSalida();
								remitoSalidaProveedor.setTipoRemitoSalida(ETipoRemitoSalida.PROVEEDOR);
								remitoSalidaProveedor.setNroFactura(0);
								remitoSalidaProveedor.setNroOrden(0);
								remitoSalidaProveedor.setPesoTotal(new BigDecimal(0));
								remitoSalidaProveedor.setPorcentajeMerma(new BigDecimal(0));
								remitoSalidaProveedor.setProveedor(getProveedorElegido());
								JDialogAgregarRemitoSalidaProveedor jdarsp = new JDialogAgregarRemitoSalidaProveedor(JFrameVerMovimientosProveedor.this, false, remitoSalidaProveedor);
								jdarsp.setVisible(true);
								break;
							}
						}
					}
				}
			});
		}
		return btnAgregarRemito;
	}

	private enum ETipoRemitoProveedor{
		REMITO_ENTRADA("Remito de entrada"),
		REMITO_SALIDA("Remito de salida");
		
		private ETipoRemitoProveedor(String tipo){
			this.tipo = tipo;
		}
		
		private String tipo;

		@Override
		public String toString() {
			return tipo;
		}
	}
	
	private void ingresarNroRecibo() {
		boolean ok = false;
		do {
			String input = JOptionPane.showInputDialog(JFrameVerMovimientosProveedor.this, "Ingrese el número de recibo: ", "Ingresar remito", JOptionPane.INFORMATION_MESSAGE);
			if(input == null){
				break;
			}
			if (input.trim().length()==0 || !GenericUtils.esNumerico(input)) {
				FWJOptionPane.showErrorMessage(JFrameVerMovimientosProveedor.this, "Ingreso incorrecto", "error");
			} else {
				ok = true;
				Integer nroRecibo = Integer.valueOf(input.trim());
				FWJTable tabla = getPanelTablaMovimientos().getTabla();
				OrdenDePago op = ((MovimientoHaberProveedor)tabla.getValueAt(tabla.getSelectedRow(),PanelTablaMovimientos.COL_OBJ )).getOrdenDePago();
				op.setNroReciboProveedor(nroRecibo);
				getOrdenDePagoFacade().actualizarOrden(op, GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName());
				buscarMovimientos();
			}
		} while (!ok);
	}

	public JButton getBtnCompletarDatosNotaDebitoCredito() {
		if(btnCompletarDatosNotaDebitoCredito == null){
			btnCompletarDatosNotaDebitoCredito = BossEstilos.createButton("ar/com/textillevel/imagenes/b_ingresar_nro_recibo.png", "ar/com/textillevel/imagenes/b_ingresar_nro_recibo_des.png"); 
			btnCompletarDatosNotaDebitoCredito.setToolTipText("Completar datos ND/NC");
			btnCompletarDatosNotaDebitoCredito.setEnabled(false);
			btnCompletarDatosNotaDebitoCredito.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if (getPanelTablaMovimientos().getTabla().getSelectedRow() > -1) {
						completarDatosNotaDebitoCredito();
					}
				}
			});
		}
		return btnCompletarDatosNotaDebitoCredito;
	}

	private void completarDatosNotaDebitoCredito() {
		FWJTable tabla = getPanelTablaMovimientos().getTabla();
		MovimientoCuenta mc = (MovimientoCuenta)tabla.getValueAt(tabla.getSelectedRow(),PanelTablaMovimientos.COL_OBJ );
		if(mc instanceof MovimientoDebeProveedor) {
			CorreccionFacturaProveedor cfp = getCorreccionFacade().getCorreccionFacturaByIdEager(((MovimientoDebeProveedor)mc).getNotaDebitoProveedor().getId());
			if(!StringUtil.isNullOrEmpty(cfp.getNroCorreccion())) {
				FWJOptionPane.showErrorMessage(this, "La nota de débito ya tiene un número asignado.", "Error");
				return;
			}
			JDialogCompletarDatosCorreccionFacturaProveedor jDialogCompletarDatosCorreccionFacturaProveedor = new JDialogCompletarDatosCorreccionFacturaProveedor(JFrameVerMovimientosProveedor.this, cfp, "Nota de Débito");
			GuiUtil.centrar(jDialogCompletarDatosCorreccionFacturaProveedor);
			jDialogCompletarDatosCorreccionFacturaProveedor.setVisible(true);
		}
		if(mc instanceof MovimientoHaberProveedor) {
			CorreccionFacturaProveedor cfp = getCorreccionFacade().getCorreccionFacturaByIdEager(((MovimientoHaberProveedor)mc).getNotaCredito().getId());
			if(!StringUtil.isNullOrEmpty(cfp.getNroCorreccion())) {
				FWJOptionPane.showErrorMessage(this, "La nota de crédito ya tiene un número asignado.", "Error");
				return;
			}
			JDialogCompletarDatosCorreccionFacturaProveedor jDialogCompletarDatosCorreccionFacturaProveedor = new JDialogCompletarDatosCorreccionFacturaProveedor(JFrameVerMovimientosProveedor.this, cfp, "Nota de Crédito");
			GuiUtil.centrar(jDialogCompletarDatosCorreccionFacturaProveedor);
			jDialogCompletarDatosCorreccionFacturaProveedor.setVisible(true);
		}
		buscarMovimientos();
	}

	
	public UsuarioSistema getUsuarioAdministrador() {
		return usuarioAdministrador;
	}

	
	public void setUsuarioAdministrador(UsuarioSistema usuarioAdministrador) {
		this.usuarioAdministrador = usuarioAdministrador;
	}
	
	private JButton getBtnAgregarRemitoCompraTela() {
		if(btnAgregarRemitoCompraTela == null){
			btnAgregarRemitoCompraTela = BossEstilos.createButton("ar/com/textillevel/imagenes/b_cheque_cartera.png", "ar/com/textillevel/imagenes/b_cheque_cartera_des.png");
			btnAgregarRemitoCompraTela.setToolTipText("Agregar remito de compra de tela");
			btnAgregarRemitoCompraTela.setEnabled(false);
			btnAgregarRemitoCompraTela.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					RemitoEntrada remitoEntrada = new RemitoEntrada();
					remitoEntrada.setProveedor(proveedorElegido);
					JDialogAgregarRemitoEntradaCompraTela dialogAgregarRemitoEntrada = new JDialogAgregarRemitoEntradaCompraTela(JFrameVerMovimientosProveedor.this, remitoEntrada, new ArrayList<OrdenDeTrabajo>(), false);
					GuiUtil.centrar(dialogAgregarRemitoEntrada);		
					dialogAgregarRemitoEntrada.setVisible(true);
					RemitoEntradaProveedor remitoEntradaProveedor = dialogAgregarRemitoEntrada.getRemitoEntradaProveedor();
					if(remitoEntradaProveedor != null) {
						if(FWJOptionPane.showQuestionMessage(JFrameVerMovimientosProveedor.this, "¿Desea Cargar la Factura del Proveedor?", "Confirmación") == FWJOptionPane.YES_OPTION) {
							RemitoEntradaProveedorFacadeRemote repfr = GTLBeanFactory.getInstance().getBean2(RemitoEntradaProveedorFacadeRemote.class);
							remitoEntradaProveedor = repfr.getByIdEager(remitoEntradaProveedor.getId());
							FacturaProveedor fp = new FacturaProveedor();
							fp.setTipoFacturaProveedor(ETipoFacturaProveedor.NORMAL);					
							fp.setUsuarioCreador(GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName());
							fp.setProveedor(proveedorElegido);
							fp.getRemitoList().add(remitoEntradaProveedor);
							JDialogCargarFacturaProveedor jdcfp = new JDialogCargarFacturaProveedor(JFrameVerMovimientosProveedor.this, fp, false, new ArrayList<RemitoEntradaProveedor>());
							GuiUtil.centrar(jdcfp);
							jdcfp.setVisible(true);
						}
					}
					buscarMovimientos();
				}
			});
		}
		return btnAgregarRemitoCompraTela;
	}

	private JButton getBtnAgregarRemitoVentaTela() {
		if(btnAgregarRemitoVentaTela == null){
			btnAgregarRemitoVentaTela = BossEstilos.createButton("ar/com/textillevel/imagenes/b_venta.png", "ar/com/textillevel/imagenes/b_venta_des.png");
			btnAgregarRemitoVentaTela.setToolTipText("Agregar remito venta de tela");
			btnAgregarRemitoVentaTela.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					IngresoRemitoSalidaHandler rsHandler = new IngresoRemitoSalidaHandler(JFrameVerMovimientosProveedor.this, ETipoRemitoSalida.CLIENTE_VENTA_DE_TELA, false, null);
					rsHandler.gestionarIngresoRemitoSalida();
					buscarMovimientos();
				}
			});
		}
		return btnAgregarRemitoVentaTela;
	}
	
	private JCheckBox getChkUltimosMovimientos() {
		if(chkUltimosMovimientos == null){
			chkUltimosMovimientos = new JCheckBox("Ver solo últimos movimientos");
			chkUltimosMovimientos.setSelected(true);
			chkUltimosMovimientos.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					getDPFechaDesde().setEnabled(!chkUltimosMovimientos.isSelected());
					getDPFechaHasta().setEnabled(!chkUltimosMovimientos.isSelected());
				}
			});
		}
		return chkUltimosMovimientos;
	}

	
	private JButton getBtnAgregarRemitoSalidaProveedor() {
		if(btnAgregarRemitoSalidaProveedor == null){
			btnAgregarRemitoSalidaProveedor = BossEstilos.createButton("ar/com/textillevel/imagenes/b_salida.png", "ar/com/textillevel/imagenes/b_salida_des.png");
			btnAgregarRemitoSalidaProveedor.setToolTipText("Agregar remito de salida proveedor");
			btnAgregarRemitoSalidaProveedor.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					RemitoSalida remitoSalidaProveedor = new RemitoSalida();
					remitoSalidaProveedor.setTipoRemitoSalida(ETipoRemitoSalida.PROVEEDOR);
					remitoSalidaProveedor.setNroFactura(0);
					remitoSalidaProveedor.setNroOrden(0);
					remitoSalidaProveedor.setPesoTotal(new BigDecimal(0));
					remitoSalidaProveedor.setPorcentajeMerma(new BigDecimal(0));
					remitoSalidaProveedor.setProveedor(proveedorElegido);
					JDialogAgregarRemitoSalidaProveedor jdarsp = new JDialogAgregarRemitoSalidaProveedor(JFrameVerMovimientosProveedor.this, false, remitoSalidaProveedor);
					jdarsp.setVisible(true);
				}
			});
		}
		return btnAgregarRemitoSalidaProveedor;
	}
}
