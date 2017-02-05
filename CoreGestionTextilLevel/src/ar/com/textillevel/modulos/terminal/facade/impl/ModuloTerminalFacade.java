package ar.com.textillevel.modulos.terminal.facade.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.com.textillevel.modulos.terminal.dao.api.local.ModuloTerminalDAOLocal;
import ar.com.textillevel.modulos.terminal.entidades.ModuloTerminal;
import ar.com.textillevel.modulos.terminal.facade.api.remote.ModuloTerminalFacadeRemote;

@Stateless
public class ModuloTerminalFacade implements ModuloTerminalFacadeRemote {

	@EJB
	private ModuloTerminalDAOLocal moduloTerminalDao;

	@Override
	public List<ModuloTerminal> getAll() {
		return moduloTerminalDao.getAll();
	}
}
