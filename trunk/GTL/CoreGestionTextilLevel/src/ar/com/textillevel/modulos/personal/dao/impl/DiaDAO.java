package ar.com.textillevel.modulos.personal.dao.impl;

import javax.ejb.Stateless;

import ar.clarin.fwjava.dao.impl.GenericDAO;
import ar.clarin.fwjava.entidades.Dia;
import ar.com.textillevel.modulos.personal.dao.api.DiaDAOLocal;

@Stateless
public class DiaDAO extends GenericDAO<Dia, Integer> implements DiaDAOLocal{

}
