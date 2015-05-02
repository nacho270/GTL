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
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.TableCellRenderer;

import ar.clarin.fwjava.boss.BossEstilos;
import ar.clarin.fwjava.componentes.CLFileSelector;
import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.CLJTable;
import ar.clarin.fwjava.componentes.CLJTextField;
import ar.clarin.fwjava.componentes.PanelTablaSinBotones;
import ar.clarin.fwjava.componentes.VerticalFlowLayout;
import ar.clarin.fwjava.componentes.error.validaciones.ValidacionException;
import ar.clarin.fwjava.util.DateUtil;
import ar.clarin.fwjava.util.GuiUtil;
import ar.com.textillevel.entidades.cheque.Banco;
import ar.com.textillevel.entidades.cuenta.CuentaBanco;
import ar.com.textillevel.entidades.cuenta.movimientos.MovimientoCuenta;
import ar.com.textillevel.entidades.cuenta.movimientos.visitor.IFilaMovimientoVisitor;
import ar.com.textillevel.facade.api.remote.BancoFacadeRemote;
import ar.com.textillevel.facade.api.remote.CuentaFacadeRemote;
import ar.com.textillevel.facade.api.remote.ParametrosGeneralesFacadeRemote;
import ar.com.textillevel.gui.acciones.visitor.banco.ConsultaDocumentoVisitor;
import ar.com.textillevel.gui.acciones.visitor.banco.GenerarFilaMovimientoVisitor;
import ar.com.textillevel.gui.acciones.visitor.banco.HabilitarBotonesCuentaVisitor;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.JasperHelper;
import ar.com.textillevel.gui.util.controles.PanelDatePicker;
import ar.com.textillevel.util.GTLBeanFactory;

public class JFrameVerMovimientosBanco extends JFrame {

	private static final long serialVersionUID = 2857257268987818318L;

	private static final String EXTENSION_EXCEL = ".xls";
	private static final String EXTENSION_PDF = ".pdf";

	private PanelTablaMovimientos tablaMovimientos;
	private JPanel panelCabecera;
	private PanelDatePicker txtFechaDesde;
	private PanelDatePicker txtFechaHasta;
	private JComboBox cmbBanco;
	private JButton btnBuscar;
	private JPanel panelSur;
	private JButton btnSalir;
	private CLJTextField txtTotalCuenta;
	private JPanel panelAcciones;
	private JPanel panelDatosUsuario;
	private JLabel lblNombre;
	private JLabel lblDireccion;
	private JLabel lblCuit;
	private JLabel lblTelefono;

	private GenerarFilaMovimientoVisitor filaMovimientoVisitor;
	private IFilaMovimientoVisitor consultaDocumentoMovimientoVisitor;
	
	private Banco bancoElegido;

	private CuentaFacadeRemote cuentaFacade;
	private BancoFacadeRemote bancoFacade;
	
	/** ACCIONES **/

//	private JButton btnAnular;
//	private JButton btnConfirmar;
	private JButton btnExportarAExcel;
	private JButton btnImprimirListado;
	private JButton btnListadoPDF;
	private JButton btnAgregarObservaciones;
	
