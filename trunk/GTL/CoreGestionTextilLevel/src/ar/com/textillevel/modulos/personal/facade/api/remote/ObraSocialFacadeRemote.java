package ar.com.textillevel.modulos.personal.facade.api.remote;

import java.util.List;

import javax.ejb.Remote;

import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.ObraSocial;

@Remote
public interface ObraSocialFacadeRemote {
	
	public ObraSocial save(ObraSocial obraSocial);
	public void remove(ObraSocial obraSocial);
	public List<ObraSocial> getAllOrderByName();
}
