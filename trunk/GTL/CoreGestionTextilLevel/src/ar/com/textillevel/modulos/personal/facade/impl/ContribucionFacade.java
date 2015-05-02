package ar.com.textillevel.modulos.personal.facade.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.com.textillevel.modulos.personal.dao.api.ContribucionDAOLocal;
import ar.com.textillevel.modulos.personal.entidades.contribuciones.Contribucion;
import ar.com.textillevel.modulos.personal.facade.api.remote.ContribucionFacadeRemote;

@Stateless
public class ContribucionFacade implements ContribucionFacadeRemote {

	@EJB
	private ContribucionDAOLocal contribucionDAO;
	
	public void remove(Contribucion contribucion) {
		contribucionDAO.removeById(contribucion.getId());
	}

	public List<Contribucion> getAllByIdSindicato(Integer idSindicato) {
		return contribucionDAO.getAllByIdSindicato(idSindicato);
	}

	public Contribucion save(Contribucion contribucion) {
		return contribucionDAO.save(contribucion);
	}

	public Contribucion getByIdEager(Integer id) {
		return contribucionDAO.getByIdEager(id);
	}

}
