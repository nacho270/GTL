package ar.com.textillevel.facade.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.com.textillevel.dao.api.local.PersonaDAOLocal;
import ar.com.textillevel.entidades.gente.Persona;
import ar.com.textillevel.facade.api.remote.PersonaFacadeRemote;

@Stateless
public class PersonaFacade implements PersonaFacadeRemote {

	@EJB
	private PersonaDAOLocal personaDAOLocal;
	
	public List<Persona> getAllOrderByName() {
		return personaDAOLocal.getAllOrderBy("apellido");
	}

	public void remove(Persona persona) {
		personaDAOLocal.removeById(persona.getId());
	}

	public Persona save(Persona persona) {
		return personaDAOLocal.save(persona);
	}

	public List<Persona> getByApellido(String apellido) {
		return personaDAOLocal.getByNombreOApellido(apellido);
	}

}