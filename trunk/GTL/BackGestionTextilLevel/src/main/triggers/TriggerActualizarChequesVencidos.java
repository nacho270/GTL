package main.triggers;

import java.util.List;

import main.GTLMainTemplate;

import ar.com.textillevel.entidades.cheque.Cheque;
import ar.com.textillevel.entidades.config.ParametrosGenerales;
import ar.com.textillevel.facade.api.remote.ChequeFacadeRemote;
import ar.com.textillevel.facade.api.remote.ParametrosGeneralesFacadeRemote;
import ar.com.textillevel.gui.acciones.JDialogListaChequesVencidos;
import ar.com.textillevel.util.GTLBeanFactory;

public class TriggerActualizarChequesVencidos extends Trigger{

	private ParametrosGenerales pg = GTLBeanFactory.getInstance().getBean2(ParametrosGeneralesFacadeRemote.class).getParametrosGenerales();

	@Override
	public boolean esValido() {
		return (pg != null && pg.getDiasAvisoVencimientoDeCheque()!=null && pg.getDiasVenceCheque() !=null);
	}

	@Override
	public void execute() {
		ChequeFacadeRemote chequeFacade = GTLBeanFactory.getInstance().getBean2(ChequeFacadeRemote.class);
		List<Cheque> chequesPorVencer = chequeFacade.obtenerChequesVencidos(pg.getDiasVenceCheque());
		if(chequesPorVencer!=null && !chequesPorVencer.isEmpty()){
			JDialogListaChequesVencidos vencer = new JDialogListaChequesVencidos(GTLMainTemplate.getFrameInstance(), chequesPorVencer);
			vencer.setVisible(true);
		}
	}
}
