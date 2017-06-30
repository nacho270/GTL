package ar.com.textillevel.gui.modulos.remitosalida.acciones;

import ar.com.fwcommon.templates.modulo.model.accionesmouse.AccionAdicional;
import ar.com.fwcommon.templates.modulo.model.listeners.AccionEvent;
import ar.com.textillevel.entidades.documentos.remito.RemitoSalida;
import main.acciones.facturacion.OperacionSobreRemitoSalidaHandler;

public class AccionDobleClickRemitoSalida extends AccionAdicional<RemitoSalida> {

	@Override
	protected void update(AccionEvent<RemitoSalida> e) {
		if(e.getSelectedElements().isEmpty()) {
			return;
		}
		OperacionSobreRemitoSalidaHandler handler = new OperacionSobreRemitoSalidaHandler(e.getSource().getFrame(), e.getSelectedElements().get(0), true);
		handler.showRemitoEntradaDialog();
	}

}
