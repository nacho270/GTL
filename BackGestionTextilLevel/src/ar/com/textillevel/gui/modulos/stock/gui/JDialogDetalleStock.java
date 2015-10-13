package ar.com.textillevel.gui.modulos.stock.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileFilter;

import ar.com.fwcommon.boss.BossEstilos;
import ar.com.fwcommon.componentes.FWFileSelector;
import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTable;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.entidades.enums.ETipoMateriaPrima;
import ar.com.textillevel.entidades.gente.Proveedor;
import ar.com.textillevel.entidades.ventas.materiaprima.MateriaPrima;
import ar.com.textillevel.entidades.ventas.materiaprima.PrecioMateriaPrima;
import ar.com.textillevel.entidades.ventas.materiaprima.Tela;
import ar.com.textillevel.entidades.ventas.materiaprima.anilina.Anilina;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.JasperHelper;

public class JDialogDetalleStock extends JDialog {

	private static final long serialVersionUID = 181866811877726161L;

	private final List<PrecioMateriaPrima> preciosMateriaPrima;

	private static final String EXTENSION_EXCEL = ".xls";
	private static final String EXTENSION_PDF = ".pdf";

	private FWJTable tablaPreciosMateriaPrima;

	private JButton btnExportarAExcel;
	private JButton btnImprimirListado;
	private JButton btnListadoPDF;
	private JButton btnAceptar;
	private JButton btnConsultarMovimientos;

	private JComboBox cmbProveedor;

	private JPanel panelSur;
	private JPanel panelNorte;

	private final Frame padre;

	private ETipoMateriaPrima tipoMateriaPrima;

	private static final int CANT_COL = 6;
	private static final int CANT_COL_ANILINA = 8;
	private static final int CANT_COL_TELA = 8;

	// Columnas comunes
	private static final int COL_DESCR = 0;
	private static final int COL_PROV = 1;
	private static final int COL_STOCK = 2;
	private static final int COL_UNIDAD = 3;
	private static final int COL_OBJ = 4;
	private static final int COL_CONCENTRACION = 5;

	// columnas anilina
	private static final int COL_COLOR_INDEX = 6;
	private static final int COL_TIPO_ANILINA = 7;

	// columnas tela
	private static final int COL_ARTICULO = 6;
	private static final int COL_GRAMAJE = 7;

	private DecimalFormat df;

	public JDialogDetalleStock(Frame padre, List<PrecioMateriaPrima> preciosMateriaPrima) {
		super(padre);
		this.padre = padre;
		this.preciosMateriaPrima = preciosMateriaPrima;
		setUpComponentes();
		setUpScreen();
		llenarTabla(getPreciosMateriaPrima());
	}

	private void llenarTabla(List<PrecioMateriaPrima> precios) {
		if (precios != null && !precios.isEmpty()) {
			getBtnExportarAExcel().setEnabled(true);
			getBtnListadoPDF().setEnabled(true);
			getBtnImprimirListado().setEnabled(true);
		} else {
			getBtnExportarAExcel().setEnabled(false);
			getBtnListadoPDF().setEnabled(false);
			getBtnImprimirListado().setEnabled(false);
		}
		getTablaPreciosMateriaPrima().removeAllRows();
		for (PrecioMateriaPrima pmp : precios) {
			agregarElemento(pmp);
		}
	}

	private void agregarElemento(PrecioMateriaPrima elemento) {
		MateriaPrima materiaPrima = elemento.getMateriaPrima();
		Object[] row = new Object[getTipoMateriaPrima() == ETipoMateriaPrima.ANILINA ? CANT_COL_ANILINA : getTipoMateriaPrima() == ETipoMateriaPrima.TELA ? CANT_COL_TELA : CANT_COL];
		row[COL_DESCR] = materiaPrima.getDescripcion() + " - " + elemento.getAlias();
		row[COL_PROV] = elemento.getPreciosProveedor().getProveedor().getNombreCorto();
		row[COL_STOCK] = (elemento.getStockActual().doubleValue() == 0 ? "0.00" : getDecimalFormat().format(elemento.getStockActual().doubleValue()));
		row[COL_UNIDAD] = materiaPrima.getUnidad().getDescripcion();
		row[COL_OBJ] = elemento;

		if (getTipoMateriaPrima() == ETipoMateriaPrima.QUIMICO || getTipoMateriaPrima() == ETipoMateriaPrima.ANILINA) {
			row[COL_CONCENTRACION] = materiaPrima.getConcentracion();
		}
		if (getTipoMateriaPrima() == ETipoMateriaPrima.ANILINA) {
			row[COL_COLOR_INDEX] = ((Anilina) materiaPrima).getColorIndex();
			row[COL_TIPO_ANILINA] = ((Anilina) materiaPrima).getTipoAnilina().getDescripcion();
		} else if (getTipoMateriaPrima() == ETipoMateriaPrima.TELA) {
			row[COL_ARTICULO] = ((Tela) materiaPrima).getArticulo().getDescripcion();
			row[COL_GRAMAJE] = ((Tela) materiaPrima).getArticulo().getGramaje();
		}

		getTablaPreciosMateriaPrima().addRow(row);
	}

