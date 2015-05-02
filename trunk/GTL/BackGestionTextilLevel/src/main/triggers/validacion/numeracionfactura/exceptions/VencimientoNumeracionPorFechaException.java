package main.triggers.validacion.numeracionfactura.exceptions;

import ar.com.textillevel.entidades.config.ConfiguracionNumeracionFactura;

public class VencimientoNumeracionPorFechaException extends Exception {

	private static final long serialVersionUID = -3442222243958864916L;

	public VencimientoNumeracionPorFechaException(ConfiguracionNumeracionFactura conf, Integer cantDias) {
		super("La configuración de factura " + conf.getTipoFactura() + " vence en " + cantDias + " días");
	}
}