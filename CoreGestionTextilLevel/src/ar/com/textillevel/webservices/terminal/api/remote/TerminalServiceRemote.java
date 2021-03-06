package ar.com.textillevel.webservices.terminal.api.remote;

import javax.ejb.Remote;

import ar.com.textillevel.entidades.to.TerminalServiceResponse;

@Remote
public interface TerminalServiceRemote {

	public TerminalServiceResponse marcarEntregado(String codigo, String nombreTerminal);
	public TerminalServiceResponse reingresar(String codigo, String nombreTerminal);
}