	private DecimalFormat getDecimalFormat() {
		if (df == null) {
			df = new DecimalFormat("#,###.00");
			df.setMaximumFractionDigits(2);
			df.setGroupingUsed(true);
		}
		return df;
	}

	private void setUpScreen() {
		setTitle("Detalle de Stock materias primas de proveedores");
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setModal(true);
		setSize(new Dimension(1024, 480));
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
		add(getPanelNorte(), BorderLayout.NORTH);
		JScrollPane jsp = new JScrollPane(getTablaPreciosMateriaPrima(), JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		add(jsp, BorderLayout.CENTER);
		add(getPanelSur(), BorderLayout.SOUTH);
	}

	private void salir() {
		if (FWJOptionPane.showQuestionMessage(this, "Está seguro que desea salir?", "Pregunta") == FWJOptionPane.YES_OPTION) {
			dispose();
		}
	}

	private void verMovimientos() {
		JDialogVerMovimientosStock jdvms = new JDialogVerMovimientosStock(padre, (PrecioMateriaPrima) getTablaPreciosMateriaPrima().getValueAt(getTablaPreciosMateriaPrima().getSelectedRow(), COL_OBJ));
		jdvms.setVisible(true);
	}

	private JButton getBtnExportarAExcel() {
		if (btnExportarAExcel == null) {
			btnExportarAExcel = BossEstilos.createButton("ar/com/fwcommon/imagenes/b_exportar_excel.png", "ar/com/fwcommon/imagenes/b_exportar_excel_des.png");
			btnExportarAExcel.setEnabled(false);
			btnExportarAExcel.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if (getTablaPreciosMateriaPrima().getRowCount() > 0) {
						FWJTable tabla = getTablaPreciosMateriaPrima();
						mostrarFileChooser("Listado de Stock materias primas de proveedores", EXTENSION_EXCEL);
						File archivoIngresado = FWFileSelector.obtenerArchivo(FWFileSelector.SAVE, FWFileSelector.FILES_ONLY, new FiltroArchivosExcel(), null);
						if (archivoIngresado != null) {
							if (!archivoIngresado.getAbsolutePath().toLowerCase().endsWith(EXTENSION_EXCEL)) {
								archivoIngresado = new File(archivoIngresado.getAbsolutePath().concat(EXTENSION_EXCEL));
							}
							GenericUtils.exportarAExcel(tabla, "Listado de Stock materias primas de proveedores", "", null, archivoIngresado.getAbsolutePath(), System
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
					JasperHelper.imprimirListado(getTablaPreciosMateriaPrima(), "Listado de Stock materias primas de proveedores", "", null, false);
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
					if (getTablaPreciosMateriaPrima().getRowCount() > 0) {
						FWJTable tabla = getTablaPreciosMateriaPrima();
						mostrarFileChooser("Listado de Stock materias primas de proveedores", EXTENSION_PDF);
						File archivoIngresado = FWFileSelector.obtenerArchivo(FWFileSelector.SAVE, FWFileSelector.FILES_ONLY, new FiltroArchivosPDF(), null);
						if (archivoIngresado != null) {
							if (!archivoIngresado.getAbsolutePath().toLowerCase().endsWith(EXTENSION_PDF)) {
								archivoIngresado = new File(archivoIngresado.getAbsolutePath().concat(EXTENSION_PDF));
							}
							JasperHelper.listadoAPDF(tabla, "Listado de Stock materias primas de proveedores", "", null, false, archivoIngresado.getAbsolutePath());
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
				FWJOptionPane.showErrorMessage(JDialogDetalleStock.this, "Se ha producido un error al guardar el archivo.\n" + e1.getMessage(), "Error");
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

	public List<PrecioMateriaPrima> getPreciosMateriaPrima() {
		return preciosMateriaPrima;
	}

	private FWJTable getTablaPreciosMateriaPrima() {
		if (tablaPreciosMateriaPrima == null) {
			setTipoMateriaPrima(getPreciosMateriaPrima().get(0).getMateriaPrima().getTipo());
			tablaPreciosMateriaPrima = new FWJTable(0, getTipoMateriaPrima() == ETipoMateriaPrima.ANILINA ? CANT_COL_ANILINA : getTipoMateriaPrima() == ETipoMateriaPrima.TELA ? CANT_COL_TELA: CANT_COL);
			llenarColumnas(tablaPreciosMateriaPrima);
			tablaPreciosMateriaPrima.setReorderingAllowed(false);
			tablaPreciosMateriaPrima.setAllowHidingColumns(false);
			tablaPreciosMateriaPrima.setAllowSorting(false);
			tablaPreciosMateriaPrima.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseReleased(MouseEvent e) {
					if (getTablaPreciosMateriaPrima().getSelectedRow() > -1) {
						if (e.getClickCount() == 2) {
							verMovimientos();
						} else if (e.getClickCount() == 1) {
							getBtnConsultarMovimientos().setEnabled(true);
							getBtnExportarAExcel().setEnabled(true);
							getBtnImprimirListado().setEnabled(true);
							getBtnListadoPDF().setEnabled(true);
						}
					} else {
						getBtnConsultarMovimientos().setEnabled(false);
						getBtnExportarAExcel().setEnabled(false);
						getBtnImprimirListado().setEnabled(false);
						getBtnListadoPDF().setEnabled(false);
					}
				}
			});
		}
		return tablaPreciosMateriaPrima;
	}

	private void llenarColumnas(FWJTable tabla) {
		tabla.setStringColumn(COL_DESCR, "Descripción", 200, 200, true);
		tabla.setStringColumn(COL_PROV, "Proveedor", 150, 150, true);
		tabla.setFloatColumn(COL_STOCK, "Stock actual", 100, true);
		tabla.setStringColumn(COL_UNIDAD, "Unidad", 60, 60, true);
		tabla.setStringColumn(COL_OBJ, "", 0, 0, true);
		if (getTipoMateriaPrima() == ETipoMateriaPrima.QUIMICO || getTipoMateriaPrima() == ETipoMateriaPrima.ANILINA) {
			tabla.setFloatColumn(COL_CONCENTRACION, "Concentración", 80, true);
		}
		if (getTipoMateriaPrima() == ETipoMateriaPrima.ANILINA) {
			tabla.setIntColumn(COL_COLOR_INDEX, "Color Index", 80, true);
			tabla.setStringColumn(COL_TIPO_ANILINA, "Tipo anilina", 120, 120, true);
		} else if (getTipoMateriaPrima() == ETipoMateriaPrima.TELA) {
			tabla.setStringColumn(COL_ARTICULO, "Artículo", 120, 120, true);
			tabla.setIntColumn(COL_GRAMAJE, "Gramaje", 80, true);
		}
	}

	private JButton getBtnAceptar() {
		if (btnAceptar == null) {
			btnAceptar = new JButton("Salir");
			btnAceptar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					salir();
				}
			});
		}
		return btnAceptar;
	}

	private JButton getBtnConsultarMovimientos() {
		if (btnConsultarMovimientos == null) {
			btnConsultarMovimientos = BossEstilos.createButton("ar/com/textillevel/imagenes/b_verificar_stock.png", "ar/com/textillevel/imagenes/b_verificar_stock_des.png");
			btnConsultarMovimientos.setEnabled(false);
			btnConsultarMovimientos.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					verMovimientos();
				}
			});
		}
		return btnConsultarMovimientos;
	}

