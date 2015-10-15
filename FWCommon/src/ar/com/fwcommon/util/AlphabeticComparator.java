package ar.com.fwcommon.util;

import java.util.Comparator;

@SuppressWarnings("rawtypes")
public class AlphabeticComparator implements Comparator {

	/**
	 * Permite comparar y ordenar una coleccion sin importar si sus elementos
	 * están en mayuscula o minuscula
	 */
	public int compare(Object o1, Object o2) {
		String s1 = o1.toString();
		String s2 = o2.toString();
		return s1.toLowerCase().compareTo(s2.toLowerCase());
	}
}
