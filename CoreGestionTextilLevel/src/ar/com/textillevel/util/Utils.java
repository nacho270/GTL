package ar.com.textillevel.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import ar.clarin.fwjava.util.StringUtil;

public class Utils {

	private static NumberFormat df;
	
	static{
		df = DecimalFormat.getNumberInstance(new Locale("es_AR"));
		df.setMaximumFractionDigits(2);
		df.setMinimumFractionDigits(2);
		df.setMinimumIntegerDigits(1);
		df.setGroupingUsed(true);
	}
	
	public static NumberFormat getDecimalFormat() {
		return df;
	}
	
	public static String fixPrecioCero(String format) {
		if(!StringUtil.isNullOrEmpty(format) && (format.equals(",00") || format.equals(".00"))) {
			return "0,00";
		}
		return format;
	}
	
	public static boolean esAfirmativo(String texto) {
		texto = texto.toLowerCase();
		return texto.equals("si") || texto.equals("yes") || 
			   texto.equals("1") || texto.equals("true") ||
			   texto.equals("verdadero");
	}
}
