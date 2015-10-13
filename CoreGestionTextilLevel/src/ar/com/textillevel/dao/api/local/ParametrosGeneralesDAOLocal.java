package ar.com.textillevel.dao.api.local;

import javax.ejb.Local;

import ar.com.fwcommon.dao.api.local.DAOLocal;
import ar.com.textillevel.entidades.config.ParametrosGenerales;

@Local
public interface ParametrosGeneralesDAOLocal extends DAOLocal<ParametrosGenerales, Integer> {

	public ParametrosGenerales getParametrosGenerales();

}
