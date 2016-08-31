package ar.com.textillevel.webservices.terminal.api.remote;

import javax.ejb.Remote;

@Remote
public interface TerminalServiceRemote {

	public void marcarEntregado(String codigo);
	public void reingresar(String codigo);
}
