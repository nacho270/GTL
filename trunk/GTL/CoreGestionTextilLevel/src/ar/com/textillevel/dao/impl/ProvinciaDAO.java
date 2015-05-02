package ar.com.textillevel.dao.impl;

import javax.ejb.Stateless;

import ar.clarin.fwjava.dao.impl.GenericDAO;
import ar.com.textillevel.dao.api.local.ProvinciaDAOLocal;
import ar.com.textillevel.entidades.gente.Provincia;

@Stateless
public class ProvinciaDAO extends GenericDAO<Provincia, Integer> implements ProvinciaDAOLocal{

}
