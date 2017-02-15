package ar.com.textillevel.gui.acciones;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import javax.xml.rpc.ServiceException;
import ar.com.textillevel.facade.api.remote.EntregaReingresoDocumentosFacadeRemote;
import ar.com.textillevel.gui.acciones.entregreingdocwsclient.TerminalService;
import ar.com.textillevel.gui.acciones.entregreingdocwsclient.TerminalServiceResponse;
import ar.com.textillevel.gui.acciones.entregreingdocwsclient.TerminalServiceServiceLocator;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.util.GTLBeanFactory;
import main.GTLGlobalCache;

public class EntregaReingresoDocumentosBusinessDelegate {

	private EntregaReingresoDocumentosFacadeRemote entReingDocFacade = GTLBeanFactory.getInstance().getBean2(EntregaReingresoDocumentosFacadeRemote.class);
	private TerminalServiceClient wsClient;

	private TerminalServiceClient getWSClient() {
		if (wsClient == null) {
			try {
				wsClient = new TerminalServiceClient();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return wsClient;
	}

	public TerminalServiceResponse marcarEntregado(String codigo) throws RemoteException {
		String usrName = GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName();
		if(GenericUtils.isSistemaTest()) {//estoy en la B
			if(docEsDeSistemaA(codigo)) {//el doc es de la A => voy por WS
				return getWSClient().marcarEntregado(codigo);
			} else {//doc es de la B => llamo localmente
				return toTerminalServiceResponseWS(entReingDocFacade.marcarEntregado(codigo, usrName));
			}
		} else {//estoy en la A
			if(docEsDeSistemaA(codigo)) {//el doc es de la A => llamo localmente
				return toTerminalServiceResponseWS(entReingDocFacade.marcarEntregado(codigo, usrName));
			} else {//el doc es de la B => voy por WS
				return getWSClient().marcarEntregado(codigo);
			}
		}
	}

	public TerminalServiceResponse reingresar(String codigo) throws RemoteException {
		String usrName = GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName();
		if(GenericUtils.isSistemaTest()) {//estoy en la B
			if(docEsDeSistemaA(codigo)) {//el doc es de la A => voy por WS
				return getWSClient().reingresar(codigo);
			} else {//doc es de la B => llamo localmente
				return toTerminalServiceResponseWS(entReingDocFacade.reingresar(codigo, usrName));
			}
		} else {//estoy en la A
			if(docEsDeSistemaA(codigo)) {//el doc es de la A => llamo localmente
				return toTerminalServiceResponseWS(entReingDocFacade.reingresar(codigo, usrName));
			} else {//el doc es de la B => voy por WS
				return getWSClient().reingresar(codigo);
			}
		}
	}

	private boolean docEsDeSistemaA(String codigo) {
		return codigo.endsWith("0");
	}
	
	private TerminalServiceResponse toTerminalServiceResponseWS(ar.com.textillevel.entidades.to.TerminalServiceResponse tsrLocal) {
		TerminalServiceResponse resp = new TerminalServiceResponse();
		resp.setCodigoError(tsrLocal.getCodigoError());
		resp.setError(tsrLocal.isError());
		resp.setMensajeError(tsrLocal.getMensajeError());
		return resp;
	}

	private class TerminalServiceClient {

	    private  TerminalService service;

	    public TerminalServiceClient() throws MalformedURLException, ServiceException {
	    	TerminalServiceServiceLocator locator = new TerminalServiceServiceLocator();
	    	service = locator.getTerminalServicePort(new URL(System.getProperty("textillevel.ipintercambio") + System.getProperty("textillevel.entregingdoc.wsendpoint")));
	    }

	    public  TerminalServiceResponse marcarEntregado(final String codigo) throws RemoteException {
            return service.marcarEntregado(codigo, GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName());
	    }

	    public  TerminalServiceResponse reingresar(final String codigo) throws RemoteException {
            return service.reingresar(codigo, GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName());
	    }

	}

}