package ar.com.textillevel.facade.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.com.textillevel.dao.api.local.ModuloDAOLocal;
import ar.com.textillevel.entidades.portal.Modulo;
import ar.com.textillevel.facade.api.remote.ModuloFacadeRemote;

@Stateless
public class ModuloFacade implements ModuloFacadeRemote{

	@EJB
	private ModuloDAOLocal moduloDao;

	public List<Modulo> getAllOrderByName() {
		return moduloDao.getAllOrderBy("nombre");
	}

	public List<Modulo> getAllWithActions() {
		return moduloDao.getAllWithActions();
	}

}
