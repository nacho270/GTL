package main.triggers.validacion.numeracionfactura.exceptions;

import ar.com.textillevel.entidades.config.ConfiguracionNumeracionFactura;

public class VencimientoNumeracionPorNumerosException extends Exception {

	private static final long serialVersionUID = 1670900405674270142L;

	public VencimientoNumeracionPorNumerosException(ConfiguracionNumeracionFactura conf, Integer cantNumeros) {
		super("A la configuración de factura " + conf.getTipoFactura() + " le quedan " + cantNumeros + " números");
	}
}