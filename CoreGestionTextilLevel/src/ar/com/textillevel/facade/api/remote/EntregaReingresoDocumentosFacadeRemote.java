package ar.com.textillevel.facade.api.remote;

import javax.ejb.Remote;

import ar.com.textillevel.entidades.to.TerminalServiceResponse;

@Remote
public interface EntregaReingresoDocumentosFacadeRemote {

	public TerminalServiceResponse marcarEntregado(String codigo, String nombreTerminal);
	public TerminalServiceResponse reingresar(String codigo, String nombreTerminal);
}
