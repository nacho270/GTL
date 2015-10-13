package ar.com.textillevel.modulos.personal.dao.impl;

import javax.ejb.Stateless;

import ar.com.fwcommon.dao.impl.GenericDAO;
import ar.com.textillevel.modulos.personal.dao.api.SindicatoDAOLocal;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.Sindicato;

@Stateless
public class SindicatoDAO extends GenericDAO<Sindicato, Integer> implements SindicatoDAOLocal {

}
