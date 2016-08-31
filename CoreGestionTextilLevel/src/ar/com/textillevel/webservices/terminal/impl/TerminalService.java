package ar.com.textillevel.webservices.terminal.impl;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebService;

import ar.com.textillevel.facade.api.local.OrdenDePagoFacadeLocal;
import ar.com.textillevel.facade.api.local.RemitoSalidaFacadeLocal;
import ar.com.textillevel.webservices.terminal.api.remote.TerminalServiceRemote;

@Stateless
@WebService
public class TerminalService implements TerminalServiceRemote {

	@EJB
	private OrdenDePagoFacadeLocal odpFacade;
	
	@EJB
	private RemitoSalidaFacadeLocal rsFacade;

	//URL: http://localhost:8080/GTL-gtlback-server/TerminalService?wsdl
	
	@Override
	public void marcarEntregado(String codigo) {
		try {
			Thread.sleep(2000);
		}catch(Exception e) {
			
		}
	}

	@Override
	public void reingresar(String codigo) {
		try {
			Thread.sleep(2000);
		}catch(Exception e) {
			
		}
	}
}
