package ar.com.textillevel.modulos.personal.dao.api;

import javax.ejb.Local;

import ar.clarin.fwjava.dao.api.local.DAOLocal;
import ar.com.textillevel.modulos.personal.entidades.legajos.Nacionalidad;

@Local
public interface NacionalidadDAOLocal extends DAOLocal<Nacionalidad, Integer>{

}
