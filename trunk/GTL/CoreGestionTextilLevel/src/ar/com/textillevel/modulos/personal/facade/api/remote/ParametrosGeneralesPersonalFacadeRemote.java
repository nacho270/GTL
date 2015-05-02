package ar.com.textillevel.modulos.personal.facade.api.remote;

import javax.ejb.Remote;

import ar.clarin.fwjava.componentes.error.CLException;
import ar.com.textillevel.modulos.personal.entidades.configuracion.ParametrosGeneralesPersonal;

@Remote
public interface ParametrosGeneralesPersonalFacadeRemote {
	public ParametrosGeneralesPersonal getParametrosGenerales();
	public ParametrosGeneralesPersonal save(ParametrosGeneralesPersonal parametrosGenerales) throws CLException;
}
