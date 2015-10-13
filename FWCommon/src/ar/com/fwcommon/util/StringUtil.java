package ar.com.fwcommon.util;

import java.text.Collator;
import java.util.Collection;
import java.util.Comparator;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Clase con funciones útiles con Strings.
 */
public class StringUtil {

    public static final String ESPACIO_BLANCO = " ";
    public static final String RETORNO_CARRO = "\n";
    public static final String TODOS = "Todos";
	private static final String ALT_MAS_255 = " ";

	/**
	 * Pone en Mayúsculas la <b>primera letra<b> de la cadena pasada por parámetro.
	 * @param str La cadena de caracteres.
	 * @return La nueva cadena de caracteres con la primera letra en mayúsculas.
	 */
	public static String ponerMayuscula(String str) {
		char chars[] = str.toCharArray();
		chars[0] = Character.toUpperCase(chars[0]);
        return String.valueOf(chars);
	}

	/**
	 * Pone en Mayúsculas las primeras letras de <b>las palabras</b> de una cadena
	 * separadas por un espacio.
	 * @param str La cadena de caracteres.
	 * @return retval La nueva cadena de caracteres con las primeras letras de cada una
	 *                de sus palabras en mayúsculas.
	 */
	public static String ponerMayusculas(String str) {
	    StringTokenizer tokens = new StringTokenizer(str, ESPACIO_BLANCO);
	    StringBuffer retval = new StringBuffer();
	    while(tokens.hasMoreTokens()) {
	        String s = tokens.nextToken();
	        retval.append(Character.toUpperCase(s.charAt(0)) + s.substring(1));
	        if(tokens.hasMoreTokens())
	            retval.append(ESPACIO_BLANCO);
	    }
	    return retval.toString();
	}

	/**
	 * Pone en Mayúsculas las primeras letras de <b>las palabras</b> de una cadena
	 * separadas por un separador.
	 * @param str La cadena de caracteres.
	 * @param separador El separador de las palabras de la cadena.
	 * @return retval La nueva cadena de caracteres con las primeras letras de cada una
	 *                de sus palabras en mayúsculas.
	 */
	public static String ponerMayusculas(String str, String separador) {
	    StringTokenizer tokens = new StringTokenizer(str, separador);
	    StringBuffer retval = new StringBuffer();
	    while(tokens.hasMoreTokens()) {
	        String s = tokens.nextToken();
	        retval.append(Character.toUpperCase(s.charAt(0)) + s.substring(1));
	        if(tokens.hasMoreTokens())
	            retval.append(separador);
	    }
	    return retval.toString();
	}

	/**
	 * Devuelve un array de String con cada una de las palabras de la cadena separadas
	 * por el separador pasado por parámetro.
	 * @param str La cadena de caracteres.
	 * @param separador El separador de palabras de la cadena.
	 * @return resval El array con cada una de las palabras de la cadena.
	 */
	public static String[] getTokens(String str, String separador) {
        StringTokenizer tokens = new StringTokenizer(str, separador);
	    String[] resval = new String[tokens.countTokens()];
	    int i = 0;
        while(tokens.hasMoreTokens()) {
            resval[i] = tokens.nextToken();
            i++;
        }
	    return resval;
	}

	/**
	 * Devuelve <b>true</b> si la cadena 1 comienza con la cadena 2.
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static boolean startsWithIgnoreCase(String str1, String str2) {
	    return str1.toUpperCase().startsWith(str2.toUpperCase());
	}

	/**
	 * Devuelve a partir de un array un String con la concatenación de todos elementos
	 * separados por un determinado separador.
	 * @param tokens Un array de elementos.
	 * @param separador El separador.
	 * @return cadena La cadena formada.
	 */
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

	/**
	 * Devuelve a partir de un array un String con la concatenación de todos elementos
	 * separados por un determinado separador.
	 * @param tokens Un array de elementos.
	 * @param separador El separador.
	 * @return cadena La cadena formada.
	 */
	public static String getCadena(Collection<?> tokens, String separador) {
	    return getCadena(tokens.toArray(), separador);
	}

