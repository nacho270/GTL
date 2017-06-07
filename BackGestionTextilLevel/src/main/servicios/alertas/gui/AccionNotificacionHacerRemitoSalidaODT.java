package main.servicios.alertas.gui;

import java.util.Collections;

import main.acciones.facturacion.IngresoRemitoSalidaNormalHandler;
import main.servicios.alertas.AccionNotificacion;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;
import ar.com.textillevel.modulos.odt.facade.api.remote.OrdenDeTrabajoFacadeRemote;
import ar.com.textillevel.util.GTLBeanFactory;

public class AccionNotificacionHacerRemitoSalidaODT implements AccionNotificacion<Integer> {

	@Override
	public String getTitulo() {
		return "Dar salida";
	}

	@Override
	public boolean ejecutar(Integer idODT) {
		OrdenDeTrabajoFacadeRemote odtFacade = GTLBeanFactory.getInstance().getBean2(OrdenDeTrabajoFacadeRemote.class);
		OrdenDeTrabajo odt = odtFacade.getByIdEager(idODT);
		IngresoRemitoSalidaNormalHandler handler = new IngresoRemitoSalidaNormalHandler(null, odt.getRemito().getCliente(), Collections.singletonList(odt));
		return handler.gestionarIngresoRemitoSalida();
	}
}
