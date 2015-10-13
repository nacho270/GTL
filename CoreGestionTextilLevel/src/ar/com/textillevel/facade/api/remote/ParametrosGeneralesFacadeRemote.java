package ar.com.textillevel.facade.api.remote;

import javax.ejb.Remote;

import ar.com.fwcommon.componentes.error.FWException;
import ar.com.textillevel.entidades.config.ParametrosGenerales;

@Remote
public interface ParametrosGeneralesFacadeRemote {
	public ParametrosGenerales getParametrosGenerales();
	public ParametrosGenerales save(ParametrosGenerales parametrosGenerales) throws FWException;
}
