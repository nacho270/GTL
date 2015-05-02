package ar.com.textillevel.modulos.personal.facade.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.com.textillevel.modulos.personal.dao.api.CategoriaDAOLocal;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.Categoria;
import ar.com.textillevel.modulos.personal.facade.api.remote.CategoriaFacadeRemote;

@Stateless
public class CategoriaFacade implements CategoriaFacadeRemote {

	@EJB
	private CategoriaDAOLocal categoriaDAO;

	public Categoria save(Categoria categoria) {
		return categoriaDAO.save(categoria);
	}

	public void remove(Categoria categoria) {
		categoriaDAO.removeById(categoria.getId());
	}

	public List<Categoria> getAllByIdSindicato(Integer idSindicato) {
		return categoriaDAO.getAllByIdSindicato(idSindicato);
	}

	public Categoria getByIdEager(Integer id) {
		return categoriaDAO.getByIdEager(id);
	}

	public List<Categoria> getCategoriasPorPuesto(Integer idPuesto) {
		return categoriaDAO.getCategoriasPorPuesto(idPuesto);
	}

	public List<Categoria> getAllByIdSindicatoEager(Integer idSindicato) {
		return categoriaDAO.getAllByIdSindicatoEager(idSindicato);
	}

}
