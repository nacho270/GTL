package ar.com.textillevel.modulos.personal.dao.api;

import javax.ejb.Local;

import ar.clarin.fwjava.dao.api.local.DAOLocal;
import ar.clarin.fwjava.entidades.Mes;

@Local
public interface MesDAOLocal extends DAOLocal<Mes, Integer>{

}
