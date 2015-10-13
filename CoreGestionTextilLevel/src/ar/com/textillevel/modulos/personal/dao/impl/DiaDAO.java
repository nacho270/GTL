package ar.com.textillevel.modulos.personal.dao.impl;

import javax.ejb.Stateless;

import ar.com.fwcommon.dao.impl.GenericDAO;
import ar.com.fwcommon.entidades.Dia;
import ar.com.textillevel.modulos.personal.dao.api.DiaDAOLocal;

@Stateless
public class DiaDAO extends GenericDAO<Dia, Integer> implements DiaDAOLocal{

}
