package ar.com.textillevel.facade.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.com.textillevel.dao.api.local.InfoLocalidadDAOLocal;
import ar.com.textillevel.entidades.gente.InfoLocalidad;
import ar.com.textillevel.facade.api.remote.InfoLocalidadFacadeRemote;

@Stateless
public class InfoLocalidadFacade implements InfoLocalidadFacadeRemote {

	@EJB
	private InfoLocalidadDAOLocal infoLocalidadDao;

	public List<InfoLocalidad> getAllInfoLocalidad() {
		return infoLocalidadDao.getAllOrderBy("nombreLocalidad");
	}

	public InfoLocalidad guardarInfoLocalidad(InfoLocalidad infoLocalidad) {
		return infoLocalidadDao.save(infoLocalidad);
	}

	public void remove(InfoLocalidad infoLocalidad) {
		infoLocalidadDao.removeById(infoLocalidad.getId());
	}
}
