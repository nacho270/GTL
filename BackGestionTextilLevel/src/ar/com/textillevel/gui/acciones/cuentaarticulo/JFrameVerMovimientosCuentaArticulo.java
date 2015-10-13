package ar.com.textillevel.gui.acciones.cuentaarticulo;

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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.TableCellRenderer;

import main.GTLGlobalCache;
import main.acciones.facturacion.IngresoRemitoSalidaHandler;
import main.acciones.facturacion.OperacionSobreRemitoEntradaHandler;
import main.acciones.facturacion.OperacionSobreRemitoSalidaHandler;
import ar.com.fwcommon.boss.BossEstilos;
import ar.com.fwcommon.componentes.FWFileSelector;
import ar.com.fwcommon.componentes.FWJNumericTextField;
import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTable;
import ar.com.fwcommon.componentes.FWJTextField;
import ar.com.fwcommon.componentes.PanelTablaSinBotones;
import ar.com.fwcommon.componentes.VerticalFlowLayout;
import ar.com.fwcommon.util.DateUtil;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.fwcommon.util.StringUtil;
import ar.com.textillevel.entidades.cuentaarticulo.CuentaArticulo;
import ar.com.textillevel.entidades.cuentaarticulo.movimientos.MovimientoCuentaArticulo;
import ar.com.textillevel.entidades.cuentaarticulo.movimientos.MovimientoDebeCuentaArticulo;
import ar.com.textillevel.entidades.cuentaarticulo.movimientos.MovimientoHaberCuentaArticulo;
import ar.com.textillevel.entidades.documentos.factura.proveedor.FacturaProveedor;
import ar.com.textillevel.entidades.documentos.factura.proveedor.enums.ETipoFacturaProveedor;
import ar.com.textillevel.entidades.documentos.remito.RemitoEntrada;
import ar.com.textillevel.entidades.documentos.remito.RemitoSalida;
import ar.com.textillevel.entidades.documentos.remito.proveedor.RemitoEntradaProveedor;
import ar.com.textillevel.entidades.enums.ETipoRemitoSalida;
import ar.com.textillevel.entidades.enums.EUnidad;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.gente.Proveedor;
import ar.com.textillevel.entidades.portal.UsuarioSistema;
import ar.com.textillevel.entidades.ventas.articulos.Articulo;
import ar.com.textillevel.facade.api.remote.ArticuloFacadeRemote;
import ar.com.textillevel.facade.api.remote.ClienteFacadeRemote;
import ar.com.textillevel.facade.api.remote.CorreccionFacadeRemote;
import ar.com.textillevel.facade.api.remote.CuentaArticuloFacadeRemote;
import ar.com.textillevel.facade.api.remote.ParametrosGeneralesFacadeRemote;
import ar.com.textillevel.facade.api.remote.RemitoEntradaProveedorFacadeRemote;
import ar.com.textillevel.facade.api.remote.RemitoSalidaFacadeRemote;
import ar.com.textillevel.facade.api.remote.UsuarioSistemaFacadeRemote;
import ar.com.textillevel.gui.acciones.JDialogAgregarRemitoEntradaCompraTela;
import ar.com.textillevel.gui.acciones.JDialogAgregarRemitoEntradaStock;
import ar.com.textillevel.gui.acciones.proveedor.JDialogCargarFacturaProveedor;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.JasperHelper;
import ar.com.textillevel.gui.util.controles.PanelDatePicker;
import ar.com.textillevel.gui.util.dialogs.JDialogPasswordInput;
import ar.com.textillevel.gui.util.dialogs.JDialogSeleccionarCliente;
import ar.com.textillevel.gui.util.dialogs.JDialogSeleccionarProveedor;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;
import ar.com.textillevel.util.GTLBeanFactory;

public class JFrameVerMovimientosCuentaArticulo extends JFrame {

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
	private JCheckBox chkFiltrarPorFecha;
	private JButton btnVerificar;
	private JButton btnAgregarObservaciones;
	
	private JButton btnAgregarRemitoEntrada01;
	private JButton btnAgregarRemitoSalida01;
	private JButton btnAgregarRemitoCompraTela;
	private JButton btnAgregarRemitoVentaTela;
	
