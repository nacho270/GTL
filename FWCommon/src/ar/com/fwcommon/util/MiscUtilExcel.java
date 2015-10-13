package ar.com.fwcommon.util;

import java.awt.Font;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.table.TableColumn;

import org.apache.commons.lang.exception.NestableException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.contrib.HSSFRegionUtil;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;

import ar.com.fwcommon.IndicadorProgreso;
import ar.com.fwcommon.componentes.FWFileSelector;
import ar.com.fwcommon.componentes.FWJTable;
import ar.com.fwcommon.componentes.FWJTable.DateColumn;

public class MiscUtilExcel {

	private static MiscUtilExcel instance = new MiscUtilExcel();

	private MiscUtilExcel() {
	}

	public static MiscUtilExcel getInstance() {
		return instance;
	}

	private List<HSSFCellStyle> estilos;
	private static final int LONG_MAX_NOMBRE_HOJA = 31;
	private static final String NOMBRE_HOJA_DEFAULT = "Hoja 1";
	private static final String TEXTO_TRUNCADO = "...";

	/**
	 * Llama a
	 * {@link #exportarAExcel(FWJTable, String, String, String, String, IndicadorProgreso, boolean)},
	 * sin filtros
	 * 
	 * @param tabla
	 * @param titulo
	 * @param subtitulo
	 * @param absolutePath
	 * @param indicador
	 * @param intercalar
	 */
	public void exportarAExcel(FWJTable tabla, String titulo, String subtitulo, String absolutePath, IndicadorProgreso indicador, boolean intercalar) {
		exportarAExcel(tabla, titulo, subtitulo, null, absolutePath, indicador, intercalar);
	}

	/**
	 * Exporta una <b>CLJTable</b> a un archivo <b>Excel</b>.
	 * 
	 * @param tabla
	 *            La CLJTable a exportar.
	 * @param subtitulo
	 *            El nombre de la hoja a la cual se va exportar la CLJTable. Si
	 *            su longitud es mayor a 31 caracteres se trunca. No puede
	 *            contener ninguno de los siguientes caracteres: /\*?[]. Si no
	 *            se especifica se le asigna un nombre predeterminado.
	 * @param ruta
	 *            La ruta completa (incluyendo el nombre del archivo .xls) donde
	 *            se va a guardar el archivo.
	 */
	public void exportarAExcel(FWJTable tabla, String titulo, String subtitulo, String filtros, String ruta, IndicadorProgreso indicador, boolean intercalar) {
		HSSFWorkbook libro = new HSSFWorkbook();
		/*
		 * if(subtitulo == null || subtitulo.trim().length() == 0) { subtitulo =
		 * NOMBRE_HOJA_DEFAULT; } if(subtitulo.trim().length() >
		 * LONG_MAX_NOMBRE_HOJA) { subtitulo = subtitulo.trim().substring(0,
		 * LONG_MAX_NOMBRE_HOJA - TEXTO_TRUNCADO.length() -
		 * 1).concat(TEXTO_TRUNCADO); }
		 */
		generarHoja(libro, tabla, titulo, subtitulo, filtros, indicador, intercalar);
		guardarLibro(libro, ruta);
	}

	public void exportarAExcelDos(FWJTable tabla, FWJTable tabla2, String titulo, String titulo2, String subtitulo, String subtitulo2, String ruta,
			IndicadorProgreso indicador, boolean intercalar) {
		HSSFWorkbook libro = new HSSFWorkbook();
		/*
		 * if(subtitulo == null || subtitulo.trim().length() == 0) { subtitulo =
		 * NOMBRE_HOJA_DEFAULT; } if(subtitulo.trim().length() >
		 * LONG_MAX_NOMBRE_HOJA) { subtitulo = subtitulo.trim().substring(0,
		 * LONG_MAX_NOMBRE_HOJA - TEXTO_TRUNCADO.length() -
		 * 1).concat(TEXTO_TRUNCADO); }
		 */
		generarHoja(libro, titulo, tabla, titulo, subtitulo, indicador, intercalar);
		generarHoja(libro, titulo2, tabla2, titulo2, subtitulo2, indicador, intercalar);
		guardarLibro(libro, ruta);
	}

