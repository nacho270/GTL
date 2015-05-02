package ar.com.textillevel.modulos.personal.facade.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.com.textillevel.modulos.personal.dao.api.SindicatoDAOLocal;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.Sindicato;
import ar.com.textillevel.modulos.personal.facade.api.remote.SindicatoFacadeRemote;

@Stateless
public class SindicatoFacade implements SindicatoFacadeRemote {

	@EJB
	private SindicatoDAOLocal sindicatoDAO;

	public Sindicato save(Sindicato sindicato) {
		return sindicatoDAO.save(sindicato);
	}

	public void remove(Sindicato sindicato) {
		sindicatoDAO.removeById(sindicato.getId());
	}

	public List<Sindicato> getAllOrderByName() {
		return sindicatoDAO.getAllOrderBy("nombre");
	}

}
