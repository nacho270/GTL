package main.triggers.personal;

import main.triggers.Trigger;
import ar.com.textillevel.gui.modulos.personal.abm.configuracion.JDialogParametrosGenerales;
import ar.com.textillevel.modulos.personal.entidades.configuracion.ParametrosGeneralesPersonal;
import ar.com.textillevel.modulos.personal.facade.api.remote.ParametrosGeneralesPersonalFacadeRemote;
import ar.com.textillevel.modulos.personal.utils.GTLPersonalBeanFactory;

public class TriggerParametrosGeneralesPersonal  extends Trigger{

	@Override
	public boolean esValido() {
		return true;
	}

	@Override
	public void execute() {
		ParametrosGeneralesPersonal pg = GTLPersonalBeanFactory.getInstance().getBean2(ParametrosGeneralesPersonalFacadeRemote.class).getParametrosGenerales();
		if(pg == null || (pg != null && pg.hayAlgunParametroVacio())){
			JDialogParametrosGenerales jd = new JDialogParametrosGenerales(null,pg);
			jd.setVisible(true);
		}
	}

}
