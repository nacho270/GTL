package ar.com.textillevel.gui.acciones;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.DecimalFormat;
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
import ar.clarin.fwjava.boss.BossEstilos;
import ar.clarin.fwjava.componentes.CLFileSelector;
import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.CLJTable;
import ar.clarin.fwjava.componentes.CLJTextField;
import ar.clarin.fwjava.componentes.PanelTablaSinBotones;
import ar.clarin.fwjava.componentes.VerticalFlowLayout;
import ar.clarin.fwjava.util.DateUtil;
import ar.clarin.fwjava.util.GuiUtil;
import ar.com.textillevel.entidades.to.ivaventas.DescripcionFacturaIVAVentasTO;
import ar.com.textillevel.entidades.to.ivaventas.IVAVentasTO;
import ar.com.textillevel.entidades.to.ivaventas.TotalesIVAVentasTO;
import ar.com.textillevel.facade.api.remote.ParametrosGeneralesFacadeRemote;
import ar.com.textillevel.gui.util.ExportadorExcel;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.JasperHelper;
import ar.com.textillevel.gui.util.TituloInfoTO;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogReporteIVAVentasPreview extends JDialog {

	private static final long serialVersionUID = 5060729556680752097L;

	private static final String EXTENSION_EXCEL = ".xls";
	private static final String EXTENSION_PDF = ".pdf";

	private JasperPrint print;

	private IVAVentasTO reporte;
	private String periodo;

	private JButton btnImprimir;
	private JButton btnAceptar;
	private JButton btnExportarAExcel;
	private JButton btnListadoPDF;
	private JButton btnSalir;

	private PanelTablaFacturasIvaVentas panelTabla;
	private JPanel panelBotones;
	private JPanel panelTotales;

	/* TOTALES RESP INSC */
	private CLJTextField txtTotalNetoGravadoRI;
	private CLJTextField txtTotalIvaRI;
	private CLJTextField txtTotalPercepcionRI;
	private CLJTextField txtTotalExentoRI;
	private CLJTextField txtTotalNoGravadoRI;
	private CLJTextField txtTotalRI;

	/* TOTALES GRAL */
	private CLJTextField txtTotalNetoGravadoGral;
	private CLJTextField txtTotalIvaGral;
	private CLJTextField txtTotalPercepcionGral;
	private CLJTextField txtTotalExentoGral;
	private CLJTextField txtTotalNoGravadoGral;
	private CLJTextField txtTotalGral;

	public JDialogReporteIVAVentasPreview(Frame padre, IVAVentasTO reporte, Date fechaDesde, Date fechaHasta) {
		super(padre);
		setReporte(reporte);
		setPeriodo(fechaDesde, fechaHasta);
		setUpComponentes();
		setUpScreen();
		setDatos();
	}

	private void setDatos() {
		llenarTabla();
		setearTotales();
	}

	private void setearTotales() {

		/* SET TOTALES RESP INSC */
		getTxtTotalNetoGravadoRI().setText((getReporte().getTotalResponsableInscripto().getTotalNetoGravado() > 0 ? getDecimalFormat().format(getReporte().getTotalResponsableInscripto().getTotalNetoGravado()) : "0.00"));
		getTxtTotalIvaRI().setText((getReporte().getTotalResponsableInscripto().getTotalIVA21() > 0 ? getDecimalFormat().format(getReporte().getTotalResponsableInscripto().getTotalIVA21()) : "0.00"));
		getTxtTotalPercepcionRI().setText((getReporte().getTotalResponsableInscripto().getTotalPercepcion() > 0 ? getDecimalFormat().format(getReporte().getTotalResponsableInscripto().getTotalPercepcion()) : "0.00"));
		getTxtTotalExentoRI().setText((getReporte().getTotalResponsableInscripto().getTotalExento() > 0 ? getDecimalFormat().format(getReporte().getTotalResponsableInscripto().getTotalExento()) : "0.00"));
		getTxtTotalNoGravadoRI().setText((getReporte().getTotalResponsableInscripto().getTotalNoGravado() > 0 ? getDecimalFormat().format(getReporte().getTotalResponsableInscripto().getTotalNoGravado()) : "0.00"));
		getTxtTotalRI().setText((getReporte().getTotalResponsableInscripto().getSumaTotalComp() > 0 ? getDecimalFormat().format(getReporte().getTotalResponsableInscripto().getSumaTotalComp()) : "0.00"));

		/* SET TOTALES GENERAL */

		getTxtTotalNetoGravadoGral().setText(getDecimalFormat().format(getReporte().getTotalGeneral().getTotalNetoGravado()));
		getTxtTotalIvaGral().setText((getReporte().getTotalGeneral().getTotalIVA21() > 0 ? getDecimalFormat().format(getReporte().getTotalGeneral().getTotalIVA21()) : "0.00"));
		getTxtTotalPercepcionGral().setText((getReporte().getTotalGeneral().getTotalPercepcion() > 0 ? getDecimalFormat().format(getReporte().getTotalGeneral().getTotalPercepcion()) : "0.00"));
		getTxtTotalExentoGral().setText((getReporte().getTotalGeneral().getTotalExento() > 0 ? getDecimalFormat().format(getReporte().getTotalGeneral().getTotalExento()) : "0.00"));
		getTxtTotalNoGravadoGral().setText((getReporte().getTotalGeneral().getTotalNoGravado() > 0 ? getDecimalFormat().format(getReporte().getTotalGeneral().getTotalNoGravado()) : "0.00"));
		getTxtTotalGral().setText(getDecimalFormat().format(getReporte().getTotalGeneral().getSumaTotalComp()));
	}

	private void llenarTabla() {
		getPanelTabla().getTabla().removeAllRows();
		for (DescripcionFacturaIVAVentasTO desc : getReporte().getFacturas()) {
			getPanelTabla().agregarElemento(desc);
		}
	}

	private void setUpScreen() {
		setTitle("Reporte iva ventas. Período: " + getPeriodo());
		setSize(GenericUtils.getDimensionPantalla());// setSize(new
		// Dimension(800, 600));
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		GuiUtil.centrar(this);
		setModal(true);
	}

	private void setUpComponentes() {
		this.add(getPanelTabla(), BorderLayout.CENTER);
		this.add(getPanelBotones(), BorderLayout.SOUTH);
	}

	public IVAVentasTO getReporte() {
		return reporte;
	}

	public void setReporte(IVAVentasTO reporte) {
		this.reporte = reporte;
	}

	private class PanelTablaFacturasIvaVentas extends PanelTablaSinBotones<DescripcionFacturaIVAVentasTO> {

		private static final long serialVersionUID = 4247457498635763788L;

		private static final int CANT_COLS_TBL_FACTS = 13;
		private static final int COL_FECHA = 0;
		private static final int COL_TIPO_CTE = 1;
		private static final int COL_NRO_CTE = 2;
		private static final int COL_RAZON_SOCIAL = 3;
		private static final int COL_CUIT = 4;
		private static final int COL_CONDICION = 5;
		private static final int COL_NETO_GRAVADO = 6;
		private static final int COL_IVA = 7;
		private static final int COL_PERCEP = 8;
		private static final int COL_EXENTO = 9;
		private static final int COL_NO_GRAV = 10;
		private static final int COL_TOTAL = 11;
		private static final int COL_OBJ = 12;

		@Override
		protected void agregarElemento(DescripcionFacturaIVAVentasTO factura) {
			Object[] row = new Object[CANT_COLS_TBL_FACTS];
			row[COL_FECHA] = factura.getFecha();
			row[COL_TIPO_CTE] = factura.getTipoCte();
			row[COL_NRO_CTE] = factura.getNroCte();
			row[COL_RAZON_SOCIAL] = factura.getRazonSocial();
			row[COL_CUIT] = factura.getCuit();
			row[COL_CONDICION] = factura.getPosIVA();
			row[COL_NETO_GRAVADO] = factura.getNetoGravado();
			row[COL_IVA] = factura.getMontoIVA21();
			row[COL_PERCEP] = factura.getPercepcion();
			row[COL_EXENTO] = factura.getExento();
			row[COL_NO_GRAV] = factura.getNoGravado();
			row[COL_TOTAL] = factura.getTotalComp();
			row[COL_OBJ] = factura;
			getTabla().addRow(row);
		}

		@Override
		protected CLJTable construirTabla() {
			CLJTable tabla = new CLJTable(0, CANT_COLS_TBL_FACTS);
			tabla.setStringColumn(COL_FECHA, "Fecha comp", 60, 60, true);
			tabla.setStringColumn(COL_TIPO_CTE, "Tipo cte", 60, 60, true);
			tabla.setStringColumn(COL_NRO_CTE, "Número cte", 120, 100, true);
			tabla.setStringColumn(COL_RAZON_SOCIAL, "Razón social", 180, 180, true);
			tabla.setStringColumn(COL_CUIT, "C.U.I.T", 120, 100, true);
			tabla.setStringColumn(COL_CONDICION, "Condición", 120, 100, true);
			tabla.setStringColumn(COL_NETO_GRAVADO, "Neto grav.", 80, 80, true);
			tabla.setStringColumn(COL_IVA, "IVA21", 80, 80, true);
			tabla.setStringColumn(COL_PERCEP, "Percep", 80, 80, true);
			tabla.setStringColumn(COL_EXENTO, "Exento", 80, 80, true);
			tabla.setStringColumn(COL_NO_GRAV, "No grav.", 80, 80, true);
			tabla.setStringColumn(COL_TOTAL, "Total comp", 80, 80, true);
			tabla.setStringColumn(COL_OBJ, "", 0, 0, true);
			tabla.setAllowHidingColumns(false);
			tabla.setAllowSorting(false);
			tabla.setReorderingAllowed(false);
			return tabla;
		}

		@Override
		protected DescripcionFacturaIVAVentasTO getElemento(int fila) {
			return (DescripcionFacturaIVAVentasTO) getTabla().getValueAt(fila, COL_OBJ);
		}

		@Override
		protected String validarElemento(int fila) {
			return null;
		}
	}

	private JButton getBtnImprimir() {
		if (btnImprimir == null) {
			btnImprimir = BossEstilos.createButton("ar/com/textillevel/imagenes/b_imp_listado.png", "ar/com/textillevel/imagenes/b_imp_listado_des.png");
			btnImprimir.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					imprimirListado();
				}
			});
		}
		return btnImprimir;
	}

	private void imprimirListado() {
		if (print == null) {
			Map<String, Object> parameters = getParametros(getReporte());
			JasperReport reporte = JasperHelper.loadReporte("/ar/com/textillevel/reportes/reporta-iva-ventas.jasper");
			setPrint(JasperHelper.fillReport(reporte, parameters, getReporte().getFacturas()));
		}
		try {
			JasperHelper.imprimirReporte(getPrint(), true, false, 1);
		} catch (JRException e1) {
			e1.printStackTrace();
			CLJOptionPane.showErrorMessage(this, "Se ha producido un error al imprimir", "Error");
		}
	}

	private Map<String, Object> getParametros(IVAVentasTO ivaVentas) {
		Map<String, Object> map = new HashMap<String, Object>();
		TotalesIVAVentasTO totalGeneral = ivaVentas.getTotalGeneral();
		TotalesIVAVentasTO totalResponsableInscripto = ivaVentas.getTotalResponsableInscripto();

		map.put("PERIODO", getPeriodo());
		map.put("NRO_IGJ", String.valueOf(GTLBeanFactory.getInstance().getBean2(ParametrosGeneralesFacadeRemote.class).getParametrosGenerales().getNroIGJ()));

		map.put("TOT_RI_NG", getDecimalFormat().format(totalResponsableInscripto.getTotalNetoGravado()));
		map.put("TOT_RI_IVA21", getDecimalFormat().format(totalResponsableInscripto.getTotalIVA21()));
		map.put("TOT_RI_PERC", (totalResponsableInscripto.getTotalPercepcion() > 0 ? getDecimalFormat().format(totalResponsableInscripto.getTotalPercepcion()) : "0.00"));
		map.put("TOT_RI_EXE", (totalResponsableInscripto.getTotalExento() > 0 ? getDecimalFormat().format(totalResponsableInscripto.getTotalExento()) : "0.00"));
		map.put("TOT_RI_NO_GRA", (totalResponsableInscripto.getTotalNoGravado() > 0 ? getDecimalFormat().format(totalResponsableInscripto.getTotalNoGravado()) : "0.00"));
		map.put("TOT_RI_TOT_COMP", getDecimalFormat().format(totalResponsableInscripto.getSumaTotalComp()));

		map.put("TOT_GEN_NG", getDecimalFormat().format(totalGeneral.getTotalNetoGravado()));
		map.put("TOT_GEN_IVA21", getDecimalFormat().format(totalGeneral.getTotalIVA21()));
		map.put("TOT_GEN_PERC", (totalGeneral.getTotalPercepcion() > 0 ? getDecimalFormat().format(totalGeneral.getTotalPercepcion()) : "0.00"));
		map.put("TOT_GEN_EXE", (totalGeneral.getTotalExento() > 0 ? getDecimalFormat().format(totalGeneral.getTotalExento()) : "0.00"));
		map.put("TOT_GEN_NO_GRA", (totalGeneral.getTotalNoGravado() > 0 ? getDecimalFormat().format(totalGeneral.getTotalNoGravado()) : "0.00"));
		map.put("TOT_GEN_TOT_COMP", getDecimalFormat().format(totalGeneral.getSumaTotalComp()));

		return map;
	}

	private DecimalFormat getDecimalFormat() {
		DecimalFormat df = new DecimalFormat("#,###.00");
		df.setMaximumFractionDigits(2);
		df.setGroupingUsed(true);
		return df;
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

	private PanelTablaFacturasIvaVentas getPanelTabla() {
		if (panelTabla == null) {
			panelTabla = new PanelTablaFacturasIvaVentas();
		}
		return panelTabla;
	}

	public String getPeriodo() {
		return periodo;
	}

	public void setPeriodo(Date fechaDesde, Date fechaHasta) {
		this.periodo = DateUtil.dateToString(fechaDesde, DateUtil.SHORT_DATE) + "  -  " + DateUtil.dateToString(fechaHasta, DateUtil.SHORT_DATE);
	}

	private JPanel getPanelBotones() {
		if (panelBotones == null) {
			panelBotones = new JPanel();
			panelBotones.setLayout(new VerticalFlowLayout(VerticalFlowLayout.CENTER, 5, 5));

			panelBotones.add(getPanelTotales());

			JPanel pnlBots = new JPanel();
			pnlBots.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 5));
			pnlBots.add(getBtnAceptar());
			pnlBots.add(getBtnSalir());
			pnlBots.add(getBtnImprimir());
			pnlBots.add(getBtnExportarAExcel());
			pnlBots.add(getBtnListadoPDF());
			panelBotones.add(pnlBots);
		}
		return panelBotones;
	}

	private JPanel getPanelTotales() {
		if (panelTotales == null) {
			panelTotales = new JPanel();
			panelTotales.setLayout(new GridLayout(3, 7));

			/* LABELS */
			panelTotales.add(new JLabel(""));
			panelTotales.add(new JLabel("Total Neto Grav.   "));
			panelTotales.add(new JLabel("Total IVA"));
			panelTotales.add(new JLabel("Total Percep.     "));
			panelTotales.add(new JLabel("Total Exento"));
			panelTotales.add(new JLabel("Total No grav.    "));
			panelTotales.add(new JLabel("Total"));

			/* TXT RESP INSC */
			panelTotales.add(new JLabel("Total Resp. Insc"));
			panelTotales.add(getTxtTotalNetoGravadoRI());
			panelTotales.add(getTxtTotalIvaRI());
			panelTotales.add(getTxtTotalPercepcionRI());
			panelTotales.add(getTxtTotalExentoRI());
			panelTotales.add(getTxtTotalNoGravadoRI());
			panelTotales.add(getTxtTotalRI());

			/* TXT GRAL */
			panelTotales.add(new JLabel("Total General"));
			panelTotales.add(getTxtTotalNetoGravadoGral());
			panelTotales.add(getTxtTotalIvaGral());
			panelTotales.add(getTxtTotalPercepcionGral());
			panelTotales.add(getTxtTotalExentoGral());
			panelTotales.add(getTxtTotalNoGravadoGral());
			panelTotales.add(getTxtTotalGral());
		}
		return panelTotales;
	}

	private CLJTextField getTxtTotalNetoGravadoRI() {
		if (txtTotalNetoGravadoRI == null) {
			txtTotalNetoGravadoRI = new CLJTextField();
			txtTotalNetoGravadoRI.setEditable(false);
		}
		return txtTotalNetoGravadoRI;
	}

	private CLJTextField getTxtTotalIvaRI() {
		if (txtTotalIvaRI == null) {
			txtTotalIvaRI = new CLJTextField();
			txtTotalIvaRI.setEditable(false);
		}
		return txtTotalIvaRI;
	}

	private CLJTextField getTxtTotalPercepcionRI() {
		if (txtTotalPercepcionRI == null) {
			txtTotalPercepcionRI = new CLJTextField();
			txtTotalPercepcionRI.setEditable(false);
		}
		return txtTotalPercepcionRI;
	}

	private CLJTextField getTxtTotalExentoRI() {
		if (txtTotalExentoRI == null) {
			txtTotalExentoRI = new CLJTextField();
			txtTotalExentoRI.setEditable(false);
		}
		return txtTotalExentoRI;
	}

	private CLJTextField getTxtTotalNoGravadoRI() {
		if (txtTotalNoGravadoRI == null) {
			txtTotalNoGravadoRI = new CLJTextField();
			txtTotalNoGravadoRI.setEditable(false);
		}
		return txtTotalNoGravadoRI;
	}

	private CLJTextField getTxtTotalRI() {
		if (txtTotalRI == null) {
			txtTotalRI = new CLJTextField();
			txtTotalRI.setEditable(false);
		}
		return txtTotalRI;
	}

	private CLJTextField getTxtTotalNetoGravadoGral() {
		if (txtTotalNetoGravadoGral == null) {
			txtTotalNetoGravadoGral = new CLJTextField();
			txtTotalNetoGravadoGral.setEditable(false);
		}
		return txtTotalNetoGravadoGral;
	}

	private CLJTextField getTxtTotalIvaGral() {
		if (txtTotalIvaGral == null) {
			txtTotalIvaGral = new CLJTextField();
			txtTotalIvaGral.setEditable(false);
		}
		return txtTotalIvaGral;
	}

	private CLJTextField getTxtTotalPercepcionGral() {
		if (txtTotalPercepcionGral == null) {
			txtTotalPercepcionGral = new CLJTextField();
			txtTotalPercepcionGral.setEditable(false);
		}
		return txtTotalPercepcionGral;
	}

	private CLJTextField getTxtTotalExentoGral() {
		if (txtTotalExentoGral == null) {
			txtTotalExentoGral = new CLJTextField();
			txtTotalExentoGral.setEditable(false);
		}
		return txtTotalExentoGral;
	}

	private CLJTextField getTxtTotalNoGravadoGral() {
		if (txtTotalNoGravadoGral == null) {
			txtTotalNoGravadoGral = new CLJTextField();
			txtTotalNoGravadoGral.setEditable(false);
		}
		return txtTotalNoGravadoGral;
	}

	private CLJTextField getTxtTotalGral() {
		if (txtTotalGral == null) {
			txtTotalGral = new CLJTextField();
			txtTotalGral.setEditable(false);
		}
		return txtTotalGral;
	}

	private JButton getBtnExportarAExcel() {
		if (btnExportarAExcel == null) {
			btnExportarAExcel = BossEstilos.createButton("ar/clarin/fwjava/imagenes/b_exportar_excel.png", "ar/clarin/fwjava/imagenes/b_exportar_excel_des.png");
			btnExportarAExcel.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if (getPanelTabla().getTabla().getRowCount() > 0) {
						CLJTable tabla = getPanelTabla().getTabla();
						mostrarFileChooser("Reporte IVA Ventas", EXTENSION_EXCEL);
						File archivoIngresado = CLFileSelector.obtenerArchivo(CLFileSelector.SAVE, CLFileSelector.FILES_ONLY, new FiltroArchivosExcel(), null);
						if (archivoIngresado != null) {
							if (!archivoIngresado.getAbsolutePath().toLowerCase().endsWith(EXTENSION_EXCEL)) {
								archivoIngresado = new File(archivoIngresado.getAbsolutePath().concat(EXTENSION_EXCEL));
							}
							new ExportadorExcel().exportarAExcel(tabla, "Reporte IVA Ventas", "Periodo: " + getPeriodo(), null, archivoIngresado.getAbsolutePath(), null, 
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

		List<TituloInfoTO> listaRI = new ArrayList<TituloInfoTO>();
		listaRI.add(new TituloInfoTO("", "Total responsable inscripto"));
		listaRI.add(new TituloInfoTO("Total Neto Gravado", getTxtTotalNetoGravadoRI().getText()));
		listaRI.add(new TituloInfoTO("Total IVA", getTxtTotalIvaRI().getText()));
		listaRI.add(new TituloInfoTO("Total percepción", getTxtTotalPercepcionRI().getText()));
		listaRI.add(new TituloInfoTO("Total exento", getTxtTotalExentoRI().getText()));
		listaRI.add(new TituloInfoTO("Total no gravado", getTxtTotalNoGravadoRI().getText()));
		listaRI.add(new TituloInfoTO("Total", getTxtTotalRI().getText()));

		List<TituloInfoTO> listaTot = new ArrayList<TituloInfoTO>();
		listaTot.add(new TituloInfoTO("", "Total responsable inscripto"));
		listaTot.add(new TituloInfoTO("Total Neto Gravado", getTxtTotalNetoGravadoGral().getText()));
		listaTot.add(new TituloInfoTO("Total IVA", getTxtTotalIvaGral().getText()));
		listaTot.add(new TituloInfoTO("Total percepción", getTxtTotalPercepcionGral().getText()));
		listaTot.add(new TituloInfoTO("Total exento", getTxtTotalExentoGral().getText()));
		listaTot.add(new TituloInfoTO("Total no gravado", getTxtTotalNoGravadoGral().getText()));
		listaTot.add(new TituloInfoTO("Total", getTxtTotalGral().getText()));

		ret.add(listaRI);
		ret.add(listaTot);

		return ret;
	}

	private JButton getBtnListadoPDF() {
		if (btnListadoPDF == null) {
			btnListadoPDF = BossEstilos.createButton("ar/com/textillevel/imagenes/btn_pdf.png", "ar/com/textillevel/imagenes/btn_pdf_des.png");
			btnListadoPDF.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if (getPanelTabla().getTabla().getRowCount() > 0) {
						mostrarFileChooser("Reporte IVA Ventas", EXTENSION_PDF);
						File archivoIngresado = CLFileSelector.obtenerArchivo(CLFileSelector.SAVE, CLFileSelector.FILES_ONLY, new FiltroArchivosPDF(), null);
						if (archivoIngresado != null) {
							if (!archivoIngresado.getAbsolutePath().toLowerCase().endsWith(EXTENSION_PDF)) {
								archivoIngresado = new File(archivoIngresado.getAbsolutePath().concat(EXTENSION_PDF));
							}
							if (print == null) {
								Map<String, Object> parameters = getParametros(getReporte());
								JasperReport reporte = JasperHelper.loadReporte("/ar/com/textillevel/reportes/reporta-iva-ventas.jasper");
								setPrint(JasperHelper.fillReport(reporte, parameters, getReporte().getFacturas()));
							}
							try {
								JasperHelper.exportarAPDF(getPrint(), archivoIngresado.getAbsolutePath());
							} catch (JRException e1) {
								e1.printStackTrace();
								CLJOptionPane.showErrorMessage(JDialogReporteIVAVentasPreview.this, "Se ha producido un error al exportar", "Error");
							}
						}
					}
				}
			});
		}
		return btnListadoPDF;
	}

	private static class FiltroArchivosExcel extends FileFilter {

		public boolean accept(File archivo) {
			return archivo.getName().endsWith(EXTENSION_EXCEL) || archivo.isDirectory();
		}

		public String getDescription() {
			return EXTENSION_EXCEL;
		}
	}

	private static class FiltroArchivosPDF extends FileFilter {

		public boolean accept(File archivo) {
			return archivo.getName().endsWith(EXTENSION_PDF) || archivo.isDirectory();
		}

		public String getDescription() {
			return EXTENSION_PDF;
		}
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
				CLJOptionPane.showErrorMessage(JDialogReporteIVAVentasPreview.this, "Se ha producido un error al guardar el archivo.\n" + e1.getMessage(), "Error");
				return;
			}
			File archivoSugerido = new File(nombreSugerido.endsWith(extension) ? nombreSugerido : nombreSugerido.concat(extension));
			CLFileSelector.setLastSelectedFile(archivoSugerido);
		}
	}

	public JasperPrint getPrint() {
		return print;
	}

	public void setPrint(JasperPrint print) {
		this.print = print;
	}

	private JButton getBtnSalir() {
		if (btnSalir == null) {
			btnSalir = new JButton("Salir");
			btnSalir.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
		}
		return btnSalir;
	}
}
