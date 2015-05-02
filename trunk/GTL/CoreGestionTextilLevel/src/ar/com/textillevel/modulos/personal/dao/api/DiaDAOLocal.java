package ar.com.textillevel.modulos.personal.dao.api;

import javax.ejb.Local;

import ar.clarin.fwjava.dao.api.local.DAOLocal;
import ar.clarin.fwjava.entidades.Dia;

@Local
public interface DiaDAOLocal extends DAOLocal<Dia, Integer>{

}
