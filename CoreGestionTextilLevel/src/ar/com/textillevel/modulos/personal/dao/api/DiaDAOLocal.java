package ar.com.textillevel.modulos.personal.dao.api;

import javax.ejb.Local;

import ar.com.fwcommon.dao.api.local.DAOLocal;
import ar.com.fwcommon.entidades.Dia;

@Local
public interface DiaDAOLocal extends DAOLocal<Dia, Integer>{

}
