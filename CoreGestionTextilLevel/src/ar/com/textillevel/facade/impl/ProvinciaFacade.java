package ar.com.textillevel.facade.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.com.textillevel.dao.api.local.ProvinciaDAOLocal;
import ar.com.textillevel.entidades.gente.Provincia;
import ar.com.textillevel.facade.api.remote.ProvinciaFacadeRemote;

@Stateless
public class ProvinciaFacade implements ProvinciaFacadeRemote{

	@EJB
	private ProvinciaDAOLocal provinciaDao;
	
	public List<Provincia> getAllOrderByName() {
		return provinciaDao.getAllOrderBy("nombre");
	}
}
