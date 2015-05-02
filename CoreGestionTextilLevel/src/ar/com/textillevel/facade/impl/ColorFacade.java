package ar.com.textillevel.facade.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.com.textillevel.dao.api.local.ColorDAOLocal;
import ar.com.textillevel.entidades.ventas.articulos.Color;
import ar.com.textillevel.facade.api.remote.ColorFacadeRemote;

@Stateless
public class ColorFacade implements ColorFacadeRemote{

	@EJB
	private ColorDAOLocal colorDao;
	
	public List<Color> getAllOrderByName() {
		return colorDao.getAllOrderBy("nombre");
	}

	public void remove(Color color) {
		colorDao.removeById(color.getId());
	}

	public Color save(Color color) {
		return colorDao.save(color);
	}

	public List<Color> getAllOrderByNameGamaEager() {
		return colorDao.getAllOrderByNameGamaEager();
	}
}
