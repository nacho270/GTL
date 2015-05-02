package ar.com.textillevel.modulos.personal.facade.api.remote;

import java.util.List;

import javax.ejb.Remote;

import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.Puesto;

@Remote
public interface PuestoFacadeRemote {

	public Puesto save(Puesto puesto);
	public void remove(Puesto puesto);
	public List<Puesto> getAllByIdSindicato(Integer idSindicato);

}
