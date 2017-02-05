package ar.com.textillevel.facade.api.local;

import javax.ejb.Local;

import ar.com.textillevel.entidades.to.TerminalServiceResponse;

@Local
public interface EntregaReingresoDocumentosFacadeLocal {

	public TerminalServiceResponse marcarEntregado(String codigo, String nombreTerminal);
	public TerminalServiceResponse reingresar(String codigo, String nombreTerminal);
}
