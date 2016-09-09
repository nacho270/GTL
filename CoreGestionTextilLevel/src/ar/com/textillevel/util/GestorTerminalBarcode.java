package ar.com.textillevel.util;

import ar.com.textillevel.entidades.cuenta.to.ETipoDocumento;

public class GestorTerminalBarcode {

	public static String crear(ETipoDocumento tipoDocumento, Integer nro, boolean test) {
		if (tipoDocumento.getPrefijoCodigoBarras() == null) {
			throw new UnsupportedOperationException("No se ha definido el prefijo");
		}
		return tipoDocumento.getPrefijoCodigoBarras() + nro + (test?"1":"0");
	}

	public static ETipoDocumento getTipoDocumento(String codigo) {
		return ETipoDocumento.getByPrefijo(codigo.substring(0, 4).toString());
	}

	public static String extraer(String codigo) {
		int len = codigo.length();
		return codigo.substring(4, len - 1);
	}
}
