package ar.com.textillevel.gui.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.CLJTable;
import ar.clarin.fwjava.util.DateUtil;
import ar.clarin.fwjava.util.FileUtil;
import ar.clarin.fwjava.util.StringUtil;
import ar.clarin.fwjava.util.SwingWorker;
import ar.com.textillevel.gui.util.dialogs.WaitDialog;
import ar.com.textillevel.gui.util.num2text.Num2Text;

public class GenericUtils {

	private static final String REG_EXP_NUMERO = "-?[0-9]+([,|\\.][0-9]+)?";
	private static final Pattern pattern = Pattern.compile(REG_EXP_NUMERO);
	private static final Random rand = new Random();
	private static final Integer MARGEN_COLOR = 55;
	private static final Integer LIM_INF_COLOR = 55;
	private static final Integer LIM_SUP_COLOR = 200;
	private static NumberFormat df;
	private static NumberFormat df2;
	private static NumberFormat df3;
	private static final Double TOLEARANCIA_A_CERO = 0.009;
	
	/* Corresponden al DateUtil.getDia(date) */
	public static final int DIA_DOMINGO = 1; 
	public static final int DIA_LUNES = 2;
	public static final int DIA_MARTES = 3;
	public static final int DIA_MIERCOLES = 4;
	public static final int DIA_JUEVES = 5;
	public static final int DIA_VIERNES = 6;
	public static final int DIA_SABADO = 7;
	
	static{
		df = DecimalFormat.getNumberInstance(new Locale("es_AR"));
		df.setMaximumFractionDigits(2);
		df.setMinimumFractionDigits(2);
		df.setMinimumIntegerDigits(1);
		df.setGroupingUsed(true);
		
		df2 = DecimalFormat.getNumberInstance(new Locale("es_AR"));
		df2.setMaximumFractionDigits(6);
		df2.setMinimumFractionDigits(6);
		df2.setMinimumIntegerDigits(1);
		df2.setGroupingUsed(false);
		
		df3 = DecimalFormat.getNumberInstance(new Locale("es_AR"));
		df3.setMaximumFractionDigits(3);
		df3.setMinimumFractionDigits(3);
		df3.setMinimumIntegerDigits(1);
		df3.setGroupingUsed(false);
	}
	
	
	public static boolean esNumerico(String text) {
		if(text == null) {
			return false;
		}
		text = text.replaceAll(",", "").replaceAll("\\.", "");
		Matcher matcher = pattern.matcher(text.trim());
		return matcher.matches();
	}
	
	public static boolean isUnix(){
		String os = System.getProperty("os.name").toLowerCase();
	    return (os.indexOf("nix") >=0 || os.indexOf("nux") >=0);
	}

	public static boolean isWindows(){
		String os = System.getProperty("os.name").toLowerCase();
	    return (os.indexOf("win" ) >= 0); 
	}
	
