package ar.com.fwcommon.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.List;

import ar.com.fwcommon.IndicadorProgreso;
import ar.com.fwcommon.componentes.FWJTable;
import ar.com.fwcommon.componentes.error.FWException;

public class MiscUtil {


	
	
	public static int TRUETYPE_FONT = Font.TRUETYPE_FONT;
	public static int TYPE1_FONT = Font.TYPE1_FONT;
	public static int PLAIN_FONT_STYLE = Font.PLAIN;
	public static int BOLD_FONT_STYLE = Font.BOLD;
	public static int ITALIC_FONT_STYLE = Font.ITALIC;

	/**
	 * Construye un color a partir de un String en formato hexadecimal
	 * (0x000000).
	 * 
	 * @param s
	 *            El String a convertir.
	 * @return El color generado a partir del String.
	 */
	public static Color stringToColor(String s) {
		return new Color(Integer.decode(s).intValue());
	}

	/**
	 * Devuelve la <b>versión</b> del JDK instalado.
	 * 
	 * @return La versión del JDK instalado.
	 */
	public static String getJDKVersion() {
		return System.getProperty("java.version");
	}

	/**
	 * Devuelve <b>true</b> si el SO es MacOS
	 * 
	 * @return Si el SO es Mac OS.
	 */
	public static boolean isMacOS() {
		return getOSName().toLowerCase().startsWith("mac os");
	}

	/**
	 * Devuelve <b>true</b> si el SO es Windows 2000.
	 * 
	 * @return Si el SO es Windows 2000.
	 */
	public static boolean isWindows2K() {
		return getOSName().toLowerCase().startsWith("windows 2000");
	}

	/**
	 * Devuelve <b>true</b> si el SO es Windows XP.
	 * 
	 * @return Si el SO es Windows XP.
	 */
	public static boolean isWindowsXP() {
		return getOSName().toLowerCase().startsWith("windows xp");
	}

	/**
	 * Devuelve la <b>descripción</b> del Sist. Operativo.
	 * 
	 * @return La descripción del SO.
	 */
	public static String getOSName() {
		return System.getProperty("os.name");
	}

	/**
	 * Devuelve la <b>versión</b> del Sist. Operativo.
	 * 
	 * @return La versión del SO.
	 */
	public static String getOSVersion() {
		return System.getProperty("os.version");
	}

	/**
	 * @param theClass
	 *            la clase.
	 * @return el path donde se encuentra la clase.
	 */
	@SuppressWarnings("unchecked")
	public static String getClassPath(Class theClass) {
		return theClass.getPackage().getName().replace('.', '/');
	}

	/**
	 * Llama a
	 * {@link #exportarAExcel(FWJTable, String, String, String, String, IndicadorProgreso, boolean)},
	 * sin filtros
	 * MiscUtilExcel.exportaAExcel
	 * @param tabla
	 * @param titulo
	 * @param subtitulo
	 * @param absolutePath
	 * @param indicador
	 * @param intercalar
	 */
	public static void exportarAExcel(FWJTable tabla, String titulo, String subtitulo, String absolutePath, IndicadorProgreso indicador, boolean intercalar) {
		MiscUtilExcel.getInstance().exportarAExcel(tabla, titulo, subtitulo, null, absolutePath, indicador, intercalar);
	}

	/**
	 * Ver javadoc de:<br>
	 * <b>{@link MiscUtilExcel#exportarAExcel(FWJTable tabla, String titulo, String subtitulo, String filtros, String ruta, IndicadorProgreso indicador,
	 *		boolean intercalar)}</b>
	 * <br/>
	 * MiscUtilExcel#exportarAExcel(CLJTable tabla, String titulo, String subtitulo, String filtros, String ruta, IndicadorProgreso indicador,
	 *		boolean intercalar)
	 * 
	 */
	public static void exportarAExcel(FWJTable tabla, String titulo, String subtitulo, String filtros, String ruta, IndicadorProgreso indicador,
			boolean intercalar) {
		MiscUtilExcel.getInstance().exportarAExcel(tabla, titulo, subtitulo, filtros, ruta, indicador, intercalar);
	}

	public static void exportarAExcelDos(FWJTable tabla, FWJTable tabla2, String titulo, String titulo2, String subtitulo, String subtitulo2, String ruta,
			IndicadorProgreso indicador, boolean intercalar) {
		MiscUtilExcel.getInstance().exportarAExcelDos(tabla, tabla2, titulo, titulo2, subtitulo, subtitulo2, ruta, indicador, intercalar);
	}

