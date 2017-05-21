package ar.com.textillevel.gui.acciones;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import ar.com.fwcommon.boss.BossEstilos;
import ar.com.fwcommon.componentes.FWFileSelector;
import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTable;
import ar.com.fwcommon.componentes.FWJTextField;
import ar.com.fwcommon.componentes.PanelTablaSinBotones;
import ar.com.fwcommon.componentes.VerticalFlowLayout;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.gui.util.ClienteMorosoTO;
import ar.com.textillevel.gui.util.ETipoInformeDeuda;
import ar.com.textillevel.gui.util.ExportadorExcel;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.JasperHelper;
import ar.com.textillevel.gui.util.TituloInfoTO;

public class JDialogInformeClientesMorosos extends JDialog {

	private static final long serialVersionUID = -8902172799078457735L;
	private static final String EXTENSION_EXCEL = ".xls";
	private static final String EXTENSION_PDF = ".pdf";
	
	private PanelTablaClientes panelTablaClientes;
	private List<ClienteMorosoTO> clientes;
	private BigDecimal totalDeuda;
	private FWJTextField txtTotalDeuda;
	private ETipoInformeDeuda tipoInforme;

	private JasperPrint print;
	private JButton btnExportarAExcel;
	private JButton btnListadoPDF;
	private JButton btnImprimir;
	
	public JDialogInformeClientesMorosos(Frame padre, List<ClienteMorosoTO> clientesAMostrar, BigDecimal totalDeuda, ETipoInformeDeuda tipoInforme) {
		super(padre);
		setTipoInforme(tipoInforme);
		setClientes(clientesAMostrar);
		setTotalDeuda(totalDeuda);
		setUpComponentes();
		setUpScreen();
		llenarTabla();
	}

	private void setUpScreen() {
		setTitle(getTipoInforme()==ETipoInformeDeuda.CLIENTE?"Clientes con deuda":"Deudas con proveedores");
		setSize(new Dimension(450, 550));
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		GuiUtil.centrar(this);
		setModal(true);
		setResizable(false);
	}

	private void setUpComponentes() {
		this.add(getPanelCentro(), BorderLayout.CENTER);
		this.add(getPanelSur(), BorderLayout.SOUTH);
	}

	private void llenarTabla() {
		for (ClienteMorosoTO c : getClientes()) {
			getPanelTablaClientes().agregarElemento(c);
		}
	}

