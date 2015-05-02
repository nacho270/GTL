package ar.com.textillevel.facade.api.local;

import javax.ejb.Local;

import ar.com.textillevel.entidades.config.ParametrosGenerales;

@Local
public interface ParametrosGeneralesFacadeLocal {
	public ParametrosGenerales getParametrosGenerales();
}
