package ar.com.textillevel.mobile.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class GenericUtils {
	
	private static NumberFormat df;
	
	static{
		df = DecimalFormat.getNumberInstance(new Locale("es_AR"));
		df.setMaximumFractionDigits(2);
		df.setMinimumFractionDigits(2);
		df.setMinimumIntegerDigits(1);
		df.setGroupingUsed(true);
	}
	
	public static String formatMonto(Number monto) {
		return df.format(monto);
	}
	
	public static boolean isNullOrEmpty(String text) {
		return text == null || text.trim().length() == 0; 
	}
}
