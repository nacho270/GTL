package ar.com.textillevel.modulos.personal.facade.api.local;

import javax.ejb.Local;

import ar.com.textillevel.modulos.personal.entidades.configuracion.ParametrosGeneralesPersonal;

@Local
public interface ParametrosGeneralesPersonalFacadeLocal {

	public ParametrosGeneralesPersonal getParametrosGenerales();
}
