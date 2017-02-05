package ar.com.textillevel.webservices.terminal.impl;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebService;

import ar.com.textillevel.entidades.to.TerminalServiceResponse;
import ar.com.textillevel.facade.api.local.EntregaReingresoDocumentosFacadeLocal;
import ar.com.textillevel.webservices.terminal.api.remote.TerminalServiceRemote;

@Stateless
@WebService
public class TerminalService implements TerminalServiceRemote {

	@EJB
	private EntregaReingresoDocumentosFacadeLocal entregaReingresoFacade;

	// URL: http://localhost:8080/GTL-gtlback-server/TerminalService?wsdl

	public TerminalServiceResponse marcarEntregado(String codigo, String nombreTerminal) {
		return entregaReingresoFacade.marcarEntregado(codigo, nombreTerminal);
	}

	public TerminalServiceResponse reingresar(String codigo, String nombreTerminal) {
		return entregaReingresoFacade.reingresar(codigo, nombreTerminal);
	}

}
