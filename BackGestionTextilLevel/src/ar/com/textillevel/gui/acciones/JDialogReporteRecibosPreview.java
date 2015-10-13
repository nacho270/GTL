package ar.com.textillevel.gui.acciones;

import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.filechooser.FileFilter;

import net.sf.jasperreports.engine.JasperPrint;
import ar.com.fwcommon.boss.BossEstilos;
import ar.com.fwcommon.componentes.FWFileSelector;
import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTable;
import ar.com.fwcommon.componentes.VerticalFlowLayout;
import ar.com.fwcommon.util.DateUtil;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.entidades.documentos.recibo.Recibo;
import ar.com.textillevel.entidades.documentos.recibo.to.ResumenReciboTO;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.to.ivacompras.IVAComprasTO;
import ar.com.textillevel.facade.api.remote.ReciboFacadeRemote;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.util.GTLBeanFactory;

@SuppressWarnings("unused")
public class JDialogReporteRecibosPreview extends JDialog {

	private static final long serialVersionUID = 5060729556680752097L;

	private static final String EXTENSION_EXCEL = ".xls";
	private static final String EXTENSION_PDF = ".pdf";

	private JasperPrint print;

	private List<ResumenReciboTO> resumenReciboList;
	private Cliente cliente;

	private String periodo;

	private JButton btnImprimir;
	private JButton btnAceptar;
	private JButton btnListadoPDF;
	private JButton btnSalir;

	private PanRecibos panRecibos;

	private JPanel panelBotones;
	private Frame frame;


	public JDialogReporteRecibosPreview(Frame frame, List<ResumenReciboTO> resumenReciboList, Cliente cliente, Date fechaDesde, Date fechaHasta) {
		super(frame);
		this.frame = frame;
		this.resumenReciboList = resumenReciboList;
		this.cliente = cliente;
		setPeriodo(fechaDesde, fechaHasta);
		setUpScreen();
		setUpComponentes();
	}

	private void setUpScreen() {
		setTitle("Reporte Recibos. Período: " + getPeriodo());
		setSize(GenericUtils.getDimensionPantalla());
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		GuiUtil.centrar(this);
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
		constraints.gridy = 1;
		constraints.gridx = 0;
		this.add(getPanelBotones(), constraints);
	}

	private JTabbedPane getPanelDetalles() {
		JTabbedPane panelDetalles = new JTabbedPane();
		panelDetalles.addTab("RECIBOS", getPanRecibos());
		return panelDetalles;
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
		this.periodo = DateUtil.dateToString(fechaDesde, DateUtil.SHORT_DATE) + "  -  " + DateUtil.dateToString(fechaHasta, DateUtil.SHORT_DATE) + (cliente == null ? "" : " CLIENTE: " + cliente.getRazonSocial());
	}

	private JPanel getPanelBotones() {
		if (panelBotones == null) {
			panelBotones = new JPanel();
			panelBotones.setLayout(new VerticalFlowLayout(VerticalFlowLayout.CENTER, 5, 5));

			JPanel pnlBots = new JPanel();
			pnlBots.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 5));
			pnlBots.add(getBtnAceptar());
			pnlBots.add(getBtnSalir());
//			pnlBots.add(getBtnImprimir());
//			pnlBots.add(getBtnListadoPDF());
			
