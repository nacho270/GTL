package main.triggers;

import main.triggers.validacion.numeracionfactura.ValidadorNumeracionFactura;
import main.triggers.validacion.numeracionfactura.exceptions.VencimientoNumeracionPorFechaException;
import main.triggers.validacion.numeracionfactura.exceptions.VencimientoNumeracionPorNumerosException;
import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.templates.main.AbstractMainTemplate;
import ar.com.textillevel.entidades.config.ConfiguracionNumeracionFactura;
import ar.com.textillevel.entidades.config.ParametrosGenerales;
import ar.com.textillevel.facade.api.remote.ParametrosGeneralesFacadeRemote;
import ar.com.textillevel.util.GTLBeanFactory;

public class TriggerVencimientoNumeracionFactura extends Trigger {

	private final ParametrosGenerales pg = GTLBeanFactory.getInstance().getBean2(ParametrosGeneralesFacadeRemote.class).getParametrosGenerales();

	@Override
	public void execute() {
		ConfiguracionNumeracionFactura configuracionFacturaA = pg.getConfiguracionFacturaA();
		ConfiguracionNumeracionFactura configuracionFacturaB = pg.getConfiguracionFacturaB();

		try {
			ValidadorNumeracionFactura.verificarValidezConfiguracionFactura(configuracionFacturaA);
		} catch (VencimientoNumeracionPorNumerosException v) {
			FWJOptionPane.showWarningMessage(AbstractMainTemplate.getFrameInstance(), v.getMessage(), "Advertencia");
		} catch (VencimientoNumeracionPorFechaException v2) {
			FWJOptionPane.showWarningMessage(AbstractMainTemplate.getFrameInstance(), v2.getMessage(), "Advertencia");
		}
		
		try {
			ValidadorNumeracionFactura.verificarValidezConfiguracionFactura(configuracionFacturaB);
		} catch (VencimientoNumeracionPorNumerosException v) {
			FWJOptionPane.showWarningMessage(AbstractMainTemplate.getFrameInstance(), v.getMessage(), "Advertencia");
		} catch (VencimientoNumeracionPorFechaException v2) {
			FWJOptionPane.showWarningMessage(AbstractMainTemplate.getFrameInstance(), v2.getMessage(), "Advertencia");
		}
	}

	@Override
	public boolean esValido() {
		return (pg != null && (pg.getConfiguracionFacturaA() != null || pg.getConfiguracionFacturaB() != null));
	}

}
