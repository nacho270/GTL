package ar.com.textillevel.gui.modulos.remitoentrada.acciones;

import ar.com.fwcommon.templates.modulo.model.accionesmouse.AccionAdicional;
import ar.com.fwcommon.templates.modulo.model.listeners.AccionEvent;
import ar.com.textillevel.entidades.documentos.remito.RemitoEntrada;
import main.acciones.facturacion.OperacionSobreRemitoEntradaHandler;

public class AccionDobleClickRemitoEntrada extends AccionAdicional<RemitoEntrada> {

	@Override
	protected void update(AccionEvent<RemitoEntrada> e) {
		if(e.getSelectedElements().isEmpty()) {
			return;
		}
		OperacionSobreRemitoEntradaHandler handler = new OperacionSobreRemitoEntradaHandler(e.getSource().getFrame(), e.getSelectedElements().get(0), true);
		handler.showRemitoEntradaDialog();
	}

}