			panelBotones.add(pnlBots);
		}
		return panelBotones;
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


	private static class PanRecibos extends JPanel {

		private static final long serialVersionUID = 1L;

		private static final int CANT_COLS = 9;
		
		private static final int COL_DESCR_RECIBO = 0;
		private static final int COL_TOT_CHEQUES = 1;
		private static final int COL_TOT_NC = 2;
		private static final int COL_TOT_TX = 3;
		private static final int COL_TOT_IVA = 4;
		private static final int COL_TOT_IIBB = 5;
		private static final int COL_TOT_EFEC = 6;
		private static final int COL_TOTAL = 7;
		private static final int COL_ID = 8;

		private FWJTable tablaImpuestos;

		private JButton btnExportarAExcel;
		private JPanel panelBotones;
		private String periodo;
		
		private JScrollPane sp;

		private List<ResumenReciboTO> resumenReciboList;
		private Frame frame;

		public PanRecibos(Frame frame, List<ResumenReciboTO> resumenReciboList, Cliente cliente, String periodo) {
			this.resumenReciboList = resumenReciboList;
			this.periodo = periodo;
			construct();
			llenarTabla();
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
								GenericUtils.exportarAExcel(getTabla(), getTituloReporte(), getDescrReporte(), null, archivoIngresado.getAbsolutePath(), System.getProperty("intercalarColoresFilas") != null && System.getProperty("intercalarColoresFilas").equals(String.valueOf(true)));
							}
						}
					}


				});
			}
			return btnExportarAExcel;
		}

		private String getTituloReporte() {
			return periodo;
		}

		private String getDescrReporte() {
			return periodo;
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

		private void llenarTabla() {
			getTablaImpuestos().removeAllRows();
			NumberFormat decimalFormat = GenericUtils.getDecimalFormat();
			for(ResumenReciboTO item : resumenReciboList) {
				Object[] row = new Object[CANT_COLS];
				row[COL_DESCR_RECIBO] = "Nro.: " + item.getNroRecibo() + " - Fecha: "  + DateUtil.dateToString(item.getFecha());
				row[COL_TOT_CHEQUES] = decimalFormat.format(item.getTotalCheques());
				row[COL_TOT_NC] = decimalFormat.format(item.getTotalNC());
				row[COL_TOT_TX] = decimalFormat.format(item.getTotalTransfBancarias());
				row[COL_TOT_IVA] = decimalFormat.format(item.getTotalIVA());
				row[COL_TOT_IIBB] = decimalFormat.format(item.getTotalIIBB());
				row[COL_TOT_EFEC] = decimalFormat.format(item.getTotalEfectivo());
				row[COL_TOTAL] = decimalFormat.format(item.getTotal());
				row[COL_ID] = item.getIdRecibo();
				getTablaImpuestos().addRow(row);
			}
		}

		private void construct() {
			setLayout(new GridBagLayout());
			sp = new JScrollPane(getTablaImpuestos());
			sp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			
			GridBagConstraints constraints = new GridBagConstraints();
			constraints.insets = new Insets(0, 5, 5, 5);
			constraints.fill = GridBagConstraints.BOTH;
			constraints.weighty = 1;
			constraints.weightx = 1;
			constraints.gridy = 0;
			constraints.gridx = 0;
			constraints.gridwidth = 2;
			add(sp, constraints);

			constraints = new GridBagConstraints();
			constraints.insets = new Insets(0, 5, 5, 5);
			constraints.fill = GridBagConstraints.BOTH;
			constraints.gridy = 1;
			constraints.gridx = 0;
			constraints.gridwidth = 2;			
			add(getPanelBotones(), constraints);
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


		private FWJTable getTablaImpuestos() {
			if(tablaImpuestos == null) {
				rebuildTabla();
			}
			return tablaImpuestos;
		}

		private void rebuildTabla() {
			tablaImpuestos = new FWJTable(0, CANT_COLS);
			tablaImpuestos.setStringColumn(COL_DESCR_RECIBO, "RECIBO", 170, 170, true);
			tablaImpuestos.setStringColumn(COL_TOT_CHEQUES, "TOTAL CHEQUES", 90, 90, true);
			tablaImpuestos.setStringColumn(COL_TOT_NC, "TOTAL NOTAS DE CREDITO", 150, 150, true);
			tablaImpuestos.setStringColumn(COL_TOT_TX, "TOTAL TRANSF. BANCARIA", 180, 180, true);
			tablaImpuestos.setStringColumn(COL_TOT_IVA, "TOTAL IVA", 120, 100, true);
			tablaImpuestos.setStringColumn(COL_TOT_IIBB, "TOTAL IIBB", 120, 100, true);
			tablaImpuestos.setStringColumn(COL_TOT_EFEC, "TOTAL EFECTIVO", 110, 110, true);
			tablaImpuestos.setStringColumn(COL_TOTAL, "TOTAL", 90, 90, true);
			tablaImpuestos.setStringColumn(COL_ID, "", 0, 0, true);

			tablaImpuestos.setAllowHidingColumns(false);
			tablaImpuestos.setAllowSorting(false);
			tablaImpuestos.setReorderingAllowed(false);

			tablaImpuestos.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseReleased(MouseEvent e) {
					if(getTabla().getSelectedRow() > -1 && e.getClickCount() == 2) {
						Integer idReciboSelected = (Integer)getTablaImpuestos().getValueAt(getTabla().getSelectedRow(), COL_ID);
						ReciboFacadeRemote rfr = GTLBeanFactory.getInstance().getBean2(ReciboFacadeRemote.class);
						Recibo recibo = rfr.getByIdEager(idReciboSelected);
						JDialogCargaRecibo dialogCargaRecibo = new JDialogCargaRecibo(frame,recibo , true);
						GuiUtil.centrar(dialogCargaRecibo);
						dialogCargaRecibo.setVisible(true);
					}
				}
			});

		}

	}

	private PanRecibos getPanRecibos() {
		if(panRecibos == null) {
			panRecibos = new PanRecibos(frame, resumenReciboList, cliente, periodo);
		}
		return panRecibos;
	}

}