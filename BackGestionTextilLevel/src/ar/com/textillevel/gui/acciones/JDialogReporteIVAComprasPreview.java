package ar.com.textillevel.gui.acciones;

import static ar.com.textillevel.gui.util.GenericUtils.doubleToStringArg;

import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.filechooser.FileFilter;

import main.acciones.informes.IvaComprasParam;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import ar.com.fwcommon.boss.BossEstilos;
import ar.com.fwcommon.componentes.FWFileSelector;
import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTable;
import ar.com.fwcommon.componentes.VerticalFlowLayout;
import ar.com.fwcommon.util.DateUtil;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.fwcommon.util.StringUtil;
import ar.com.textillevel.entidades.documentos.factura.proveedor.ImpuestoItemProveedor;
import ar.com.textillevel.entidades.enums.ETipoImpuesto;
import ar.com.textillevel.entidades.gente.Provincia;
import ar.com.textillevel.entidades.to.ivacompras.DescripcionFacturaIVAComprasTO;
import ar.com.textillevel.entidades.to.ivacompras.DetalleImpuestoFacturaIVAComprasTO;
import ar.com.textillevel.entidades.to.ivacompras.IVAComprasTO;
import ar.com.textillevel.facade.api.remote.ImpuestoItemProveedorFacadeRemote;
import ar.com.textillevel.gui.util.ExportadorExcel;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.JasperHelper;
import ar.com.textillevel.gui.util.TituloInfoTO;
import ar.com.textillevel.util.GTLBeanFactory;

@SuppressWarnings("unused")
public class JDialogReporteIVAComprasPreview extends JDialog {

	private static final long serialVersionUID = 5060729556680752097L;

	private static final String EXTENSION_EXCEL = ".xls";
	private static final String EXTENSION_PDF = ".pdf";

	private JasperPrint print;

	private IVAComprasTO reporte;
	private String periodo;

	private JButton btnImprimir;
	private JButton btnAceptar;
	private JButton btnListadoPDF;

	private PanImpuestoFacturaProveedor panImpuestoIVACompras;
	private PanImpuestoFacturaProveedor panImpuestoIBCompras;
	private IvaComprasParam ivaComprasParam;

	private JPanel panelBotones;

	public JDialogReporteIVAComprasPreview(Frame padre, IVAComprasTO reporte, IvaComprasParam ivaComprasParam) {
		super(padre);
		this.ivaComprasParam = ivaComprasParam;
		setPeriodo(ivaComprasParam.getFechaDesde(), ivaComprasParam.getFechaHasta());
		setReporte(reporte);
		setUpScreen();
		setUpComponentes();
	}

	private void setUpScreen() {
		setTitle("Reporte iva compras. Período: " + getPeriodo());
		setSize(GenericUtils.getDimensionPantalla());
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		GuiUtil.centrar(this);
		setModal(true);
	}

	private void setUpComponentes() {
		setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.insets = new Insets(0, 5, 5, 5);
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weighty = 1;
		constraints.weightx = 1;
		constraints.gridy = 0;
		constraints.gridx = 0;
		this.add(getPanelDetalles(), constraints);
		constraints = new GridBagConstraints();
		constraints.insets = new Insets(0, 5, 5, 5);
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.gridy = 1;
		constraints.gridx = 0;
		this.add(getPanelBotones(), constraints);
	}

	private JTabbedPane getPanelDetalles() {
		JTabbedPane panelDetalles = new JTabbedPane();
		panelDetalles.addTab("IVA", getPanImpuestoIVACompras());
		panelDetalles.addTab("INGRESOS BRUTOS", getPanImpuestoIBCompras());
		return panelDetalles;
	}

	public IVAComprasTO getReporte() {
		return reporte;
	}

