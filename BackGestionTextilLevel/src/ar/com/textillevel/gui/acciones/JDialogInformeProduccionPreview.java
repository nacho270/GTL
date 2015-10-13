package ar.com.textillevel.gui.acciones;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.filechooser.FileFilter;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import ar.com.fwcommon.boss.BossEstilos;
import ar.com.fwcommon.componentes.FWFileSelector;
import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTable;
import ar.com.fwcommon.componentes.FWJTableAnalisis;
import ar.com.fwcommon.componentes.FWJTextField;
import ar.com.fwcommon.componentes.PanelTablaSinBotones;
import ar.com.fwcommon.componentes.VerticalFlowLayout;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.entidades.to.informeproduccion.ClienteCantidadTO;
import ar.com.textillevel.entidades.to.informeproduccion.InformeProduccionTO;
import ar.com.textillevel.entidades.to.informeproduccion.ItemInformeProduccionTO;
import ar.com.textillevel.gui.util.ExportadorExcel;
import ar.com.textillevel.gui.util.JasperHelper;
import ar.com.textillevel.gui.util.TituloInfoTO;

public class JDialogInformeProduccionPreview extends JDialog {

	private static final long serialVersionUID = -5474362497323185844L;

	private static final String EXTENSION_EXCEL = ".xls";
	private static final String EXTENSION_PDF = ".pdf";

	private InformeProduccionTO informeProduccion;

	private JPanel panelBotones;
	private JPanel panelSuperior;
	private JButton btnImprimir;
	private JButton btnAceptar;
	private JButton btnExportarAExcel;
	private JButton btnListadoPDF;
	private JButton btnSalir;

	private PanelTablaInformeProduccion panelTabla;

	private JasperPrint print;

	private FWJTextField txtTotal;
	private FWJTextField txtFechaDesde;
	private FWJTextField txtFechaHasta;
	private FWJTextField txtTotalDias;
	
	public JDialogInformeProduccionPreview(Frame padre, InformeProduccionTO informe, String strFechaDesde, String strFechaHasta, int restaFechas) {
		super(padre);
		setInformeProduccion(informe);
		setUpComponentes();
		setUpScreen();
		getTxtFechaDesde().setText(strFechaDesde);
		getTxtFechaHasta().setText(strFechaHasta);
		getTxtTotalDias().setText(String.valueOf(restaFechas));
		setDatosInforme();
	}

	private void setDatosInforme() {
		for(ItemInformeProduccionTO item : getInformeProduccion().getItems()){
			getPanelTabla().agregarElemento(item);
		}
		getTxtTotal().setText(getInformeProduccion().getTotal());
	}

	private void setUpScreen() {
		setTitle("Informe de producción");
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setModal(true);
		setSize(new Dimension(700, 600));
		GuiUtil.centrar(this);
		setResizable(false);
	}

