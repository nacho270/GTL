package ar.com.textillevel.modulos.personal.dao.impl;

import javax.ejb.Stateless;

import ar.clarin.fwjava.dao.impl.GenericDAO;
import ar.com.textillevel.modulos.personal.dao.api.NacionalidadDAOLocal;
import ar.com.textillevel.modulos.personal.entidades.legajos.Nacionalidad;

@Stateless
public class NacionalidadDAO extends GenericDAO<Nacionalidad, Integer> implements NacionalidadDAOLocal{

}
