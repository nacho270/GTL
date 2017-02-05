package ar.com.textillevel.modulos.terminal.dao.impl;

import javax.ejb.Stateless;

import ar.com.fwcommon.dao.impl.GenericDAO;
import ar.com.textillevel.modulos.terminal.dao.api.local.ModuloTerminalDAOLocal;
import ar.com.textillevel.modulos.terminal.entidades.ModuloTerminal;

@Stateless
public class ModuloTerminalDAO extends GenericDAO<ModuloTerminal, Integer> implements ModuloTerminalDAOLocal {

}