	/**
	 * Exporta una lista de <b>CLJTable</b> a un archivo <b>Excel</b>, cada
	 * tabla a una hoja distinta.
	 * 
	 * @param tablas
	 *            La lista de <b>CLJTable</b> a exportar.
	 * @param nombresHojas
	 *            El nombre de la hoja a la cual se va exportar la CLJTable. Si
	 *            su longitud es mayor a 31 caracteres se trunca. No puede
	 *            contener ninguno de los siguientes caracteres: /\*?[]. Si no
	 *            se especifica se le asigna un nombre predeterminado.
	 * @param ruta
	 *            La ruta completa (incluyendo el nombre del archivo .xls) donde
	 *            se va a guardar el archivo.
	 */
	public void exportarAExcel(List<FWJTable> tablas, List<String> titulos, List<String> nombresHojas, List<String> filtros, String ruta,
			IndicadorProgreso indicador, boolean intercalar) {
		HSSFWorkbook libro = new HSSFWorkbook();
		for(int i = 0; i < tablas.size(); i++) {
			FWJTable tabla = tablas.get(i);
			if(tabla != null) {
				String nombreHoja = nombresHojas != null ? nombresHojas.get(i) : null;
				if(nombreHoja == null || nombreHoja.trim().length() == 0) {
					nombreHoja = NOMBRE_HOJA_DEFAULT + i;
				}
				if(nombreHoja.trim().length() > LONG_MAX_NOMBRE_HOJA) {
					nombreHoja = nombreHoja.trim().substring(0, LONG_MAX_NOMBRE_HOJA - TEXTO_TRUNCADO.length() - 1).concat(TEXTO_TRUNCADO);
				}
				generarHoja(libro, tablas.get(i), titulos.get(i), filtros.get(i), nombreHoja, indicador, intercalar);
			}
		}
		guardarLibro(libro, ruta);
	}

	private void generarHoja(HSSFWorkbook libro, String nombreHoja, FWJTable tabla, String titulo, String subtitulo, IndicadorProgreso indicador,
			boolean intercalar) {
		generarHoja(nombreHoja, libro, tabla, titulo, subtitulo, null, indicador, intercalar);
	}

	private void generarHoja(HSSFWorkbook libro, FWJTable tabla, String titulo, String subtitulo, String filtros, IndicadorProgreso indicador,
			boolean intercalar) {
		generarHoja(NOMBRE_HOJA_DEFAULT, libro, tabla, titulo, subtitulo, filtros, indicador, intercalar);
	}

	private static final String EXTENSION_EXCEL = ".xls";

	public static void setearSugerenciaNombreArchivoExcel(String nombre) {
		File directorioCorriente = FWFileSelector.getLastSelectedFile();
		if(directorioCorriente != null) {
			if(directorioCorriente.isFile())
				directorioCorriente = directorioCorriente.getParentFile();
			String nombreSugerido = null;
			try {
				nombreSugerido = directorioCorriente.getCanonicalPath() + File.separator + nombre;
			} catch(IOException e1) {
				e1.printStackTrace();
			}
			File archivoSugerido = new File(nombreSugerido.concat(EXTENSION_EXCEL));
			FWFileSelector.setLastSelectedFile(archivoSugerido);
		}
	}

