package ar.com.textillevel.modulos.personal.facade.api.remote;

import java.util.List;

import javax.ejb.Remote;

import ar.com.textillevel.modulos.personal.entidades.contribuciones.Contribucion;

@Remote
public interface ContribucionFacadeRemote {

	public void remove(Contribucion contribucion);

	public List<Contribucion> getAllByIdSindicato(Integer idSindicato);

	public Contribucion save(Contribucion contribucion);

	public Contribucion getByIdEager(Integer id);

}