	private FilaMovimientoCuentaArticuloVisitor filaMovimientoVisitor;

	private CuentaArticuloFacadeRemote cuentaFacade;
	private CorreccionFacadeRemote correccionFacade;
	private RemitoSalidaFacadeRemote remitoSalidaFacade;
	private ClienteFacadeRemote clienteFacade;

	private Cliente clienteElegido;
	private Articulo articuloElegido;

	/** ACCIONES **/

	private JButton btnAgregarRemitoEntrada;
	private JButton btnExportarAExcel;
	private JButton btnListadoPDF;
	private JButton btnImprimirListado;
	
	private JComboBox cmbArticulo;
	
	private UsuarioSistema usuarioAdministrador;

	private JLabel lblArticulo;

	
	public JFrameVerMovimientosCuentaArticulo(Frame padre) {
		setUpComponentes();
		setUpScreen();
		getTxtBusquedaCliente().requestFocus();
		getBtnAgregarRemitoEntrada().setEnabled(false);
	}

	private void setUpScreen() {
		setSize(new Dimension(1000, 600));
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setTitle("Ver movimientos");
		GuiUtil.centrar(this);
		setExtendedState(MAXIMIZED_BOTH);
	}

	private void llenarTablaMovimientos(List<MovimientoCuentaArticulo> movimientos) {
		getPanelTablaMovimientos().getTabla().removeAllRows();
		if (movimientos != null) {
			getPanelTablaMovimientos().agregarElementos(movimientos);
			setSaldoCuenta();
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
		int ret = FWJOptionPane.showQuestionMessage(this, "Va a cerrar el módulo, ¿Está seguro?", "Cuenta de Telas");
		if (ret == FWJOptionPane.YES_OPTION) {
			dispose();
		}
	}

	private class PanelTablaMovimientos extends PanelTablaSinBotones<MovimientoCuentaArticulo> {

		private static final long serialVersionUID = -2675346740708514360L;

		private static final int CANT_COLS_TBL_MOVS = 8;
		private static final int COL_DESCR = 0;
		private static final int COL_DEBE = 1;
		private static final int COL_HABER = 2;
		private static final int COL_SALDO = 3;
		private static final int COL_VERIFICADO = 4;
		private static final int COL_USR_VERIFICACION = 5;
		private static final int COL_OBSERVACIONES = 6;
		private static final int COL_OBJ = 7;

		@Override
		protected void agregarElemento(MovimientoCuentaArticulo mov) {
			mov.aceptarVisitor(filaMovimientoVisitor);
		}

		@Override
		protected FWJTable construirTabla() {
			FWJTable tabla = new FWJTable(0, CANT_COLS_TBL_MOVS);
			tabla.setStringColumn(COL_DESCR, "Descripción", 160, 260, true);
			tabla.setFloatColumn(COL_DEBE, "Debe", 80, true);
			tabla.setFloatColumn(COL_HABER, "Haber", 80, true);
			tabla.setFloatColumn(COL_SALDO, "Saldo", 80, true);
			tabla.setCheckColumn(COL_VERIFICADO, "Verificado", 80, true);
			tabla.setStringColumn(COL_USR_VERIFICACION, "Usuario verificador", 150, 150, true);
			tabla.setStringColumn(COL_OBSERVACIONES, "Observaciones", 150, 150, true);
			tabla.setStringColumn(COL_OBJ, "", 0, 0, true);
			tabla.setReorderingAllowed(false);
			tabla.setHeaderAlignment(COL_DESCR, FWJTable.CENTER_ALIGN);
			tabla.setHeaderAlignment(COL_DEBE, FWJTable.CENTER_ALIGN);
			tabla.setHeaderAlignment(COL_HABER, FWJTable.CENTER_ALIGN);
			tabla.setHeaderAlignment(COL_SALDO, FWJTable.CENTER_ALIGN);
			tabla.setHeaderAlignment(COL_VERIFICADO, FWJTable.CENTER_ALIGN);
			tabla.setHeaderAlignment(COL_USR_VERIFICACION, FWJTable.CENTER_ALIGN);
			tabla.setHeaderAlignment(COL_OBSERVACIONES, FWJTable.CENTER_ALIGN);
			tabla.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseReleased(MouseEvent e) {
					if(getTabla().getSelectedRow() > -1) {
						MovimientoCuentaArticulo mca = (MovimientoCuentaArticulo)getTabla().getValueAt(getTabla().getSelectedRow(), COL_OBJ);
						getBtnVerificar().setEnabled(mca.getUsuarioConfirmacion()==null);
						getBtnAgregarObservaciones().setEnabled(true);
						if(e.getClickCount() == 2) {
							if(mca instanceof MovimientoHaberCuentaArticulo) {
								RemitoEntrada re = ((MovimientoHaberCuentaArticulo)mca).getRemitoEntrada();
								OperacionSobreRemitoEntradaHandler consultaREHandler = new OperacionSobreRemitoEntradaHandler(JFrameVerMovimientosCuentaArticulo.this, re, true);
								consultaREHandler.showRemitoEntradaDialog();
							}
							if(mca instanceof MovimientoDebeCuentaArticulo) {
								RemitoSalida rsEager = getRemitoSalidaFacade().getByIdConPiezasYProductos(((MovimientoDebeCuentaArticulo)mca).getRemitoSalida().getId());
								OperacionSobreRemitoSalidaHandler handler = new OperacionSobreRemitoSalidaHandler(JFrameVerMovimientosCuentaArticulo.this, rsEager, true);
								handler.showRemitoEntradaDialog();
							}
						}
					}else{
						getBtnVerificar().setEnabled(false);
						getBtnAgregarObservaciones().setEnabled(false);
					}
				}
			});

			return tabla;
		}

//		public CellRenderer getCellRenderer() {
//			return new CellRenderer();
//		}