	private void generarHoja(String nombreHoja, HSSFWorkbook libro, FWJTable tabla, String titulo, String subtitulo, String filtros,
			IndicadorProgreso indicador, boolean intercalar) {
		if(nombreHoja == null) {
			nombreHoja = NOMBRE_HOJA_DEFAULT;
		}
		ArrayList<FuenteCelda> fuentesCeldas = new ArrayList<FuenteCelda>();
		int cantColumnasReal = 0;
		int cantColumnasTabla = tabla.getColumnCount();
		int cantFilas = tabla.getRowCount();
		short contColumnas = 0;
		short[] anchosColumnas;
		HSSFCell celda;
		HSSFRow fila;
		HSSFSheet hoja = libro.createSheet(nombreHoja);
		hoja.setMargin(HSSFSheet.LeftMargin, 0.5d);
		hoja.setMargin(HSSFSheet.TopMargin, 0.5d);
		hoja.setMargin(HSSFSheet.RightMargin, 0.5d);
		hoja.setMargin(HSSFSheet.BottomMargin, 0.5d);
		// Configuracion de la impresora
		HSSFPrintSetup configImpresion = hoja.getPrintSetup();
		configImpresion.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);
		configImpresion.setLandscape(true);
		hoja.setAutobreaks(true);
		configImpresion.setFitWidth((short)1);
		configImpresion.setFitHeight((short)0);
		estilos = new ArrayList<HSSFCellStyle>();
		for(int i = 0; i < cantColumnasTabla; i++) {
			if(tabla.getColumnModel().getColumn(i).getWidth() != 0) {
				cantColumnasReal++;
			}
		}
		anchosColumnas = new short[cantColumnasReal];
		Font fuenteHeader = tabla.getTableHeader().getFont();
		HSSFFont fuenteHeaderExcel = libro.createFont();
		fuenteHeaderExcel.setFontName(fuenteHeader.getFontName());
		fuenteHeaderExcel.setBoldweight(fuenteHeader.isBold() ? HSSFFont.BOLDWEIGHT_BOLD : HSSFFont.BOLDWEIGHT_NORMAL);
		fuenteHeaderExcel.setItalic(fuenteHeader.isItalic());
		fuenteHeaderExcel.setColor(new HSSFColor.WHITE().getIndex());
		Font fuenteTabla = tabla.getFont();
		HSSFFont fuenteTablaExcel = libro.createFont();
		fuenteTablaExcel.setFontName(fuenteTabla.getFontName());
		fuenteTablaExcel.setBoldweight(fuenteTabla.isBold() ? HSSFFont.BOLDWEIGHT_BOLD : HSSFFont.BOLDWEIGHT_NORMAL);
		fuenteTablaExcel.setItalic(fuenteTabla.isItalic());
		HSSFFont fuenteTitulo = libro.createFont();
		fuenteTitulo.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		fuenteTitulo.setColor(new HSSFColor.WHITE().getIndex());
		// Genero la celda para el titulo
		HSSFCellStyle cellStyleTitulo = libro.createCellStyle();
		cellStyleTitulo.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		cellStyleTitulo.setFillForegroundColor(new HSSFColor.PLUM().getIndex());
		cellStyleTitulo.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		cellStyleTitulo.setBorderTop(HSSFCellStyle.BORDER_THIN);
		cellStyleTitulo.setBorderRight(HSSFCellStyle.BORDER_THIN);
		cellStyleTitulo.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		cellStyleTitulo.setFont(fuenteTitulo);
		cellStyleTitulo.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		fila = hoja.createRow(0);
		celda = fila.createCell((short)0);
		celda.setCellStyle(cellStyleTitulo);
		celda.setCellValue(titulo);
		// Genero la celda para el subtitulo
		HSSFFont fuenteSubtitulo = libro.createFont();
		fuenteTitulo.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		HSSFCellStyle cellStyleSubtitulo = libro.createCellStyle();
		cellStyleSubtitulo.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		cellStyleSubtitulo.setBorderTop(HSSFCellStyle.BORDER_THIN);
		cellStyleSubtitulo.setBorderRight(HSSFCellStyle.BORDER_THIN);
		cellStyleSubtitulo.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		cellStyleSubtitulo.setFont(fuenteSubtitulo);
		cellStyleSubtitulo.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		fila = hoja.createRow(1);
		celda = fila.createCell((short)0);
		celda.setCellStyle(cellStyleSubtitulo);
		celda.setCellValue(subtitulo);
		// Genera la celda para los filtros
		HSSFFont fuenteFiltros = libro.createFont();
		fuenteTitulo.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		HSSFCellStyle cellStyleFiltros = libro.createCellStyle();
		cellStyleFiltros.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		cellStyleFiltros.setBorderTop(HSSFCellStyle.BORDER_THIN);
		cellStyleFiltros.setBorderRight(HSSFCellStyle.BORDER_THIN);
		cellStyleFiltros.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		cellStyleFiltros.setFont(fuenteFiltros);
		cellStyleFiltros.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		fila = hoja.createRow(2);
		celda = fila.createCell((short)0);
		celda.setCellStyle(cellStyleFiltros);
		celda.setCellValue(filtros);
		// Genera la cabecera con los campos de la tabla
		fila = hoja.createRow(4);
		for(int i = 0; i < cantColumnasTabla; i++) {
			TableColumn columna = tabla.getColumnModel().getColumn(i);
			if(columna.getWidth() != 0) {
				String nombreColumna = columna.getIdentifier().toString();
				HSSFCellStyle cellStyleHeader = libro.createCellStyle();
				cellStyleHeader.setFont(fuenteHeaderExcel);
				cellStyleHeader.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
				cellStyleHeader.setFillForegroundColor(new HSSFColor.PLUM().getIndex());
				cellStyleHeader.setBorderLeft(HSSFCellStyle.BORDER_THIN);
				cellStyleHeader.setBorderRight(HSSFCellStyle.BORDER_THIN);
				cellStyleHeader.setBorderTop(HSSFCellStyle.BORDER_THIN);
				cellStyleHeader.setBorderBottom(HSSFCellStyle.BORDER_THIN);
				celda = fila.createCell(contColumnas);
				celda.setCellStyle(cellStyleHeader);
				celda.setCellValue(nombreColumna);
				anchosColumnas[contColumnas] = (short)nombreColumna.length();
				contColumnas++;
			}
		}
		if(indicador != null) {
			indicador.setReady(0, cantFilas * cantColumnasTabla);
		}
		// Genero la tabla con los datos
		for(int i = 0; i < cantFilas; i++) {
			contColumnas = 0;
			fila = hoja.createRow(i + 5);
			for(short j = 0; j < cantColumnasTabla; j++) {
				if(tabla.getColumnModel().getColumn(j).getWidth() != 0) {
					Object valorCelda = tabla.getValueForExcelAt(i, j);
					celda = fila.createCell(contColumnas);
					if(valorCelda != null) {
						String stringValorCelda = valorCelda.toString();
						short anchoColumnaTemp = (short)stringValorCelda.length();
						if(anchoColumnaTemp > anchosColumnas[contColumnas]) {
							anchosColumnas[contColumnas] = anchoColumnaTemp;
						}
						if(valorCelda instanceof Boolean) {
							//celda.setCellValue(((valorCelda == null && (Boolean)valorCelda ? "NO" : "SI")));
							if((Boolean)valorCelda.equals(true)){
								celda.setCellValue("SI");
							}else
							celda.setCellValue("NO");
						} else if(valorCelda instanceof Date) {
							celda.setCellValue(HSSFDateUtil.getExcelDate((Date)valorCelda));
						} else if(valorCelda instanceof Timestamp) {
							Date d = new Date(((Timestamp)valorCelda).getTime());
							celda.setCellValue(HSSFDateUtil.getExcelDate(d));
						} else if(valorCelda instanceof Number) {
							celda.setCellValue(((Number)valorCelda).doubleValue());
						} else {
							celda.setCellValue(stringValorCelda);
						}
					}
					celda.setCellStyle(getCellStyle(libro, fuentesCeldas, tabla, i, j, intercalar));
					contColumnas++;
				}
				if(indicador != null) {
					indicador.setValorActual(indicador.getValorActual() + 1);
				}
			}
		}
		if(indicador != null) {
			indicador.setFinished(true);
		}
		// SETEAR LOS ANCHOS
		for(short i = 0; i < cantColumnasReal; i++) {
			hoja.setColumnWidth(i, (short)((anchosColumnas[i] * 256) + (256 * 2)));
		}
		// AGRUPAR Y DELIMITAR (BORDES) DE TITULO, SUBTITULO.... (Agrupar para
		// que tengan un ancho de aprox 50)
		short colFin = calcularColumnaLimiteAgruparEncabezado(cantColumnasReal, anchosColumnas);
		delimitarAgruparEncabezados(libro, hoja, colFin);
		//
		//
		List<FWJTable.GrupoCeldas> gruposCeldas = tabla.getGruposCeldas();
		for(Iterator<FWJTable.GrupoCeldas> i = gruposCeldas.iterator(); i.hasNext();) {
			FWJTable.GrupoCeldas grupoCeldas = i.next();
			Region region = new Region(grupoCeldas.getFila() + 5, (short)grupoCeldas.getCol(), grupoCeldas.getFila() + grupoCeldas.getAlto() + 4,
					(short)(grupoCeldas.getCol() + grupoCeldas.getAncho() - 1));
			hoja.addMergedRegion(region);
		}
		// al imprimir se deben repetir en cada pagina impresa: A1:A3
		libro.setRepeatingRowsAndColumns(libro.getSheetIndex(nombreHoja), -1, -1, 0, 3);
		// Area de impresion de esta hoja.
		libro.setPrintArea(libro.getSheetIndex(nombreHoja), 0, cantColumnasReal - 1, 0, cantFilas + 4);
		MiscUtil.sleep(1000);
	}

	/**
	 * @param cantColumnasReal
	 * @param anchosColumnas
	 * @return Una cantidad de columnas que es la que mas se acerca al ancho
	 *         <b>50</b>. Si todas las columnas no llegan a 50 =>
	 *         (cantColumnasReal-1);
	 */
	private short calcularColumnaLimiteAgruparEncabezado(int cantColumnasReal, short[] anchosColumnas) {
		short colFin = (short)(cantColumnasReal - 1);
		int anchoColumnas = 0;
		for(short i = 0; i < cantColumnasReal; i++) {
			anchoColumnas += anchosColumnas[i];
			if(anchoColumnas >= 50) {
				int dif = anchoColumnas - 50;
				int difAnterior = anchoColumnas - anchosColumnas[i] - 50;
				if(i > 0 && difAnterior < dif) {
					colFin = (short)(i - 1);
					break;
				} else {
					colFin = i;
					break;
				}
			}
		}
		return colFin;
	}

	/**
	 * Agrupa y pone bordes a las primeras 3 rows (titulo, subtitulo, filtros)
	 * 
	 * @param libro
	 * @param hoja
	 * @param colFin
	 */
	private void delimitarAgruparEncabezados(HSSFWorkbook libro, HSSFSheet hoja, short colFin) {
		Region regionTitulo = new Region((short)0, (short)0, (short)0, colFin);
		Region regionSubtitulo = new Region((short)1, (short)0, (short)1, colFin);
		Region regionFiltros = new Region((short)2, (short)0, (short)2, colFin);
		hoja.addMergedRegion(regionTitulo);
		hoja.addMergedRegion(regionSubtitulo);
		hoja.addMergedRegion(regionFiltros);
		try {
			// Bordear Titulo
			HSSFRegionUtil.setBorderBottom(HSSFCellStyle.BORDER_THIN, regionTitulo, hoja, libro);
			HSSFRegionUtil.setBorderTop(HSSFCellStyle.BORDER_THIN, regionTitulo, hoja, libro);
			HSSFRegionUtil.setBorderLeft(HSSFCellStyle.BORDER_THIN, regionTitulo, hoja, libro);
			HSSFRegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN, regionTitulo, hoja, libro);
			// Bordear Subtitulos
			HSSFRegionUtil.setBorderBottom(HSSFCellStyle.BORDER_THIN, regionSubtitulo, hoja, libro);
			HSSFRegionUtil.setBorderTop(HSSFCellStyle.BORDER_THIN, regionSubtitulo, hoja, libro);
			HSSFRegionUtil.setBorderLeft(HSSFCellStyle.BORDER_THIN, regionSubtitulo, hoja, libro);
			HSSFRegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN, regionSubtitulo, hoja, libro);
			// Bordear los filtros
			HSSFRegionUtil.setBorderBottom(HSSFCellStyle.BORDER_THIN, regionFiltros, hoja, libro);
			HSSFRegionUtil.setBorderTop(HSSFCellStyle.BORDER_THIN, regionFiltros, hoja, libro);
			HSSFRegionUtil.setBorderLeft(HSSFCellStyle.BORDER_THIN, regionFiltros, hoja, libro);
			HSSFRegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN, regionFiltros, hoja, libro);
		} catch(NestableException e) {
			e.printStackTrace();
		}
	}

	private void guardarLibro(HSSFWorkbook libro, String ruta) {
		try {
			FileOutputStream fileOut = new FileOutputStream(ruta);
			libro.write(fileOut);
			fileOut.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Retorna la constante que representa la alineación de una celda.
	 * 
	 * @param tabla
	 * @param fila
	 * @param col
	 * @return
	 */
	private short getAlineacionCelda(FWJTable tabla, int fila, int col) {
		int alineacionCelda = tabla.getAlignmentCell(fila, col);
		if(alineacionCelda == FWJTable.CENTER_ALIGN) {
			return HSSFCellStyle.ALIGN_CENTER;
		} else if(alineacionCelda == FWJTable.RIGHT_ALIGN) {
			return HSSFCellStyle.ALIGN_RIGHT;
		}
		return HSSFCellStyle.ALIGN_LEFT;
	}

	/**
	 * Clase que retorna el estilo de una celda si existe, de lo contrario, lo
	 * crea.
	 * 
	 * @param libro
	 * @param tabla
	 * @param fila
	 * @param col
	 * @return cellStyle
	 */
	private HSSFCellStyle getCellStyle(HSSFWorkbook libro, ArrayList<FuenteCelda> fuentesCeldas, FWJTable tabla, int fila, int col, boolean intercalar) {
		Font fuenteCelda = tabla.getFontCell(fila, col);
		short color = (fila % 2 == 0 && !tabla.isCeldaGrupo(fila, col) && intercalar) ? new HSSFColor.GREY_25_PERCENT().getIndex() : new HSSFColor.WHITE()
				.getIndex();
		short formatoDato = getFormatoDato(libro, tabla, fila, col);
		for(Iterator<HSSFCellStyle> i = estilos.iterator(); i.hasNext();) {
			HSSFCellStyle cellStyle = i.next();
			if(cellStyle.getAlignment() == getAlineacionCelda(tabla, fila, col) && cellStyle.getWrapText() == tabla.isMultilineColumn(col)
					&& cellStyle.getDataFormat() == formatoDato && cellStyle.getFillForegroundColor() == color) {
				HSSFFont fuenteCellStyle = libro.getFontAt(cellStyle.getFontIndex());
				String nombreFuente;
				if(fuenteCelda.getFontName().indexOf(".") != -1) {
					nombreFuente = fuenteCelda.getFontName().substring(0, fuenteCelda.getFontName().indexOf("."));
				} else {
					nombreFuente = fuenteCelda.getFontName();
				}
				if(nombreFuente.compareTo(fuenteCellStyle.getFontName()) == 0
						&& fuenteCelda.isBold() == (fuenteCellStyle.getBoldweight() == HSSFFont.BOLDWEIGHT_BOLD)
						&& fuenteCelda.isItalic() == fuenteCellStyle.getItalic()) {
					return cellStyle;
				}
			}
		}
		HSSFCellStyle cellStyle = libro.createCellStyle();
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		if(!tabla.isCeldaGrupo(fila, col) && fila % 2 == 0 && intercalar) {
			cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStyle.setFillForegroundColor(new HSSFColor.GREY_25_PERCENT().getIndex());
		} else {
			cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStyle.setFillForegroundColor(new HSSFColor.WHITE().getIndex());
		}
		cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderLeft(HSSFCellStyle.BORDER_DASHED);
		cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderRight(HSSFCellStyle.BORDER_DASHED);
		cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderTop(HSSFCellStyle.BORDER_DASHED);
		cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderBottom(HSSFCellStyle.BORDER_DASHED);
		cellStyle.setAlignment(getAlineacionCelda(tabla, fila, col));
		cellStyle.setWrapText(tabla.isMultilineColumn(col));
		cellStyle.setDataFormat(formatoDato);
		cellStyle.setFont(getHSSFFont(libro, fuentesCeldas, fuenteCelda.getName(), fuenteCelda.isBold(), fuenteCelda.isItalic()));
		estilos.add(cellStyle);
		return cellStyle;
	}

	private String getFormatoFechaCompatibleExcel(String formatoFechaTabla) {
		String formatoFechaCompatibleExcel = StringUtil.replaceAll(formatoFechaTabla, "EEEE", "[$-2C0A]dddd");
		formatoFechaCompatibleExcel = StringUtil.replaceAll(formatoFechaCompatibleExcel, "E", "[$-2C0A]ddd");
		return formatoFechaCompatibleExcel;
	}

	private short getFormatoDato(HSSFWorkbook libro, FWJTable tabla, int fila, int col) {
		if(tabla.getMask(fila, col) != null) {
			return libro.createDataFormat().getFormat(getFormatoFechaCompatibleExcel(tabla.getMask(fila, col)));
		} else if(tabla.getTipoColumna(col).getMask() != null) {
			return libro.createDataFormat().getFormat(getFormatoFechaCompatibleExcel(tabla.getTipoColumna(col).getMask()));
		}
		if(tabla.getValueAt(fila, col) instanceof Date || tabla.getTipoColumna(col) instanceof DateColumn) {
			return libro.createDataFormat().getFormat(getFormatoFechaCompatibleExcel(tabla.getDateMask()));
		} else if(tabla.getValueAt(fila, col) instanceof Timestamp) {
			return libro.createDataFormat().getFormat(tabla.getTimeMask());
		}
		return 0;
	}

	/**
	 * Recorre las instancias ya creadas de FuenteCelda y las compara con los
	 * atributos de la tipografía recibidos como parámetro. Si existe alguna que
	 * coincide, retorna la instancia de HSSFFont que esta contiene. Caso
	 * contrario, agrega una nueva instancia de FuenteCelda a la colección.
	 * 
	 * @param libro
	 * @param fuentesCeldas
	 * @param nombre
	 * @param negrita
	 * @param cursiva
	 * @return La instancia de la clase HSSFFont correspondiente a los atributos
	 *         de la tipografía recibidos como parámetro.
	 */
	private HSSFFont getHSSFFont(HSSFWorkbook libro, ArrayList<FuenteCelda> fuentesCeldas, String nombre, boolean negrita, boolean cursiva) {
		for(FuenteCelda fuenteCelda : fuentesCeldas) {
			if(fuenteCelda.comparar(nombre, negrita, cursiva)) {
				return fuenteCelda.getHSSFFont();
			}
		}
		FuenteCelda fuenteCelda = new FuenteCelda(libro, nombre, negrita, cursiva);
		fuentesCeldas.add(fuenteCelda);
		return fuenteCelda.getHSSFFont();
	}

	/**
	 * Clase que mantiene la relación entre los atributos de una tipografía y
	 * una objeto HSSFFont.
	 */
	static class FuenteCelda {

		private String nombre;
		private boolean negrita;
		private boolean cursiva;
		private HSSFFont hssfFont;

		public FuenteCelda(HSSFWorkbook libro, String nombre, boolean negrita, boolean cursiva) {
			this.nombre = nombre;
			this.negrita = negrita;
			this.cursiva = cursiva;
			generarHSSFFont(libro);
		}

		private void generarHSSFFont(HSSFWorkbook libro) {
			hssfFont = libro.createFont();
			hssfFont.setFontName(nombre);
			hssfFont.setBoldweight(negrita ? HSSFFont.BOLDWEIGHT_BOLD : HSSFFont.BOLDWEIGHT_NORMAL);
			hssfFont.setItalic(cursiva);
		}

		public HSSFFont getHSSFFont() {
			return hssfFont;
		}

		public boolean comparar(String nombre, boolean negrita, boolean cursiva) {
			return this.nombre.compareTo(nombre) == 0 && this.negrita == negrita && this.cursiva == cursiva;
		}
	}
}
