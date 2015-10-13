package ar.com.textillevel.gui.modulos.personal.modulos.recibossueldo.acciones;

import ar.com.fwcommon.templates.modulo.model.accionesmouse.AccionAdicional;
import ar.com.fwcommon.templates.modulo.model.listeners.AccionEvent;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.gui.modulos.personal.modulos.recibossueldo.gui.JDialogReciboSueldo;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.ReciboSueldo;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.enums.EEstadoReciboSueldo;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.to.InfoReciboSueltoTO;
import ar.com.textillevel.modulos.personal.facade.api.remote.ReciboSueldoFacadeRemote;
import ar.com.textillevel.modulos.personal.utils.GTLPersonalBeanFactory;

public class AccionDobleClickReciboSueldo extends AccionAdicional<InfoReciboSueltoTO>{

	private ReciboSueldoFacadeRemote reciboSueldoFacade;

	@Override
	protected void update(AccionEvent<InfoReciboSueltoTO> e) {
		//Solo si el recibo está impreso
		InfoReciboSueltoTO irsto = e.getSelectedElements().get(0);
		ReciboSueldo rs = null;
		if(irsto.getReciboSueldo() != null && irsto.getReciboSueldo().getEstado() == EEstadoReciboSueldo.IMPRESO) {
			rs = getReciboSueldoFacade().getByIdEager(irsto.getReciboSueldo().getId());
			JDialogReciboSueldo dialog = new JDialogReciboSueldo(e.getSource().getFrame(), rs);
			GuiUtil.centrar(dialog);
			dialog.setVisible(true);
		}
	}

	private ReciboSueldoFacadeRemote getReciboSueldoFacade() {
		if(reciboSueldoFacade == null) {
			reciboSueldoFacade = GTLPersonalBeanFactory.getInstance().getBean2(ReciboSueldoFacadeRemote.class);
		}
		return reciboSueldoFacade;
	}


}