    /**
     * @param valor el String original al que se quiere preceder de zeros.
     * @param totalLength El tamaño total deseado para el String.
     * @return Un String del tamaño total pedido con el valor original precedido de los zeros necesarios.
     */
    public static String fillLeftWithZeros(String valor, int totalLength) {
        StringBuilder buff = new StringBuilder(totalLength);
        for(int k = valor.length(); k < totalLength; k++) {
            buff.append('0');
        }
        buff.append(valor);
        return buff.toString();
    }

    /**
     * @param valor el String original al que se quiere continuar con blancos de zeros.
     * @param totalLength El tamaño total deseado para el String.
     * @return Un String del tamaño total pedido con el valor original seguido de los espacios en blanco necesarios.
     */
    public static String fillRightWithSpaces(String valor, int totalLength) {
        StringBuilder buff = new StringBuilder(totalLength);
        buff.append(valor);
        for(int k = valor.length(); k < totalLength; k++) {
            buff.append(ESPACIO_BLANCO);
        }
        return buff.toString();
    }

	public static String replaceAll(String input, String oldPattern, String newPattern) {
		  int start = input.indexOf(oldPattern);
		  if(start == -1) {
			  return input;
		  }
		  int lf = oldPattern.length();
		  char[] targetChars = input.toCharArray();
		  StringBuffer buffer = new StringBuffer();
		  int copyFrom = 0;
		  while(start != -1) {
              buffer.append(targetChars, copyFrom, start - copyFrom);
              buffer.append(newPattern);
              copyFrom = start + lf;
              start = input.indexOf(oldPattern, copyFrom);
		  }
		  buffer.append(targetChars, copyFrom, targetChars.length - copyFrom);
		  return buffer.toString();
	}

    /**
     * @param parts Las partes a concatenar.
     * @return Un string construido eficientemente concatenando las partes indicadas.
     */
    public static String build(Object... parts) {
        StringBuffer stringBuffer = new StringBuffer();
        for(Object part : parts) {
            stringBuffer.append(part.toString());
        }
        return stringBuffer.toString();
    }

    /**
     * Devuelve <b>true</b> si el caracter es ASCII 7 bit imprimible.
     * @param ch El caracter a chequear.
     * @return
     */
    public static boolean isAsciiPrintable(char ch) {
        return (ch >= 32 && ch < 127);
    }

    /**
     * Trunca el string a la longitud indicada y agrega <b>...</b> al
     * final de la cadena para indicar que fue truncada.
     * @param str La cadena a truncar.
     * @param length La longitud máxima.
     * @return La cadena truncada.
     */
    public static String truncate(String str, int length) {
    	StringBuffer sb = new StringBuffer();
    	if(str.length() > length && str.length() > 3) {
    		sb.append(str.substring(0, length - 3)).append("...");
    	} else {
    		sb.append(str);
    	}
    	return sb.toString();
    }

    /**
     * Reemplaza en una cadena cada vocal acentuada por su correspondiente vocal
     * sin acento.
     * @param str
     * @return La cadena sin las vocales acentuadas
     */
    public static String reemplazarAcentos(String str) {
    	if(str != null) {
    		str = str.replaceAll("á", "a");
    		str = str.replaceAll("Á", "A");
    		str = str.replaceAll("é", "e");
    		str = str.replaceAll("É", "E");
    		str = str.replaceAll("í", "i");
    		str = str.replaceAll("Í", "I");
    		str = str.replaceAll("ó", "o");
    		str = str.replaceAll("Ó", "O");
    		str = str.replaceAll("ú", "u");
    		str = str.replaceAll("Ú", "U");
    	}
    	return str;
    }

    /**
     * Reformatea la cadena <code>str</code> a un párrafo.
     * @param str
     * @return La cadena reformateada.
     */
	public static String toParagraph2(String str) {
		StringBuffer buffer = new StringBuffer();
		if(str != null) {
			String tmp = str.replaceAll("-", "");
			String patternStr = "(^.*\\S+.*$)+";
			Pattern pattern = Pattern.compile(patternStr, Pattern.MULTILINE);
			Matcher matcher = pattern.matcher(tmp);
			while(matcher.find()) {
				buffer.append(matcher.group());
			}
		}
		return buffer.toString();
	}

