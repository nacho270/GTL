package ar.com.textillevel.modulos.personal.facade.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.com.textillevel.modulos.personal.dao.api.PuestoDAOLocal;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.Puesto;
import ar.com.textillevel.modulos.personal.facade.api.remote.PuestoFacadeRemote;

@Stateless
public class PuestoFacade implements PuestoFacadeRemote {

	@EJB
	private PuestoDAOLocal puestoDAO;

	public Puesto save(Puesto puesto) {
		return puestoDAO.save(puesto);
	}

	public void remove(Puesto puesto) {
		puestoDAO.removeById(puesto.getId());
	}

	public List<Puesto> getAllByIdSindicato(Integer idSindicato) {
		return puestoDAO.getAllByIdSindicato(idSindicato);
	}

}
