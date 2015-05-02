package ar.com.textillevel.modulos.personal.facade.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.com.textillevel.modulos.personal.dao.api.NacionalidadDAOLocal;
import ar.com.textillevel.modulos.personal.entidades.legajos.Nacionalidad;
import ar.com.textillevel.modulos.personal.facade.api.remote.NacionalidadFacadeRemote;

@Stateless
public class NacionalidadFacade implements NacionalidadFacadeRemote{

	@EJB
	private NacionalidadDAOLocal nacionalidadDao;
	
	public Nacionalidad save(Nacionalidad nacionalidad) {
		return nacionalidadDao.save(nacionalidad);
	}

	public List<Nacionalidad> getAllOrderByName() {
		return nacionalidadDao.getAllOrderBy("nombre");
	}
}