	public static String toParagraph(String str) {
		if (str == null)
			return null ;
		String[] lineas = str.split("\n");
		StringBuffer buffer = new StringBuffer();
		if(lineas.length > 0) {
			buffer.append(lineas[0]);
			for(int i = 1; i < lineas.length; i++) {
				if(lineas[i - 1].endsWith("-")) {
					buffer.deleteCharAt(buffer.length() - 1);
				} else {
					buffer.append(" ");
				}
				buffer.append(lineas[i]);
			}
		}
		return buffer.toString();
	}

	/**
	 * Clase útil para la comparación de Strings.
	 * @param <T>
	 */
	public static class StringComparator<T> implements Comparator<T> {
		public int compare(T o1, T o2) {
			return o1.toString().compareToIgnoreCase(o2.toString());
		}
	}
	
	public static class StringComparatorSpanish implements Comparator<String>{
		public int compare(String st1, String st2) {
			return Collator.getInstance(new Locale("es-ar")).compare(st1, st2);
		}
	}
	

	/**
	 * Realiza un trim sobre un string teniendo en cuenta que el string puede ser null y puede contener ocurrencias
	 * del caracter ALT + 255
	 * @param text
	 * @return
	 */
	public static String trim(String text) {
		if(text == null)
			return "";
		return text.replaceAll(ALT_MAS_255, ESPACIO_BLANCO).trim();
	}

	/**
	 * Devuelve <code>true</code> si <code>text</code> es <code>null</code> o es un string vacio.
	 * @param text
	 * @return <code>true</code> si <code>text</code> es <code>null</code> o es un string vacio
	 */
	public static boolean isNullOrEmptyString(String text) {
		return text == null || text.trim().length() == 0; 
	}
	
	/**
	 * idem isNullOrEmptyString
	 */
	public static boolean isNullOrEmpty(String text) {
		//abrevio el metodo. la clase se llama StringUtil, esta claro que la palabra String al final no era necesaria.
		return isNullOrEmptyString(text); 
	}
	
	/**
	 * Recibe una palabra y reemplaza los acentos de texto por acentos HTML
	 * @param text
	 * @return
	 */
	
	public static String acentosToHTML(String text){
		text = text.replaceAll("á", "&aacute;");
		text = text.replaceAll("é", "&eacute;");
		text = text.replaceAll("í", "&iacute;");
		text = text.replaceAll("ó", "&oacute;");
		text = text.replaceAll("ú", "&uacute;");
		text = text.replaceAll("ñ", "&ntilde;");
		
		text = text.replaceAll("Á", "&Aacute;");
		text = text.replaceAll("É", "&Eacute;");
		text = text.replaceAll("Í", "&Iacute;");
		text = text.replaceAll("Ó", "&Oacute;");
		text = text.replaceAll("Ú", "&Uacute;");
		text = text.replaceAll("Ñ", "&Ntilde;");
		
		return text;
	}
	
	
	/**
	 * Busca un string dentro de otro. Ignora acentos y mayusculas.
	 * @param contenedor
	 * @param busqueda
	 * @return
	 */
	public static boolean findSubstringSpanish (String contenedor, String busqueda){
		if (contenedor == null)
			contenedor ="";
		if (busqueda == null)
			busqueda = "";
		
		contenedor = reemplazarAcentos(contenedor);
		busqueda = reemplazarAcentos(busqueda);
		String pattStr = ".*" + busqueda + ".*";
		Pattern pattern = Pattern.compile(pattStr, Pattern.CANON_EQ |Pattern.CASE_INSENSITIVE );
		return pattern.matcher(contenedor).find();
	}
	public static String reemplazarTagsXML(String str) {
    	if(str != null) {
    		str = str.replaceAll("<", "");
    		str = str.replaceAll(">", "");
    	}
    	return str;
    }
	public static void main (String [] args){
		System.out.println(findSubstringSpanish("Pablo fóresto", "Pablo foresto"));
		System.out.println(findSubstringSpanish("ss", "a"));
		System.out.println(findSubstringSpanish("Vega", "Véga"));
		System.out.println(reemplazarTagsXML("HOLA <ESTE SE VA A REEMPLAZAR>"));
	}
	
	public static String allTrim(String texto){
		if(texto == null) return "";
		return texto.replaceAll("\\s+", StringUtil.ESPACIO_BLANCO).trim();
	}

}