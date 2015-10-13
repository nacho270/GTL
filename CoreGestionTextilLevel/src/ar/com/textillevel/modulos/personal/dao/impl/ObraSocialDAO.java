package ar.com.textillevel.modulos.personal.dao.impl;

import javax.ejb.Stateless;

import ar.com.fwcommon.dao.impl.GenericDAO;
import ar.com.textillevel.modulos.personal.dao.api.ObraSocialDAOLocal;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.ObraSocial;

@Stateless
public class ObraSocialDAO extends GenericDAO<ObraSocial, Integer> implements ObraSocialDAOLocal {

}
