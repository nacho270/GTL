package ar.com.textillevel.dao.api.local;

import javax.ejb.Local;

import ar.com.fwcommon.dao.api.local.DAOLocal;
import ar.com.textillevel.entidades.gente.Provincia;

@Local
public interface ProvinciaDAOLocal extends DAOLocal<Provincia, Integer>{

}
