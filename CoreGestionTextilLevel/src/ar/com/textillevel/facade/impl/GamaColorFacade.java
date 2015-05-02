package ar.com.textillevel.facade.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.com.textillevel.dao.api.local.GamaColorDAOLocal;
import ar.com.textillevel.entidades.ventas.articulos.GamaColor;
import ar.com.textillevel.facade.api.remote.GamaColorFacadeRemote;

@Stateless
public class GamaColorFacade implements GamaColorFacadeRemote{

	@EJB
	private GamaColorDAOLocal gamaDao;
	
	public List<GamaColor> getAllOrderByName() {
		return gamaDao.getAllOrderBy("nombre");
	}

	public void remove(GamaColor gama) {
		gamaDao.removeById(gama.getId());
	}

	public GamaColor save(GamaColor gama) {
		return gamaDao.save(gama);
	}
}
