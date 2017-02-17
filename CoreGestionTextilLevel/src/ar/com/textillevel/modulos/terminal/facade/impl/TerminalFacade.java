package ar.com.textillevel.modulos.terminal.facade.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.com.textillevel.modulos.terminal.dao.api.local.TerminalDAOLocal;
import ar.com.textillevel.modulos.terminal.entidades.Terminal;
import ar.com.textillevel.modulos.terminal.facade.api.remote.TerminalFacadeRemote;

@Stateless
public class TerminalFacade implements TerminalFacadeRemote {

	@EJB
	private TerminalDAOLocal terminalDao;

	@Override
	public Terminal getByIP(final String ip) {
		return terminalDao.getByIP(ip);
	}

	@Override
	public Terminal getById(Integer id) {
		return terminalDao.getById(id);
	}

	@Override
	public Terminal save(Terminal terminal) {
		return terminalDao.save(terminal);
	}

	@Override
	public List<Terminal> getAll() {
		return terminalDao.getAll();
	}

	@Override
	public boolean existeNombre(Integer id, String nombre) {
		return terminalDao.existeNombre(id, nombre);
	}

	@Override
	public boolean existeIp(Integer id, String ip) {
		return terminalDao.existeIp(id, ip);
	}

	@Override
	public boolean existeCodigo(Integer id, String codigo) {
		return terminalDao.existeCodigo(id, codigo);
	}

	@Override
	public void remove(Terminal terminal) {
		terminalDao.removeById(terminal.getId());
	}

}
