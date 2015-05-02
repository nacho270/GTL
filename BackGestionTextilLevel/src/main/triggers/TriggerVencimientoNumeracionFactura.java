package main.triggers;

import main.triggers.validacion.numeracionfactura.ValidadorNumeracionFactura;
import main.triggers.validacion.numeracionfactura.exceptions.VencimientoNumeracionPorFechaException;
import main.triggers.validacion.numeracionfactura.exceptions.VencimientoNumeracionPorNumerosException;
import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.templates.main.AbstractMainTemplate;
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
			CLJOptionPane.showWarningMessage(AbstractMainTemplate.getFrameInstance(), v.getMessage(), "Advertencia");
		} catch (VencimientoNumeracionPorFechaException v2) {
			CLJOptionPane.showWarningMessage(AbstractMainTemplate.getFrameInstance(), v2.getMessage(), "Advertencia");
		}
		
		try {
			ValidadorNumeracionFactura.verificarValidezConfiguracionFactura(configuracionFacturaB);
		} catch (VencimientoNumeracionPorNumerosException v) {
			CLJOptionPane.showWarningMessage(AbstractMainTemplate.getFrameInstance(), v.getMessage(), "Advertencia");
		} catch (VencimientoNumeracionPorFechaException v2) {
			CLJOptionPane.showWarningMessage(AbstractMainTemplate.getFrameInstance(), v2.getMessage(), "Advertencia");
		}
	}

	@Override
	public boolean esValido() {
		return (pg != null && (pg.getConfiguracionFacturaA() != null || pg.getConfiguracionFacturaB() != null));
	}

}
