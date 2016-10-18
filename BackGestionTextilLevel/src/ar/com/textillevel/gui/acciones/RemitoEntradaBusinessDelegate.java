package ar.com.textillevel.gui.acciones;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.rpc.ServiceException;

import main.GTLGlobalCache;

import org.apache.commons.lang.ArrayUtils;

import ar.com.textillevel.entidades.documentos.remito.to.DetallePiezaRemitoEntradaSinSalida;
import ar.com.textillevel.entidades.documentos.remito.to.DetalleRemitoEntradaNoFacturado;
import ar.com.textillevel.gui.acciones.odtwsclient.ODTService;
import ar.com.textillevel.gui.acciones.odtwsclient.ODTServiceServiceLocator;
import ar.com.textillevel.gui.acciones.odtwsclient.OdtEagerTO;
import ar.com.textillevel.gui.acciones.odtwsclient.RemitoEntradaTO;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;
import ar.com.textillevel.modulos.odt.enums.EEstadoODT;
import ar.com.textillevel.modulos.odt.facade.api.remote.OrdenDeTrabajoFacadeRemote;
import ar.com.textillevel.util.GTLBeanFactory;

public class RemitoEntradaBusinessDelegate {

	private OrdenDeTrabajoFacadeRemote odtFacade = GTLBeanFactory.getInstance().getBean2(OrdenDeTrabajoFacadeRemote.class);
	private ODTServiceClient wsClient;

	public List<DetallePiezaRemitoEntradaSinSalida> getInfoPiezasEntradaSinSalidaByClient(Integer idCliente) throws RemoteException {
		if(GenericUtils.isSistemaTest()) {
			List<DetallePiezaRemitoEntradaSinSalida> infoPiezas = odtFacade.getInfoPiezasEntradaSinSalidaByClient(idCliente);
			infoPiezas.addAll(marcarComoNoLocales(getWSClient().getInfoPiezasEntradaSinSalidaByClient(idCliente)));
			return infoPiezas;
		}
		return odtFacade.getInfoPiezasEntradaSinSalidaByClient(idCliente);
	}

	private List<DetallePiezaRemitoEntradaSinSalida> marcarComoNoLocales(List<DetallePiezaRemitoEntradaSinSalida> infoPiezasEntradaSinSalidaByClient) {
		for(DetallePiezaRemitoEntradaSinSalida d : infoPiezasEntradaSinSalidaByClient) {
			d.setNoLocales(true);
		}
		return infoPiezasEntradaSinSalidaByClient;
	}

	public List<OrdenDeTrabajo> getODTByIdsEager(List<DetallePiezaRemitoEntradaSinSalida> odtsInfo) throws RemoteException {
		List<OrdenDeTrabajo> result = new ArrayList<OrdenDeTrabajo>();
		List<Integer> idsODTsLocales = new ArrayList<Integer>();
		List<Integer> idsODTsExternas = new ArrayList<Integer>();
		for(DetallePiezaRemitoEntradaSinSalida dprss : odtsInfo) {
			if(dprss.isNoLocales()) {
				idsODTsExternas.add(dprss.getIdODT());
			} else {
				idsODTsLocales.add(dprss.getIdODT());
			}
		}
		if(!idsODTsExternas.isEmpty()) {
			result.addAll(marcarODTComoNoLocales(getWSClient().getByIdsEager(idsODTsExternas)));
		}
		result.addAll(odtFacade.getByIdsEager(idsODTsLocales));
		return result;
	}

	private List<OrdenDeTrabajo> marcarODTComoNoLocales(List<OrdenDeTrabajo> odts) {
		for(OrdenDeTrabajo odt : odts) {
			odt.setNoLocal(true);
		}
		return odts;
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
			service = locator.getODTServicePort(new URL(System.getProperty("textillevel.ipintercambio") + System.getProperty("textillevel.odt.wsendpoint")));
		}

