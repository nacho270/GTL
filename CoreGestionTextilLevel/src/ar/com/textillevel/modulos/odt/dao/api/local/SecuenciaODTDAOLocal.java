package ar.com.textillevel.modulos.odt.dao.api.local;

import javax.ejb.Local;

import ar.clarin.fwjava.dao.api.local.DAOLocal;
import ar.com.textillevel.modulos.odt.entidades.secuencia.odt.SecuenciaODT;

@Local
public interface SecuenciaODTDAOLocal extends DAOLocal<SecuenciaODT, Integer>{

}
