package ar.com.textillevel.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import org.hibernate.exception.ConstraintViolationException;

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

	public static <E extends Object, T extends Comparable<E>> boolean dentroDelRango(T elem, E desde, E hasta) {
		return (desde != null || hasta != null) && (desde == null || elem.compareTo(desde) >= 0) && (hasta ==null || elem.compareTo(hasta) <= 0);
	}

	public static <E extends Object, T extends Comparable<E>> boolean dentroDelRangoEstricto(T elem, E desde, E hasta) {
		return (desde != null || hasta != null) && (desde == null || elem.compareTo(desde) > 0) && (hasta ==null || elem.compareTo(hasta) < 0);
	}

	public static boolean isConstraintViolationException (Exception e) {
		return e.getCause()!=null && e.getCause().getCause()!=null && e.getCause().getCause().getCause()!=null && e.getCause().getCause() instanceof ConstraintViolationException;
	}

}