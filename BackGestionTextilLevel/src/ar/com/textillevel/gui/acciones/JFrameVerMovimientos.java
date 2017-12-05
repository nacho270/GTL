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
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.filechooser.FileFilter;

import org.apache.taglibs.string.util.StringW;

import ar.com.fwcommon.boss.BossError;
import ar.com.fwcommon.boss.BossEstilos;
import ar.com.fwcommon.componentes.FWFileSelector;
import ar.com.fwcommon.componentes.FWJNumericTextField;
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
import ar.com.textillevel.entidades.cuenta.CuentaCliente;
import ar.com.textillevel.entidades.cuenta.movimientos.MovimientoCuenta;
import ar.com.textillevel.entidades.cuenta.movimientos.visitor.IFilaMovimientoVisitor;
import ar.com.textillevel.entidades.cuenta.to.ETipoDocumento;
import ar.com.textillevel.entidades.documentos.factura.CorreccionFactura;
import ar.com.textillevel.entidades.documentos.factura.DocumentoContableCliente;
import ar.com.textillevel.entidades.documentos.factura.Factura;
import ar.com.textillevel.entidades.documentos.factura.NotaCredito;
import ar.com.textillevel.entidades.documentos.factura.NotaDebito;
import ar.com.textillevel.entidades.documentos.factura.to.InfoCuentaTO;
import ar.com.textillevel.entidades.documentos.recibo.Recibo;
import ar.com.textillevel.entidades.documentos.remito.RemitoSalida;
import ar.com.textillevel.entidades.enums.EEstadoFactura;
import ar.com.textillevel.entidades.enums.EEstadoRecibo;
import ar.com.textillevel.entidades.enums.ETipoBusquedaAgenda;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.portal.UsuarioSistema;
import ar.com.textillevel.entidades.ventas.cotizacion.Cotizacion;
import ar.com.textillevel.entidades.ventas.cotizacion.VersionListaDePrecios;
import ar.com.textillevel.excepciones.EValidacionException;
import ar.com.textillevel.facade.api.remote.ClienteFacadeRemote;
import ar.com.textillevel.facade.api.remote.CorreccionFacadeRemote;
import ar.com.textillevel.facade.api.remote.CuentaFacadeRemote;
import ar.com.textillevel.facade.api.remote.FacturaFacadeRemote;
import ar.com.textillevel.facade.api.remote.ListaDePreciosFacadeRemote;
import ar.com.textillevel.facade.api.remote.ParametrosGeneralesFacadeRemote;
import ar.com.textillevel.facade.api.remote.ReciboFacadeRemote;
import ar.com.textillevel.facade.api.remote.RemitoEntradaFacadeRemote;
import ar.com.textillevel.facade.api.remote.RemitoSalidaFacadeRemote;
import ar.com.textillevel.facade.api.remote.UsuarioSistemaFacadeRemote;
import ar.com.textillevel.gui.acciones.impresionfactura.ImpresionFacturaHandler;
import ar.com.textillevel.gui.acciones.impresionrecibo.ImprimirReciboHandler;
import ar.com.textillevel.gui.acciones.impresionremito.ImprimirRemitoHandler;
import ar.com.textillevel.gui.acciones.visitor.cuenta.AccionAnularCuentaVisitor;
import ar.com.textillevel.gui.acciones.visitor.cuenta.AccionConfirmarCuentaVisitor;
import ar.com.textillevel.gui.acciones.visitor.cuenta.AccionEliminarFacturaCuentaVisitor;
import ar.com.textillevel.gui.acciones.visitor.cuenta.AccionEnviarDocumentoContablePorEmailCuentaVisitor;
import ar.com.textillevel.gui.acciones.visitor.cuenta.CellRenderer;
import ar.com.textillevel.gui.acciones.visitor.cuenta.ConsultaDocumentoVisitor;
import ar.com.textillevel.gui.acciones.visitor.cuenta.EditarDocumentoVisitor;
import ar.com.textillevel.gui.acciones.visitor.cuenta.GenerarFilaMovimientoVisitor;
import ar.com.textillevel.gui.acciones.visitor.cuenta.HabilitarBotonesCuentaVisitor;
import ar.com.textillevel.gui.acciones.visitor.cuenta.PintarFilaReciboVisitor;
import ar.com.textillevel.gui.acciones.visitor.cuenta.PintarRecibosSecondPassVisitor;
import ar.com.textillevel.gui.modulos.abm.listaprecios.ImprimirListaDePreciosHandler;
import ar.com.textillevel.gui.util.EmailSender;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.GenericUtils.BackgroundTask;
import ar.com.textillevel.gui.util.JasperHelper;
import ar.com.textillevel.gui.util.controles.PanelDatePicker;
import ar.com.textillevel.gui.util.dialogs.JDialogDestinatariosEmail;
import ar.com.textillevel.gui.util.dialogs.JDialogDestinatariosEmail.PerformEnvioEmailHandler;
import ar.com.textillevel.gui.util.dialogs.JDialogPasswordInput;
import ar.com.textillevel.gui.util.dialogs.JDialogSeleccionarCliente;
import ar.com.textillevel.gui.util.dialogs.JDialogSeleccionarCliente.EModoDialogo;
import ar.com.textillevel.util.GTLBeanFactory;
import main.GTLGlobalCache;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public class JFrameVerMovimientos extends JFrame {

	private static final long serialVersionUID = 2857257268987818318L;

	private static final String EXTENSION_EXCEL = ".xls";
	private static final String EXTENSION_PDF = ".pdf";

	private PanelTablaMovimientos tablaMovimientos;
	private JPanel panelCabecera;
	private PanelDatePicker txtFechaDesde;
	private PanelDatePicker txtFechaHasta;
	private JComboBox cmbTipoBusquedaCliente;
	private FWJNumericTextField txtBusquedaCliente;
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
	private JLabel lblCondicionVenta;
	private JCheckBox chkUltimosMovimientos;
	// private JComboBox cmbOrdenMovimientos;
	private JComboBox cmbFiltroTipoDocumento;

	private GenerarFilaMovimientoVisitor filaMovimientoVisitor;
	private IFilaMovimientoVisitor consultaDocumentoMovimientoVisitor;

	private CuentaFacadeRemote cuentaFacade;
	private FacturaFacadeRemote facturaFacade;
	private ReciboFacadeRemote reciboFacade;
	private CorreccionFacadeRemote correccionFacade;
	private RemitoSalidaFacadeRemote remitoSalidaFacade;
	private RemitoEntradaFacadeRemote remitoEntradaFacade;
	private ListaDePreciosFacadeRemote listaDePreciosFacade;

	/** ACCIONES **/

	private JButton btnEditar;
	private JButton btnAnular;
	private JButton btnConfirmar;
	private JButton btnExportarAExcel;
	private JButton btnImprimirListado;
	private JButton btnListadoPDF;
	private JButton btnEliminarFactura;
	private JButton btnAgregarRecibo;
	// private JButton btnEliminarRecibo;
	private JButton btnAgregarObservaciones;
	private JButton btnVisualizarCotizacionActual;
	private JButton btnEnviarCotizacionPorEmail;
	private JButton btnEnviarExtractoCuentaPorEmail;
	private JButton btnEnviarDocumentoContablePorEmail;

	private UsuarioSistema usuarioAdministrador;
	private Cliente clienteBuscado;
	private VersionListaDePrecios versionListaDePreciosCotizada;
	private Cotizacion cotizacionActual;
	JasperPrint jasperPrintCotizacion = null;

	public JFrameVerMovimientos(Frame padre) {
		setUpComponentes();
		setUpScreen();
		getTxtBusquedaCliente().requestFocus();
		getBtnAnular().setEnabled(false);
		getBtnConfirmar().setEnabled(false);
		getDPFechaDesde().setEnabled(!getChkUltimosMovimientos().isSelected());
		getDPFechaHasta().setEnabled(!getChkUltimosMovimientos().isSelected());
	}

	private void setUpScreen() {
		setSize(new Dimension(1000, 600));
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setTitle("Ver movimientos");
		GuiUtil.centrar(this);
		setExtendedState(MAXIMIZED_BOTH);
	}

	private void llenarTablaMovimientos(List<MovimientoCuenta> movimientos, BigDecimal transporteCuenta) {
		getPanelTablaMovimientos().getTabla().removeAllRows();
		if (transporteCuenta.doubleValue() != 0) {
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

		private static final int CANT_COLS_TBL_MOVS = 11;
		private static final int COL_PAGOS = 0;
		private static final int COL_ODT = 1;
		private static final int COL_DESCR = 2;
		private static final int COL_DEBE = 3;
		private static final int COL_HABER = 4;
		private static final int COL_SALDO = 5;
		private static final int COL_OBJ = 6;
		private static final int COL_VERIFICADO = 7;
		private static final int COL_USUARIO_VERIFICADOR = 8;
		private static final int COL_OBSERVACIONES = 9;
		private static final int COL_ENTREGADO = 10;

		@Override
		protected void agregarElemento(MovimientoCuenta mov) {
			mov.aceptarVisitor(filaMovimientoVisitor);
		}

		public void agregarTransporte(BigDecimal transporteCuenta) {
			Object[] row = new Object[CANT_COLS_TBL_MOVS];
			row[COL_PAGOS] = "";
			row[COL_ODT] = "";
			row[COL_DESCR] = "TRANSPORTE CUENTA";
			row[COL_DEBE] = "";
			row[COL_HABER] = "";
			row[COL_SALDO] = getDecimalFormat().format(transporteCuenta);
			row[COL_OBJ] = "";
			row[COL_VERIFICADO] = false;
			row[COL_USUARIO_VERIFICADOR] = "";
			row[COL_ENTREGADO] = "<html><div style=\"padding:2px 0px;\">&nbsp;</div></html>";
			getTabla().addRow(row);
			getTabla().setForegroundCell(getTabla().getRowCount() - 1, COL_SALDO, transporteCuenta.doubleValue() > 0 ? Color.GREEN.darker() : transporteCuenta.doubleValue() < 0 ? Color.RED : Color.BLACK);
		}

		@Override
		protected FWJTable construirTabla() {
			FWJTable tabla = new FWJTable(0, CANT_COLS_TBL_MOVS);
			tabla.setStringColumn(COL_PAGOS, "Pagos", 100);
			tabla.setStringColumn(COL_ODT, "ODT", 90, 90, true);
			tabla.setStringColumn(COL_DESCR, "Descripción", 160, 260, true);
			tabla.setFloatColumn(COL_DEBE, "Debe", 80, true);
			tabla.setFloatColumn(COL_HABER, "Haber", 80, true);
			tabla.setFloatColumn(COL_SALDO, "Saldo", 80, true);
			tabla.setStringColumn(COL_OBJ, "", 0, 0, true);
			tabla.setCheckColumn(COL_VERIFICADO, "Verificado", 60, true);
			tabla.setStringColumn(COL_USUARIO_VERIFICADOR, "Usuario verficador", 100, 100, true);
			tabla.setStringColumn(COL_OBSERVACIONES, "Observaciones", 160, 260, true);
			tabla.setMultilineColumn(COL_ENTREGADO, "Entregado", 250, true, true);
			tabla.setReorderingAllowed(false);
			tabla.setHeaderAlignment(COL_PAGOS, FWJTable.CENTER_ALIGN);
			tabla.setHeaderAlignment(COL_ODT, FWJTable.CENTER_ALIGN);
			tabla.setHeaderAlignment(COL_DESCR, FWJTable.CENTER_ALIGN);
			tabla.setHeaderAlignment(COL_DEBE, FWJTable.CENTER_ALIGN);
			tabla.setHeaderAlignment(COL_HABER, FWJTable.CENTER_ALIGN);
			tabla.setHeaderAlignment(COL_SALDO, FWJTable.CENTER_ALIGN);
			tabla.setHeaderAlignment(COL_VERIFICADO, FWJTable.CENTER_ALIGN);
			tabla.setHeaderAlignment(COL_USUARIO_VERIFICADOR, FWJTable.CENTER_ALIGN);
			tabla.setHeaderAlignment(COL_OBSERVACIONES, FWJTable.CENTER_ALIGN);
			tabla.setHeaderAlignment(COL_ENTREGADO, FWJTable.CENTER_ALIGN);
			tabla.setAlignment(COL_USUARIO_VERIFICADOR, FWJTable.CENTER_ALIGN);
			tabla.getColumnModel().getColumn(COL_PAGOS).setCellRenderer(getCellRenderer());
			consultaDocumentoMovimientoVisitor = new ConsultaDocumentoVisitor(JFrameVerMovimientos.this);
			tabla.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseReleased(MouseEvent e) {
					if (getTabla().getSelectedRow() > -1) {
						if (getTabla().getValueAt(getTabla().getSelectedRow(), COL_OBJ) instanceof MovimientoCuenta) {
							if (e.getClickCount() == 2 && getTabla().getSelectedRow() > -1) {
								((MovimientoCuenta) getTabla().getValueAt(getTabla().getSelectedRow(), COL_OBJ)).aceptarVisitor(consultaDocumentoMovimientoVisitor);
							} else if (e.getClickCount() == 1 && getTabla().getSelectedRow() > -1) {
								HabilitarBotonesCuentaVisitor hbcv = new HabilitarBotonesCuentaVisitor(JFrameVerMovimientos.this);
								((MovimientoCuenta) getTabla().getValueAt(getTabla().getSelectedRow(), COL_OBJ)).aceptarVisitor(hbcv);
							} else if (getTabla().getSelectedRow() < 0) {
								getBtnAnular().setEnabled(false);
								getBtnConfirmar().setEnabled(false);
							}
						}
					}
				}
			});

			tabla.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					int selectedRow = getTabla().getSelectedRow();
					if (selectedRow > -1) {
						if (getTabla().getValueAt(selectedRow, COL_OBJ) instanceof MovimientoCuenta) {
							if (e.getKeyCode() == KeyEvent.VK_ENTER && selectedRow > -1) {
								if (selectedRow - 1 < 0) {
									selectedRow = getTabla().getRowCount() - 1;
								} else {
									selectedRow--;
								}
								getTabla().setRowSelectionInterval(selectedRow, selectedRow);
								((MovimientoCuenta) getTabla().getValueAt(selectedRow, COL_OBJ)).aceptarVisitor(consultaDocumentoMovimientoVisitor);
							} else if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN && getTabla().getSelectedRow() > -1) {
								HabilitarBotonesCuentaVisitor hbcv = new HabilitarBotonesCuentaVisitor(JFrameVerMovimientos.this);
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

		public List<MovimientoTO> createListaMovimientosTO() {
			List<MovimientoTO> lista = new ArrayList<JFrameVerMovimientos.MovimientoTO>();
			FWJTable tabla = getTabla();
			for (int i = 0; i < tabla.getRowCount(); i++) {
				MovimientoTO mto = new MovimientoTO();
				String descripcion = (String) tabla.getValueAt(i, COL_DESCR);
				if (GenericUtils.isSistemaTest()) {
					int indexOfFC = descripcion.indexOf(" - FC ");
					if(indexOfFC != -1) {
						descripcion = descripcion.substring(0, indexOfFC);
					}
				}
				mto.setDescripcion(descripcion);
				mto.setDebe((String) tabla.getValueAt(i, COL_DEBE));
				mto.setHaber((String) tabla.getValueAt(i, COL_HABER));
				mto.setSaldo((String) tabla.getValueAt(i, COL_SALDO));
				lista.add(mto);
			}
			return lista;
		}
	}

	private JPanel getPanelCabecera() {
		if (panelCabecera == null) {
			panelCabecera = new JPanel();
			panelCabecera.setLayout(new GridBagLayout());
			JPanel panel = new JPanel(new VerticalFlowLayout());
			panel.setBorder(BorderFactory.createEtchedBorder());

			final Component[] comps = new Component[] { getCmbTipoBusquedaCliente(), getTxtBusquedaCliente(), getDPFechaDesde(), getDPFechaHasta(), getBtnBuscar() };
			FocusTraversalPolicy policy = new FocusTraversalPolicy() {

				List<Component> textList = Arrays.asList(comps);

				@Override
				public Component getFirstComponent(Container focusCycleRoot) {
					return comps[1];
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

			JPanel panelCliente = new JPanel();
			panelCliente.setLayout(new FlowLayout());
			panelCliente.add(new JLabel("Cliente: "));
			panelCliente.add(getCmbTipoBusquedaCliente());
			panelCliente.add(getTxtBusquedaCliente());
			panelCliente.add(getBtnBuscar());

			JPanel panelFechas = new JPanel();
			panelFechas.setLayout(new FlowLayout());
			panelFechas.add(getDPFechaDesde());
			panelFechas.add(getDPFechaHasta());

			JPanel panelFiltros = new JPanel();
			panelFiltros.setLayout(new FlowLayout());
			panelFiltros.add(getChkUltimosMovimientos());
			panelFiltros.add(new JLabel("Tipo documento: "));
			panelFiltros.add(getCmbFiltroTipoDocumento());
			// panelFiltros.add(getCmbOrdenMovimientos());

			panel.add(panelCliente);
			panel.add(panelFechas);
			panel.add(panelFiltros);

			panelCabecera.add(getPanelDatosUsuario(), new GridBagConstraints(0, 0, 1, 1, 0.4, 0, GridBagConstraints.WEST, GridBagConstraints.LINE_END, new Insets(15, 5, 15, 5), 0, 0));
			panelCabecera.add(panel, new GridBagConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.LINE_END, new Insets(15, 5, 15, 5), 0, 0));
		}
		return panelCabecera;
	}

	private JButton getBtnBuscar() {
		if (btnBuscar == null) {
			btnBuscar = new JButton("Buscar");
			btnBuscar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent evt) {
					if (getDPFechaDesde().getDate() == null) {
						FWJOptionPane.showErrorMessage(JFrameVerMovimientos.this, "La 'fecha desde' ingresada no es válida", "Error");
						return;
					}
					if (getDPFechaHasta().getDate() == null) {
						FWJOptionPane.showErrorMessage(JFrameVerMovimientos.this, "La 'fecha hasta' ingresada no es válida", "Error");
						return;
					}
					if (!getDPFechaDesde().getDate().after(getDPFechaHasta().getDate())) {
						if (getTxtBusquedaCliente().getText().trim().length() > 0) {
							buscarMovimientos();
						} else {
							FWJOptionPane.showErrorMessage(JFrameVerMovimientos.this, "Debe ingresar un cliente", "Error");
						}
					} else {
						FWJOptionPane.showErrorMessage(JFrameVerMovimientos.this, "La 'fecha desde' no debe ser posterior a la 'fecha hasta'", "Error");
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
					Integer idCliente = getTxtBusquedaCliente().getValue();
					java.sql.Date fechaDesde = getChkUltimosMovimientos().isSelected()?null:new java.sql.Date(getDPFechaDesde().getDate().getTime());
					java.sql.Date fechaHasta = getChkUltimosMovimientos().isSelected()?null:DateUtil.getManiana(new java.sql.Date(getDPFechaHasta().getDate().getTime() + DateUtil.ONE_DAY));
					List<MovimientoCuenta> movs = null;
					ETipoDocumento filtroTipoDocumento = getCmbFiltroTipoDocumento().getSelectedItem().equals("TODOS") ? null : (ETipoDocumento) getCmbFiltroTipoDocumento().getSelectedItem();
					if(getChkUltimosMovimientos().isSelected()){
						movs = getCuentaFacade().getMovimientosByIdClienteYFecha(idCliente, null,null,true,filtroTipoDocumento/*,getCmbOrdenMovimientos().getSelectedItem().equals("MAS ANTIGUO PRIMERO")*/);
					}else{
						movs = getCuentaFacade().getMovimientosByIdClienteYFecha(idCliente, fechaDesde,fechaHasta,false,filtroTipoDocumento/*,getCmbOrdenMovimientos().getSelectedItem().equals("MAS ANTIGUO PRIMERO")*/);
					}
					
//					ESTO ES LO QUE HICIMOS EL SABADO EN LA FABRICA Y DIEGO DESPUES DIJO QUE NO FUNCIONO
//					VUELVO A PONER LO ANTERIOR
//					
//					boolean restarMovimiento = false;
//					if (movs != null) {
//						BigDecimal transporteCuenta = new BigDecimal(0d);
//						if(getChkUltimosMovimientos().isSelected()){
//							if(!movs.isEmpty()){
//								transporteCuenta = getCuentaFacade().getTransporteCuenta(idCliente,new java.sql.Date(movs.get(0).getFechaHora().getTime()),false);
//								restarMovimiento = true;
//							}
//						}else{
//							transporteCuenta = getCuentaFacade().getTransporteCuenta(idCliente,fechaDesde,true);
//						}
//						double transporteAbsoluto = Math.abs(transporteCuenta.doubleValue()); 
//						if(transporteCuenta!=null && transporteAbsoluto>0d && restarMovimiento){
//							transporteCuenta = transporteCuenta.add(movs.get(0).getMonto());
//						}
					if (movs != null && !movs.isEmpty()) {
						BigDecimal transporteCuenta = new BigDecimal(0d);
						List<MovimientoCuenta> movimientosTransporteCuenta = getCuentaFacade().getMovimientosTransporteCuentaCliente(idCliente, new java.sql.Date(movs.get(0).getFechaHora().getTime()));
						transporteCuenta = calcularTransporte(movimientosTransporteCuenta, movs);
						filaMovimientoVisitor = getPanelTablaMovimientos().createVisitorFilaMovimientos(transporteCuenta);
						boolean habilitarBotonesImpresion = movs.size() > 0;
						getBtnExportarAExcel().setEnabled(habilitarBotonesImpresion);
						getBtnImprimirListado().setEnabled(habilitarBotonesImpresion);
						getBtnListadoPDF().setEnabled(habilitarBotonesImpresion);
						llenarTablaMovimientos(movs,transporteCuenta);
						setSaldoCuenta();
						if(filtroTipoDocumento == null) {
							Map<Integer, Color> mapaColores = filaMovimientoVisitor.getMapaColores();
							InfoCuentaTO infoCuentaTO = getCuentaFacade().getInfoReciboYPagosRecibidos(idCliente);
							pintarRecibos(mapaColores);
							pintarFacturasPagadas(mapaColores, infoCuentaTO);
							pintarRecibosSecondPass(filaMovimientoVisitor.getRowsPagosSaldoAFavor());
						}
						getBtnEnviarExtractoCuentaPorEmail().setEnabled(true);
					} else {
						getBtnAgregarRecibo().setEnabled(false);
						getBtnExportarAExcel().setEnabled(false);
						getBtnImprimirListado().setEnabled(false);
						getBtnListadoPDF().setEnabled(false);
						getPanelTablaMovimientos().getTabla().removeAllRows();
						getTxtTotalCuenta().setText("");
						FWJOptionPane.showWarningMessage(JFrameVerMovimientos.this, "El cliente no registra movimientos", "Error");
						getBtnEnviarExtractoCuentaPorEmail().setEnabled(false);
					}
					Cliente cliente = GTLBeanFactory.getInstance().getBean2(ClienteFacadeRemote.class).getClienteByNumero(idCliente);
					if (cliente != null) {
						try {
							cotizacionActual = getListaDePreciosFacade().getCotizacionVigente(cliente);
							if (cotizacionActual == null) {
								versionListaDePreciosCotizada = getListaDePreciosFacade().getVersionActual(cliente);
							}
							getBtnVisualizarCotizacionActual().setEnabled(true);
							getBtnEnviarCotizacionPorEmail().setEnabled(cotizacionActual != null && cliente.getEmail() != null);
						}catch(ValidacionException vle) {
							versionListaDePreciosCotizada = null;
							getBtnVisualizarCotizacionActual().setEnabled(false);
							getBtnEnviarCotizacionPorEmail().setEnabled(false);
						}
					} else {
						versionListaDePreciosCotizada = null;
						getBtnVisualizarCotizacionActual().setEnabled(false);
						getBtnEnviarCotizacionPorEmail().setEnabled(false);
					}
					
				}catch(ValidacionException vle){
					FWJOptionPane.showErrorMessage(JFrameVerMovimientos.this, vle.getMensajeError(), "Error");
				}
			}
		});

	}

	private BigDecimal calcularTransporte(List<MovimientoCuenta> movsTransporte, List<MovimientoCuenta> allMovs) {
		BigDecimal transporteCuenta = new BigDecimal(0d);
		for (MovimientoCuenta movT : movsTransporte) {
			if (!allMovs.contains(movT)) {
				transporteCuenta = transporteCuenta.add(movT.getMonto());
			}
		}
		return transporteCuenta.negate();
	}

	private void pintarRecibosSecondPass(List<InfoSecondPass> rowsPagosSaldoAFavor) {
		FWJTable tablaMov = getPanelTablaMovimientos().getTabla();
		CellRenderer cellRenderer = (CellRenderer) tablaMov.getColumnModel().getColumn(0).getCellRenderer();
		for (InfoSecondPass isp : rowsPagosSaldoAFavor) {
			for (int i = isp.getFila() + 1; i < tablaMov.getRowCount(); i++) {
				if (tablaMov.getValueAt(i, PanelTablaMovimientos.COL_OBJ) instanceof MovimientoCuenta) {
					PintarRecibosSecondPassVisitor prspv = new PintarRecibosSecondPassVisitor(isp, i, cellRenderer);
					((MovimientoCuenta) tablaMov.getValueAt(i, PanelTablaMovimientos.COL_OBJ)).aceptarVisitor(prspv);
				}
			}
		}
	}

	private void pintarFacturasPagadas(Map<Integer, Color> mapaColores, InfoCuentaTO infoCuentaTO) {
		FWJTable tabla = getPanelTablaMovimientos().getTabla();
		CellRenderer cellRenderer = (CellRenderer) getPanelTablaMovimientos().getTabla().getColumnModel().getColumn(0).getCellRenderer();
		PintarFilaReciboVisitor pfrv = new PintarFilaReciboVisitor(tabla, mapaColores, cellRenderer, infoCuentaTO);
		for (int i = 0; i < tabla.getRowCount(); i++) {
			if (tabla.getValueAt(i, PanelTablaMovimientos.COL_OBJ) instanceof MovimientoCuenta) {
				pfrv.setFilaActual(i);
				((MovimientoCuenta) tabla.getValueAt(i, PanelTablaMovimientos.COL_OBJ)).aceptarVisitor(pfrv);
			}
		}
	}

	private void pintarRecibos(Map<Integer, Color> mapaColores) {
		FWJTable tabla = getPanelTablaMovimientos().getTabla();
		CellRenderer cellRenderer = (CellRenderer) getPanelTablaMovimientos().getTabla().getColumnModel().getColumn(0).getCellRenderer();
		cellRenderer.clear();
		PintarFilaReciboVisitor pfrv = new PintarFilaReciboVisitor(tabla, mapaColores, cellRenderer);
		for (int i = 0; i < tabla.getRowCount(); i++) {
			if (tabla.getValueAt(i, PanelTablaMovimientos.COL_OBJ) instanceof MovimientoCuenta) {
				pfrv.setFilaActual(i);
				((MovimientoCuenta) tabla.getValueAt(i, PanelTablaMovimientos.COL_OBJ)).aceptarVisitor(pfrv);
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

	private JComboBox getCmbTipoBusquedaCliente() {
		if (cmbTipoBusquedaCliente == null) {
			cmbTipoBusquedaCliente = new JComboBox();
			cmbTipoBusquedaCliente.addItem("ID");
			cmbTipoBusquedaCliente.addItem("NOMBRE");
			cmbTipoBusquedaCliente.addItemListener(new ItemListener() {

				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						if (cmbTipoBusquedaCliente.getSelectedItem().equals("NOMBRE")) {
							JDialogSeleccionarCliente dialogSeleccionarCliente = new JDialogSeleccionarCliente(null, EModoDialogo.MODO_NOMBRE);
							GuiUtil.centrar(dialogSeleccionarCliente);
							dialogSeleccionarCliente.setVisible(true);
							Cliente clienteElegido = dialogSeleccionarCliente.getCliente();
							if (clienteElegido != null) {
								getTxtBusquedaCliente().setText(String.valueOf(clienteElegido.getNroCliente()));
								getLblNombre().setText(clienteElegido.getRazonSocial());
								getLblDireccion().setText(clienteElegido.getDireccionFiscal().getDireccion() + " " + clienteElegido.getDireccionFiscal().getLocalidad().getNombreLocalidad());
								getLblCuit().setText(clienteElegido.getCuit());
								getLblTelefono().setText(clienteElegido.getTelefono());
								getLblCondicionVenta().setText("COND. PAGO: " + clienteElegido.getCondicionVenta().getNombre());
								getBtnBuscar().doClick();
							}
							getCmbTipoBusquedaCliente().setSelectedIndex(0);
						}
					}
				}
			});
		}
		return cmbTipoBusquedaCliente;
	}

	private FWJNumericTextField getTxtBusquedaCliente() {
		if (txtBusquedaCliente == null) {
			txtBusquedaCliente = new FWJNumericTextField();
			txtBusquedaCliente.setPreferredSize(new Dimension(100, 20));
			txtBusquedaCliente.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					getBtnBuscar().doClick();
				}
			});
		}
		return txtBusquedaCliente;
	}

	private JPanel getPanelSur() {
		if (panelSur == null) {
			panelSur = new JPanel();
			panelSur.setLayout(new GridBagLayout());
			panelSur.add(getPanelAcciones(), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 1, 1, 1, 1));
			JPanel panelTotal = new JPanel();
			panelTotal.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
			panelTotal.add(new JLabel("Total: "));
			panelTotal.add(getTxtTotalCuenta());
			panelSur.add(panelTotal, GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 1, 1, 1, 1));
		}
		return panelSur;
	}

	private JPanel getPanelAcciones() {
		if (panelAcciones == null) {
			panelAcciones = new JPanel();
			panelAcciones.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 2));
			panelAcciones.add(getBtnVisualizarCotizacionActual());
			panelAcciones.add(getBtnEnviarCotizacionPorEmail());
			panelAcciones.add(getBtnEnviarExtractoCuentaPorEmail());
			panelAcciones.add(getBtnEnviarDocumentoContablePorEmail());
			panelAcciones.add(getBtnAnular());
			panelAcciones.add(getBtnEliminarFactura());
			panelAcciones.add(getBtnEditar());
			panelAcciones.add(getBtnAgregarRecibo());
			panelAcciones.add(getBtnConfirmar());
			panelAcciones.add(getBtnAgregarObservaciones());
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
		CuentaCliente cuenta = getCuentaFacade().getCuentaClienteByNroCliente(getTxtBusquedaCliente().getValue());
		if (cuenta != null) {
			Cliente cliente = cuenta.getCliente();
			setClienteBuscado(cliente);
			getBtnAgregarRecibo().setEnabled(true);
			getLblNombre().setText(cliente.getRazonSocial());
			getLblDireccion().setText(cliente.getDireccionFiscal().getDireccion() + " " + (cliente.getDireccionFiscal().getLocalidad() == null ? "" : cliente.getDireccionFiscal().getLocalidad().getNombreLocalidad()));
			getLblCuit().setText(cliente.getCuit());
			getLblTelefono().setText(cliente.getTelefono());
			getLblCondicionVenta().setText("COND. PAGO: " + cliente.getCondicionVenta().getNombre());
			BigDecimal saldo = new BigDecimal(filaMovimientoVisitor.getSaldo());
			String format = getDecimalFormat().format(saldo);
			if (saldo.doubleValue() < 1 && saldo.doubleValue() > 0) {
				format = "0" + format;
			}
			getTxtTotalCuenta().setText(format);
			if (saldo.doubleValue() > 0) {
				getTxtTotalCuenta().setForeground(Color.GREEN.darker());
			} else if (saldo.doubleValue() < 0) {
				getTxtTotalCuenta().setForeground(Color.RED);
			} else {
				getTxtTotalCuenta().setForeground(Color.BLACK);
			}
		}
	}

	public JButton getBtnEditar() {
		if (btnEditar == null) {
			btnEditar = BossEstilos.createButton("ar/com/textillevel/imagenes/b_modificar_fila.png", "ar/com/textillevel/imagenes/b_modificar_fila_des.png");
			btnEditar.setEnabled(false);
			btnEditar.setToolTipText("Editar");
			btnEditar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (getPanelTablaMovimientos().getTabla().getSelectedRow() > -1) {
						if (!GTLGlobalCache.getInstance().getUsuarioSistema().getPerfil().getIsAdmin()) {
							JDialogPasswordInput jDialogPasswordInput = new JDialogPasswordInput(JFrameVerMovimientos.this, "Editar documento");
							if (jDialogPasswordInput.isAcepto()) {
								String pass = new String(jDialogPasswordInput.getPassword());
								UsuarioSistema usrAdmin = GTLBeanFactory.getInstance().getBean2(UsuarioSistemaFacadeRemote.class).esPasswordDeAdministrador(pass);
								if (usrAdmin != null) {
									if (getUsuarioAdministrador() == null) {
										setUsuarioAdministrador(usrAdmin);
									}
									EditarDocumentoVisitor racv = new EditarDocumentoVisitor(JFrameVerMovimientos.this);
									((MovimientoCuenta) getPanelTablaMovimientos().getTabla().getValueAt(getPanelTablaMovimientos().getTabla().getSelectedRow(), PanelTablaMovimientos.COL_OBJ)).aceptarVisitor(racv);
								} else {
									FWJOptionPane.showErrorMessage(JFrameVerMovimientos.this, "La clave ingresada no peternece a un usuario administrador", "Error");
								}
							}
						} else {
							setUsuarioAdministrador(GTLGlobalCache.getInstance().getUsuarioSistema());
							EditarDocumentoVisitor racv = new EditarDocumentoVisitor(JFrameVerMovimientos.this);
							((MovimientoCuenta) getPanelTablaMovimientos().getTabla().getValueAt(getPanelTablaMovimientos().getTabla().getSelectedRow(), PanelTablaMovimientos.COL_OBJ)).aceptarVisitor(racv);
						}
						buscarMovimientos();
					}
				}
			});
		}
		return btnEditar;
	}

	public JButton getBtnEliminarFactura() {
		if (btnEliminarFactura == null) {
			btnEliminarFactura = BossEstilos.createButton("ar/com/textillevel/imagenes/b_eliminar.png", "ar/com/textillevel/imagenes/b_eliminar_des.png");
			btnEliminarFactura.setToolTipText("Eliminar");
			btnEliminarFactura.setEnabled(false);
			btnEliminarFactura.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if (getPanelTablaMovimientos().getTabla().getSelectedRow() > -1) {
						realizarAccionEliminarFactura();
					}
				}
			});
		}
		return btnEliminarFactura;
	}

	public JButton getBtnAgregarObservaciones() {
		if (btnAgregarObservaciones == null) {
			btnAgregarObservaciones = BossEstilos.createButton("ar/com/textillevel/imagenes/b_obs.png", "ar/com/textillevel/imagenes/b_obs_des.png");
			btnAgregarObservaciones.setEnabled(false);
			btnAgregarObservaciones.setToolTipText("Agregar observaciones");
			btnAgregarObservaciones.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int fila = getPanelTablaMovimientos().getTabla().getSelectedRow();
					if (fila > -1) {
						MovimientoCuenta mov = getPanelTablaMovimientos().getElemento(fila);
						String observaciones = JOptionPane.showInputDialog(JFrameVerMovimientos.this, "Observaciones", mov.getObservaciones());
						if (observaciones != null) {
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

	private JButton getBtnVisualizarCotizacionActual() {
		if (btnVisualizarCotizacionActual == null) {
			btnVisualizarCotizacionActual = BossEstilos.createButton("ar/com/textillevel/imagenes/b_venta.png", "ar/com/textillevel/imagenes/b_venta_des.png");
			btnVisualizarCotizacionActual.setToolTipText("Visualizar lista de precios actual");
			btnVisualizarCotizacionActual.setEnabled(false);
			btnVisualizarCotizacionActual.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					createJasperPrintCotizacion();
					if (jasperPrintCotizacion != null) {
						JasperHelper.visualizarReporte(jasperPrintCotizacion);
					}
				}
			});
		}
		return btnVisualizarCotizacionActual;
	}

	private JButton getBtnEnviarCotizacionPorEmail() {
		if (btnEnviarCotizacionPorEmail == null) {
			btnEnviarCotizacionPorEmail = BossEstilos.createButton("ar/com/textillevel/imagenes/b_cotizacion_email.png", "ar/com/textillevel/imagenes/b_cotizacion_email_des.png");
			btnEnviarCotizacionPorEmail.setToolTipText("Enviar lista de precios actual por email");
			btnEnviarCotizacionPorEmail.setEnabled(false);
			btnEnviarCotizacionPorEmail.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					createJasperPrintCotizacion();
					if (jasperPrintCotizacion != null) {
						new JDialogDestinatariosEmail(JFrameVerMovimientos.this, getClienteBuscado().getEmail(), new PerformEnvioEmailHandler() {
							@Override
							public void performEnvio(final List<String> to, final List<String> cc) {
								GenericUtils.realizarOperacionConDialogoDeEspera("Enviando cotización a: " + getClienteBuscado().getRazonSocial(), new BackgroundTask() {
									public void perform() {
										try {
											EmailSender.enviarCotizacionPorEmail(jasperPrintCotizacion, to, cc, getClienteBuscado().getId(), ETipoBusquedaAgenda.CLIENTE);
											String textoMensaje = "Se ha enviado la cotización por correo a: " + StringUtil.getCadena(to, ", ");
											if (to != null && !to.isEmpty()) {
												textoMensaje +=  "\nCon copia a: " + StringUtil.getCadena(cc, ", ");
											}
											FWJOptionPane.showInformationMessage(JFrameVerMovimientos.this, StringW.wordWrap(textoMensaje), "Información");
										} catch (Exception ex) {
											FWJOptionPane.showErrorMessage(JFrameVerMovimientos.this, "Ha ocurrido un error al enviar el email", "Error");
											ex.printStackTrace();
										}
									}
								});
							}
						}, getClienteBuscado().getId(), ETipoBusquedaAgenda.CLIENTE).setVisible(true);
					}
				}
			});
		}
		return btnEnviarCotizacionPorEmail;
	}

	public JButton getBtnEnviarDocumentoContablePorEmail() {
		if (btnEnviarDocumentoContablePorEmail == null) {
			btnEnviarDocumentoContablePorEmail = BossEstilos.createButton("ar/com/textillevel/imagenes/b_salida.png", "ar/com/textillevel/imagenes/b_salida_des.png");
			btnEnviarDocumentoContablePorEmail.setToolTipText("Enviar documento por email");
			btnEnviarDocumentoContablePorEmail.setEnabled(false);
			btnEnviarDocumentoContablePorEmail.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (getPanelTablaMovimientos().getTabla().getSelectedRow() > -1) {
						AccionEnviarDocumentoContablePorEmailCuentaVisitor racv = new AccionEnviarDocumentoContablePorEmailCuentaVisitor(JFrameVerMovimientos.this);
						((MovimientoCuenta) getPanelTablaMovimientos().getTabla().getValueAt(getPanelTablaMovimientos().getTabla().getSelectedRow(), PanelTablaMovimientos.COL_OBJ)).aceptarVisitor(racv);
					}
				}
			});
		}
		return btnEnviarDocumentoContablePorEmail;
	}

	public void enviarDocumentoContablePorEmail(final DocumentoContableCliente documentoContable) {
		final Cliente clienteBuscado = getClienteBuscado();
		if (clienteBuscado != null) {
			new JDialogDestinatariosEmail(this, clienteBuscado.getEmail(), new PerformEnvioEmailHandler() {
				@Override
				public void performEnvio(final List<String> to, final List<String> cc) {
					GenericUtils.realizarOperacionConDialogoDeEspera("Enviando " + documentoContable.getTipoDocumento().toString().toLowerCase().replaceAll("_", " ") + " a: " + getClienteBuscado().getRazonSocial(), new BackgroundTask() {
						public void perform() {
							try {
								DocumentoContableCliente documentoContable2 = null;
								if (documentoContable instanceof Factura) {
									FacturaFacadeRemote ffr = GTLBeanFactory.getInstance().getBean(FacturaFacadeRemote.class);
									documentoContable2 = ffr.getByIdEager(documentoContable.getId());
								} else if (documentoContable instanceof CorreccionFactura) {
									CorreccionFacadeRemote cfr = GTLBeanFactory.getInstance().getBean(CorreccionFacadeRemote.class);
									documentoContable2 = cfr.getCorreccionById(documentoContable.getId());
								}
								JasperPrint jasperPrint = new ImpresionFacturaHandler(documentoContable2, "1").getJasperPrint();
								Integer nroRemito = null;
								if (documentoContable2.getTipoDocumento() == ETipoDocumento.FACTURA && !((Factura)documentoContable2).getRemitos().isEmpty()) {
									nroRemito = ((Factura)documentoContable2).getRemitos().get(0).getNroRemito();
								}
								EmailSender.enviarDocumentoContablePorEmail(documentoContable.getTipoDocumento(), documentoContable.getNroFactura(), nroRemito, jasperPrint, to, cc, getClienteBuscado().getId(), ETipoBusquedaAgenda.CLIENTE);
							} catch (Exception ex) {
								FWJOptionPane.showErrorMessage(JFrameVerMovimientos.this, "Ha ocurrido un error al enviar el email", "Error");
								ex.printStackTrace();
							}
						}
					});
				}
			}, getClienteBuscado().getId(), ETipoBusquedaAgenda.CLIENTE).setVisible(true);
		}
	}

	private JButton getBtnEnviarExtractoCuentaPorEmail() {
		if (btnEnviarExtractoCuentaPorEmail == null) {
			btnEnviarExtractoCuentaPorEmail = BossEstilos.createButton("ar/com/fwcommon/imagenes/b_subir.png", "ar/com/fwcommon/imagenes/b_subir_des.png");
			btnEnviarExtractoCuentaPorEmail.setToolTipText("Enviar resumen de cuenta por email");
			btnEnviarExtractoCuentaPorEmail.setEnabled(false);
			btnEnviarExtractoCuentaPorEmail.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					final Cliente cliente = loadClienteBuscado();
					if (cliente != null) {
						new JDialogDestinatariosEmail(JFrameVerMovimientos.this, cliente.getEmail(), new PerformEnvioEmailHandler() {
							@Override
							public void performEnvio(final List<String> to, final List<String> cc) {
								GenericUtils.realizarOperacionConDialogoDeEspera("Enviando resumen de cuenta a: " + getClienteBuscado().getRazonSocial(), new BackgroundTask() {
									public void perform() {
										try {
											EmailSender.enviarResumenCuentaPorEmail(createJasperResumenCuenta(cliente), to, cc, getClienteBuscado().getId(), ETipoBusquedaAgenda.CLIENTE);
											String textoMensaje = "Se ha enviado la cotización por correo a: " + StringUtil.getCadena(to, ", ");
											if (to != null && !to.isEmpty()) {
												textoMensaje +=  "\nCon copia a: " + StringUtil.getCadena(cc, ", ");
											}
											FWJOptionPane.showInformationMessage(JFrameVerMovimientos.this, StringW.wordWrap(textoMensaje), "Información");
										} catch (Exception ex) {
											FWJOptionPane.showErrorMessage(JFrameVerMovimientos.this, "Ha ocurrido un error al enviar el email", "Error");
											ex.printStackTrace();
										}
									}
								});
							}
						}, getClienteBuscado().getId(), ETipoBusquedaAgenda.CLIENTE).setVisible(true);
					}
				}
			});
		}
		return btnEnviarExtractoCuentaPorEmail;
	}

	private static final String ARCHIVO_JASPER_RESUMEN_CUENTA = "/ar/com/textillevel/reportes/resumenCuenta.jasper";

	public JasperPrint createJasperResumenCuenta(Cliente cliente) {
		JasperReport reporte = JasperHelper.loadReporte(ARCHIVO_JASPER_RESUMEN_CUENTA);
		return JasperHelper.fillReport(reporte, getParametersResumenCuenta(cliente), getPanelTablaMovimientos().createListaMovimientosTO());
	}

	private Map<String, Object> getParametersResumenCuenta(Cliente cliente) {
		Map<String, Object> mapa = new HashMap<String, Object>();
		if (getChkUltimosMovimientos().isSelected()) {
			mapa.put("FECHA", "DETALLE DE ULTIMOS 20 MOVIMIENTOS");
		} else {
			java.sql.Date fechaDesde = new java.sql.Date(getDPFechaDesde().getDate().getTime());
			java.sql.Date fechaHasta = DateUtil.getManiana(new java.sql.Date(getDPFechaHasta().getDate().getTime()));
			mapa.put("FECHA", "DE " + DateUtil.dateToString(fechaDesde, DateUtil.SHORT_DATE) + " A " + DateUtil.dateToString(fechaHasta, DateUtil.SHORT_DATE));
		}
		mapa.put("DIRECCION", cliente.getDireccionFiscal().getDireccion());
		mapa.put("LOCALIDAD", cliente.getDireccionFiscal().getLocalidad().getNombreLocalidad());
		mapa.put("TELEFONO", cliente.getTelefono());
		mapa.put("MAIL", cliente.getEmail());
		mapa.put("SALDO", "$ " + getTxtTotalCuenta().getText());
		mapa.put("CLIENTE", cliente.getRazonSocial());
		mapa.put("IS_TEST", GenericUtils.isSistemaTest());
		return mapa;
	}

	public static class MovimientoTO implements Serializable {

		private static final long serialVersionUID = 1L;

		private String descripcion;
		private String debe;
		private String haber;
		private String saldo;

		public String getDescripcion() {
			return descripcion;
		}

		public void setDescripcion(String descripcion) {
			this.descripcion = descripcion;
		}

		public String getDebe() {
			return debe;
		}

		public void setDebe(String debe) {
			this.debe = debe;
		}

		public String getHaber() {
			return haber;
		}

		public void setHaber(String haber) {
			this.haber = haber;
		}

		public String getSaldo() {
			return saldo;
		}

		public void setSaldo(String saldo) {
			this.saldo = saldo;
		}
	}