	private JPanel getPanelSur() {
		JButton btnSalir = new JButton("Aceptar");
		btnSalir.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.CENTER));
		panel.add(btnSalir);
		panel.add(getBtnImprimir());
		panel.add(getBtnExportarAExcel());
		panel.add(getBtnListadoPDF());
		return panel;
	}
	
	private JPanel getPanelCentro() {
		JPanel panel = new JPanel();
		panel.setLayout(new VerticalFlowLayout(VerticalFlowLayout.CENTER,5,5));
		panel.add(getPanelTablaClientes());
		
		JPanel panelTotal = new JPanel();
		panelTotal.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		panelTotal.add(new JLabel("Total: "));
		panelTotal.add(getTxtTotalDeuda());
		
		panel.add(getPanelTablaClientes());
		panel.add(panelTotal);
		return panel;
	}

	private class PanelTablaClientes extends PanelTablaSinBotones<ClienteMorosoTO> {

		private static final long serialVersionUID = 4247457498635763788L;

		private static final int CANT_COLS_TBL_CLIENTES = 3;
		private static final int COL_RAZON_SOCIAL = 0;
		private static final int COL_DEUDA = 1;
		private static final int COL_OBJ = 2;

		@Override
		protected void agregarElemento(ClienteMorosoTO cliente) {
			Object[] row = new Object[CANT_COLS_TBL_CLIENTES];
			row[COL_RAZON_SOCIAL] = cliente.getRazonSocial();
			row[COL_DEUDA] = cliente.getMontoDeuda();
			row[COL_OBJ] = cliente;
			getTabla().addRow(row);
		}

		@Override
		protected FWJTable construirTabla() {
			FWJTable tabla = new FWJTable(0, CANT_COLS_TBL_CLIENTES);
			tabla.setStringColumn(COL_RAZON_SOCIAL, "Razón social", 320, 320, true);
			tabla.setStringColumn(COL_DEUDA, "Deuda", 100, 100, true);
			tabla.setStringColumn(COL_OBJ, "", 0, 0, true);
			tabla.setAllowHidingColumns(false);
			tabla.setAllowSorting(true);
			tabla.setReorderingAllowed(false);
			return tabla;
		}

		@Override
		protected ClienteMorosoTO getElemento(int fila) {
			return (ClienteMorosoTO) getTabla().getValueAt(fila, COL_OBJ);
		}

		@Override
		protected String validarElemento(int fila) {
			return null;
		}
	}

	private PanelTablaClientes getPanelTablaClientes() {
		if (panelTablaClientes == null) {
			panelTablaClientes = new PanelTablaClientes();
		}
		return panelTablaClientes;
	}

	public static void main(String[] args) {
		new JDialogListaDeChequesPorVencer(null, null);
	}

	public List<ClienteMorosoTO> getClientes() {
		return clientes;
	}

	public void setClientes(List<ClienteMorosoTO> clientes) {
		this.clientes = clientes;
	}
	
	public JasperPrint getPrint() {
		if (print == null) {
			Map<String, Object> parameters = getParametros();
			JasperReport reporte = JasperHelper.loadReporte("/ar/com/textillevel/reportes/reporte-morosos.jasper");
			setPrint(JasperHelper.fillReport(reporte, parameters, getClientes()));
		}
		return print;
	}

	public void setPrint(JasperPrint print) {
		this.print = print;
	}
	
	private Map<String, Object> getParametros() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("TOTAL_DEUDA", getTxtTotalDeuda().getText());
		map.put("TITULO", getTipoInforme()==ETipoInformeDeuda.CLIENTE?"DE CLIENTES":"CON PROVEEDORES");
		map.put("IS_TEST", GenericUtils.isSistemaTest());
		return map;
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
				FWJOptionPane.showErrorMessage(JDialogInformeClientesMorosos.this, "Se ha producido un error al guardar el archivo.\n" + e1.getMessage(), "Error");
				return;
			}
			File archivoSugerido = new File(nombreSugerido.endsWith(extension) ? nombreSugerido : nombreSugerido.concat(extension));
			FWFileSelector.setLastSelectedFile(archivoSugerido);
		}
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
	
	private JButton getBtnExportarAExcel() {
		if (btnExportarAExcel == null) {
			btnExportarAExcel = BossEstilos.createButton("ar/com/fwcommon/imagenes/b_exportar_excel.png", "ar/com/fwcommon/imagenes/b_exportar_excel_des.png");
			btnExportarAExcel.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if (getPanelTablaClientes().getTabla().getRowCount() > 0) {
						FWJTable tabla = getPanelTablaClientes().getTabla();
						mostrarFileChooser(getTipoInforme()==ETipoInformeDeuda.CLIENTE?"Informe de clientes con deuda":"Informe de deudas con proveedores", EXTENSION_EXCEL);
						File archivoIngresado = FWFileSelector.obtenerArchivo(FWFileSelector.SAVE, FWFileSelector.FILES_ONLY, new FiltroArchivosExcel(), null);
						if (archivoIngresado != null) {
							if (!archivoIngresado.getAbsolutePath().toLowerCase().endsWith(EXTENSION_EXCEL)) {
								archivoIngresado = new File(archivoIngresado.getAbsolutePath().concat(EXTENSION_EXCEL));
							}
							new ExportadorExcel().exportarAExcel(tabla, "Informe de "+getTipoInforme().toString().toLowerCase()+" con deuda", "", null, archivoIngresado.getAbsolutePath(), null, 
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
		listaTot.add(new TituloInfoTO("Total",getTxtTotalDeuda().getText()));
		ret.add(listaTot);
		return ret;
	}	

	private JButton getBtnListadoPDF() {
		if (btnListadoPDF == null) {
			btnListadoPDF = BossEstilos.createButton("ar/com/textillevel/imagenes/btn_pdf.png", "ar/com/textillevel/imagenes/btn_pdf_des.png");
			btnListadoPDF.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if (getPanelTablaClientes().getTabla().getRowCount() > 0) {
						mostrarFileChooser(getTipoInforme()==ETipoInformeDeuda.CLIENTE?"Informe de clientes con deuda":"Informe de deudas con proveedores", EXTENSION_PDF);
						File archivoIngresado = FWFileSelector.obtenerArchivo(FWFileSelector.SAVE, FWFileSelector.FILES_ONLY, new FiltroArchivosPDF(), null);
						if (archivoIngresado != null) {
							if (!archivoIngresado.getAbsolutePath().toLowerCase().endsWith(EXTENSION_PDF)) {
								archivoIngresado = new File(archivoIngresado.getAbsolutePath().concat(EXTENSION_PDF));
							}
							try {
								JasperHelper.exportarAPDF(getPrint(), archivoIngresado.getAbsolutePath());
							} catch (JRException e1) {
								e1.printStackTrace();
								FWJOptionPane.showErrorMessage(JDialogInformeClientesMorosos.this, "Se ha producido un error al expoertar", "Error");
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

	public FWJTextField getTxtTotalDeuda() {
		if(txtTotalDeuda == null){
			txtTotalDeuda = new FWJTextField();
			txtTotalDeuda.setEditable(false);
			txtTotalDeuda.setPreferredSize(new Dimension(120, 20));
			if(getTotalDeuda()!=null){
				txtTotalDeuda.setText("$ " + GenericUtils.getDecimalFormat().format(getTotalDeuda()));
			}
		}
		return txtTotalDeuda;
	}

	public BigDecimal getTotalDeuda() {
		return totalDeuda;
	}
	
	public void setTotalDeuda(BigDecimal totalDeuda) {
		this.totalDeuda = totalDeuda;
	}

	
	public ETipoInformeDeuda getTipoInforme() {
		return tipoInforme;
	}

	
	public void setTipoInforme(ETipoInformeDeuda tipoInforme) {
		this.tipoInforme = tipoInforme;
	}
}
