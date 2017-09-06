package ar.com.textillevel.gui.modulos.cheques.acciones;

import ar.com.fwcommon.templates.modulo.model.accionesmouse.AccionAdicional;
import ar.com.fwcommon.templates.modulo.model.listeners.AccionEvent;
import ar.com.textillevel.entidades.cheque.Cheque;
import ar.com.textillevel.gui.modulos.cheques.gui.JDialogAgregarCheque;

public class AccionDobleClickCheque extends AccionAdicional<Cheque> {

	@Override
	protected void update(AccionEvent<Cheque> e) {
		if(e.getSelectedElements().isEmpty()) {
			return;
		}
		Cheque cheque = e.getSelectedElements().get(0);
		new JDialogAgregarCheque(e.getSource().getFrame(), cheque,true,false);
	}

}
