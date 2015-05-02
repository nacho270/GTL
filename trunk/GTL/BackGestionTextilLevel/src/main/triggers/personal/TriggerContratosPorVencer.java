package main.triggers.personal;

import java.util.List;

import main.triggers.Trigger;
import ar.clarin.fwjava.templates.main.AbstractMainTemplate;
import ar.com.textillevel.gui.modulos.personal.gui.JDialogContratosPorVencer;
import ar.com.textillevel.modulos.personal.entidades.configuracion.ParametrosGeneralesPersonal;
import ar.com.textillevel.modulos.personal.entidades.legajos.to.DatosVencimientoContratoEmpleadoTO;
import ar.com.textillevel.modulos.personal.facade.api.remote.EmpleadoFacadeRemote;
import ar.com.textillevel.modulos.personal.facade.api.remote.ParametrosGeneralesPersonalFacadeRemote;
import ar.com.textillevel.modulos.personal.utils.GTLPersonalBeanFactory;

public class TriggerContratosPorVencer extends Trigger{
	
	private final ParametrosGeneralesPersonal pg = GTLPersonalBeanFactory.getInstance().getBean2(ParametrosGeneralesPersonalFacadeRemote.class).getParametrosGenerales();
	
	@Override
	public boolean esValido() {
		return pg != null && pg.getAlarmasFinContrato()!=null && !pg.getAlarmasFinContrato().isEmpty();
	}

	@Override
	public void execute() {
		List<DatosVencimientoContratoEmpleadoTO> empleadosConContratosPorVencer = GTLPersonalBeanFactory.getInstance().getBean2(EmpleadoFacadeRemote.class).getEmpleadosConContratoPorVencer(pg.getAlarmasFinContrato());
		if(empleadosConContratosPorVencer!=null && !empleadosConContratosPorVencer.isEmpty()){
			new JDialogContratosPorVencer(AbstractMainTemplate.getFrameInstance(), empleadosConContratosPorVencer).setVisible(true);
		}
	}
}
