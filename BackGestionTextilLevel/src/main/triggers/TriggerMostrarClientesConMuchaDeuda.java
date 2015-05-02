package main.triggers;

import java.util.List;

import main.GTLMainTemplate;

import ar.com.textillevel.entidades.config.ParametrosGenerales;
import ar.com.textillevel.entidades.to.ClienteDeudaTO;
import ar.com.textillevel.facade.api.remote.ClienteFacadeRemote;
import ar.com.textillevel.facade.api.remote.ParametrosGeneralesFacadeRemote;
import ar.com.textillevel.gui.acciones.JDialogVerClientesConMuchaDeuda;
import ar.com.textillevel.util.GTLBeanFactory;

public class TriggerMostrarClientesConMuchaDeuda extends Trigger{

	private ParametrosGenerales pg = GTLBeanFactory.getInstance().getBean2(ParametrosGeneralesFacadeRemote.class).getParametrosGenerales();
	
	@Override
	public boolean esValido() {
		return (pg != null && pg.getUmbralDeuda()!=null);
	}

	@Override
	public void execute() {
		List<ClienteDeudaTO> clientes = GTLBeanFactory.getInstance().getBean2(ClienteFacadeRemote.class).getClientesConDeudaMayorA(pg.getUmbralDeuda());
		if(clientes!=null && !clientes.isEmpty()){
			new JDialogVerClientesConMuchaDeuda(GTLMainTemplate.getFrameInstance(),clientes,pg.getUmbralDeuda()).setVisible(true);
		}
	}
}
