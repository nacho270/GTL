package ar.com.textillevel.dao.api.local;

import java.util.List;

import javax.ejb.Local;

import ar.clarin.fwjava.dao.api.local.DAOLocal;
import ar.com.textillevel.entidades.portal.Modulo;

@Local
public interface ModuloDAOLocal extends DAOLocal<Modulo, Integer>{

	public List<Modulo> getAllWithActions();

}
