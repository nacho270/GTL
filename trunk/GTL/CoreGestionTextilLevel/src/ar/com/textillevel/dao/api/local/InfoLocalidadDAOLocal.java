package ar.com.textillevel.dao.api.local;

import javax.ejb.Local;

import ar.clarin.fwjava.dao.api.local.DAOLocal;
import ar.com.textillevel.entidades.gente.InfoLocalidad;

@Local
public interface InfoLocalidadDAOLocal extends DAOLocal<InfoLocalidad, Integer>{

}
