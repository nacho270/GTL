package ar.com.textillevel.facade.api.remote;

import java.util.List;

import javax.ejb.Remote;

import ar.com.textillevel.entidades.gente.Provincia;

@Remote
public interface ProvinciaFacadeRemote {
	public List<Provincia> getAllOrderByName();
}
