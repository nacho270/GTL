package ar.com.textillevel.modulos.terminal.dao.api.local;

import javax.ejb.Local;

import ar.com.fwcommon.dao.api.local.DAOLocal;
import ar.com.textillevel.modulos.terminal.entidades.ModuloTerminal;

@Local
public interface ModuloTerminalDAOLocal extends DAOLocal<ModuloTerminal, Integer> {

}
