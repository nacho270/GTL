package ar.com.textillevel.gui.modulos.agenda.acciones;

import ar.clarin.fwjava.templates.modulo.model.accionesmouse.AccionAdicional;
import ar.clarin.fwjava.templates.modulo.model.listeners.AccionEvent;
import ar.com.textillevel.entidades.gente.IAgendable;
import ar.com.textillevel.gui.modulos.agenda.gui.JDialogDetalleContacto;

public class AccionDobleClickItemAgenda extends AccionAdicional<IAgendable>{

	@Override
	protected void update(AccionEvent<IAgendable> e) {
		if(e.getSelectedElements().size() == 1) {
			new JDialogDetalleContacto(e.getSelectedElements().get(0),e.getSource().getFrame());
		}
	}

}
