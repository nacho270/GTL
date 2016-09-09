package ar.com.textillevel.webservices.terminal.api.remote;

import javax.ejb.Remote;

import ar.com.textillevel.webservices.terminal.impl.TerminalService.TerminalServiceResponse;

@Remote
public interface TerminalServiceRemote {

	public TerminalServiceResponse marcarEntregado(String codigo, String nombreTerminal);
	public TerminalServiceResponse reingresar(String codigo, String nombreTerminal);
}
