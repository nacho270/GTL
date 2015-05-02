package ar.com.textillevel.modulos.personal.facade.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.com.textillevel.modulos.personal.dao.api.ObraSocialDAOLocal;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.ObraSocial;
import ar.com.textillevel.modulos.personal.facade.api.remote.ObraSocialFacadeRemote;

@Stateless
public class ObraSocialFacade implements ObraSocialFacadeRemote {

	@EJB
	private ObraSocialDAOLocal obraSocialDAO;

	public ObraSocial save(ObraSocial obraSocial) {
		return obraSocialDAO.save(obraSocial);
	}

	public void remove(ObraSocial obraSocial) {
		obraSocialDAO.removeById(obraSocial.getId());
	}

	public List<ObraSocial> getAllOrderByName() {
		return obraSocialDAO.getAllOrderBy("nombre");
	}

}