//	public JButton getBtnEliminarRecibo() {
//	if (btnEliminarRecibo == null) {
//		btnEliminarRecibo = new JButton("   Eliminar recibo/NC",ImageUtil.loadIcon("ar/com/textillevel/imagenes/b_eliminar.png"));
//		btnEliminarRecibo.setDisabledIcon(ImageUtil.loadIcon("ar/com/textillevel/imagenes/b_eliminar_des.png"));
//		btnEliminarRecibo.setToolTipText("Eliminar recibo");
//		btnEliminarRecibo.setEnabled(false);
//		btnEliminarRecibo.addActionListener(new ActionListener() {
//
//			public void actionPerformed(ActionEvent e) {
//				if (getPanelTablaMovimientos().getTabla().getSelectedRow() > -1) {
//					realizarAccionEliminarRecibo();
//				}
//			}
//		});
//	}
//	return btnEliminarRecibo;
//}

	private JButton getBtnAgregarRecibo() {
		if (btnAgregarRecibo == null) {
			btnAgregarRecibo = BossEstilos.createButton("ar/com/textillevel/imagenes/b_rechazar_cheque.png", "ar/com/textillevel/imagenes/b_rechazar_cheque_des.png");
			btnAgregarRecibo.setEnabled(false);
			btnAgregarRecibo.setToolTipText("Agregar recibo");
			btnAgregarRecibo.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Recibo recibo = new Recibo();
					recibo.setCliente(getClienteBuscado());
					recibo.setNroRecibo(getProximoNroRecibo());
					JDialogCargaRecibo dialogCargarRecibo = new JDialogCargaRecibo(JFrameVerMovimientos.this, recibo, false);
					GuiUtil.centrar(dialogCargarRecibo);
					dialogCargarRecibo.setVisible(true);
					buscarMovimientos();
				}

				private Integer getProximoNroRecibo() {
					ReciboFacadeRemote reciboFacade = GTLBeanFactory.getInstance().getBean2(ReciboFacadeRemote.class);
					Integer lastNroRecibo = reciboFacade.getLastNroRecibo();
					if (lastNroRecibo == null) {
						ParametrosGeneralesFacadeRemote paramGeneralesFacade = GTLBeanFactory.getInstance().getBean2(ParametrosGeneralesFacadeRemote.class);
						Integer nroComienzoRecibo = paramGeneralesFacade.getParametrosGenerales().getNroComienzoRecibo();
						if (nroComienzoRecibo == null) {
							throw new RuntimeException("Falta configurar el número de comienzo de recibo en los parámetros generales");
						}
						lastNroRecibo = nroComienzoRecibo;
					} else {
						lastNroRecibo++;
					}
					return lastNroRecibo;
				}
			});
		}
		return btnAgregarRecibo;
	}

	public JButton getBtnAnular() {
		if (btnAnular == null) {
			btnAnular = BossEstilos.createButton("ar/com/textillevel/imagenes/b_anular_recibo.png", "ar/com/textillevel/imagenes/b_anular_recibo_des.png");
			btnAnular.setToolTipText("Anular");
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
		if (!GTLGlobalCache.getInstance().getUsuarioSistema().getPerfil().getIsAdmin()) {
			JDialogPasswordInput jDialogPasswordInput = new JDialogPasswordInput(JFrameVerMovimientos.this, "Confirmar movimiento");
			if (jDialogPasswordInput.isAcepto()) {
				String pass = new String(jDialogPasswordInput.getPassword());
				UsuarioSistema usrAdmin = GTLBeanFactory.getInstance().getBean2(UsuarioSistemaFacadeRemote.class).esPasswordDeAdministrador(pass);
				if (usrAdmin != null) {
					if (getUsuarioAdministrador() == null) {
						setUsuarioAdministrador(usrAdmin);
					}
					AccionConfirmarCuentaVisitor racv = new AccionConfirmarCuentaVisitor(JFrameVerMovimientos.this);
					((MovimientoCuenta) getPanelTablaMovimientos().getTabla().getValueAt(getPanelTablaMovimientos().getTabla().getSelectedRow(), PanelTablaMovimientos.COL_OBJ)).aceptarVisitor(racv);
				} else {
					FWJOptionPane.showErrorMessage(JFrameVerMovimientos.this, "La clave ingresada no peternece a un usuario administrador", "Error");
				}
			}
		} else {
			setUsuarioAdministrador(GTLGlobalCache.getInstance().getUsuarioSistema());
			AccionConfirmarCuentaVisitor racv = new AccionConfirmarCuentaVisitor(JFrameVerMovimientos.this);
			((MovimientoCuenta) getPanelTablaMovimientos().getTabla().getValueAt(getPanelTablaMovimientos().getTabla().getSelectedRow(), PanelTablaMovimientos.COL_OBJ)).aceptarVisitor(racv);
		}
	}

	private void realizarAccionAnular() {
		if (!GTLGlobalCache.getInstance().getUsuarioSistema().getPerfil().getIsAdmin()) {
			JDialogPasswordInput jDialogPasswordInput = new JDialogPasswordInput(JFrameVerMovimientos.this, "Anular movimiento");
			if (jDialogPasswordInput.isAcepto()) {
				String pass = new String(jDialogPasswordInput.getPassword());
				UsuarioSistema usrAdmin = GTLBeanFactory.getInstance().getBean2(UsuarioSistemaFacadeRemote.class).esPasswordDeAdministrador(pass);
				if (usrAdmin != null) {
					if (getUsuarioAdministrador() == null) {
						setUsuarioAdministrador(usrAdmin);
					}
					AccionAnularCuentaVisitor racv = new AccionAnularCuentaVisitor(JFrameVerMovimientos.this);
					((MovimientoCuenta) getPanelTablaMovimientos().getTabla().getValueAt(getPanelTablaMovimientos().getTabla().getSelectedRow(), PanelTablaMovimientos.COL_OBJ)).aceptarVisitor(racv);
				} else {
					FWJOptionPane.showErrorMessage(JFrameVerMovimientos.this, "La clave ingresada no peternece a un usuario administrador", "Error");
				}
			}
		} else {
			setUsuarioAdministrador(GTLGlobalCache.getInstance().getUsuarioSistema());
			AccionAnularCuentaVisitor racv = new AccionAnularCuentaVisitor(JFrameVerMovimientos.this);
			((MovimientoCuenta) getPanelTablaMovimientos().getTabla().getValueAt(getPanelTablaMovimientos().getTabla().getSelectedRow(), PanelTablaMovimientos.COL_OBJ)).aceptarVisitor(racv);
		}
	}

	private void realizarAccionEliminarFactura() {
		if (!GTLGlobalCache.getInstance().getUsuarioSistema().getPerfil().getIsAdmin()) {
			JDialogPasswordInput jDialogPasswordInput = new JDialogPasswordInput(JFrameVerMovimientos.this, "Eliminar movimiento");
			if (jDialogPasswordInput.isAcepto()) {
				String pass = new String(jDialogPasswordInput.getPassword());
				UsuarioSistema usrAdmin = GTLBeanFactory.getInstance().getBean2(UsuarioSistemaFacadeRemote.class).esPasswordDeAdministrador(pass);
				if (usrAdmin != null) {
					if (getUsuarioAdministrador() == null) {
						setUsuarioAdministrador(usrAdmin);
					}
					AccionEliminarFacturaCuentaVisitor racv = new AccionEliminarFacturaCuentaVisitor(JFrameVerMovimientos.this);
					((MovimientoCuenta) getPanelTablaMovimientos().getTabla().getValueAt(getPanelTablaMovimientos().getTabla().getSelectedRow(), PanelTablaMovimientos.COL_OBJ)).aceptarVisitor(racv);
				} else {
					FWJOptionPane.showErrorMessage(JFrameVerMovimientos.this, "La clave ingresada no peternece a un usuario administrador", "Error");
				}
			}
		} else {
			setUsuarioAdministrador(GTLGlobalCache.getInstance().getUsuarioSistema());
			AccionEliminarFacturaCuentaVisitor racv = new AccionEliminarFacturaCuentaVisitor(JFrameVerMovimientos.this);
			((MovimientoCuenta) getPanelTablaMovimientos().getTabla().getValueAt(getPanelTablaMovimientos().getTabla().getSelectedRow(), PanelTablaMovimientos.COL_OBJ)).aceptarVisitor(racv);
		}
	}

//	private void realizarAccionEliminarRecibo() {
//	JDialogPasswordInput jDialogPasswordInput = new JDialogPasswordInput(JFrameVerMovimientos.this);
//	if (jDialogPasswordInput.isAcepto()) {
//		String pass = new String(jDialogPasswordInput.getPassword());
//		UsuarioSistema usrAdmin = GTLBeanFactory.getInstance().getBean2(UsuarioSistemaFacadeRemote.class).esPasswordDeAdministrador(pass);
//		if (usrAdmin != null) {
//			if(getUsuarioAdministrador()==null){
//				setUsuarioAdministrador(usrAdmin);
//			}
//			AccionEliminarReciboCuentaVisitor racv = new AccionEliminarReciboCuentaVisitor(JFrameVerMovimientos.this);
//			((MovimientoCuenta) getPanelTablaMovimientos().getTabla().getValueAt(getPanelTablaMovimientos().getTabla().getSelectedRow(), PanelTablaMovimientos.COL_OBJ)).aceptarVisitor(racv);
//		} else {
//			CLJOptionPane.showErrorMessage(JFrameVerMovimientos.this, "La clave ingresada no peternece a un usuario administrador", "Error");
//		}
//	}
//}
	
	public void eliminarCorreccion(CorreccionFactura correccion) {
		try {
			if (FWJOptionPane.showQuestionMessage(this, "Está seguro que desea eliminar la " + correccion.getTipo().getDescripcion() + "?", "Pregunta") == FWJOptionPane.YES_OPTION) {
				if (correccion instanceof NotaDebito) {
					NotaDebito nd = (NotaDebito) correccion;
					if (nd.getChequeRechazado() != null) {
						if (FWJOptionPane.showQuestionMessage(this, StringW.wordWrap("Esta es una nota de débito por rechazo de cheque. Se volverá el cheque implicado al estado anterior y se eliminará la nota de débito del proveedor. Desea continuar?"), "Pregunta") == FWJOptionPane.NO_OPTION) {
							return;
						}
					}
				}
				getCorreccionFacade().eliminarCorreccion(correccion, getUsuarioAdministrador().getUsrName());
				buscarMovimientos();
			}
		} catch (ValidacionException vle) {
			FWJOptionPane.showErrorMessage(this, vle.getMensajeError(), "Error");
		} catch (FWException cle) {
			BossError.gestionarError(cle);
		}
	}

	public void eliminarFactura(Factura factura) {
//		if(factura.getEstadoImpresion()==EEstadoImpresionDocumento.IMPRESO){
//		if(CLJOptionPane.showQuestionMessage(this, "No se puede eliminar la factura debido a que ya ha sido impresa. Desea anularla?", "Pregunta") == CLJOptionPane.YES_OPTION){
//			anularFactura(factura);
//		}
//	}else{
		try {

			JDialogSeleccionarDocumentoABorrar jds = new JDialogSeleccionarDocumentoABorrar(this);
			jds.setVisible(true);
			if (jds.isAcepto()) {
				if (jds.isBorrarFactura()) {
					getFacturaFacade().eliminarFactura(factura, getUsuarioAdministrador().getUsrName());
				}
				if (jds.isBorrarRemitoSalida() && factura.getRemitos() != null) {
					for(RemitoSalida rs : factura.getRemitos()) {
						getRemitoSalidaFacade().eliminarRemitoSalida(rs.getId(), GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName());
					}
				}
				if (jds.isBorrarRemitoEntrada()) {
					// TODO: no sé como llegar a este
				}
				FWJOptionPane.showInformationMessage(this, "La operación ha finalizado con éxito", "Información");
				buscarMovimientos();
			}
		} catch (ValidacionException e) {
			FWJOptionPane.showErrorMessage(this, e.getMensajeError(), "Error");
		} catch (FWException e) {
			BossError.gestionarError(e);
		}
		// }
	}

	public void eliminarRecibo(Recibo recibo) {
		JDialogTratarOperacionRecibo dialogo = new JDialogTratarOperacionRecibo(this);
		dialogo.setVisible(true);
		if (dialogo.isAcepto()) {
			if (dialogo.isEliminarRecibo()) {
				if (FWJOptionPane.showQuestionMessage(this, "¿Está seguro que desea eliminar el recibo?", "Confirmación") == FWJOptionPane.YES_OPTION) {
					try {
						getReciboFacade().checkEliminacionRecibo(recibo.getId());
						getReciboFacade().borrarRecibo(recibo, GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName());
						FWJOptionPane.showInformationMessage(this, "La operación ha finalizado con éxito", "Información");
						buscarMovimientos();
					} catch (ValidacionException e) {
						FWJOptionPane.showInformationMessage(this, StringW.wordWrap(e.getMensajeError()), "Información");
					}
				}
			} else {
				if (FWJOptionPane.showQuestionMessage(this, "¿Está seguro que desea editar el recibo?", "Confirmación") == FWJOptionPane.YES_OPTION) {
					editarRecibo(recibo);
				}
			}
		}
	}

	public void anularCorreccion(CorreccionFactura correccion) {
		try {
			getCorreccionFacade().anularCorreccion(correccion, getUsuarioAdministrador().getUsrName());
			FWJOptionPane.showInformationMessage(this, StringW.wordWrap("La " + correccion.getTipo().getDescripcion()) + " se ha anulado correctamente", "Información");
			buscarMovimientos();
		} catch (ValidacionException e) {
			FWJOptionPane.showErrorMessage(this, e.getMensajeError(), "Error");
		} catch (FWException e) {
			BossError.gestionarError(e);
		}
	}

	public void anularFactura(Factura factura) {
		try {
			boolean anularRemitoSalida = false;
			if (factura.getRemitos() != null) {
				int respuesta = -1;
				respuesta = FWJOptionPane.showQuestionMessageWithCancelOption(this, StringW.wordWrap("¿Desea anular el Remito de Salida también? Si presiona 'Cancelar' se cancela la operación completa."), "Confirmación");
				if (respuesta == FWJOptionPane.CANCEL_OPTION) {
					return;
				} else {
					anularRemitoSalida = respuesta == FWJOptionPane.YES_OPTION;
				}
			}
			getFacturaFacade().anularFactura(factura, anularRemitoSalida, getUsuarioAdministrador().getUsrName());
			String msgPostAccion = anularRemitoSalida ? "La Factura y el Remito de Salida han sido anulados con éxito" : "La Factura ha sido anulada con éxito";
			FWJOptionPane.showInformationMessage(this, StringW.wordWrap(msgPostAccion), "Información");
			buscarMovimientos();
		} catch (ValidacionException e) {
			FWJOptionPane.showErrorMessage(this, e.getMensajeError(), "Error");
		} catch (FWException e) {
			BossError.gestionarError(e);
		}
	}

	public void confirmarFactura(Factura factura) {
		getFacturaFacade().cambiarEstadoFactura(factura, EEstadoFactura.VERIFICADA, getUsuarioAdministrador().getUsrName());
		buscarMovimientos();
	}

	public void confirmarNotaDebito(NotaDebito nd) {
		getCorreccionFacade().cambiarVerificacionCorreccion(nd, true, getUsuarioAdministrador().getUsrName());
		buscarMovimientos();
	}

	public void confirmarCredito(NotaCredito nc) {
		getCorreccionFacade().cambiarVerificacionCorreccion(nc, true, getUsuarioAdministrador().getUsrName());
		buscarMovimientos();
	}

	public void anularRecibo(Recibo recibo) {
		try {
			getReciboFacade().anularRecibo(recibo, getUsuarioAdministrador().getUsrName());
			FWJOptionPane.showInformationMessage(this, "El recibo ha sido anulado con éxito", "Información");
			buscarMovimientos();
		} catch (FWException cle) {
			BossError.gestionarError(cle);
		}
	}

	public void confirmarRecibo(Recibo r) {
		getReciboFacade().cambiarEstadoRecibo(r, EEstadoRecibo.ACEPTADO, getUsuarioAdministrador().getUsrName());
		buscarMovimientos();
	}

	public FacturaFacadeRemote getFacturaFacade() {
		if (facturaFacade == null) {
			facturaFacade = GTLBeanFactory.getInstance().getBean2(FacturaFacadeRemote.class);
		}
		return facturaFacade;
	}

	public ReciboFacadeRemote getReciboFacade() {
		if (reciboFacade == null) {
			reciboFacade = GTLBeanFactory.getInstance().getBean2(ReciboFacadeRemote.class);
		}
		return reciboFacade;
	}

	public RemitoSalidaFacadeRemote getRemitoSalidaFacade() {
		if (remitoSalidaFacade == null) {
			remitoSalidaFacade = GTLBeanFactory.getInstance().getBean2(RemitoSalidaFacadeRemote.class);
		}
		return remitoSalidaFacade;
	}

	public RemitoEntradaFacadeRemote getRemitoEntradaFacade() {
		if (remitoEntradaFacade == null) {
			remitoEntradaFacade = GTLBeanFactory.getInstance().getBean2(RemitoEntradaFacadeRemote.class);
		}
		return remitoEntradaFacade;
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
						mostrarFileChooser("Listado de Movimientos - Cliente Nro - " + getTxtBusquedaCliente().getText(), EXTENSION_EXCEL);
						File archivoIngresado = FWFileSelector.obtenerArchivo(FWFileSelector.SAVE, FWFileSelector.FILES_ONLY, new FiltroArchivosExcel(), null);
						if (archivoIngresado != null) {
							if (!archivoIngresado.getAbsolutePath().toLowerCase().endsWith(EXTENSION_EXCEL)) {
								archivoIngresado = new File(archivoIngresado.getAbsolutePath().concat(EXTENSION_EXCEL));
							}
							GenericUtils.exportarAExcel(tabla, "  Saldo: " + getTxtTotalCuenta().getText(), "  " + getLblNombre().getText() + " - " + getLblDireccion().getText() + " - "
									+ getLblCuit().getText() + " - " + getLblTelefono().getText() + " " + getLblCondicionVenta().getText(), null, archivoIngresado.getAbsolutePath(), System.getProperty("intercalarColoresFilas") != null
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
					try {
						JasperHelper.imprimirReporte(createJasperResumenCuenta(loadClienteBuscado()), true, false, 1);
					} catch (JRException e1) {
						e1.printStackTrace();
						if (FWJOptionPane.showQuestionMessage(JFrameVerMovimientos.this, StringW.wordWrap("Ha ocurrido un error al imprimir el nuevo extracto de cuenta. Desea imprimir de la forma anterior ?"), "Pregunta") == FWJOptionPane.YES_OPTION) {
							JasperHelper.imprimirListado(getPanelTablaMovimientos().getTabla(), "  Saldo: " + getTxtTotalCuenta().getText(), "  " + getLblNombre().getText() + " - "
									+ getLblDireccion().getText() + " - " + getLblCuit().getText() + " - " + getLblTelefono().getText()+ " " + getLblCondicionVenta().getText(), null, false);
						}
					}
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
						mostrarFileChooser("Listado de Movimientos - Cliente Nro - " + getTxtBusquedaCliente().getText(), EXTENSION_PDF);
						File archivoIngresado = FWFileSelector.obtenerArchivo(FWFileSelector.SAVE, FWFileSelector.FILES_ONLY, new FiltroArchivosPDF(), null);
						if (archivoIngresado != null) {
							if (!archivoIngresado.getAbsolutePath().toLowerCase().endsWith(EXTENSION_PDF)) {
								archivoIngresado = new File(archivoIngresado.getAbsolutePath().concat(EXTENSION_PDF));
							}
							JasperHelper.listadoAPDF(tabla, "  Saldo: " + getTxtTotalCuenta().getText(), "  " + getLblNombre().getText() + " - " + getLblDireccion().getText() + " - "
									+ getLblCuit().getText() + " - " + getLblTelefono().getText() + " " + getLblCondicionVenta().getText(), null, false, archivoIngresado.getAbsolutePath());
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
				FWJOptionPane.showErrorMessage(JFrameVerMovimientos.this, "Se ha producido un error al guardar el archivo.\n" + e1.getMessage(), "Error");
				return;
			}
			File archivoSugerido = new File(nombreSugerido.endsWith(extension) ? nombreSugerido : nombreSugerido.concat(extension));
			FWFileSelector.setLastSelectedFile(archivoSugerido);
		}
	}

	private JPanel getPanelDatosUsuario() {
		if (panelDatosUsuario == null) {
			panelDatosUsuario = new JPanel();
			panelDatosUsuario.setLayout(new VerticalFlowLayout(VerticalFlowLayout.TOP, 5, 2));
			panelDatosUsuario.add(getLblNombre());
			panelDatosUsuario.add(getLblDireccion());
			panelDatosUsuario.add(getLblCuit());
			panelDatosUsuario.add(getLblTelefono());
			panelDatosUsuario.add(getLblCondicionVenta());
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

	private JLabel getLblCondicionVenta() {
		if (lblCondicionVenta == null) {
			lblCondicionVenta = new JLabel();
			Font fuente = lblCondicionVenta.getFont();
			lblCondicionVenta.setFont(new Font(fuente.getName(), Font.BOLD, fuente.getSize() + 3));
		}
		return lblCondicionVenta;
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

	private JCheckBox getChkUltimosMovimientos() {
		if (chkUltimosMovimientos == null) {
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

	public CorreccionFacadeRemote getCorreccionFacade() {
		if (correccionFacade == null) {
			correccionFacade = GTLBeanFactory.getInstance().getBean2(CorreccionFacadeRemote.class);
		}
		return correccionFacade;
	}

	private class JDialogSeleccionarDocumentoABorrar extends JDialog {

		private static final long serialVersionUID = -8979845617144032895L;

		private boolean acepto;
		private boolean borrarFactura;
		private boolean borrarRemitoSalida;
		private boolean borrarRemitoEntrada;

		private JButton btnAceptar;
		private JButton btnSalir;

		private JCheckBox chkBorrarFactura;
		private JCheckBox chkBorrarRemitoSalida;
		private JCheckBox chkBorrarRemitoEntrada;

		private JPanel panelBotones;
		private JPanel panelCentral;

		public JDialogSeleccionarDocumentoABorrar(Frame padre) {
			super(padre);
			this.setUpComponentes();
			this.setUpScreen();
		}

		private void setUpScreen() {
			this.setTitle("Seleccione los documentos que desea borrar");
			this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			GuiUtil.centrar(this);
			this.setModal(true);
			this.setSize(new Dimension(300, 150));
			this.setResizable(false);
		}

		private void setUpComponentes() {
			this.add(getPanelCentral(), BorderLayout.CENTER);
			this.add(getPanelBotones(), BorderLayout.SOUTH);
		}

		public boolean isAcepto() {
			return acepto;
		}

		public void setAcepto(boolean acepto) {
			this.acepto = acepto;
		}

		public boolean isBorrarFactura() {
			return borrarFactura;
		}

		public void setBorrarFactura(boolean borrarFactura) {
			this.borrarFactura = borrarFactura;
		}

		public boolean isBorrarRemitoSalida() {
			return borrarRemitoSalida;
		}

		public void setBorrarRemitoSalida(boolean borrarRemitoSalida) {
			this.borrarRemitoSalida = borrarRemitoSalida;
		}

		public boolean isBorrarRemitoEntrada() {
			return borrarRemitoEntrada;
		}

		public void setBorrarRemitoEntrada(boolean borrarRemitoEntrada) {
			this.borrarRemitoEntrada = borrarRemitoEntrada;
		}

		public JButton getBtnAceptar() {
			if (btnAceptar == null) {
				btnAceptar = new JButton("Aceptar");
				btnAceptar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						setearDatos();
						setAcepto(true);
						dispose();
					}
				});
			}
			return btnAceptar;
		}

		private void setearDatos() {
			setBorrarFactura(getChkBorrarFactura().isSelected());
			setBorrarRemitoEntrada(getChkBorrarRemitoEntrada().isSelected());
			setBorrarRemitoSalida(getChkBorrarRemitoSalida().isSelected());
		}

		public JButton getBtnSalir() {
			if (this.btnSalir == null) {
				this.btnSalir = new JButton("Cancelar");
				this.btnSalir.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						setAcepto(false);
						dispose();
					}
				});
			}
			return this.btnSalir;
		}

		public JCheckBox getChkBorrarFactura() {
			if (chkBorrarFactura == null) {
				chkBorrarFactura = new JCheckBox("Borrar factura");
			}
			return chkBorrarFactura;
		}

		public JCheckBox getChkBorrarRemitoSalida() {
			if (chkBorrarRemitoSalida == null) {
				chkBorrarRemitoSalida = new JCheckBox("Borrar remito de salida");
				chkBorrarRemitoSalida.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						getChkBorrarFactura().setSelected(chkBorrarRemitoSalida.isSelected());
					}
				});
			}
			return chkBorrarRemitoSalida;
		}

		public JCheckBox getChkBorrarRemitoEntrada() {
			if (chkBorrarRemitoEntrada == null) {
				chkBorrarRemitoEntrada = new JCheckBox("Borrar remito de entrada");
				chkBorrarRemitoEntrada.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						getChkBorrarFactura().setSelected(chkBorrarRemitoEntrada.isSelected());
						getChkBorrarRemitoSalida().setSelected(chkBorrarRemitoEntrada.isSelected());
					}
				});
			}
			return chkBorrarRemitoEntrada;
		}

		public JPanel getPanelBotones() {
			if (panelBotones == null) {
				panelBotones = new JPanel();
				panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
				panelBotones.add(getBtnAceptar());
				panelBotones.add(getBtnSalir());
			}
			return panelBotones;
		}

		public JPanel getPanelCentral() {
			if (panelCentral == null) {
				panelCentral = new JPanel();
				panelCentral.setLayout(new GridLayout(3, 1));
				panelCentral.add(getChkBorrarFactura());
				panelCentral.add(getChkBorrarRemitoSalida());
				panelCentral.add(getChkBorrarRemitoEntrada());
			}
			return panelCentral;
		}
	}

	private class JDialogTratarOperacionRecibo extends JDialog {

		private static final long serialVersionUID = -6190180108168188694L;

		private boolean acepto;
		private boolean eliminarRecibo;
		private boolean editarRecibo;

		private JButton btnAceptar;
		private JButton btnSalir;

		private JRadioButton rbtEliminarRecibo;
		private JRadioButton rbtEditarRecibo;

		private JPanel panelBotones;
		private JPanel panelCentral;

		public JDialogTratarOperacionRecibo(Frame padre) {
			super(padre);
			this.setUpComponentes();
			this.setUpScreen();
			getRbtEditarRecibo().setSelected(true);
		}

		private void setUpScreen() {
			this.setTitle("Seleccione el tipo de operación");
			this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			GuiUtil.centrar(this);
			this.setModal(true);
			this.setSize(new Dimension(300, 150));
			this.setResizable(false);
		}

		private void setUpComponentes() {
			this.add(getPanelCentral(), BorderLayout.CENTER);
			this.add(getPanelBotones(), BorderLayout.SOUTH);
		}

		public boolean isAcepto() {
			return acepto;
		}

		public void setAcepto(boolean acepto) {
			this.acepto = acepto;
		}

		public boolean isEliminarRecibo() {
			return eliminarRecibo;
		}

		public void setEliminarRecibo(boolean eliminarRecibo) {
			this.eliminarRecibo = eliminarRecibo;
		}

		@SuppressWarnings("unused")
		public boolean isEditarRecibo() {
			return editarRecibo;
		}

		public void setEditarRecibo(boolean editarRecibo) {
			this.editarRecibo = editarRecibo;
		}

		public JButton getBtnAceptar() {
			if (btnAceptar == null) {
				btnAceptar = new JButton("Aceptar");
				btnAceptar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						setearDatos();
						setAcepto(true);
						dispose();
					}
				});
			}
			return btnAceptar;
		}

		private void setearDatos() {
			setEliminarRecibo(getRbtEliminarRecibo().isSelected());
			setEditarRecibo(getRbtEditarRecibo().isSelected());
		}

		public JButton getBtnSalir() {
			if (this.btnSalir == null) {
				this.btnSalir = new JButton("Cancelar");
				this.btnSalir.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						setAcepto(false);
						dispose();
					}
				});
			}
			return this.btnSalir;
		}

		public JPanel getPanelBotones() {
			if (panelBotones == null) {
				panelBotones = new JPanel();
				panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
				panelBotones.add(getBtnAceptar());
				panelBotones.add(getBtnSalir());
			}
			return panelBotones;
		}

		public JPanel getPanelCentral() {
			if (panelCentral == null) {
				panelCentral = new JPanel();
				panelCentral.setLayout(new GridLayout(2, 1));
				panelCentral.add(getRbtEditarRecibo());
				panelCentral.add(getRbtEliminarRecibo());
				ButtonGroup bgOpcionProceso = new ButtonGroup();
				bgOpcionProceso.add(getRbtEditarRecibo());
				bgOpcionProceso.add(getRbtEliminarRecibo());
			}
			return panelCentral;
		}

		private JRadioButton getRbtEliminarRecibo() {
			if (rbtEliminarRecibo == null) {
				rbtEliminarRecibo = new JRadioButton();
				rbtEliminarRecibo.setText("Eliminar Recibo");
			}
			return rbtEliminarRecibo;
		}

		private JRadioButton getRbtEditarRecibo() {
			if (rbtEditarRecibo == null) {
				rbtEditarRecibo = new JRadioButton();
				rbtEditarRecibo.setText("Editar Recibo");
			}
			return rbtEditarRecibo;
		}
	}

	public UsuarioSistema getUsuarioAdministrador() {
		return usuarioAdministrador;
	}

	public void setUsuarioAdministrador(UsuarioSistema usuarioAdministrador) {
		this.usuarioAdministrador = usuarioAdministrador;
	}

	public Cliente getClienteBuscado() {
		return clienteBuscado;
	}

	public void setClienteBuscado(Cliente clienteBuscado) {
		this.clienteBuscado = clienteBuscado;
	}

	public void editarRecibo(Recibo recibo) {
		try {
			getReciboFacade().checkEdicionRecibo(recibo.getId());
		} catch (ValidacionException e) {
			// si falló otra validación diferente a la esperada
			if (e.getCodigoError() != EValidacionException.RECIBO_IMPOSIBLE_EDITAR_NO_ES_EL_ULTIMO_BY_CLIENTE.getCodigo()) {
				FWJOptionPane.showInformationMessage(this, StringW.wordWrap(e.getMensajeError()), "Información");
				return;
			}
		}
		// edito el recibo
		recibo = getReciboFacade().getByIdEager(recibo.getId());
		JDialogCargaRecibo dialogoCargaRecibo = new JDialogCargaRecibo(this, recibo, false);
		GuiUtil.centrar(dialogoCargaRecibo);
		dialogoCargaRecibo.setVisible(true);
		buscarMovimientos();
	}

	public void editarFactura(Factura factura) {
		try {
			FacturaFacadeRemote ffr = GTLBeanFactory.getInstance().getBean(FacturaFacadeRemote.class);
			Factura f = ffr.getByIdEager(factura.getId());
			JDialogCargaFactura dialogCargaFactura = new JDialogCargaFactura(this, f, false);
			dialogCargaFactura.setVisible(true);
			buscarMovimientos();
		} catch (FWException e) {
			BossError.gestionarError(e);
		}
	}

	public void editarCorreccion(CorreccionFactura correccion) {
		CorreccionFacadeRemote cfr = GTLBeanFactory.getInstance().getBean2(CorreccionFacadeRemote.class);
		correccion = cfr.getCorreccionById(correccion.getId());
		JDialogCargaFactura dialogCargaFactura = new JDialogCargaFactura(this, correccion, false);
		dialogCargaFactura.setVisible(true);
		buscarMovimientos();
	}

	public ListaDePreciosFacadeRemote getListaDePreciosFacade() {
		if (listaDePreciosFacade == null) {
			listaDePreciosFacade = GTLBeanFactory.getInstance().getBean2(ListaDePreciosFacadeRemote.class);
		}
		return listaDePreciosFacade;
	}

	public VersionListaDePrecios getVersionListaDePreciosCotizada() {
		return versionListaDePreciosCotizada;
	}

	private void createJasperPrintCotizacion() {
		Cliente cliente = loadClienteBuscado();
		if (cliente != null) {
			if (cotizacionActual != null) {
				jasperPrintCotizacion = new ImprimirListaDePreciosHandler(JFrameVerMovimientos.this, cliente,
						cotizacionActual.getVersionListaPrecio()).createJasperPrint(cotizacionActual.getValidez() + "", cotizacionActual.getNumero());
			} else if (versionListaDePreciosCotizada != null) {
				jasperPrintCotizacion = new ImprimirListaDePreciosHandler(JFrameVerMovimientos.this, cliente,
						versionListaDePreciosCotizada).createJasperPrint(""+GTLBeanFactory.getInstance().getBean2(ParametrosGeneralesFacadeRemote.class).getParametrosGenerales().getValidezCotizaciones(), null);
			}
		}
	}

	private Cliente loadClienteBuscado() {
		Cliente cliente = getClienteBuscado();
		if (cliente == null) {
			cliente = GTLBeanFactory.getInstance().getBean2(ClienteFacadeRemote.class).getClienteByNumero(getTxtBusquedaCliente().getValue());
			this.clienteBuscado = cliente;
		}
		return cliente;
	}

	public void enviarFacturaORemitoPorMail(Factura factura) {
		String[] docs = new String[]{"FACTURA", "REMITO/S"};
		Object opcion = JOptionPane.showInputDialog(null, "Seleccione el documento que desea enviar:", "Lista de opciones", JOptionPane.INFORMATION_MESSAGE, null, docs,docs[0]);
		if(opcion!=null){
			if (opcion.equals("FACTURA")) {
				enviarDocumentoContablePorEmail(factura);
			} else {
				enviarRemitosPorMail(factura.getRemitos());
			}
		}
	}

	private void enviarRemitosPorMail(final List<RemitoSalida> remitos) {
		final Cliente clienteBuscado = getClienteBuscado();
		if (clienteBuscado != null) {
			new JDialogDestinatariosEmail(this, clienteBuscado.getEmail(), new PerformEnvioEmailHandler() {
				@Override
				public void performEnvio(final List<String> to, final List<String> cc) {
					GenericUtils.realizarOperacionConDialogoDeEspera("Enviando remito/s a: " + getClienteBuscado().getRazonSocial(), new BackgroundTask() {
						public void perform() {
							try {
								List<Integer> ids = new ArrayList<Integer>();
								List<Integer> nros = new ArrayList<Integer>();
								Integer nroSucursal = null;
								Integer nroFactura = null;
								for(RemitoSalida rs : remitos) {
									ids.add(rs.getId());
									nros.add(rs.getNroRemito());
									nroSucursal = rs.getNroSucursal();
									nroFactura = rs.getNroFactura();
								}
								List<RemitoSalida> remitosEager = getRemitoSalidaFacade().getByIdsConPiezasYProductos(ids);
								List<JasperPrint> jasperPrints = ImprimirRemitoHandler.getJasperPrints(remitosEager, nroSucursal);
								EmailSender.enviarRemitoPorEmail(nroFactura, nros, jasperPrints, to, cc, getClienteBuscado().getId(), ETipoBusquedaAgenda.CLIENTE);
							} catch (Exception ex) {
								FWJOptionPane.showErrorMessage(JFrameVerMovimientos.this, "Ha ocurrido un error al enviar el email", "Error");
								ex.printStackTrace();
							}
						}
					});
				}
			}, getClienteBuscado().getId(), ETipoBusquedaAgenda.CLIENTE).setVisible(true);
		}
	}

	public void enviarReciboPorMail(final Recibo recibo) {
		final Cliente clienteBuscado = getClienteBuscado();
		if (clienteBuscado != null) {
			new JDialogDestinatariosEmail(this, clienteBuscado.getEmail(), new PerformEnvioEmailHandler() {
				@Override
				public void performEnvio(final List<String> to, final List<String> cc) {
					GenericUtils.realizarOperacionConDialogoDeEspera("Enviando recibo a: " + getClienteBuscado().getRazonSocial(), new BackgroundTask() {
						public void perform() {
							Recibo reciboEager = getReciboFacade().getByIdEager(recibo.getId());
							JasperPrint jasper = ImprimirReciboHandler.getJasperPrint(reciboEager, GTLBeanFactory.getInstance().getBean2(ParametrosGeneralesFacadeRemote.class).getParametrosGenerales().getNroSucursal());
							try{
								EmailSender.enviarReciboPorEmail(recibo.getNroRecibo(), jasper, to, cc, getClienteBuscado().getId(), ETipoBusquedaAgenda.CLIENTE);
							} catch (Exception ex) {
								FWJOptionPane.showErrorMessage(JFrameVerMovimientos.this, "Ha ocurrido un error al enviar el email", "Error");
								ex.printStackTrace();
							}
						}
					});
				}
			}, clienteBuscado.getId(), ETipoBusquedaAgenda.CLIENTE).setVisible(true);
		}
	}

	private JComboBox getCmbFiltroTipoDocumento() {
		if (cmbFiltroTipoDocumento == null) {
			cmbFiltroTipoDocumento = new JComboBox();
			cmbFiltroTipoDocumento.addItem("TODOS");
			cmbFiltroTipoDocumento.addItem(ETipoDocumento.FACTURA);
			cmbFiltroTipoDocumento.addItem(ETipoDocumento.NOTA_DEBITO);
			cmbFiltroTipoDocumento.addItem(ETipoDocumento.RECIBO);
			cmbFiltroTipoDocumento.addItem(ETipoDocumento.NOTA_CREDITO);
		}
		return cmbFiltroTipoDocumento;
	}

	// private JComboBox getCmbOrdenMovimientos() {
	// if(cmbOrdenMovimientos == null){
	// cmbOrdenMovimientos = new JComboBox();
	// cmbOrdenMovimientos.addItem("MAS ANTIGUO PRIMERO");
	// cmbOrdenMovimientos.addItem("MAS ANTIGUO ULTIMO");
	// }
	// return cmbOrdenMovimientos;
	// }
}
