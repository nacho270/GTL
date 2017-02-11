package ar.com.textillevel.modulos.terminal.facade.api.remote;

import java.util.List;

import javax.ejb.Remote;

import ar.com.textillevel.modulos.terminal.entidades.Terminal;

@Remote
public interface TerminalFacadeRemote {

	public Terminal getByIP(final String ip);
	public Terminal getById(Integer id);
	public Terminal save(Terminal terminalActual);
	public List<Terminal> getAll();
	public boolean existeNombre(Integer id, String nombre);
	public boolean existeIp(Integer id, String ip);
	public boolean existeCodigo(Integer id, String codigo);
}
