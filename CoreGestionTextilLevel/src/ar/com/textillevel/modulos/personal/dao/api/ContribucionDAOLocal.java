package ar.com.textillevel.modulos.personal.dao.api;

import java.util.List;

import javax.ejb.Local;

import ar.com.fwcommon.dao.api.local.DAOLocal;
import ar.com.textillevel.modulos.personal.entidades.contribuciones.Contribucion;

@Local
public interface ContribucionDAOLocal extends DAOLocal<Contribucion, Integer> {

	public List<Contribucion> getAllByIdSindicato(Integer idSindicato);

	public Contribucion getByIdEager(Integer id);

}