	private void setUpComponentes() {
		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				salir();
			}
		});
		add(getPanelSuperior(),BorderLayout.NORTH);
		add(getPanelTabla(), BorderLayout.CENTER);
		add(getPanelBotones(), BorderLayout.SOUTH);
	}

	private class PanelTablaInformeProduccion extends PanelTablaSinBotones<ItemInformeProduccionTO> {

		private static final long serialVersionUID = -5244819513897874537L;

		private static final int CANT_COLS = 4;
		private static final int COL_FECHA = 0;
		private static final int COL_NOMBRE = 1;
		private static final int COL_CANT = 2;
		private static final int COL_OBJ = 3;
		
		@Override
		protected void agregarElemento(ItemInformeProduccionTO elemento) {
			boolean yaEstaElDia = false;
			for(ClienteCantidadTO item : elemento.getClienteCantidadList()){
				Object[] row = new Object[CANT_COLS];
				if(yaEstaElDia){
					row[COL_FECHA] = "";
				}else{
					row[COL_FECHA] = elemento.getDia();
					yaEstaElDia = true;
				}
				row[COL_NOMBRE] = item.getNombre();
				row[COL_CANT] = item.getCantidad();
				row[COL_OBJ] = elemento;
				getTabla().addRow(row);
			}
		}

		@Override
		protected FWJTable construirTabla() {
			FWJTableAnalisis tabla = new FWJTableAnalisis(0,CANT_COLS);
			tabla.setStringColumn(COL_FECHA,"Fecha",120,120,true);
			tabla.setStringColumn(COL_NOMBRE,"Nombre",300,300,true);
			tabla.setStringColumn(COL_CANT, "Cantidad", 150,150,true);
			tabla.setStringColumn(COL_OBJ, "", 0);
			tabla.setReorderingAllowed(false);
			tabla.setAllowHidingColumns(false);
			tabla.setAllowSorting(false);
			tabla.setAlignment(COL_FECHA, FWJTableAnalisis.CENTER_ALIGN);
			tabla.setAlignment(COL_NOMBRE, FWJTableAnalisis.CENTER_ALIGN);
			tabla.setAlignment(COL_CANT, FWJTableAnalisis.CENTER_ALIGN);
			tabla.setHeaderAlignment(COL_FECHA, FWJTableAnalisis.CENTER_ALIGN);
			tabla.setHeaderAlignment(COL_NOMBRE, FWJTableAnalisis.CENTER_ALIGN);
			tabla.setHeaderAlignment(COL_CANT, FWJTableAnalisis.CENTER_ALIGN);
			tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			tabla.setPermiteSelector(true);
			tabla.agruparColumna(COL_FECHA);
			return tabla;
		}

		@Override
		protected ItemInformeProduccionTO getElemento(int fila) {
			return null;
		}

		@Override
		protected String validarElemento(int fila) {
			return null;
		}

	}

	private void salir() {
		if (FWJOptionPane.showQuestionMessage(this, "Desea salir?", "Pregunta") == FWJOptionPane.YES_OPTION) {
			dispose();
		}
	}

	private JPanel getPanelBotones() {
		if (panelBotones == null) {
			panelBotones = new JPanel();
			panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 5));
			
			JPanel panVer = new JPanel();
			panVer.setLayout(new VerticalFlowLayout(VerticalFlowLayout.CENTER, 5, 5));
			
			JPanel panTotal = new JPanel();
			panTotal.setLayout(new FlowLayout(FlowLayout.CENTER,5,5));
			panTotal.add(new JLabel("Total: "));
			panTotal.add(getTxtTotal());
			panVer.add(panTotal);

			JPanel pnlBots = new JPanel();
			pnlBots.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 5));
			pnlBots.add(getBtnAceptar());
			pnlBots.add(getBtnSalir());
			pnlBots.add(getBtnImprimir());
			pnlBots.add(getBtnExportarAExcel());
			pnlBots.add(getBtnListadoPDF());
			panVer.add(pnlBots);
			panelBotones.add(panVer);
		}
		return panelBotones;
	}

	private JButton getBtnAceptar() {
		if (btnAceptar == null) {
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
		}
		return btnAceptar;
	}

	private JButton getBtnImprimir() {
		if (btnImprimir == null) {
			btnImprimir = BossEstilos.createButton("ar/com/textillevel/imagenes/b_imp_listado.png", "ar/com/textillevel/imagenes/b_imp_listado_des.png");
			btnImprimir.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					imprimir();
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

	private JButton getBtnExportarAExcel() {
		if (btnExportarAExcel == null) {
			btnExportarAExcel = BossEstilos.createButton("ar/com/fwcommon/imagenes/b_exportar_excel.png", "ar/com/fwcommon/imagenes/b_exportar_excel_des.png");
			btnExportarAExcel.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if (getPanelTabla().getTabla().getRowCount() > 0) {
						FWJTable tabla = getPanelTabla().getTabla();
						mostrarFileChooser("Informe de producción", EXTENSION_EXCEL);
						File archivoIngresado = FWFileSelector.obtenerArchivo(FWFileSelector.SAVE, FWFileSelector.FILES_ONLY, new FiltroArchivosExcel(), null);
						if (archivoIngresado != null) {
							if (!archivoIngresado.getAbsolutePath().toLowerCase().endsWith(EXTENSION_EXCEL)) {
								archivoIngresado = new File(archivoIngresado.getAbsolutePath().concat(EXTENSION_EXCEL));
							}
							new ExportadorExcel().exportarAExcel(tabla, "Informe de producción", "", null, archivoIngresado.getAbsolutePath(), null, 
									System.getProperty("intercalarColoresFilas") != null
									&& System.getProperty("intercalarColoresFilas").equals(String.valueOf(true)), getListaInformacion());
						}
					}
				}
			});
		}
		return btnExportarAExcel;
	}

	private List<List<TituloInfoTO>> getListaInformacion() {
		List<List<TituloInfoTO>> ret = new ArrayList<List<TituloInfoTO>>();
		List<TituloInfoTO> listaTot = new ArrayList<TituloInfoTO>();
		listaTot.add(new TituloInfoTO("Total",getTxtTotal().getText()));
		ret.add(listaTot);
		return ret;
	}	

	private JButton getBtnListadoPDF() {
		if (btnListadoPDF == null) {
			btnListadoPDF = BossEstilos.createButton("ar/com/textillevel/imagenes/btn_pdf.png", "ar/com/textillevel/imagenes/btn_pdf_des.png");
			btnListadoPDF.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if (getPanelTabla().getTabla().getRowCount() > 0) {
						mostrarFileChooser("Informe de producción", EXTENSION_PDF);
						File archivoIngresado = FWFileSelector.obtenerArchivo(FWFileSelector.SAVE, FWFileSelector.FILES_ONLY, new FiltroArchivosPDF(), null);
						if (archivoIngresado != null) {
							if (!archivoIngresado.getAbsolutePath().toLowerCase().endsWith(EXTENSION_PDF)) {
								archivoIngresado = new File(archivoIngresado.getAbsolutePath().concat(EXTENSION_PDF));
							}
							try {
								JasperHelper.exportarAPDF(getPrint(), archivoIngresado.getAbsolutePath());
							} catch (JRException e1) {
								e1.printStackTrace();
								FWJOptionPane.showErrorMessage(JDialogInformeProduccionPreview.this, "Se ha producido un error al expoertar", "Error");
							}
						}
					}
				}
			});
		}
		return btnListadoPDF;
	}

	private void imprimir(){
		try {
			JasperHelper.imprimirReporte(getPrint(), true, false, 1);
		} catch (JRException e1) {
			e1.printStackTrace();
			FWJOptionPane.showErrorMessage(this, "Se ha producido un error al imprimir", "Error");
		}
	}
	
	private Map<String, Object> getParametros() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("FECHA_DESDE", getTxtFechaDesde().getText());
		map.put("FECHA_HASTA", getTxtFechaHasta().getText());
		map.put("CANT_DIAS", getTxtTotalDias().getText());
		map.put("TOTAL", getInformeProduccion().getTotal());
		map.put("SUBREPORT_DIR", "ar/com/textillevel/reportes/");
		return map;
	}

	public JasperPrint getPrint() {
		if (print == null) {
			Map<String, Object> parameters = getParametros();
			JasperReport reporte = JasperHelper.loadReporte("/ar/com/textillevel/reportes/informe-produccion.jasper");
			setPrint(JasperHelper.fillReport(reporte, parameters, getInformeProduccion().getItems()));
		}
		return print;
	}

	public void setPrint(JasperPrint print) {
		this.print = print;
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
				FWJOptionPane.showErrorMessage(JDialogInformeProduccionPreview.this, "Se ha producido un error al guardar el archivo.\n" + e1.getMessage(), "Error");
				return;
			}
			File archivoSugerido = new File(nombreSugerido.endsWith(extension) ? nombreSugerido : nombreSugerido.concat(extension));
			FWFileSelector.setLastSelectedFile(archivoSugerido);
		}
	}

	public InformeProduccionTO getInformeProduccion() {
		return informeProduccion;
	}

	public void setInformeProduccion(InformeProduccionTO informeProduccion) {
		this.informeProduccion = informeProduccion;
	}

	private FWJTextField getTxtTotal() {
		if(txtTotal == null){
			txtTotal = new FWJTextField();
			txtTotal.setPreferredSize(new Dimension(120, 20));
			txtTotal.setEditable(false);
		}
		return txtTotal;
	}

	private PanelTablaInformeProduccion getPanelTabla() {
		if(panelTabla == null){
			panelTabla = new PanelTablaInformeProduccion();
		}
		return panelTabla;
	}
	
	private FWJTextField getTxtFechaDesde() {
		if(txtFechaDesde == null){
			txtFechaDesde = new FWJTextField();
			txtFechaDesde.setPreferredSize(new Dimension(120, 20));
			txtFechaDesde.setEditable(false);
		}
		return txtFechaDesde;
	}
	
	private FWJTextField getTxtFechaHasta() {
		if(txtFechaHasta==null){
			txtFechaHasta = new FWJTextField();
			txtFechaHasta.setPreferredSize(new Dimension(120, 20));
			txtFechaHasta.setEditable(false);
		}
		return txtFechaHasta;
	}

	private FWJTextField getTxtTotalDias() {
		if(txtTotalDias == null){
			txtTotalDias = new FWJTextField();
			txtTotalDias.setPreferredSize(new Dimension(120, 20));
			txtTotalDias.setEditable(false);
		}
		return txtTotalDias;
	}
	
	private JPanel getPanelSuperior() {
		if(panelSuperior == null){
			panelSuperior = new JPanel();
			panelSuperior.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 15));
			panelSuperior.add(new JLabel("Fecha desde: "));
			panelSuperior.add(getTxtFechaDesde());
			panelSuperior.add(new JLabel("Fecha hasta: "));
			panelSuperior.add(getTxtFechaHasta());
			panelSuperior.add(new JLabel("Total días: "));
			panelSuperior.add(getTxtTotalDias());
		}
		return panelSuperior;
	}
}
