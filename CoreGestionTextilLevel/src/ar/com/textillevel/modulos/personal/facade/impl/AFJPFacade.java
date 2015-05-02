package ar.com.textillevel.modulos.personal.facade.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.com.textillevel.modulos.personal.dao.api.AFJPDAOLocal;
import ar.com.textillevel.modulos.personal.entidades.commons.AFJP;
import ar.com.textillevel.modulos.personal.facade.api.remote.AFJPFacadeRemote;

@Stateless
public class AFJPFacade implements AFJPFacadeRemote{

	@EJB
	private AFJPDAOLocal afjpDao;
	
	public AFJP save(AFJP afjp) {
		return afjpDao.save(afjp);
	}

	public List<AFJP> getAllOrderByName() {
		return afjpDao.getAllOrderBy("nombre");
	}
}
