package ar.com.textillevel.modulos.personal.facade.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.com.textillevel.modulos.personal.dao.api.ArtDAOLocal;
import ar.com.textillevel.modulos.personal.entidades.commons.Art;
import ar.com.textillevel.modulos.personal.facade.api.remote.ArtFacadeRemote;

@Stateless
public class ArtFacade implements ArtFacadeRemote{

	@EJB
	private ArtDAOLocal artDao;
	
	public Art save(Art art) {
		return artDao.save(art);
	}

	public void remove(Art art) {
		artDao.removeById(art.getId());
	}

	public List<Art> getAllOrderByName() {
		return artDao.getAllOrderBy("nombre");
	}
}
