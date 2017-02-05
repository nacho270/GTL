package ar.com.textillevel.modulos.terminal.facade.api.remote;

import java.util.List;

import javax.ejb.Remote;

import ar.com.textillevel.modulos.terminal.entidades.ModuloTerminal;

@Remote
public interface ModuloTerminalFacadeRemote {

	public List<ModuloTerminal> getAll();
}
