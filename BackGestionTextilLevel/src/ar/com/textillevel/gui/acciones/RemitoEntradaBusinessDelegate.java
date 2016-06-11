package ar.com.textillevel.gui.acciones;

import java.util.List;

import ar.com.textillevel.entidades.documentos.remito.to.DetallePiezaRemitoEntradaSinSalida;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;
import ar.com.textillevel.modulos.odt.facade.api.remote.OrdenDeTrabajoFacadeRemote;
import ar.com.textillevel.util.GTLBeanFactory;

public class RemitoEntradaBusinessDelegate {

	private OrdenDeTrabajoFacadeRemote odtFacade = GTLBeanFactory.getInstance().getBean2(OrdenDeTrabajoFacadeRemote.class);

	public List<DetallePiezaRemitoEntradaSinSalida> getInfoPiezasEntradaSinSalidaByClient(Integer idCliente) {
		if(GenericUtils.isSistemaTest()) {
			throw new RuntimeException("No implementado todavia");
		}
		return odtFacade.getInfoPiezasEntradaSinSalidaByClient(idCliente);
	}

	public List<OrdenDeTrabajo> getODTByIdsEager(List<Integer> ids) {
		if(GenericUtils.isSistemaTest()) {
			throw new RuntimeException("No implementado todavia");
		}
		return odtFacade.getByIdsEager(ids);
	}
}