	public void setReporte(IVAComprasTO reporte) {
		this.reporte = reporte;
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
			setPrint(JasperHelper.fillReport(reporte, parameters, getReporte().getItems()));
		}
		try {
			JasperHelper.imprimirReporte(getPrint(), true, false, 1);
		} catch (JRException e1) {
			e1.printStackTrace();
			FWJOptionPane.showErrorMessage(this, "Se ha producido un error al imprimir", "Error");
		}
	}

	private Map<String, Object> getParametros(IVAComprasTO ivaVentas) {
		Map<String, Object> map = new HashMap<String, Object>();
		/*
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
		*/
		//TODO: Ver que onda esto!!!
		
		return map;
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

	public String getPeriodo() {
		return periodo;
	}

	public void setPeriodo(Date fechaDesde, Date fechaHasta) {
		this.periodo = DateUtil.dateToString(fechaDesde, DateUtil.SHORT_DATE) + "  -  " + DateUtil.dateToString(fechaHasta, DateUtil.SHORT_DATE);
	}

	private JPanel getPanelBotones() {
		if (panelBotones == null) {
			panelBotones = new JPanel();
			panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 5));
			panelBotones.add(getBtnAceptar());
		}
		return panelBotones;
	}

	private JButton getBtnListadoPDF() {
		if (btnListadoPDF == null) {
			btnListadoPDF = BossEstilos.createButton("ar/com/textillevel/imagenes/btn_pdf.png", "ar/com/textillevel/imagenes/btn_pdf_des.png");
			btnListadoPDF.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if (getPanImpuestoIVACompras().getTabla().getRowCount() > 0) {
 						//TODO:
//						mostrarFileChooser("Reporte IVA Ventas", EXTENSION_PDF);
						File archivoIngresado = FWFileSelector.obtenerArchivo(FWFileSelector.SAVE, FWFileSelector.FILES_ONLY, new FiltroArchivosPDF(), null);
						if (archivoIngresado != null) {
							if (!archivoIngresado.getAbsolutePath().toLowerCase().endsWith(EXTENSION_PDF)) {
								archivoIngresado = new File(archivoIngresado.getAbsolutePath().concat(EXTENSION_PDF));
							}
							if (print == null) {
								Map<String, Object> parameters = getParametros(getReporte());
								JasperReport reporte = JasperHelper.loadReporte("/ar/com/textillevel/reportes/reporta-iva-ventas.jasper");
								setPrint(JasperHelper.fillReport(reporte, parameters, getReporte().getItems()));
							}
							try {
								JasperHelper.exportarAPDF(getPrint(), archivoIngresado.getAbsolutePath());
							} catch (JRException e1) {
								e1.printStackTrace();
								FWJOptionPane.showErrorMessage(JDialogReporteIVAComprasPreview.this, "Se ha producido un error al expoertar", "Error");
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

	public JasperPrint getPrint() {
		return print;
	}

	public void setPrint(JasperPrint print) {
		this.print = print;
	}

	private static class PanImpuestoFacturaProveedor extends JPanel {

		private static final long serialVersionUID = 1L;

		private static final int COL_FECHA = 0;
		private static final int COL_TIPO_CTE = 1;
		private static final int COL_NRO_COMPROBANTE = 2;
		private static final int COL_RAZON_SOCIAL = 3;
		private static final int COL_CUIT = 4;
		private static final int COL_CONDICION = 5;
		private static final int COL_NETO_GRAVADO = 6;
		private static final int COL_EXENTO = 7;
		private static final int COL_NO_GRAVADO = 8;
		private static final int COL_PERC_IVA = 9;
		private static final int COL_TOTAL_COMPRAS = 10;

		private FWJTable tablaImpuestos;
		private Map<Integer, ImpuestoWrapper> impuestosColMap;
		private Map<Integer, ImpuestoWrapper> impuestosMap = new HashMap<Integer, ImpuestoWrapper>();
		private Map<Integer, JTextField> impuestosTxtMap = new HashMap<Integer, JTextField>();
		
		private ImpuestoItemProveedorFacadeRemote impuestoFacade;
		private ETipoImpuesto tipoImpuesto;
		private JComboBox cmbTipoImpuestoIIBB;
		private JLabel lblAplicaEn;

		private Integer colTotalImpuestos;
		
		private JButton btnExportarAExcel;
		private JPanel panelBotones;
		private JPanel panDinamicTotal;
		
		private JTextField txtTotalImpuestos;
		private JTextField txtTotalNetoGrav;
		private JTextField txtTotalGral;
		private JTextField txtTotalNetoNoGravado;
		private JTextField txtTotalPercIVA;
		
		private IvaComprasParam ivaComprasParam;

		private JScrollPane sp;

		private List<DescripcionFacturaIVAComprasTO> itemsFacturaProveedor;
		private DescripcionFacturaIVAComprasTO itemTotal;



		public PanImpuestoFacturaProveedor(ETipoImpuesto tipoImpuesto, List<DescripcionFacturaIVAComprasTO> itemsFacturaProveedor, IvaComprasParam ivaComprasParam) {
			this.tipoImpuesto = tipoImpuesto;
			this.ivaComprasParam = ivaComprasParam;
			this.itemsFacturaProveedor = itemsFacturaProveedor;
			llenarMapImpuestos();
			construct();
			llenarTablaAndTotales();
			getCmbTipoImpuestoIIBB().setVisible(false);
			getLblAplicaEn().setVisible(false);
		}

		public FWJTable getTabla() {
			return tablaImpuestos;
		}

		private JButton getBtnExportarAExcel() {
			if (btnExportarAExcel == null) {
				btnExportarAExcel = BossEstilos.createButton("ar/com/fwcommon/imagenes/b_exportar_excel.png", "ar/com/fwcommon/imagenes/b_exportar_excel_des.png");
				btnExportarAExcel.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e) {
						if (getTabla().getRowCount() > 0) {
							mostrarFileChooser(getTituloReporte(), EXTENSION_EXCEL);
							File archivoIngresado = FWFileSelector.obtenerArchivo(FWFileSelector.SAVE, FWFileSelector.FILES_ONLY, new FiltroArchivosExcel(), null);
							if (archivoIngresado != null) {
								if (!archivoIngresado.getAbsolutePath().toLowerCase().endsWith(EXTENSION_EXCEL)) {
									archivoIngresado = new File(archivoIngresado.getAbsolutePath().concat(EXTENSION_EXCEL));
								}
								new ExportadorExcel().exportarAExcel(getTabla(), getTituloReporte(), getDescrReporte(), null, archivoIngresado.getAbsolutePath(), null, System.getProperty("intercalarColoresFilas") != null && System.getProperty("intercalarColoresFilas").equals(String.valueOf(true)), getListaTotales());
							}
						}
					}


				});
			}
			return btnExportarAExcel;
		}

		private String getTituloReporte() {
			return "Reporte " + tipoImpuesto.getDescripcion() + " Compras " + (tipoImpuesto == ETipoImpuesto.INGRESOS_BRUTOS && ((ETipoAplicacionIIBB)getCmbTipoImpuestoIIBB().getSelectedItem()) != ETipoAplicacionIIBB.TODOS ? "[" + (ETipoAplicacionIIBB)getCmbTipoImpuestoIIBB().getSelectedItem() + "]" : "");
		}

		private String getDescrReporte() {
			return "Compras en el período [" + DateUtil.dateToString(ivaComprasParam.getFechaDesde()) + " - " + DateUtil.dateToString(ivaComprasParam.getFechaHasta()) + "]" + (ivaComprasParam.getProveedor() == null ? "" : " Proveedor: " + ivaComprasParam.getProveedor().getRazonSocial());
		}

		private List<List<TituloInfoTO>> getListaTotales() {
			List<List<TituloInfoTO>> ret = new ArrayList<List<TituloInfoTO>>();
			List<TituloInfoTO> listaRI = new ArrayList<TituloInfoTO>();

			//Offset de columnas para lograr matchear las columnas impuestos con sus totaless
			for(int i = 0; i < 6; i++) {
				listaRI.add(new TituloInfoTO("", ""));
			}

			//Columnas totales
			listaRI.add(new TituloInfoTO("Total Neto Gravado", getTxtTotalNetoGravado().getText()));
			listaRI.add(new TituloInfoTO("Total Exento", "0")); //TODO: Al parecer, el total exento siempre es cero. Consultar
			listaRI.add(new TituloInfoTO("Total No Gravado", getTxtTotalNetoNoGravado().getText()));
			listaRI.add(new TituloInfoTO("Total Perc. IVA", getTxtTotalPercIVA().getText()));
			listaRI.add(new TituloInfoTO("Total Compras", getTxtTotalGral().getText()));

			//lleno las columnas de impuestos
			for(Integer i : impuestosColMap.keySet()) {
				Integer idImpuesto = impuestosColMap.get(i).getId();
				JTextField txtImpuesto = impuestosTxtMap.get(idImpuesto);
				if(txtImpuesto != null) {
					DetalleImpuestoFacturaIVAComprasTO d = itemTotal.getDetalleImpuesto(idImpuesto);
					if(d != null) {
						txtImpuesto.setText(GenericUtils.getDecimalFormat().format(d.getImporte()));
						listaRI.add(new TituloInfoTO("Total " + impuestosMap.get(d.getIdImpuesto()).toString(), doubleToStringArg(d.getImporte())));
					}
				}
			}

			//total del impuesto
			listaRI.add(new TituloInfoTO("Total " + tipoImpuesto, getTxtTotalImpuestos().getText().trim()));

			ret.add(listaRI);
			return ret;
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
					FWJOptionPane.showErrorMessage(null, "Se ha producido un error al guardar el archivo.\n" + e1.getMessage(), "Error");
					return;
				}
				File archivoSugerido = new File(nombreSugerido.endsWith(extension) ? nombreSugerido : nombreSugerido.concat(extension));
				FWFileSelector.setLastSelectedFile(archivoSugerido);
			}
		}

		private void llenarTablaAndTotales() {
			itemTotal = createItemImpuestoTotal();
			getTablaImpuestos().removeAllRows();
			for(DescripcionFacturaIVAComprasTO item : itemsFacturaProveedor) {
				Object[] row = getRow(item);
				getTablaImpuestos().addRow(row);
			}

			//lleno los totales
			double totalImpuestos = 0d;
			for(Integer i : impuestosMap.keySet()) {
				JTextField txtImpuesto = impuestosTxtMap.get(i);
				if(txtImpuesto != null) {
					DetalleImpuestoFacturaIVAComprasTO d = itemTotal.getDetalleImpuesto(i);
					if(d != null) {
						txtImpuesto.setText(doubleToStringArg(d.getImporte()));
						totalImpuestos += d.getImporte();
					}
				}
			}
			getTxtTotalNetoGravado().setText(doubleToStringArg((Double.valueOf(itemTotal.getNetoGravado()))));
			getTxtTotalImpuestos().setText(doubleToStringArg(totalImpuestos));
			getTxtTotalNetoNoGravado().setText(doubleToStringArg(Double.valueOf(itemTotal.getNetoNoGravado())));
			getTxtTotalPercIVA().setText(doubleToStringArg(Double.valueOf(itemTotal.getPercIVA())));
			getTxtTotalGral().setText(doubleToStringArg(Double.valueOf(itemTotal.getTotalComp())));
		}

		private DescripcionFacturaIVAComprasTO createItemImpuestoTotal() {
			itemTotal = new DescripcionFacturaIVAComprasTO();
			for(Integer i : impuestosMap.keySet()) {
				DetalleImpuestoFacturaIVAComprasTO detalleImpuesto = new DetalleImpuestoFacturaIVAComprasTO(i, 0d);
				itemTotal.getDetalleImpuestoList().add(detalleImpuesto);
			}
			return itemTotal;
		}

		private Object[] getRow(DescripcionFacturaIVAComprasTO item) {
			Object[] row = new Object[getTotalColumnas()];
			row[COL_FECHA] = DateUtil.dateToString(item.getFecha());
			row[COL_TIPO_CTE] = item.getTipoComprobante();
			row[COL_NRO_COMPROBANTE] = item.getNroComprobante();
			row[COL_RAZON_SOCIAL] = item.getRazonSocial();
			row[COL_CUIT] = item.getCuit();
			row[COL_CONDICION] = item.getPosIVA();
			
			row[COL_NETO_GRAVADO] = doubleToStringArg(internalToDouble(item.getNetoGravado()));
			itemTotal.setNetoGravado(calcularTotal(itemTotal.getNetoGravado(), item.getNetoGravado()));

			row[COL_EXENTO] = doubleToStringArg(internalToDouble(item.getExento()));
			row[COL_NO_GRAVADO] = doubleToStringArg(internalToDouble(item.getNetoNoGravado()));
			itemTotal.setNetoNoGravado(calcularTotal(itemTotal.getNetoNoGravado(), item.getNetoNoGravado()));

			row[COL_PERC_IVA] = doubleToStringArg(internalToDouble(item.getPercIVA()));
			itemTotal.setPercIVA(calcularTotal(itemTotal.getPercIVA(), item.getPercIVA()));

			row[COL_TOTAL_COMPRAS] = doubleToStringArg(internalToDouble(item.getTotalComp()));
			itemTotal.setTotalComp(calcularTotal(itemTotal.getTotalComp(), item.getTotalComp()));

			BigDecimal totalImpuestos = new BigDecimal(0);

			Map<Integer, Double> mapTotalByImpuestoWrapper = new HashMap<Integer, Double>();
			for(Integer key : getImpuestosColMap().keySet()) {
				ImpuestoWrapper impuestoWrapper = getImpuestosColMap().get(key);
				double valorImpuesto = getValorImpuesto(item, impuestoWrapper);
				Double valor = mapTotalByImpuestoWrapper.get(key);
				if(valor != null) {
					valor += valorImpuesto; 
				}
				mapTotalByImpuestoWrapper.put(key, valorImpuesto);
				totalImpuestos = totalImpuestos.add(new BigDecimal(valorImpuesto));
				//Incremento el item donde llevo el totla
				DetalleImpuestoFacturaIVAComprasTO detalleImpuesto = itemTotal.getDetalleImpuesto(impuestoWrapper.getId());
				if(detalleImpuesto != null) {
					detalleImpuesto.setImporte(detalleImpuesto.getImporte() + valorImpuesto);
				}
			}
			for(Integer key : getImpuestosColMap().keySet()) {
				row[key] = doubleToStringArg(mapTotalByImpuestoWrapper.get(key));
			}

			row[colTotalImpuestos] = doubleToStringArg(totalImpuestos.doubleValue());
			return row;
		}

		private String calcularTotal(String actualValueStr, String valueSumarStr) {
			double valueSumar = Double.valueOf(valueSumarStr);
			double actValue = StringUtil.isNullOrEmpty(actualValueStr) ? 0d : Double.valueOf(actualValueStr);
			return String.valueOf(valueSumar + actValue);
		}

		private Double internalToDouble(String v) {
			if(v == null) {
				return null;
			} else {
				return Double.valueOf(v);
			}
		}

		private double getValorImpuesto(DescripcionFacturaIVAComprasTO item, ImpuestoWrapper impuestoWrapper) {
			double importeTotal = 0d; 
			for(DetalleImpuestoFacturaIVAComprasTO detalle : item.getDetalleImpuestoList()) {
				if(impuestoWrapper.containsImpuesto(detalle.getIdImpuesto())) {
					importeTotal += detalle.getImporte();
				}
			}
			return importeTotal;
		}

		private void construct() {
			setLayout(new GridBagLayout());
			sp = new JScrollPane(getTablaImpuestos());
			sp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			
			GridBagConstraints constraints = new GridBagConstraints();
			constraints.insets = new Insets(0, 5, 5, 5);
			constraints.fill = GridBagConstraints.NONE;
			constraints.gridy = 0;
			constraints.gridx = 0;
			add(getLblAplicaEn(), constraints);
			
			constraints = new GridBagConstraints();
			constraints.insets = new Insets(0, 5, 5, 5);
			constraints.fill = GridBagConstraints.NONE;
			constraints.anchor = GridBagConstraints.WEST;
			constraints.gridy = 0;
			constraints.gridx = 1;
			add(getCmbTipoImpuestoIIBB(), constraints);
			
			constraints = new GridBagConstraints();
			constraints.insets = new Insets(0, 5, 5, 5);
			constraints.fill = GridBagConstraints.BOTH;
			constraints.weighty = 1;
			constraints.weightx = 1;
			constraints.gridy = 1;
			constraints.gridx = 0;
			constraints.gridwidth = 2;
			add(sp, constraints);

			
			constraints = new GridBagConstraints();
			constraints.insets = new Insets(0, 5, 5, 5);
			constraints.fill = GridBagConstraints.BOTH;
			constraints.weightx = 1;
			constraints.gridy = 2;
			constraints.gridx = 0;
			constraints.gridwidth = 2;

			add(getDinamicPanTotal(), constraints);
			
			constraints = new GridBagConstraints();
			constraints.insets = new Insets(0, 5, 5, 5);
			constraints.fill = GridBagConstraints.BOTH;
			constraints.gridy = 3;
			constraints.gridx = 0;
			constraints.gridwidth = 2;			
			add(getPanelBotones(), constraints);
		}

		private JPanel getDinamicPanTotal() {
			if(panDinamicTotal == null) {
				panDinamicTotal = new JPanel();
				panDinamicTotal.setLayout(new GridLayout(2,impuestosMap.keySet().size() + 3));
				for(Integer i : impuestosMap.keySet()) {
					panDinamicTotal.add(new JLabel(impuestosMap.get(i).toString()));
				}
				panDinamicTotal.add(new JLabel("Total " + tipoImpuesto));
				panDinamicTotal.add(new JLabel("Total Neto Gravado"));
				panDinamicTotal.add(new JLabel("Total Neto No Gravado"));
				panDinamicTotal.add(new JLabel("Total Perc. IVA"));
				panDinamicTotal.add(new JLabel("Total General"));

				for(Integer i : impuestosMap.keySet()) {
					JTextField txt = new JTextField();
					txt.setEditable(false);
					panDinamicTotal.add(txt);
					impuestosTxtMap.put(i, txt);
				}

				panDinamicTotal.add(getTxtTotalImpuestos());
				panDinamicTotal.add(getTxtTotalNetoGravado());
				panDinamicTotal.add(getTxtTotalNetoNoGravado());
				panDinamicTotal.add(getTxtTotalPercIVA());
				panDinamicTotal.add(getTxtTotalGral());
				panDinamicTotal.setBorder(BorderFactory.createTitledBorder("TOTALES"));
			}
			return panDinamicTotal;
		}

		public JTextField getTxtTotalImpuestos() {
			if(txtTotalImpuestos == null) {
				txtTotalImpuestos = new JTextField();
				txtTotalImpuestos.setEditable(false);
			}
			return txtTotalImpuestos;
		}

		public JTextField getTxtTotalNetoGravado() {
			if(txtTotalNetoGrav == null) {
				txtTotalNetoGrav = new JTextField();
				txtTotalNetoGrav.setEditable(false);
			}
			return txtTotalNetoGrav;
		}

		public JTextField getTxtTotalNetoNoGravado() {
			if(txtTotalNetoNoGravado == null) {
				txtTotalNetoNoGravado = new JTextField();
				txtTotalNetoNoGravado.setEditable(false);
			}
			return txtTotalNetoNoGravado;
		}

		public JTextField getTxtTotalPercIVA() {
			if(txtTotalPercIVA == null) {
				txtTotalPercIVA = new JTextField();
				txtTotalPercIVA.setEditable(false);
			}
			return txtTotalPercIVA;
		}

		public JTextField getTxtTotalGral() {
			if(txtTotalGral == null) {
				txtTotalGral = new JTextField();
				txtTotalGral.setEditable(false);
			}
			return txtTotalGral;
		}
		
		
		private JLabel getLblAplicaEn() {
			if(lblAplicaEn == null) {
				lblAplicaEn = new JLabel("Aplica en: ");
			}
			return lblAplicaEn;
		}

		private JPanel getPanelBotones() {
			if (panelBotones == null) {
				panelBotones = new JPanel();
				panelBotones.setLayout(new VerticalFlowLayout(VerticalFlowLayout.CENTER, 5, 5));
				JPanel pnlBots = new JPanel();
				pnlBots.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 5));
				pnlBots.add(getBtnExportarAExcel());
				panelBotones.add(pnlBots);
			}
			return panelBotones;
		}

		
		private void llenarMapImpuestos() {
			List<ImpuestoItemProveedor> allImpuestos = getImpuestoFacade().getAllOrderByName();
			Collections.sort(allImpuestos, new Comparator<ImpuestoItemProveedor>() {

				public int compare(ImpuestoItemProveedor o1,ImpuestoItemProveedor o2) {
					return Double.valueOf(o1.getPorcDescuento()).compareTo(Double.valueOf(o2.getPorcDescuento()));
				}

			});
			
			int i = 1;
			for(ImpuestoItemProveedor iip : allImpuestos) {
				if(iip.getTipoImpuesto() == tipoImpuesto) {
					if(tipoImpuesto == ETipoImpuesto.INGRESOS_BRUTOS) {
						Provincia provincia = iip.getProvincia();
						ImpuestoWrapper impuestoWrapper = impuestosMap.get(provincia.getId());
						if(impuestoWrapper == null) {
							impuestoWrapper = new ImpuestoWrapper(provincia, iip);
							impuestosMap.put(impuestoWrapper.getId(), impuestoWrapper);
							getImpuestosColMap().put(COL_TOTAL_COMPRAS + i, impuestoWrapper);
							i++;
						} else {
							impuestoWrapper.addImpuestoItem(iip);
						}
					} else {
						ImpuestoWrapper impuestoWrapper = new ImpuestoWrapper(iip);
						impuestosMap.put(impuestoWrapper.getId(), impuestoWrapper);
						getImpuestosColMap().put(COL_TOTAL_COMPRAS + i, impuestoWrapper);
						i++;
					}
				}
			}
		}

		private Map<Integer, ImpuestoWrapper> getImpuestosColMap() {
			if(impuestosColMap == null) {
				impuestosColMap = new LinkedHashMap<Integer, ImpuestoWrapper>();
			}
			return impuestosColMap;
		}

		private FWJTable getTablaImpuestos() {
			if(tablaImpuestos == null) {
				rebuildTabla();
			}
			return tablaImpuestos;
		}

		private void rebuildTabla() {
			tablaImpuestos = new FWJTable(0, getTotalColumnas());
			tablaImpuestos.setStringColumn(COL_FECHA, "Fecha comp", 60, 60, true);
			tablaImpuestos.setStringColumn(COL_TIPO_CTE, "Tipo Comprobante", 100, 100, true);
			tablaImpuestos.setStringColumn(COL_NRO_COMPROBANTE, "Nro. Comprobante", 120, 100, true);
			tablaImpuestos.setStringColumn(COL_RAZON_SOCIAL, "Razón social", 180, 180, true);
			tablaImpuestos.setStringColumn(COL_CUIT, "C.U.I.T", 120, 100, true);
			tablaImpuestos.setStringColumn(COL_CONDICION, "Condición", 120, 100, true);
			tablaImpuestos.setStringColumn(COL_NETO_GRAVADO, "Neto gravado", 80, 80, true);
			tablaImpuestos.setStringColumn(COL_EXENTO, "Exento", 80, 80, true);
			tablaImpuestos.setStringColumn(COL_NO_GRAVADO, "No gravado", 80, 80, true);
			tablaImpuestos.setStringColumn(COL_PERC_IVA, "Perc. IVA", 80, 80, true);
			tablaImpuestos.setStringColumn(COL_TOTAL_COMPRAS, "Total Compras", 80, 80, true);

			for(Integer k : getImpuestosColMap().keySet()) {
				tablaImpuestos.setStringColumn(k, getImpuestosColMap().get(k).toString(), 80, 80, true);
			}

			colTotalImpuestos = COL_TOTAL_COMPRAS + getImpuestosColMap().keySet().size() + 1;
			tablaImpuestos.setStringColumn(colTotalImpuestos, "Total " + tipoImpuesto.getDescripcion(), 80, 80, true);

			tablaImpuestos.setAllowHidingColumns(false);
			tablaImpuestos.setAllowSorting(false);
			tablaImpuestos.setReorderingAllowed(false);
		}

		private int getTotalColumnas() {
			return COL_TOTAL_COMPRAS + getImpuestosColMap().keySet().size() + 2;
		}

		private ImpuestoItemProveedorFacadeRemote getImpuestoFacade() {
			if(impuestoFacade == null) {
				impuestoFacade = GTLBeanFactory.getInstance().getBean2(ImpuestoItemProveedorFacadeRemote.class);
			}
			return impuestoFacade;
		}

		private JComboBox getCmbTipoImpuestoIIBB() {
			if(cmbTipoImpuestoIIBB == null) {
				cmbTipoImpuestoIIBB = new JComboBox();
				GuiUtil.llenarCombo(cmbTipoImpuestoIIBB, Arrays.asList(ETipoAplicacionIIBB.values()), true);
//				cmbTipoImpuestoIIBB.addItemListener(new ItemListener() {
//
//					public void itemStateChanged(ItemEvent e) {
//						if(GuiUtil.isControlEventoEnabled(e) && e.getStateChange() == ItemEvent.SELECTED) {
//							getImpuestosColMap().clear();
//							llenarMapImpuestos();
//							rebuildTabla();
//							if(sp != null) {
//								sp.setViewportView(getTablaImpuestos());
//								llenarTablaAndTotales();
//							}
//						}
//					}
//
//				});
				
				cmbTipoImpuestoIIBB.setSelectedItem(ETipoAplicacionIIBB.TODOS);
			}
			return cmbTipoImpuestoIIBB;
		}

		private enum ETipoAplicacionIIBB {

			PROVINCIA,
			CAPITAL,
			TODOS;

		}
		
	}

	private PanImpuestoFacturaProveedor getPanImpuestoIVACompras() {
		if(panImpuestoIVACompras == null) {
			panImpuestoIVACompras = new PanImpuestoFacturaProveedor(ETipoImpuesto.IVA, reporte.getItems(), ivaComprasParam);
		}
		return panImpuestoIVACompras;
	}
	
	private PanImpuestoFacturaProveedor getPanImpuestoIBCompras() {
		if(panImpuestoIBCompras == null) {
			panImpuestoIBCompras = new PanImpuestoFacturaProveedor(ETipoImpuesto.INGRESOS_BRUTOS, reporte.getItems(), ivaComprasParam);
		}
		return panImpuestoIBCompras;
	}

	private static class ImpuestoWrapper {

		private Provincia provincia;
		private Set<ImpuestoItemProveedor> impuestoSet = new HashSet<ImpuestoItemProveedor>();
		private ImpuestoItemProveedor impuestoItem;

		public ImpuestoWrapper(Provincia provincia, ImpuestoItemProveedor impuestoItem) {
			this.provincia = provincia;
			impuestoSet.add(impuestoItem);
		}

		public boolean containsImpuesto(Integer idImpuesto) {
			if(provincia == null) {
				return impuestoItem.getId().equals(idImpuesto);
			} else {
				for(ImpuestoItemProveedor iip : impuestoSet) {
					if(iip.getId().equals(idImpuesto)) {
						return true;
					}
				}
				return false;
			}
		}

		public ImpuestoWrapper(ImpuestoItemProveedor impuestoItem) {
			this.impuestoItem = impuestoItem;
		}
		
		public void addImpuestoItem(ImpuestoItemProveedor impuestoItem) {
			impuestoSet.add(impuestoItem);
		}
		
		public Integer getId() {
			if(impuestoItem == null) {
				return provincia.getId();
			} else {
				return impuestoItem.getId();
			}
		}
		
		public String toString() {
			if(impuestoItem == null) {
				return provincia.getNombre();
			} else {
				return impuestoItem.toString();
			}
		}

	}
	
}