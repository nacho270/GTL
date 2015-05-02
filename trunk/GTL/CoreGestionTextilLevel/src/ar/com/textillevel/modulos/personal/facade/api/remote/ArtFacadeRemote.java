package ar.com.textillevel.modulos.personal.facade.api.remote;

import java.util.List;

import javax.ejb.Remote;

import ar.com.textillevel.modulos.personal.entidades.commons.Art;

@Remote
public interface ArtFacadeRemote {
	public Art save(Art art);
	public void remove(Art art);
	public List<Art> getAllOrderByName();
}
