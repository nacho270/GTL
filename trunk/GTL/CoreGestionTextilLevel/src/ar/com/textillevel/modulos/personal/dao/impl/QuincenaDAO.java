package ar.com.textillevel.modulos.personal.dao.impl;

import javax.ejb.Stateless;

import ar.clarin.fwjava.dao.impl.GenericDAO;
import ar.com.textillevel.modulos.personal.dao.api.QuincenaDAOLocal;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.Quincena;

@Stateless
public class QuincenaDAO extends GenericDAO<Quincena, Integer> implements QuincenaDAOLocal {

}
