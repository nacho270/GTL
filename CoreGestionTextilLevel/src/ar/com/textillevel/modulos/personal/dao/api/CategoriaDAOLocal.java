package ar.com.textillevel.modulos.personal.dao.api;

import java.util.List;

import javax.ejb.Local;

import ar.com.fwcommon.dao.api.local.DAOLocal;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.Categoria;

@Local
public interface CategoriaDAOLocal extends DAOLocal<Categoria, Integer> {

	public List<Categoria> getAllByIdSindicato(Integer idSindicato);

	public Categoria getByIdEager(Integer id);

	public List<Categoria> getCategoriasPorPuesto(Integer idPuesto);

	public List<Categoria> getAllByIdSindicatoEager(Integer idSindicato);

}
