package ar.com.textillevel.mobile.util;

import java.util.Collection;

public class StringUtil {

	public static String getCadena(Object[] tokens, String separador) {
	    StringBuffer cadena = new StringBuffer();
		for(int i = 0; i < tokens.length; i++) {
			if(cadena.length() != 0) {
			    cadena.append(separador);
			}
			cadena.append(tokens[i].toString().trim());
		}
	    return cadena.toString();
	}

	public static String getCadena(Collection<?> tokens, String separador) {
	    return getCadena(tokens.toArray(), separador);
	}

}