package ar.com.textillevel.facade.api.remote;

import java.util.List;

import javax.ejb.Remote;

import ar.com.textillevel.entidades.portal.Modulo;

@Remote
public interface ModuloFacadeRemote {
	
	public List<Modulo> getAllOrderByName();
	public List<Modulo> getAllWithActions();
	
}
