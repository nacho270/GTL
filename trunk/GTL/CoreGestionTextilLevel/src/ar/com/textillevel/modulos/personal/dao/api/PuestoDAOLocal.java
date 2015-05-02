package ar.com.textillevel.modulos.personal.dao.api;

import java.util.List;

import javax.ejb.Local;

import ar.clarin.fwjava.dao.api.local.DAOLocal;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.Puesto;

@Local
public interface PuestoDAOLocal extends DAOLocal<Puesto, Integer> {

	public List<Puesto> getAllByIdSindicato(Integer idSindicato);

}