		@Override
		protected MovimientoCuentaArticulo getElemento(int fila) {
			return (MovimientoCuentaArticulo) getTabla().getValueAt(fila, COL_OBJ);
		}

		@Override
		protected String validarElemento(int fila) {
			return null;
		}

		public FilaMovimientoCuentaArticuloVisitor createVisitorFilaMovimientos() {
			return new FilaMovimientoCuentaArticuloVisitor(getTabla());
		}

	}

	public class CellRenderer extends JLabel implements TableCellRenderer {

		private static final long serialVersionUID = 3832001743928342843L;

		Map<Integer, String> mapaFilaTexto = new HashMap<Integer, String>();

		public CellRenderer() {

		}

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			String insert = mapaFilaTexto.get(row) == null ? "" : mapaFilaTexto.get(row);
			setText("<html>" + insert + "</html>");
			return this;
		}

		public void agregarTexto(int fila, String texto) {
			String add = mapaFilaTexto.get(fila);
			if (add == null) {
				add = "";
			}
			add = add.concat(texto);
			mapaFilaTexto.put(fila, add);
		}

		public void clear() {
			mapaFilaTexto.clear();
		}

	}

	private JPanel getPanelCabecera() {
		if (panelCabecera == null) {
			panelCabecera = new JPanel();
			panelCabecera.setLayout(new GridBagLayout());
			JPanel panel = new JPanel(new VerticalFlowLayout());
			panel.setBorder(BorderFactory.createEtchedBorder());

			final Component[] comps = new Component[] { getCmbTipoBusquedaCliente(), getTxtBusquedaCliente(), getCmbArticulo(), getDPFechaDesde(), getDPFechaHasta(), getBtnBuscar() };
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
			
			panelCliente.add(new JLabel("Articulo: "));
			panelCliente.add(getCmbArticulo());
			panelCliente.add(getBtnBuscar());

			JPanel panelFechas = new JPanel();
			panelFechas.setLayout(new FlowLayout());
			panelFechas.add(getChkFiltrarPorFecha());
			panelFechas.add(getDPFechaDesde());
			panelFechas.add(getDPFechaHasta());
			
			JPanel panelFiltros = new JPanel();
			panelFiltros.setLayout(new FlowLayout());
			
			panel.add(panelCliente);
			panel.add(panelFechas);
			panel.add(panelFiltros);

			panelCabecera.add(getPanelDatosUsuario(), new GridBagConstraints(0, 0, 1, 1, 0.4, 0, GridBagConstraints.WEST, GridBagConstraints.LINE_END, new Insets(15, 5, 15, 5), 0, 0));
			panelCabecera.add(panel, new GridBagConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.LINE_END, new Insets(15, 5, 15, 5), 0, 0));
		}
		return panelCabecera;
	}

	private JComboBox getCmbArticulo() {
		if(cmbArticulo == null) {
			cmbArticulo = new JComboBox();
			ArticuloFacadeRemote articuloFacade = GTLBeanFactory.getInstance().getBean2(ArticuloFacadeRemote.class);
			GuiUtil.llenarCombo(cmbArticulo, articuloFacade.getAllOrderByName(), true);
			cmbArticulo.setSelectedItem(GTLGlobalCache.getInstance().getArticuloDefault());
			cmbArticulo.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						articuloElegido = ((Articulo)getCmbArticulo().getSelectedItem());
						if (clienteElegido != null) {
							getTxtBusquedaCliente().setText(String.valueOf(clienteElegido.getNroCliente()));
							getLblNombre().setText(clienteElegido.getRazonSocial());
							getLblDireccion().setText(clienteElegido.getDireccionFiscal().getDireccion() + " " + clienteElegido.getDireccionFiscal().getLocalidad().getNombreLocalidad());
							getLblCuit().setText(clienteElegido.getCuit());
							getLblTelefono().setText(clienteElegido.getTelefono());
							getLblArticulo().setText(articuloElegido.getNombre());
						}
					}
				}
			});
		}
		articuloElegido = (Articulo)cmbArticulo.getSelectedItem();
		return cmbArticulo;
	}

	private JButton getBtnBuscar() {
		if (btnBuscar == null) {
			btnBuscar = new JButton("Buscar");
			btnBuscar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent evt) {
					if(getDPFechaDesde().getDate()==null){
						FWJOptionPane.showErrorMessage(JFrameVerMovimientosCuentaArticulo.this, "La 'fecha desde' ingresada no es válida", "Error");
						return;
					}
					if(getDPFechaHasta().getDate()==null){
						FWJOptionPane.showErrorMessage(JFrameVerMovimientosCuentaArticulo.this, "La 'fecha hasta' ingresada no es válida", "Error");
						return;
					}
					if (!getDPFechaDesde().getDate().after(getDPFechaHasta().getDate())) {
						if (getTxtBusquedaCliente().getText().trim().length() > 0) {
							buscarMovimientos();
						} else {
							FWJOptionPane.showErrorMessage(JFrameVerMovimientosCuentaArticulo.this, "Debe ingresar un cliente", "Error");
						}
					} else {
						FWJOptionPane.showErrorMessage(JFrameVerMovimientosCuentaArticulo.this, "La 'fecha desde' no debe ser posterior a la 'fecha hasta'", "Error");
					}
				}
			});
		}
		return btnBuscar;
	}

	private void buscarMovimientos() {
		if(clienteElegido == null || articuloElegido == null) {
			FWJOptionPane.showInformationMessage(this, "Debe seleccionar el cliente y un artículo.", "Información");
			return;
		}
		CuentaArticulo cuentaArticulo = getCuentaFacade().getCuentaArticulo(clienteElegido, articuloElegido, EUnidad.METROS);
		java.sql.Date fechaDesde = getChkFiltrarPorFecha().isSelected()?new java.sql.Date(getDPFechaDesde().getDate().getTime()):null;
		java.sql.Date fechaHasta = getChkFiltrarPorFecha().isSelected()?DateUtil.getManiana(new java.sql.Date(getDPFechaHasta().getDate().getTime() + DateUtil.ONE_DAY)):null;
		List<MovimientoCuentaArticulo> movs = getCuentaFacade().getMovimientosCuentaArticulo(cuentaArticulo.getId(), fechaDesde, fechaHasta);
		if (movs != null) {
			filaMovimientoVisitor = getPanelTablaMovimientos().createVisitorFilaMovimientos();
			boolean habilitarBotonesImpresion = movs.size() > 0;
			getBtnExportarAExcel().setEnabled(habilitarBotonesImpresion);
			getBtnImprimirListado().setEnabled(habilitarBotonesImpresion);
			getBtnListadoPDF().setEnabled(habilitarBotonesImpresion);
			getBtnAgregarRemitoEntrada01().setEnabled(true);
			getBtnAgregarRemitoSalida01().setEnabled(true);
			getBtnAgregarRemitoVentaTela().setEnabled(true);
			llenarTablaMovimientos(movs);
		} else {
			getBtnAgregarRemitoEntrada01().setEnabled(false);
			getBtnAgregarRemitoSalida01().setEnabled(false);
			getBtnAgregarRemitoVentaTela().setEnabled(false);
			FWJOptionPane.showWarningMessage(JFrameVerMovimientosCuentaArticulo.this, "No se han encontrado resultados", "Error");
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
							JDialogSeleccionarCliente dialogSeleccionarCliente = new JDialogSeleccionarCliente(null);
							GuiUtil.centrar(dialogSeleccionarCliente);
							dialogSeleccionarCliente.setVisible(true);
							clienteElegido = dialogSeleccionarCliente.getCliente();
							if (clienteElegido != null) {
								getTxtBusquedaCliente().setText(String.valueOf(clienteElegido.getNroCliente()));
								getLblNombre().setText(clienteElegido.getRazonSocial());
								getLblDireccion().setText(clienteElegido.getDireccionFiscal().getDireccion() + " " + clienteElegido.getDireccionFiscal().getLocalidad().getNombreLocalidad());
								getLblCuit().setText(clienteElegido.getCuit());
								getLblTelefono().setText(clienteElegido.getTelefono());
								if(articuloElegido != null) {
									getLblArticulo().setText(articuloElegido.getNombre());
								}
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
					String nroClienteStr = getTxtBusquedaCliente().getText();
					if(StringUtil.isNullOrEmpty(nroClienteStr)) {
						clienteElegido = null;
						FWJOptionPane.showErrorMessage(JFrameVerMovimientosCuentaArticulo.this, "Debe ingresar un número de cliente.", "Error");
						llenarTablaMovimientos(new ArrayList<MovimientoCuentaArticulo>());
					} else {
						clienteElegido = getClienteFacade().getClienteByNumero(Integer.valueOf(nroClienteStr));
					}
					if(clienteElegido == null) {
						FWJOptionPane.showErrorMessage(JFrameVerMovimientosCuentaArticulo.this, "No existe ningún cliente con el número ingresado.", "Error");
						llenarTablaMovimientos(new ArrayList<MovimientoCuentaArticulo>());
					} else {
						getBtnBuscar().doClick();
					}
					
				}
			});
		}
		return txtBusquedaCliente;
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
			panelAcciones.add(getBtnAgregarRemitoEntrada());
			panelAcciones.add(getBtnAgregarRemitoEntrada01());
			panelAcciones.add(getBtnAgregarRemitoSalida01());
			panelAcciones.add(getBtnAgregarRemitoVentaTela());
			panelAcciones.add(getBtnAgregarRemitoCompraTela());
			panelAcciones.add(getBtnVerificar());
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

	private CuentaArticuloFacadeRemote getCuentaFacade() {
		if (cuentaFacade == null) {
			cuentaFacade = GTLBeanFactory.getInstance().getBean2(CuentaArticuloFacadeRemote.class);
		}
		return cuentaFacade;
	}

	private void setSaldoCuenta() {
		if(clienteElegido == null || articuloElegido == null) {
			return;
		}
		CuentaArticulo cuenta = getCuentaFacade().getCuentaArticulo(clienteElegido, articuloElegido, EUnidad.METROS);
		if (cuenta != null) {
			Cliente cliente = cuenta.getCliente();
			clienteElegido = cliente;
			getLblNombre().setText(cliente.getRazonSocial());
			if(cliente.getDireccionFiscal() != null && cliente.getDireccionFiscal().getLocalidad() != null) {
				getLblDireccion().setText(cliente.getDireccionFiscal().getDireccion() + " " + cliente.getDireccionFiscal().getLocalidad().getNombreLocalidad());
			}
			getLblCuit().setText(cliente.getCuit());
			getLblTelefono().setText(cliente.getTelefono());
			BigDecimal saldo = new BigDecimal(filaMovimientoVisitor.getSaldo());
			int saldoDouble = Math.round(Double.valueOf(saldo.doubleValue()).floatValue());
			if ( saldoDouble == 0) {
				getTxtTotalCuenta().setText(String.valueOf( saldoDouble)+".00");
			} else {
				getTxtTotalCuenta().setText(getDecimalFormat().format(saldo));
			}
			if (saldoDouble > 0) {
				getTxtTotalCuenta().setForeground(Color.GREEN.darker());
			} else if (saldoDouble < 0) {
				getTxtTotalCuenta().setForeground(Color.RED);
			} else {
				getTxtTotalCuenta().setForeground(Color.BLACK);
			}
		}
	}

	public JButton getBtnAgregarRemitoEntrada() {
		if (btnAgregarRemitoEntrada == null) {
			btnAgregarRemitoEntrada = BossEstilos.createButton("ar/com/textillevel/imagenes/b_anular_recibo.png", "ar/com/textillevel/imagenes/b_anular_recibo_des.png"); 
			btnAgregarRemitoEntrada.setToolTipText("Agregar Remito de Entrada");
			btnAgregarRemitoEntrada.setEnabled(false);
			btnAgregarRemitoEntrada.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if (getPanelTablaMovimientos().getTabla().getSelectedRow() > -1) {
						realizarAccionAgregarRemitoEntrada();
					}
				}
			});
		}
		return btnAgregarRemitoEntrada;
	}


	private void realizarAccionAgregarRemitoEntrada() {
		//TODO:
	}

	private ClienteFacadeRemote getClienteFacade() {
		if(clienteFacade == null) {
			clienteFacade = GTLBeanFactory.getInstance().getBean2(ClienteFacadeRemote.class);
		}
		return clienteFacade;
	}

	private RemitoSalidaFacadeRemote getRemitoSalidaFacade() {
		if (remitoSalidaFacade == null) {
			remitoSalidaFacade = GTLBeanFactory.getInstance().getBean2(RemitoSalidaFacadeRemote.class);
		}
		return remitoSalidaFacade;
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
					// CLJOptionPane.showQuestionMessage(JFrameVerMovimientos.this,
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
						mostrarFileChooser("Listado de Movimientos - Cliente Nro - " + getTxtBusquedaCliente().getText(), EXTENSION_PDF);
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
				FWJOptionPane.showErrorMessage(JFrameVerMovimientosCuentaArticulo.this, "Se ha producido un error al guardar el archivo.\n" + e1.getMessage(), "Error");
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
			panelDatosUsuario.add(getLblArticulo());
		}
		return panelDatosUsuario;
	}

	private JLabel getLblArticulo() {
		if (lblArticulo == null) {
			lblArticulo = new JLabel();
			Font fuente = lblArticulo.getFont();
			lblArticulo.setFont(new Font(fuente.getName(), Font.BOLD, fuente.getSize() + 3));
		}
		return lblArticulo;
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
	
	public CorreccionFacadeRemote getCorreccionFacade(){
		if(correccionFacade==null){
			correccionFacade = GTLBeanFactory.getInstance().getBean2(CorreccionFacadeRemote.class);
		}
		return correccionFacade;
	}

	public UsuarioSistema getUsuarioAdministrador() {
		return usuarioAdministrador;
	}
	
	public void setUsuarioAdministrador(UsuarioSistema usuarioAdministrador) {
		this.usuarioAdministrador = usuarioAdministrador;
	}

	public JCheckBox getChkFiltrarPorFecha() {
		if(chkFiltrarPorFecha == null){
			chkFiltrarPorFecha = new JCheckBox("Filtrar por fecha");
			chkFiltrarPorFecha.setSelected(true);
			chkFiltrarPorFecha.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					getDPFechaDesde().setEnabled(chkFiltrarPorFecha.isSelected());
					getDPFechaHasta().setEnabled(chkFiltrarPorFecha.isSelected());
				}
			});
		}
		return chkFiltrarPorFecha;
	}

	public JButton getBtnVerificar() {
		if(btnVerificar == null){
			btnVerificar = BossEstilos.createButton("ar/com/textillevel/imagenes/b_cancelar_anulacion_recibo.png", "ar/com/textillevel/imagenes/b_cancelar_anulacion_recibo_des.png");
			btnVerificar.setToolTipText("Confirmar movimiento");
			btnVerificar.setEnabled(false);
			btnVerificar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(!GTLGlobalCache.getInstance().getUsuarioSistema().getPerfil().getIsAdmin()){
						JDialogPasswordInput jDialogPasswordInput = new JDialogPasswordInput(JFrameVerMovimientosCuentaArticulo.this,"Confirmar movimiento");
						if (jDialogPasswordInput.isAcepto()) {
							String pass = new String(jDialogPasswordInput.getPassword());
							UsuarioSistema usrAdmin = GTLBeanFactory.getInstance().getBean2(UsuarioSistemaFacadeRemote.class).esPasswordDeAdministrador(pass);
							if (usrAdmin != null) {
								MovimientoCuentaArticulo mov = getPanelTablaMovimientos().getElemento(getPanelTablaMovimientos().getTabla().getSelectedRow());
								mov.setUsuarioConfirmacion(usrAdmin);
								getCuentaFacade().actualizarMovimiento(mov);

							} else {
								FWJOptionPane.showErrorMessage(JFrameVerMovimientosCuentaArticulo.this, "La clave ingresada no peternece a un usuario administrador", "Error");
							}
						}
					}else{
						MovimientoCuentaArticulo mov = getPanelTablaMovimientos().getElemento(getPanelTablaMovimientos().getTabla().getSelectedRow());
						mov.setUsuarioConfirmacion(GTLGlobalCache.getInstance().getUsuarioSistema());
						getCuentaFacade().actualizarMovimiento(mov);
					}
					buscarMovimientos();
				}
			});	
		}
		return btnVerificar;
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
						MovimientoCuentaArticulo mov = getPanelTablaMovimientos().getElemento(fila);
						String observaciones = JOptionPane.showInputDialog(JFrameVerMovimientosCuentaArticulo.this, "Observaciones", mov.getObservaciones());
						if(observaciones!=null){
							mov.setObservaciones(observaciones);
							getCuentaFacade().actualizarMovimiento(mov);
							btnAgregarObservaciones.setEnabled(false);
							buscarMovimientos();
						}
					}
				}
			});
		}
		return btnAgregarObservaciones;
	}

	private JButton getBtnAgregarRemitoEntrada01() {
		if(btnAgregarRemitoEntrada01 == null){
			btnAgregarRemitoEntrada01 = BossEstilos.createButton("ar/com/textillevel/imagenes/b_entrada.png", "ar/com/textillevel/imagenes/b_entrada_des.png");
			btnAgregarRemitoEntrada01.setToolTipText("Agregar remito de entrda 01");
			btnAgregarRemitoEntrada01.setEnabled(false);
			btnAgregarRemitoEntrada01.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					RemitoEntrada remitoEntrada = new RemitoEntrada();
					remitoEntrada.setCliente(clienteElegido);
					JDialogAgregarRemitoEntradaStock dialogAgregarRemitoEntrada = new JDialogAgregarRemitoEntradaStock(JFrameVerMovimientosCuentaArticulo.this, remitoEntrada, new ArrayList<OrdenDeTrabajo>(), false);
					GuiUtil.centrar(dialogAgregarRemitoEntrada);		
					dialogAgregarRemitoEntrada.setVisible(true);
					buscarMovimientos();
				}
			});
		}
		return btnAgregarRemitoEntrada01;
	}

	private JButton getBtnAgregarRemitoSalida01() {
		if(btnAgregarRemitoSalida01 == null){
			btnAgregarRemitoSalida01 =BossEstilos.createButton("ar/com/textillevel/imagenes/b_salida.png", "ar/com/textillevel/imagenes/b_salida_des.png");
			btnAgregarRemitoSalida01.setToolTipText("Agregar remito de salida 01");
			btnAgregarRemitoSalida01.setEnabled(false);
			btnAgregarRemitoSalida01.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					IngresoRemitoSalidaHandler rsHandler = new IngresoRemitoSalidaHandler(JFrameVerMovimientosCuentaArticulo.this, ETipoRemitoSalida.CLIENTE_SALIDA_01, false, null);
					rsHandler.agregarRemitoCliente(GTLBeanFactory.getInstance().getBean2(ParametrosGeneralesFacadeRemote.class).getParametrosGenerales(),clienteElegido);
					buscarMovimientos();
				}
			});
		}
		return btnAgregarRemitoSalida01;
	}

	private JButton getBtnAgregarRemitoCompraTela() {
		if(btnAgregarRemitoCompraTela == null){
			btnAgregarRemitoCompraTela = BossEstilos.createButton("ar/com/textillevel/imagenes/b_cheque_cartera.png", "ar/com/textillevel/imagenes/b_cheque_cartera_des.png");
			btnAgregarRemitoCompraTela.setToolTipText("Agregar remito de compra de tela");
			btnAgregarRemitoCompraTela.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JDialogSeleccionarProveedor dialogSeleccionarProveedor = new JDialogSeleccionarProveedor(JFrameVerMovimientosCuentaArticulo.this);
					GuiUtil.centrar(dialogSeleccionarProveedor);
					dialogSeleccionarProveedor.setVisible(true);
					Proveedor proveedorElegido = dialogSeleccionarProveedor.getProveedor();
					if(proveedorElegido != null) {
						RemitoEntrada remitoEntrada = new RemitoEntrada();
						remitoEntrada.setProveedor(proveedorElegido);
						JDialogAgregarRemitoEntradaCompraTela dialogAgregarRemitoEntrada = new JDialogAgregarRemitoEntradaCompraTela(JFrameVerMovimientosCuentaArticulo.this, remitoEntrada, new ArrayList<OrdenDeTrabajo>(), false);
						GuiUtil.centrar(dialogAgregarRemitoEntrada);		
						dialogAgregarRemitoEntrada.setVisible(true);
						RemitoEntradaProveedor remitoEntradaProveedor = dialogAgregarRemitoEntrada.getRemitoEntradaProveedor();
						if(remitoEntradaProveedor != null) {
							if(FWJOptionPane.showQuestionMessage(JFrameVerMovimientosCuentaArticulo.this, "¿Desea Cargar la Factura del Proveedor?", "Confirmación") == FWJOptionPane.YES_OPTION) {
								RemitoEntradaProveedorFacadeRemote repfr = GTLBeanFactory.getInstance().getBean2(RemitoEntradaProveedorFacadeRemote.class);
								remitoEntradaProveedor = repfr.getByIdEager(remitoEntradaProveedor.getId());
								FacturaProveedor fp = new FacturaProveedor();
								fp.setTipoFacturaProveedor(ETipoFacturaProveedor.NORMAL);					
								fp.setUsuarioCreador(GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName());
								fp.setProveedor(proveedorElegido);
								fp.getRemitoList().add(remitoEntradaProveedor);
								JDialogCargarFacturaProveedor jdcfp = new JDialogCargarFacturaProveedor(JFrameVerMovimientosCuentaArticulo.this, fp, false, new ArrayList<RemitoEntradaProveedor>());
								GuiUtil.centrar(jdcfp);
								jdcfp.setVisible(true);
							}
						}
						buscarMovimientos();
					}
				}
			});
		}
		return btnAgregarRemitoCompraTela;
	}

	private JButton getBtnAgregarRemitoVentaTela() {
		if(btnAgregarRemitoVentaTela == null){
			btnAgregarRemitoVentaTela = BossEstilos.createButton("ar/com/textillevel/imagenes/b_venta.png", "ar/com/textillevel/imagenes/b_venta_des.png");
			btnAgregarRemitoVentaTela.setToolTipText("Agregar remito venta de tela");
			btnAgregarRemitoVentaTela.setEnabled(false);
			btnAgregarRemitoVentaTela.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					IngresoRemitoSalidaHandler rsHandler = new IngresoRemitoSalidaHandler(JFrameVerMovimientosCuentaArticulo.this, ETipoRemitoSalida.CLIENTE_VENTA_DE_TELA, false, null);
					rsHandler.agregarRemitoCliente(GTLBeanFactory.getInstance().getBean2(ParametrosGeneralesFacadeRemote.class).getParametrosGenerales(), clienteElegido);
					buscarMovimientos();
				}
			});
		}
		return btnAgregarRemitoVentaTela;
	}
}