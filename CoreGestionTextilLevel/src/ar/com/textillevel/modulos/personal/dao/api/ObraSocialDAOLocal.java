package ar.com.textillevel.modulos.personal.dao.api;

import javax.ejb.Local;

import ar.com.fwcommon.dao.api.local.DAOLocal;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.ObraSocial;

@Local
public interface ObraSocialDAOLocal extends DAOLocal<ObraSocial, Integer> {

}
