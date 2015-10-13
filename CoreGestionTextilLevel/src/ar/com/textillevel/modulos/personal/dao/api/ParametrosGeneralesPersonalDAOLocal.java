package ar.com.textillevel.modulos.personal.dao.api;

import javax.ejb.Local;

import ar.com.fwcommon.dao.api.local.DAOLocal;
import ar.com.textillevel.modulos.personal.entidades.configuracion.ParametrosGeneralesPersonal;

@Local
public interface ParametrosGeneralesPersonalDAOLocal extends DAOLocal<ParametrosGeneralesPersonal, Integer>{
	public ParametrosGeneralesPersonal getParametrosGenerales();
}
