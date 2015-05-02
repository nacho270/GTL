package ar.com.textillevel.facade.api.remote;

import java.util.List;

import javax.ejb.Remote;

import ar.clarin.fwjava.componentes.error.validaciones.ValidacionException;
import ar.com.textillevel.entidades.portal.Perfil;

@Remote
public interface PerfilFacadeRemote {
	public List<Perfil> getAllOrderByName();
	public Perfil save(Perfil perfil) throws ValidacionException;
	public void remove(Perfil perfil); 
}
