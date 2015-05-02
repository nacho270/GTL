package ar.com.textillevel.gui.modulos.odt.acciones;

import ar.clarin.fwjava.templates.modulo.model.accionesmouse.AccionAdicional;
import ar.clarin.fwjava.templates.modulo.model.listeners.AccionEvent;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;

public class AccionDobleClickODT extends AccionAdicional<OrdenDeTrabajo> {

	@Override
	protected void update(AccionEvent<OrdenDeTrabajo> e) {
		AccionCargarSecuenciaDeTrabajoODT.ejecutarCargaSecuencia(e);
	}
}
