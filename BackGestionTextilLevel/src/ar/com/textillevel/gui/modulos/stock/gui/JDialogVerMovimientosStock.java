package ar.com.textillevel.gui.modulos.stock.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

import ar.com.fwcommon.boss.BossEstilos;
import ar.com.fwcommon.componentes.FWFileSelector;
import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTable;
import ar.com.fwcommon.componentes.FWJTextField;
import ar.com.fwcommon.componentes.PanelTablaSinBotones;
import ar.com.fwcommon.componentes.VerticalFlowLayout;
import ar.com.fwcommon.util.DateUtil;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.entidades.stock.MovimientoStock;
import ar.com.textillevel.entidades.stock.MovimientoStockSuma;
import ar.com.textillevel.entidades.ventas.materiaprima.PrecioMateriaPrima;
import ar.com.textillevel.facade.api.remote.MovimientoStockFacadeRemote;
import ar.com.textillevel.gui.modulos.stock.gui.visitor.ConsultarDocumentoStockVisitor;
import ar.com.textillevel.gui.modulos.stock.gui.visitor.GenerarFilaMovimientoStockVisitor;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.JasperHelper;
import ar.com.textillevel.gui.util.controles.PanelDatePicker;
import ar.com.textillevel.gui.util.controles.PanelPaginador;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogVerMovimientosStock extends JDialog {

	private static final long serialVersionUID = -9186580759475611219L;

	private static final String EXTENSION_EXCEL = ".xls";
	private static final String EXTENSION_PDF = ".pdf";
	public static final Integer MAX_ROWS = 30;

	private PrecioMateriaPrima precioMateriaPrima;

	private PanelDatePicker panelFechaDesde;
	private PanelDatePicker panelFechaHasta;
	private JButton btnBuscar;
	private JButton btnExportarAExcel;
	private JButton btnImprimirListado;
	private JButton btnListadoPDF;
	private JButton btnAceptar;
	private JButton btnConsultarDocumento;
	private PanelTablaMovmientosStock panelTablaMovimientos;
	private PanelPaginador panelPaginador;

	private JPanel panelCabecera;
	private JPanel panelBotones;

	private FWJTextField txtStockActual;
	
	private MovimientoStockFacadeRemote movimientoStockFacade;

	private final Frame padre;
	
	public JDialogVerMovimientosStock(Frame owner, PrecioMateriaPrima pm) {
		super(owner);
		this.padre = owner;
		setPrecioMateriaPrima(pm);
		setUpComponentes();
		setUpScreen();
		buscar();
	}

	private void setUpScreen() {
		setTitle("Ver movimientos de stock");
		setSize(new Dimension(800, 600));
		setModal(true);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setResizable(false);
		GuiUtil.centrar(this);
	}

	private void setUpComponentes() {
		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				salir();
			}
		});
		add(getPanelCabecera(), BorderLayout.NORTH);
		add(getPanelTablaMovimientos(), BorderLayout.CENTER);
		add(getPanelBotones(), BorderLayout.SOUTH);
	}

	public PrecioMateriaPrima getPrecioMateriaPrima() {
		return precioMateriaPrima;
	}

	public void setPrecioMateriaPrima(PrecioMateriaPrima precioMateriaPrima) {
		this.precioMateriaPrima = precioMateriaPrima;
	}

	private static class PanelTablaMovmientosStock extends PanelTablaSinBotones<MovimientoStock> {

		private static final long serialVersionUID = 180462022384721721L;

		private static final Integer CANT_COLS = 5;
		private static final Integer COL_PROVEEDOR = 0;
		private static final Integer COL_DOCUMENTO = 1;
		private static final Integer COL_CANT_CANT = 2;
		private static final Integer COL_FECHA_MOV = 3;
		private static final Integer COL_OBJ = 4;

		private final JDialogVerMovimientosStock owner;
		
		public PanelTablaMovmientosStock(JDialogVerMovimientosStock owner){
			this.owner = owner;
		}
		
		@Override
		protected void agregarElemento(MovimientoStock elemento) {
			elemento.aceptarVisitor(createVisitorFilaMovimientos());
		}

		@Override
		protected FWJTable construirTabla() {
			FWJTable tabla = new FWJTable(0, CANT_COLS);
			tabla.setStringColumn(COL_PROVEEDOR, "Proveedor", 120, 120, true);
			tabla.setStringColumn(COL_DOCUMENTO, "Documento", 200, 200, true);
			tabla.setFloatColumn(COL_CANT_CANT, "Stock nuevo", 100, true);
			tabla.setDateColumn(COL_FECHA_MOV, "Fecha", 80, true);
			tabla.setStringColumn(COL_OBJ, "", 0);

			tabla.setHeaderAlignment(COL_PROVEEDOR, FWJTable.CENTER_ALIGN);
			tabla.setHeaderAlignment(COL_CANT_CANT, FWJTable.CENTER_ALIGN);
			tabla.setHeaderAlignment(COL_FECHA_MOV, FWJTable.CENTER_ALIGN);
			tabla.setHeaderAlignment(COL_DOCUMENTO, FWJTable.CENTER_ALIGN);

			tabla.setReorderingAllowed(false);
			tabla.setAllowHidingColumns(false);
			
			tabla.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseReleased(MouseEvent e) {
					if (e.getClickCount() == 2 && getTabla().getSelectedRow() > -1) {
						consultarDocumento();
					} else if (e.getClickCount() == 1 && getTabla().getSelectedRow() > -1) {
						owner.getBtnConsultarDocumento().setEnabled(true);
					} else if (getTabla().getSelectedRow() < 0) {
						owner.getBtnConsultarDocumento().setEnabled(false);
					}
				}
			});
			
			return tabla;
		}

		@Override
		protected MovimientoStock getElemento(int fila) {
			return (MovimientoStock) getTabla().getValueAt(fila, COL_OBJ);
		}

		@Override
		protected String validarElemento(int fila) {
			return null;
		}

		public GenerarFilaMovimientoStockVisitor createVisitorFilaMovimientos() {
			return new GenerarFilaMovimientoStockVisitor(getTabla(), CANT_COLS);
		}

		private void consultarDocumento() {
			ConsultarDocumentoStockVisitor cdsv = new ConsultarDocumentoStockVisitor(owner.padre);
			((MovimientoStock)getTabla().getValueAt(getTabla().getSelectedRow(), COL_OBJ)).aceptarVisitor(cdsv);
		}
	}

	private PanelDatePicker getPanelFechaDesde() {
		if (panelFechaDesde == null) {
			panelFechaDesde = new PanelDatePicker();
			panelFechaDesde.setCaption("Fecha desde:");
			panelFechaDesde.setSelectedDate(DateUtil.restarDias(DateUtil.getHoy(), 60));
		}
		return panelFechaDesde;
	}

	private PanelDatePicker getPanelFechaHasta() {
		if (panelFechaHasta == null) {
			panelFechaHasta = new PanelDatePicker();
			panelFechaHasta.setCaption("Fecha hasta:");
		}
		return panelFechaHasta;
	}

	private JButton getBtnBuscar() {
		if (btnBuscar == null) {
			btnBuscar = new JButton("Buscar");
			btnBuscar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					buscar();
				}
			});
		}
		return btnBuscar;
	}
	
	private void buscar() {
		if (getPanelFechaHasta().getDate().before(getPanelFechaDesde().getDate())) {
			FWJOptionPane.showErrorMessage(JDialogVerMovimientosStock.this, "La fecha 'Hasta' debe ser mayor o igual que la fecha 'Desde'", "Validación de fechas");
			return;
		}
		getPanelPaginador().setPageIndex(1);
		buscarItems();
	}

	private void buscarItems() {
		List<MovimientoStock> movs = getMovimientoStockFacade().getAllMovimientosByPrecioMateriaPrimaPorFechaYPaginado(getPrecioMateriaPrima(),
				new java.sql.Date(getPanelFechaDesde().getDate().getTime()), new java.sql.Date(getPanelFechaHasta().getDate().getTime()), getPanelPaginador().getPageIndex(), MAX_ROWS);
		getPanelTablaMovimientos().getTabla().removeAllRows();
		if (movs != null && !movs.isEmpty()) {
			Double stockActual = 0d;
			for (MovimientoStock m : movs) {
				boolean habilitarBotonesImpresion = movs.size() > 0;
				getBtnExportarAExcel().setEnabled(habilitarBotonesImpresion);
				getBtnImprimirListado().setEnabled(habilitarBotonesImpresion);
				getBtnListadoPDF().setEnabled(habilitarBotonesImpresion);
				getPanelTablaMovimientos().agregarElemento(m);
				double cant = Math.abs(m.getCantidad().doubleValue());
				if(m instanceof MovimientoStockSuma){
					stockActual+=cant;
				}else{
					stockActual-=cant;
				}
			}
			getTxtStockActual().setText(GenericUtils.fixPrecioCero(GenericUtils.getDecimalFormat().format(stockActual)));
			refrescarPaginador();
		}
	}

	private void refrescarPaginador() {
		Integer rows = this.getCantidadDeRegistros();
		getPanelPaginador().setRowsCount(rows);
		getPanelPaginador().setRowsPageSize(MAX_ROWS);
	}

	private Integer getCantidadDeRegistros() {
		return getMovimientoStockFacade().getCantidadMovimientosByPrecioMateriaPrimaPorFecha(getPrecioMateriaPrima(), new java.sql.Date(getPanelFechaDesde().getDate().getTime()),
				new java.sql.Date(getPanelFechaHasta().getDate().getTime()));
	}

	private JButton getBtnExportarAExcel() {
		if (btnExportarAExcel == null) {
			btnExportarAExcel = BossEstilos.createButton("ar/com/fwcommon/imagenes/b_exportar_excel.png", "ar/com/fwcommon/imagenes/b_exportar_excel_des.png");
			btnExportarAExcel.setEnabled(false);
			btnExportarAExcel.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if (getPanelTablaMovimientos().getTabla().getRowCount() > 0) {
						FWJTable tabla = getPanelTablaMovimientos().getTabla();
						mostrarFileChooser("Listado de Movimientos - " + getPrecioMateriaPrima().getMateriaPrima().getDescripcion() + " - " + getPrecioMateriaPrima().getAlias(), EXTENSION_EXCEL);
						File archivoIngresado = FWFileSelector.obtenerArchivo(FWFileSelector.SAVE, FWFileSelector.FILES_ONLY, new FiltroArchivosExcel(), null);
						if (archivoIngresado != null) {
							if (!archivoIngresado.getAbsolutePath().toLowerCase().endsWith(EXTENSION_EXCEL)) {
								archivoIngresado = new File(archivoIngresado.getAbsolutePath().concat(EXTENSION_EXCEL));
							}
							GenericUtils.exportarAExcel(tabla, "Listado de Movimientos - " + getPrecioMateriaPrima().getMateriaPrima().getDescripcion() + " - " + getPrecioMateriaPrima().getAlias(),
									"Periodo: " + DateUtil.dateToString(getPanelFechaDesde().getDate(), DateUtil.SHORT_DATE) + " - "
											+ DateUtil.dateToString(getPanelFechaHasta().getDate(), DateUtil.SHORT_DATE), null, archivoIngresado.getAbsolutePath(), System
											.getProperty("intercalarColoresFilas") != null
											&& System.getProperty("intercalarColoresFilas").equals(String.valueOf(true)));
						}
					}
				}
			});
		}
		return btnExportarAExcel;
	}
	
	private JButton getBtnImprimirListado() {
		if (btnImprimirListado == null) {
			btnImprimirListado = BossEstilos.createButton("ar/com/textillevel/imagenes/b_imp_listado.png", "ar/com/textillevel/imagenes/b_imp_listado_des.png");
			btnImprimirListado.setEnabled(false);
			btnImprimirListado.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					JasperHelper.imprimirListado(getPanelTablaMovimientos().getTabla(), "Listado de Movimientos - " + getPrecioMateriaPrima().getMateriaPrima().getDescripcion() + " - "
							+ getPrecioMateriaPrima().getAlias(), "Periodo: " + DateUtil.dateToString(getPanelFechaDesde().getDate(), DateUtil.SHORT_DATE) + " - "
							+ DateUtil.dateToString(getPanelFechaHasta().getDate(), DateUtil.SHORT_DATE), null, false);
				}
			});
		}
		return btnImprimirListado;
	}

	private JButton getBtnListadoPDF() {
		if (btnListadoPDF == null) {
			btnListadoPDF = BossEstilos.createButton("ar/com/textillevel/imagenes/btn_pdf.png", "ar/com/textillevel/imagenes/btn_pdf_des.png");
			btnListadoPDF.setEnabled(false);
			btnListadoPDF.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if (getPanelTablaMovimientos().getTabla().getRowCount() > 0) {
						FWJTable tabla = getPanelTablaMovimientos().getTabla();
						mostrarFileChooser("Listado de Movimientos - " + getPrecioMateriaPrima().getMateriaPrima().getDescripcion() + " - " + getPrecioMateriaPrima().getAlias(), EXTENSION_PDF);
						File archivoIngresado = FWFileSelector.obtenerArchivo(FWFileSelector.SAVE, FWFileSelector.FILES_ONLY, new FiltroArchivosPDF(), null);
						if (archivoIngresado != null) {
							if (!archivoIngresado.getAbsolutePath().toLowerCase().endsWith(EXTENSION_PDF)) {
								archivoIngresado = new File(archivoIngresado.getAbsolutePath().concat(EXTENSION_PDF));
							}
							JasperHelper.listadoAPDF(tabla, "Listado de Movimientos - " + getPrecioMateriaPrima().getMateriaPrima().getDescripcion() + " - " + getPrecioMateriaPrima().getAlias(),
									"Periodo: " + DateUtil.dateToString(getPanelFechaDesde().getDate(), DateUtil.SHORT_DATE) + " - "
											+ DateUtil.dateToString(getPanelFechaHasta().getDate(), DateUtil.SHORT_DATE), null, false, archivoIngresado.getAbsolutePath());
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
				FWJOptionPane.showErrorMessage(JDialogVerMovimientosStock.this, "Se ha producido un error al guardar el archivo.\n" + e1.getMessage(), "Error");
				return;
			}
			File archivoSugerido = new File(nombreSugerido.endsWith(extension) ? nombreSugerido : nombreSugerido.concat(extension));
			FWFileSelector.setLastSelectedFile(archivoSugerido);
		}
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


	private PanelTablaMovmientosStock getPanelTablaMovimientos() {
		if (panelTablaMovimientos == null) {
			panelTablaMovimientos = new PanelTablaMovmientosStock(this);
		}
		return panelTablaMovimientos;
	}

	private JPanel getPanelCabecera() {
		if (panelCabecera == null) {

			JPanel panelCabeceraSup = new JPanel();
			panelCabeceraSup.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
			panelCabeceraSup.add(getPanelFechaDesde());
			panelCabeceraSup.add(getPanelFechaHasta());
			panelCabeceraSup.add(getBtnBuscar());

			JPanel panelCabeceraInf = new JPanel();
			panelCabeceraInf.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
			panelCabeceraInf.add(getPanelPaginador());

			JPanel panelCabeceraTot = new JPanel();
			panelCabeceraTot.setLayout(new VerticalFlowLayout(VerticalFlowLayout.CENTER, 5, 5));
			panelCabeceraTot.add(panelCabeceraSup);
			panelCabeceraTot.add(panelCabeceraInf);

			panelCabecera = new JPanel();
			panelCabecera.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
			panelCabecera.add(panelCabeceraTot);
		}
		return panelCabecera;
	}

	private JPanel getPanelBotones() {
		if (panelBotones == null) {
			panelBotones = new JPanel();
			panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
			panelBotones.add(getBtnAceptar());
			panelBotones.add(getBtnConsultarDocumento());
			panelBotones.add(getBtnImprimirListado());
			panelBotones.add(getBtnExportarAExcel());
			panelBotones.add(getBtnListadoPDF());
			panelBotones.add(new JLabel("    stock actual: "));
			panelBotones.add(getTxtStockActual());
		}
		return panelBotones;
	}

	private JButton getBtnAceptar() {
		if (btnAceptar == null) {
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					salir();
				}
			});
		}
		return btnAceptar;
	}

	private void salir() {
		if (FWJOptionPane.showQuestionMessage(this, "Está seguro que desea salir?", "Pregunta") == FWJOptionPane.YES_OPTION) {
			dispose();
		}
	}

	private PanelPaginador getPanelPaginador() {
		if (panelPaginador == null) {
			panelPaginador = new PanelPaginador();
		}
		return panelPaginador;
	}

	private MovimientoStockFacadeRemote getMovimientoStockFacade() {
		if (movimientoStockFacade == null) {
			movimientoStockFacade = GTLBeanFactory.getInstance().getBean2(MovimientoStockFacadeRemote.class);
		}
		return movimientoStockFacade;
	}

	private JButton getBtnConsultarDocumento() {
		if(btnConsultarDocumento == null){
			btnConsultarDocumento = BossEstilos.createButton("ar/com/textillevel/imagenes/b_consultar_contacto.png", "ar/com/textillevel/imagenes/b_consultar_contacto_des.png");
			btnConsultarDocumento.setEnabled(false);
			btnConsultarDocumento.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					getPanelTablaMovimientos().consultarDocumento();
				}
			});
		}
		return btnConsultarDocumento;
	}
	
	private FWJTextField getTxtStockActual() {
		if(txtStockActual == null){
			txtStockActual = new FWJTextField();
			txtStockActual.setEditable(false);
			txtStockActual.setPreferredSize(new Dimension(120, 20));
		}
		return txtStockActual;
	}
}
