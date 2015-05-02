package ar.com.textillevel.modulos.personal.facade.api.remote;

import java.util.List;

import javax.ejb.Remote;

import ar.com.textillevel.modulos.personal.entidades.legajos.Nacionalidad;

@Remote
public interface NacionalidadFacadeRemote {
	public Nacionalidad save(Nacionalidad nacionalidad);
	public List<Nacionalidad> getAllOrderByName();
}
