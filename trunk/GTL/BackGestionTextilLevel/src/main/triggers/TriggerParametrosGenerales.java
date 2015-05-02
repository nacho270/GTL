package main.triggers;

import ar.com.textillevel.entidades.config.ParametrosGenerales;
import ar.com.textillevel.facade.api.remote.ParametrosGeneralesFacadeRemote;
import ar.com.textillevel.gui.acciones.JDialogParametrosGenerales;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.util.GTLBeanFactory;

public class TriggerParametrosGenerales extends Trigger{

	@Override
	public boolean esValido() {
		return true;
	}

	@Override
	public void execute() {
		ParametrosGenerales pg = GTLBeanFactory.getInstance().getBean2(ParametrosGeneralesFacadeRemote.class).getParametrosGenerales();
		if(pg == null || (pg != null && pg.hayAlgunParametroVacio(GenericUtils.isSistemaTest()))){
			JDialogParametrosGenerales jd = new JDialogParametrosGenerales(null,pg);
			jd.setVisible(true);
		}
	}
}
