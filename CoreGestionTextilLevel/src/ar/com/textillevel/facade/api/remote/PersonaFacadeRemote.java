package ar.com.textillevel.facade.api.remote;

import java.util.List;

import javax.ejb.Remote;

import ar.com.textillevel.entidades.gente.Persona;

@Remote
public interface PersonaFacadeRemote {

	public abstract List<Persona> getAllOrderByName();

	public abstract Persona save(Persona persona);

	public abstract void remove(Persona persona);

	public abstract List<Persona> getByApellido(String apellido);

}
