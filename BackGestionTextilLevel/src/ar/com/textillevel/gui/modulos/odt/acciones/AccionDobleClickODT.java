package ar.com.textillevel.gui.modulos.odt.acciones;

import ar.com.fwcommon.templates.modulo.model.accionesmouse.AccionAdicional;
import ar.com.fwcommon.templates.modulo.model.listeners.AccionEvent;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.gui.acciones.odt.JDialogVisualizarDetalleODT;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;
import ar.com.textillevel.modulos.odt.facade.api.remote.OrdenDeTrabajoFacadeRemote;
import ar.com.textillevel.modulos.odt.to.ODTTO;
import ar.com.textillevel.util.GTLBeanFactory;

public class AccionDobleClickODT extends AccionAdicional<ODTTO> {

	@Override
	protected void update(AccionEvent<ODTTO> e) {
		ODTTO odtto = e.getSelectedElements().get(0);
		OrdenDeTrabajo ordenDeTrabajo = GTLBeanFactory.getInstance().getBean2(OrdenDeTrabajoFacadeRemote.class).getByIdEager(odtto.getId());
		JDialogVisualizarDetalleODT v = new JDialogVisualizarDetalleODT(e.getSource().getFrame(), ordenDeTrabajo);
		GuiUtil.centrar(v);
		v.setVisible(true);
	}

}
