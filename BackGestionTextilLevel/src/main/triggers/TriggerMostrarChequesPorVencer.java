package main.triggers;

import java.util.List;

import main.GTLMainTemplate;

import ar.com.textillevel.entidades.cheque.Cheque;
import ar.com.textillevel.entidades.config.ParametrosGenerales;
import ar.com.textillevel.facade.api.remote.ChequeFacadeRemote;
import ar.com.textillevel.facade.api.remote.ParametrosGeneralesFacadeRemote;
import ar.com.textillevel.gui.acciones.JDialogListaDeChequesPorVencer;
import ar.com.textillevel.util.GTLBeanFactory;

public class TriggerMostrarChequesPorVencer extends Trigger{

	private ParametrosGenerales pg = GTLBeanFactory.getInstance().getBean2(ParametrosGeneralesFacadeRemote.class).getParametrosGenerales();
	
	@Override
	public boolean esValido() {
		return (pg != null && pg.getDiasAvisoVencimientoDeCheque()!=null && pg.getDiasVenceCheque() !=null);	
	}

	@Override
	public void execute() {
		List<Cheque> cheques = GTLBeanFactory.getInstance().getBean2(ChequeFacadeRemote.class).getListaDeChequesProximosAVencer(pg.getDiasAvisoVencimientoDeCheque(), pg.getDiasVenceCheque());
		if(cheques!=null && !cheques.isEmpty()){
			JDialogListaDeChequesPorVencer vencer = new JDialogListaDeChequesPorVencer(GTLMainTemplate.getFrameInstance(), cheques);
			vencer.setVisible(true);
		}
	}
}
