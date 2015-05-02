package main.triggers.validacion.numeracionfactura;

import main.triggers.validacion.numeracionfactura.exceptions.VencimientoNumeracionPorFechaException;
import main.triggers.validacion.numeracionfactura.exceptions.VencimientoNumeracionPorNumerosException;
import ar.clarin.fwjava.util.DateUtil;
import ar.com.textillevel.entidades.config.ConfiguracionNumeracionFactura;
import ar.com.textillevel.entidades.config.NumeracionFactura;
import ar.com.textillevel.facade.api.remote.FacturaFacadeRemote;
import ar.com.textillevel.util.GTLBeanFactory;

public class ValidadorNumeracionFactura {

	private static FacturaFacadeRemote ffr;

	static {
		ffr = GTLBeanFactory.getInstance().getBean2(FacturaFacadeRemote.class);
	}

	public static void verificarValidezConfiguracionFactura(ConfiguracionNumeracionFactura configuracionFactura) throws VencimientoNumeracionPorFechaException, VencimientoNumeracionPorNumerosException {
		if (configuracionFactura != null) {
			validarConfiguracionPorFecha(configuracionFactura);
			validarConfiguracionPorNumero(configuracionFactura);
		}
	}

	private static void validarConfiguracionPorNumero(ConfiguracionNumeracionFactura configuracionFactura) throws VencimientoNumeracionPorNumerosException {
		Integer ultimoNumeroFacturaImpreso = ffr.getUltimoNumeroFacturaImpreso(configuracionFactura.getTipoFactura());
		NumeracionFactura nf = configuracionFactura.getNumeracionActual(DateUtil.getHoy());
		if (ultimoNumeroFacturaImpreso != null && nf != null) {
			if ((nf.getNroHasta() - ultimoNumeroFacturaImpreso) <= configuracionFactura.getNumerosAntesAviso()) {
				throw new VencimientoNumeracionPorNumerosException(configuracionFactura, nf.getNroHasta() - ultimoNumeroFacturaImpreso);
			}
		}
	}

	private static void validarConfiguracionPorFecha(ConfiguracionNumeracionFactura configuracionFactura) throws VencimientoNumeracionPorFechaException {
		NumeracionFactura nf = configuracionFactura.getNumeracionActual(DateUtil.getHoy());
		if (nf != null) {
			Integer dias = DateUtil.getDaysBetween(DateUtil.getHoy(), nf.getFechaHasta());
			if (dias <= configuracionFactura.getDiasAntesAviso()) {
				throw new VencimientoNumeracionPorFechaException(configuracionFactura, dias);
			}
		}
	}
}
