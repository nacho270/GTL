package ar.com.textillevel.gui.acciones;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

import main.acciones.informes.InformeStockAction.ItemSimpleInformeStockTO;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import ar.com.fwcommon.boss.BossEstilos;
import ar.com.fwcommon.componentes.FWFileSelector;
import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTable;
import ar.com.fwcommon.componentes.PanelTablaSinBotones;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.fwcommon.util.MiscUtil;
import ar.com.textillevel.gui.util.JasperHelper;

public class JDialogInformeStock extends JDialog {

	private static final long serialVersionUID = -8902172799078457735L;
	private static final String EXTENSION_EXCEL = ".xls";
	private static final String EXTENSION_PDF = ".pdf";
	
	private PanelTablaItems panelTablaItems;
	private List<ItemSimpleInformeStockTO> items;

	private JasperPrint print;
	private JButton btnExportarAExcel;
	private JButton btnListadoPDF;
	private JButton btnImprimir;
	
	public JDialogInformeStock(Frame padre, List<ItemSimpleInformeStockTO> items) {
		super(padre);
		setItems(items);
		setUpComponentes();
		setUpScreen();
		llenarTabla();
	}

	private void setUpScreen() {
		setTitle("Informe de stock");
		setSize(new Dimension(550, 600));
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		GuiUtil.centrar(this);
		setModal(true);
		setResizable(false);
	}

	private void setUpComponentes() {
		this.add(getPanelTablaItems(), BorderLayout.CENTER);
		this.add(getPanelSur(), BorderLayout.SOUTH);
	}

	private void llenarTabla() {
		for (ItemSimpleInformeStockTO c : getItems()) {
			getPanelTablaItems().agregarElemento(c);
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

	private class PanelTablaItems extends PanelTablaSinBotones<ItemSimpleInformeStockTO> {

		private static final long serialVersionUID = 4247457498635763788L;

		private static final int CANT_COLS_TBL_CLIENTES = 3;
		private static final int COL_NOMBRES_PM = 0;
		private static final int COL_STOCK = 1;
		private static final int COL_OBJ = 2;

		@Override
		protected void agregarElemento(ItemSimpleInformeStockTO item) {
			Object[] row = new Object[CANT_COLS_TBL_CLIENTES];
			row[COL_NOMBRES_PM] = item.getNombre();
			row[COL_STOCK] = item.getStock();
			row[COL_OBJ] = item;
			getTabla().addRow(row);
		}

		@Override
		protected FWJTable construirTabla() {
			FWJTable tabla = new FWJTable(0, CANT_COLS_TBL_CLIENTES);
			tabla.setStringColumn(COL_NOMBRES_PM, "Item", 400, 400, true);
			tabla.setStringColumn(COL_STOCK, "Stock", 100, 100, true);
			tabla.setStringColumn(COL_OBJ, "", 0, 0, true);
			tabla.setAllowHidingColumns(false);
			tabla.setAllowSorting(true);
			tabla.setReorderingAllowed(false);
			return tabla;
		}

		@Override
		protected ItemSimpleInformeStockTO getElemento(int fila) {
			return (ItemSimpleInformeStockTO) getTabla().getValueAt(fila, COL_OBJ);
		}

		@Override
		protected String validarElemento(int fila) {
			return null;
		}
	}

	private PanelTablaItems getPanelTablaItems() {
		if (panelTablaItems == null) {
			panelTablaItems = new PanelTablaItems();
		}
		return panelTablaItems;
	}

	public static void main(String[] args) {
		new JDialogListaDeChequesPorVencer(null, null);
	}
	
	public JasperPrint getPrint() {
		if (print == null) {
			JasperReport reporte = JasperHelper.loadReporte("/ar/com/textillevel/reportes/informe-stock.jasper");
			setPrint(JasperHelper.fillReport(reporte, null, getItems()));
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
				FWJOptionPane.showErrorMessage(JDialogInformeStock.this, "Se ha producido un error al guardar el archivo.\n" + e1.getMessage(), "Error");
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
					if (getPanelTablaItems().getTabla().getRowCount() > 0) {
						FWJTable tabla = getPanelTablaItems().getTabla();
						mostrarFileChooser("Informe de stock", EXTENSION_EXCEL);
						File archivoIngresado = FWFileSelector.obtenerArchivo(FWFileSelector.SAVE, FWFileSelector.FILES_ONLY, new FiltroArchivosExcel(), null);
						if (archivoIngresado != null) {
							if (!archivoIngresado.getAbsolutePath().toLowerCase().endsWith(EXTENSION_EXCEL)) {
								archivoIngresado = new File(archivoIngresado.getAbsolutePath().concat(EXTENSION_EXCEL));
							}
							MiscUtil.exportarAExcel(tabla, "Informe de clientes con deuda", "", null, archivoIngresado.getAbsolutePath(), null, 
									System.getProperty("intercalarColoresFilas") != null
									&& System.getProperty("intercalarColoresFilas").equals(String.valueOf(true)));
						}
					}
				}
			});
		}
		return btnExportarAExcel;
	}

	private JButton getBtnListadoPDF() {
		if (btnListadoPDF == null) {
			btnListadoPDF = BossEstilos.createButton("ar/com/textillevel/imagenes/btn_pdf.png", "ar/com/textillevel/imagenes/btn_pdf_des.png");
			btnListadoPDF.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if (getPanelTablaItems().getTabla().getRowCount() > 0) {
						mostrarFileChooser("Informe de stock", EXTENSION_PDF);
						File archivoIngresado = FWFileSelector.obtenerArchivo(FWFileSelector.SAVE, FWFileSelector.FILES_ONLY, new FiltroArchivosPDF(), null);
						if (archivoIngresado != null) {
							if (!archivoIngresado.getAbsolutePath().toLowerCase().endsWith(EXTENSION_PDF)) {
								archivoIngresado = new File(archivoIngresado.getAbsolutePath().concat(EXTENSION_PDF));
							}
							try {
								JasperHelper.exportarAPDF(getPrint(), archivoIngresado.getAbsolutePath());
							} catch (JRException e1) {
								e1.printStackTrace();
								FWJOptionPane.showErrorMessage(JDialogInformeStock.this, "Se ha producido un error al expoertar", "Error");
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
	
	public List<ItemSimpleInformeStockTO> getItems() {
		return items;
	}

	
	public void setItems(List<ItemSimpleInformeStockTO> items) {
		this.items = items;
	}
}
