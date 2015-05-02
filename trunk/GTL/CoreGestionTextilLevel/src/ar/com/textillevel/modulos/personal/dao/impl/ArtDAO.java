package ar.com.textillevel.modulos.personal.dao.impl;

import javax.ejb.Stateless;

import ar.clarin.fwjava.dao.impl.GenericDAO;
import ar.com.textillevel.modulos.personal.dao.api.ArtDAOLocal;
import ar.com.textillevel.modulos.personal.entidades.commons.Art;

@Stateless
public class ArtDAO extends GenericDAO<Art, Integer> implements ArtDAOLocal{

}