	private JPanel getPanelSur() {
		if (panelSur == null) {
			panelSur = new JPanel();
			panelSur.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			panelSur.add(getBtnImprimirListado());
			panelSur.add(getBtnExportarAExcel());
			panelSur.add(getBtnListadoPDF());
			panelSur.add(getBtnConsultarMovimientos());
			panelSur.add(getBtnAceptar());
		}
		return panelSur;
	}

	public static void main(String[] args) {
		new JDialogDetalleStock(null, null).setVisible(true);
		System.exit(1);

	}

	private JPanel getPanelNorte() {
		if (panelNorte == null) {
			panelNorte = new JPanel();
			panelNorte.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			panelNorte.add(new JLabel("Proveedor: "));
			panelNorte.add(getCmbProveedor());
		}
		return panelNorte;
	}

	private JComboBox getCmbProveedor() {
		if (cmbProveedor == null) {
			cmbProveedor = new JComboBox();
			List<Proveedor> proveedores = new ArrayList<Proveedor>(); //GTLBeanFactory.getInstance().getBean2(ProveedorFacadeRemote.class).getAllProveedoresOrderByName();
			for(PrecioMateriaPrima pmp : getPreciosMateriaPrima()){
				if(!proveedores.contains(pmp.getPreciosProveedor().getProveedor())){
					proveedores.add(pmp.getPreciosProveedor().getProveedor());
				}
			}
			//TODO: hacer lista de checkbox
			cmbProveedor.addItem("TODOS");
			for(Proveedor p : proveedores){
				cmbProveedor.addItem(p);
			}
			cmbProveedor.addItemListener(new ItemListener() {

				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						if(getCmbProveedor().getSelectedItem().equals("TODOS")){
							llenarTabla(getPreciosMateriaPrima());
							return;
						}
						Proveedor proveedor = (Proveedor) cmbProveedor.getSelectedItem();
						List<PrecioMateriaPrima> listaNueva = new ArrayList<PrecioMateriaPrima>();
						for (PrecioMateriaPrima p : getPreciosMateriaPrima()) {
							if (p.getPreciosProveedor().getProveedor().equals(proveedor)) {
								listaNueva.add(p);
							}
						}
						llenarTabla(listaNueva);
					}
				}
			});
		}
		return cmbProveedor;
	}

	public ETipoMateriaPrima getTipoMateriaPrima() {
		return tipoMateriaPrima;
	}

	public void setTipoMateriaPrima(ETipoMateriaPrima tipoMateriaPrima) {
		this.tipoMateriaPrima = tipoMateriaPrima;
	}
}
