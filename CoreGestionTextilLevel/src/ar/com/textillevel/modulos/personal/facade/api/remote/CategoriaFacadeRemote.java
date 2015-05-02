package ar.com.textillevel.modulos.personal.facade.api.remote;

import java.util.List;

import javax.ejb.Remote;

import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.Categoria;

@Remote
public interface CategoriaFacadeRemote {

	public Categoria save(Categoria categoria);
	public void remove(Categoria categoria);
	public List<Categoria> getAllByIdSindicato(Integer idSindicato);
	public Categoria getByIdEager(Integer id);
	public List<Categoria> getCategoriasPorPuesto(Integer idPuesto);
	public List<Categoria> getAllByIdSindicatoEager(Integer idSindicato);
}