		public List<OrdenDeTrabajo> getByIdsEager(List<Integer> ids) throws RemoteException {
			List<OrdenDeTrabajo> odts = new ArrayList<OrdenDeTrabajo>();
			OdtEagerTO[] odtsArr = service.getByIdsEager(ArrayUtils.toPrimitive(ids.toArray(new Integer[ids.size()])));
			if (odtsArr == null || odtsArr.length == 0) {
				return odts;
			}
			for(OdtEagerTO odtEager : odtsArr) {
				odts.add(ODTTOConverter.fromTO(odtEager));
			}
			return odts;
		}

		public List<DetallePiezaRemitoEntradaSinSalida> getInfoPiezasEntradaSinSalidaByClient(Integer idCliente) throws RemoteException {
			List<DetallePiezaRemitoEntradaSinSalida> lista = new ArrayList<DetallePiezaRemitoEntradaSinSalida>();
			ar.com.textillevel.gui.acciones.odtwsclient.DetallePiezaRemitoEntradaSinSalida[] infoPiezasEntradaSinSalidaByClient = service.getInfoPiezasEntradaSinSalidaByClient(idCliente);
			if(infoPiezasEntradaSinSalidaByClient != null) {
				for(ar.com.textillevel.gui.acciones.odtwsclient.DetallePiezaRemitoEntradaSinSalida d : infoPiezasEntradaSinSalidaByClient) {
					lista.add(new DetallePiezaRemitoEntradaSinSalida(d.getNroRemito(), d.getIdODT(), d.getCodigoODT(), d.getProducto(), d.getCantPiezas(), d.getMetrosTotales()));
				}
			}
			return lista;
		}
		
		public boolean recibirRemitoEntrada(RemitoEntradaTO r) throws RemoteException {
			return service.recibirRemitoEntrada(r, GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName());
		}

		public boolean borrarRemitoDeEntrada(Integer idRE) throws RemoteException {
			return service.borrarRemitoDeEntrada(idRE);
		}

		public List<OrdenDeTrabajo> getOrdenesDeTrabajo(EEstadoODT estado, Date desde, Date hasta) throws RemoteException {
			GregorianCalendar gcdesde = new GregorianCalendar();
			gcdesde.setTime(desde);
			GregorianCalendar gchasta = new GregorianCalendar();
			gchasta.setTime(hasta);
			OdtEagerTO[] odtsWS = service.getOrdenesDeTrabajo(estado.getId(), gcdesde, gchasta);
			if(odtsWS == null) {
				return new ArrayList<OrdenDeTrabajo>();
			} 
			List<OrdenDeTrabajo> odts = new ArrayList<OrdenDeTrabajo>(odtsWS.length);
			for(OdtEagerTO odtWS : odtsWS) {
				odts.add(ODTTOConverter.fromTO(odtWS));
			}
			return odts;
		}
	}

	public boolean retornarRemito(DetalleRemitoEntradaNoFacturado elemento) throws RemoteException {
		if (!GenericUtils.isSistemaTest()) {
			throw new RuntimeException("Operacion invalida desde este sistema");
		}
		return getWSClient().recibirRemitoEntrada(ODTTOConverter.toRemitoWSTO(elemento));
	}

	public boolean borrarRemitoDeEntrada(Integer idRE) throws RemoteException {
		if (!GenericUtils.isSistemaTest()) {
			throw new RuntimeException("Operacion invalida desde este sistema");
		}
		return getWSClient().borrarRemitoDeEntrada(idRE);
	}

	public List<OrdenDeTrabajo> getOrdenesDeTrabajos(EEstadoODT estado, Date desde, Date hasta) throws RemoteException {
		if(GenericUtils.isSistemaTest() && estado==EEstadoODT.EN_OFICINA) {
			List<OrdenDeTrabajo> infoPiezas = odtFacade.getOrdenesDeTrabajo(estado, desde, hasta);
			infoPiezas.addAll(marcarODTComoNoLocales(getWSClient().getOrdenesDeTrabajo(estado, desde, hasta)));
			return infoPiezas;
		}
		return odtFacade.getOrdenesDeTrabajo(estado, desde, hasta);
	}

}