package ar.com.textillevel.dao.api.local;

import java.util.List;

import javax.ejb.Local;

import ar.clarin.fwjava.dao.api.local.DAOLocal;
import ar.com.textillevel.entidades.gente.Persona;
import ar.com.textillevel.entidades.gente.Rubro;

@Local
public interface PersonaDAOLocal extends DAOLocal<Persona, Integer> {
	public List<Persona> getAllOrderByName();
	public List<Persona> getAllByRubroOrderByName(Rubro rubro);
	public List<Persona> getByNombreOApellido(String criterio);
	public List<Persona> getByNombreOApellidoYRubro(String criterio, Rubro rubro);
}