	public JFrameVerMovimientosBanco(Frame padre) {
		setUpComponentes();
		setUpScreen();
		getCmbBanco().requestFocus();
//		getBtnAnular().setEnabled(false);
//		getBtnConfirmar().setEnabled(false);
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
		int ret = CLJOptionPane.showQuestionMessage(this, "Va a cerrar el m�dulo, esta seguro?", "Factura");
		if (ret == CLJOptionPane.YES_OPTION) {
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
		protected CLJTable construirTabla() {
			CLJTable tabla = new CLJTable(0, CANT_COLS_TBL_MOVS);
			tabla.setStringColumn(COL_PAGOS, "Pagos", 100);
			tabla.setStringColumn(COL_DESCR, "Descripci�n", 220, 320, true);
			tabla.setFloatColumn(COL_DEBE, "Debe", 80, true);
			tabla.setFloatColumn(COL_HABER, "Haber", 80, true);
			tabla.setFloatColumn(COL_SALDO, "Saldo", 80, true);
			tabla.setStringColumn(COL_OBJ, "", 0, 0, true);
			tabla.setCheckColumn(COL_VERIFICADO, "Verificado", 60, true);
			tabla.setStringColumn(COL_USUARIO_VERIFICADOR, "Usuario verficador", 100, 100, true);
			tabla.setStringColumn(COL_USUARIO_CREADOR, "Usuario creador", 100, 100, true);
			tabla.setStringColumn(COL_OBSERVACIONES, "Observaciones", 220, 320, true);
			tabla.setReorderingAllowed(false);
			tabla.setHeaderAlignment(COL_PAGOS, CLJTable.CENTER_ALIGN);
			tabla.setHeaderAlignment(COL_DESCR, CLJTable.CENTER_ALIGN);
			tabla.setHeaderAlignment(COL_DEBE, CLJTable.CENTER_ALIGN);
			tabla.setHeaderAlignment(COL_HABER, CLJTable.CENTER_ALIGN);
			tabla.setHeaderAlignment(COL_SALDO, CLJTable.CENTER_ALIGN);
			tabla.setHeaderAlignment(COL_VERIFICADO, CLJTable.CENTER_ALIGN);
			tabla.setHeaderAlignment(COL_USUARIO_VERIFICADOR, CLJTable.CENTER_ALIGN);
			tabla.setHeaderAlignment(COL_USUARIO_CREADOR, CLJTable.CENTER_ALIGN);
			tabla.setHeaderAlignment(COL_OBSERVACIONES, CLJTable.CENTER_ALIGN);
			tabla.setAlignment(COL_USUARIO_VERIFICADOR, CLJTable.CENTER_ALIGN);
			tabla.setAlignment(COL_USUARIO_CREADOR, CLJTable.CENTER_ALIGN);
			tabla.getColumnModel().getColumn(COL_PAGOS).setCellRenderer(getCellRenderer());
			consultaDocumentoMovimientoVisitor = new ConsultaDocumentoVisitor(JFrameVerMovimientosBanco.this);
			tabla.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseReleased(MouseEvent e) {
					if(getTabla().getSelectedRow()>-1){
						if(getTabla().getValueAt(getTabla().getSelectedRow(), COL_OBJ) instanceof MovimientoCuenta){
							if (e.getClickCount() == 2 && getTabla().getSelectedRow() > -1) {
								((MovimientoCuenta) getTabla().getValueAt(getTabla().getSelectedRow(), COL_OBJ)).aceptarVisitor(consultaDocumentoMovimientoVisitor);
							} else if (e.getClickCount() == 1 && getTabla().getSelectedRow() > -1) {
								HabilitarBotonesCuentaVisitor hbcv = new HabilitarBotonesCuentaVisitor(JFrameVerMovimientosBanco.this);
								((MovimientoCuenta) getTabla().getValueAt(getTabla().getSelectedRow(), COL_OBJ)).aceptarVisitor(hbcv);
							} else if (getTabla().getSelectedRow() < 0) {
//								getBtnAnular().setEnabled(false);
//								getBtnConfirmar().setEnabled(false);
							}
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
								HabilitarBotonesCuentaVisitor hbcv = new HabilitarBotonesCuentaVisitor(JFrameVerMovimientosBanco.this);
								((MovimientoCuenta) getTabla().getValueAt(getTabla().getSelectedRow(), COL_OBJ)).aceptarVisitor(hbcv);
							} else if (getTabla().getSelectedRow() < 0) {
//								getBtnAnular().setEnabled(false);
//								getBtnConfirmar().setEnabled(false);
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

			final Component[] comps = new Component[] { getCmbBanco(), getDPFechaDesde(), getDPFechaHasta(), getBtnBuscar() };
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
			panelProveedor.add(new JLabel("Banco: "));
			panelProveedor.add(getCmbBanco());
			panelProveedor.add(getBtnBuscar());

			JPanel panelFechas = new JPanel();
			panelFechas.setLayout(new FlowLayout());
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
						CLJOptionPane.showErrorMessage(JFrameVerMovimientosBanco.this, "La 'fecha desde' ingresada no es v�lida", "Error");
						return;
					}
					if(getDPFechaHasta().getDate()==null){
						CLJOptionPane.showErrorMessage(JFrameVerMovimientosBanco.this, "La 'fecha hasta' ingresada no es v�lida", "Error");
						return;
					}
					if (!getDPFechaDesde().getDate().after(getDPFechaHasta().getDate())) {
						if (getCmbBanco().getSelectedItem()!=null && getBancoElegido()!=null) {
							buscarMovimientos();
						} else {
							CLJOptionPane.showErrorMessage(JFrameVerMovimientosBanco.this, "Debe elegir un banco", "Error");
						}
					} else {
						CLJOptionPane.showErrorMessage(JFrameVerMovimientosBanco.this, "La 'fecha desde' no debe ser posterior a la 'fecha hasta'", "Error");
					}
				}
			});
		}
		return btnBuscar;
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
						String observaciones = JOptionPane.showInputDialog(JFrameVerMovimientosBanco.this, "Observaciones", mov.getObservaciones());
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


	private void buscarMovimientos() {
		try{
			Banco banco = getBancoElegido();
			java.sql.Date fechaDesde = new java.sql.Date(getDPFechaDesde().getDate().getTime());
			java.sql.Date fechaHasta = DateUtil.getManiana(new java.sql.Date(getDPFechaHasta().getDate().getTime() + DateUtil.ONE_DAY));
			BigDecimal transporteCuenta = new BigDecimal(0d);
			filaMovimientoVisitor = getPanelTablaMovimientos().createVisitorFilaMovimientos(transporteCuenta);
			List<MovimientoCuenta> movs = getCuentaFacade().getMovimientosByIdBancoYFecha(banco.getId(), 
										fechaDesde,	fechaHasta
										/*,getCmbOrdenMovimientos().getSelectedItem().equals("MAS ANTIGUO PRIMERO")*/);
			if (movs != null && !movs.isEmpty()) {
				List<MovimientoCuenta> movimientosTransporteCuenta = getCuentaFacade().getMovimientosTransporteCuentaBanco(banco.getId(), new java.sql.Date(movs.get(0).getFechaHora().getTime()));
				transporteCuenta = calcularTransporte(movimientosTransporteCuenta, movs);
				boolean habilitarBotonesImpresion = movs.size() > 0;
				getBtnExportarAExcel().setEnabled(habilitarBotonesImpresion);
				getBtnImprimirListado().setEnabled(habilitarBotonesImpresion);
				getBtnListadoPDF().setEnabled(habilitarBotonesImpresion);
				llenarTablaMovimientos(movs,transporteCuenta);
				setSaldoCuenta();
	//			Map<Integer, Color> mapaColores = filaMovimientoVisitor.getMapaColores();
	//			InfoCuentaTO infoCuentaTO = getCuentaFacade().getInfoReciboYPagosRecibidos(idCliente);
	//			pintarRecibos(mapaColores);
	//			pintarFacturasPagadas(mapaColores, infoCuentaTO);
	//			pintarRecibosSecondPass(filaMovimientoVisitor.getRowsPagosSaldoAFavor());
			} else {
				CLJOptionPane.showWarningMessage(JFrameVerMovimientosBanco.this, "No se han encontrado resultados", "Error");
			}
		}catch(ValidacionException vle){
			CLJOptionPane.showErrorMessage(this, vle.getMensajeError(), "Error");
		}
	}
	
	private BigDecimal calcularTransporte(List<MovimientoCuenta> movsTransporte, List<MovimientoCuenta> allMovs){
		BigDecimal transporteCuenta = new BigDecimal(0d);
		for(MovimientoCuenta movT : movsTransporte){
			if(!allMovs.contains(movT)){
				transporteCuenta = transporteCuenta.add(movT.getMonto());
			}
		}
		return transporteCuenta.negate();
	}
	
//	private void pintarRecibosSecondPass(List<InfoSecondPass> rowsPagosSaldoAFavor) {
//		CLJTable tablaMov = getPanelTablaMovimientos().getTabla();
//		CellRenderer cellRenderer = (CellRenderer) tablaMov.getColumnModel().getColumn(0).getCellRenderer();
//		for (InfoSecondPass isp : rowsPagosSaldoAFavor) {
//			for (int i = isp.getFila() + 1; i < tablaMov.getRowCount(); i++) {
//				if( tablaMov.getValueAt(i, 5) instanceof MovimientoCuenta){
//					PintarRecibosSecondPassVisitor prspv = new PintarRecibosSecondPassVisitor(isp, i, cellRenderer);
//					((MovimientoCuenta) tablaMov.getValueAt(i, 5)).aceptarVisitor(prspv);
//				}
//			}
//		}
//	}
//
//	private void pintarFacturasPagadas(Map<Integer, Color> mapaColores, InfoCuentaTO infoCuentaTO) {
//		CLJTable tabla = getPanelTablaMovimientos().getTabla();
//		CellRenderer cellRenderer = (CellRenderer) getPanelTablaMovimientos().getTabla().getColumnModel().getColumn(0).getCellRenderer();
//		PintarFilaReciboVisitor pfrv = new PintarFilaReciboVisitor(tabla, mapaColores, cellRenderer, infoCuentaTO);
//		for (int i = 0; i < tabla.getRowCount(); i++) {
//			if( tabla.getValueAt(i, 5) instanceof MovimientoCuenta){
//				pfrv.setFilaActual(i);
//				((MovimientoCuenta) tabla.getValueAt(i, 5)).aceptarVisitor(pfrv);
//			}
//		}
//	}
//
//	private void pintarRecibos(Map<Integer, Color> mapaColores) {
//		CLJTable tabla = getPanelTablaMovimientos().getTabla();
//		CellRenderer cellRenderer = (CellRenderer) getPanelTablaMovimientos().getTabla().getColumnModel().getColumn(0).getCellRenderer();
//		cellRenderer.clear();
//		PintarFilaReciboVisitor pfrv = new PintarFilaReciboVisitor(tabla, mapaColores, cellRenderer);
//		for (int i = 0; i < tabla.getRowCount(); i++) {
//			if( tabla.getValueAt(i, 5) instanceof MovimientoCuenta){
//				pfrv.setFilaActual(i);
//				((MovimientoCuenta) tabla.getValueAt(i, 5)).aceptarVisitor(pfrv);
//			}
//		}
//	}

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
//			panelAcciones.add(getBtnEliminarFactura());
//			panelAcciones.add(getBtnConfirmar());
//			panelAcciones.add(getBtnAgregarNroReciboOrdenDePago());
//			panelAcciones.add(getBtnCompletarDatosNotaDebitoCredito());
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

	private CLJTextField getTxtTotalCuenta() {
		if (txtTotalCuenta == null) {
			txtTotalCuenta = new CLJTextField();
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
		CuentaBanco cuenta = getCuentaFacade().getCuentaBancoByIdBanco(getBancoElegido().getId());
		if (cuenta != null) {
			Banco b = cuenta.getBanco();
			getLblNombre().setText(b.getNombre());
			if(b.getDireccion().getDireccion()!=null){
				getLblDireccion().setText(b.getDireccion().getDireccion() + " - " + b.getDireccion().getLocalidad());
			}else{
				getLblDireccion().setText("");
			}
			getLblCuit().setText(String.valueOf(b.getCodigoBanco()));
			getLblTelefono().setText(b.getTelefono());
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
	
//	public JButton getBtnEliminarFactura() {
//		if (btnEliminarFactura == null) {
//			btnEliminarFactura = BossEstilos.createButton("ar/com/textillevel/imagenes/b_eliminar.png", "ar/com/textillevel/imagenes/b_eliminar_des.png");
//			btnEliminarFactura.setEnabled(false);
//			btnEliminarFactura.addActionListener(new ActionListener() {
//
//				public void actionPerformed(ActionEvent e) {
//					if (getPanelTablaMovimientos().getTabla().getSelectedRow() > -1) {
//						realizarAccionEliminarFactura();
//					}
//				}
//			});
//		}
//		return btnEliminarFactura;
//	}
//
//	public JButton getBtnAnular() {
//		if (btnAnular == null) {
//			btnAnular = BossEstilos.createButton("ar/com/textillevel/imagenes/b_anular_recibo.png", "ar/com/textillevel/imagenes/b_anular_recibo_des.png");
//			btnAnular.setEnabled(false);
//			btnAnular.addActionListener(new ActionListener() {
//
//				public void actionPerformed(ActionEvent e) {
//					if (getPanelTablaMovimientos().getTabla().getSelectedRow() > -1) {
//						realizarAccionAnular();
//					}
//				}
//			});
//		}
//		return btnAnular;
//	}

//	public JButton getBtnConfirmar() {
//		if (btnConfirmar == null) {
//			btnConfirmar = new JButton("   Confirmar", ImageUtil.loadIcon("ar/com/textillevel/imagenes/b_cancelar_anulacion_recibo.png")) ;
//			btnConfirmar.setDisabledIcon(ImageUtil.loadIcon( "ar/com/textillevel/imagenes/b_cancelar_anulacion_recibo_des.png"));
//			btnConfirmar.setEnabled(false);
//			btnConfirmar.addActionListener(new ActionListener() {
//
//				public void actionPerformed(ActionEvent e) {
//					if (getPanelTablaMovimientos().getTabla().getSelectedRow() > -1) {
//						realizarAccionConfirmar();
//					}
//				}
//			});
//		}
//		return btnConfirmar;
//	}

//	private void realizarAccionConfirmar() {
//		JDialogPasswordInput jDialogPasswordInput = new JDialogPasswordInput(JFrameVerMovimientosBanco.this);
//		if (jDialogPasswordInput.isAcepto()) {
//			String pass = new String(jDialogPasswordInput.getPassword());
//			if (PortalUtils.getHash(pass, "MD5").equals(GTLGlobalCache.getInstance().getUsuarioSistema().getPassword())) {
//				AccionConfirmarCuentaVisitor racv = new AccionConfirmarCuentaVisitor(JFrameVerMovimientosBanco.this);
//				((MovimientoCuenta) getPanelTablaMovimientos().getTabla().getValueAt(getPanelTablaMovimientos().getTabla().getSelectedRow(), PanelTablaMovimientos.COL_OBJ)).aceptarVisitor(racv);
//			} else {
//				CLJOptionPane.showErrorMessage(JFrameVerMovimientosBanco.this, "La clave ingresada no peternece a la del usuario en sesi�n", "Error");
//			}
//		}
//	}

//	private void realizarAccionAnular() {
//		JDialogPasswordInput jDialogPasswordInput = new JDialogPasswordInput(JFrameVerMovimientosBanco.this);
//		if (jDialogPasswordInput.isAcepto()) {
//			String pass = new String(jDialogPasswordInput.getPassword());
//			if (PortalUtils.getHash(pass, "MD5").equals(GTLGlobalCache.getInstance().getUsuarioSistema().getPassword())) {
//				AccionAnularCuentaVisitor racv = new AccionAnularCuentaVisitor(JFrameVerMovimientosBanco.this);
//				((MovimientoCuenta) getPanelTablaMovimientos().getTabla().getValueAt(getPanelTablaMovimientos().getTabla().getSelectedRow(), PanelTablaMovimientos.COL_OBJ)).aceptarVisitor(racv);
//			} else {
//				CLJOptionPane.showErrorMessage(JFrameVerMovimientosBanco.this, "La clave ingresada no peternece a la del usuario en sesi�n", "Error");
//			}
//		}
//	}
	
//	private void realizarAccionEliminarFactura() {
//		JDialogPasswordInput jDialogPasswordInput = new JDialogPasswordInput(JFrameVerMovimientosBanco.this);
//		if (jDialogPasswordInput.isAcepto()) {
//			String pass = new String(jDialogPasswordInput.getPassword());
//			if (PortalUtils.getHash(pass, "MD5").equals(GTLGlobalCache.getInstance().getUsuarioSistema().getPassword())) {
//				AccionEliminarFacturaCuentaVisitor racv = new AccionEliminarFacturaCuentaVisitor(JFrameVerMovimientosBanco.this);
//				((MovimientoCuenta) getPanelTablaMovimientos().getTabla().getValueAt(getPanelTablaMovimientos().getTabla().getSelectedRow(), PanelTablaMovimientos.COL_OBJ)).aceptarVisitor(racv);
//			} else {
//				CLJOptionPane.showErrorMessage(JFrameVerMovimientosBanco.this, "La clave ingresada no peternece a la del usuario en sesi�n", "Error");
//			}
//		}
//	}

	private DecimalFormat getDecimalFormat() {
		DecimalFormat df = new DecimalFormat("#,###.00");
		df.setMaximumFractionDigits(2);
		df.setGroupingUsed(true);
		return df;
	}

	private JButton getBtnExportarAExcel() {
		if (btnExportarAExcel == null) {
			btnExportarAExcel = BossEstilos.createButton("ar/clarin/fwjava/imagenes/b_exportar_excel.png", "ar/clarin/fwjava/imagenes/b_exportar_excel_des.png"); 
			btnExportarAExcel.setToolTipText("Exportar a Excel");
			btnExportarAExcel.setEnabled(false);
			btnExportarAExcel.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if (getPanelTablaMovimientos().getTabla().getRowCount() > 0) {
						CLJTable tabla = getPanelTablaMovimientos().getTabla();
						mostrarFileChooser("Listado de Movimientos - " + ((Banco)getCmbBanco().getSelectedItem()).getNombre(), EXTENSION_PDF);
						File archivoIngresado = CLFileSelector.obtenerArchivo(CLFileSelector.SAVE, CLFileSelector.FILES_ONLY, new FiltroArchivosExcel(), null);
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
					// CLJOptionPane.showQuestionMessage(JFrameVerMovimientosBanco.this,
					// "Desea previsualizar la impresi�n?",
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
						CLJTable tabla = getPanelTablaMovimientos().getTabla();
						mostrarFileChooser("Listado de Movimientos - " + ((Banco)getCmbBanco().getSelectedItem()).getNombre(), EXTENSION_PDF);
						File archivoIngresado = CLFileSelector.obtenerArchivo(CLFileSelector.SAVE, CLFileSelector.FILES_ONLY, new FiltroArchivosPDF(), null);
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
		File directorioCorriente = CLFileSelector.getLastSelectedFile();
		if (directorioCorriente != null) {
			String nombreSugerido = null;
			try {
				if (directorioCorriente.isFile()) {
					nombreSugerido = directorioCorriente.getCanonicalPath();
				} else {
					nombreSugerido = directorioCorriente.getCanonicalPath() + File.separator + nombreArchivo;
				}
			} catch (IOException e1) {
				CLJOptionPane.showErrorMessage(JFrameVerMovimientosBanco.this, "Se ha producido un error al guardar el archivo.\n" + e1.getMessage(), "Error");
				return;
			}
			File archivoSugerido = new File(nombreSugerido.endsWith(extension) ? nombreSugerido : nombreSugerido.concat(extension));
			CLFileSelector.setLastSelectedFile(archivoSugerido);
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
	
	public Banco getBancoElegido() {
		return bancoElegido;
	}

	
	public void setBancoElegido(Banco bancoElegido) {
		this.bancoElegido = bancoElegido;
	}
	
	private JComboBox getCmbBanco() {
		if(cmbBanco == null){
			cmbBanco = new JComboBox();
			GuiUtil.llenarCombo(cmbBanco, getBancoFacade().getAllOrderByName(), true);
			cmbBanco.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if(e.getStateChange()==ItemEvent.SELECTED){
						setBancoElegido((Banco)cmbBanco.getSelectedItem());
					}
				}
			});
			Banco bancoDefault = GTLBeanFactory.getInstance().getBean2(ParametrosGeneralesFacadeRemote.class).getParametrosGenerales().getBancoDefault();
			if(bancoDefault!=null){
				cmbBanco.setSelectedItem(bancoDefault);
				setBancoElegido(bancoDefault);
			}else{
				setBancoElegido((Banco)cmbBanco.getSelectedItem());
			}
		}
		return cmbBanco;
	}
	
	private BancoFacadeRemote getBancoFacade(){
		if(bancoFacade == null){
			bancoFacade = GTLBeanFactory.getInstance().getBean2(BancoFacadeRemote.class);
		}
		return bancoFacade;
	}

}
