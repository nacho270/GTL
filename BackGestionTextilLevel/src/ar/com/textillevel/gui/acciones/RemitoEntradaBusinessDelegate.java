package ar.com.textillevel.gui.acciones;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.rpc.ServiceException;

import org.apache.commons.lang.ArrayUtils;

import ar.com.textillevel.entidades.documentos.remito.to.DetallePiezaRemitoEntradaSinSalida;
import ar.com.textillevel.gui.acciones.odtwsclient.ODTService;
import ar.com.textillevel.gui.acciones.odtwsclient.ODTServiceServiceLocator;
import ar.com.textillevel.gui.acciones.odtwsclient.OdtEagerTO;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;
import ar.com.textillevel.modulos.odt.facade.api.remote.OrdenDeTrabajoFacadeRemote;
import ar.com.textillevel.util.GTLBeanFactory;

public class RemitoEntradaBusinessDelegate {

	private OrdenDeTrabajoFacadeRemote odtFacade = GTLBeanFactory.getInstance().getBean2(OrdenDeTrabajoFacadeRemote.class);
	private ODTServiceClient wsClient;

	public List<DetallePiezaRemitoEntradaSinSalida> getInfoPiezasEntradaSinSalidaByClient(Integer idCliente) throws RemoteException {
		if(GenericUtils.isSistemaTest()) {
			return getWSClient().getInfoPiezasEntradaSinSalidaByClient(idCliente);
		}
		return odtFacade.getInfoPiezasEntradaSinSalidaByClient(idCliente);
	}

	public List<OrdenDeTrabajo> getODTByIdsEager(List<Integer> ids) throws RemoteException {
		if(GenericUtils.isSistemaTest()) {
			return getWSClient().getByIdsEager(ids);
		}
		return odtFacade.getByIdsEager(ids);
	}

	private ODTServiceClient getWSClient() {
		if (wsClient == null) {
			try {
				wsClient = new ODTServiceClient();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return wsClient;
	}
	
	private class ODTServiceClient {
		ODTService service;
		public ODTServiceClient() throws MalformedURLException, ServiceException {
			ODTServiceServiceLocator locator = new ODTServiceServiceLocator();
			service = locator.getODTServicePort(new URL(System.getProperty("textillevel.odt.ipintercambio")));
		}

		public List<OrdenDeTrabajo> getByIdsEager(List<Integer> ids) throws RemoteException {
			OdtEagerTO[] byIdsEager = service.getByIdsEager(ArrayUtils.toPrimitive(ids.toArray(new Integer[ids.size()])));
			return null;
		}

		public List<DetallePiezaRemitoEntradaSinSalida> getInfoPiezasEntradaSinSalidaByClient(Integer idCliente) throws RemoteException{
			List<DetallePiezaRemitoEntradaSinSalida> lista = new ArrayList<DetallePiezaRemitoEntradaSinSalida>();
			for(ar.com.textillevel.gui.acciones.odtwsclient.DetallePiezaRemitoEntradaSinSalida d :service.getInfoPiezasEntradaSinSalidaByClient(idCliente)) {
				lista.add(new DetallePiezaRemitoEntradaSinSalida(d.getNroRemito(), d.getIdODT(), d.getCodigoODT(), d.getProducto(), d.getCantPiezas(), d.getMetrosTotales()));
			}
			return lista;
		}
	}
}