	/**
	 * 
	 * Exporta una lista de <b>CLJTable</b> a un archivo <b>Excel</b>, cada
	 * tabla a una hoja distinta.
	 * MiscUtilExcel.exportaAExcel
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
	public static void exportarAExcel(List<FWJTable> tablas, List<String> titulos, List<String> nombresHojas, List<String> filtros, String ruta,
			IndicadorProgreso indicador, boolean intercalar) {
		MiscUtilExcel.getInstance().exportarAExcel(tablas, titulos, nombresHojas, filtros, ruta, indicador, intercalar);
	}





	/**
	 * Crea una tipografìa a partir de un archivo.
	 * 
	 * @param fontFile
	 *            El archivo de la tipografìa.
	 * @param fontFormat
	 *            El formato de tipografìa. Puede ser MiscUtil.TRUETYPE_FONT o
	 *            MiscUtil.TYPE1_FONT.
	 * @param fontStyle
	 *            El estilo de la tipografìa. Puede ser
	 *            MiscUtil.PLAIN_FONT_STYLE o MiscUtil.BOLD_FONT_STYLE o
	 *            MiscUtil.ITALIC_FONT_STYLE.
	 * @param fontSize
	 *            El tamaño de la tipografìa.
	 * @return La fuente creada a partir del archivo.
	 * @throws IOException
	 * @throws FontFormatException
	 */
	public static Font crearFont(String fontFile, int fontFormat, int fontStyle, float fontSize) throws FontFormatException, IOException {
		Font fontPrototype = Font.createFont(fontFormat, FileUtil.getResourceAsStream(fontFile));
		return fontPrototype.deriveFont(fontStyle, fontSize);
	}

	/**
	 * Convierte un valor booleano a String.
	 * 
	 * @param b
	 * @return
	 */
	public static String booleanToString(boolean b) {
		return (b ? "1" : "0");
	}

	public static void log(String mensaje) {
		System.out.println(DateUtil.getAhora().toString() + ": " + mensaje);
	}

	public static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch(InterruptedException e) {
		}
	}

	public static void beep() {
		Toolkit.getDefaultToolkit().beep();
	}

	public static void serializar(Serializable obj, String filename) throws FileNotFoundException, IOException {
		ObjectOutput oo = new ObjectOutputStream(new FileOutputStream(filename));
		oo.writeObject(obj);
		oo.close();
	}

	public static void serializar(Serializable obj) throws FileNotFoundException, IOException {
		serializar(obj, obj.getClass().getName() + ".ser");
	}

	public static Object deserializar(File file) throws FileNotFoundException, IOException, ClassNotFoundException {
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
		Object obj = ois.readObject();
		ois.close();
		return obj;
	}

	public static Object deserializar(String filename) throws FileNotFoundException, IOException, ClassNotFoundException {
		return deserializar(new File(filename));
	}

	public static String getClipboardText() throws FWException {
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable contents = clipboard.getContents(null);
		String text = "";
		if(contents != null && contents.isDataFlavorSupported(DataFlavor.stringFlavor)) {
			try {
				text = (String)contents.getTransferData(DataFlavor.stringFlavor);
			} catch(Exception e) {
				throw new FWException("No se pudo recuperar el texto del Portapapeles", e);
			}
		}
		return text;
	}

	public static void writeToClipboard(String text) {
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable contents = new StringSelection(text);
		clipboard.setContents(contents, null);
	}
	
	/**
	 * - Nunca devuelve null
	 * - hace trim
	 * - reemplaza el signo porcentaje
	 * - reemplaza el signo 'guion bajo' (_)
	 * @param param
	 * @return
	 */
	public static String normalizarSqlLikeParam (String param){
		if (param == null){
			return "";
		}
		return param.replaceAll("%", "").replaceAll("_", "").trim();
	}

	/**
	 * @param throwable
	 * @return el String correspondiente al stack trace de la excepción pasada como parámetro.
	 */
	public static String getTextoExcepcion (Throwable throwable) {
		if (throwable == null)
			return null ;
		StringWriter sw = new StringWriter();
		throwable.printStackTrace(new PrintWriter(sw));
		return sw.toString() ;
	}

	/**
	 * Reemplaza acentos, ñ's, espacios y pasa a mayúsculas.
	 * Ej) La Nación -> LA_NACION
	 * @param string
	 * @return el string pasado como parámetro transformado.
	 */
	public static String getNombreCarpeta(String string) {
		string = StringUtil.trim(string) ;
		string = StringUtil.reemplazarAcentos(string) ;
		string = string.replaceAll("ñ", "n");
		string = string.replaceAll("Ñ", "N");
		string = string.replaceAll(StringUtil.ESPACIO_BLANCO, "_");
		return string.toUpperCase() ;
	}

}