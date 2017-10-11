package ar.com.textillevel.gui.modulos.remitosalida.acciones;

import ar.com.fwcommon.templates.modulo.model.accionesmouse.AccionAdicional;
import ar.com.fwcommon.templates.modulo.model.listeners.AccionEvent;
import ar.com.textillevel.entidades.documentos.remito.RemitoSalida;
import ar.com.textillevel.facade.api.remote.RemitoSalidaFacadeRemote;
import ar.com.textillevel.util.GTLBeanFactory;
import main.acciones.facturacion.OperacionSobreRemitoSalidaHandler;

public class AccionDobleClickRemitoSalida extends AccionAdicional<RemitoSalida> {

	@Override
	protected void update(AccionEvent<RemitoSalida> e) {
		if(e.getSelectedElements().isEmpty()) {
			return;
		}
		RemitoSalida rs = e.getSelectedElements().get(0);
		RemitoSalida rsEager = GTLBeanFactory.getInstance().getBean2(RemitoSalidaFacadeRemote.class).getByIdConPiezasYProductos(rs.getId());
		OperacionSobreRemitoSalidaHandler handler = new OperacionSobreRemitoSalidaHandler(e.getSource().getFrame(), rsEager, true);
		handler.showRemitoEntradaDialog();
	}

}
