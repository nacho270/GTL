package ar.com.textillevel.modulos.personal.dao.impl;

import javax.ejb.Stateless;

import ar.clarin.fwjava.dao.impl.GenericDAO;
import ar.clarin.fwjava.entidades.Mes;
import ar.com.textillevel.modulos.personal.dao.api.MesDAOLocal;

@Stateless
public class MesDAO extends GenericDAO<Mes, Integer> implements MesDAOLocal {

}