	public static GridBagConstraints createGridBagConstraints(int x, int y, int posicion, int fill, Insets insets, int cantX, int cantY, double weightx, double weighty) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = posicion;
		gbc.fill = fill;
		gbc.insets = insets;
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = cantX;
		gbc.gridheight = cantY;
		gbc.weightx = weightx;
		gbc.weighty = weighty;
		return gbc;
	}
	
	public static Color getRandomColor() {
		Color color = new Color(rand.nextInt(256), 	//RED
								rand.nextInt(256), 	//GREEN
								rand.nextInt(256));	//BLUE
		while(!checkColor(color)){
			color = new Color(rand.nextInt(256), 	//RED
							  rand.nextInt(256), 	//GREEN
							  rand.nextInt(256));	//BLUE
		}
        return color;	
    }
	
	private static boolean checkColor(Color color){
		return (checkRed(color) || checkGreen(color) || checkBlue(color)) && checkRange(color);
	}
	
	private static boolean checkRange(Color color) {
		return	(color.getRed()<LIM_SUP_COLOR && color.getRed()>LIM_INF_COLOR) &&
				(color.getBlue()<LIM_SUP_COLOR && color.getBlue()>LIM_INF_COLOR) &&
				(color.getGreen()<LIM_SUP_COLOR && color.getGreen()>LIM_INF_COLOR);
	}

	private static boolean checkRed(Color color) {
		return (255-color.getRed()>MARGEN_COLOR) && MARGEN_COLOR<=color.getGreen() && MARGEN_COLOR<=color.getBlue();
	}

	private static boolean checkGreen(Color color) {
		return (255-color.getGreen()>MARGEN_COLOR) && MARGEN_COLOR<=color.getRed() && MARGEN_COLOR<=color.getBlue();
	}
	
	private static boolean checkBlue(Color color) {
		return (255-color.getBlue()>MARGEN_COLOR) && MARGEN_COLOR<=color.getGreen() && MARGEN_COLOR<=color.getRed();
	}
	
	public static String convertirNumeroATexto(Double numero){
		if(numero<0){
			return "";
		}
		if(Math.floor(numero.doubleValue())-Math.abs(numero.doubleValue())==0){
			return Num2Text.getInstance().convertirLetras(numero.intValue());
		}else{
			String strNro = String.valueOf(numero);
			String pe = strNro.substring(0, strNro.indexOf('.'));
			String pd = strNro.substring(strNro.indexOf('.')+1,strNro.length());
			if(pd.length() >= 2) {
				pd = pd.substring(0,2);
			}
			pd = (pd.length()==1?pd+"0":pd);
			return Num2Text.getInstance().convertirLetras(Integer.parseInt(pe)) + " con " + Num2Text.getInstance().convertirLetras(Integer.parseInt(pd));
		}
	}
	
	public static <T> Collection<T> restaConjuntosOrdenada(Collection<T> izq, Collection<T> der) {
		ArrayList<T> aux = new ArrayList<T>();
		for (T obj : izq) {
			if (!der.contains(obj)) {
				aux.add(obj);
			}
		}
		return aux;
	}
	
	public static void exportarAExcel(CLJTable tabla, String titulo, String subtitulo, String filtros, String ruta, boolean intercalar) {
		ar.clarin.fwjava.util.MiscUtil.exportarAExcel(tabla, titulo, subtitulo, filtros, ruta, null, intercalar);
	}
	
	public static void ponerFondoAnulada(final JScrollPane sp) {
		String url = "ar/com/textillevel/imagenes/fondo_anulada.png";
		sp.setBackground(Color.WHITE);  
		sp.getViewport().setOpaque(false);  
		final Border bkgrnd;
		try {
			InputStream resourceAsStream = FileUtil.getResourceAsStream(url);
			bkgrnd = new CentredBackgroundBorder(ImageIO.read(resourceAsStream));
			Runnable r = new Runnable() {  
		        public void run() { 
					sp.setViewportBorder(bkgrnd);  
		        	sp.repaint();  
		        }  
		    };  
		    SwingUtilities.invokeLater(r);  
		} catch (IOException e) {
			CLJOptionPane.showErrorMessage(null, e.getMessage(), "Error");
		}
	}
	
	public static Dimension getDimensionPantalla() {
		Toolkit tk = Toolkit.getDefaultToolkit();
		return tk.getScreenSize();
	}

	public static NumberFormat getDecimalFormat() {
		return df;
	}

	public static NumberFormat getDecimalFormat2() {
		return df2;
	}
	
	public static NumberFormat getDecimalFormat3() {
		return df3;
	}
	
	public static boolean esHoy(java.sql.Date fecha){
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(DateUtil.getAhora());
		return cal.get(GregorianCalendar.DAY_OF_MONTH)==DateUtil.getDia(fecha) && 
			   cal.get(GregorianCalendar.MONTH)==DateUtil.getMes(fecha) && 
			   cal.get(GregorianCalendar.YEAR)== DateUtil.getAnio(fecha);
	}
	
	public static String fixPrecioCero(String format) {
		if(!StringUtil.isNullOrEmpty(format) && (format.equals(",00") || format.equals(".00"))) {
			return "0,00";
		}
		if(format.indexOf(",")==-1 && format.indexOf(".")==-1){
			return format+",00";
		}
		return format;
	}
	
	public static int restarFechas(Date fechaInicial, Date fechaFinal) {
		long fechaInicialMs = fechaInicial.getTime();
		long fechaFinalMs = fechaFinal.getTime();
		long diferencia = fechaFinalMs - fechaInicialMs;
		double dias = Math.floor(diferencia / (1000 * 60 * 60 * 24));
		return ((int) dias);
	}

	public static void main(String[] args) {
		String nn = "234";
		System.out.println(esNumerico(nn));
	}

	public static boolean esCero(Double numero){
		return Math.abs(numero) < TOLEARANCIA_A_CERO;
	}

	public static float getFloatValueInJTextField(JTextField txt) {
		String contenidoTexto = txt.getText().trim();
		return Float.valueOf(contenidoTexto.replaceAll(",", "\\."));
	}

	public static double getDoubleValueInJTextField(JTextField txt) {
		String contenidoTexto = txt.getText().trim();
		return getDoubleValue(contenidoTexto);
	}

	public static String doubleToStringArg(Double d) {
		if(d == null) {
			return null;
		}
		String valorStr = getDecimalFormat().format(d);
		valorStr = swapSeparadores(valorStr);
		return valorStr;
	}

	public static String formatDoubleStringArg(String d) {
		if(d == null) {
			return null;
		}
		return swapSeparadores(d);
	}

	private static String swapSeparadores(String valorStr) {
		if(valorStr == null) {
			return null;
		}
		valorStr = valorStr.replaceAll(",", "#");
		valorStr = valorStr.replaceAll("\\.", ",");
		valorStr = valorStr.replaceAll("#", "\\.");
		return valorStr;
	}

	public static double getDoubleValue(String contenidoTexto) {
		if(contenidoTexto.indexOf("\\.") != -1) {
			contenidoTexto = contenidoTexto.replaceAll(".", ",");
		} /*else {
			return Double.valueOf(contenidoTexto.replaceAll(",", "\\."));
		}*/
		return Double.valueOf(contenidoTexto.replaceAll("\\.", "").replaceAll(",", "."));
	}
	
	public static void setValorComboSinListeners(JComboBox combo, Object valor){
		List<ItemListener> itemListenerList = Arrays.asList(combo.getItemListeners()) ;
		for (ItemListener itemListener : itemListenerList) {
			combo.removeItemListener(itemListener) ;
		}
		combo.setSelectedItem(valor);
		for (ItemListener itemListener : itemListenerList) {
			combo.addItemListener(itemListener) ;
		}
	}

	public static Integer getMaximo(Integer... numeros){
		Integer max = 0;
		for(Integer n : numeros){
			if(n > max){
				max = n;
			}
		}
		return max;
	}
	
	public static boolean isSistemaTest(){
		return System.getProperties().getProperty("test")!= null && System.getProperties().getProperty("test").equals("1");
	}

	public static String colorToHexa(Color color){
		String hex = Integer.toHexString(color.getRGB() & 0xffffff);
		if (hex.length() < 6) {
		    hex = "0" + hex;
		}
		return "#" + hex;
	}

	public static String repeat(String str, int cantVeces) {
		StringBuilder result = new StringBuilder();
		for(int i = 0; i < cantVeces; i++) {
			result = result.append(str);
		}
		return result.toString();
	}

	public static String formatAsSuperIndex(String texto, boolean underline){
		int coma = texto.indexOf(",");
		if(coma == -1){
			return texto;
		}
		String decimales = texto.substring(coma + 1, texto.length());
		if(underline){
			return texto.substring(0, coma+1) + "<sup><u>" + decimales + "</u></sup>";
		}else{
			return texto.substring(0, coma+1) + "<sup>" + decimales + "</sup>";
		}
	}
	
	public interface BackgroundTask{
		public void perform();
	}
	
	public static void realizarOperacionConDialogoDeEspera(String textoEspera, final BackgroundTask task){
		SwingWorker sw = new SwingWorker() {
			@Override
			public Object construct() {
				task.perform();
				WaitDialog.stopWait();
				return 0;
			}
		};
		sw.start();
		WaitDialog.startWait(textoEspera);
	}
}