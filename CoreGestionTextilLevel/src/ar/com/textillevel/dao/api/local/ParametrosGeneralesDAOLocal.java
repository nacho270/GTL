package ar.com.textillevel.dao.api.local;

import javax.ejb.Local;
import ar.clarin.fwjava.dao.api.local.DAOLocal;
import ar.com.textillevel.entidades.config.ParametrosGenerales;

@Local
public interface ParametrosGeneralesDAOLocal extends DAOLocal<ParametrosGenerales, Integer> {

	public ParametrosGenerales getParametrosGenerales();

}
