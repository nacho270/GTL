package ar.com.textillevel.gui.modulos.personal.modulos.legajos.acciones;

import ar.clarin.fwjava.templates.modulo.model.accionesmouse.AccionAdicional;
import ar.clarin.fwjava.templates.modulo.model.listeners.AccionEvent;
import ar.com.textillevel.gui.modulos.personal.modulos.legajos.gui.JDialogResumenLegajo;
import ar.com.textillevel.modulos.personal.entidades.legajos.Empleado;

public class AccionDobleClickResumenLegajo extends AccionAdicional<Empleado>{

	@Override
	protected void update(AccionEvent<Empleado> e) {
		if(e.getSelectedElements().size() == 1) {
			Empleado empleado = e.getSelectedElements().get(0);
			JDialogResumenLegajo dialog = new JDialogResumenLegajo(e.getSource().getFrame(), empleado);
			dialog.setVisible(true);
		}
	}

}
