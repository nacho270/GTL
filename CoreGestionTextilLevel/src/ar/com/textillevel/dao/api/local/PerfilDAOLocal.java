package ar.com.textillevel.dao.api.local;

import javax.ejb.Local;

import ar.com.fwcommon.dao.api.local.DAOLocal;
import ar.com.textillevel.entidades.portal.Perfil;

@Local
public interface PerfilDAOLocal extends DAOLocal<Perfil, Integer>{

	boolean yaHayPerfilAdministrador();

}
