package ar.com.textillevel.modulos.terminal.dao.api.local;

import javax.ejb.Local;

import ar.com.fwcommon.dao.api.local.DAOLocal;
import ar.com.textillevel.modulos.terminal.entidades.Terminal;

@Local
public interface TerminalDAOLocal extends DAOLocal<Terminal, Integer> {

	public Terminal getByIP(final String ip);
	public boolean existeNombre(Integer id, String nombre);
	public boolean existeIp(Integer id, String ip);

}
