package ar.com.textillevel.facade.api.remote;

import java.util.List;

import javax.ejb.Remote;

import ar.com.textillevel.entidades.gente.InfoLocalidad;

@Remote
public interface InfoLocalidadFacadeRemote {
	public List<InfoLocalidad> getAllInfoLocalidad();
	public InfoLocalidad guardarInfoLocalidad(InfoLocalidad infoLocalidad);
	public void remove(InfoLocalidad infoLocalidad);
}
